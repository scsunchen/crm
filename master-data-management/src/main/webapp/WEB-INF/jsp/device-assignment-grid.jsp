<%--
  Created by IntelliJ IDEA.
  User: Nikola
  Date: 16/11/2015
  Time: 15:45
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>

<nav class="navbar navbar-default" <c:if test="${param['masterPartnerId'] == null}">hidden</c:if>>
    </br>
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="btn btn-default"
               href="${pageContext.request.contextPath}/deviceholder/device-assignment.html?businessPartnerId=&deviceCustomCode=&page=0">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="BusinessPartnerDetails.Button.Back"/></a></div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-6"><p class="navbar-text navbar-right">
            <c:out value="${param['masterPartnerId']} / ${param['masterPartnerName']}"/></p></div>
    </div>
</nav>
<form:form modelAttribute="item" method="post">
    <fieldset>
        <div class="form-group">
            <div class="col-lg-6">
                <div class="form-group">
                    <c:choose>
                        <c:when test="${action == 'create'}">
                            <spring:bind path="id">
                                <label for="id"><spring:message code="BusinessPartnerContacts.Table.Id"/></label>
                                <form:input id="id" path="id" class="form-control" disabled="true"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </spring:bind>
                        </c:when>
                    </c:choose>
                </div>
                <div class="form-group">
                    <label for="deviceCustomCode"><spring:message
                            code="BusinessPartnerDevice.Table.customCode"/></label>
                    <form:input id="deviceCustomCode" class="typeahead form-control" type="text"
                                path="deviceCustomCode" style="margin-bottom:  15px;"/>
                    <form:hidden id="deviceCustomCodeHidden" path="deviceId"/>
                </div>
                <div class="form-group">
                    <label for="businessPartnerName"><spring:message
                            code="BusinessPartnerDevice.Table.businessPartnerName"/></label>
                    <form:input id="businessPartnerName" class="typeahead form-control" type="text"
                                path="businessPartnerName" style="margin-bottom:  15px;"/>
                    <form:hidden id="businessPartnerNameHidden" path="businessPartnerId"/>
                </div>
                <spring:bind path="connectionTypeId">
                    <div class="form-group">
                        <label for="connectionType">Tip Konekcije</label>
                        <form:select path="connectionTypeId" id="connectionType" class="form-control"
                                     itemLabel="connectionType">
                            <form:option value="${connectionTypeId}">${connectionTypeDescription}</form:option>
                            <form:options items="${connectionTypes}" itemLabel="description" itemValue="id"/>
                        </form:select>
                    </div>
                </spring:bind>
                <spring:bind path="refillTypeId">
                    <div class="form-group">
                        <label for="refillType">Tip Dopune</label>
                        <form:select path="refillTypeId" id="refillType" class="form-control" itemLabel="refillType">
                            <form:option value="${refillTypeId}">${refillTypeDescription}</form:option>
                            <form:options items="${refillTypes}" itemLabel="description" itemValue="id"/>
                        </form:select>
                    </div>
                </spring:bind>
                <div class="col-lg-4">
                    <spring:bind path="workingStartTime">
                        <div class="form-group row">

                            <label for="workingStartTime" style="margin-top: 15px;"><spring:message
                                    code="DeviceHolder.label.WorkingStartTime"/></label>
                            <form:input id="workingStartTime" path="workingStartTime" type="text"
                                        class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </spring:bind>
                    <spring:bind path="workingEndTime">
                        <div class="form-group row">
                            <label for="workingEndTime" style="margin-top: 15px;"><spring:message
                                    code="DeviceHolder.label.WorkingEndTime"/></label>
                            <form:input id="workingEndTime" path="workingEndTime" type="text"
                                        class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </spring:bind>
                </div>

            </div>
            <div class="col-lg-6">
                <input:inputField name="MSISDN" label="MSISDN"/>
                <input:inputField name="ICCID" label="ICCID"/>
                <input:inputField name="transactionLimit" label="Limit transakcija"/>
                <input:inputField name="limitPerDay" label="Dnevni limit"/>
                <input:inputField name="limitPerMonth" label="Mesecni limit"/>

                <spring:bind path="activationDate">
                    <div class="form-group row">
                        <div class="col-lg-6">
                            <label for="date" style="margin-top: 15px;"><spring:message
                                    code="DeviceHolder.label.ActivationDate"/></label>
                            <form:input id="date" path="activationDate" type="text"
                                        class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </div>
                </spring:bind>

                <input:inputField name="telekomId" label="Telekom ID"/>
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
                <li><a href="/masterdata/device/registerTerminal">Registracija Terminala</a></li>
            </ul>
        </div>
    </div>
</form:form>

<script type="text/javascript">

    $('#date').datepicker({});
    $('#workingStartTime').datetimepicker({
        format: 'HH:mm'
    });
    $('#workingEndTime').datetimepicker({
        format: 'HH:mm'
    });

    $('#businessPartnerName').typeahead({
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
                url: '${pageContext.request.contextPath}/partner/read-all-pos/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#businessPartnerName').bind('typeahead:selected', function (obj, datum, name) {
        $('#businessPartnerNameHidden').val(datum['id']);
    });

    $('#deviceCustomCode').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'customCode',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/read-device/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#deviceCustomCode').bind('typeahead:selected', function (obj, datum, name) {
        console.log("evo ga rezultat jebeni")
        $('#deviceCustomCodeHidden').val(datum['id']);
    });

</script>


