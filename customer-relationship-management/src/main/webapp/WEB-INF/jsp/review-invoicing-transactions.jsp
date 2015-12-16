<%-- 
    Document   : invoice-table
    Created on : May 30, 2015, 8:11:46 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>
<!-- Search invoices by document, invoiceDate range, business partner -->

<form:form action="${pageContext.request.contextPath}/transactions/review-invoicing-transactions.html"
           modelAttribute="invoicingTransactionDTO"
           method="GET">

    <nav class="navbar navbar-default">
        <br/>
        <div class="form-group col-lg-4">
            <form:select path="id" id="status" class="form-control" itemLabel="status">
                <form:options items="${invoicingTransactions}" itemLabel="displayPeriod" itemValue="id"/>
            </form:select>
        </div>
        <div class="form-group col-lg-4">
            <label for="partner" class="sr-only"><spring:message code="FindInvoice.Label.Partner"
                                                                 var="partnerLabel"/></label>
            <form:input id="partner" class="form-control" type="text" path="partnerName"
                        placeholder="Partner..."/>
        </div>

        <div class="col-lg-4">
            <button type="submit" class="btn btn-primary"><span class=" glyphicon glyphicon-search"></span></button>
        </div>
    </nav>
</form:form>


<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th></th>
            <th><spring:message code="Invoice.Table.ClientName"/></th>
            <th><spring:message code="Invoice.Table.OrgUnitName"/></th>
            <th><spring:message code="Invoice.Table.Document"/></th>
            <th><spring:message code="Invoice.Table.Partner"/></th>
            <th><spring:message code="Invoice.Table.Status"/></th>
            <th><spring:message code="Invoice.Table.CreditDebitRelationDate"/></th>
            <th><spring:message code="Invoice.Table.InvoiceDate"/></th>
            <th><spring:message code="Invoice.Table.Recorded"/></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="invoice" items="${data}">
            <!-- Modal -->

            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">

                        <a href="${pageContext.request.contextPath}/invoice/update.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&page=${page}"
                           class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> pregled</a>
                    </div>
                </td>
                <td><c:out value="${invoice.clientDesc}"/></td>
                <td><c:out value="${invoice.orgUnitDesc}"/></td>
                <td><c:out value="${invoice.document}"/></td>
                <td><c:out value="${invoice.partnerName}"/></td>
                <td><c:out value="${invoice.proForma.description}"/></td>
                <td><spring:eval expression="invoice.creditRelationDate"/></td>
                <td><spring:eval expression="invoice.invoiceDate"/></td>
                <td>
                    <c:choose>
                        <c:when test="${invoice.recorded}">
                            <spring:message code="Invoice.Table.Recorded"/>
                        </c:when>
                        <c:otherwise>
                            <spring:message code="Invoice.Table.NotRecorded"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">
        <spring:message code="Invoice.Table.Page"/>
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}">
                   <c:out value="${pageContext.request.contextPath}/invoice/read-page.html?document=${param['document']}&partnerName=${param['partnerName']}&partnerId=${param['partnerId']}&dateFrom=${param['dateFrom']}&dateTo=${param['dateTo']}&page=${page-1}" /></c:if>">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="Invoice.Table.PrevPage"/>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}">
                   <c:out value="${pageContext.request.contextPath}/invoice/read-page.html?document=${param['document']}&partnerName=${param['partnerName']}&partnerId=${param['partnerId']}&dateFrom=${param['dateFrom']}&dateTo=${param['dateTo']}&page=${page+1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span>
                <spring:message code="Invoice.Table.NextPage"/>
            </a>
        </li>
    </ul>
</nav>
<script type="text/javascript">
    $('#dateFrom').datepicker({});
    $('#dateTo').datepicker({});

</script>