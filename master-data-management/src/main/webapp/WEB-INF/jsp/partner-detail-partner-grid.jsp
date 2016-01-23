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
    <input:alert message="${message}" alertType="${alertType}"/>
    <fieldset class="col-lg-12">
        <form:hidden path="companyIdNumber"/>
        <form:hidden path="type"/>
        <form:hidden path="TIN"/>
        <form:hidden path="version"/>
        <div class="form-group">
            <div class="col-lg-6">
                <input:inputField label="BusinessPartner.Table.Id" name="id" disabled="true"/>
                <spring:bind path="typeT">
                    <div class="form-group">
                        <label for="typeT"><spring:message code="BusinessPartner.Table.Type"></spring:message></label>
                        <form:select path="typeT" id="typeT" class="form-control" itemLabel="typeT">
                            <form:option value="${item.type}">${item.typeDescription}</form:option>
                            <form:options items="${types}" itemLabel="description"/>
                        </form:select>
                    </div>
                </spring:bind>
                <spring:bind path="name">
                    <div>
                        <input:inputField label="BusinessPartner.Table.Name" name="name"/>
                    <span class="help-inline"><c:if test="${status.error}"><c:out
                            value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
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
                    <div class="col-lg-9">
                        <spring:bind path="place">
                            <div class="form-group">
                                <label for="tPlace"><spring:message code="BusinessPartnerContacts.Table.Place"/></label>
                                <form:input id="tPlace" class="typeahead form-control" type="text"
                                            path="tPlace" style="margin-bottom:  15px;"/>
                                <form:hidden id="placeCodeHidden" path="tPlaceCode"/>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-lg-3">
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
                    <div class="col-lg-9">
                        <spring:bind path="tStreet">
                            <div class="form-group">
                                <label for="tStreet"><spring:message
                                        code="BusinessPartnerContacts.Table.Street"/></label>
                                <form:input id="tStreet" class="typeahead form-control" type="text"
                                            path="tStreet" style="margin-bottom:  15px;"/>
                                <form:hidden id="streetCodeHidden" path="tStreetCode"/>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-lg-3">
                        <spring:bind path="tHouseNumber">
                            <div class="form-group">
                                <label for="tHouseNumber"><spring:message
                                        code="BusinessPartnerContacts.Table.HouseNumber"/></label>
                                <form:input id="tHouseNumber" path="tHouseNumber" class="form-control"/>
                                <form:hidden id="houseNumberCodeHidden" path="tHouseNumberCode"/>
                            </div>
                        </spring:bind>
                    </div>
                </nav>
                <form:hidden path="tAddressCode"/>
            </div>
            <div class="col-lg-6">
                <input:inputField name="phone" label="BusinessPartner.Table.Phone"/>
                <input:inputField name="fax" label="BusinessPartner.Table.Fax"/>
                <input:inputField name="EMail" label="BusinessPartner.Table.eMail"/>
                <div class="form-group">
                    <label for="partnerName"><spring:message
                            code="BusinessPartner.Table.ParentPartner"></spring:message></label>
                    <form:input id="partnerName" class="typeahead form-control" type="text"
                                path="parentBusinesspartnerName" style="margin-bottom:  15px;"/>
                    <form:hidden id="partnerNameHidden" path="parentBusinessPartnerId"/>
                </div>
                <div class="form-group">
                    <spring:bind path="posTypeId">
                        <label for="posTypeId"><spring:message code="BusinessPartner.POS.Type"></spring:message></label>
                        <form:select path="posTypeId" id="posTypeId" class="form-control">
                            <form:option value=""></form:option>
                            <form:options items="${POSTypes}" itemLabel="description" itemValue="id"/>
                        </form:select>
                    </spring:bind>
                </div>
                <div class="col-lg-6">
                    <input:inputField name="longitude" label="BusinessPartner.Table.Longitude"></input:inputField>
                </div>
                <div class="col-lg-6">
                    <input:inputField name="latitude" label="BusinessPartner.Table.Latitude"></input:inputField>
                </div>
                <input:inputField label="BusinessPartner.Table.TelekomId" name="telekomId"/>
                <spring:bind path="telekomStatus">
                    <div class="form-group">
                        <label for="telekomStatus"><spring:message
                                code="BusinessPartner.Table.TelekomStatus"></spring:message></label>
                        <form:select path="telekomStatus" id="telekomStatus" class="form-control" itemLabel="type">
                            <form:option value="${item.telekomStatus}">${item.telekomStatusDescription}</form:option>
                            <form:options items="${telekomStatuses}" itemLabel="description"/>
                        </form:select>
                    </div>
                </spring:bind>

            </div>
            <div class="col-lg-12">
                <input:inputTextArea name="remark" label="BusinessPartner.Table.Remark"></input:inputTextArea>
            </div>
        </div>
    </fieldset>

    <div class="form-group">
        </br>
        <a href="${pageContext.request.contextPath}/partner/read-subpartners.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${param['page']}"
           class="btn btn-default">
            <span class="glyphicon glyphicon-backward"></span>
            <spring:message code="Common.Button.Back"/></a>
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

                <li>
                    <button class="btn btn-default btn-block" type="submit" name="register"><spring:message
                            code="BusinessPartner.POS.Registration"></spring:message></button>
                </li>
                <li>
                    <button class="btn btn-default btn-block" type="submit" name="update"><spring:message
                            code="BusinessPartner.POS.Update"></spring:message></button>
                </li>
                <li role="separator" class="divider"></li>
                <li>
                    <button class="btn btn-default btn-block" type="submit" name="deactivation"><spring:message
                            code="BusinessPartner.POS.Deactiovation"></spring:message>
                    </button>
                </li>

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

    $('#tPlace').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'place',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/address/read-mesto/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#tPlace').bind('typeahead:selected', function (obj, datum, name) {
        $('#placeCodeHidden').val(datum['adrKod']);
    });

    $('#tStreet').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'street',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/address/read-streets/%QUERY',
                replace: function () {
                    var q = '${pageContext.request.contextPath}/partner/address/read-streets/' + encodeURIComponent($('#tStreet').val());
                    if ($('#placeCodeHidden').val()) {
                        q += "?place=" + encodeURIComponent($('#placeCodeHidden').val());
                    }
                    return q;
                },
                wildcard: '%QUERY'
            }
        })
    });
    $('#tStreet').bind('typeahead:selected', function (obj, datum, name) {
        $('#streetCodeHidden').val(datum['adrKod']);
    });


    $('#tHouseNumber').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'houseNumber',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/address/read-housenumbers/%QUERY',
                replace: function () {
                    var q = '${pageContext.request.contextPath}/partner/address/read-housenumbers/' + encodeURIComponent($('#tStreet').val());
                    if ($('#placeCodeHidden').val()) {
                        q += "?place=" + encodeURIComponent($('#placeCodeHidden').val());
                    }
                    if ($('#streetCodeHidden').val()) {
                        q += "&street=" + encodeURIComponent($('#streetCodeHidden').val());
                    }
                    console.log(q);
                    return q;
                },
                wildcard: '%QUERY'
            }
        })
    });
    $('#tHouseNumber').bind('typeahead:selected', function (obj, datum, name) {
        $('#houseNumberCodeHidden').val(datum['adrKod']);
    });
</script>

