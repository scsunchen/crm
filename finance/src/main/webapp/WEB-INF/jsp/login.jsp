<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page session="true"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title><spring:message code="Login.Label.Title" /></title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.2.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
        <style>
            body {
                padding-top: 40px;
                padding-bottom: 40px;
                background-color: #f5f5f5;
            }

            .form-signin {
                max-width: 350px;
                padding: 19px 29px 29px;
                margin: 0 auto 20px;
                background-color: #fff;
                border: 1px solid #e5e5e5;
                /*text-align:  center;*/
                -webkit-border-radius: 5px;
                -moz-border-radius: 5px;
                border-radius: 5px;
                -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
            }
            .form-signin .form-signin-heading,
            .form-signin .checkbox {
                margin-bottom: 10px;
            }
            .form-signin input[type="text"],
            .form-signin input[type="password"] {
                font-size: 16px;
                height: auto;
                margin-bottom: 15px;
                padding: 7px 9px;
            }


        </style>
    </head>
    <body>
        <div class="container">
            <form class="form-signin" action="<c:url value='/j_spring_security_check' />" method="POST">
                <h2 class="form-signin-heading" style="text-align: center;"><spring:message code="Login.Label.Title" /></h2>
                <c:if test="${not empty param['error']}" ><div style="text-align: center;"><spring:message code="Login.Label.Fail" /></div></c:if>
                <input type="text" id="username" name="username"  class="form-control" placeholder="<spring:message code="Login.Label.Username" />" autofocus>
                <input type="password" name="password" class="form-control" placeholder="<spring:message code="Login.Label.Password" />" >
                <button class="btn btn-lg btn-primary btn-block" type="submit"><spring:message code="Login.Label.Submit" /></button>
            </form>
        </div>
    </body>
</html>
