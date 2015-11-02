<%-- 
    Document   : menu-form
    Created on : May 8, 2015, 10:07:23 AM
    Author     : Bobic Dragan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="table-responsive">
    <table class="table table-striped">
        <thead>
        <tr>
            <th><a class="btn btn-primary" href="/masterdata/partner/${page}/create"><span
                    class="glyphicon glyphicon-plus"></span>
                Kreiraj</a></th>
            <th>Matični broj</th>
            <th>Naziv</th>
            <th>Dodatni naziv</th>
            <th>Adresa</th>
            <th>Telefon</th>
            <th>email</th>
            <th>Račun</th>
            <th>Kontakt osoba</th>
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
                        <a href="${page}/update/${item.id}" class="btn btn-primary"><span
                                class="glyphicon glyphicon-search"></span> pregled</a>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                class="glyphicon glyphicon-trash"></span> brisanje
                        </button>
                    </div>
                </td>
                <td><c:out value="${item.companyIdNumber}"/></td>
                <td><c:out value="${item.name}"/></td>
                <td><c:out value="${item.name1}"/></td>
                <td class="form-inline">
                    <c:out value="${item.country}"/>
                    <c:out value="${item.place}"/>
                    <c:out value="${item.street}"/>
                    <c:out value="${item.postCode}"/>
                </td>
                <td><c:out value="${item.phone}"/></td>
                <td><c:out value="${item.EMail}"/></td>
                <td><c:out value="${item.currentAccount}"/></td>
                <td><c:out value="${item.contactPersoneName}"/></td>
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
<div role="tabpanel">
    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="contacts" role="tab"
                                                  data-toggle="tab"><spring:message
                code="BusinessPartnerContactDetails.tabb.label"/></a></li>
        <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">list-form
            kombinacija</a></li>
        <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">proba</a></li>
        <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">proba</a></li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="contacts">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><a class="btn btn-primary" href="/masterdata/partnercontacts/${page}/create"><span
                            class="glyphicon glyphicon-plus"></span>
                        Kreiraj</a></th>
                    <th>Ime</th>
                    <th>Telefon</th>
                    <th>Email</th>
                    <th>Funkcija</th>
                    <th>Država</th>
                    <th>Mesto</th>
                    <th>Poštanski broj</th>
                    <th>Ulica i broj</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="count" value="0" scope="page"/>
                <c:forEach var="contactItem" items="${contactData}">
                    <!-- Modal -->
                    <div class="modal fade" id="dialog${count}" tabindex="-1" role="dialog"
                         aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-body">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                            aria-hidden="true">&times;</span></button>
                                    <h4 class="modal-title" id="myModalContactLabel">Da li ste sigurni da želite da
                                        obrišete ${contactItem.name}?</h4>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Odustani</button>
                                    <a type="button" class="btn btn-danger"
                                       href="${page}/${contactItem.id}/delete.html">Obriši</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <tr>
                        <td>
                            <div class="btn-group btn-group-sm" role="group">
                                <a href="${page}/update/${item.id}" class="btn btn-primary"><span
                                        class="glyphicon glyphicon-search"></span> pregled</a>
                                <button class="btn btn-danger" data-toggle="modal" data-target="#dialog${count}"><span
                                        class="glyphicon glyphicon-trash"></span> brisanje
                                </button>
                            </div>
                        </td>
                        <td><c:out value="${contactItem.name}"/></td>
                        <td><c:out value="${contactItem.phone}"/></td>
                        <td><c:out value="${contactItem.email}"/></td>
                        <td><c:out value="${contactItem.country}"/></td>
                        <td><c:out value="${contactItem.place}"/></td>
                        <td><c:out value="${contactItem.street}"/></td>
                        <td><c:out value="${contactItem.postCode}"/></td>
                    </tr>
                    <c:set var="count" value="${count + 1}" scope="page"/>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div role="tabpanel" class="tab-pane" id="profile">
            <div class="list-group">
                <a href="#" class="list-group-item">
                    <h4 class="list-group-item-heading">text</h4>

                    <p class="list-group-item-text">opis</p>
                </a>
                <a href="#" class="list-group-item">
                    <h4 class="list-group-item-heading">text</h4>

                    <p class="list-group-item-text">opis</p>
                </a>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('[data-toggle="offcanvas"]').click(function () {
            $('.row-offcanvas-left').toggleClass('active')
        });
    });
</script>