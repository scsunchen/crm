<%-- 
    Document   : item-grid
    Created on : May 30, 2015, 1:54:06 PM
    Author     : bdragan
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:if test = "${exception != null}">
    <div class="alert alert-warning">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
            ${exception.message}
    </div>
</c:if>
<form:form modelAttribute="item" method="post">
    <fieldset>
        <div class="col-lg-4">
            <spring:bind path="middle">
                <div>
                    <input:inputDate name="applicationDate" label="Datum Kursne Liste *" placeholder="dd.mm.yyyy"/>
                    <span class="help-inline"><c:if test="${status.error}"><c:out
                            value="${status.errorMessage}"/></c:if></span>
                </div>
            </spring:bind>
        </div>
        <div class="form-group col-lg-4">
            <spring:bind path="middle">
                <div>
                    <label for="middle"><spring:message code="ExchangeRate.Label.ListNo"/></label>
                    <form:input id="middle" path="listNumber"
                                class="form-control"/>
                        <span class="help-inline"><c:if test="${status.error}"><c:out
                                value="${status.errorMessage}"/></c:if></span>
                </div>
            </spring:bind>
        </div>

        <div class="form-group col-lg-4">
            <spring:bind path="middle">
                <div>
                    <label for="currency">Valuta *</label>
                    <form:input id="currency" class="typeahead form-control" type="text"
                                path="currency" style="margin-bottom:  15px;"/>
                    <form:hidden id="currencyISOHidden" path="currencyISOCode"/>
                    <span class="help-inline"><c:if test="${status.error}"><c:out
                            value="${status.errorMessage}"/></c:if></span>
                </div>
            </spring:bind>
        </div>

        <div class="form-group col-lg-4">
            <spring:bind path="buying">
                <div>
                    <label for="buying"><spring:message code="ExchangeRate.Label.Buying"/></label>
                    <form:input id="buying" path="buying"
                                class="form-control"/>
                        <span class="help-inline"><c:if test="${status.error}"><c:out
                                value="${status.errorMessage}"/></c:if></span>
                </div>
            </spring:bind>
        </div>


        <div class="form-group col-lg-4">
            <spring:bind path="middle">
                <div>
                    <label for="middle"><spring:message code="ExchangeRate.Label.middle"/></label>
                    <form:input id="middle" path="middle"
                                class="form-control"/>
                        <span class="help-inline"><c:if test="${status.error}"><c:out
                                value="${status.errorMessage}"/></c:if></span>
                </div>
            </spring:bind>
        </div>


        <div class="form-group col-lg-4">
            <spring:bind path="selling">
                <div>
                    <label for="selling"><spring:message code="ExchangeRate.Label.selling"/></label>
                    <form:input id="selling" path="selling"
                                class="form-control"/>
                        <span class="help-inline"><c:if test="${status.error}"><c:out
                                value="${status.errorMessage}"/></c:if></span>
                </div>
            </spring:bind>
        </div>

        <form:hidden path="version"/>

    </fieldset>

    <div class="form-group">
        <button type="submit" class="btn btn-primary" >
            <c:choose>
                <c:when test="${action == 'create'}">
                    <spring:message code="ExchangeRate.Button.Create" />
                </c:when>
                <c:otherwise>
                    <spring:message code="ExchangeRate.Button.Update" />
                </c:otherwise>
            </c:choose>
        </button>
        <a href="${pageContext.request.contextPath}/exchange-rate/${page}" class="btn btn-default" >
            <span class="glyphicon glyphicon-backward"></span> <spring:message code="ExchangeRate.Button.Back" /></a>
    </div>

</form:form>
<script type="text/javascript">
    $('#currency').typeahead({
        hint: false,
        highlight: true,
        minLength: 1,
        limit: 1000
    }, {
        display: 'currency',
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '${pageContext.request.contextPath}/read-currency/byname/%QUERY',
                wildcard: '%QUERY'
            }
        })
    });
    $('#currency').bind('typeahead:selected', function (obj, datum, name) {
        $('#currencyISOHidden').val(datum['isocode']);
    });
</script>

