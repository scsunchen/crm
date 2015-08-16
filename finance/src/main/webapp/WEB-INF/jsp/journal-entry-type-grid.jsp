<%-- 
    Document   : journal-entry-type-grid
    Created on : Jul 20, 2015, 10:34:40 AM
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
<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<form:form modelAttribute="type" method="post">
    <div class="form-group " >
        <label for="client"><spring:message code="JournalEntryType.Label.Client" /></label>
        <c:choose>
            <c:when test="${action == 'create'}">
                <form:input id="client" class="typeahead form-control" type="text" 
                    path="clientName" autofocus="true" />
            </c:when>
            <c:otherwise>
               <form:input id="client" class="typeahead form-control" type="text" 
                           path="clientName" readonly="true" />
            </c:otherwise>
        </c:choose>
        <form:input id="client-hidden" type="hidden" path="clientId"/>
    </div>
    <form:hidden path="typeId" />
    <label style="margin-top: 20px;" ></label>
    <i:textField label="JournalEntryType.Label.Name" name="name" />
    <spring:bind path="journalEntryNumber">
        <div class="form-group row" >
            <div class="col-lg-6" >
                <label for="journalEntryNumber"><spring:message code="JournalEntryType.Label.Number"/></label>            
                <form:input id="journalEntryNumber" path="journalEntryNumber" class="form-control" readonly="true"/>
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
            </div>
    </spring:bind>
    <form:hidden path="version" />
    <div class="form-group">
        <button type="submit" class="btn btn-primary" >
            <c:choose>
                <c:when test="${action == 'create'}">
                    <spring:message code="JournalEntryType.Button.Create" />
                </c:when>
                <c:otherwise>
                    <spring:message code="JournalEntryType.Button.Update" />
                </c:otherwise>
            </c:choose>
        </button>
        <a href="${pageContext.request.contextPath}/journal-entry-type/${page}" 
           class="btn btn-default" >
            <span class="glyphicon glyphicon-backward"></span> 
            <spring:message code="JournalEntryType.Button.Back" />
        </a>
    </div>
</form:form>
<script type="text/javascript">
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
</script>