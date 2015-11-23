<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>
<script src="${pageContext.request.contextPath}/resources/js/handlebars-v3.0.3.js"></script>
<div class="modal fade" id="addItemDialog" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <c:if test = "${itemException != null}">
                    <div class="alert alert-warning">
                        <a href="#" class="close" data-dismiss="alert">&times;</a>
                        ${itemException.message}
                    </div>
                </c:if>
                <spring:url value="/terms/${page}/${itemsPage}/${terms.id}/add-item.html" var="addItemHRef" />
                <form:form action="${addItemHRef}" method="post" modelAttribute="termsItemToAdd" >
                    <form:hidden path="terms.id" ></form:hidden>
                    <form:hidden path="terms.version" ></form:hidden>
                        <div class="form-group" >
                            <label for="serviceCode">
                                <spring:message code="BusinessTermsItem.Label.Service" />
                            </label>
                            <form:input id="serviceCode" class="typeahead form-control" 
                                        type="text" path="article.code" />
                        </div>
                    <spring:bind path="totalAmount">
                        <div class="form-group">
                            <label style="margin-top: 15px;" for ="totalAmount" >
                                <spring:message code="BusinessTermsItem.Label.TotalAmount" />
                            </label>
                            <form:input id="totalAmount" path="totalAmount" class="form-control" />
                            <span class="help-inline">
                                <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                                </span>
                            </div>
                    </spring:bind>
                    <spring:bind path="totalQuantity">
                        <div class="form-group">
                            <label for ="totalQuantity" >
                                <spring:message code="BusinessTermsItem.Label.TotalQuantity" />
                            </label>
                            <form:input id="totalQuantity" path="totalQuantity" class="form-control" />
                            <span class="help-inline">
                                <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                                </span>
                            </div>
                    </spring:bind>
                    <spring:bind path="rebate">
                        <div class="form-group">
                            <label for="rebate" >
                                <spring:message code="BusinessTermsItem.Label.Rebate" />
                            </label>
                            <form:input id="rebate" path="rebate" class="form-control"/>                    
                            <span class="help-inline">
                                <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                                </span>
                            </div>
                    </spring:bind>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <spring:message code="BusinessTermsItem.Button.Cancel" /></button>                                             
                    <button type="submit" class="btn btn-primary">
                        <spring:message code="BusinessTermsItem.Button.AddItem" />
                    </button>
                </div>
            </div>
        </form:form>
    </div>
</div>
<c:if test="${showDialog}" >
    <script type="text/javascript">
        $('#addItemDialog').modal('show');
    </script>
</c:if>      
<label>${terms.businessPartner.name}</label>
<br/>
<br/>
<a data-toggle="modal" data-target="#addItemDialog" class="btn btn-primary" >
    <span class="glyphicon glyphicon-plus"></span><spring:message code="BusinessTermsItem.Button.AddItem" /></a>
<br/>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th></th>
                <th><spring:message code="BusinessTermsItem.Table.ServiceCode" /></th>
                <th><spring:message code="BusinessTermsItem.Table.ServiceDesc" /></th>
                <th><spring:message code="BusinessTermsItem.Table.TotalAmount" /></th>
                <th><spring:message code="BusinessTermsItem.Table.TotalQuantity" /></th>
                <th><spring:message code="BusinessTermsItem.Table.Rebate" /></th>
            </tr>
        </thead>
        <tbody>            
            <c:set var="count" value="0" scope="page" />
            <c:forEach var="item" items="${items}">
            <div class="modal fade" id="removeDialog${item.ordinal}" tabindex="-1" role="dialog">
                <div class="modal-dialog">
                    <spring:url value="/terms/${page}/${itemsPage}/${terms.id}/${item.ordinal}/${terms.version}/remove-item.html" 
                                var="deleteItemHRef" />
                    <form:form action="${deleteItemHRef}" method="post" >
                        <div class="modal-content">
                            <div class="modal-body">
                                <button type="button" class="close" data-dismiss="modal">
                                    <span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title">
                                    <spring:message code="BusinessTermsItem.DeleteQuestion" 
                                                    arguments="${item.article.description}" />
                                </h4>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">
                                    <spring:message code="BusinessTermsItem.Button.Cancel" /></button>                                             
                                <button type="submit" class="btn btn-danger">
                                    <spring:message code="BusinessTermsItem.Button.Delete" />
                                </button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
            <tr>
                <td>
                    <button class="btn btn-danger btn-sm" data-toggle="modal" 
                            data-target="#removeDialog${item.ordinal}">
                        <span class="glyphicon glyphicon-trash"></span> <spring:message code="BusinessTermsItem.Button.RemoveItem" /></button>
                </td>
                <td>${item.article.code}</td>
                <td>${item.article.description}</td>
                <td><spring:eval expression="item.totalAmount"/></td>                                 
                <td><spring:eval expression="item.totalQuantity"/></td>
                <td><spring:eval expression="item.rebate"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">
        <spring:message code="BusinessPartnerTerms.Table.Page"/>
        <li class="<c:if test="${itemsPage == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${itemsPage > 0}">
               <c:out value="${pageContext.request.contextPath}/terms/${page}/${itemsPage - 1}/${terms.id}/details.html"/>
               </c:if>">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="BusinessPartnerTerms.Table.PrevPage"/>
            </a>
        </li>
        <spring:message code="BusinessPartnerTerms.Table.PageRange" arguments="${itemsPage+1}, ${numberOfPages+1}" />
        <li class="<c:if test="${itemsPage == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${itemsPage < numberOfPages}">
                   <c:out value="${pageContext.request.contextPath}/terms/${page}/${itemsPage + 1}/${terms.id}/details.html"/>
               </c:if>">
                <span class="glyphicon glyphicon-forward"></span>
                <spring:message code="BusinessPartnerTerms.Table.NextPage"/>
            </a>
        </li>
    </ul>
</nav>
<script>
     $('#serviceCode').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'code',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/terms/read-service/%QUERY',
                wildcard: '%QUERY'
            }
        }),
        templates: {
            suggestion: Handlebars.compile('<div><strong>{{code}}</strong> &nbsp; {{description}}</div>')
        }
    });
</script>
