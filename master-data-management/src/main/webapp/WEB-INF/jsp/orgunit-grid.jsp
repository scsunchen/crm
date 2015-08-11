<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<form:form modelAttribute="item" method="post">

    <div class="form-group">
        <div class="col-md-6">
            <c:choose>
                <c:when test="${action == 'update'}">
                    <input:inputField label="Šifra *" name="id" autofocus="true" disabled="true"/>
                </c:when>
            </c:choose>
            <input:inputField label="Korisnička Šifra *" name="customId"/>
            <input:inputField label="Naziv *" name="name"/>
        </div>
    </div>

    <div class="col-md-6">
        <div class="form-group">
            <input:inputField name="place" label="Mesto"/>
            <input:inputField name="street" label="Ulica i broj"/>
            <spring:bind path="clientId">
                <div class="form-group">
                    <label for="clientId">Kompanija korisnik *</label>
                    <form:select path="clientId" id="clientId" class="form-control" itemLabel="clientId">
                        <form:option value="${clientId}">${clientName}</form:option>
                        <form:options items="${clients}" itemLabel="name" itemValue="id"/>
                    </form:select>
                </div>
            </spring:bind>
        </div>
        <div class="form-group">
            <label for="parentOrgUnitName">Nadređena OJ</label>
            <form:input id="parentOrgUnitName" class="typeahead form-control" type="text"
                        path="parentOrgUnitName" style="margin-bottom:  15px;"/>
            <form:hidden id="itemDescHidden" path="parentOrgUnitId"/>
        </div>
    </div>

    <form:hidden path="version"/>

    <div class="form-group">
        <a class="btn btn-primary" href="/masterdata/org-unit/0">Povratak</a>
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
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/org-unit/read-orgunit/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#parentOrgUnitName').bind('typeahead:selected', function (obj, datum, name) {
        $('#itemDescHidden').val(datum['id']);
    });
</script>