<%-- 
    Document   : service-provider-services-table
    Created on : Sep 16, 2015, 1:08:21 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<spring:url value="/service-provider-services/${page}/create.html" 
                    var="createHRef"/>
<a class="btn btn-primary" href="${createHRef}" ><span class="glyphicon glyphicon-plus"></span> 
    
    <spring:message code="ServiceProviderServices.Button.Create" /></a>
<br/>
<br/>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th></th>
                <th><spring:message code="ServiceProviderServices.Table.ServiceProvider"/></th>
                <th><spring:message code="ServiceProviderServices.Table.Description"/></th>
                <th><spring:message code="ServiceProviderServices.Table.ServiceCode"/></th>
                <th><spring:message code="ServiceProviderServices.Table.ServiceDescription"/></th>
                <th><spring:message code="ServiceProviderServices.Table.DateFrom" /></th>
                <th><spring:message code="ServiceProviderServices.Table.DateTo" /></th>
            </tr>
        </thead>
        <tbody>
            <c:set var="count" value="0" scope="page" />
            <c:forEach var="item" items="${data}">
                <!-- Modal -->
            <div class="modal fade" id="dialog${count}" tabindex="-1" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">
                                <spring:message code="ServiceProviderServices.DeleteQuestion" 
                                                arguments="${item.description}" />
                            </h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                            <spring:message code="ServiceProviderServices.Button.Cancel" /></button>
                            <spring:url value="/service-provider-services/${page}/${item.id}/delete.html" 
                                        var="deletehref"/>
                            <a type="button" class="btn btn-danger" href="${deletehref}">
                                <spring:message code="ServiceProviderServices.Button.Delete" /></a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group" >
                        <spring:url value="/service-provider-services/${page}/${item.id}/update.html" 
                                        var="readhref"/>
                        <a href="${readhref}" class="btn btn-primary">
                            <span class="glyphicon glyphicon-search"></span> 
                            <spring:message code="ServiceProviderServices.Button.ReadRow" />
                        </a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}">
                            <span class="glyphicon glyphicon-trash"></span> 
                            <spring:message code="ServiceProviderServices.Button.DeleteRow" />                            
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.serviceProvider.name}"/></td>
                <td><c:out value="${item.description}"/></td>
                <td><c:out value="${item.service.code}"/></td>
                <td><c:out value="${item.service.description}"/></td>
                <td><spring:eval expression="item.dateFrom"/></td>
                <td><spring:eval expression="item.dateTo"/></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">                       
        <spring:message code="ServiceProviderServices.Table.Page" />
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}" /></c:if>">
                    <span class="glyphicon glyphicon-backward"></span> 
                    <spring:message code="ServiceProviderServices.Table.PrevPage" />
                </a>
            </li>
        <spring:message code="ServiceProviderServices.Table.PageRange" arguments="${page+1}, ${numberOfPages+1}" /> 
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>" >
                <span class="glyphicon glyphicon-forward"></span> 
                    <spring:message code="ServiceProviderServices.Table.NextPage" />
            </a>
        </li>
    </ul>
</nav>