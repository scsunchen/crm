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
        <c:choose>
          <c:when test="${action == 'create'}">
            <input:inputField label="Matični broj *" name="id" autofocus="true"/>
          </c:when>
          <c:otherwise>
            <input:inputField label="Matični broj *" name="id" disabled="true"/>
          </c:otherwise>
        </c:choose>
        <input:inputField label="Naziv *" name="name"/>
      </div>
      <div class="col-lg-3">
        <input:inputField name="place" label="Mesto"/>
        <input:inputField name="street" label="Ulica i broj"/>
        <input:inputField name="postCode" label="Poštanski Broj"/>
      </div>
      <div class="col-lg-3">
        <input:inputField label="Kontakt osoba" name="contactPerson"/>
        <input:inputField label="Funkcija kontakt osobe" name="contactFunction"/>
        <input:inputField label="Tekući račun" name="account"/>
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
