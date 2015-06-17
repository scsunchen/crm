<%--
  Created by IntelliJ IDEA.
  User: NikolaB
  Date: 6/7/2015
  Time: 9:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<a class="btn btn-primary" href="/masterdata/job/${page}/create"><span class="glyphicon glyphicon-plus"></span>
  Kreiraj</a>
<br/>
<br/>

<div class="table-responsive">
  <table class="table table-striped">
    <thead>
    <tr>
      <th></th>
      <th>Šifra</th>
      <th>Naziv</th>
    </tr>
    </thead>
    <tbody>
    <c:set var="count" value="0" scope="page"/>
    <c:forEach var="item" items="${data}">
      <!-- Modal -->
      <div class="modal fade" id="dialog${count}" tabindex="-1" role="dialog"
           aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-body">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                      aria-hidden="true">&times;</span></button>
              <h4 class="modal-title" id="myModalLabel">Da li ste sigurni da želite da
                obrišete ${item.name}?</h4>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Odustani</button>
              <a type="button" class="btn btn-danger" href="${page}/${item.id}/delete.html">Obriši</a>
            </div>
          </div>
        </div>
      </div>
      <tr>
        <td>
          <div class="btn-group btn-group-sm" role="group">
            <a href="${page}/update/${item.code}" class="btn btn-primary"><span
                    class="glyphicon glyphicon-search"></span> pregled</a>
            <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                    class="glyphicon glyphicon-trash"></span> brisanje
            </button>
          </div>
        </td>
        <td><c:out value="${item.id}"/></td>
        <td><c:out value="${item.name}"/></td>
        <td><c:out value="${item.description}"/></td>
      </tr>
      <c:set var="count" value="${count + 1}" scope="page"/>
    </c:forEach>
    </tbody>
  </table>
</div>
<nav>
  <ul class="pager pull-right">
    Strana
    <li class="<c:if test="${page == 0}"><c:out value="disabled" /></c:if>">
      <a href="<c:if test="${page > 0}"><c:out value="${page - 1}" /></c:if>">
        <span class="glyphicon glyphicon-backward"></span> Prethodna
      </a>
    </li>
    <c:out value="${page+1} od ${numberOfPages+1}"/>
    <li class="<c:if test="${page == numberOfPages}"><c:out value="disabled"/></c:if>">
      <a href="<c:if test="${page < numberOfPages}"><c:out value="${page + 1}"/></c:if>">
        <span class="glyphicon glyphicon-forward"></span> Naredna
      </a>
    </li>
  </ul>
</nav>

