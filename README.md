# CRM

## Uputstvo za programere

###Formatiranje polja ekranske forme

Trebalo bi koristiti Spring org.springframework.format.annotation.DateTimeFormat za formatiranje datuma i 
org.springframework.format.annotation.NumberFormat za formatiranje procenata, brojeva i novcanih iznosa. 
U svakom modulu bi trebalo podesiti Spring MVC da koristi klasu com.invado.core.format.FormattingConversionServiceFactoryBean 
kao servis za konverziju(engl. conversion service). U XML datoteci bi trebalo da imate :
```xml
<mvc:annotation-driven conversion-service="conversionService">
<bean id="conversionService" class="com.invado.core.format.FormattingConversionServiceFactoryBean" />
```
U properties datoteci za svako polje morate uneti poruku koju korisnik vidi ako pogresno unese vrednost npr.
```xml
<form:form modelAttribute="item" method="post">
 <spring:bind path="purchasePrice">
   <form:input id="purchasePrice" path="purchasePrice" class="form-control" />
     <span class="help-inline"><c:if test="${status.error}"><c:out value="${status.errorMessage}" /></c:if></span>
         
 </spring:bind>
</form:form>
```
trebalo bi dodati u datoteku sa porukama red typeMismatch.item.purchasePrice=Greska prilikom unosa nabavne cene. Kljuc koji se unosi u datoteku sa porukama(typeMismatch.item.purchasePrice) sadrzi uvek _typeMismatch_._modelAttribute_(instanca navedena u form:form koja popunjava polja forme)._purchasePrice_(atribut na koji se poruka odnosi).
###Provera ispravnosti unetih vrednosti

Za proveru ispravnosti unetih podataka se koristi JSR 303: Bean Validation i 
provera s izvrsava se samo u sloju poslovne logike (npr. u metodama InvoiceService klase). 
Ako je korisnik uneo pogresne podatke metoda
 baca com.invado.core.exception.ConstraintViolationException izuzetak sa nizom 
poruka koje bi trebalo obraditi u korisnickom interfejsu tako da korisnik vidi 
ekransku formu sa pogresno unetim podacima i poruku sta bi trebalo da promeni. 
