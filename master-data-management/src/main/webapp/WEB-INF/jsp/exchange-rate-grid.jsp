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
                <input:inputDate name="applicationDate" label="Datum Kursne Liste" placeholder="dd.mm.yyyy"/>
            </div>
            <div class="col-lg-3">
                <div class="form-group">
                    <label for="currencyDescription">Valuta</label>
                    <form:input id="currencyDescription" class="typeahead form-control" type="text"
                                path="currencyDescription" style="margin-bottom:  15px;"/>
                    <form:hidden id="currencyISOHidden" path="ISOCode"/>
                </div>
            </div>
            <spring:bind path="buying">
                <div class="form-group row ">
                    <div class="col-lg-6">
                        <label for="buying"><spring:message code="ExchangeRate.Label.Buying"/></label>
                        <form:input id="buying" path="buying"
                                    class="form-control"/>
                        <span class="help-inline"><c:if test="${status.error}"><c:out
                                value="${status.errorMessage}"/></c:if></span>
                    </div>
                </div>
            </spring:bind>
            <spring:bind path="middle">
                <div class="form-group row ">
                    <div class="col-lg-6">
                        <label for="middle"><spring:message code="ExchangeRate.Label.middle"/></label>
                        <form:input id="middle" path="middle"
                                    class="form-control"/>
                        <span class="help-inline"><c:if test="${status.error}"><c:out
                                value="${status.errorMessage}"/></c:if></span>
                    </div>
                </div>
            </spring:bind>
            <spring:bind path="selling">
                <div class="form-group row ">
                    <div class="col-lg-6">
                        <label for="selling"><spring:message code="ExchangeRate.Label.selling"/></label>
                        <form:input id="selling" path="selling"
                                    class="form-control"/>
                        <span class="help-inline"><c:if test="${status.error}"><c:out
                                value="${status.errorMessage}"/></c:if></span>
                    </div>
                </div>
            </spring:bind>
            <form:hidden path="version"/>
        </div>
    </fieldset>
    <div class="form-group btn-group-sm">
        <a class="btn btn-primary" href="/masterdata/exchange-rate/0">Povratak</a>
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
    <div>
        <p>${message}</p>
    </div>
</form:form>
<script type="text/javascript">
    $('#currencyDescription').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'currency',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/masterdata/read-partner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#currencyDescription').bind('typeahead:selected', function (obj, datum, name) {
        $('#currencyISOHidden').val(datum['ISOcode']);
    });
</script>
