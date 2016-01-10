<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="value" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<fmt:parseDate value="${value}" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
<fmt:formatDate value="${parsedDate}" var="stdDatum" type="date" pattern="dd.MM.yyyy"/>
<c:out value="${stdDatum}"/>