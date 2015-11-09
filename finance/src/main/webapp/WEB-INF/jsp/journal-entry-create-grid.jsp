<%-- 
    Document   : journal entry create grid
    Created on : Jul 18, 2015, 6:58:43 PM
    Author     : bdragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>

<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<form:form modelAttribute="journalEntry" method="post">
    <div class="form-group " >
        <label for="client"><spring:message code="JournalEntry.Label.Client" /></label>
        <form:input id="client" class="typeahead form-control" type="text" 
                    path="clientName" autofocus="true" />
        <form:input id="client-hidden" type="hidden" path="clientId"/>
    </div>
    <div class="form-group">
        <label for="type1" style="margin-top: 15px;"><spring:message code="JournalEntry.Label.Type" /></label>
        <form:input id="type1" path="typeName" class="typeahead form-control" 
                    type="text" />
        <form:input id="type-hidden" type="hidden" path="typeId" />
    </div>
    <div class="form-group row">
        <div class="col-lg-6" >
            <label for="typeNumber" style="margin-top: 15px;"><spring:message code="JournalEntry.Label.TypeNumber" /></label>
            <form:input id="typeNumber" path="typeNumber" class="form-control" readonly="true" />
        </div>
    </div>
    <spring:bind path="journalEntryNumber">
        <div class="form-group row">
            <div class="col-lg-6" >
                <label for="journalEntryNumber"><spring:message code="JournalEntry.Label.JournalEntryNumber" /></label>
                <form:input id="journalEntryNumber" path="journalEntryNumber" class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
            </div>
    </spring:bind>
    <spring:bind path="recordDate">
        <div class="form-group row" >
            <div class="col-lg-6" >
                <label for="date"><spring:message code="JournalEntry.Label.Date" /></label>
                <form:input id="date" path="recordDate" type="text" class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
            </div>
    </spring:bind>
    <form:hidden path="version" />    
    <div class="form-group">
        <button type="submit" class="btn btn-primary" ><spring:message code="JournalEntry.Button.Create" /></button>
        <a href="${pageContext.request.contextPath}/journal-entry/${page}" class="btn btn-default" >
            <span class="glyphicon glyphicon-backward"></span> <spring:message code="JournalEntry.Button.Back" />
        </a>
    </div>
</form:form>
<script type="text/javascript">
    $('#date').datepicker({});
    $('#client').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-client/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#client').bind('typeahead:selected', function (obj, datum, name) {
        $('#client-hidden').val(datum['id']);
    });
    
    $('#type1').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound( {
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/read-journal-entry-type/',
                replace: function(url, query) {
                    if($('#client-hidden').val().length === 0) {
                        return url.concat(query);
                    }
                    return url.concat(query).concat("/").concat($('#client-hidden').val());
                }
            }
        } ),
        templates: {
            suggestion: Handlebars.compile('<div><strong>{{clientName}}</strong> &nbsp; {{name}}</div>')
        }
        
    });
    $('#type1').bind('typeahead:selected', function (obj, datum, name) {
        $('#type-hidden').val(datum['typeId']);
        $('#typeNumber').val(datum['journalEntryNumber']);
    });
    
</script>