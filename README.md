# CRM

## Uputstvo za programere

###Formatiranje polja ekranske forme

Trebalo bi koristiti Spring org.springframework.format.annotation.DateTimeFormat za formatiranje datuma i 
org.springframework.format.annotation.NumberFormat za formatiranje procenata, brojeva i novcanih iznosa. 
U svakom modulu bi trebalo podesiti Spring MVC da koristi klasu com.invado.core.format.FormattingConversionServiceFactoryBean 
kao servis za konverziju(engl. conversion service). U XML datoteci bi trebalo da imate :

1. <mvc:annotation-driven conversion-service="conversionService">
2. <bean id="conversionService" class="com.invado.core.format.FormattingConversionServiceFactoryBean" />

U properties datoteci za svako polje morate uneti poruku koju korisnik vidi ako pogresno unese vrednost npr.
typeMismatch.java.time.LocalDate=Uneta vrednost se ne može formatirati kao datum

###Provera ispravnosti unetih vrednosti(engl. validation)
