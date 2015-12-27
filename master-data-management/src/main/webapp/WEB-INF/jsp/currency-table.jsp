<%--
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="table-responsive generic-container">
  <table class="table table-striped">
    <thead>
    <tr>
      <th><a class="btn btn-primary" href="/masterdata/currency/${page}/create"><span class="glyphicon glyphicon-plus"></span>
        Kreiraj</a></th>
      <th>ISO kod</th>
      <th>ISO broj</th>
      <th>Naziv</th>
      <th>Država</th>
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
                obrišete ${item.ISOCode}?</h4>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Odustani</button>
              <a type="button" class="btn btn-danger" href="${page}/${item.ISOCode}/delete.html">Obriši</a>
            </div>
          </div>
        </div>
      </div>
      <tr>
        <td>
          <div class="btn-group btn-group-sm" role="group">
            <a href="${page}/update/${item.ISOCode}" class="btn btn-primary"><span
                    class="glyphicon glyphicon-search"></span> pregled</a>
            <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                    class="glyphicon glyphicon-trash"></span> brisanje
            </button>
          </div>
        </td>
        <td><c:out value="${item.ISOCode}"/></td>
        <td><c:out value="${item.ISONumber}"/></td>
        <td><c:out value="${item.currency}"/></td>
        <td><c:out value="${item.state}"/></td>
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
