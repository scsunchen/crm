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

<form:form modelAttribute="transactionDTO" method="post"
           action="${pageContext.request.contextPath}/invoicing/invoices/${page}">
    <div class="modal fade" id="dialogGenInvoices" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="form-group input-group col-md-2">
                        <label for="distributorName">Distributor</label>
                        <form:input id="distributorName" class="typeahead form-control" type="text"
                                    path="distributorName" style="margin-bottom:  15px;"/>
                        <form:hidden id="distributorIdHidden" path="distributorId"/>
                    </div>
                    <input:inputDate name="invoicingDate" label="Datum do: " placeholder="dd.mm.yyyy."/>
                    <div class="modal-footer">
                        <button class="btn btn-default" data-dismiss="modal"><spring:message
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


<form:form role="search" modelAttribute="transactionDTO" method="POST">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <!-- Pretraživanje poslovnih partnera -->

            <div id="bs-example-navbar-collapse-1">
                <div class="form-group input-group col-md-4">
                    <input:inputDate name="invoicingDate" label="Datum do: " placeholder="dd.mm.yyyy."/>
                </div>
                <button type="submit" class="btn btn-default">Pretraga</button>
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
            <th>Distributor</th>
            <th>Merchant</th>
            <th>POS</th>
            <th>Terminal</th>
            <th>Usluga</th>
            <th>Iznos za fakturisanje</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="count" value="0" scope="page"/>
        <c:forEach var="item" items="${data}">
            <!-- Modal -->
            <tr>
                <td hidden><c:out value="${item.distributorId}"/></td>
                <td><c:out value="${item.distributorName}"/></td>
                <td hidden><c:out value="${item.merchantId}"/></td>
                <td><c:out value="${item.merchantName}"/></td>
                <td hidden><c:out value="${item.posId}"/></td>
                <td><c:out value="${item.posName}"/></td>
                <td hidden><c:out value="${item.treminalId}"/></td>
                <td><c:out value="${item.treminalName}"/></td>
                <td hidden><c:out value="${item.serviceId}"/></td>
                <td><c:out value="${item.serviceDescription}"/></td>
                <td><c:out value="${item.amount}"/></td>
            </tr>
            <c:set var="count" value="${count + 1}" scope="page"/>
        </c:forEach>
        </tbody>
    </table>
</div>
<nav>
    <ul class="pager pull-left">
        <button data-toggle="modal" data-target="#dialogGenInvoices" class="btn btn-primary">
            <span class="glyphicon glyphicon-plus"></span><spring:message code="Invoicing.Button.GenInvoice"/></button>
        <br/>
    </ul>
    <ul class="pager pull-right">
        Strana
        <li class="<c:if test="${page == 0}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${pageContext.request.contextPath}/transactions/-${transactionDTO.serviceProviderId}-${transactionDTO.pointOfSaleId}${transactionDTO.terminalId}-${transactionDTO.typeId}-${transactionDTO.distributorId}/${page - 1}"/></c:if>">
                <span class="glyphicon glyphicon-backward"></span> Prethodna
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${pageContext.request.contextPath}/transactions/-${transactionDTO.serviceProviderId}-${transactionDTO.pointOfSaleId}${transactionDTO.terminalId}-${transactionDTO.typeId}-${transactionDTO.distributorId}/${page + 1}"/></c:if>">
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
                url: '${pageContext.request.contextPath}/masterdat/read-distributor/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#distributorName').bind('typeahead:selected', function (obj, datum, name) {
        $('#distributorIdHidden').val(datum['id']);
    });
</script>
