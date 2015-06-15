<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>

<a href="${pageContext.request.contextPath}/invoice/${page}/create" 
   class="btn btn-default" 
   ><span class="glyphicon glyphicon-plus"></span>Kreiraj</a>
<div class="modal fade" id="dialogDelete" tabindex="-1" role="dialog" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Da li ste sigurni da želite da obrišete fakturu za preduzeće ${invoice.clientDesc} sa dokumentom ${invoice.document}?</h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Odustani</button>
                <a  class="btn btn-danger" href="${pageContext.request.contextPath}/invoice/${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/delete.html">Obriši</a>
            </div>
        </div>
    </div>
</div>
<button class="btn btn-default" data-toggle="modal" data-target="#dialogDelete">
    <span class="glyphicon glyphicon-trash"></span> Brisanje</button>
<a href=
   "${pageContext.request.contextPath}/invoice/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/print-preview.html" 
   class="btn btn-default" 
   target="_blank">
    <span class="glyphicon glyphicon-search"></span>Pregled štampe</a>
<br/>
<br/>
<form:form modelAttribute="invoice" method="post"
           action="${pageContext.request.contextPath}/invoice/${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/update.html">
    <fieldset>
        <legend class="control-label"></legend>
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
    </fieldset>
</form:form>
<!--SPORNO*******************************************************************-->
<legend class="control-label">Stavke fakture</legend>
<div class="col-lg-12">        
    <form:form modelAttribute="item" method="post" 
               action="${pageContext.request.contextPath}/invoice/${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/addItem.html" >
        <div class="modal fade" id="dialogAddItem" tabindex="-1" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">

                        <form:hidden path="clientId" /> 
                        <form:hidden path="unitId" /> 
                        <form:hidden path="invoiceDocument" />                         
                        <div class="form-group" >
                            <label for="itemDesc"><spring:message code="InvoiceItems.Label.Article" /></label>
                            <form:input id="itemDesc" class="typeahead form-control" type="text" 
                                        path="articleDesc" style="margin-bottom:  15px;"/>
                            <form:hidden id="itemDescHidden" path="articleCode"/>
                        </div>
                        <div class=" row " >                                
                            <spring:bind path="quantity">
                                <div class="form-group  col-lg-6">                                
                                    <label for="quantity" ><spring:message code="InvoiceItems.Label.Quantity" /></label>            
                                    <form:input id="quantity" path="quantity" 
                                                class="form-control" />
                                    <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                                    </div>
                            </spring:bind>
                            <spring:bind path="rabatPercent">
                                <div class="form-group col-lg-6 ">                                
                                    <label for="rabatPercent" ><spring:message code="InvoiceItems.Label.Rabat"  /></label>            
                                    <form:input id="rabatPercent" path="rabatPercent" 
                                                class="form-control" />
                                    <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                                    </div>
                            </spring:bind>
                        </div>
                        <spring:bind path="netPrice">
                            <div class="form-group row ">                                
                                <div class="col-lg-6">    
                                    <label for="netPrice" ><spring:message code="InvoiceItems.Label.Price" /></label>            
                                    <form:input id="netPrice" path="netPrice" 
                                                class="form-control" />
                                    <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                                    </div>
                                </div>
                        </spring:bind>
                    </div>
                    <div class="modal-footer">
                        <button  class="btn btn-default" data-dismiss="modal">Odustani</button>
                        <button class="btn btn-primary" type="submit">Dodaj</button>
                    </div>
                </div>
            </div>
        </div>
    </form:form>                
    <a data-toggle="modal" data-target="#dialogAddItem" class="btn btn-primary" >
        <span class="glyphicon glyphicon-plus"></span>Dodaj</a>
    <br/>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th></th>
                    <th><spring:message code="InvoiceItems.Table.Ordinal" /></th>
                    <th><spring:message code="InvoiceItems.Table.Article" /></th>
                    <th><spring:message code="InvoiceItems.Table.Quantity" /></th>
                    <th><spring:message code="InvoiceItems.Table.NetPrice" /></th>
                    <th><spring:message code="InvoiceItems.Table.VatPercent" /></th>
                    <th><spring:message code="InvoiceItems.Table.RabatPercent" /></th>
                    <th><spring:message code="InvoiceItems.Table.TotalCost" /></th>
                </tr>
            </thead>
            <tbody>            
                <c:set var="count" value="0" scope="page" />
                <c:forEach var="item" items="${items}">
                <!--DELETE DIALOG********************************************-->
                <div class="modal fade" id="dialog${item.ordinal}" tabindex="-1" role="dialog" >
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-body">
                                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel">Da li ste sigurni da želite da obrišete stavku fakture sa rednim brojem ${item.ordinal} za artikal ${item.articleDesc}?</h4>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Odustani</button>
                                <a type="button" class="btn btn-danger" href="${pageContext.request.contextPath}/invoice/${page}/${item.clientId}/${item.unitId}/${item.invoiceDocument}/${item.ordinal}/${invoice.version}/deleteItem.html">Obriši</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!--*********************************************************-->
                <tr>
                    <td>
                    <button class="btn btn-danger btn-sm" data-toggle="modal" data-target="#dialog${item.ordinal}"><span class="glyphicon glyphicon-trash"></span> brisanje</button>
                    </td>
                    <td><spring:eval expression="item.ordinal" /></td>
                    <td><c:out value="${item.articleDesc}"/></td>
                    <td><spring:eval expression="item.quantity" /></td>
                    <td><spring:eval expression="item.netPrice" /></td>
                    <td><spring:eval expression="item.VATPercent" /></td>
                    <td><spring:eval expression="item.rabatPercent" /></td>
                    <td><spring:eval expression="item.totalCost" /></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
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
    $('#itemDesc').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'description',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-item/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#itemDesc').bind('typeahead:selected', function (obj, datum, name) {
        $('#itemDescHidden').val(datum['code']);
    });
</script>
