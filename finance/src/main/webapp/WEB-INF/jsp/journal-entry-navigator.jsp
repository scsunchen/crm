<%--     
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<a href="${pageContext.request.contextPath}/journal-entry/${page}/${journalEntry.clientId}/${journalEntry.typeId}/${journalEntry.journalEntryNumber}/update.html" class="btn btn-default"><spring:message code="JournalEntry.Button.Header" /></a>
<a href="${pageContext.request.contextPath}/journal-entry/${page}/0/${journalEntry.clientId}/${journalEntry.typeId}/${journalEntry.journalEntryNumber}/details.html" class="btn btn-default"><spring:message code="JournalEntry.Button.Details" /></a>
<a href=
   "${pageContext.request.contextPath}/journal-entry/${journalEntry.clientId}/${journalEntry.typeId}/${journalEntry.journalEntryNumber}/print.html" 
   class="btn btn-default" 
   target="_blank">
    <span class="glyphicon glyphicon-search"></span> <spring:message code="JournalEntry.Button.PrintPreview" /></a>
<div class="modal fade" id="dialogRecord" tabindex="-1" role="dialog" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">
                    <spring:message code="JournalEntry.RecordQuestion" 
                                    arguments="${journalEntry.clientName},${journalEntry.typeName},${journalEntry.journalEntryNumber}"/>
                </h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="JournalEntry.Button.Cancel" /></button>
                <a class="btn btn-primary"
                   href="${pageContext.request.contextPath}/journal-entry/${page}/${journalEntry.clientId}/${journalEntry.typeId}/${journalEntry.journalEntryNumber}/${journalEntry.version}/record.html">
                   <spring:message code="JournalEntry.Button.Record" /></a>
            </div>
        </div>
    </div>
</div>
<button data-toggle="modal" data-target="#dialogRecord" class="btn btn-default" <c:if test="${journalEntry.isPosted == true}">disabled</c:if>>
    <span class="glyphicon glyphicon-pencil"></span> <spring:message code="JournalEntry.Button.Record" />
</button>
<a href="${pageContext.request.contextPath}/journal-entry/${journalEntry.clientId}/${journalEntry.typeId}/${journalEntry.journalEntryNumber}/export.html" class="btn btn-default" >
    <span class="glyphicon glyphicon-download"></span> <spring:message code="JournalEntry.Button.Export" /></a>
<a href="${pageContext.request.contextPath}/journal-entry/${page}" class="btn btn-default" >
    <span class="glyphicon glyphicon-backward"></span> <spring:message code="JournalEntry.Button.Back" /></a>
