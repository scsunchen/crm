<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="cssclass" required="false" rtexprvalue="true"
              description="Added css class..." %>
<%@ attribute name="label" required="false" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="autofocus" required="false" rtexprvalue="true" type="java.lang.Boolean"
              description="When present, it specifies that an <input> element should automatically get focus when the page loads." %>
<%@ attribute name="disabled" required="false" rtexprvalue="true" type="java.lang.Boolean"
              description="When present, it specifies that an <input> element is disabled." %>
<%@ attribute name="placeholder" required="false" rtexprvalue="true" type="java.lang.String"
              description="When present, it specifies value placeholder." %>

<spring:bind path="${name}">
    <c:choose>
        <c:when test="${label == null}">
            <label for="${name}" hidden="true">${label}</label>
        </c:when>
        <c:otherwise>
            <label for="${name}">${label}</label>
        </c:otherwise>
    </c:choose>
    <div class="form-group ">
        <form:input id="${name}" path="${name}"
                    class="form-control ${cssclass} ${status.error ? 'error' : '' }"
                    disabled="${disabled}"
                    autofocus="${autofocus}"
                    placeholder="${placeholder}"/>
        <span class="help-inline">${status.errorMessage}</span>
    </div>
</spring:bind>
<script type="text/javascript">
    $('#${name}').datepicker({});
</script>


