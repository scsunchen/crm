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
<%@ taglib prefix="sprong" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form:form method="post" modelAttribute="transactionDTO"
           action="${pageContext.request.contextPath}/invoicing/review-invoices.html">
    <div class="modal fade" id="dialogGenInvoices" tabindex="-1" role="dialog" action="/invoicing/review-invoices.html">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <spring:bind path="invoicingGenDate">
                        <div class="form-group row">
                            <div class="col-lg-6">
                                <form:input id="date" path="invoicingGenDate" type="text"
                                            class="form-control" placeholder="dd.mm.yyyy."/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                            </div>
                        </div>
                    </spring:bind>
                    <div class="form-group input-group">
                        <label for="invoicingDistributorName">Distributor</label>
                        <form:input id="invoicingDistributorName" class="typeahead form-control" type="text"
                                    path="invoicingDistributorName" style="margin-bottom:  15px;"/>
                        <form:hidden id="invoicingDistributorIdHidden" path="invoicingDistributorId"/>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-default" data-dismiss="modal" name="genInvoices"><spring:message
                                code="Invoicing.Button.Cancel"/></button>
                        <button class="btn btn-primary" type="submit"><spring:message
                                code="Invoicing.Button.Confirm"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${showDialog}">
        <script type="text/javascript">
            $('#dialogGenInvoices').modal('show');
        </script>
    </c:if>
</form:form>

<nav class="navbar navbar-default">
    </br>
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="btn btn-default"
               href="${pageContext.request.contextPath}/transactions/in-transactions-per-pos.html?posId=${item.posId}&posName=${item.posName}&merchantId=${param['merchantId']}&merchantName=${param['merchantName']}&invoicingDate=${param['invoicingDate']}&page=0">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="Invoicing.Button.Back"/></a></div>
        <div class="collapse navbar-collapse" id="bs-total-navbar-collapse-6">
            <p class="navbar-text navbar-right"><spring:message code="Review.Table.POS"></spring:message> <strong><c:out value="${param['posId']}  ${param['posName']}"></c:out></strong></p>
        </div>
    </div>
</nav>


<div class="table-responsive">
    <table class="table table-striped" data-sort-name="item.distributorName">
        <thead>
        <tr>
            <th>
                <button data-toggle="modal" data-target="#dialogGenInvoices" class="btn btn-primary">
                    <span class="glyphicon glyphicon-plus"></span><spring:message code="Invoicing.Button.GenInvoice"/>
                </button>
            </th>
            <th><spring:message code="Review.Table.Article"></spring:message> </th>
            <th><spring:message code="Review.Table.Amount"></spring:message> </th>
            <th><spring:message code="Invoice.Table.Distributor"></spring:message> </th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="item" items="${data}">
            <!-- Modal -->
            <tr>
                <td></td>
                <td><c:out value="${item.posName}"/></td>
                <td><c:out value="${item.serviceDescription}"/></td>
                <td><spring:eval expression="item.amount"/></td>
                <td><c:out value="${item.distributorName}"/></td>

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
            <a href="<c:if test="${page > 0}"><c:out value="${pageContext.request.contextPath}/transactions/in-transactions-per-pos.html?&merchantId=${param['merchantId']}&invoicingDate=${param['invoicingDate']}&page=${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> <spring:message code="Common.Button.PreviousPage"></spring:message>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">

            <a href="<c:if test="${page < numberOfPages}"><c:out value="${pageContext.request.contextPath}/transactions/in-transactions-per-pos.html?&merchantId=${param['merchantId']}&invoicingDate=${param['invoicingDate']}&page=${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> <spring:message code="Common.Button.NextPage"></spring:message>
            </a>
        </li>
    </ul>
    <table class="table">
        <tr class=" col-lg-3">
            <td><spring:message code="Common.Summary.SumPerPage"></spring:message></td>
            <td><strong><fmt:formatNumber type="currency"
                                          maxFractionDigits="2"
                                          value="${sumAmountPerPage}"/></strong></td>
        </tr>
        <tr class=" col-lg-3">
            <td><spring:message code="Common.Summary.SumPerQuery"></spring:message></td>
            <td><strong><fmt:formatNumber type="currency"
                                          maxFractionDigits="2"
                                          value="${sumAmount}"/></strong></td>
        </tr>
    </table>
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
