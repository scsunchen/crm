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
<a class="btn btn-primary" href="${page}/create">
    <span class="glyphicon glyphicon-plus"></span> <spring:message code="BusinessPartnerTerms.Button.Create"/></a>
<br/>
<br/>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Pretraživanje poslovnih partnera -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <form class="navbar-form navbar-left" role="search" modelAttribute="requestData" method="GET"
                  action="${pageContext.request.contextPath}/receivable-payable-card/show-terms.html">

                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Search">
                </div>
                <button type="submit" class="btn btn-default">Submit</button>
            </form>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th></th>
            <th>Matični broj</th>
            <th>Naziv</th>
            <th>Dodatni naziv</th>
            <th>Adresa</th>
            <th>Telefon</th>
            <th>email</th>
            <th>Račun</th>
            <th>Kontakt osoba</th>
        </tr>
        </thead>
        <tbody>
            <!-- Modal -->
            <tr>
                <td><c:out value="${partnerData.companyIdNumber}"/></td>
                <td><c:out value="${partnerData.name}"/></td>
                <td><c:out value="${partnerData.name1}"/></td>
                <td class="form-inline">
                    <c:out value="${partnerData.country}"/>
                    <c:out value="${partnerData.place}"/>
                    <c:out value="${partnerData.street}"/>
                    <c:out value="${partnerData.postCode}"/>
                </td>
                <td><c:out value="${partnerData.phone}"/></td>
                <td><c:out value="${partnerData.EMail}"/></td>
                <td><c:out value="${partnerData.currentAccount}"/></td>
                <td><c:out value="${partnerData.contactPersoneName}"/></td>
            </tr>
        </tbody>
    </table>
</div>
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
        <c:forEach var="businessPartnerTerms" items="${termsata}">
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
<script type="text/javascript">
    $('#partnerName').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
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
    $('#partnerName').bind('typeahead:selected', function (obj, datum, name) {
        $('#partnerNameHidden').val(datum['id']);
    });
</script>