<%-- 
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- Search items by code and name -->
<form class=" well well-lg form-inline" 
      action="${pageContext.request.contextPath}/item/read-page.html"  
      method="get">
    <div class="form-group" >
            <label for="code" class="sr-only"  ><spring:message code="Article.Label.SearchCode"/></label>
            <input id="code" name="code" placeholder="<spring:message code="Article.Label.SearchCode"/>" 
                   type="text" class="form-control" value="${param['code']}"/>
    </div>
    <div class="form-group">
        <label for="name" class="sr-only"><spring:message code="Article.Label.SearchDesc" /></label>
        <input id="name" class="form-control" type="text" name="name" value="${param['name']}"
                placeholder="<spring:message code="Article.Label.SearchDesc" />" />
    </div> 
    <button type="submit" class="btn btn-primary" name="page" value="0"><spring:message code="FindInvoice.Button.Search" /></button>
</form>
<a class="btn btn-primary" href="${page}/create" ><span class="glyphicon glyphicon-plus"></span> 
    <spring:message code="Article.Button.Create" /></a>
<br/>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th></th>
                <th><spring:message code="Article.Table.Code" /></th>
                <th><spring:message code="Article.Table.Desc" /></th>
                <th><spring:message code="Article.Table.VAT" /></th>
                <th><spring:message code="Article.Table.Unit" /></th>
                <th><spring:message code="Article.Table.LastUpdateBy" /></th>
                <th><spring:message code="Article.Table.Updated" /></th>
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
                                <spring:message code="Article.DeleteQuestion" arguments="${item.description}" />
                            </h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                <spring:message code="Article.Button.Cancel" /></button>
                                <spring:url value="${page}/${item.code}/delete.html" var="deletehref"/>
                            <a type="button" class="btn btn-danger" href="${deletehref}">
                                <spring:message code="Article.Button.Delete" /></a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group" >
                        <a href="${page}/update/${item.code}" class="btn btn-primary">
                            <span class="glyphicon glyphicon-search"></span> 
                            <spring:message code="Article.Button.ReadRow" />
                        </a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}">
                            <span class="glyphicon glyphicon-trash"></span> 
                            <spring:message code="Article.Button.DeleteRow" />                            
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.code}"/></td>
                <td><c:out value="${item.description}"/></td>
                <td><c:out value="${item.VATRate.description}"/></td>
                <td><c:out value="${item.unitOfMeasureCode}"/></td>
                <td><c:out value="${item.lastUpdateBy.username}"/></td>
                <td><spring:eval expression="item.updated" /></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>                   
<nav>
    <ul class="pager pull-right">                
        <spring:message code="Article.Table.Page" />        
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}">
                   <c:out value="${pageContext.request.contextPath}/item/read-page.html?code=${param['code']}&name=${param['name']}&page=${page-1}"/></c:if>">
                   <span class="glyphicon glyphicon-backward"></span> 
               <spring:message code="Article.Table.PrevPage" />
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}" />
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}">
                   <c:out value="${pageContext.request.contextPath}/item/read-page.html?code=${param['code']}&name=${param['name']}&page=${page+1}"/></c:if>" >
                   <span class="glyphicon glyphicon-forward"></span> 
               <spring:message code="Article.Table.NextPage" />
            </a>
        </li>
    </ul>
</nav>