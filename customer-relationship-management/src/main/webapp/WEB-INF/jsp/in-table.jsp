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

<form:form method="post" modelAttribute="transactionDTO"
           action="${pageContext.request.contextPath}/invoicing/review-invoices.html">
    <div class="modal fade" id="dialogGenInvoices" tabindex="-1" role="dialog" action="/invoicing/review-invoices.html">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <input:inputDate name="invoicingGenDate" label="Datum do: " placeholder="dd.mm.yyyy."/>
                    <div class="form-group input-group">
                        <label for="invoicingDistributorName">Distributor</label>
                        <form:input id="invoicingDistributorName" class="typeahead form-control" type="text"
                                    path="invoicingDistributorName" style="margin-bottom:  15px;"/>
                        <form:hidden id="invoicingDistributorIdHidden" path="invoicingDistributorId"/>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-default" data-dismiss="modal" name="genInvoices"><spring:message
                                code="Invoicing.Button.Cancel"/></button>
                        <button class="btn btn-primary" type="submit"><spring:message
                                code="Invoicing.Button.Confirm"/></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${showDialog}">
        <script type="text/javascript">
            $('#dialogGenInvoices').modal('show');
        </script>
    </c:if>
</form:form>
<form:form role="search" modelAttribute="transactionDTO" method="GET"
           action="${pageContext.request.contextPath}/in-transactions.html">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <br/>
            <!-- Pretraživanje poslovnih partnera -->
            <div class="col-md-4 right">
                <input:inputDate name="invoicingDate" placeholder="Datum do... (dd.mm.yyyy.)"/>
            </div>
            <div class="col-md-4">
                <form:input id="distributorName" class="typeahead form-control" type="text"
                            path="distributorName" style="margin-bottom:  15px;" placeholder="Distributor..."/>
                <form:hidden id="distributorIdHidden" path="distributorId"/>
                <form:input id="type-hidden" type="hidden" path="page" value="0"/>
            </div>
            <button type="submit" class="btn btn-default">Pretraga</button>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
</form:form>
<div class="table-responsive">
    <table class="table table-striped" data-sort-name="item.distributorName">
        <thead>
        <tr>
            <th>Merchant</th>
            <th>POS</th>
            <th>Terminal</th>
            <th>Servis</th>
            <th>Iznos</th>
            <th>Distributor</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="item" items="${data}">
            <!-- Modal -->
            <tr>
                <td><c:out value="${item.merchantName}"/></td>
                <td><c:out value="${item.posName}"/></td>
                <td><c:out value="${item.terminalName}"/></td>
                <td><c:out value="${item.serviceDescription}"/></td>
                <td><spring:eval expression="item.amount"/></td>
                <td><c:out value="${item.distributorName}"/></td>

            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-left">
        <button data-toggle="modal" data-target="#dialogGenInvoices" class="btn btn-primary">
            <span class="glyphicon glyphicon-plus"></span><spring:message code="Invoicing.Button.GenInvoice"/>
        </button>
        <br/>
    </ul>
    <ul class="pager pull-right">
        Strana
        <li class="<c:if test="${page == 0}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${pageContext.request.contextPath}/crm/in-transactions.html?&distributorId=${param['distiributorId']}&invoicingDate=${param['invoicingDate']}&page=${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> Prethodna
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">

            <a href="<c:if test="${page < numberOfPages}"><c:out value="${pageContext.request.contextPath}/crm/in-transactions.html?&distributorId=${param['distiributorId']}&invoicingDate=${param['invoicingDate']}&page=${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> Naredna
            </a>
        </li>
    </ul>
</nav>


<script type="text/javascript">
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
                url: '${pageContext.request.contextPath}/masterdata/read-distributor/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#distributorName').bind('typeahead:selected', function (obj, datum, name) {
        $('#distributorIdHidden').val(datum['id']);
    });
</script>

<script type="text/javascript">
    $('#invoicingDistributorName').typeahead({
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
                url: '${pageContext.request.contextPath}/masterdata/read-distributor/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#invoicingDistributorName').bind('typeahead:selected', function (obj, datum, name) {
        $('#invoicingDistributorIdHidden').val(datum['id']);
    });
</script>
