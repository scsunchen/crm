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

<form:form class="navbar-form navbar-left" role="search" modelAttribute="transactionDTO" method="POST">
    <nav class="navbar navbar-default">
        <div class="row">
            <div class="container-fluid">
                <!-- Pretraživanje poslovnih partnera -->
                <div id="bs-example-navbar-collapse-1">
                    <input:inputDate name="requestTime" label="Datum do: " placeholder="dd.mm.yyyy."/>
                    <button type="submit" class="btn btn-default">Pretraga</button>
                </div>
                <!-- /.navbar-collapse -->
            </div>
            <!-- /.container-fluid -->
        </div>
    </nav>
</form:form>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Distributor</th>
            <th>Merchant</th>
            <th>POS</th>
            <th>Terminal</th>
            <th>Usluga</th>
            <th>Iznos za fakturisanje</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="item" items="${data}">
            <!-- Modal -->
            <tr>
                <td hidden><c:out value="${item.distributorId}"/></td>
                <td><c:out value="${item.distributorName}"/></td>
                <td hidden><c:out value="${item.merchantId}"/></td>
                <td><c:out value="${item.merchantName}"/></td>
                <td hidden><c:out value="${item.posId}"/></td>
                <td><c:out value="${item.posName}"/></td>
                <td hidden><c:out value="${item.treminalId}"/></td>
                <td><c:out value="${item.treminalName}"/></td>
                <td hidden><c:out value="${item.serviceId}"/></td>
                <td><c:out value="${item.serviceDescription}"/></td>
                <td><c:out value="${item.amount}"/></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>

    <ul class="pager pull-right">
        Strana
        <li class="<c:if test="${page == 0}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${pageContext.request.contextPath}/invoicing/-${transactionDTO.serviceProviderId}-${transactionDTO.pointOfSaleId}${transactionDTO.terminalId}-${transactionDTO.typeId}-${transactionDTO.distributorId}/${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> Prethodna
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${pageContext.request.contextPath}/invoicing/-${transactionDTO.serviceProviderId}-${transactionDTO.pointOfSaleId}${transactionDTO.terminalId}-${transactionDTO.typeId}-${transactionDTO.distributorId}/${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> Naredna
            </a>
        </li>
    </ul>
</nav>

<script type="text/javascript">
    $('#serviceProviderName').typeahead({
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
                url: '${pageContext.request.contextPath}/masterdat/read-serviceprovider/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#serviceProviderName').bind('typeahead:selected', function (obj, datum, name) {
        $('#serviceProviderIdHidden').val(datum['id']);
    });
</script>
<script type="text/javascript">
    $('#pointOfSaleName').typeahead({
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
                url: '${pageContext.request.contextPath}/masterdat/read-businesspartner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#pointOfSaleName').bind('typeahead:selected', function (obj, datum, name) {
        $('#pointOfSaleIdHidden').val(datum['id']);
    });
</script>
<script type="text/javascript">
    $('#terminalCustomCode').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'customCode',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/masterdat/read-terminal/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#terminalCustomCode').bind('typeahead:selected', function (obj, datum, name) {
        $('#terminalIdHidden').val(datum['id']);
    });
</script>
<script type="text/javascript">
    $('#typeDescription').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'description',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/masterdat/read-transactiontype/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#typeDescription').bind('typeahead:selected', function (obj, datum, name) {
        $('#typeIdHidden').val(datum['id']);
    });
</script>
<script type="text/javascript">
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
                url: '${pageContext.request.contextPath}/masterdat/read-distributor/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#distributorName').bind('typeahead:selected', function (obj, datum, name) {
        $('#distributorIdHidden').val(datum['id']);
    });
</script>