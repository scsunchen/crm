<%-- 
    Author     : bdragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<button data-toggle="modal" data-target="#dialogAddItem" class="btn btn-primary" <c:if test="${journalEntry.isPosted == true}">disabled</c:if>>
    <span class="glyphicon glyphicon-plus"></span><spring:message code="JournalEntry.Button.AddItem" /></button>
<br/>
<link href="${pageContext.request.contextPath}/resources/css/typeahead.css" rel="stylesheet">    
<script src="${pageContext.request.contextPath}/resources/js/typeahead.bundle.min.js"></script>
<form:form modelAttribute="journalEntryItem" method="post" 
           action="${pageContext.request.contextPath}/journal-entry/${page}/${journalEntry.clientId}/${journalEntry.typeId}/${journalEntry.journalEntryNumber}/${journalEntry.version}/addItem.html" >
    <div class="modal fade" id="dialogAddItem" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <c:if test = "${dialogException != null}">
                        <div class="alert alert-warning">
                            <a href="#" class="close" data-dismiss="alert">&times;</a>
                            ${dialogException.message}
                        </div>
                    </c:if>
                    <form:hidden path="clientId" /> 
                    <form:hidden path="typeId" /> 
                    <form:hidden path="journalEntryNumber" />                         
                    <div class="form-group" >
                        <label for="orgUnit"><spring:message code="JournalEntryItem.Label.OrgUnit" /></label>
                        <form:input id="orgUnit" class="typeahead form-control" type="text" 
                                    path="unitName" />
                        <form:hidden id="orgUnitId" path="unitId"/>
                    </div>
                    <div class="form-group" >
                        <label for="document" style="margin-top:  15px;"><spring:message code="JournalEntryItem.Label.Document" /></label>
                        <form:input id="document" class="form-control" type="text" 
                                    path="document" />
                    </div>                    
                    <div class="row ">                                
                        <spring:bind path="creditDebitRelationDate">
                            <div class="form-group col-lg-6 ">                                
                                <label for="date" ><spring:message code="JournalEntryItem.Label.Date" /></label>            
                                <form:input id="date" path="creditDebitRelationDate" 
                                            class="form-control" />
                                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                                </div>
                        </spring:bind>
                        <spring:bind path="valueDate">
                            <div class="form-group  col-lg-6">    
                                <label for="valueDate" ><spring:message code="JournalEntryItem.Label.Value" /></label>            
                                <form:input id="valueDate" path="valueDate" 
                                            class="form-control" />
                                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="form-group" >
                        <label for="desc" ><spring:message code="JournalEntryItem.Label.Desc" /></label>
                        <form:input id="desc" class="typeahead form-control" type="text" 
                                    path="descName" />
                        <form:hidden id="descId" path="descId"/>
                    </div>
                    <div class="form-group" >
                        <label for="accountCode" style="margin-top:  15px;"><spring:message code="JournalEntryItem.Label.Account" /></label>
                        <form:input id="accountCode" class="typeahead form-control" 
                                    type="text" path="accountCode" />
                    </div>
                    <div class="form-group" >
                        <label for="partnerName" style="margin-top:  15px;"><spring:message code="JournalEntryItem.Label.Partner" /></label>
                        <form:input id="partnerName" class="typeahead form-control" 
                                    type="text" path="partnerName" />
                        <form:hidden id="partnerCompanyId" path="partnerCompanyId"/>
                    </div>
                    <div class="form-group" >
                        <label for="internalDocument" style="margin-top:  15px;"><spring:message code="JournalEntryItem.Label.Document1" /></label>
                        <form:input id="internalDocument" class="typeahead form-control" 
                                    type="text" path="internalDocument" />
                    </div>
                    <div class="row ">                                
                        <spring:bind path="debit">
                            <div class="form-group col-lg-6">    
                                <label for="debit" ><spring:message code="JournalEntryItem.Label.Debit" /></label>            
                                <form:input id="debit" path="debit" class="form-control" />
                                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                                </div>
                        </spring:bind>
                        <spring:bind path="credit">
                            <div class="form-group col-lg-6">    
                                <label for="credit" ><spring:message code="JournalEntryItem.Label.Credit" /></label>            
                                <form:input id="credit" path="credit" 
                                            class="form-control" />
                                <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
                                </div>
                        </spring:bind>
                    </div>
                </div>
                <div class="modal-footer">
                    <button  class="btn btn-default" data-dismiss="modal"><spring:message code="JournalEntry.Button.Cancel" /></button>
                    <button class="btn btn-primary" type="submit"><spring:message code="JournalEntry.Button.AddItem" /></button>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${showDialog}" >
        <script type="text/javascript">
            $('#dialogAddItem').modal('show');
        </script>
    </c:if>      
