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
                    <input:inputField label="Šifra *" name="id" disabled="true"/>
                </c:when>
            </c:choose>
            <c:choose>
                <c:when test="${action == 'create'}">
                    <input:inputField label="Matični broj *" name="companyIDNumber" autofocus="true"/>
                </c:when>
                <c:otherwise>
                    <input:inputField label="Matični broj *" name="companyIDNumber" disabled="true"/>
                </c:otherwise>
            </c:choose>
            <input:inputField label="Naziv *" name="name"/>
        </div>
        <div class="col-lg-3">
            <input:inputField name="country" label="Država"/>
            <input:inputField name="place" label="Mesto"/>
            <spring:bind path="township.code">
                <div class="form-group">
                    <label for="township">Opština</label>
                    <form:select path="township" id="township" class="form-control" itemLabel="township" >
                        <form:option value="${item.township.code}">${item.township.name}</form:option>
                        <form:options items="${townships}" itemLabel="name" itemValue="code"/>
                    </form:select>
                </div>
            </spring:bind>
            <input:inputField name="street" label="Ulica i broj"/>
            <input:inputField name="postCode" label="Poštanski Broj"/>
            <input:inputField label="Telefon" name="phone"/>
            <input:inputField label="Fax" name="fax"/>
            <input:inputField label="email" name="EMail"/>
        </div>
        <div class="col-lg-3">
            <input:inputField label="PIB" name="TIN"/>
            <input:inputField label="Šifra delatnosti" name="businessActivityCode"/>
            <spring:bind path="bank.id">
                <div class="form-group">
                    <label for="bank">Banka</label>
                    <form:select path="bankCreditor" id="bank" class="form-control" itemLabel="bank">
                        <form:option value="${item.bank.id}">${item.bank.name}</form:option>
                        <form:options items="${banks}" itemLabel="name" itemValue="id"/>
                    </form:select>
                </div>
            </spring:bind>
            <input:inputField label="Tekući račun" name="bankAccount"/>
            <input:inputField label="Osnovni kapital" name="initialCapital"/>
            <div class="form-group">
                <label for="status">Status</label>
                <form:select path="status" id="status" class="form-control" itemLabel="status">
                    <form:option value="${item.status}">${item.status.description}</form:option>
                    <form:options items="${statuses}" itemLabel="description"/>
                </form:select>
            </div>
            <input:inputField name="registrationNumber" label="Registracioni broj"/>
            <input:inputField name="logo" label="Logo"/>
        </div>
        <form:hidden path="version"/>
    </div>
    <div class="form-group">
        <a class="btn btn-primary" href="/masterdata/client/0">Povratak</a>
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
