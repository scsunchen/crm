<%-- 
    Document   : invoice-record-grid
    Created on : Nov 14, 2015, 2:09:56 PM
    Author     : dbobic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
<form:form modelAttribute="requestInvoiceRecording" 
           method="post" 
           action="${pageContext.request.contextPath}/invoice/${page}/record.html">
    <c:if test = "${recordInvoiceException != null}">
        <div class="alert alert-warning">
            <a href="#" class="close" data-dismiss="alert">&times;</a>
            ${recordInvoiceException.message}
        </div>
    </c:if>
    <form:hidden path="clientId" />
    <label>${requestInvoiceRecording.clientName}</label>
    <form:hidden path="orgUnitId" />
    <label>${requestInvoiceRecording.orgUnitName}</label>
    <br/>
    <form:hidden path="document" />
    <label>${requestInvoiceRecording.document}</label>
    <br/>
    <div class="form-group" >
        <label for="type"><spring:message code="Invoice.Label.Type" /></label>
        <form:input id="type" class="form-control typeahead" type="text" path="journalEntryTypeName" />
        <form:input id="typeId" type="hidden" path="entryOrderType"/>
    </div>
    <div class="form-group">
        <label for="typeNumber" style="margin-top: 15px;"><spring:message code="Invoice.Label.TypeNumber" /></label>
        <form:input id="typeNumber" path="journalEntryTypeNumber" class="form-control" readonly="true" />
    </div>
    <spring:bind path="journalEntryNumber">
        <div class="form-group">
            <label for="journalEntryNumber" ><spring:message code="Invoice.Label.JournalEntryNumber" /></label>
            <form:input id="journalEntryNumber" 
                        path="journalEntryNumber" 
                        class="form-control" />
            <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
            </div>
    </spring:bind>
    <div class="form-group">
        <label for="description"><spring:message code="Invoice.Label.Description" /></label>
        <form:input id="description" class="form-control typeahead" type="text" path="descriptionName" />
        <form:input id="descriptionId" type="hidden" path="description"/>
    </div>
    <spring:bind path="recordDate">
        <div class="form-group row" >
            <div class="col-lg-6" >
                <label for="date" style="margin-top: 15px;"><spring:message code="Invoice.Label.RecordDate" /></label>
                <form:input id="date" path="recordDate" type="text" 
                            class="form-control" />                        
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
            </div>
    </spring:bind>
    <button type="submit" class="btn btn-primary"><spring:message code="Invoice.Button.Record" /></button>
</form:form>
<script>
    $('#date').datepicker({});
    $('#description').autocomplete( 
            'name',
            '${pageContext.request.contextPath}/invoice/read-description/%QUERY');
    $('#description').bind('typeahead:selected', function (obj, datum, name) {
        $('#descriptionId').val(datum['id']);
    });
    $('#type').typeahead({
        hint: false,
        highlight: true
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-journal-entry-type/',
                replace: function (url, query) {
                    return url.concat(query).concat("/").concat(${requestInvoiceRecording.clientId});
                }
            }
        }),
        templates: {
            suggestion: Handlebars.compile('<div><strong>{{clientName}}</strong> &nbsp; {{name}}</div>')
        }

    });
    $('#type').bind('typeahead:selected', function (obj, datum, name) {
        $('#typeId').val(datum['typeId']);
        $('#typeNumber').val(datum['journalEntryNumber']);
    });
</script>
