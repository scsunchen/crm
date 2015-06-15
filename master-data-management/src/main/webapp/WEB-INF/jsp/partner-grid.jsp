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
            <div class="col-lg-6">
                <c:choose>
                    <c:when test="${action == 'create'}">
                        <input:inputField label="Matični broj *" name="companyIdNumber" autofocus="true"/>
                    </c:when>
                    <c:otherwise>
                        <input:inputField label="Matični broj *" name="companyIdNumber" disabled="true"/>
                    </c:otherwise>
                </c:choose>
                <input:inputField label="Naziv *" name="name"/>
                <input:inputField label="Dodatni Naziv" name="name1"/>
            </div>
            <div class="col-lg-3">
                <input:inputField name="address.country" label="Država"/>
                <input:inputField name="address.place" label="Mesto"/>
                <input:inputField name="address.street" label="Ulica i broj"/>
                <input:inputField name="address.postCode" label="Poštanski Broj"/>
                <input:inputField label="Telefon" name="phone"/>
                <input:inputField label="Fax" name="fax"/>
                <input:inputField label="email" name="EMail"/>
            </div>
            <div class="col-lg-3">
                <input:inputField label="PIB" name="TIN"/>
                <input:inputField label="Šifra delatnosti" name="activityCode"/>
                <input:inputField label="Tekući račun" name="currentAccount"/>
                <spring:bind path="currencyDesignation">
                    <div class="form-group">
                        <label for="currencyDesignation">Valuta</label>
                        <form:select path="currencyDesignation" id="currencyDesignation" class="form-control"
                                     items="${currencyDesignation}"
                                     itemLabel="description"/>
                    </div>
                </spring:bind>
                <input:inputField label="Rabat" name="rebate"/>
                <input:inputField label="Beskamatni period" name="interestFreeDays"/>
                <spring:bind path="parentBusinessPartner.companyIdNumber">
                    <div class="form-group">
                        <label for="parent">Nadređeni partner</label>
                        <form:select path="parentBusinessPartner" id="parent" class="form-control" itemLabel="parent">
                            <form:option value="">&nbsp;</form:option>
                            <form:options items="${parents}" itemLabel="name" itemValue="companyIdNumber"/>
                        </form:select>
                    </div>
                </spring:bind>
                <div class="checkbox">
                    <label><form:checkbox path="VAT" id="VAT" class="checkbox"/>PDV</label>
                </div>
            </div>
            <form:hidden path="version"/>
        </div>
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
