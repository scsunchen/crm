<%--
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<nav class="navbar navbar-default">
  </br>
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="btn btn-default"
         in-transactions-per-period.html?merchantId=&id=&page=0
         href="${pageContext.request.contextPath}/transactions/in-transactions-per-period.html?merchantId=&id=${param['id']}&page=${param['page']}">
        <span class="glyphicon glyphicon-backward"></span>
        <spring:message code="Invoicing.Button.Back"/></a></div>
    <div class="collapse navbar-collapse" id="bs-total-navbar-collapse-6"><p class="navbar-text navbar-right">
      <spring:message code="Invoicing.Amount.Total"/><spring:eval expression="totalAmount"/></p></div>
  </div>
</nav>
<div class="table-responsive">
  <table class="table table-striped" data-sort-name="item.distributorName">
    <thead>
    <tr>
      <th></th>
      <th>Merchant</th>
      <th>POS</th>
      <th>Iznos</th>
      <th>Distributor</th>
    </tr>
    </thead>
    <tbody>
    <c:set var="count" value="0" scope="page"/>
    <c:forEach var="item" items="${data}">
      <!-- Modal -->
      <tr>
        <td>
          <div class="btn-group btn-group-sm" role="group">
            <a href="${pageContext.request.contextPath}/transactions/in-transactions-per-article.html?posId=${item.posId}&posName=${item.posName}&merchantId=${param['merchantId']}&merchantName=${param['merchantName']}&invoicingDate=${param['invoicingDate']}&page=0"
               class="btn btn-primary"><span
                    class="glyphicon glyphicon-search"></span>
              <spring:message code="Intable.Button.ViewPerArticle"/></a>
          </div>
        </td>
        <td><c:out value="${item.merchantName}"/></td>
        <td><c:out value="${item.posName}"/></td>
        <td><spring:eval expression="item.amount"/></td>
        <td><c:out value="${item.distributorName}"/></td>

      </tr>
      <c:set var="count" value="${count + 1}" scope="page"/>
    </c:forEach>
    </tbody>
  </table>
</div>
<nav>
  <ul class="pager pull-left">
    <button data-toggle="modal" data-target="#dialogGenInvoices" class="btn btn-primary">
      <span class="glyphicon glyphicon-plus"></span><spring:message code="Invoicing.Button.GenInvoice"/>
    </button>
    <br/>
  </ul>
  <ul class="pager pull-right">
    Strana
    <li class="<c:if test="${page == 0}"><c:out value="disabled"/></c:if>">
      <a href="<c:if test="${page > 0}"><c:out value="${pageContext.request.contextPath}/transactions/in-transactions-per-pos-per-period.html?merchantId=${param['merchantId']}&id=${param['id']}&page=${page - 1}"/></c:if>">
        <span class="glyphicon glyphicon-backward"></span> Prethodna
      </a>
    </li>
    <c:out value="${page+1} od ${numberOfPages+1}"/>
    <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">

      <a href="<c:if test="${page < numberOfPages}"><c:out value="${pageContext.request.contextPath}/transactions/in-transactions-per-pos-per-period.html?merchantId=${param['merchantId']}&id=${param['id']}&page=${page + 1}"/></c:if>">
        <span class="glyphicon glyphicon-forward"></span> Naredna
      </a>
    </li>
  </ul>
</nav>


<script type="text/javascript">

  $('#date').datepicker({});

  $('#distributorName').typeahead({
    hint: false,
    highlight: true,
    minLength: 1,
    limit: 1000
  }, {
    display: 'name',
    source: new Bloodhound({
      datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: '${pageContext.request.contextPath}/masterdata/read-distributor/%QUERY',
        wildcard: '%QUERY'
      }
    })
  });
  $('#distributorName').bind('typeahead:selected', function (obj, datum, name) {
    $('#distributorIdHidden').val(datum['id']);
  });
</script>

<script type="text/javascript">
  $('#invoicingDistributorName').typeahead({
    hint: false,
    highlight: true,
    minLength: 1,
    limit: 1000
  }, {
    display: 'name',
    source: new Bloodhound({
      datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: '${pageContext.request.contextPath}/masterdata/read-distributor/%QUERY',
        wildcard: '%QUERY'
      }
    })
  });
  $('#invoicingDistributorName').bind('typeahead:selected', function (obj, datum, name) {
    $('#invoicingDistributorIdHidden').val(datum['id']);
  });
</script>
