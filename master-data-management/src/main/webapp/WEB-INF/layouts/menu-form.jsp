<%-- 
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title><tiles:getAsString name="title"/></title>
    <tiles:insertAttribute name="head"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/datepicker3.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.2.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/Moment.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap-datepicker.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap-datetimepicker.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/locales/bootstrap-datepicker.sr-latin.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/typeahead/typeahead.bundle.min.js"></script>



</head>
<body>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button"
                    class="navbar-toggle"
                    data-toggle="collapse"
                    data-target="#navbar"
                    aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <tiles:importAttribute name="modules"/>
                <c:forEach var="module" items="${modules}">
                    <c:if test="${module.isVisibleForUser('') == true}">
                        <!-- TODO : postavi active na izabrani modul -->
                        <li><a href="/${module.path}">${module.name}</a></li>
                    </c:if>
                </c:forEach>
                <li><a href="#">Odjavi se</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <div class="row row-offcanvas-left">
        <div class="col-xs-12 col-sm-6 col-md-2 sidebar-offcanvas">
            <div class="well well-sm"
                 style="word-wrap: break-word;">
                <tiles:importAttribute name="selectedModule" ignore="true"/>
                <c:forEach items="${selectedModule.getFeaturesByGroupForUser('')}"
                           var="entry">
                    <h4>${entry.key}</h4>
                    <ul>
                        <c:forEach var="feature"
                                   items="${entry.value}">
                            <!--postavi funkcije modula-->
                            <!-- TODO : postavi active na izabranu funkciju-->
                            <li><a href="${feature.path}">${feature.name}</a></li>
                        </c:forEach>
                    </ul>
                </c:forEach>
            </div>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-10 main">
            <div class="panel panel-default">
                <div class="panel-heading"><tiles:getAsString name="panel-title"/></div>
                <div class="panel-body">
                    <div class="row">
                        <p class="pull-left visible-xs visible-sm">
                            <button type="button"
                                    class="btn btn-sm btn-default"
                                    data-toggle="offcanvas">
                                <span class="glyphicon glyphicon-menu-right"></span> meni
                            </button>
                        </p>
                    </div>
                    <tiles:insertAttribute name="body"/>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>