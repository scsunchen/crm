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

<form:form action="${pageContext.request.contextPath}/transactions/in-transactions-per-period.html"
           modelAttribute="invoicingTransactionDTO"
           method="GET">

    <nav class="navbar navbar-default">
        <br/>

        <div class="form-group col-lg-4">
            <form:select path="id" id="status" class="form-control" itemLabel="status">
                <form:option value="" label="Period fakturisanja..."/>
                <form:options items="${invoicingTransactions}" itemLabel="displayPeriod" itemValue="id"/>
            </form:select>
        </div>
        <div class="form-group col-lg-4">
            <form:input id="merchant" class="typeahead form-control"
                        placeholder="Merchant..." type="text" path="partnerName"/>
            <form:input id="merchant-hidden" type="hidden"
                        path="partnerId"/>
        </div>
        <form:input id="page-hidden" type="hidden" path="page" value="0"/>
        <div class="col-lg-4">
            <button type="submit" class="btn btn-primary"><span class=" glyphicon glyphicon-search"></span></button>
        </div>
    </nav>
</form:form>
<div class="table-responsive">
    <table class="table table-striped" data-sort-name="item.distributorName">
        <thead>
        <tr>
            <th></th>
            <th><spring:message code="Invoice.Table.Partner"></spring:message> </th>
            <th><spring:message code="Invoice.Table.Ammount"></spring:message> </th>
            <th><spring:message code="Invoice.Table.Distributor"></spring:message> </th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="item" items="${data}">
            <!-- Modal -->
            <tr>
                <td>
                    <div class="btn-group btn-group-sm" role="group">
                        <a href="${pageContext.request.contextPath}/transactions/in-transactions-per-pos-per-period.html?merchantId=${item.merchantId}&id=${param['id']}&page=0"
                           class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span>
                            <spring:message code="Intable.Button.ViewPerPOS"/></a>
                    </div>
                </td>
                <td><c:out value="${item.merchantName}"/></td>
                <td><spring:eval expression="item.amount"/></td>
                <td><c:out value="${item.distributorName}"/></td>

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
            <a href="<c:if test="${page > 0}"><c:out value="${pageContext.request.contextPath}/transactions/in-transactions-per-period.html?id=${param['id']}&partnerId=${param['partnerId']}&partnerName=${param['partnerName']}&page=${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> <spring:message code="Common.Button.PreviousPage"></spring:message>
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">

            <a href="<c:if test="${page < numberOfPages}"><c:out value="${pageContext.request.contextPath}/transactions/in-transactions-per-period.html?id=${param['id']}&partnerId=${param['partnerId']}&partnerName=${param['partnerName']}&page=${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> <spring:message code="Common.Button.NextPage"></spring:message>
            </a>
        </li>
    </ul>
</nav>


<script type="text/javascript">

    $('#date').datepicker({});
    $('#invoicingDate').datepicker({});


    $('#merchantName').typeahead({
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
                url: '${pageContext.request.contextPath}/masterdata/read-merchant/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#merchantName').bind('typeahead:selected', function (obj, datum, name) {
        $('#merchantIdHidden').val(datum['id']);
    });
</script>

</script>
