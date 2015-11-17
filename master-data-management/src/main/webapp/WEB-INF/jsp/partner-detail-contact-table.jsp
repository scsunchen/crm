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
    <div class="container-fluid">
        <h1>${param['masterPartnerId']} ${param['masterPartnerName']}</h1>
    </div>
</nav>
<form:form role="search" modelAttribute="businessPartnerDTO" method="GET"
           action="${pageContext.request.contextPath}/partner/0">
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
<div class="table-responsive">

    <table class="table table-striped">
        <thead>
        <tr>
            <th><a class="btn btn-primary" href="/masterdata/partnercontacts/${page}/create"><span
                    class="glyphicon glyphicon-plus"></span>
                Kreiraj</a></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Name"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Phone"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Email"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Position"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.State"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Place"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.ZipCode"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Street"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.HouseNumber"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Merchant"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.POS"/></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="contactItem" items="${contactData}">
            <!-- Modal -->
            <div class="modal fade" id="dialog${count}" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalContactLabel">Da li ste sigurni da želite da
                                obrišete ${contactItem.name}?</h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Odustani</button>
                            <a type="button" class="btn btn-danger"
                               href="${page}/${contactItem.id}/delete.html">Obriši</a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <a href="${page}/update/${item.id}" class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> brisanje
                        </button>
                    </div>
                </td>
                <td><c:out value="${contactItem.contactPerson.name}"/></td>
                <td><c:out value="${contactItem.contactPerson.phone}"/></td>
                <td><c:out value="${contactItem.contactPerson.email}"/></td>
                <td><c:out value="${contactItem.contactPerson.function}"/>/td>
                <td><c:out value="${contactItem.contactPerson.country}"/></td>
                <td><c:out value="${contactItem.contactPerson.place}"/></td>
                <td><c:out value="${contactItem.contactPerson.postCode}"/></td>
                <td><c:out value="${contactItem.contactPerson.street}"/></td>
                <td><c:out value="${contactItem.contactPerson.houseNumber}"/></td>
                <td><c:out value="${contactItem.merchant.name}"/></td>
                <td><c:out value="${contactItem.merchant.poinOfSlae}"/></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<div role="tabpanel" class="tab-pane" id="profile">
    <div class="list-group">
        <a href="#" class="list-group-item">
            <h4 class="list-group-item-heading">text</h4>

            <p class="list-group-item-text">opis</p>
        </a>
        <a href="#" class="list-group-item">
            <h4 class="list-group-item-heading">text</h4>

            <p class="list-group-item-text">opis</p>
        </a>
    </div>
</div>

</div>
<nav>
    <ul class="pager pull-right">
        Strana
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}?type=${bussinesPartnerDTO.type}&name=${bussinesPartnerDTO.name}&id=${bussinesPartnerDTO.id}" /></c:if>">
                <span class="glyphicon glyphicon-backward"></span> Prethodna
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}?type=${bussinesPartnerDTO.type}&name=${bussinesPartnerDTO.name}&id=${bussinesPartnerDTO.id}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> Naredna
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
