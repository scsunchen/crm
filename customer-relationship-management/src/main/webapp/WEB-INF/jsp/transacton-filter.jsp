<%--
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Pretraživanje poslovnih partnera -->

    <div id="bs-example-navbar-collapse-1">
      <form:form class="navbar-form navbar-left" role="search" modelAttribute="transactionDTO" method="GET">
        <div class="form-group input-group col-md-2">
          <label for="serviceProviderName">Service provider</label>
          <form:input id="serviceProviderName" class="typeahead form-control" type="text"
                      path="serviceProviderName" style="margin-bottom:  15px;"/>
          <form:hidden id="serviceProviderIdHidden" path="serviceProviderId"/>
        </div>
        <div class="form-group input-group col-md-2">
          <label for="pointOfSaleName">POS</label>
          <form:input id="pointOfSaleName" class="typeahead form-control" type="text"
                      path="pointOfSaleName" style="margin-bottom:  15px;"/>
          <form:hidden id="pointOfSaleIdHidden" path="pointOfSaleId"/>
        </div>
        <div class="form-group input-group col-md-2">
          <label for="terminalCustomCode">Treminal</label>
          <form:input id="terminalCustomCode" class="typeahead form-control" type="text"
                      path="terminalCustomCode" style="margin-bottom:  15px;"/>
          <form:hidden id="terminalIdHidden" path="terminalId"/>
        </div>
        <div class="form-group input-group col-md-2">
          <label for="typeDescription">Tip transakcije</label>
          <form:input id="typeDescription" class="typeahead form-control" type="text"
                      path="typeDescription" style="margin-bottom:  15px;"/>
          <form:hidden id="typeIdHidden" path="typeId"/>
        </div>
        <div class="form-group input-group col-md-2">
          <label for="distributorName">Distributor</label>
          <form:input id="distributorName" class="typeahead form-control" type="text"
                      path="distributorName" style="margin-bottom:  15px;"/>
          <form:hidden id="distributorIdHidden" path="distributorId"/>
        </div>

        <button type="submit" class="btn btn-default">Pretraga</button>
      </form:form>
    </div>
    <!-- /.navbar-collapse -->
  </div>
  <!-- /.container-fluid -->
</nav>