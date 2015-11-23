<%-- 
    Document   : invoice-table
    Created on : May 30, 2015, 8:11:46 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>
<!-- Search invoices by document, invoiceDate range, business partner -->
<br/>
<form:form class="well well-lg" 
           action="${pageContext.request.contextPath}/invoice/read-page.html"  
           modelAttribute="requestInvoices" 
           method="GET">        
    <div class="form-group row" >
        <div class="col-lg-6" >
            <label for="document"  ><spring:message code="FindInvoice.Label.Document"/></label>
            <form:input id="document" path="document" type="text" class="form-control" />
        </div>
        <div class="col-lg-6">
            <label for="partner" ><spring:message code="FindInvoice.Label.Partner" /></label>
            <form:input id="partner" class="typeahead form-control" type="text" path="partnerName" />
            <form:input id="partner-hidden" type="hidden" path="partnerId"/>
        </div> 

    </div>
    <div class="form-group row">
        <div class="col-lg-6" >
            <spring:bind path="dateFrom" >                    
                <label for="dateFrom"><spring:message code="FindInvoice.Label.DateFrom" /></label>
                <form:input id="dateFrom" type="text" class="form-control" path="dateFrom" />
                <span class="help-inline">
                    <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                    </span>
            </spring:bind>
        </div>
        <div class="col-lg-6" >
            <spring:bind path="dateTo" >                    
                <label for="dateTo"><spring:message code="FindInvoice.Label.DateTo" /></label>
                <form:input id="dateTo" type="text" class="form-control" path="dateTo"/>
                <span class="help-inline">
                    <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                    </span>
            </spring:bind>
        </div>
    </div>

    <button type="submit" class="btn btn-primary" name="page" value="0">
        <spring:message code="FindInvoice.Button.Search" />
    </button>
</form:form>
<a class="btn btn-primary" href="${page}/create">
    <span class="glyphicon glyphicon-plus"></span> <spring:message code="Invoice.Button.Create" /></a>
<br/>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th></th>
                <th><spring:message code="Invoice.Table.ClientName" /></th>
                <th><spring:message code="Invoice.Table.OrgUnitName" /></th>
                <th><spring:message code="Invoice.Table.Document" /></th>
                <th><spring:message code="Invoice.Table.Partner" /></th>
                <th><spring:message code="Invoice.Table.Status" /></th>
                <th><spring:message code="Invoice.Table.CreditDebitRelationDate" /></th>
                <th><spring:message code="Invoice.Table.InvoiceDate" /></th>
                <th><spring:message code="Invoice.Table.Recorded" /></th>
            </tr>
        </thead>
        <tbody>
            <c:set var="count" value="0" scope="page" />
            <c:forEach var="invoice" items="${data}">
                <!-- Modal -->
            <div class="modal fade" id="dialog${count}" tabindex="-1" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">
                                <spring:message code="Invoice.Delete.Question" 
                                                arguments="${invoice.clientDesc},${invoice.document}"/>
                            </h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                <spring:message code="Invoice.Button.Cancel" /></button>
                            <a type="button" class="btn btn-danger" 
                               href="${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/delete.html">
                                <spring:message code="Invoice.Button.Delete" /></a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group" >
                        
                        <a href="${pageContext.request.contextPath}/invoice/update.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&page=${page}" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span class="glyphicon glyphicon-trash"></span> brisanje</button>
                    </div>
                </td>
                <td><c:out value="${invoice.clientDesc}"/></td>
                <td><c:out value="${invoice.orgUnitDesc}"/></td>
                <td><c:out value="${invoice.document}"/></td>
                <td><c:out value="${invoice.partnerName}"/></td>
                <td><c:out value="${invoice.proForma.description}"/></td>
                <td><spring:eval expression="invoice.creditRelationDate" /></td>
                <td><spring:eval expression="invoice.invoiceDate" /></td>
                <td>
                    <c:choose>
                        <c:when test="${invoice.recorded}" >
                            <spring:message code="Invoice.Table.Recorded" />
                        </c:when>
                        <c:otherwise>
                            <spring:message code="Invoice.Table.NotRecorded" />
                        </c:otherwise>
                    </c:choose> 
                </td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">                       
        <spring:message code="Invoice.Table.Page" />
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}">
                   <c:out value="${pageContext.request.contextPath}/invoice/read-page.html?document=${param['document']}&partnerName=${param['partnerName']}&partnerId=${param['partnerId']}&dateFrom=${param['dateFrom']}&dateTo=${param['dateTo']}&page=${page-1}" /></c:if>">
                   <span class="glyphicon glyphicon-backward"></span> 
               <spring:message code="Invoice.Table.PrevPage" />
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}" />
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}">
                   <c:out value="${pageContext.request.contextPath}/invoice/read-page.html?document=${param['document']}&partnerName=${param['partnerName']}&partnerId=${param['partnerId']}&dateFrom=${param['dateFrom']}&dateTo=${param['dateTo']}&page=${page+1}"/></c:if>" >
                   <span class="glyphicon glyphicon-forward"></span> 
               <spring:message code="Invoice.Table.NextPage" />
            </a>
        </li>
    </ul>
</nav>
<script type="text/javascript">
    $('#dateFrom').datepicker({});
    $('#dateTo').datepicker({});
    $('#partner').typeahead({
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
    $('#partner').bind('typeahead:selected', function (obj, datum, name) {
        $('#partner-hidden').val(datum['id']);
    });
</script>