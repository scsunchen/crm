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
    <input:alert message="${message}" alertType="${alertType}"/>
    <fieldset class="col-lg-12">
        <div class="form-group">
            <div class="col-lg-4">
                <spring:bind path="type">
                    <div class="form-group">
                        <label for="type">Tip partnera</label>
                        <form:select path="type" id="type" class="form-control" itemLabel="type">
                            <form:option value="${item.type}">${item.typeDescription}</form:option>
                            <form:options items="${types}" itemLabel="description"/>
                        </form:select>
                    </div>
                </spring:bind>
                <input:inputField label="Šifra *" name="id" disabled="true"/>
                <spring:bind path="name">
                    <div>
                        <input:inputField label="Naziv *" name="name"/>
                    <span class="help-inline"><c:if test="${status.error}"><c:out
                            value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
                <spring:bind path="companyIdNumber">
                    <div>
                        <input:inputField label="Matični broj *" name="companyIdNumber"/>
                    <span class="help-inline"><c:if test="${status.error}"><c:out
                            value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
                <spring:bind path="TIN">
                    <div>
                        <input:inputField label="PIB *" name="TIN"/>
                    <span class="help-inline"><c:if test="${status.error}"><c:out
                            value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
            </div>

            <div class="col-lg-4">
                <nav class="nav navbar-default">
                    </br>
                    <div class="col-lg-12">
                        <spring:bind path="country">
                            <div class="form-group">
                                <label for="country"><spring:message
                                        code="BusinessPartnerContacts.Table.State"/></label>
                                <form:input id="country" path="country" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-lg-8">
                        <spring:bind path="place">
                            <div class="form-group">
                                <label for="place"><spring:message code="BusinessPartnerContacts.Table.Place"/></label>
                                <form:input id="place" path="place" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-lg-4">
                        <spring:bind path="postCode">
                            <div class="form-group">
                                <label for="postCode"><spring:message
                                        code="BusinessPartnerContacts.Table.ZipCode"/></label>
                                <form:input id="place" path="postCode" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </div>
                        </spring:bind>
                    </div>
                    <spring:bind path="street">
                        <div class="form-group col-lg-10">
                            <label for="street"><spring:message code="BusinessPartnerContacts.Table.Street"/></label>
                            <form:input id="street" path="street" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </spring:bind>
                    <spring:bind path="houseNumber">
                        <div class="form-group col-lg-2">
                            <label for="houseNumber"><spring:message
                                    code="BusinessPartnerContacts.Table.HouseNumber"/></label>
                            <form:input id="houseNumber" path="houseNumber" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </spring:bind>
                </nav>

                <input:inputField name="phone" label="Telefon"/>
                <input:inputField name="fax" label="Fax"/>
                <input:inputField name="EMail" label="email"/>
            </div>
            <div class="col-lg-4">
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
                <div class="form-group">
                    <label for="partnerName">Nadređeni partner</label>
                    <form:input id="partnerName" class="typeahead form-control" type="text"
                                path="parentBusinesspartnerName" style="margin-bottom:  15px;"/>
                    <form:hidden id="partnerNameHidden" path="parentBusinessPartnerId"/>
                </div>
                <div class="checkbox">
                    <label><form:checkbox path="VAT" id="VAT" class="checkbox"/>PDV</label>
                </div>
                <input:inputField label="TelekomID" name="telekomId"/>
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
        <div class="btn-group pull-right">
            <button class="btn btn-default btn-lg dropdown-toggle" type="button" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                Telekom WS <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li><a href="/masterdata/partner/registerMerchant">Registracija partnera</a></li>
                <li><a href="/masterdata/partner/updateMerchant">Izmena partnera</a></li>
                <li role="separator" class="divider"></li>
                <li><a href="/masterdata/partner/deactivateMerchant">Deaktivacija partnera</a></li>
            </ul>
        </div>
    </div>
</form:form>
<script type="text/javascript">
    $('#partnerName').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/read-partner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#partnerName').bind('typeahead:selected', function (obj, datum, name) {
        $('#partnerNameHidden').val(datum['id']);
    });
</script>
