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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form role="search" modelAttribute="deviceDTO" method="GET"
           action="${pageContext.request.contextPath}/device/read-page.html?">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <br/>
            <!-- Pretraživanje poslovnih partnera -->
            <div class="form-group col-lg-4">
                <form:input path="customCode" id="customCode" type="text" class="form-control"
                            placeholder="Oznaka terminala..."/>
            </div>
            <div class="form-group col-lg-4">
                <form:input path="serialNumber" id="serialNumber" type="text" class="form-control"
                            placeholder="Serijski broj..."/>
                <form:input id="type-hidden" type="hidden" path="page" value="0"/>

            </div>
            <div class="col-lg-4">
                <button type="submit" class="btn btn-primary"><span class=" glyphicon glyphicon-search"></span></button>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
</form:form>


<div class="table-responsive generic-container">
  <table class="table table-striped">
        <thead>
        <tr>
            <th>   <a class="btn btn-primary" href="/masterdata/device/create.html"><span class="glyphicon glyphicon-plus"></span>
                <spring:message code="Common.Button.Create"></spring:message> </a>
            </th>
            <th><spring:message code="Device.Table.Id"></spring:message> </th>
            <th><spring:message code="Device.Table.CustomCode"></spring:message> </th>
            <th><spring:message code="Device.Table.Type"></spring:message></th>
            <th><spring:message code="Device.Table.SerialNo"></spring:message> </th>
            <th><spring:message code="Device.Table.Status"></spring:message></th>
            <th><spring:message code="Device.label.CreationDate"></spring:message> </th>
            <th><spring:message code="Device.Table.Firmware"></spring:message></th>
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
                                obrišete ${item.customCode}?</h4>
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
                        <a href="update.html?id=${item.id}&page=${page}"
                           class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> brisanje
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.id}"/></td>
                <td><c:out value="${item.customCode}"/></td>
                <td><c:out value="${item.articleDescription}"/></td>
                <td><c:out value="${item.serialNumber}"/></td>
                <td><c:out value="${item.deviceStatusName}"/></td>
                <td><spring:eval expression="item.creationDate"/></td>
                <td><c:out value="${item.installedSoftwareVersion}"/></td>
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
            <a href="<c:if test="${page > 0}"><c:out value="?customCode=${param['customCode']}&serialNumber=${param['serialNumber']}&page=${param['page'] - 1}" /></c:if>">
                <span class="glyphicon glyphicon-backward"></span> <spring:message code="Common.Button.PreviousPage"></spring:message>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="?customCode=${param['customCode']}&serialNumber=${param['serialNumber']}&page=${param['page'] + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> <spring:message code="Common.Button.NextPage"></spring:message>
            </a>
        </li>
    </ul>
</nav>
