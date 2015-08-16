<%-- 
    Document   : invoice-master-toolbar
    Created on : Jun 16, 2015, 8:59:02 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>
<form:form modelAttribute="requestInvoiceRecording" 
           method="post" 
           action="${pageContext.request.contextPath}/invoice/${page}/recording.html">
    <div class="modal fade" id="dialogInvoiceRecording" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <c:if test = "${recordInvoiceException != null}">
                        <div class="alert alert-warning">
                            <a href="#" class="close" data-dismiss="alert">&times;</a>
                            ${recordInvoiceException.message}
                        </div>
                    </c:if>
                    <form:hidden path="clientId" />
                    <form:hidden path="orgUnitId" />
                    <form:hidden path="document" />
                    <div class="form-group" >
                        <label for="type"><spring:message code="Invoice.Label.Type" /></label>
                        <form:input id="type" class="form-control" type="text" path="journalEntryTypeName" />
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
                        <form:input id="description" class="form-control" 
                                    type="text" path="descriptionName" />
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
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="Invoice.Button.Cancel" /></button>
                    <button type="submit" class="btn btn-primary"><spring:message code="Invoice.Button.Record" /></button>
                </div>
            </div>
        </div>
    </div>
</form:form>
<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>

<a href="${pageContext.request.contextPath}/invoice/${page}/create" 
   class="btn btn-default" ><span class="glyphicon glyphicon-plus"></span><spring:message code="Invoice.Button.Create" /></a>
<div class="modal fade" id="dialogDelete" tabindex="-1" role="dialog" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">
                    <spring:message code="Invoice.Delete.Question" 
                                    arguments="${invoice.clientDesc},${invoice.document}" />
                </h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="Invoice.Button.Cancel" /></button>
                <a  class="btn btn-danger" 
                    href="${pageContext.request.contextPath}/invoice/${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/delete.html">
                    <spring:message code="Invoice.Button.Delete" /></a>
            </div>
        </div>
    </div>
</div>
<button class="btn btn-default" <c:if test="${invoice.recorded == true}">disabled</c:if> data-toggle="modal" data-target="#dialogDelete">
    <span class="glyphicon glyphicon-trash"></span> <spring:message code="Invoice.Button.Delete" /></button>
<a href=
   "${pageContext.request.contextPath}/invoice/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/print-preview.html" 
   class="btn btn-default" 
   target="_blank">
    <span class="glyphicon glyphicon-search"></span> <spring:message code="Invoice.Button.PrintPreview" /></a>
<button class="btn btn-default" <c:if test="${invoice.recorded == true}">disabled</c:if> data-toggle="modal" data-target="#dialogInvoiceRecording"><span class="glyphicon glyphicon-edit"></span> <spring:message code="Invoice.Button.Record" /></button>
<a href="${pageContext.request.contextPath}/invoice/${page}" class="btn btn-default" ><span class="glyphicon glyphicon-backward"></span> <spring:message code="Invoice.Button.Back" /></a>
<script type="text/javascript">
    $('#description').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 10
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-description/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#description').bind('typeahead:selected', function (obj, datum, name) {
        $('#descriptionId').val(datum['id']);
    });
    $('#type').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-journal-entry-type/',
                replace: function (url, query) {
                    return url.concat(query).concat("/").concat(${invoice.clientId});
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
<c:if test="${showRecordDialog}" >
    <script type="text/javascript">
        $('#dialogInvoiceRecording').modal('show');
    </script>
</c:if>      