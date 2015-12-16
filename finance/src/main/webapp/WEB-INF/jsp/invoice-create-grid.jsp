<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>

<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<form:form modelAttribute="invoice" method="post">
    <div class="col-lg-6" >
        <div class="form-group " >
            <label for="client"><spring:message code="Invoice.Label.Client" /></label>
            <form:input id="client" class="typeahead form-control" type="text" 
                        path="clientDesc" autofocus="true" />
            <form:input id="client-hidden" type="hidden" path="clientId"/>
        </div>
        <div class="form-group" >
            <label for="orgUnit" style="margin-top: 15px;"><spring:message code="Invoice.Label.OrgUnit" /></label>
            <form:input id="orgUnit" class="typeahead form-control" type="text" 
                        path="orgUnitDesc" />
            <form:input id="orgUnit-hidden" type="hidden" path="orgUnitId"/>
        </div>
         <div class="form-group" >
            <label for="document"  style="margin-top: 15px;"><spring:message code="Invoice.Label.Document" /></label>
            <form:input id="document" class="form-control" path="document" />
        </div>
        <div class="form-group" >
            <label for="businessPartner"><spring:message code="Invoice.Label.Partner" /></label>
            <form:input id="businessPartner" class="typeahead form-control" 
                        type="text" path="partnerName" />
            <form:input id="businessPartner-hidden" type="hidden" 
                        path="partnerID"/>
        </div>
        <div class="form-group ">
            <label for="bank" style="margin-top: 15px;"><spring:message code="Invoice.Label.Bank" /></label>
            <form:input id="bank" class="typeahead form-control" 
                        type="text" path="bankName" />
            <form:input id="bank-hidden" type="hidden" path="bankID"/>
        </div>
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="currency" style="margin-top: 15px;"><spring:message code="Invoice.Label.Currency" /></label>
                <form:input id="currency" class="typeahead form-control" 
                            type="text" path="currencyISOCode"  />
            </div>
        </div>
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="partnerType" ><spring:message code="Invoice.Label.PartnerType" /></label>   
                <form:select id="partnerType" class="form-control" 
                             path="partnerType" items="${partnerTypes}" 
                             itemLabel="description" />
            </div>
        </div>
    </div>
    <div class="col-lg-6" >
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="type"><spring:message code="Invoice.Label.Type" /></label>        
                <form:select id="type" class="form-control" 
                             path="proForma" items="${invoiceTypes}" 
                             itemLabel="description" />
            </div>
        </div>
        <spring:bind path="invoiceDate">
            <div class="form-group row">
                <div class="col-lg-6">                        
                    <label for="invoiceDate" >
                        <spring:message code="Invoice.Label.InvoiceDate"  />
                    </label>            
                    <form:input id="invoiceDate" path="invoiceDate" class="form-control" />                    
                    <span class="help-inline">
                        <c:if test="${status.error}">
                            <c:out value="${status.errorMessage}" />
                        </c:if>
                    </span>
                </div>
            </div>
        </spring:bind>
        <spring:bind path="creditRelationDate">
            <div class="form-group row">
                <div class="col-lg-6">
                    <label for="creditRelationDate" >
                        <spring:message code="Invoice.Label.CreditDate" />
                    </label>            
                    <form:input id="creditRelationDate" path="creditRelationDate" 
                                class="form-control" />                    
                    <span class="help-inline">
                        <c:if test="${status.error}">
                            <c:out value="${status.errorMessage}" />
                        </c:if>
                    </span>
                </div>
            </div>
        </spring:bind>
        <spring:bind path="valueDate">
            <div class="form-group row">
                <div class="col-lg-6">
                    <label for="valueDate" >
                        <spring:message code="Invoice.Label.ValueDate" />
                    </label>            
                    <form:input id="valueDate" path="valueDate" 
                                class="form-control" />                    
                    <span class="help-inline">
                        <c:if test="${status.error}">
                            <c:out value="${status.errorMessage}" />
                        </c:if>
                    </span>
                </div>
            </div>
        </spring:bind>
        <spring:bind path="contractDate">
            <div class="form-group row">
                <div class="col-lg-6">
                    <label for="contractDate" >
                        <spring:message code="Invoice.Label.ContractDate" />
                    </label>            
                    <form:input id="contractDate" path="contractDate" 
                                class="form-control" />                    
                    <span class="help-inline">
                        <c:if test="${status.error}">
                            <c:out value="${status.errorMessage}" />
                        </c:if>
                    </span>
                </div>
            </div>
        </spring:bind>
        <i:textField label="Invoice.Label.ContractNumber" name="contractNumber" />
        <div class="checkbox"><label><form:checkbox id="paid" path="paid"  />
                <spring:message code="Invoice.Label.Paid" /></label></div>
    </div>

    <div class="col-lg-12 form-group">
        <a href="${pageContext.request.contextPath}/invoice/read-page.html?document=&partnerName=&partnerId=&dateFrom=&dateTo=&page=${page}" class="btn btn-default" >
    <span class="glyphicon glyphicon-backward"></span> <spring:message code="Invoice.Button.Back" /></a>
        <button type="submit" class="btn btn-primary" >
            <span class="glyphicon glyphicon-pencil"></span> <spring:message code="Invoice.Button.Create" />
        </button>
    </div>
</form:form>
<script type="text/javascript">
    $('#invoiceDate').datepicker({});
    $('#creditRelationDate').datepicker({});
    $('#valueDate').datepicker({});
    $('#contractDate').datepicker({});
    $('#businessPartner').autocomplete(
            'name',
            '${pageContext.request.contextPath}/invoice/read-businesspartner/%QUERY/20',
            10);
    $('#businessPartner').bind('typeahead:selected', function (obj, datum, name) {
        $('#businessPartner-hidden').val(datum['id']);
    });
    $('#client').autocomplete(
            'name', 
            '${pageContext.request.contextPath}/invoice/read-client/%QUERY');
    $('#client').bind('typeahead:selected', function (obj, datum, name) {
        $('#client-hidden').val(datum['id']);
    });
    $('#orgUnit').typeahead({
        hint: false,
        highlight: true
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-orgunit/',
                replace: function (url, query) {
                    return url.concat(query).concat("/").concat($('#client-hidden').val());
                }
            }
        }),
        templates: {
            suggestion: Handlebars.compile('<div><strong>{{clientName}}</strong> &nbsp; {{name}}</div>')
        }

    });
    $('#orgUnit').bind('typeahead:selected', function (obj, datum, name) {                
        $('#client-hidden').val(datum['clientID']);
        $('#client').val(datum['clientName']);
        $('#orgUnit-hidden').val(datum['id']);
    });
    
    $('#bank').autocomplete(
            'name', 
            '${pageContext.request.contextPath}/invoice/read-bank/%QUERY');
    $('#bank').bind('typeahead:selected', function (obj, datum, name) {
        $('#bank-hidden').val(datum['id']);
    });
    $('#currency').autocomplete(
            'isocode', 
            '${pageContext.request.contextPath}/invoice/read-currency/%QUERY');
</script>
