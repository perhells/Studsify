# # Kodskelett för Studs2016/Dewire-event
Github-repository för kodskelett och instruktioner för uppgiften i 
Studs2016-eventet hos Dewire Consultants AB.



## Uppgiften
Uppgiften går ut på att bygga en Androidapp som kan användas som ett slags tangentbord
utan att touchskärmen används till inmatning. Detta innebär således att det är tillåtet
att presentera information med skärmen. Endast att vidröra skärmen för att mata in text
är alltså förbjudet. Hårdvaruknappar är tillåtna. Endast svenska gemener samt 
mellanrum behöver kunna skrivas. Se `Tävlingssystemet` för fler detaljer.

####Begränsningar
Appen får byggas med med enda begränsningen att kodbibliotek och API:er som i sin helhet
utgör en lösning inte får användas. Ingen extern hårdvara får användas, såsom handsfree,
USB-tillbehör, bluetooth, extra telefoner eller tangentbord. Vid tveksamheter, prata med 
någon från Dewire.

####Länkar
En lista på sensorer finns här:
http://developer.android.com/guide/topics/sensors/sensors_overview.html

Här finns instruktioner för hur man aktiverar utvecklaralternativ på androidenheter:
http://www.greenbot.com/article/2457986/how-to-enable-developer-options-on-your-android-phone-or-tablet.html

#### Kodskelettet
Kodskelettet bör kompilera på samtliga androidenheter med version 4.4 (KitKat) eller uppåt.
Kodskelettet får modifieras fritt, med undantag för filen DewireContestConnection.java.




## Tävlingssystemet
På sidan http://studs16.dewi.re kan man finna tävlingssystemet. Här kan man se alla
anslutna deltagare och se hur tävlingen presenteras och fungerar. Tryck `Enter` för att 
starta tidtagning och `Esc` för att nollställa. 

#### Anslutning till tävlingssystemet
När applikationen startas så anropas MainActivity.java. Detta är mer eller mindre där
all kod som definierar appen utgår ifrån i någon form. Notera att MainActivity.java
innehåller en rad där ni måste byta ut standard-grupptaggen mot er egen grupptagg.

Ändra
```
private static String GROUP_TAG = "Grp-"+Math.abs((new Random()).nextInt()%100000);
```
till er grupptagg, exempelvis:
```
private static String GROUP_TAG = "NinjaBears";
```

Efter uppstart kommer appen därefter använda klassen DewireContestConnection för att ansluta 
till tävlingssystemet, och under tävlingen ska MainActivity.java-funktionen
```
public void updateString(String str)
```
användas för att ladda upp sin skrivna textsträng med. Detta skall göras upprepat under tiden 
man varje gång en ny bokstav tillkommer eller tas bort, dock åtminstone när man skrivit klart, 
annars registreras inte tiden. För att förtydliga, hela din textsträng ska skickas in, rätt eller 
fel oavsett. Kalla ej på denna funktion överdrivet eller onödigt mycket, då det skapar onödig trafik
till tävlingssystemet.

Kom ihåg att om updateString används innan tävlingen startat så kan andra deltagare se hur snabbt ni skriver!

Vid problem, prata med någon från Dewire.




## Tips och Tricks för att koda Androidfrontend

Androidappar är kompletta program anpassade för operativsystemet Android, där det grundläggande
programmeringsspråket är Java. 

Som då kan anas så definieras både det grafiska interfacet och logik i en androidapp, där interfacet
definieras i layout-XML:er. Exempelvis så definieras det grafiska interfacet för MainActivity i filen
`/Studs2016Template/app/src/main/res/layout/activity_main.xml`. Här definieras alla frontend-element, och
sen knyter man kod till detta interface i `MainActivity.java`. Exempelvis, om man vill interagera
med textfältet i interfacet så måste man först notera dess typ `TextView` och sen dess id-property
`android:id="@+id/hellotext"`. 
En `TextView` är precis som alla andra tänkbara element i en layout också i grunden en `View`.
Först när en activity skapas i `protected void onCreate(Bundle savedInstanceState)` så måste man
definiera layouten som ska användas, och därefter instansieras alla alla Views i den, och dessa
kan användas. Detta görs automatiskt av `setContentView(R.layout.activity_main);`. Här refererar
`R.layout.activity_main` till `activity_main.xml`.

Sen kan man exempelvis ändra text i textvyn:
Här är id-värdet `hellotext` för textvyn, och då kan man lägga in kod i MainActivity.java
för att använda den. Detta kan göras när som helst efter initialisering.
```
TextView t = (TextView)findViewById(R.id.hellotext);
t.setText("The quick brown fox...");
```

Notera att alla Views som hämtas från activityn via id tolkas alltid som View, men för att exempelvis modifiera den som
en textvy här så måste man konvertera om till rätt typ, och sen kan dess metoder användas.

