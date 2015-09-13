<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="placeholder" required="false" rtexprvalue="true"
              description="Placeholder" %>
<%@ attribute name="cssclass" required="false" rtexprvalue="true"
              description="Added css class..." %>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="autofocus" required="false" rtexprvalue="true" type="java.lang.Boolean"
              description="When present, it specifies that an <input> element should automatically get focus when the page loads." %>
<%@ attribute name="disabled" required="false" rtexprvalue="true" type="java.lang.Boolean"
              description="When present, it specifies that an <input> element is disabled." %>

<spring:bind path="${name}">
    <div>
        <div class="form-group" id="datetimepickcontainer">
            <label for="${name}">${label}</label>
            <div class='input-group date' id='datetimepicker'>
                <form:input id="${name}" path="${name}"
                            class="form-control ${cssclass} ${status.error ? 'error' : '' }"
                            disabled="${disabled}"
                            autofocus="${autofocus}" placeholder="${placeholder}"/>
             <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                     </span>
            </div>
        </div>
    </div>
    <span class="help-inline">${status.errorMessage}</span>
</spring:bind>
<script type="text/javascript">
    $('#datetimepickcontainer div').datepicker({
        todayBtn: true,
        language: "sr-latin",
        forceParse: false,
        calendarWeeks: true,
        autoclose: true,
        todayHighlight: true,
        beforeShowDay: function (date) {
            if (date.getMonth() == (new Date()).getMonth())
                switch (date.getDate()) {
                    case 4:
                        return {
                            tooltip: 'Example tooltip',
                            classes: 'active'
                        };
                    case 8:
                        return false;
                    case 12:
                        return "green";
                }
        },
        beforeShowMonth: function (date) {
            switch (date.getMonth()) {
                case 8:
                    return false;
            }
        },
        toggleActive: true
    });
</script>


