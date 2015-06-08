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
                <input:inputField label="Šifra *" name="companyIdNumber" autofocus="true"/>
            </c:when>
            <c:otherwise>
                <input:inputField label="Šifra *" name="companyIdNumber" disabled="true"/>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="form-group">
        <div class="col-md-6">
            <input:inputField label="Naziv *" name="name"/>
        </div>
        <div class="col-md-6">
            <input:inputField label="Dodatni Naziv" name="name1"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-3">
            <input:inputField name="address.country" label="Država"/>
        </div>
        <div class="col-md-3">
            <input:inputField name="address.place" label="Mesto"/>
        </div>
        <div class="col-md-3">
            <input:inputField name="address.street" label="Ulica i broj"/>
        </div>
        <div class="col-md-3">
            <input:inputField name="address.postCode" label="Poštanski Broj"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-3">
            <input:inputField label="Telefon" name="phone"/>
        </div>
        <div class="col-md-3">
            <input:inputField label="Fax" name="fax"/>
        </div>
        <div class="col-md-6">
            <input:inputField label="email" name="EMail"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-3">
            <input:inputField label="PIB" name="TIN"/>
        </div>
        <div class="col-md-3">
            <input:inputField label="Šifra delatnosti" name="activityCode"/>
        </div>
        <div class="col-md-4">
            <input:inputField label="Tekući račun" name="currentAccount"/>
        </div>
        <div class="col-md-2 checkbox">
            <label><form:checkbox path="VAT" id="VAT" class="checkbox"/>PDV</label>
        </div>
    </div>


    <div class="form-group">
        <div class="col-md-4">
            <spring:bind path="currencyDesignation">
                <div class="form-group">
                    <label for="currencyDesignation">Valuta</label>
                    <form:select path="currencyDesignation" id="currencyDesignation" class="form-control"
                                 items="${currencyDesignation}"
                                 itemLabel="description"/>
                </div>
            </spring:bind>
        </div>
        <div class="col-md-4">
            <input:inputField label="Rabat" name="rebate"/>
        </div>
        <div class="col-md-4">
            <input:inputField label="Beskamatni period" name="interestFreeDays"/>
        </div>
    </div>
    <form:hidden path="version"/>

    <div class="form-group col-md-1">
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
