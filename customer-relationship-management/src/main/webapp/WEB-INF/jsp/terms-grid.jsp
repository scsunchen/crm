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
    <fieldset class="col-lg-12">
        <div class="form-group">
            <c:choose>
            <c:when test="${action == 'create'}">
                <input:inputField label="Šifra *" name="id" autofocus="true"/>
            </c:when>
            <c:otherwise>
                <input:inputField label="Šifra *" name="id" disabled="true"/>
            </c:otherwise>
            </c:choose>
            <spring:bind path="businessPartner.companyIdNumber">
            <div class="form-group">
                <label for="partner">Poslovni partner</label>
                <form:select path="businessPartner" id="partner" class="form-control" itemLabel="partner">
                    <form:option value="">&nbsp;</form:option>
                    <form:options items="${partners}" itemLabel="name" itemValue="companyIdNumber"/>
                </form:select>
            </div>
            </spring:bind>
                <input:inputField label="Važi od" name="dateFrom"/>
                <input:inputField label="Važi do" name="endDate"/>
                <input:inputField label="DPO" name="daysToPay"/>
                <input:inputField label="Rabat" name="rebate"/>
                <input:inputTextArea label="Napomena" name="remark"/>
                <form:hidden path="version"/>
    </fieldset>
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
