<%--
  Created by IntelliJ IDEA.
  User: Nikola
  Date: 16/11/2015
  Time: 15:45
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>


<nav id="masterPartner" class="navbar navbar-default" <c:if test="${param['masterPartnerId'] == null || param['masterPartnerId'] == ''}">hidden</c:if>>
    </br>
    <div class="navbar-header">

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-6"><p class="navbar-text navbar-right">
            <c:out value="${param['masterPartnerId']} / ${param['masterPartnerName']}"/></p></div>
    </div>
</nav>
<nav id="partner" class="navbar navbar-default" <c:if test="${param['partnerId'] == null && param['partnerId'] == ''}">hidden</c:if>>
    </br>
    <div class="navbar-header">
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-66"><p class="navbar-text navbar-right">
            <c:out value="${param['partnerId']} / ${param['partnerName']}"/></p></div>
    </div>
</nav>
<form:form modelAttribute="item" method="post" cssClass="generic-container">
    <fieldset>
        <div class="form-group">
            <div class="col-lg-6">
                <div class="form-group">
                    <c:choose>
                        <c:when test="${action == 'create'}">
                            <spring:bind path="id">
                                <label for="id"><spring:message code="BusinessPartnerAccount.Table.Id"/></label>
                                <form:input id="id" path="id" class="form-control" disabled="true"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </spring:bind>
                        </c:when>
                    </c:choose>
                </div>
                <spring:bind path="account">
                    <div class="form-group">
                        <label for="account"><spring:message code="BusinessPartnerAccount.Table.Account"/></label>
                        <form:input id="account" path="account" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
            </div>
            <div class="col-lg-6">

                <div class="form-group">
                    <label for="accountOwnerName"><spring:message
                            code="BusinessPartnerAccount.Table.OwnerName"/></label>
                    <form:input id="accountOwnerName" class="typeahead form-control" type="text"
                                path="accountOwnerName" style="margin-bottom:  15px;"/>
                    <form:hidden id="accountOwnerHidden" path="accountOwnerId"/>
                </div>
                <div class="form-group">
                    <label for="bankName"><spring:message code="BusinessPartnerAccount.Table.BankName"/></label>
                    <form:input id="bankName" class="typeahead form-control" type="text"
                                path="bankName" style="margin-bottom:  15px;"/>
                    <form:hidden id="bankHidden" path="bankId"/>
                </div>
                <div class="form-group">
                    <spring:bind path="currency">
                        <div>
                            <label for="currency"><spring:message
                                    code="BusinessPartnerAccount.Table.Currency"></spring:message> </label>
                            <form:input id="currency" class="typeahead form-control" type="text"
                                        path="currency" style="margin-bottom:  15px;"/>
                            <form:hidden id="currencyISOHidden" path="currencyISOCode"/>
                    <span class="help-inline"><c:if test="${status.error}"><c:out
                            value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </spring:bind>
                </div>
            </div>
            <form:hidden path="version"/>
        </div>
    </fieldset>
    <div class="form-group">
        <a class="btn btn-default"
           href="${pageContext.request.contextPath}/partner/read-accounts-page.html?partnerId=${param['partnerId']}&partnerName=${param['partnerName']}&pointOfSaleId=${param['pointOfSaleId']}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${param['page']}">
            <span class="glyphicon glyphicon-backward"></span>
            <spring:message code="BusinessPartnerDetails.Button.Back"/></a>
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
    $('#accountOwnerName').typeahead({
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
                url: '${pageContext.request.contextPath}/partner/read-merchant/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#accountOwnerName').bind('typeahead:selected', function (obj, datum, name) {
        $('#accountOwnereHidden').val(datum['id']);
    });

    $('#bankName').typeahead({
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
                url: '${pageContext.request.contextPath}/partner/read-merchant/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#bankName').bind('typeahead:selected', function (obj, datum, name) {
        $('#bankHidden').val(datum['id']);
    });


    $('#currency').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'currency',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/read-currency/byname/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#currency').bind('typeahead:selected', function (obj, datum, name) {
        $('#currencyISOHidden').val(datum['isocode']);
    });

</script>


