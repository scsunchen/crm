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

<form:form modelAttribute="item" method="post" cssClass="generic-container">
    <fieldset>
        <div class="form-group">
            <div class="col-lg-6">
                <div class="form-group">
                    <c:choose>
                        <c:when test="${action == 'create'}">
                            <spring:bind path="id">
                                <label for="id"><spring:message code="BusinessPartnerDevice.Table.Id"/></label>
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

            </div>
            <div class="col-lg-6">
                <spring:bind path="activationDate">
                    <div class="form-group row">
                        <label for="date" style="margin-top: 15px;"><spring:message
                                code="DeviceHolder.label.ActivationDate"/></label>
                        <form:input id="date" path="activationDate" type="text"
                                    class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
                <spring:bind path="startDate">
                    <div class="form-group row">
                        <label for="startDate" style="margin-top: 15px;"><spring:message
                                code="DeviceHolder.label.StartDate"/></label>
                        <form:input id="startDate" path="startDate" type="text"
                                    class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
                <spring:bind path="endDate">
                    <div class="form-group row">
                        <label for="endDate" style="margin-top: 15px;"><spring:message
                                code="DeviceHolder.label.EndDate"/></label>
                        <form:input id="endDate" path="endDate" type="text"
                                    class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
            </div>
            <form:hidden path="version"/>

        </div>
    </fieldset>
    <div class="form-group">
        <c:choose>
            <c:when test="${param['masterPartnerId'] == null || param['masterPartnerId'] == ''}">
                <a class="btn btn-default"
                   href="${pageContext.request.contextPath}/deviceholder/device-assignment.html?businessPartnerId=&deviceCustomCode=&page=0">
                    <span class="glyphicon glyphicon-backward"></span>
                    <spring:message code="BusinessPartnerDetails.Button.Back"/></a>
            </c:when>
            <c:otherwise>
                <a class="btn btn-default"
                   href="${pageContext.request.contextPath}/partner/read-deviceholderdetails-page.html?masterPartnerId=${param['masterPartnerId']}&pointOfSaleId=${param['pointOfSaleId']}&page=0">
                    <span class="glyphicon glyphicon-backward"></span>
                    <spring:message code="BusinessPartnerDetails.Button.Back"/></a>

            </c:otherwise>
        </c:choose>
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

<script type="text/javascript">

    $('#date').datepicker({});
    $('#startDate').datepicker({});
    $('#endDate').datepicker({});
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


