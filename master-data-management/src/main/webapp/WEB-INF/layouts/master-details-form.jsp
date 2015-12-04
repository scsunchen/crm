<%-- 
    Document   : master-details-form
    Created on : Jun 16, 2015, 5:53:18 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insertAttribute name="navigation" />
<br/>
<br/>
<fieldset>
    <legend class="control-label"></legend>
    <tiles:insertAttribute name="content" />
</fieldset>

