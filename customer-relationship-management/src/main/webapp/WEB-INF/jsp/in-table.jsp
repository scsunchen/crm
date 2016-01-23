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
<form:form role="search" modelAttribute="transactionDTO" method="GET"
           action="${pageContext.request.contextPath}/transactions/in-transactions.html">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <br/>
            <!-- Pretraživanje poslovnih partnera -->
            <div class="form-group">
                <spring:bind path="invoicingDate">

                    <div class="col-lg-4">
                        <form:input id="invoicingDate" path="invoicingDate" type="text"
                                    class="form-control" placeholder="dd.mm.yyyy."/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                    </div>

                </spring:bind>
                <div class="col-md-4">
                    <form:input id="merchantName" class="typeahead form-control" type="text"
                                path="merchantName" style="margin-bottom:  15px;" placeholder="Merchant..."/>
                    <form:hidden id="merchantIdHidden" path="merchantId"/>
                    <form:input id="type-hidden" type="hidden" path="page" value="0"/>
                </div>
                <button type="submit" class="btn btn-default">Pretraga</button>
                <!-- /.navbar-collapse -->
            </div>
        </div>
        <!-- /.container-fluid -->
    </nav>
</form:form>
<div class="table-responsive">
    <table class="table table-striped" data-sort-name="item.distributorName">
        <thead>
        <tr>
            <th>
                <button data-toggle="modal" data-target="#dialogGenInvoices" class="btn btn-primary">
                    <span class="glyphicon glyphicon-plus"></span><spring:message code="Invoicing.Button.GenInvoice"/>
                </button>
            </th>
            <th><spring:message code="Review.Table.Merchant"></spring:message> </th>
            <th><spring:message code="Review.Table.Amount"></spring:message> </th>
            <th><spring:message code="Invoice.Table.Distributor"></spring:message> </th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="item" items="${data}">
            <!-- Modal -->
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <a href="${pageContext.request.contextPath}/transactions/in-transactions-per-pos.html?merchantId=${item.merchantId}&merchantName=${item.merchantName}&invoicingDate=${param['invoicingDate']}&page=0"
                           class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span>
                            <spring:message code="Intable.Button.ViewPerPOS"/></a>
                    </div>
                </td>
                <td><c:out value="${item.merchantName}"/></td>
                <td><spring:eval expression="item.amount"/></td>
                <td><c:out value="${item.distributorName}"/></td>

            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>


    <ul class="pager pull-right ">
        Strana
        <li class="<c:if test="${page == 0}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${pageContext.request.contextPath}/transactions/in-transactions.html?merchantId=${param['merchantId']}&invoicingDate=${param['invoicingDate']}&page=${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> <spring:message code="Common.Button.PreviousPage"></spring:message>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">

            <a href="<c:if test="${page < numberOfPages}"><c:out value="${pageContext.request.contextPath}/transactions/in-transactions.html?merchantId=${param['merchantId']}&invoicingDate=${param['invoicingDate']}&page=${page + 1}"/></c:if>">
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
    $('#invoicingDate').datepicker({});


    $('#merchantName').typeahead({
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
                url: '${pageContext.request.contextPath}/masterdata/read-merchant/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#merchantName').bind('typeahead:selected', function (obj, datum, name) {
        $('#merchantIdHidden').val(datum['id']);
    });
</script>

</script>
