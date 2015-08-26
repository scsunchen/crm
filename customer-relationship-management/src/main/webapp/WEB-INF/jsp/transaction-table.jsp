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
    <div class="container-fluid">
        <!-- Pretraživanje poslovnih partnera -->

        <div id="bs-example-navbar-collapse-1">
            <form:form class="navbar-form navbar-left" role="search" modelAttribute="transactionDTO" method="POST">
                <div class="form-group input-group col-md-2">
                    <label for="serviceProviderName">Service provider</label>
                    <form:input id="serviceProviderName" class="typeahead form-control" type="text"
                                path="serviceProviderName" style="margin-bottom:  15px;"/>
                    <form:hidden id="serviceProviderIdHidden" path="serviceProviderId"/>
                </div>
                <div class="form-group input-group col-md-2">
                    <label for="pointOfSaleName">POS</label>
                    <form:input id="pointOfSaleName" class="typeahead form-control" type="text"
                                path="pointOfSaleName" style="margin-bottom:  15px;"/>
                    <form:hidden id="pointOfSaleIdHidden" path="pointOfSaleId"/>
                </div>
                <div class="form-group input-group col-md-2">
                    <label for="terminalCustomCode">Treminal</label>
                    <form:input id="terminalCustomCode" class="typeahead form-control" type="text"
                                path="terminalCustomCode" style="margin-bottom:  15px;"/>
                    <form:hidden id="terminalIdHidden" path="terminalId"/>
                </div>
                <div class="form-group input-group col-md-2">
                    <label for="typeDescription">Tip transakcije</label>
                    <form:input id="typeDescription" class="typeahead form-control" type="text"
                                path="typeDescription" style="margin-bottom:  15px;"/>
                    <form:hidden id="typeIdHidden" path="typeId"/>
                </div>
                <div class="form-group input-group col-md-2">
                    <label for="distributorName">Distributor</label>
                    <form:input id="distributorName" class="typeahead form-control" type="text"
                                path="distributorName" style="margin-bottom:  15px;"/>
                    <form:hidden id="distributorIdHidden" path="distributorId"/>
                </div>

                <button type="submit" class="btn btn-default">Pretraga</button>
            </form:form>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Šifra</th>
            <th>Tip</th>
            <th>Terminal</th>
            <th>POS</th>
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
                <td><spring:eval expression="item.amount"/></td>
                <td><c:out value="${item.serviceProviderName}"/></td>
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
        <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
            <a href="<c:if test="${page > 0}"><c:out value="${page - 1}" /></c:if>">
                <span class="glyphicon glyphicon-backward"></span> Prethodna
            </a>
        </li>
        <c:out value="${page+1} od ${numberOfPages+1}"/>
        <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
            <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>">
                <span class="glyphicon glyphicon-forward"></span> Naredna
            </a>
        </li>
    </ul>
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
                url: '${pageContext.request.contextPath}/masterdat/read-serviceprovider/%QUERY',
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
                url: '${pageContext.request.contextPath}/masterdat/read-businesspartner/%QUERY',
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
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/masterdat/read-terminal/%QUERY',
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
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/masterdat/read-transactiontype/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#typeDescription').bind('typeahead:selected', function (obj, datum, name) {
        $('#typeIdHidden').val(datum['id']);
    });
</script>
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