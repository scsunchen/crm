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
<form:form modelAttribute="invoice" method="post"
           action="${pageContext.request.contextPath}/invoice/${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/update.html">
    <div class="col-lg-6" >
        <div class="form-group">
            <label for="client"><spring:message code="Invoice.Label.Client" /></label>
            <c:choose>
                <c:when test="${invoice.recorded == false}">
                    <form:input id="client" class="form-control" 
                                path="clientDesc" 
                                style="margin-bottom: 15px;" 
                                readonly="true" />
                    <form:input id="client-hidden" type="hidden" path="clientId"/>
                </c:when>
                <c:otherwise>
                    <form:input id="client" class="form-control" 
                                path="clientDesc" 
                                style="margin-bottom: 15px;" 
                                disabled="true" />
                </c:otherwise>
            </c:choose>
        </div>
        <div class="form-group" >
            <label for="orgUnit" ><spring:message code="Invoice.Label.OrgUnit" /></label>
            <c:choose>
                <c:when test="${invoice.recorded == false}">
                    <form:input id="orgUnit" class="form-control" type="text" 
                                path="orgUnitDesc" style="margin-bottom: 15px;" 
                                readonly="true"/>
                    <form:input id="orgUnit-hidden" type="hidden" path="orgUnitId" />
                </c:when>
                <c:otherwise>
                    <form:input id="orgUnit" class="form-control" type="text" 
                                path="orgUnitDesc" style="margin-bottom: 15px;" 
                                disabled="true"/>
                </c:otherwise>
            </c:choose>            
        </div>
        <div class="form-group" >
            <label for="document"  ><spring:message code="Invoice.Label.Document" /></label>
            <c:choose>
                <c:when test="${invoice.recorded == false}">
                    <form:input id="document" class="form-control" path="document" readonly="true"/>
                </c:when>
                <c:otherwise>
                    <form:input id="document" class="form-control" path="document" disabled="true"/>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="form-group" >
            <label for="businessPartner"><spring:message code="Invoice.Label.Partner" /></label>
            <c:choose>
                <c:when test="${invoice.recorded == false}">
                    <form:input id="businessPartner" class="typeahead form-control" 
                                type="text" path="partnerName" />
                    <form:input id="businessPartner-hidden" type="hidden" 
                                path="partnerID"/>
                </c:when>
                <c:otherwise>
                    <form:input id="businessPartner" class="typeahead form-control" 
                                type="text" path="partnerName" disabled="true"/>
                </c:otherwise>
            </c:choose>

        </div>
        <div class="form-group ">
            <label for="bank" style="margin-top: 15px;"><spring:message code="Invoice.Label.Bank" /></label>
            <c:choose>
                <c:when test="${invoice.recorded == false}">
                    <form:input id="bank" class="typeahead form-control" 
                                type="text" path="bankName" />
                    <form:input id="bank-hidden" type="hidden" path="bankID"/>
                </c:when>
                <c:otherwise>
                    <form:input id="bank" class="typeahead form-control" 
                                type="text" path="bankName" disabled="true"/>
                </c:otherwise>
            </c:choose>            
        </div>
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="currency" style="margin-top: 15px;"><spring:message code="Invoice.Label.Currency" /></label>
                <c:choose>
                    <c:when test="${invoice.recorded == false}">
                        <form:input id="currency" class="typeahead form-control" 
                                    type="text" path="currencyISOCode" />
                    </c:when>
                    <c:otherwise>
                        <form:input id="currency" class="typeahead form-control" 
                                    type="text" path="currencyISOCode" disabled="true"/>
                    </c:otherwise>
                </c:choose>
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
                <c:choose>
                    <c:when test="${invoice.recorded == false}">
                        <form:select id="type" class="form-control" 
                                     path="proForma" items="${invoiceTypes}" 
                                     itemLabel="description" />
                    </c:when>
                    <c:otherwise>
                        <form:select id="type" class="form-control" disabled="true"
                                     path="proForma" items="${invoiceTypes}" 
                                     itemLabel="description" />
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <spring:bind path="invoiceDate">
            <div class="form-group row">
                <div class="col-lg-6">                        
                    <label for="invoiceDate" >
                        <spring:message code="Invoice.Label.InvoiceDate"  />
                    </label>            
                    <c:choose>
                        <c:when test="${invoice.recorded == false}">
                            <form:input id="invoiceDate" path="invoiceDate" 
                                        class="form-control" />                    
                        </c:when>
                        <c:otherwise>
                            <form:input id="invoiceDate" path="invoiceDate" 
                                        class="form-control" disabled="true" />                    
                        </c:otherwise>
                    </c:choose>
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
                    <c:choose>
                        <c:when test="${invoice.recorded == false}">
                            <form:input id="creditRelationDate" path="creditRelationDate" 
                                        class="form-control" />                    
                        </c:when>
                        <c:otherwise>
                            <form:input id="creditRelationDate" path="creditRelationDate" 
                                        class="form-control" disabled="true"/>                    
                        </c:otherwise>
                    </c:choose>
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
                    <c:choose>
                        <c:when test="${invoice.recorded == false}">
                            <form:input id="valueDate" path="valueDate" 
                                        class="form-control" />                    
                        </c:when>
                        <c:otherwise>
                            <form:input id="valueDate" path="valueDate" 
                                        class="form-control" disabled="true"/>                    
                        </c:otherwise>
                    </c:choose>
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
                    <c:choose>
                        <c:when test="${invoice.recorded == false}">
                            <form:input id="contractDate" path="contractDate" 
                                        class="form-control" />                    
                        </c:when>
                        <c:otherwise>
                            <form:input id="contractDate" path="contractDate" 
                                        class="form-control" disabled="true"/>                    
                        </c:otherwise>
                    </c:choose>
                    <span class="help-inline">
                        <c:if test="${status.error}">
                            <c:out value="${status.errorMessage}" />
                        </c:if>
                    </span>
                </div>
            </div>
        </spring:bind>
        <c:choose>
            <c:when test="${invoice.recorded == false}">
                <i:textField label="Invoice.Label.ContractNumber" name="contractNumber" />
            </c:when>
            <c:otherwise>
                <i:textField label="Invoice.Label.ContractNumber" name="contractNumber" disabled="true"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${invoice.recorded == false}">
                <div class="checkbox"><label><form:checkbox id="paid" path="paid"  />
                        <spring:message code="Invoice.Label.Paid" /></label></div>
            </c:when>
            <c:otherwise>
                <div class="checkbox"><label><form:checkbox id="paid" path="paid" disabled="true" />
                        <spring:message code="Invoice.Label.Paid" /></label></div>
            </c:otherwise>
        </c:choose>
                <form:hidden path="version" />
    </div>

    <div class="col-lg-12 form-group">
        <button type="submit" class="btn btn-default" <c:if test="${invoice.recorded == true}">disabled</c:if>><span class="glyphicon glyphicon-pencil"></span><spring:message code="Invoice.Button.Update" /></button>
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
        $('#businessPartner-hidden').val(datum['id']);
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
