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
<nav class="navbar navbar-default" <c:if test="${param['masterPartnerId'] == null}">hidden</c:if>>
    </br>
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="btn btn-default"
               href="${pageContext.request.contextPath}/partner/read-contactsdetals-page.html?posId=${item.pointOfSaleId}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${param['page']}">
                <span class="glyphicon glyphicon-backward"></span>
                <spring:message code="BusinessPartnerDetails.Button.Back"/></a></div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-6"><p class="navbar-text navbar-right">
            <c:out value="${param['masterPartnerId']} / ${param['masterPartnerName']}"/></p></div>
    </div>
</nav>
<form:form modelAttribute="item" method="post">
    <fieldset>
        <div class="form-group">
            <div class="col-lg-6">
                <div class="form-group">
                    <c:choose>
                        <c:when test="${action == 'create'}">
                            <spring:bind path="id">
                                <label for="id"><spring:message code="BusinessPartnerContacts.Table.Id"/></label>
                                <form:input id="id" path="id" class="form-control" disabled="true"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </spring:bind>
                        </c:when>
                    </c:choose>
                </div>
                <spring:bind path="name">
                    <div class="form-group">
                        <label for="name"><spring:message code="BusinessPartnerContacts.Table.Name"/></label>
                        <form:input id="name" path="name" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
                <spring:bind path="function">
                    <div class="form-group">
                        <label for="function"><spring:message code="BusinessPartnerContacts.Table.Position"/></label>
                        <form:input id="function" path="function" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
                <spring:bind path="phone">
                    <div class="form-group">
                        <label for="phone"><spring:message code="BusinessPartnerContacts.Table.Phone"/></label>
                        <form:input id="phone" path="phone" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
                <spring:bind path="email">
                    <div class="form-group">
                        <label for="email"><spring:message code="BusinessPartnerContacts.Table.Email"/></label>
                        <form:input id="email" path="email" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
            </div>
            <div class="col-lg-6">
                <nav class="nav navbar-default">
                    </br>
                    <spring:bind path="country">
                        <div class="form-group">
                            <label for="country"><spring:message code="BusinessPartnerContacts.Table.State"/></label>
                            <form:input id="country" path="country" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </spring:bind>
                    <div class="col-lg-6">
                        <spring:bind path="place">
                            <div class="form-group">
                                <label for="place"><spring:message code="BusinessPartnerContacts.Table.Place"/></label>
                                <form:input id="place" path="place" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-lg-6">
                        <spring:bind path="postCode">
                            <div class="form-group">
                                <label for="postCode"><spring:message
                                        code="BusinessPartnerContacts.Table.ZipCode"/></label>
                                <form:input id="place" path="postCode" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </div>
                        </spring:bind>
                    </div>
                    <spring:bind path="street">
                        <div class="form-group col-lg-10">
                            <label for="street"><spring:message code="BusinessPartnerContacts.Table.Street"/></label>
                            <form:input id="street" path="street" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </spring:bind>
                    <spring:bind path="houseNumber">
                        <div class="form-group col-lg-2">
                            <label for="houseNumber"><spring:message
                                    code="BusinessPartnerContacts.Table.HouseNumber"/></label>
                            <form:input id="houseNumber" path="houseNumber" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                        </div>
                    </spring:bind>
                </nav>
                <div class="form-group">
                    <label for="merchantName"><spring:message code="BusinessPartnerContacts.Table.Merchant"/></label>
                    <form:input id="merchantName" class="typeahead form-control" type="text"
                                path="merchantName" style="margin-bottom:  15px;"/>
                    <form:hidden id="merchantNameHidden" path="merchantId"/>
                </div>
                <div class="form-group">
                    <label for="pointOfSaleName"><spring:message code="BusinessPartnerContacts.Table.POS"/></label>
                    <form:input id="pointOfSaleName" class="typeahead form-control" type="text"
                                path="pointOfSaleName" style="margin-bottom:  15px;"/>
                    <form:hidden id="pointOfSaleNameHidden" path="pointOfSaleId"/>
                </div>

            </div>
            <form:hidden path="version"/>
        </div>
    </fieldset>
    <div class="form-group">
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
                url: '${pageContext.request.contextPath}/partner/read-merchant/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#merchantName').bind('typeahead:selected', function (obj, datum, name) {
        $('#merchantNameHidden').val(datum['id']);
    });

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
                url: '${pageContext.request.contextPath}/partner/read-pos/%QUERY',
                replace: function () {
                    var q = '${pageContext.request.contextPath}/partner/read-pos/' + encodeURIComponent($('#pointOfSaleName').val());
                    if ($('#merchantNameHidden').val()) {
                        q += "?merchantId=" + encodeURIComponent($('#merchantNameHidden').val());
                    }
                    return q;
                },
                wildcard: '%QUERY'
            }
        })
    });

    $('#pointOfSaleName').bind('typeahead:change',
            function (evt, datum) {
                //2. ako ostavi prazno polje nista nije uneo
                if (datum === "") {
                    $('#pointOfSaleNameHidden').val('');
                } else {
                    //1. ovde ispita id i postavi ime
                    $('#pointOfSaleName').val($('#pointOfSaleName').attr('name'));
                }
            }
    )
            .bind('typeahead:select',
            function (evt, datum) {
                $('#pointOfSaleName').attr('name', datum['name']);
                $('#pointOfSaleNameHidden').val(datum['id']);
            }
    );
</script>


