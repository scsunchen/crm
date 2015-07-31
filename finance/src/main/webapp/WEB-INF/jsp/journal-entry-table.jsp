<%-- 
    Document   : journal-entry
    Created on : Jul 20, 2015, 11:56:14 AM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<a class="btn btn-primary" href="${page}/create.html" ><span class="glyphicon glyphicon-plus"></span> 
    <spring:message code="JournalEntry.Button.Create" /></a>
<br/>
<br/>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th></th>
                <th><spring:message code="JournalEntry.Table.ClientName" /></th>
                <th><spring:message code="JournalEntry.Table.TypeName" /></th>
                <th><spring:message code="JournalEntry.Table.JournalEntryNumber" /></th>
                <th><spring:message code="JournalEntry.Table.Date" /></th>
                <th><spring:message code="JournalEntry.Table.Posted" /></th>
                <th><spring:message code="JournalEntry.Table.Balance" /></th>
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
                                <spring:message code="JournalEntry.DeleteQuestion" 
                                                arguments="${item.clientName},${item.typeName},${item.journalEntryNumber}"/>
                            </h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                <spring:message code="JournalEntry.Button.Cancel" /></button>
                            <spring:url value="${page}/${item.clientId}/${item.typeId}/${item.journalEntryNumber}/delete.html" var="deletehref"/>
                            <a type="button" class="btn btn-danger" href="${deletehref}">
                                <spring:message code="JournalEntry.Button.Delete" /></a>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Modal -->
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group" >
                        <a href="${page}/${item.clientId}/${item.typeId}/${item.journalEntryNumber}/update.html" class="btn btn-primary">
                            <span class="glyphicon glyphicon-search"></span> 
                            <spring:message code="JournalEntry.Button.ReadRow" />
                        </a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}">
                            <span class="glyphicon glyphicon-trash"></span> 
                            <spring:message code="JournalEntry.Button.DeleteRow" />                            
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.clientName}"/></td>
                <td><c:out value="${item.typeName}"/></td>
                <td><c:out value="${item.journalEntryNumber}"/></td>
                <td><spring:eval expression="item.recordDate"/></td>
                <td><c:choose>
                <c:when test="${item.isPosted == true}">
                    <spring:message code="JournalEntry.Table.True" />
                </c:when>
                <c:otherwise>
                    <spring:message code="JournalEntry.Table.False" />
                </c:otherwise>
            </c:choose></td>
                <td><spring:eval expression="item.balance"/></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">                       
        <spring:message code="JournalEntry.Table.Page" />
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}" /></c:if>">
                    <span class="glyphicon glyphicon-backward"></span> 
                <spring:message code="JournalEntry.Table.PrevPage" />
            </a>
        </li>
        <spring:message code="JournalEntry.Table.PageRange" arguments="${page+1}, ${numberOfPages+1}" />            
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>" >
                    <span class="glyphicon glyphicon-forward"></span> 
                <spring:message code="JournalEntry.Table.NextPage" />
            </a>
        </li>
    </ul>
</nav>
