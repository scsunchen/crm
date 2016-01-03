<%-- 
    Document   : invoice-table
    Created on : May 30, 2015, 8:11:46 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<br/>
<br/>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th></th>
            <th><spring:message code="Invoice.Table.ClientName" /></th>
            <th><spring:message code="Invoice.Table.OrgUnitName" /></th>
            <th><spring:message code="Invoice.Table.Document" /></th>
            <th><spring:message code="Invoice.Table.Partner" /></th>
            <th><spring:message code="Invoice.Table.Status" /></th>
            <th><spring:message code="Invoice.Table.CreditDebitRelationDate" /></th>
            <th><spring:message code="Invoice.Table.InvoiceDate" /></th>
            <th><spring:message code="Invoice.Table.TotalAmount" /></th>
            <th><spring:message code="Invoice.Table.ReturnValue" /></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page" />
        <c:forEach var="invoice" items="${data}">
            <!-- Modal -->
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <a href="${pageContext.request.contextPath}/transactions/print-preview.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&page=${page}"
                           class="btn btn-primary"><span class="glyphicon glyphicon-print"></span>
                            <spring:message code="Common.Button.Print"></spring:message> </a>
                    </div>
                </td>
                <td><c:out value="${invoice.value.clientDesc}"/></td>
                <td><c:out value="${invoice.value.orgUnitDesc}"/></td>
                <td><c:out value="${invoice.value.document}"/></td>
                <td><c:out value="${invoice.value.partnerName}"/></td>
                <td><c:out value="${invoice.value.proForma.description}"/></td>
                <td><spring:eval expression="invoice.value.creditRelationDate" /></td>
                <td><spring:eval expression="invoice.value.invoiceDate" /></td>
                <td><spring:eval expression="invoice.value.totalAmount"/> </td>
                <td><spring:eval expression="invoice.value.returnValue"/></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">
        <spring:message code="Invoice.Table.Page" />
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}" /></c:if>">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="Invoice.Table.PrevPage" />
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}" />
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>" >
                <span class="glyphicon glyphicon-forward"></span>
                <spring:message code="Invoice.Table.NextPage" />
            </a>
        </li>
    </ul>
</nav>