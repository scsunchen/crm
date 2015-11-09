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

<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<form:form modelAttribute="terms" method="post">
    <form:hidden path="id"/>
    <div class="form-group">
        <label for="partner"><spring:message code="BusinessPartnerTerms.Label.BusinessPartnerName"/></label>
        <form:input id="businessPartner" class="typeahead form-control" 
                    type="text"   path="businessPartner.name" autofocus="true"/>
        <form:input id="businessPartner-hidden" type="hidden" 
                    path="transientPartnerId"/>
    </div>
    <div class="row">
        <div class="form-group col-lg-6">
            <label style="margin-top: 20px;" for="status"><spring:message code="BusinessPartnerTerms.Label.Status"/></label>
            <form:select path="status" id="status" class="form-control" itemLabel="status">
                <form:options items="${statuses}" itemLabel="description"/>
            </form:select>
        </div>
        <spring:bind path="daysToPay">
            <div class="form-group col-lg-6">
                <label for="daysToPay" style="margin-top: 20px;" >
                    <spring:message code="BusinessPartnerTerms.Label.DaysToPay" />
                </label>
                <form:input id="daysToPay" path="daysToPay" class="form-control"/>                    
                <span class="help-inline">
                    <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                    </span>
                </div>
        </spring:bind>
    </div>
    <div class="row">
        <spring:bind path="dateFrom">
            <div class="form-group col-lg-6">
                <label for="dateFrom">
                    <spring:message code="BusinessPartnerTerms.Label.DateFrom" />
                </label>
                <form:input id="dateFrom" path="dateFrom" class="form-control"/>                    
                <span class="help-inline">
                    <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                    </span>
                </div>
        </spring:bind>
        <spring:bind path="endDate">
            <div class="form-group col-lg-6">
                <label for="endDate" >
                    <spring:message code="BusinessPartnerTerms.Label.EndDate" />
                </label>
                <form:input id="endDate" path="endDate" class="form-control"/>                    
                <span class="help-inline">
                    <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                    </span>
                </div>
        </spring:bind>
    </div>
    <spring:bind path="rebate">
        <div class="row">
            <div class="form-group col-lg-6">
                <label for="rebate" >
                    <spring:message code="BusinessPartnerTerms.Label.Rebate" />
                </label>
                <form:input id="rebate" path="rebate" class="form-control"/>                    
                <span class="help-inline">
                    <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                    </span>
                </div>
            </div>
    </spring:bind>
    <form:hidden path="version"/>
    <div class="form-group">
        <spring:url value="/terms/${page}/read-page.html" var="back"/>
        <a class="btn btn-primary" href="${back}">
            <spring:message code="BusinessPartnerTerms.Button.Back" />
        </a>
        <button type="submit" class="btn btn-primary">
            <spring:message code="BusinessPartnerTerms.Button.Create"/>
        </button>
    </div>
</form:form>
<script type="text/javascript">
    $('#dateFrom').datepicker({});
    $('#endDate').datepicker({});
    $('#businessPartner').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/terms/read-merchant/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#businessPartner').bind('typeahead:selected', function (obj, datum, name) {
        $('#businessPartner-hidden').val(datum['id']);
    });
</script>