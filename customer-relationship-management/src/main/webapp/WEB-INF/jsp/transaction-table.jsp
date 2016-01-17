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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form:form role="search" modelAttribute="transactionDTO" method="GET"
           action="${pageContext.request.contextPath}/transactions/view-transactions-page.html">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <!-- Pretraživanje poslovnih partnera -->
            <br/>

            <div class="col-md-2">
                <form:input path="id" class="form-control" placeholder="Transaction id..."/>
            </div>
            <div class="col-md-2">
                <form:input id="serviceProviderName" class="typeahead form-control" type="text"
                            path="serviceProviderName" style="margin-bottom:  15px;"
                            placeholder="Service provider..."/>
                <form:hidden id="serviceProviderIdHidden" path="serviceProviderId"/>
            </div>
            <div class="col-md-2">
                <form:input id="pointOfSaleName" class="typeahead form-control" type="text"
                            path="pointOfSaleName" style="margin-bottom:  15px;" placeholder="POS..."/>
                <form:hidden id="pointOfSaleIdHidden" path="pointOfSaleId"/>
            </div>
            <div class="col-md-2">
                <form:input id="terminalCustomCode" class="typeahead form-control" type="text"
                            path="terminalCustomCode" style="margin-bottom:  15px;" placeholder="Treminal..."/>
                <form:hidden id="terminalIdHidden" path="terminalId"/>
            </div>
            <div class="col-md-1">
                <form:input id="typeDescription" class="typeahead form-control" type="text"
                            path="typeDescription" style="margin-bottom:  15px;" placeholder="Tip transakcije..."/>
                <form:hidden id="typeIdHidden" path="typeId"/>
            </div>
            <div class="form-group col-md-1">
                <form:input id="dateFrom" path="responseTimeFrom" type="text"
                            class="form-control" placeholder="Datum od..."/>
            </div>
            <div class="form-group col-md-1">
                <form:input id="dateTo" path="responseTimeTo" type="text"
                            class="form-control" placeholder="Datum do..."/>
            </div>
            <form:hidden id="page" path="page" value="0"/>
            <button type="submit" class="btn btn-primary"><span class=" glyphicon glyphicon-search"></span></button>

        </div>
    </nav>
</form:form>

<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Šifra</th>
            <th>Tip</th>
            <th>Terminal</th>
            <th>POS</th>
            <th>Request</th>
            <th>Response</th>
            <th>Iznos</th>
            <th>Service provider</th>
            <th>Distributor</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="item" items="${data}">
            <!-- Modal -->
            <tr>
                <td><c:out value="${item.id}"/></td>
                <td><c:out value="${item.typeDescription}"/></td>
                <td><c:out value="${item.terminalCustomCode}"/></td>
                <td><c:out value="${item.pointOfSaleName}"/></td>
                <td><spring:eval expression="item.requestTime"/></td>
                <td><spring:eval expression="item.responseTime"/></td>
                <td><spring:eval expression="item.amount"/></td>
                <td><c:out value="${item.serviceProviderName}"/></td>
                <td><c:out value="${item.distributorName}"/></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav class="row">
    <ul class="pager pull-right">
        Strana
        <li class="<c:if test="${page == 0}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${pageContext.request.contextPath}/transactions/view-transactions-page.html?page=${page - 1}&serviceProviderId=${param['serviceProviderId']}&pointOfSaleId=${param['pointOfSaleId']}&&terminalCustomCode=${param['terminalCustomCode']}&terminalId=${param['terminalId']}&typeId=${param['typeId']}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> Prethodna
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${pageContext.request.contextPath}/transactions/view-transactions-page.html?page=${page + 1}&serviceProviderId=${param['serviceProviderId']}&pointOfSaleId=${param['pointOfSaleId']}&&terminalCustomCode=${param['terminalCustomCode']}&terminalId=${param['terminalId']}&typeId=${param['typeId']}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> Naredna
            </a>
        </li>
    </ul>
    <table class="table">
        <tr class="col-lg-3">
            <td><spring:message code="Common.Summary.SumPerPage"></spring:message></td>
            <td><strong><fmt:formatNumber type="currency"
                                          maxFractionDigits="2"
                                          value="${sumAmountPerPage}"/></strong></td>
        </tr>
        <tr class=" col-lg-3">
            <td><spring:message code="Common.Summary.SumPerQuery"></spring:message></td>
            <td><strong><fmt:formatNumber type="currency"
                                          maxFractionDigits="2"
                                          value="${sumAmount}"/></strong></td>
        </tr>
    </table>
</nav>
<script type="text/javascript">
    $('#serviceProviderName').typeahead({
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
                url: '${pageContext.request.contextPath}/crm/read-serviceprovider/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#serviceProviderName').bind('typeahead:selected', function (obj, datum, name) {
        $('#serviceProviderIdHidden').val(datum['id']);
    });
</script>
<script type="text/javascript">
    $('#pointOfSaleName').typeahead({
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
                url: '${pageContext.request.contextPath}/crm/read-businesspartner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#pointOfSaleName').bind('typeahead:selected', function (obj, datum, name) {
        $('#pointOfSaleIdHidden').val(datum['id']);
    });
</script>
<script type="text/javascript">
    $('#terminalCustomCode').typeahead({
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
                url: '${pageContext.request.contextPath}/crm/read-terminal/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#terminalCustomCode').bind('typeahead:selected', function (obj, datum, name) {
        $('#terminalIdHidden').val(datum['id']);
    });
</script>
<script type="text/javascript">
    $('#typeDescription').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'description',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/crm/read-transactiontype/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#typeDescription').bind('typeahead:selected', function (obj, datum, name) {
        $('#typeIdHidden').val(datum['id']);
    });
</script>
<script type="text/javascript">
    $('#dateFrom').datepicker({});
    $('#dateTo').datepicker({});
    $('#distributorName').typeahead({
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
                url: '${pageContext.request.contextPath}/crm/read-distributor/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#distributorName').bind('typeahead:selected', function (obj, datum, name) {
        $('#distributorIdHidden').val(datum['id']);
    });
</script>