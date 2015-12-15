<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>
<div class="col-lg-12">        
    <div class="modal fade" id="dialogAddItem" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <form:form modelAttribute="invoiceItem" 
                       method="POST" 
                       action="${pageContext.request.contextPath}/invoice/${page}/${itemsPage}/addItem.html" >
                <div class="modal-content">
                    <div class="modal-body">
                        <c:if test = "${addItemException != null}">
                            <div class="alert alert-warning">
                                <a href="#" class="close" data-dismiss="alert">&times;</a>
                                ${addItemException.message}
                            </div>
                        </c:if>
                        <form:hidden path="clientId" /> 
                        <form:hidden path="unitId" /> 
                        <form:hidden path="invoiceDocument" />                         
                        <form:hidden path="invoiceVersion" />                         
                        <div class="form-group" >
                            <label for="itemCode"><spring:message code="InvoiceItems.Label.Article" /></label>
                            <form:input id="itemCode" class="typeahead form-control" type="text" 
                                        path="articleCode" style="margin-bottom:  15px;"/>
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
                        <button  class="btn btn-default" data-dismiss="modal"><spring:message code="Invoice.Button.Cancel" /></button>
                        <button class="btn btn-primary" type="submit"><spring:message code="Invoice.Button.AddItem" /></button>
                    </div>
                </div>
            </form:form> 
        </div>
    </div>
    <c:if test="${showDialog}" >
        <script type="text/javascript">
            $('#dialogAddItem').modal('show');
        </script>
    </c:if>      
    <label>${invoice.clientDesc}</label>
    <label>${invoice.orgUnitDesc}</label>
    <br/>
    <label>${invoice.document}</label>
    <br/>
    <br/>
    <button data-toggle="modal" data-target="#dialogAddItem" class="btn btn-primary" <c:if test="${invoice.recorded == true}">disabled</c:if>>
        <span class="glyphicon glyphicon-plus"></span><spring:message code="Invoice.Button.AddItem" /></button>
    <br/>
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th></th>
                    <th><spring:message code="InvoiceItems.Table.Ordinal" /></th>
                    <th><spring:message code="InvoiceItems.Table.ArticleCode" /></th>
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
                <div class="modal fade" id="dialog${item.ordinal}" 
                     tabindex="-1" role="dialog" >
                    <div class="modal-dialog">
                        <form method="POST"                          
                              action="${pageContext.request.contextPath}/invoice/deleteItem.html" >
                            <div class="modal-content">
                                <div class="modal-body">
                                    <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                                    <h4 class="modal-title" id="myModalLabel">
                                        <spring:message code="InvoiceItems.Delete.Question" 
                                                        arguments="${item.ordinal},${item.articleDesc}" />
                                    </h4>
                                    <input type="hidden" value="${item.clientId}" 
                                           name="clientId" />
                                    <input type="hidden" value="${item.unitId}" 
                                           name="unitId" />
                                    <input type="hidden" 
                                           value="${item.invoiceDocument}" 
                                           name="invoiceDocument" />
                                    <input type="hidden" 
                                           value="${item.ordinal}" 
                                           name="ordinal" />
                                    <input type="hidden" 
                                           value="${item.invoiceVersion}" 
                                           name="version" />
                                    <input type="hidden" 
                                           value="${page}" 
                                           name="page" />
                                    <input type="hidden" 
                                           value="${itemsPage}" 
                                           name="itemsPage" />
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">
                                        <spring:message code="Invoice.Button.Cancel" /></button>
                                    <button type="submit" class="btn btn-danger">
                                        <spring:message code="Invoice.Button.Delete" />
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <!--*********************************************************-->
                <tr>
                    <td>
                        <button class="btn btn-danger btn-sm" <c:if test="${invoice.recorded == true}">disabled="true"</c:if> 
                                data-toggle="modal" data-target="#dialog${item.ordinal}">
                            <span class="glyphicon glyphicon-trash"></span> 
                            <spring:message code="Invoice.Button.DeleteRow" />
                        </button>
                    </td>
                    <td><spring:eval expression="item.ordinal" /></td>
                    <td><c:out value="${item.articleCode}"/></td>
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
    <nav>
        <ul class="pager pull-right">                       
            <spring:message code="Invoice.Table.Page" />
            <li class="<c:if test="${itemsPage == 0}"><c:out value="disabled" /></c:if>">
                <a href="<c:if test="${itemsPage > 0}">
                       <c:out value="${pageContext.request.contextPath}/invoice/details.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&page=${page}&itemsPage=${itemsPage-1}" /></c:if>">
                       <span class="glyphicon glyphicon-backward"></span> 
                   <spring:message code="Invoice.Table.PrevPage" />
                </a>
            </li>
            <c:out value="${itemsPage+1} od ${numberOfPages+1}" />
            <li class="<c:if test="${itemsPage == numberOfPages}"><c:out value="disabled"/></c:if>">
                <a href="<c:if test="${itemsPage < numberOfPages}">
                       <c:out value="${pageContext.request.contextPath}/invoice/details.html?clientId=${invoice.clientId}&unitId=${invoice.orgUnitId}&document=${invoice.document}&page=${page}&itemsPage=${itemsPage+1}"/></c:if>" >
                       <span class="glyphicon glyphicon-forward"></span> 
                   <spring:message code="Invoice.Table.NextPage" />
                </a>
            </li>
        </ul>
    </nav>
</div>

<script>
    $('#itemCode').typeahead({
        hint: false,
        highlight: true
    }, {
        limit: 10,
        display: 'code',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/invoice/read-item/%QUERY',
                wildcard: '%QUERY'
            }
        }),
        templates: {
            suggestion: Handlebars.compile('<div><strong>{{code}}</strong> &nbsp; {{description}}</div>')
        }
    });
</script>
