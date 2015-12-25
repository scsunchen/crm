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
               href="${pageContext.request.contextPath}/partner/read-documents-page.html?pointOfSaleId=${param['pointOfSaleId']}&masterPartnerId=${param['masterPartnerId']}&masterPartnerName=${param['masterPartnerName']}&page=${param['page']}">
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
                                <label for="id"><spring:message code="BusinessPartnerAccount.Table.Id"/></label>
                                <form:input id="id" path="id" class="form-control" disabled="true"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                            </spring:bind>
                        </c:when>
                    </c:choose>
                </div>

                <spring:bind path="description">
                    <div class="form-group">
                        <label for="description"><spring:message
                                code="BusinessPartnerDocument.Table.Description"/></label>
                        <form:input id="description" path="description" class="form-control"/>
                                <span class="help-inline"><c:if test="${status.error}"><c:out
                                        value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
                <div class="form-group">
                    <label for="businessPartnerOwnerName"><spring:message
                            code="BusinessPartnerAccount.Table.OwnerName"/></label>
                    <form:input id="businessPartnerOwnerName" class="typeahead form-control" type="text"
                                path="businessPartnerOwnerName" style="margin-bottom:  15px;"/>
                    <form:hidden id="businessPartnerOwnerHidden" path="businessPartnerOwnerId"/>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="col-lg-6">
                    <label for="inputDate" style="margin-top: 15px;"><spring:message
                            code="BusinessPartnerDocument.Table.InputDate"/></label>
                    <form:input id="inputDate" path="inputDate" type="text"
                                class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                </div>
                <div class="col-lg-6">
                    <label for="validUntil" style="margin-top: 15px;"><spring:message
                            code="BusinessPartnerDocument.Table.ValidUntil"/></label>
                    <form:input id="validUntil" path="validUntil" type="text"
                                class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                </div>
                <spring:bind path="status">
                    <div class="form-group">
                        <label for="status"><spring:message
                                code="BusinessPartnerDocument.Table.DocumentStatus"/></label>
                        <form:select path="status" id="status" class="form-control" itemLabel="type">
                            <form:option value="${item.status}">${item.statusDescription}</form:option>
                            <form:options items="${statuses}" itemLabel="description"/>
                        </form:select>
                    </div>
                </spring:bind>
                <spring:bind path="typeId">
                    <div class="form-group">
                        <label for="type"><spring:message
                                code="BusinessPartnerDocument.Table.Type"/></label>
                        <form:select path="typeId" id="typeId" class="form-control" itemLabel="type">
                            <form:option value="${item.type.id}">${item.typeDescription}</form:option>
                            <form:options items="${types}" itemLabel="description" itemValue="id"/>
                        </form:select>
                        <span class="help-inline"><c:if test="${status.error}"><c:out
                                value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
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
    $('#inputDate').datepicker({});
    $('#validUntil').datepicker({});

    $('#businessPartnerOwnerName').typeahead({
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
    $('#businessPartnerOwnerName').bind('typeahead:selected', function (obj, datum, name) {
        $('#businessPartnerOwnerHidden').val(datum['id']);
    });


</script>


