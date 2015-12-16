<%-- 
    Document   : desc-grid
    Created on : Jul 18, 2015, 6:58:43 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>
<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<form:form modelAttribute="desc" method="post">
    <i:textField label="Desc.Label.Name" name="name" autofocus="true" />
    <form:hidden path="version" />    
    <div class="form-group">
        <a href="${pageContext.request.contextPath}/desc/${page}" class="btn btn-default" >
            <span class="glyphicon glyphicon-backward"></span> <spring:message code="Desc.Button.Back" />
        </a>
        <button type="submit" class="btn btn-primary" >
            <c:choose>
                <c:when test="${action == 'create'}">
                    <spring:message code="Desc.Button.Create" />
                </c:when>
                <c:otherwise>
                    <spring:message code="Desc.Button.Update" />
                </c:otherwise>
            </c:choose>
        </button>
    </div>
</form:form>
