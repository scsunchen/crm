<div class="well well-sm"
     style="word-wrap: break-word;">
  <tiles:importAttribute name="selectedModule" ignore="true"/>
  <c:forEach items="${selectedModule.getFeaturesByGroupForUser('')}"
             var="entry">
    <h4>${entry.key}</h4>
    <ul>
      <c:forEach var="feature"
                 items="${entry.value}">
        <!--postavi funkcije modula-->
        <!-- TODO : postavi active na izabranu funkciju-->
        <li><a href="${feature.path}" >${feature.name}</a></li>
      </c:forEach>
    </ul>
  </c:forEach>
</div>