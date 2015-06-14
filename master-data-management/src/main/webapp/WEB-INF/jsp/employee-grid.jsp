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
    <div class="col-lg-6">
        <c:choose>
            <c:when test="${action == 'create'}">
                <input:inputField label="Šifra *" name="id" autofocus="true"/>
            </c:when>
            <c:otherwise>
                <input:inputField label="Šifra" name="id" disabled="true"/>
            </c:otherwise>
        </c:choose>
        <input:inputField label="Ime *" name="name"/>
        <input:inputField label="Srednje ime" name="middleName"/>
        <input:inputField label="Prezime" name="lastName"/>
        <input:inputField label="Datum rođenja" name="dateofBirth"/>
        <input:inputField label="Telefon" name="phone"/>
        <input:inputField label="Email" name="email"/>
        <input:inputField label="Fotografija" name="picture"/>
        <input:inputField label="Datum rođenja" name="dateofBirth"/>
        <spring:bind path="orgUnit.id">
            <div class="form-group">
                <label for="orgunit">Opština</label>
                <form:select path="orgUnit" id="orgunit" class="form-control" itemLabel="orgUnit">
                    <form:option value="">&nbsp;</form:option>
                    <form:options items="${orgunits}" itemLabel="name" itemValue="id"/>
                </form:select>
            </div>
        </spring:bind>
        <input:inputField label="Datum zapošljenja" name="hireDate"/>
        <input:inputField label="Datum odlaska" name="endDate"/>
        <spring:bind path="job.id">
            <div class="form-group">
                <label for="job">Radno mesto</label>
                <form:select path="job" id="job" class="form-control" itemLabel="job">
                    <form:option value="">&nbsp;</form:option>
                    <form:options items="${jobs}" itemLabel="name" itemValue="id"/>
                </form:select>
            </div>
        </spring:bind>
        <input:inputField label="Država" name="address.country"/>
        <input:inputField label="Mesto" name="address.place"/>
        <input:inputField label="Ulica i broj" name="address.street"/>
        <input:inputField label="Poštanski broj" name="address.postCode"/>
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

