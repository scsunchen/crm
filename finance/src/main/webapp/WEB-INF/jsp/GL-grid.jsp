<%-- 
    Document   : desc-grid
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
<form:form modelAttribute="GLRequest" method="GET" 
           action="${pageContext.request.contextPath}/gl-card/read-general-ledger-submit.html">
    <div class="form-group" >
        <label for="client"><spring:message code="LedgerCard.Label.Client" /></label>
        <form:input id="client" class="typeahead form-control" path="clientName" type="text"  />
        <form:hidden id="clientId" path="clientID"/>
    </div>
    <div class="form-group" >
        <label for="orgUnit" style="margin-top:  15px;"><spring:message code="LedgerCard.Label.OrgUnit" /></label>
        <form:input id="orgUnit" class="typeahead form-control" path="orgUnitName" type="text" />
        <form:hidden id="orgUnitId" path="orgUnitID"/>
    </div>
    <div class="form-group" >
        <label for="accountNumber" style="margin-top:  15px;"><spring:message code="LedgerCard.Label.Account" /></label>
        <form:input id="accountNumber" class="typeahead form-control" 
                    type="text" path="accountNumber" />
    </div>
    <div class="row ">                                
        <spring:bind path="creditDebitRelationDateFrom">
            <div class="form-group col-lg-6 ">                                
                <label for="dateFrom" style="margin-top:  15px;"><spring:message code="LedgerCard.Label.CreditDebitRelationFrom" /></label>            
                <form:input id="dateFrom" path="creditDebitRelationDateFrom" class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
            </div>
        </spring:bind>
        <spring:bind path="creditDebitRelationDateTo">
            <div class="form-group col-lg-6">    
                <label for="dateTo" style="margin-top:  15px;"><spring:message code="LedgerCard.Label.CreditDebitRelationTo" /></label>            
                <form:input id="dateTo" path="creditDebitRelationDateTo" class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
            </div>
        </spring:bind>
    </div>
    <div class="row ">                                
        <spring:bind path="valueDateFrom">
            <div class="form-group col-lg-6 ">                                
                <label for="valueDateFrom" style="margin-top:  15px;"><spring:message code="LedgerCard.Label.ValuePeriodFrom" /></label>            
                <form:input id="valueDateFrom" path="valueDateFrom" class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
        </spring:bind>
        <spring:bind path="valueDateTo">
            <div class="form-group col-lg-6">    
                <label for="valueDateTo" style="margin-top:  15px;"><spring:message code="LedgerCard.Label.ValuePeriodTo" /></label>            
                <form:input id="valueDateTo" path="valueDateTo" class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
        </spring:bind>
    </div>
    <div class="form-group">
        <label for="currency" ><spring:message code="LedgerCard.Label.Currency" /></label>
        <form:input id="currency" class="typeahead form-control" 
                    type="text" path="foreignCurrencyISOCode" />
    </div>
    <div class="form-group">
        <button formtarget="_blank" type="submit" class="btn btn-primary" style="margin-top:  15px;"><spring:message code="LedgerCard.Button.Print" /></button>
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
                url: '${pageContext.request.contextPath}/gl-card/read-client/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#client').bind('typeahead:selected', function (obj, datum, name) {
        $('#clientId').val(datum['id']);
    });
    $('#orgUnit').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/gl-card/read-orgunit/',
                replace: function(url, query) {
                    if($('#clientId').val().length === 0) {
                        return url.concat(query);
                    }
                    return url.concat(query).concat("/").concat($('#clientId').val());
                }
            }
        }),
        templates: {
            suggestion: Handlebars.compile('<div><strong>{{clientName}}</strong> &nbsp; {{name}}</div>')
        }
    });
    $('#orgUnit').bind('typeahead:selected', function (obj, datum, name) {
        $('#orgUnitId').val(datum['id']);
    });
    $('#accountNumber').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'number',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/gl-card/read-account/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#currency').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'isocode',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/gl-card/read-currency/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
</script>