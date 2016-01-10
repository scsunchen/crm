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
<%@ taglib prefix="sprong" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form:form modelAttribute="item" enctype="multipart/form-data" method="post" cssClass="generic-container">
    <div class="form-group">
        <div class="col-lg-6">
            <c:choose>
                <c:when test="${action == 'create'}">
                    <input:inputField label="Employee.Table.Id" name="id" autofocus="true" disabled="true"/>
                </c:when>
                <c:otherwise>
                    <form:hidden path="id"/>
                </c:otherwise>
            </c:choose>

            <input:inputField label="Employee.Table.Name" name="name"/>
            <input:inputField label="Employee.Table.MiddleName" name="middleName"/>
            <input:inputField label="Employee.Table.Surname" name="lastName"/>
            <spring:bind path="dateOfBirth">
                <div class="form-group row">
                    <div class="col-lg-6">
                        <label for="dateOfBirth" style="margin-top: 15px;"><spring:message
                                code="Employee.Table.BirthDate"/></label>
                        <form:input id="dateOfBirth" path="dateOfBirth" type="text"
                                    class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                    </div>
                </div>
            </spring:bind>
            <input:inputField label="Employee.Table.Phone" name="phone"/>
            <input:inputField label="Employee.Table.Email" name="email"/>
            <a href="#" class="btn btn-default"
               onclick="document.getElementById('fileID').click(); return false;">
                <span class="glyphicon glyphicon-user"></span>
                <spring:message code="Employee.Table.FileUpload"/></a>
            <input type="file" id="fileID" name="fileUpload" style="visibility: hidden;"/>
            <label for="photo"><spring:message code="Employee.Table.Photo"/></label>
            <a href="download-image.html?id=${item.id}&page=0" id="photo"
               class="custom-width">${item.pictureName}</a>
        </div>
        <div class="col-lg-6">
            <spring:bind path="orgUnitId">
                <div class="form-group">
                    <label for="orgunit"><spring:message code="Employee.Table.OrgUnit"></spring:message> </label>
                    <form:select path="orgUnitId" id="orgunit" class="form-control" itemLabel="orgUnit">
                        <form:option value="${item.orgUnitId}">${item.orgUnitName}</form:option>
                        <form:options items="${orgUnits}" itemLabel="name" itemValue="id"/>
                    </form:select>
                </div>
            </spring:bind>
            <div class="col-lg-6">
                <spring:bind path="hireDate">
                    <div class="form-group row">
                        <label for="hireDate" style="margin-top: 15px;"><spring:message
                                code="Employee.Table.EmploymentStartDate"/></label>
                        <form:input id="hireDate" path="hireDate" type="text"
                                    class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
            </div>
            <div class="col-lg-6">
                <spring:bind path="endDate">
                    <div class="form-group row">
                        <label for="endDate" style="margin-top: 15px;"><spring:message
                                code="Employee.Table.EmploymentEndDate"/></label>
                        <form:input id="endDate" path="endDate" type="text"
                                    class="form-control"/>
                            <span class="help-inline"><c:if test="${status.error}"><c:out
                                    value="${status.errorMessage}"/></c:if></span>
                    </div>
                </spring:bind>
            </div>
            <spring:bind path="jobId">
                <div class="form-group">
                    <label for="job"><spring:message code="Employee.Table.Job"></spring:message> </label>
                    <form:select path="jobId" id="job" class="form-control" itemLabel="job">
                        <form:option value="${item.jobId}">${item.jobName}</form:option>
                        <form:options items="${jobs}" itemLabel="name" itemValue="id"/>
                    </form:select>
                </div>
            </spring:bind>
            <input:inputField label="Employee.Table.State" name="country"/>
            <div class="col-lg-8">
                <input:inputField label="Employee.Table.Place" name="place"/>
            </div>
            <div class="col-lg-4">
                <input:inputField label="Employee.Table.Zip" name="postCode"/>
            </div>
            <input:inputField label="Employee.Table.Street" name="street"/>
            <nav class="nav navbar-default">
                <c:choose>
                    <c:when test="${action == 'create'}">
                        <div class="col-lg-6">
                            <input:inputField name="appUsername" label="Employee.Table.Username"/>
                        </div>
                        <div class="col-lg-5">
                            <label for="appUserPassword"><spring:message code="Employee.Table.Password"/></label>
                            <form:input id="appUserPassword" path="appUserPassword"
                                        class="form-control ${cssclass} ${status.error ? 'error' : '' }"
                                        type="password"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="col-lg-6">
                            <input:inputField name="appUsername" label="Employee.Table.Username" disabled="true"/>
                        </div>
                        <div class="col-lg-5">
                            <label for="appUserPassword"><spring:message code="Employee.Table.Password"/></label>
                            <form:input id="appUserPassword" path="appUserPassword"
                                        class="form-control ${cssclass} ${status.error ? 'error' : '' }"
                                        type="password" disabled="true"/>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="col-lg-11">
                    <input:inputTextArea label="Employee.Table.UserDescription" name="appUserDesc"/>
                </div>
            </nav>
        </div>
        <form:hidden path="version"/>
    </div>
    <div class="form-group row">
        <a class="btn btn-default" href="/HR/employee/read-page.html?page=0">
            <span class="glyphicon glyphicon-backward"></span>
            <spring:message code="Common.Button.Back"></spring:message> </a>
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
<script>
    $('#dateOfBirth').datepicker({});
    $('#hireDate').datepicker({});
    $('#endDate').datepicker({});
</script>

