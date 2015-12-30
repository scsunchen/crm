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

    <c:choose>
        <c:when test="${action == 'update'}">
            <input:inputField label="Device.Table.Id" name="id" disabled="true"/>
        </c:when>
    </c:choose>
    <input:inputField name="customCode" label="Device.Table.CustomCode"/>
    <div class="form-group">
        <label for="itemDesc"><spring:message code="Device.Table.Type"></spring:message> </label>
        <form:input id="itemDesc" class="typeahead form-control" type="text"
                    path="articleDescription" style="margin-bottom:  15px;"/>
        <form:hidden id="itemDescHidden" path="articleCode"/>
    </div>

    <input:inputField name="serialNumber" label="Device.Table.SerialNo"/>
    <spring:bind path="deviceStatusId">
        <div class="form-group">
            <label for="status"><spring:message code="Device.Table.Status"></spring:message> </label>
            <form:select path="deviceStatusId" id="status" class="form-control" itemLabel="status">
                <form:option value="${deviceStatusId}">${deviceStatusName}</form:option>
                <form:options items="${deviceStatuses}" itemLabel="name" itemValue="id"/>
            </form:select>
        </div>
    </spring:bind>
    <spring:bind path="creationDate">
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="date" style="margin-top: 15px;"><spring:message
                        code="Device.label.CreationDate"/></label>
                <form:input id="date" path="creationDate" type="text"
                            class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
            </div>
        </div>
    </spring:bind>
    <input:inputField name="installedSoftwareVersion" label="Device.Table.Firmware"/>
    <form:hidden path="version"/>
    <div class="form-group">
        <a class="btn btn-default" href="/masterdata/device/read-page.html?page=0">
            <span class="glyphicon glyphicon-backward"></span>
            <spring:message code="Common.Button.Back"></spring:message> </a>
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
                <spring:message code="Device.Table.TelekomWS"></spring:message><span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li><a href="/masterdata/deviceholder/terminal/register"><spring:message
                        code="Device.Table.Register"></spring:message></a></li>
                <li><a href="/masterdata/deviceholder/terminal/update"><spring:message code="Device.Table.Update"/></a>
                </li>
                <li><a href="/masterdata/deviceholder/terminal/update-status"><spring:message
                        code="Device.Table.StatusUpdate"></spring:message> </a></li>
                <li><a href="/masterdata/partner/cancelActivateTerminal"><spring:message
                        code="Device.table.CancelActivate"></spring:message></a></li>
                <li><a href="/masterdata/partner/checkTerminalStatus"><spring:message
                        code="Device.Table.StatusCheck"></spring:message></a></li>
            </ul>
        </div>
    </div>
</form:form>
<script type="text/javascript">

    $('#date').datepicker({});

    $('#itemDesc').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'description',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/device/read-item/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#itemDesc').bind('typeahead:selected', function (obj, datum, name) {
        console.log(datum['code']);
        $('#itemDescHidden').val(datum['code']);
    });
</script>

