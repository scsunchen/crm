<div class="container-fluid">
  <div class="navbar-header">
    <button type="button"
            class="navbar-toggle"
            data-toggle="collapse"
            data-target="#navbar"
            aria-controls="navbar">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
  </div>
  <div id="navbar" class="navbar-collapse collapse">
    <ul class="nav navbar-nav navbar-right">
      <tiles:importAttribute name="modules" />
      <c:forEach var="module" items="${applicationScope['modules']}">
        <c:if test="${module.isVisibleForUser('') == true}" >
          <!-- TODO : postavi active na izabrani modul -->
          <li><a href="/${module.path}">${module.name}</a></li>
        </c:if>
      </c:forEach>
      <li><a href="#">Odjavi se</a></li>
    </ul>
  </div>
</div>