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


<nav class="navbar navbar-default">
    <br/>
    <!-- Pretraživanje merchant...pos... -->
    <form:form role="search" method="GET" modelAttribute="deviceServiceProviderRegistrationDTO"
               action="${pageContext.request.contextPath}/deviceservprovider/device-serv-provider.html">
        <div class="form-group  col-lg-4">
            <form:input id="deviceCustomCode" class="typeahead form-control" type="text"
                        path="deviceCustomCode" style="margin-bottom:  15px;" placeholder="Terminal..."/>
            <form:hidden id="deviceCustomCodeHidden" path="deviceId"/>
        </div>
        <div class="form-group col-lg-4">
            <form:input id="serviceProvider" class="typeahead form-control"
                        placeholder="Service provider..." type="text" path="serviceProviderName"/>
            <form:input id="serviceProvider-hidden" type="hidden" path="serviceProviderId"/>
        </div>
        <form:hidden path="page" value="0"></form:hidden>
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
                   href="/masterdata/deviceservprovider/create.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&pointOfSaleId=${param['pointOfSaleId']}&page=${param['page']}"><span
                    class="glyphicon glyphicon-plus"></span>
                <spring:message code="Common.Button.Create"></spring:message> </a></th>
            <th><spring:message code="Device.Table.CustomCode"></spring:message></th>
            <th></th>
            <th><spring:message code="Device.Table.SerialNo"></spring:message></th>
            <th><spring:message code="ServiceProviderDevice.Table.ServiceProvider"></spring:message></th>
            <th><spring:message code="ServiceProviderDevice.Table.RefillType"></spring:message></th>
            <th><spring:message code="ServiceProviderDevice.Table.ConnectionType"></spring:message></th>
            <th><spring:message code="ServiceProviderDevice.Table.Registration"></spring:message></th>
            <th><spring:message code="ServiceProviderDevice.Table.Status"></spring:message></th>
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
                        <a href="${pageContext.request.contextPath}/deviceservprovider/update-serv-provider.html?id=${item.id}&page=0"
                           class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> brisanje
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.deviceCustomCode}"/></td>
                <td><a href="${pageContext.request.contextPath}/device/update.html?id=${item.deviceId}&page=0"
                       class="btn btn-primary"><span
                        class="glyphicon glyphicon-search"></span> terminal</a></td>
                <td><c:out value="${item.deviceSerialNumber}"/></td>
                <td><c:out value="${item.serviceProviderName}"/></td>
                <td><c:out value="${item.refillDescription}"></c:out></td>
                <td><c:out value="${item.connectionDescription}"></c:out></td>
                <td><c:out value="${item.registrationId}"></c:out></td>
                <td><c:out value="${item.deviceCustomCode}"></c:out>
                    <StatusName
                    /td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-right">
        Strana
        <li class="<c:if test="${page == 0}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="?businessPartnerId=${param['businessPartnerId']}&deviceCustomCode=${param['deviceCustomCode']}&page=${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> <spring:message
                    code="Common.Button.PreviousPage"></spring:message>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="?businessPartnerId=${param['businessPartnerId']}&deviceCustomCode=${param['deviceCustomCode']}&page=${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> <spring:message
                    code="Common.Button.NextPage"></spring:message>
            </a>
        </li>
    </ul>
</nav>
<script>
    $('#serviceProvider').typeahead({
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/read-service-provider/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#serviceProvider').bind('typeahead:selected', function (obj, datum, name) {
        $('#serviceProvider-hidden').val(datum['id']);
    });

    $('#deviceCustomCode').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'customCode',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/read-device/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#deviceCustomCode').bind('typeahead:selected', function (obj, datum, name) {
        console.log("evo ga rezultat jebeni")
        $('#deviceCustomCodeHidden').val(datum['id']);
    });
</script>
