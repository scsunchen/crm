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

    <c:choose>
        <c:when test="${action == 'update'}">
            <input:inputField label="Šifra *" name="id" disabled="true"/>
        </c:when>
    </c:choose>
    <input:inputField name="customCode" label="Korisnička šifra"/>
    <div class="form-group">
        <label for="itemDesc">Tip terminala</label>
        <form:input id="itemDesc" class="typeahead form-control" type="text"
                    path="articleDescription" style="margin-bottom:  15px;"/>
        <form:hidden id="itemDescHidden" path="articleCode"/>
    </div>

    <input:inputField name="serialNumber" label="Serijski Broj"/>
    <spring:bind path="deviceStatusId">
        <div class="form-group">
            <label for="status">Status</label>
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
    <input:inputField name="installedSoftwareVersion" label="Firmware verzija"/>
    <form:hidden path="version"/>
    <div class="form-group">
        <a class="btn btn-primary" href="/masterdata/device/read-page?page=0">Povratak</a>
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
                <li><a href="/masterdata/partner/updateTerminal">Izmena Terminala</a></li>
                <li><a href="/masterdata/partner/updateTerminalStatus">Izmena Status Terminala</a></li>
                <li><a href="/masterdata/partner/cancelActivateTerminal">Otkazi/Akirajtiv</a></li>
                <li><a href="/masterdata/partner/checkTerminalStatus">Provera Status Terminala</a></li>
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

