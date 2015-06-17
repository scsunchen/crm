<%-- 
    Document   : invoice-master-grid
    Created on : Jun 16, 2015, 8:59:15 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<form:form modelAttribute="invoice" method="post"
           action="${pageContext.request.contextPath}/invoice/${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/update.html">
    <div class="col-lg-6" >
        <div class="form-group " >
            <label for="client"><spring:message code="Invoice.Label.Client" /></label>
            <form:input id="client" class="form-control" type="text" 
                        path="clientDesc" 
                        style="margin-bottom: 15px;" 
                        disabled="true"/>
            <form:input id="client-hidden" type="hidden" path="clientId"/>
        </div>
        <div class="form-group" >
            <label for="orgUnit" ><spring:message code="Invoice.Label.OrgUnit" /></label>
            <form:input id="orgUnit" class="form-control" type="text" 
                        path="orgUnitDesc" style="margin-bottom: 15px;" 
                        disabled="true"/>
            <form:input id="orgUnit-hidden" type="hidden" path="orgUnitId"/>
        </div>
        <i:textField label="Invoice.Label.Document" name="document" disabled="true"/>
        <div class="form-group" >
            <label for="businessPartner"><spring:message code="Invoice.Label.Partner" /></label>
            <form:input id="businessPartner" class="typeahead form-control" 
                        type="text" path="partnerName" style="margin-bottom: 15px;"/>
            <form:input id="businessPartner-hidden" type="hidden" 
                        path="partnerID"/>
        </div>
        <div class="form-group ">
            <label for="bank"><spring:message code="Invoice.Label.Bank" /></label>
            <form:input id="bank" class="typeahead form-control" 
                        type="text" path="bankName" style="margin-bottom: 15px;"/>
            <form:input id="bank-hidden" type="hidden" path="bankID"/>
        </div>
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="currency"><spring:message code="Invoice.Label.Currency" /></label>
                <form:input id="currency" class="typeahead form-control" 
                            type="text" path="currencyISOCode" />
            </div>
        </div>
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="partnerType" ><spring:message code="Invoice.Label.PartnerType" /></label>   
                <form:select id="partnerType" class="form-control" disabled="true"
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
        <button type="submit" class="btn btn-default" ><span class="glyphicon glyphicon-pencil"></span>
            <c:choose>
                <c:when test="${action == 'create'}">
                    <spring:message code="Invoice.Button.Create" />
                </c:when>
                <c:otherwise>
                    <spring:message code="Invoice.Button.Update" />
                </c:otherwise>
            </c:choose>
        </button>
    </div>
</form:form>
<script type="text/javascript">
    $('#businessPartner').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-businesspartner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#businessPartner').bind('typeahead:selected', function (obj, datum, name) {
        $('#businessPartner-hidden').val(datum['companyIdNumber']);
    });
    $('#bank').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-bank/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#bank').bind('typeahead:selected', function (obj, datum, name) {
        $('#bank-hidden').val(datum['id']);
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
                url: '${pageContext.request.contextPath}/invoice/read-currency/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
</script>
