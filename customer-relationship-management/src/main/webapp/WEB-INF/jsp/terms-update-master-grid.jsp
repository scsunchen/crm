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
                <input:inputField label="BusinessPartnerTerms.Table.Id" name="id" autofocus="true" disabled="true"/>
            </c:when>
            <c:otherwise>
                <form:hidden path="id"/>
            </c:otherwise>
            </c:choose>
            <spring:bind path="businessPartner.companyIdNumber">
            <div class="form-group col-md-6">
                <label for="partner"><spring:message code="BusinessPartnerTerms.Table.BusinessPartnerName"/></label>
                <form:select path="transientPartnerId" id="partner" class="form-control" itemLabel="partner">
                    <form:option value="${item.businessPartner.id}">${item.businessPartner.name}</form:option>
                    <form:options items="${partners}" itemLabel="name" itemValue="id"/>
                </form:select>
            </div>
            </spring:bind>

            <div class="form-group col-md-6">
                <label for="status">Status</label>
                <form:select path="status" id="status" class="form-control" itemLabel="status">
                    <form:option value="${item.status}">${item.status.description}</form:option>
                    <form:options items="${statuses}" itemLabel="description"/>
                </form:select>
            </div>
            <div class="col-md-6">
                <input:inputDate label="BusinessPartnerTerms.Table.DateFrom" name="dateFrom"/>
                <input:inputDate label="BusinessPartnerTerms.Table.EndDate" name="endDate"/>
            </div>
            <div class="col-md-6">
                <input:inputField label="BusinessPartnerTerms.Table.DPO" name="daysToPay"/>
                <input:inputField label="BusinessPartnerTerms.Table.Rebate" name="rebate"/>
            </div>
                <input:inputTextArea label="BusinessPartnerTerms.Table.Remark" name="remark"/>
                <form:hidden path="version"/>
    </fieldset>
    <div class="form-group">
        <button type="submit" class="btn btn-primary">
            <c:choose>
                <c:when test="${action == 'create'}">
                    <spring:message code="BusinessPartnerTerms.Button.Create"/>
                </c:when>
                <c:otherwise>
                    <spring:message code="BusinessPartnerTerms.Button.Update"/>
                </c:otherwise>
            </c:choose>
        </button>
    </div>
</form:form>
