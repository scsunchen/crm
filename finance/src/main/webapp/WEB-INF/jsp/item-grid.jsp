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

<form:form modelAttribute="item" method="post">
    <c:choose>
        <c:when test="${action == 'create'}">
            <i:textField label="Šifra *" name="code" autofocus="true" />
        </c:when>
        <c:otherwise>
            <i:textField label="Šifra *" name="code" disabled = "true"/>
        </c:otherwise>
    </c:choose>
    <i:textField label="Naziv *" name="description" />
    <div class="control-group">
        <spring:bind path="VATRate" >
            <div class="form-group">
                <label for="VATRate">PDV</label>
                <form:select id="VATRate" class="form-control" path="VATRate" 
                             items="${VATOptions}" itemLabel="description" />
                <span class="help-inline">${status.errorMessage}</span>
            </div>
        </spring:bind>

    </div>
    <div class="control-group">
        <spring:bind path="unitOfMeasureCode" >
            <div class="form-group">
                <label for="unitOfMeasure">Jedinica mere</label>
                <form:select id="unitOfMeasure" class="form-control" 
                             path="unitOfMeasureCode" items="${unitOfMeasure}" 
                             itemLabel="description" itemValue="description"/>
                <span class="help-inline">${status.errorMessage}</span>
            </div>
        </spring:bind>
    </div>
    <i:currencyFormattedField label="Nabavna cena" name="purchasePrice"/>
    <i:currencyFormattedField label="Prodajna cena" name="salePrice"/>
    <i:currencyFormattedField label="Prodajna cena + PDV" name="salePriceWithVAT"/>
    <form:hidden path="version" />
    <div class="form-group">
        <button type="submit" class="btn btn-primary" >
            <c:choose>
                <c:when test="${action == 'create'}">
                    <c:out value="Kreiraj" />
                </c:when>
                <c:otherwise>
                    <c:out value="Promeni" />
                </c:otherwise>
            </c:choose>
        </button>
    </div>
</form:form>