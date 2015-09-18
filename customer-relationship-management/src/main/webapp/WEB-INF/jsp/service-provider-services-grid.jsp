<%-- 
    Document   : service-provider-services-grid
    Created on : Sep 16, 2015, 2:35:33 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>

<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<form:form modelAttribute="service" method="post">
    <form:hidden path="id" />
    <form:hidden path="version" />
    <div class="form-group" >
        <label for="serviceProvider"><spring:message code="ServiceProviderServices.Label.BusinessPartner" /></label>
        <c:choose>
                <c:when test="${action == 'create'}">
                    <form:input id="serviceProvider" class="typeahead form-control" 
                                type="text" path="serviceProvider.name" autofocus="true"/>
                </c:when>
                <c:otherwise>
                    <form:input id="serviceProvider" class="typeahead form-control" 
                                type="text" path="serviceProvider.name"/>
                </c:otherwise>
            </c:choose>
        <form:input id="serviceProvider-hidden" type="hidden" 
                    path="serviceProvider.id"/>
    </div>
    <label style="margin-top: 20px;"></label>
    <spring:message code="ServiceProviderServices.Label.Description" 
                    var="descriptionLabel"/>
    <input:inputField label="${descriptionLabel}" name="description" />        
    <div class="form-group" >
        <label for="serviceDescription"><spring:message code="ServiceProviderServices.Label.Service" /></label>
         <form:input id="serviceDescription" class="typeahead form-control" 
                    type="text" path="service.description" />
        <form:input id="serviceDescription-hidden" type="hidden" 
                    path="service.code"/>
    </div>
    <spring:bind path="dateFrom">
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="dateFrom" style="margin-top: 20px;">
                    <spring:message code="ServiceProviderServices.Label.DateFrom" />
                </label>
                <form:input id="dateFrom" path="dateFrom" class="form-control"/>                    
                <span class="help-inline">
                    <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                    </span>
                </div>
            </div>
    </spring:bind>
    <spring:bind path="dateTo">
        <div class="form-group row">
            <div class="col-lg-6">
                <label for="dateTo" >
                    <spring:message code="ServiceProviderServices.Label.DateTo" />
                </label>
                <form:input id="dateTo" path="dateTo" class="form-control"/>                    
                <span class="help-inline">
                    <c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if>
                    </span>
                </div>
            </div>
    </spring:bind>
    <div class="form-group">
        <spring:url value="/service-provider-services/${page}/read-page.html" 
                    var="back"/>
        <a class="btn btn-primary" href="${back}">
            <spring:message code="ServiceProviderServices.Button.Back" />
        </a>
        <button type="submit" class="btn btn-primary">
            <c:choose>
                <c:when test="${action == 'create'}">
                    <spring:message code="ServiceProviderServices.Button.Create" />
                </c:when>
                <c:otherwise>
                    <spring:message code="ServiceProviderServices.Button.Update" />
                </c:otherwise>
            </c:choose>
        </button>
    </div>
</form:form>
<script type="text/javascript">
    $('#serviceProvider').typeahead({
        hint: false,
        highlight: true,
        minLength: 1
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/service-provider-services/read-businesspartner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#serviceProvider').bind('typeahead:selected', function (obj, datum, name) {
        $('#serviceProvider-hidden').val(datum['id']);
    });
    $('#serviceDescription').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'description',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/service-provider-services/read-item/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#serviceDescription').bind('typeahead:selected', function (obj, datum, name) {
        $('#serviceDescription-hidden').val(datum['code']);
    });
</script>