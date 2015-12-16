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
<form:form modelAttribute="account" method="post">
    <c:choose>
        <c:when test="${action == 'create'}">
            <i:textField label="Account.Label.Id" name="number" autofocus="true"/>
        </c:when>
        <c:otherwise>
            <i:textField label="Account.Label.Id" name="number" disabled="true" />
        </c:otherwise>
    </c:choose>
    <i:textField label="Account.Label.Name" name="name" />
    <div class="form-group">
        <label for="type"><spring:message code="Account.Label.Determination" /></label>
        <ul style="list-style-type: none;">            
            <form:radiobuttons path="determination" items="${accountDeterminations}" 
                               itemLabel="description" element="li"/>
        </ul>
    </div>
    <div class="form-group">
        <label for="type"><spring:message code="Account.Label.Type" /></label>        
        <ul style="list-style-type: none;">            
        <form:radiobuttons path="type" items="${accountTypes}" 
                           element="li"
                           itemLabel="description" />
        </ul>
    </div>
    <form:hidden path="version" />
    <div class="form-group">
        <a href="${pageContext.request.contextPath}/account/${page}" class="btn btn-default" >
            <span class="glyphicon glyphicon-backward"></span> <spring:message code="Account.Button.Back" />
        </a>
        <button type="submit" class="btn btn-primary" >
            <c:choose>
                <c:when test="${action == 'create'}">
                    <spring:message code="Account.Button.Create" />
                </c:when>
                <c:otherwise>
                    <spring:message code="Account.Button.Update" />
                </c:otherwise>
            </c:choose>
        </button>
    </div>
</form:form>