</form:form>            
<div class="table-responsive">
    <table class="table table-striped">
        <thead>
            <tr>
                <th></th>
                <th><spring:message code="JournalEntryItem.Table.Ordinal" /></th>
                <th><spring:message code="JournalEntryItem.Table.OrgUnit" /></th>
                <th><spring:message code="JournalEntryItem.Table.Document" /></th>
                <th><spring:message code="JournalEntryItem.Table.Date" /></th>
                <th><spring:message code="JournalEntryItem.Table.Value" /></th>
                <th><spring:message code="JournalEntryItem.Table.Desc" /></th>
                <th><spring:message code="JournalEntryItem.Table.Account" /></th>
                <th><spring:message code="JournalEntryItem.Table.Document1" /></th>
                <th><spring:message code="JournalEntryItem.Table.Partner" /></th>
                <th><spring:message code="JournalEntryItem.Table.Debit" /></th>
                <th><spring:message code="JournalEntryItem.Table.Credit" /></th>
            </tr>
        </thead>
        <tbody>            
            <c:set var="count" value="0" scope="page" />
            <c:forEach var="item" items="${items}">
                <!--DELETE DIALOG********************************************-->
            <div class="modal fade" id="dialog${item.ordinalNumber}" tabindex="-1" role="dialog" >
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
                            <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">
                                <spring:message code="JournalEntryItem.Question" 
                                                arguments="${item.ordinalNumber}" />
                            </h4>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                <spring:message code="Invoice.Button.Cancel" /></button>
                            <a type="button" class="btn btn-danger" 
                               href="${pageContext.request.contextPath}/journal-entry/${page}/${item.clientId}/${item.typeId}/${item.journalEntryNumber}/${item.ordinalNumber}/${journalEntry.version}/deleteItem.html">Obriši</a>
                        </div>
                    </div>
                </div>
            </div>
            <!--*********************************************************-->
            <tr>
                <td>
                    <button class="btn btn-danger btn-sm" <c:if test="${journalEntry.isPosted == true}">disabled</c:if> data-toggle="modal" data-target="#dialog${item.ordinalNumber}"><span class="glyphicon glyphicon-trash"></span> brisanje</button>
                </td>
                <td><spring:eval expression="item.ordinalNumber" /></td>
                <td><c:out value="${item.unitName}"/></td>
                <td><c:out value="${item.document}"/></td>
                <td><spring:eval expression="item.creditDebitRelationDate" /></td>
                <td><spring:eval expression="item.valueDate" /></td>
                <td><c:out value="${item.descName}"/></td>
                <td><c:out value="${item.accountCode}"/></td>
                <td><c:out value="${item.internalDocument}"/></td>
                <td><c:out value="${item.partnerName}"/></td>
                <td><spring:eval expression="item.debit" /></td>
                <td><spring:eval expression="item.credit" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script type="text/javascript">
    $('#orgUnit').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 10
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/journal-entry/read-orgunit/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#orgUnit').bind('typeahead:selected', function (obj, datum, name) {
        $('#orgUnitId').val(datum['id']);
    });
    $('#desc').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 10
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/journal-entry/read-description/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#desc').bind('typeahead:selected', function (obj, datum, name) {
        $('#descId').val(datum['id']);
    });
    $('#accountCode').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 10
    }, {
        display: 'number',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/journal-entry/read-account/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#partnerName').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 10
    }, {
        display: 'name',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/journal-entry/read-businesspartner/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#partnerName').bind('typeahead:selected', function (obj, datum, name) {
        $('#partnerCompanyId').val(datum['companyIdNumber']);
    });
</script>
