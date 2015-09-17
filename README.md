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
###Sigurnost

Za proveru identiteta korisnika (engl. authentication) i proveru prava pristupa lokacijama na serveru(autorizacija) koristi se _Spring Security_ basic HTTP authentication.
Za svaki modul bi u pom.xml trebalo dodati :
```xml
 <!-- Security -->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-web</artifactId>
        </dependency>   
        <!-- Spring Security JSP Taglib -->     
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-taglibs</artifactId>
        </dependency>        
```
U web.xml bi trebalo integrisati Spring Security tj. oznaciti datoteku u kojoj se spring security podesavanja(/WEB-INF/spring-security.xml):
```xml
<context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/application-context.xml /WEB-INF/security.xml</param-value>
</context-param>    
```
i navesti Spring Security filtere:
```xml
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```
u security.xml se navodi koje uloge(engl. role) korisnik mora da ima da bi pristupio resursima na serveru, koja jsp stranica pirkazuje formu za proveru identiteta(form-login tag) i koji servis obavlja proveru za prosledjene podatke iz forme(klasa koja implementira UserDetailsService interfejs i koja je registrovana u Spring kontejneru za ubacivanje zavisnosti sa nazivom _userService_ ). Primer za security.xml moze se videti u finance modulu.
