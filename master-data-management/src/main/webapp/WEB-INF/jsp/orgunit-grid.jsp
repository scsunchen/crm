<%--
  Created by IntelliJ IDEA.
  User: NikolaB
  Date: 6/7/2015
  Time: 10:06 PM
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="item" method="post">
    <div class="col-md-6">
        <div class="form-group">
            <c:choose>
                <c:when test="${action == 'create'}">
                    <input:inputField label="Šifra *" name="id" autofocus="true" disabled="true" />
                </c:when>
            </c:choose>
        </div>
        <input:inputField label="Korisnička Šifra *" name="customId"/>
        <input:inputField label="Naziv *" name="name"/>
        <spring:bind path="client.id">
            <div class="form-group">
                <label for="client">Kompanija korisnik</label>
                <form:select path="client" id="client" class="form-control" itemLabel="client">
                    <form:option value="${item.client.id}">${item.client.name}</form:option>
                    <form:options items="${clients}" itemLabel="name" itemValue="id"/>
                </form:select>
            </div>
        </spring:bind>
    </div>
    <div class="form-group">
        <div class="col-md-6">
            <div class="form-group">
                <label for="parentOrgUnitName">Nadređena jedinica</label>
                <form:input id="parentOrgUnitName" class="typeahead form-control" type="text"
                            path="parentOrgUnitName" style="margin-bottom:  15px;"/>
                <form:hidden id="itemDescHidden" path="parentOrgUnitId"/>
            </div>
            <input:inputField name="place" label="Mesto"/>
            <input:inputField name="street" label="Ulica i broj"/>
        </div>
    </div>

    <form:hidden path="version"/>

    <div class="form-group">
        <a class="btn btn-primary" href="/masterdata/orgunit/0">Povratak</a>
        <button type="submit" class="btn btn-primary">
            <c:choose>
                <c:when test="${action == 'create'}">
                    <c:out value="Kreiraj"/>
                </c:when>
                <c:otherwise>
                    <c:out value="Promeni"/>
                </c:otherwise>
            </c:choose>
        </button>
    </div>
</form:form>
<script type="text/javascript">
    $('#parentOrgUnitName').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'customId ' + 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/orgunit/read-orgunit/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#parentOrgUnitName').bind('typeahead:selected', function (obj, datum, name) {
        $('#parentOrgUnitName').val(datum['id']);
    });
</script>

