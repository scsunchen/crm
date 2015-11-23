<%-- 
    Document   : terms-navigation
    Created on : Jun 16, 2015, 8:59:02 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
            ${exception.message}
    </div>
</c:if>
<a class="btn btn-default"
   href="${pageContext.request.contextPath}/terms/${page}/${terms.id}/update.html">
    <spring:message code="BusinessPartnerTerms.Button.Header"/></a>
<a class="btn btn-default"
   href="${pageContext.request.contextPath}/terms/${page}/0/${terms.id}/details.html">
    <spring:message code="BusinessPartnerTerms.Button.Items"/></a>
<a href="${pageContext.request.contextPath}/terms/${page}/read-page.html" class="btn btn-default">
    <span class="glyphicon glyphicon-backward"></span> <spring:message code="BusinessPartnerTerms.Button.Back"/></a>
