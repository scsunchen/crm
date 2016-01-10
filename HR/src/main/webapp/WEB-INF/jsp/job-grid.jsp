<%--
  Created by IntelliJ IDEA.
  User: NikolaB
  Date: 6/7/2015
  Time: 10:06 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="item" method="post" cssClass="generic-container">
    <div class="form-group">
        <div class="col-lg-3">
            <c:choose>
                <c:when test="${action == 'create'}">
                    <input:inputField label="Job.Table.Id" name="id" disabled="true"/>
                </c:when>
            </c:choose>
        </div>
        <div class="col-lg-9">
        <input:inputField label="Job.Table.Name" name="name"/>
        </div>
        <input:inputTextArea label="Job.Table.Description" name="description"/>
        <form:hidden path="version"/>
    </div>
    <div class="form-group btn-group-sm">
        <a class="btn btn-default" href="/HR/job/read-page.html?page=0">
            <span class="glyphicon glyphicon-backward"></span>
            <spring:message code="Common.Button.Back"></spring:message> </a>
        <button type="submit" class="btn btn-primary">
            <c:choose>
                <c:when test="${action == 'create'}">
                    <c:out value="Kreiraj"/>
                </c:when>
                <c:otherwise>
                    <c:out value="Promeni"/>
                </c:otherwise>
            </c:choose>
        </button>
    </div>
</form:form>

