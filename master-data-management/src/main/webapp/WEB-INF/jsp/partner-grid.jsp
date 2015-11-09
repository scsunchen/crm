<%-- 
    Document   : item-grid
    Created on : May 30, 2015, 1:54:06 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>

<form:form modelAttribute="item" method="post">
    <fieldset class="col-lg-12">
        <div class="form-group">
            <div class="col-lg-6">
                <div class="form-group">
                    <c:choose>
                        <c:when test="${action == 'create'}">
                            <input:inputField label="Šifra *" name="id" disabled="true"/>
                        </c:when>
                    </c:choose>
                </div>
                <input:inputField label="Naziv *" name="name"/>
                <input:inputField label="Dodatni Naziv" name="name1"/>
            </div>
            <div class="col-lg-3">
                <input:inputField label="Matični broj" name="companyIdNumber"/>
                <input:inputField name="country" label="Država"/>
                <input:inputField name="place" label="Mesto"/>
                <input:inputField name="street" label="Ulica i broj"/>
                <input:inputField name="postCode" label="Poštanski Broj"/>
                <input:inputField label="Telefon" name="phone"/>
                <input:inputField label="Fax" name="fax"/>
                <input:inputField label="email" name="EMail"/>
            </div>
            <div class="col-lg-3">
                <input:inputField label="PIB" name="TIN"/>
                <input:inputField label="Šifra delatnosti" name="activityCode"/>
                <input:inputField label="Tekući račun" name="currentAccount"/>
                <spring:bind path="currencyDesignation">
                    <div class="form-group">
                        <label for="currencyDesignation">Valuta</label>
                        <form:select path="currencyDesignation" id="currencyDesignation" class="form-control"
                                     items="${currencyDesignation}"
                                     itemLabel="description"/>
                    </div>
                </spring:bind>
                <input:inputField label="Rabat" name="rebate"/>
                <input:inputField label="Beskamatni period" name="interestFreeDays"/>
                <div class="form-group">
                    <label for="partnerName">Nadređeni partner</label>
                    <form:input id="partnerName" class="typeahead form-control" type="text"
                                path="parentBusinesspartnerName" style="margin-bottom:  15px;"/>
                    <form:hidden id="partnerNameHidden" path="parentBusinessPartnerId"/>
                </div>
                <div class="checkbox">
                    <label><form:checkbox path="VAT" id="VAT" class="checkbox"/>PDV</label>
                </div>
                <div class="form-group">
                    <label for="type">Tip</label>
                    <form:select path="type" id="type" class="form-control" itemLabel="type">
                        <form:option value="${type}">${typeDescription}</form:option>
                        <form:options items="${types}" itemLabel="description"/>
                    </form:select>
                </div>
            </div>
            <form:hidden path="version"/>
        </div>
    </fieldset>
    <div class="form-group">
        <a class="btn btn-primary" href="/masterdata/partner/0">Povratak</a>
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
