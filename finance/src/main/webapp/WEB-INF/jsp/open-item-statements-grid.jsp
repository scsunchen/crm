<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>

<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>

<div class="modal fade" id="closeItemsDialog" tabindex="-1" role="dialog" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal">
                    <span>&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">
                    <spring:message code="OpenStatements.CloseItemsQuestion" />
                </h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <spring:message code="OpenStatements.Button.Cancel" />
                </button>
                <a  class="btn btn-primary" 
                    href="${pageContext.request.contextPath}/open-item-statements/closeItems.html">
                    <spring:message code="OpenStatements.Button.CloseItems" />
                </a>
            </div>
        </div>
    </div>
</div>
<a data-toggle="modal" data-target="#closeItemsDialog"
   class="btn btn-default" >
    <spring:message code="OpenStatements.Button.CloseItems" />
</a>
<div class="modal fade" id="openItemsDialog" tabindex="-1" role="dialog" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal">
                    <span>&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">
                    <spring:message code="OpenStatements.OpenItemsQuestion" />
                </h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <spring:message code="OpenStatements.Button.Cancel" />
                </button>
                <a  class="btn btn-primary" 
                    href="${pageContext.request.contextPath}/open-item-statements/openItems.html">
                    <spring:message code="OpenStatements.Button.OpenItems" />
                </a>
            </div>
        </div>
    </div>
</div>
<a data-toggle="modal" data-target="#openItemsDialog"
   class="btn btn-default" >
    <spring:message code="OpenStatements.Button.OpenItems" />
</a>
<br/>
<br/>
<fieldset>
    <legend class="control-label"></legend>    
    <form:form modelAttribute="requestOpenItems" method="GET" 
               action="${pageContext.request.contextPath}/open-item-statements/process.html">
        <div class="form-group" >
            <label for="client"><spring:message code="OpenStatements.Label.Client" /></label>
            <form:input id="client" class="typeahead form-control" path="clientName" type="text"  />
            <form:hidden id="clientID" path="clientID"/>
        </div>
        <div class="form-group" >
            <label for="orgUnit" style="margin-top:  15px;"><spring:message code="OpenStatements.Label.OrgUnit" /></label>
            <form:input id="orgUnit" class="typeahead form-control" path="orgUnitName" type="text" />
            <form:hidden id="orgUnitId" path="orgUnitID"/>
        </div>
        <div class="form-group" >
            <label for="accountNumber" style="margin-top:  15px;"><spring:message code="OpenStatements.Label.Account" /></label>
            <form:input id="accountNumber" class="typeahead form-control" 
                        type="text" path="accountNumber" />
        </div>
        <div class="form-group" >
            <label for="businessPartnerName" style="margin-top:  15px;"><spring:message code="LedgerCard.Label.BusinessPartnerName" /></label>
            <form:input id="businessPartnerName" class="typeahead form-control" 
                        type="text" path="partnerName" />
            <form:hidden id="partnerID" path="partnerID" />
        </div>
        <div class="row ">                                
            <spring:bind path="valueDate">
                <div class="form-group col-lg-6 ">                                
                    <label for="valueDate" style="margin-top:  15px;"><spring:message code="OpenStatements.Label.ValueDate" /></label>            
                    <form:input id="valueDate" path="valueDate" class="form-control" />
                    <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                    </div>
            </spring:bind>
            <spring:bind path="printDate">
                <div class="form-group col-lg-6">    
                    <label for="printDate" style="margin-top:  15px;"><spring:message code="OpenStatements.Label.PrintDate" /></label>            
                    <form:input id="printDate" path="printDate" class="form-control" />
                    <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                    </div>
            </spring:bind>
        </div>
        <div class="form-group">
                <label for="type" style="margin-top:  15px;"><spring:message code="OpenStatements.Label.ShowItems" /></label>        
            <ul style="list-style-type: none;">            
                <form:radiobuttons path="p" 
                                   items="${showItems}" 
                                   element="li"
                                   itemLabel="description" />
            </ul>
        </div>
        <div class="row ">                                
            <spring:bind path="min">
                <div class="form-group col-lg-6 ">                                
                    <label for="amountFrom" ><spring:message code="OpenStatements.Label.AmountFrom" /></label>            
                    <form:input id="amountFrom" path="min" class="form-control" />
                    <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
            </spring:bind>
            <spring:bind path="max">
                <div class="form-group col-lg-6">    
                    <label for="amountTo" ><spring:message code="OpenStatements.Label.AmountTo" /></label>            
                    <form:input id="amountTo" path="max" class="form-control" />
                    <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                    </div>
            </spring:bind>
        </div>
        <div class="form-group">
            <button formtarget="_blank" type="submit" class="btn btn-primary" style="margin-top:  15px;"><spring:message code="OpenStatements.Button.Print" /></button>
        </div>
    </form:form>
</fieldset>
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
                url: '${pageContext.request.contextPath}/open-item-statements/read-client/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#client').bind('typeahead:selected', function (obj, datum, name) {
        $('#clientID').val(datum['id']);
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
                url: '${pageContext.request.contextPath}/open-item-statements/read-orgunit/',
                replace: function (url, query) {
                    if ($('#clientID').val().length === 0) {
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
                url: '${pageContext.request.contextPath}/open-item-statements/read-account/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#businessPartnerName').typeahead({
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/open-item-statements/read-businesspartner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#businessPartnerName').bind('typeahead:selected', function (obj, datum, name) {
        $('#businessPartnerRegNo').val(datum['companyIdNumber']);
    });    
</script>