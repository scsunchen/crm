<%-- 
    Document   : desc-table
    Created on : Jul 18, 2015, 6:07:17 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<a class="btn btn-primary" href="${page}/create.html" ><span class="glyphicon glyphicon-plus"></span> 
    <spring:message code="JournalEntryType.Button.Create" /></a>
<br/>
<br/>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th></th>
                <th><spring:message code="JournalEntryType.Table.Client" /></th>
                <th><spring:message code="JournalEntryType.Table.Desc" /></th>
                <th><spring:message code="JournalEntryType.Table.Number" /></th>
            </tr>
        </thead>
        <tbody>
            <c:set var="count" value="0" scope="page" />
            <c:forEach var="item" items="${data}">
                <div class="modal fade" id="dialog${count}" tabindex="-1" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">
                                <spring:message code="JournalEntryType.DeleteQuestion" arguments="${item.name}" />
                            </h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                <spring:message code="JournalEntryType.Button.Cancel" /></button>
                            <spring:url value="${page}/${item.clientId}/${item.typeId}/delete.html" 
                                        var="deletehref"/>
                            <a type="button" class="btn btn-danger" href="${deletehref}">
                                <spring:message code="JournalEntryType.Button.Delete" /></a>
                        </div>
                    </div>
                </div>
            </div>
                <!-- Modal -->
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group" >
                        <a href="${page}/${item.clientId}/${item.typeId}/update.html" 
                           class="btn btn-primary">
                            <span class="glyphicon glyphicon-search"></span> 
                            <spring:message code="JournalEntryType.Button.ReadRow" />
                        </a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}">
                            <span class="glyphicon glyphicon-trash"></span> 
                            <spring:message code="JournalEntryType.Button.DeleteRow" />                            
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.clientName}"/></td>
                <td><c:out value="${item.name}"/></td>
                <td><c:out value="${item.journalEntryNumber}"/></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">                       
        <spring:message code="JournalEntryType.Table.Page" />
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}" /></c:if>">
                    <span class="glyphicon glyphicon-backward"></span> 
                <spring:message code="JournalEntryType.Table.PrevPage" />
            </a>
        </li>
        <spring:message code="JournalEntryType.Table.PageRange" arguments="${page+1}, ${numberOfPages+1}" />            
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>" >
                    <span class="glyphicon glyphicon-forward"></span> 
                <spring:message code="JournalEntryType.Table.NextPage" />
            </a>
        </li>
    </ul>
</nav>