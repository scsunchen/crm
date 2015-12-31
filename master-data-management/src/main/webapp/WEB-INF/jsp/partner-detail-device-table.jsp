<%--
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>

<nav class="navbar navbar-default" <c:if test="${param['masterPartnerId'] == null}">hidden</c:if>>
    </br>
    <div class="container-fluid">
        <div class="navbar-header">
            <c:choose>
                <c:when test="${param['pointOfSaleId'] == null}">
                    <a class="btn btn-default"
                       href="${pageContext.request.contextPath}/partner/update.html?id=${param['masterPartnerId']}&name=${param['masterPartnerName']}&page=${param['page']}">
                        <span class="glyphicon glyphicon-backward"></span>
                        <spring:message code="Common.Button.Back"/></a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-default"
                       href="${pageContext.request.contextPath}/partner/update-subpartner.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&id=${param['pointOfSaleId']}&page=${param['page']}">
                        <span class="glyphicon glyphicon-backward"></span>
                        <spring:message code="Common.Button.Back"/></a>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-6"><p class="navbar-text navbar-right">
            <c:out value="${param['masterPartnerId']} / ${param['masterPartnerName']}"/></p></div>
    </div>
</nav>
<nav class="navbar navbar-default" <c:if test="${param['masterPartnerId'] != null}">hidden</c:if>>
    <br/>
    <!-- Pretraživanje merchant...pos... -->
    <form:form role="search" method="GET" modelAttribute="filterObjectsList"
               action="${pageContext.request.contextPath}/contact/0">
        <c:forEach var="businessPartnerDTOs" items="${filterObjectsList.businessPartnerDTOs}" varStatus="status">
            <div class="form-group col-lg-4">
                <c:if test="${status.index  == 0}">
                    <form:input id="merchant" class="typeahead form-control"
                                placeholder="Merchant..." type="text" path="businessPartnerDTOs[${status.index}].name"/>
                    <form:input id="merchant-hidden" type="hidden"
                                path="businessPartnerDTOs[${status.index}].id"/>
                </c:if>
                <c:if test="${status.index == 1}">
                    <form:input id="pointOfSale" class="typeahead form-control"
                                placeholder="POS..." type="text" path="businessPartnerDTOs[${status.index}].name"/>
                    <form:input id="pointOfSale-hidden" type="hidden"
                                path="businessPartnerDTOs[${status.index}].id"/>
                </c:if>

            </div>
        </c:forEach>
        <div class="col-lg-4">
            <button type="submit" class="btn btn-primary"><span class=" glyphicon glyphicon-search"></span></button>
        </div>
    </form:form>
</nav>

<div class="table-responsive generic-container">
    <table class="table table-striped">
        <thead>
        <tr>
            <th><a class="btn btn-primary"
                   href="/masterdata/deviceholder/create.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&pointOfSaleId=${param['pointOfSaleId']}&page=${param['page']}"><span
                    class="glyphicon glyphicon-plus"></span>
                <spring:message code="Common.Button.Create"></spring:message> </a></th>

            <th><spring:message code="Device.Table.CustomCode"></spring:message> </th>
            <th></th>
            <th><spring:message code="Device.Table.SerialNo"></spring:message> </th>
            <th><spring:message code="BusinessPartnerDevice.Table.POS"></spring:message> </th>
            <th><spring:message code="BusinessPartnerDevice.Table.RefillType"></spring:message> </th>
            <th><spring:message code="BusinessPartnerDevice.Table.ConnectionType"></spring:message></th>
            <th><spring:message code="BusinessPartnerDevice.Table.FromDate"></spring:message></th>
            <th><spring:message code="BusinessPartnerDevice.Table.ToDate"></spring:message></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="item" items="${data}">
            <!-- Modal -->
            <div class="modal fade" id="dialog${count}" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Da li ste sigurni da želite da
                                obrišete ${item.deviceCustomCode}?</h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Odustani</button>
                            <a type="button" class="btn btn-danger" href="${page}/${item.id}/delete.html">Obriši</a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <a href="/masterdata/deviceholder/update-assignment.html?id=${item.id}&pointOfSaleId=${param['pointOfSaleId']}&masterPartnerId=${param['masterPartnerId']}&page=0"
                           class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> brisanje
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.deviceCustomCode}"/></td>
                <td><a href="${pageContext.request.contextPath}/device/0/update/${item.deviceId}"
                       class="btn btn-primary"><span
                        class="glyphicon glyphicon-search"></span> terminal</a></td>
                <td><c:out value="${item.deviceSerialNumber}"/></td>
                <td><c:out value="${item.businessPartnerName}"/></td>
                <td><c:out value="${item.refillDescription}"></c:out></td>
                <td><c:out value="${item.connectionDescription}"></c:out></td>
                <td><spring:eval expression="item.startDate"/></td>
                <td><spring:eval expression="item.endDate"/></td>
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
                <span class="glyphicon glyphicon-backward"></span> <spring:message code="Common.Button.PreviousPage"></spring:message>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> <spring:message code="Common.Button.NextPage"></spring:message>
            </a>
        </li>
    </ul>
</nav>
