<%-- 
    Document   : invoice-master-toolbar
    Created on : Jun 16, 2015, 8:59:02 PM
    Author     : bdragan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
            ${exception.message}
    </div>
</c:if>
<a href="${pageContext.request.contextPath}/terms/${page}/create"
   class="btn btn-default"><span class="glyphicon glyphicon-plus"></span><spring:message
        code="BusinessPartnerTerms.Button.Create"/></a>

<div class="modal fade" id="dialogDelete" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">
                    <spring:message code="BusinessPartnerTerms.Delete.Question"
                                    arguments="${terms.clientDesc},${terms.id}"/>
                </h4>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message
                        code="BusinessPartnerTerms.Button.Cancel"/></button>
                <a class="btn btn-danger"
                   href="${pageContext.request.contextPath}/terms/${page}/${terms.id}/delete.html">
                    <spring:message code="BusinessPartnerTerms.Button.Delete"/></a>
            </div>
        </div>
    </div>
</div>
<button class="btn btn-default" data-toggle="modal" data-target="#dialogDelete">
    <span class="glyphicon glyphicon-trash"></span> <spring:message code="BusinessPartnerTerms.Button.Delete"/></button>
<a href=
           "${pageContext.request.contextPath}/terms/${terms.id}/print-preview.html"
   class="btn btn-default"
   target="_blank">
    <span class="glyphicon glyphicon-search"></span> <spring:message
        code="BusinessPartnerTerms.Button.PrintPreview"/></a>
<a href="${pageContext.request.contextPath}/terms/${page}" class="btn btn-default">
    <span class="glyphicon glyphicon-backward"></span> <spring:message code="BusinessPartnerTerms.Button.Back"/></a>
