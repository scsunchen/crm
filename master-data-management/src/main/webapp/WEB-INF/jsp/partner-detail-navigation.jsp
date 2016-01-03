<%-- 
    Document   : invoice-master-toolbar
    Created on : Jun 16, 2015, 8:59:02 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:if test="${exception != null}">
  <div class="alert alert-warning">
    <a href="#" class="close" data-dismiss="alert">&times;</a>
      ${exception.message}
  </div>
</c:if>
<!--
<a href="${pageContext.request.contextPath}/partner/read-subpartners.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${param['page']}"
   class="btn btn-default">
    <span class="glyphicon glyphicon-backward"></span>
    <spring:message code="Common.Button.Back"/></a>
-->
<a class="btn btn-default"
   href="${pageContext.request.contextPath}/partner/read-contactsdetals-page.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&pointOfSaleId=${item.id}&page=${param['page']}"
   <c:if test="${(action == 'create')}">disabled</c:if>>
  <spring:message code="BusinessPartnerDetails.Button.Contacts"/></a>
<a class="btn btn-default"
   href="${pageContext.request.contextPath}/partner/read-deviceholderdetails-page.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&pointOfSaleId=${item.id}&page=${param['page']}"
   <c:if test="${(action == 'create')}">disabled</c:if>>
    <spring:message code="BusinessPartnerDetails.Button.Devices"/></a>

<%--
<a class="btn btn-default" href="${pageContext.request.contextPath}/invoice/details.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&page=${page}&itemsPage=0"><spring:message code="Invoice.Button.Detals"/></a>
<a class="btn btn-default" href="${pageContext.request.contextPath}/invoice/record.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&pageNumber=${page}" <c:if test="${invoice.recorded == true}">disabled</c:if> >
    <spring:message code="Invoice.Button.Record" /></a>
--%>

</a>
