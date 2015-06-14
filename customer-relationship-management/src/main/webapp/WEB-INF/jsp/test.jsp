<form:form modelAttribute="invoice" method="post">
  <fieldset>
    <legend class="control-label">With fieldset and legend.control-label</legend>
    <div class="col-lg-6" >
      <div class="form-group " >
        <label for="client">Preduzeće * </label>
        <form:input id="client" class="typeahead form-control input-mini" type="text"
                    path="clientDesc" />
        <form:input id="client-hidden" type="hidden" path="clientId"/>
      </div>
      <div class="form-group">
        <label for="orgUnit">Organizaciona jedinica * </label>
        <form:input id="orgUnit" class="typeahead form-control" type="text"
                    path="orgUnitDesc" />
        <form:input id="orgUnit-hidden" type="hidden" path="orgUnitId"/>
      </div>
      <i:textField label="Dokument * " name="document" />
      <div class="form-group">
        <label for="businessPartner">Poslovni partner * </label>
        <form:input id="businessPartner" class="typeahead form-control"
                    type="text" path="partnerName" />
        <form:input id="businessPartner-hidden" type="hidden"
                    path="partnerID"/>
      </div>
      <div class="form-group">
        <label for="bank">Banka * </label>
        <form:input id="bank" class="typeahead form-control"
                    type="text" path="bankName" />
        <form:input id="bank-hidden" type="hidden" path="bankID"/>
      </div>
      <div class="form-group">
        <label for="currency">Valuta * </label>
        <form:input id="currency" class="typeahead form-control"
                    type="text" path="currencyISOCode" />
      </div>
      <div class="form-group ">
        <label for="partnerType">Partner</label>
        <form:select id="partnerType" class="form-control"
                     path="partnerType" items="${partnerTypes}"
                     itemLabel="description" itemValue="description"/>
      </div>
      <div class="form-group">
        <label for="type">Tip</label>
        <form:select id="type" class="form-control"
                     path="proForma" items="${invoiceTypes}"
                     itemLabel="description" itemValue="description"/>
      </div>
    </div>
    <div class="col-lg-6 " >
      <i:localDateFormattedField label="Datum kreiranja * " name="invoiceDate" />
      <i:localDateFormattedField label="Datum DPO * " name="creditRelationDate" />
      <i:localDateFormattedField label="Datum valute * " name="valueDate" />
      <i:textField label="Broj ugovora" name="contractNumber" />
      <i:localDateFormattedField label="Datum ugovora" name="contractDate" />
      <div class="form-group">
        <label for="paid">Plaćeno</label>
        <form:checkbox id="paid" class="form-control" path="paid" value="Plaćeno" />
      </div>
    </div>
  </fieldset>
</form:form>