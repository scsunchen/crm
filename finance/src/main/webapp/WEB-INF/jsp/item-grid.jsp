<%-- 
    Document   : item-grid
    Created on : May 30, 2015, 1:54:06 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags" %>
<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        ${exception.message}
    </div>
</c:if>
<form:form modelAttribute="item" method="post">
    <c:choose>
        <c:when test="${action == 'create'}">
            <i:textField label="Article.Label.Code" name="code" autofocus="true" />
        </c:when>
        <c:otherwise>
            <i:textField label="Article.Label.Code" name="code" disabled = "true"/>
        </c:otherwise>
    </c:choose>
    <i:textField label="Article.Label.Desc" name="description" />
    <spring:bind path="VATRate" >
        <div class="form-group row">
            <div class="col-lg-6">                
                <label for="VATRate"><spring:message code="Article.Label.VATRate" /></label>
                <form:select id="VATRate" class="form-control" path="VATRate" 
                             items="${VATOptions}" itemLabel="description" />
                <span class="help-inline">${status.errorMessage}</span>
            </div>
        </div>
    </spring:bind>
    <spring:bind path="unitOfMeasureCode" >
        <div class="form-group row">
            <div class="col-lg-6">                
                <label for="unitOfMeasure"><spring:message code="Article.Label.UnitOfMeasure" /></label>
                <form:select id="unitOfMeasure" class="form-control" 
                             path="unitOfMeasureCode" items="${unitOfMeasure}" 
                             itemLabel="description" itemValue="description"/>
                <span class="help-inline">${status.errorMessage}</span>
            </div>
        </div>
    </spring:bind>
    <spring:bind path="purchasePrice">
        <div class="form-group row" >
            <div class="col-lg-6" >
                <label for="purchasePrice"><spring:message code="Article.Label.PurchasePrice"/></label>            
                <form:input id="purchasePrice" path="purchasePrice" class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
            </div>
    </spring:bind>
    <spring:bind path="salePrice">
        <div class="form-group row" >
            <div class="col-lg-6" >
                <label for="salePrice"  ><spring:message code="Article.Label.SalePrice"/></label>            
                <form:input id="salePrice" path="salePrice" 
                            class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
            </div>
    </spring:bind>
    <spring:bind path="salePriceWithVAT" >
        <div class="form-group row" >
            <div class="col-lg-6" >                
                <label for="salePriceWithVAT"  ><spring:message code="Article.Label.SalePriceVAT"/></label>            
                <form:input id="salePriceWithVAT" path="salePriceWithVAT" 
                            class="form-control" />
                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                </div>
            </div>
    </spring:bind>
    <form:hidden path="version" />    
    <div class="form-group">
        <button type="submit" class="btn btn-primary" >
            <c:choose>
                <c:when test="${action == 'create'}">
                    <spring:message code="Article.Button.Create" />
                </c:when>
                <c:otherwise>
                    <spring:message code="Article.Button.Update" />
                </c:otherwise>
            </c:choose>
        </button>
        <a href="${pageContext.request.contextPath}/item/${page}" class="btn btn-default" >
            <span class="glyphicon glyphicon-backward"></span> <spring:message code="Article.Button.Back" /></a>
    </div>
</form:form>