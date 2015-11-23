<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div>
  <p>${exception.message}</p>
  <c:forEach var="stackTraceElem" items="${exception.stackTrace}">
      <c:out value="${stackTraceElem}"/><br/>
  </c:forEach>
</div>
