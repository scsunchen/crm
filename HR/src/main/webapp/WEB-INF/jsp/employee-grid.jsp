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
                <input:inputField label="Šifra *" name="id" autofocus="true" disabled="true"/>
            </c:when>
            <c:otherwise>
                <form:hidden path="id"/>
            </c:otherwise>
        </c:choose>
        <input:inputField label="Ime *" name="name"/>
        <input:inputField label="Srednje ime" name="middleName"/>
        <input:inputField label="Prezime *" name="lastName"/>
        <input:inputDate  label="Datum rođenja" name="dateOfBirth" placeholder="dd.mm.yyyy."/>
        <input:inputField label="Telefon" name="phone"/>
        <input:inputField label="Email" name="email"/>
        <input:inputField label="Fotografija" name="picture"/>
        <spring:bind path="orgUnitId">
            <div class="form-group">
                <label for="orgunit">Organizaciona jedinica</label>
                <form:select path="orgUnitId" id="orgunit" class="form-control" itemLabel="orgUnit">
                    <form:option value="${item.orgUnitId}">${item.orgUnitName}</form:option>
                    <form:options items="${orgUnits}" itemLabel="name" itemValue="id"/>
                </form:select>
            </div>
        </spring:bind>
        <input:inputDate label="Datum zapošljenja" name="hireDate" placeholder="dd.mm.yyyy."/>
        <input:inputDate label="Datum odlaska" name="endDate" placeholder="dd.mm.yyyy."/>
        <spring:bind path="jobId">
            <div class="form-group">
                <label for="job">Radno mesto</label>
                <form:select path="jobId" id="job" class="form-control" itemLabel="job">
                    <form:option value="${item.jobId}">${item.jobName}</form:option>
                    <form:options items="${jobs}" itemLabel="name" itemValue="id"/>
                </form:select>
            </div>
        </spring:bind>
        <input:inputField label="Država" name="country"/>
        <input:inputField label="Mesto" name="place"/>
        <input:inputField label="Poštanski broj" name="postCode"/>
        <input:inputField label="Ulica i broj" name="street"/>
        <form:hidden path="version"/>
    </div>
    <div class="form-group">

        <a class="btn btn-primary" href="/HR/employee/0">Povratak</a>
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

