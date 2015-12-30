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
        <div class="col-lg-6">
            <input:inputField label="Client.Table.Id" name="id" disabled="true"/>
            <c:choose>
                <c:when test="${action == 'create'}">
                    <input:inputField label="Client.Table.CompanyIDNumber" name="companyIDNumber" autofocus="true"/>
                </c:when>
                <c:otherwise>
                    <input:inputField label="Client.Table.CompanyIDNumber" name="companyIDNumber" disabled="true"/>
                </c:otherwise>
            </c:choose>
            <input:inputField label="Client.Table.Name" name="name"/>
        </div>
        <div class="col-lg-3">
            <input:inputField name="country" label="Client.Table.State"/>
            <input:inputField name="place" label="Client.Table.Place"/>
            <spring:bind path="townshipCode">
                <div class="form-group">
                    <label for="township"><spring:message code="Client.Table.Township"/></label>
                    <form:select path="townshipCode" id="township" class="form-control" itemLabel="township">
                        <form:option value="${townshipCode}">${townshipName}</form:option>
                        <form:options items="${townships}" itemLabel="name" itemValue="code"/>
                    </form:select>
                </div>
            </spring:bind>
            <input:inputField name="street" label="Client.Table.Street"/>
            <input:inputField name="postCode" label="Client.Table.Zip"/>
            <input:inputField name="phone" label="Client.Table.Phone"/>
            <input:inputField name="fax" label="Client.Table.Fax"/>
            <input:inputField name="EMail" label="Client.Table.Email"/>
        </div>
        <div class="col-lg-3">
            <input:inputField label="Client.Table.TIN" name="TIN"/>
            <input:inputField label="Client.Table.ActivityCode" name="businessActivityCode"/>
            <spring:bind path="bankId">
                <div class="form-group">
                    <label for="bank"><spring:message code="Client.Table.Bank"></spring:message> </label>
                    <form:select path="bankId" id="bank" class="form-control" itemLabel="bank">
                        <form:option value="${bankId}">${bankName}</form:option>
                        <form:options items="${banks}" itemLabel="name" itemValue="id"/>
                    </form:select>
                </div>
            </spring:bind>
            <input:inputField label="Client.Table.Account" name="bankAccount"/>
            <input:inputField label="Client.Table.InitialCapital" name="initialCapital"/>
            <div class="form-group">
                <label for="status"><spring:message code="Client.Table.Status"></spring:message> </label>
                <form:select path="status" id="status" class="form-control" itemLabel="status">
                    <form:option value="${status}">${statusDescription}</form:option>
                    <form:options items="${statuses}" itemLabel="description"/>
                </form:select>
            </div>
            <div class="form-group">
                <label for="invoicingType"><spring:message code="Client.Table.InvoicingType"/> </label>
                <form:select path="invoicingType" id="invoicingType" class="form-control" itemLabel="status">
                    <form:option value="${invoicingType}">${inovicingTypeDescription}</form:option>
                    <form:options items="${invoicingTypes}" itemLabel="description"/>
                </form:select>
            </div>
            <input:inputField name="registrationNumber" label="Client.Table.RegNo"/>
            <input:inputField name="logo" label="Client.Table.Logo"/>
        </div>
        <form:hidden path="version"/>
    </div>
    <div class="form-group">
        <a class="btn btn-default" href="/masterdata/client/0">
            <span class="glyphicon glyphicon-backward"></span>
            <spring:message code="Client.Table.BackButton"></spring:message> </a>
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
