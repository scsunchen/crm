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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>
<spring:url value="/terms/${page}/create.html" var="createHRef"/>
<a class="btn btn-primary" href="${createHRef}">
    <span class="glyphicon glyphicon-plus"></span> 
    <spring:message code="BusinessPartnerTerms.Button.Create"/>
</a>
<br/>
<br/>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th></th>
            <th><spring:message code="BusinessPartnerTerms.Table.BusinessPartnerName"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.Status"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.DateFrom"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.EndDate"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.DaysToPay"/></th>
            <th><spring:message code="BusinessPartnerTerms.Table.Rebate"/></th>
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
                                                arguments="${businessPartnerTerms.businessPartner.name}"/>
                            </h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                <spring:message code="BusinessPartnerTerms.Button.Cancel"/></button>
                            <spring:url value="/terms/${page}/${businessPartnerTerms.id}/delete.html" var="deleteHRef"/>   
                            <a type="button" class="btn btn-danger"
                               href="${deleteHRef}">
                                <spring:message code="BusinessPartnerTerms.Button.Delete"/></a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <spring:url value="/terms/${page}/${businessPartnerTerms.id}/update.html" var="readRow"/>   
                        <a href="${readRow}" class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span> <spring:message code="BusinessPartnerTerms.Table.ReadButton"/></a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> <spring:message code="BusinessPartnerTerms.Table.RemoveButton"/>
                        </button>
                    </div>
                </td>
                <td><c:out value="${businessPartnerTerms.businessPartner.name}"/></td>
                <td><c:out value="${businessPartnerTerms.status.description}"/></td>
                <td><spring:eval expression="businessPartnerTerms.dateFrom"/></td>
                <td><spring:eval expression="businessPartnerTerms.endDate"/></td>
                <td><c:out value="${businessPartnerTerms.daysToPay}"/></td>
                <td><spring:eval expression="businessPartnerTerms.rebate"/></td>
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
            <c:if test="${page > 0}" >
                <spring:url value="/terms/${page - 1}/read-page.html" var="prevPageHref"/>             
            </c:if>
            <a href="${prevPageHref}">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="BusinessPartnerTerms.Table.PrevPage"/>
            </a>
        </li>
        <spring:message code="BusinessPartnerTerms.Table.PageRange" 
                        arguments="${page+1}, ${numberOfPages+1}" /> 
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <c:if test="${page < numberOfPages}">
                <spring:url value="/terms/${page + 1}/read-page.html" var="nextPageHref"/>
            </c:if>
            <a href="${nextPageHref}">
                <span class="glyphicon glyphicon-forward"></span>
                <spring:message code="BusinessPartnerTerms.Table.NextPage"/>
            </a>
        </li>
    </ul>
</nav>