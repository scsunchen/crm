<%-- 
    Author     : bdragan
--%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="journalEntry" method="post" >
    <div class="form-group " >
        <label for="client"><spring:message code="JournalEntry.Label.Client" /></label>
        <form:input id="client" class="form-control" type="text" path="clientName" readonly="true" />
        <form:input id="client-hidden" type="hidden" path="clientId"/>
    </div>
    <div class="form-group " >
        <label for="type"><spring:message code="JournalEntry.Label.Type" /></label>
        <form:input id="type" class="form-control" type="text" path="typeName" readonly="true" />
        <form:input id="type-hidden" type="hidden" path="typeId"/>
    </div>
    <div class="form-group row">
        <div class="col-lg-6" >
            <label for="typeNumber"><spring:message code="JournalEntry.Label.TypeNumber" /></label>
            <form:input id="typeNumber" path="typeNumber" class="form-control" readonly="true" />
        </div>
    </div>
    <div class="form-group row">
        <div class="col-lg-6" >
            <label for="journalEntryNumber"><spring:message code="JournalEntry.Label.JournalEntryNumber" /></label>
            <form:input id="journalEntryNumber" path="journalEntryNumber" 
                        class="form-control" readonly="true"/>
        </div>
    </div>
    <spring:bind path="recordDate">
        <div class="form-group row" >
            <div class="col-lg-6" >
                <label for="date"><spring:message code="JournalEntry.Label.Date" /></label>
                <c:choose>
                    <c:when test="${journalEntry.isPosted == true}">
                        <form:input id="date" path="recordDate" type="text" class="form-control" readonly="true"/>                        
                    </c:when>
                    <c:otherwise>
                        <form:input id="date" path="recordDate" type="text" class="form-control" />                                                
                    </c:otherwise>
                </c:choose>
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
            </div>
        </div>
    </spring:bind>
    <form:hidden path="version" />    
    <div class="form-group">
        <button type="submit" class="btn btn-primary <c:if test="${journalEntry.isPosted == true}">disabled</c:if>" ><spring:message code="JournalEntry.Button.Update" /></button>
    </div>
</form:form>