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
<a class="btn btn-primary" href="${page}/create">
    <span class="glyphicon glyphicon-plus"></span> <spring:message code="Invoice.Button.Create" /></a>
<br/>
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
                            <button type="button" class="btn btn-default" data-dismiss="modal">Odustani</button>
                            <a type="button" class="btn btn-danger" 
                               href="${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/delete.html">Obriši</a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group" >
                        <a href="${page}/${invoice.clientId}/${invoice.orgUnitId}/${invoice.document}/update.html" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span> pregled</a>
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
        Strana
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}" /></c:if>">
                    <span class="glyphicon glyphicon-backward"></span> 
                <spring:message code="Invoice.Table.PrevPage" />
                </a>
            </li>
        <c:out value="${page+1} od ${numberOfPages+1}" />
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>" >
                <span class="glyphicon glyphicon-forward"></span> 
                <spring:message code="Invoice.Table.NextPage" />
            </a>
        </li>
    </ul>
</nav>