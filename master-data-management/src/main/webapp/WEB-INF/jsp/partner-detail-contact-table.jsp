<%--
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<nav class="navbar navbar-default" <c:if test="${param['masterPartnerId'] == null}">hidden</c:if>>
    </br>
    <div class="container-fluid">
        <div class="navbar-header">
            <c:choose>
                <c:when test="${param['pointOfSaleId'] == null || param['pointOfSaleId'] == ''}">
                    <a class="btn btn-default"
                       href="${pageContext.request.contextPath}/partner/update-merchant.html?id=${param['masterPartnerId']}&name=${param['masterPartnerName']}&page=${param['page']}">
                        <span class="glyphicon glyphicon-backward"></span>
                        <spring:message code="BusinessPartnerDetails.Button.Back"/></a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-default"
                       href="${pageContext.request.contextPath}/partner/update-subpartner.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&id=${param['pointOfSaleId']}&page=${param['page']}">
                        <span class="glyphicon glyphicon-backward"></span>
                        <spring:message code="BusinessPartnerDetails.Button.Back"/></a>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-6"><p class="navbar-text navbar-right">
           <strong> <c:out value="${param['masterPartnerId']} / ${param['masterPartnerName']}"/></strong></p></div>
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
                   href="/masterdata/contact/create.html?masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&pointOfSaleId=${param['pointOfSaleId']}&page=${param['page']}"><span
                    class="glyphicon glyphicon-plus"></span>
                Kreiraj</a></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Name"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Phone"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Email"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Position"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Address"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.Merchant"/></th>
            <th><spring:message code="BusinessPartnerContacts.Table.POS"/></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="contactItem" items="${data}">
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
                               href="/masterdata/contact/delete.html?id=${contactItem.id}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${param['page']}">Obriši</a>
                        </div>
                    </div>
                </div>
            </div>
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <a href="/masterdata/contact/update.html?id=${contactItem.id}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&pointOfSaleId=${param['pointOfSaleId']}&page=${param['page']}"
                           class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> brisanje
                        </button>
                    </div>
                </td>
                <td><c:out value="${contactItem.name}"/></td>
                <td><c:out value="${contactItem.phone}"/></td>
                <td><c:out value="${contactItem.email}"/></td>
                <td><c:out value="${contactItem.function}"/></td>
                <td><c:out
                        value="${contactItem.country}, ${contactItem.place} ${contactItem.postCode}, ${contactItem.street} ${contactItem.houseNumber}"/></td>
                <td><c:out value="${contactItem.merchantName}"/></td>
                <td><c:out value="${contactItem.pointOfSaleName}"/></td>
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
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}??masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> Prethodna
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page - 1}??masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> Naredna
            </a>
        </li>
    </ul>
</nav>
<script type="text/javascript">
    $('#merchant').typeahead({
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/read-merchant/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#merchant').bind('typeahead:selected', function (obj, datum, name) {

        $('#merchant-hidden').val(datum['id']);
    });
    $('#pointOfSale').typeahead({
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/partner/read-pos/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#pointOfSale').bind('typeahead:selected', function (obj, datum, name) {
        $('#pointOfSale-hidden').val(datum['id']);
    });
</script>
