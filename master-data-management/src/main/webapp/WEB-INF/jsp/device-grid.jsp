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


<form:form modelAttribute="item" method="post">
    <div class="form-group">
        <c:choose>
            <c:when test="${action == 'create'}">
                <input:inputField label="Šifra *" name="id" autofocus="true"/>
            </c:when>
            <c:otherwise>
                <input:inputField label="Šifra" name="id" disabled="true"/>
            </c:otherwise>
        </c:choose>
        <input:inputField name="customCode" label="Korisnička šifra"/>
        <input:inputField name="article" label="Tip terminala"/>
        <input:inputField name="serialNumber" label="Serisjki Broj"/>
        <spring:bind path="status.id">
            <div class="form-group">
                <label for="status">Status terminala</label>
                <form:select path="status" id="status" class="form-control" itemLabel="status">
                    <form:option value="">&nbsp;</form:option>
                    <form:options items="${statuses}" itemLabel="name" itemValue="id"/>
                </form:select>
            </div>
        </spring:bind>
        <input:inputDate name="creationDate" label="Datum Kreiranja"/>
        <input:inputTime name="workingStartTime" label="Početak rada" />
        <input:inputTime name="workingEndTime" label="Kraj rada" />
        <input:inputField name="installedSoftwareVersion" label="Firmware verzija" />
        <form:hidden path="version"/>
    </div>
    <div class="form-group">
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
