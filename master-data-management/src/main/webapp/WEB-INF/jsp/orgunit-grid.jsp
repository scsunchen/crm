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
    <div class="form-group">
        <c:choose>
            <c:when test="${action == 'create'}">
                <input:inputField label="Šifra *" name="id" autofocus="true"/>
            </c:when>
            <c:otherwise>
                <input:inputField label="Šifra *" name="id" disabled="true"/>
            </c:otherwise>
        </c:choose>
    </div>

    <input:inputField label="Naziv *" name="name"/>

    <div class="form-group">
        <div class="col-md-6">
            <input:inputField name="place" label="Mesto"/>
        </div>
        <div class="col-md-6">
            <input:inputField name="street" label="Ulica i broj"/>
        </div
    </div>

    <form:hidden path="version"/>

    <div class="form-group col-md-1">
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

