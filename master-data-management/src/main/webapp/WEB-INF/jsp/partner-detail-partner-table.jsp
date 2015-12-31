<%--
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<nav class="navbar navbar-default">
    </br>
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="btn btn-default"
               href="${pageContext.request.contextPath}/partner/update-merchant.html?id=${param['masterPartnerId']}&name=${param['masterPartnerName']}&page=${param['page']}">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="BusinessPartnerDetails.Button.Back"/></a></div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-6"><p class="navbar-text navbar-right">
           <strong>Prodavac: <c:out value="${param['masterPartnerId']} / ${param['masterPartnerName']}"/></strong></p></div>
    </div>
</nav>

<form:form role="search" modelAttribute="businessPartnerDTO" method="POST"
           action="${pageContext.request.contextPath}/partner/read-subpartners.html?type=${param['type']}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${param['page']}">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <br/>
            <!-- Pretraživanje poslovnih partnera -->
            <form:input id="type-hidden" type="hidden" path="typeValue"/>
            <div class="form-group col-lg-4">
                <form:input id="businessPartner" class="typeahead form-control"
                            placeholder="Naziv poslovnog partnera..." type="text" path="name"/>
                <form:input id="businessPartner-hidden" type="hidden"
                            path="id"/>
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
            <th><a class="btn btn-primary"
                   href="/masterdata/partner/create-detail.html?type=${param['type']}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${param['page']}"><span
                    class="glyphicon glyphicon-plus"></span>
                <spring:message code="Common.Button.Create"></spring:message> </a></th>
            <th><spring:message code="BusinessPartner.Table.CompaniIDNumber"/></th>
            <th><spring:message code="BusinessPartner.Table.Name"/></th>
            <th><spring:message code="BusinessPartner.Table.Address"/></th>
            <th><spring:message code="BusinessPartner.Table.Phone"/></th>
            <th><spring:message code="BusinessPartner.Table.eMail"/></th>
            <th><spring:message code="BusinessPartner.Table.BankAccount"/></th>
            <th><spring:message code="BusinessPartner.Table.ContactPerson"/></th>
            <th><spring:message code="BusinessPartner.Table.Type"/></th>
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
                                obrišete ${item.name}?</h4>
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
                        <a href="update-subpartner.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&id=${item.id}&page=${param['page']}" class="btn btn-primary">
                           <span class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> brisanje
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.companyIdNumber}"/></td>
                <td><c:out value="${item.name}"/></td>
                <td><c:out
                        value="${item.country} ${item.place} ${item.postCode} ${item.street} ${item.houseNumber}"/></td>
                <td><c:out value="${item.phone}"/></td>
                <td><c:out value="${item.EMail}"/></td>
                <td><c:out value="${item.currentAccount}"/></td>
                <td><c:out value="${item.contactPersoneName}"/></td>
                <td><c:out value="${item.typeDescription}"/></td>
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
            <a href="<c:if test="${page > 0}"><c:out value="?type=${param['type']}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${page - 1}" /></c:if>">
                <span class="glyphicon glyphicon-backward"></span> <spring:message code="Common.Button.PreviousPage"></spring:message>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="?type=${param['type']}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> <spring:message code="Common.Button.NextPage"></spring:message>
            </a>
        </li>
    </ul>
</nav>
<script type="text/javascript">
    $('#businessPartner').typeahead({
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/read-partner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#businessPartner').bind('typeahead:selected', function (obj, datum, name) {
        console.log("obj " + obj);
        console.log("datum " + datum);
        console.log("name " + name);
        $('#businessPartner-hidden').val(datum['id']);
    });
</script>
