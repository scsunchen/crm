<%-- 
    Document   : invoice-master-toolbar
    Created on : Jun 16, 2015, 8:59:02 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<a class="btn btn-default" href="${pageContext.request.contextPath}/invoice/update.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&page=${page}"><spring:message code="Invoice.Button.Header"/></a>
<a class="btn btn-default" href="${pageContext.request.contextPath}/invoice/details.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&page=${page}&itemsPage=0"><spring:message code="Invoice.Button.Detals"/></a>
<a class="btn btn-default" href="${pageContext.request.contextPath}/invoice/record.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&pageNumber=${page}" <c:if test="${invoice.recorded == true}">disabled</c:if> >
    <spring:message code="Invoice.Button.Record" />
</a>
<a href=
   "${pageContext.request.contextPath}/invoice/print-preview.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}" 
   class="btn btn-default" 
   target="_blank"><span class="glyphicon glyphicon-search" ></span> <spring:message code="Invoice.Button.PrintPreview" /></a>
<a href="${pageContext.request.contextPath}/invoice/read-page.html?document=&partnerName=&dateFrom=&dateTo=&page=${page}" 
   class="btn btn-default" >
    <span class="glyphicon glyphicon-backward" ></span>
    <spring:message code="Invoice.Button.Back" />
</a>
