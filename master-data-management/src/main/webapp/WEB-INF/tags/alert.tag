<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="message" required="true" rtexprvalue="true"
              description="Alert message to be displayed" %>
<%@ attribute name="alertType" required="true" rtexprvalue="true"
              description="Alert type to be displayed" %>

<div id="${alertType}" class="alert alert-dismissible" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span>
    </button>
    <strong id="message">${message}</strong>
</div>
<script>
    $('document').ready(function () {
        if ($("div[role='alert']").text() == null) {
            $("div[role='alert']").hide();
        } else {
            $("div[role='alert']").addClass('alert-' + $("div[role='alert']").attr("id"));
        }
    })
</script>




