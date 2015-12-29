<%-- 
    Document   : item-grid
    Created on : May 30, 2015, 1:54:06 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>


<form:form modelAttribute="item" method="post" cssClass="generic-container">
    <div class="form-group">
        <c:choose>
            <c:when test="${action == 'create'}">
                <input:inputField label="ISO Kod" name="ISOCode"/>
            </c:when>
            <c:otherwise>
                <input:inputField label="ISO Kod" name="ISOCode" disabled="true"/>
            </c:otherwise>
        </c:choose>
        <input:inputField name="ISONumber" label="ISO Broj"/>
        <input:inputField name="currency" label="Naziv"/>
        <input:inputField name="state" label="Država"/>
        <form:hidden path="version"/>
    </div>
    <div class="form-group">
        <a class="btn btn-primary" href="/masterdata/currency/0">Povratak</a>
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
