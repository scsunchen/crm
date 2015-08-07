<%-- 
    Document   : invoice-table
    Created on : May 30, 2015, 8:11:46 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>
<a class="btn btn-primary" href="${page}/create">
    <span class="glyphicon glyphicon-plus"></span> <spring:message code="BusinessPartnerTerms.Button.Create"/></a>
<br/>
<br/>

<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th></th>
            <th><spring:message code="BusinessPartnerTerms.Table.BusinessPartnerName"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.DateFrom"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.EndDate"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.DPO"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.Status"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.Remark"/></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="businessPartnerTerms" items="${data}">
            <!-- Modal -->
            <div class="modal fade" id="dialog${count}" tabindex="-1" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">
                                <spring:message code="BusinessPartnerTerms.Delete.Question"
                                                arguments="${businessPartnerTerms.businessPartnerName}"/>
                            </h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                <spring:message code="BusinessPartnerTerms.Button.Cancel"/></button>
                            <a type="button" class="btn btn-danger"
                               href="${page}/${businessPartnerTerms.id}/delete.html">
                                <spring:message code="BusinessPartnerTerms.Button.Delete"/></a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <a href="${page}/update/${businessPartnerTerms.id}" class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> brisanje
                        </button>
                    </div>
                </td>
                <td><c:out value="${businessPartnerTerms.businessPartnerName}"/></td>
                <td><input:displayDate value="${businessPartnerTerms.dateFrom}"/></td>
                <td><input:displayDate value="${businessPartnerTerms.endDate}"/></td>
                <td><c:out value="${businessPartnerTerms.daysToPay}"/></td>
                <td><c:out value="${businessPartnerTerms.status}"/></td>
                <td><c:out value="${businessPartnerTerms.remark}"/></td>

            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">
        <spring:message code="BusinessPartnerTerms.Table.Page"/>
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}" /></c:if>">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="BusinessPartnerTerms.Table.PrevPage"/>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span>
                <spring:message code="BusinessPartnerTerms.Table.NextPage"/>
            </a>
        </li>
    </ul>
</nav>