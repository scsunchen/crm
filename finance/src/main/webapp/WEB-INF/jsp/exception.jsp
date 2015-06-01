<%-- 
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tiles:insertDefinition name="main-menu-form" >        
    <tiles:putAttribute name="title">Greška</tiles:putAttribute>
    <tiles:putAttribute name="panel-title">Greška prilikom izvršavanja programa</tiles:putAttribute>
    <tiles:putAttribute name="body">
                            <p>${exception.message}</p>
    </tiles:putAttribute>
</tiles:insertDefinition>