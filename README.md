# CoinShop – Coins, Server-Shop, Spieler-Shop, Glücksrad und Investitionen

Ein Plugin für **Paper 26.3** (Java 25).

## Was kann das Plugin?

| Befehl | Wirkung |
|---|---|
| `/shop` | Öffnet das Hauptmenü: **Server-Shop**, **Spieler-Shop**, **Glücksrad**, **Investitionen** (und zeigt das Shop-Konto) |
| `/shop sell [Preis]` | Bietet das Item in deiner Hand im Spieler-Shop an (Alternative zum Menü) |
| `/coins [Spieler]` | Zeigt deinen Kontostand oder den eines anderen Spielers |
| `/pay [Spieler] [Betrag]` | Überweist Coins an einen anderen Spieler |
| `/baltop` | Rangliste der 10 reichsten Spieler |
| `/coinsadmin give\|take\|set [Spieler] [Betrag]` | Coins verwalten (nur Admins) |
| `/coinsadmin resetshop` | Server-Shop sofort neu auslosen, ohne Einkommen (nur Admins) |
| `/coinsadmin reload` | `config.yml` und `messages.yml` neu laden (nur Admins) |

### Coins verdienen
- **Erfolge nach Schwierigkeit:** leicht 10 / mittel 30 / schwer 100 / sehr schwer 250 / extrem 600 Coins. Die Stufe richtet sich nach der Erfolgsart (Aufgabe/Ziel/Herausforderung); besonders schwere Erfolge (z. B. Enderdrachen besiegen, Wither, alle Mobs töten) stehen in `config.yml` unter `achievements.tiers`. Einzelne Erfolge lassen sich unter `overrides` anpassen.
- **Börse** (Investitionen -> Börse): Ein Kurs für alle (1-100 Coins), ändert sich täglich zusammen mit dem Shop-Wechsel. Man kauft mit beliebig vielen Coins ganze Anteile und verkauft sie später zum dann gültigen Kurs. 10 % Gebühr bei Kauf und Verkauf (`stock.fee-percent`), die geht aufs Shop-Konto. Kursgewinne sind neu geschaffene Coins, Verluste verschwinden.
- **Tägliches Einkommen:** 10 Coins für jeden Spieler (auch offline), ausgezahlt zusammen mit dem Shop-Wechsel um 12:00 Uhr. Dazu kommt das Einkommen aus Investitions-Sets. Diese Coins werden neu erzeugt.
- **Verkaufen** im Spieler-Shop, **Gewinnen** am Glücksrad.
- Kein Startguthaben.

### Server-Shop
- 10 zufällige Angebote, **feste Preise**, täglich um 12:00 Uhr (Europe/Berlin) komplett neu.
- Es können **alle Items auftauchen, die es im Vanilla-Survival gibt** (Items wie Command-Blöcke, Spawn-Eier, Bedrock usw. sind ausgeschlossen, siehe `exclude` in der config).
- Dazu **verzauberte Rüstungsteile** (12 % pro Platz) und **Verzauberungsbücher** (18 % pro Platz).
- Die seltensten Items kosten bis zu **100.000 Coins** (Netheritblock 100.000, Elytra 37.500 ...). Das Drachenei gibt es im Shop nicht, alle anderen sind daran angelehnt. Teure Items erscheinen seltener.
- Menge pro Kauf ist zufällig (billige Items in großen Mengen, teure einzeln). Jeder Spieler darf jedes Angebot 3x pro Tag kaufen.

### Spieler-Shop
- Spieler bieten Items zu einem frei gewählten Gesamtpreis an. Der Verkäufer bekommt die Coins sofort (auch offline), abzüglich Steuer.
- **Verkaufs-Slots müssen gekauft werden:** der erste Slot ist gratis, der nächste kostet 5 Coins, jeder weitere kostet das Doppelte (10, 20, 40 ...), höchstens 32 Slots. Kauf unter *Spieler-Shop > Meine Angebote*.
- **Schutz gegen Zwischenlager-Missbrauch:** Slots kosten Coins, Einstellgebühr (5 %, nie erstattet), Angebote laufen nach 48 h ab, Preisgrenzen, Verkaufssteuer, Sperrliste.

### Konto des Shops
Alle Einnahmen des Shops landen hier und werden im Hauptmenü angezeigt:
Käufe im Server-Shop, Einstellgebühren, Verkaufssteuern, Slot-Käufe, Glücksrad-Einsätze und Käufe von Investitions-Items.

### Glücksrad
- Eine Drehung kostet 5 Coins (einstellbar), der Einsatz geht aufs Shop-Konto.
- **Jackpot = das gesamte Shop-Konto**, Chance 1 zu 10.000.
- Gewinnstufen: 1 zu 10 = 5 Coins, 1 zu 20 = 10, 1 zu 50 = 50, 1 zu 100 = 100/200/500, 1 zu 1000 = 1000/1500/2000. Alles bezahlt vom Shop-Konto (höchstens so viel, wie darauf ist).
- Die Gewinnchancen sieht man im Menü. Alles ist in der `config.yml` einstellbar.

### Investitionen (Sets)
- **25 Sets mit je 3 Items.** Teuerstes Set: 1000 Coins pro Item und +1000 Coins Einkommen pro Tag. Das billigste kostet 100 pro Item und bringt +100 pro Tag. Dazwischen sinken die Preise gleichmäßig.
- Jedes Item gibt es **nur einmal** auf dem Server (wie Straßen bei Monopoly). Im Menü sieht jeder, wem welches Item gehört.
- 1 Item eines Sets bringt 10 % des Einkommens, 2 Items 35 %, das komplette Set 100 %.
- Items sind **übertragbar:** im Set-Menü auf dein Item klicken, Spielernamen in den Chat schreiben, mit `ja` bestätigen.

---

## So bekommst du die fertige Plugin-Datei (`CoinShop.jar`)

### Weg A: Automatisch auf GitHub bauen lassen (nichts installieren)
1. Öffne dein Repository und lade den **gesamten Inhalt dieser ZIP** hoch. Bestehende Dateien werden dabei ersetzt. Der Ordner `.github/workflows/build.yml` muss erhalten bleiben (hast du schon).
2. Der Bau startet automatisch unter dem Reiter **Actions**.
3. Beim fertigen (grünen) Durchlauf unten bei **Artifacts** `CoinShop` herunterladen. Darin liegt die `CoinShop.jar`.
4. Bei einem roten Durchlauf: Schritt **Run ./gradlew build** öffnen und die Zeilen mit `error:` kopieren.

### Weg B: Auf dem eigenen PC bauen
1. Java 25 installieren (z. B. Temurin von <https://adoptium.net>).
2. In diesem Ordner: Windows `gradlew.bat build`, Mac/Linux `./gradlew build`.
3. Die Datei liegt danach in `build/libs/CoinShop.jar`.

## Installation / Update auf dem Server (Serverminer)

1. Server stoppen.
2. Alte `CoinShop.jar` im Ordner `plugins` durch die neue ersetzen.
3. **Wichtig beim Update:** Lösche im Ordner `plugins/CoinShop/` die Datei **`config.yml`**, damit die neue (mit Preisen, Glücksrad und Sets) erzeugt wird. Die Datenbank `coinshop.db` bleibt erhalten und damit alle Kontostände. Falls du Texte in `messages.yml` angepasst hast, sichere sie vorher. Fehlende Texte werden automatisch ergänzt.
4. Server starten. Es sollte `CoinShop wurde gestartet.` in der Konsole stehen.
5. Zum Testen: `/coinsadmin give DeinName 5000`, dann `/shop`.

## Wichtig: Noch nicht getestet

Der Code wurde geschrieben und auf Tippfehler geprüft, aber **nicht kompiliert und nicht auf einem echten Server getestet**. Beim Bau können noch Fehlermeldungen auftauchen (z. B. weil ein API-Name in Paper 26.3 anders heißt). Besonders neu sind Verzauberungen (Rüstung und Bücher) und die Animation im Glücksrad. Fehlermeldung kopieren, dann wird es behoben.

## Projektaufbau (zum Nachlesen)

```
src/main/resources/
  plugin.yml          Steckbrief: Name, Befehle, Rechte
  config.yml          Einstellungen (Preise, Glücksrad, Slots, Einkommen, 25 Sets)
  messages.yml        Alle Texte (deutsch, änderbar)
src/main/java/de/coinshop/
  CoinShopPlugin.java       Start/Stopp, verbindet alles
  Database.java             Speichert alles in einer SQLite-Datei (coinshop.db)
  Messages.java / ItemUtil.java / ChatPrompts.java   Hilfsklassen (Texte, Items, Chat-Eingaben)
  PlayerListener.java       Beitreten, Erfolge -> Coins, Chat-Eingaben
  CoinCommands.java         /coins /pay /baltop /coinsadmin
  ShopCommand.java          /shop
  ShopItemGenerator.java    Lost die 10 Server-Shop-Angebote aus (Items, Rüstung, Bücher)
  ServerShopManager.java    Täglicher Wechsel um 12:00 Uhr, Kauf im Server-Shop
  PlayerShopManager.java    Slots, Einstellen, Kaufen, Abbrechen, Ablaufen, Abholen
  WheelManager.java         Glücksrad-Logik (Einsatz, Jackpot, Trostpreise)
  InvestmentManager.java    Sets, Besitz, Übertragung, tägliches Einkommen
  gui/                      Alle Menüs (Haupt-, Server-Shop, Spieler-Shop, Meine Angebote,
                            Abholung, Glücksrad, Investitionen, Set)
```


## Update auf Version 3.0
Lösche auf dem Server einmalig `plugins/CoinShop/config.yml` und `messages.yml` (die Datenbank bleibt erhalten), damit die neuen Einstellungen (Börse, Erfolgsstufen) erzeugt werden.

## Update auf Version 4.0
- **Server-Shop ausverkauft:** Jedes Angebot gibt es nur 3-mal insgesamt (`server-shop.stock-per-offer`). Sind alle 14 Angebote ausverkauft, werden sofort neue ausgelost (ohne Einkommen/Börsenkurs).
- **Ranglisten** (Hauptmenü): Diamanterze, Antiker Schutt, Holz, Stein, Angeln, Mobs, wenigste Tode, meiste Tode, Villager-Handel, gewanderte Blöcke, besiegte Bosse (Warden, Enderdrache, Elder Guardian, Wither), platzierte Blöcke und alle abgebauten Blöcke. Die Zahlen kommen aus den Minecraft-Statistiken (zählen also auch Dinge von vor dem Plugin). Die Top 5 bekommen +10/+5/+3/+2/+1 Coins Tageseinkommen pro Liste (`rankings.bonus`). "Wenigste Tode" zählt erst ab 120 Minuten Spielzeit.
- **Geheime Erfolge** (Hauptmenü): 11 Stück, sichtbar erst nach dem Freischalten. Belohnungen in `secret-achievements`, Texte in `messages.yml` unter `secret`.
- Nach dem Update `plugins/CoinShop/messages.yml` löschen (damit die neuen Texte kommen). Die `config.yml` ergänzt sich fehlende Teile selbst, wenn du sie aber neu erzeugen lässt, hast du alle neuen Werte sichtbar.

## Update auf Version 5.0
- **Schlafen:** Schlafen 10 % der aktiven Spieler (nicht AFK, nicht im Zuschauer-Modus) einer Welt, läuft die Nacht im Zeitraffer ab. Es wird nur die Tageszeit beschleunigt, nichts sonst (`sleep.*` in der config.yml).
- **Teleportieren:** `/tp spawn` (oder `/spawn`), `/sethome`, `/home`, `/tpa [Spieler]`, `/tpaccept`, `/tpdeny`. Jede Teleportation startet nach 3 Sekunden (`teleport.delay-seconds`). Der Spawn ist der Welt-Spawn (mit dem normalen Befehl `/setworldspawn` änderbar). Admins können `/tp` weiterhin normal benutzen.
- **Freecam:** `/freecam` (oder `/cam`) schaltet ein und aus. Du fliegst im Zuschauer-Modus, aber nicht durch Blöcke (nur durch Glas) und höchstens 64 Blöcke weit weg. Dein Körper bleibt als Figur stehen, trägt deine Rüstung, hat deine Lebenspunkte und wird von Monstern angegriffen. Stirbt die Figur, stirbst du an dieser Stelle.
- **Suche im Spieler-Shop:** Knopf mit Fernglas unten (Linksklick = Suchbegriff in den Chat schreiben, Rechtsklick = Suche löschen). Auch deutsche Wörter wie Diamant oder Schwert funktionieren.

## Update auf Version 6.0
- **Server-Shop:** 14 Angebote, Elytra und Drachenei gibt es dort nicht mehr.
- **Shop-Reset:** `/shopreset` (nur Admins; auch `/coinsadmin resetshop`) macht den kompletten Reset: neue Angebote, neuer Börsenkurs und das tägliche Einkommen wird ausgezahlt (genau wie um 12:00 Uhr). Ist der Shop nur ausverkauft, werden dagegen nur die Angebote neu gemischt.
- **Ranglisten:** "Mobs" heißt jetzt "Kreaturen" (alle Tiere, Monster, Dorfbewohner ...), neu ist die Rangliste "Spielzeit". Haben mehrere Spieler denselben Wert, teilen sie sich die Boni ihrer Plätze fair (ein Rest-Coin geht per Zufall an einen von ihnen).
- **Einkommens-Übersicht:** Investitionen > Uhr-Symbol. Zeigt für jeden Spieler das Einkommen (Grundeinkommen, Sets, Ranglisten) und den reichsten Spieler.
- **Kollektionen** (Hauptmenü): 13 Stück (Schallplatten, Rüstungsverzierungen, Keramikscherben, Köpfe, Netherit-Ausrüstung, Seltene Schätze, Rohstoffe, Farbstoffe, Wolle, Korallen, Blumen, Setzlinge, Fische). Wer alle Items einer Kollektion einmal besessen hat, bekommt die Belohnung. Eigene Kollektionen kannst du in der config.yml unter `collections` ergänzen.
- **Sets teurer:** Ein Item kostet jetzt das 20-fache des Set-Preises (teuerstes Set: 20.000 Coins pro Item). Das Einkommen bleibt gleich (`investments.price-multiplier`).
- **Glücksrad:** Eine Drehung kostet 10 Coins. Das Gewinnfeld bleibt nach der Drehung stehen und zeigt genau den ausgezahlten Betrag.
- **Geheimer Erfolg "Big Tasty Bacon":** Wird jetzt durch das Töten eines Zombifizierten Piglins freigeschaltet.

## Update auf Version 6.1
- **Neue geheime Erfolge:** grünen Teppich platzieren, zwei Frösche paaren.
- **Kreaturen-Kollektionen:** Monster der Oberwelt, Nether-Kreaturen, Bauernhoftiere, Wildtiere, Wasserwesen, Dorfbewohner und Helfer, Bosse. Eine Kreatur zählt, wenn du sie getötet oder durch ein Fernglas angesehen hast.

## Update auf Version 7.0
- **Investitionen mit Zwischenmenü:** Hauptmenü > Investitionen öffnet jetzt ein Menü mit drei Bereichen: **Item-Sets**, **Börse** und **Einkommen**. Von dort geht es mit dem Zurück-Knopf immer eine Ebene zurück.
- **"Anteile" heißen jetzt "Aktien"** (Börse, Texte in `messages.yml`).
- **Server-Shop Preise:** Schwache Rüstung ist günstiger (Leder 25, Kupfer 60, Gold 100, Kette 200, Eisen 150, Schildkrötenpanzer 300). Verzauberungen zählen bei schwachen Materialien nur anteilig (`server-shop.enchant-material-factor`, z. B. Leder 25 %, Eisen 60 %, Diamant/Netherit 100 %).
- **Verzauberte Werkzeuge und Waffen:** Schwerter, Äxte, Spitzhacken, Schaufeln, Hacken, Bögen, Armbrüste, Dreizacke, Streitkolben und Angeln können jetzt verzaubert im Server-Shop auftauchen (`server-shop.enchanted-tool-chance`).
- **Karten** gibt es nur noch einzeln und kosten 750 Coins (`server-shop.single-only`, `prices.MAP`).
- **Teurere Verzauberungen:** Hohe Stufen werden überproportional teurer (`enchant-level-exponent`). Verzauberungen, die es nicht am Verzauberungstisch gibt, sind besonders teuer: Windstoß 10.000, Schleichtempo 10.000, Seelenläufer 9.000, Frostläufer 8.000, Reparatur 6.000 (die günstigste davon) (jeweils pro Stufe I).
- **Neue Kollektion:** Bannermuster.
- Nach dem Update `plugins/CoinShop/messages.yml` UND `config.yml` löschen (neue Preise und Texte).

## Update auf Version 8.0
- **Tägliche Aufgaben** (Hauptmenü oder `/aufgaben`): Jeden Tag (Wechsel um 12:00 Uhr) gibt es 3 Aufgaben für alle, z. B. "Baue 300 Blöcke ab". Belohnung je nach Größe der Aufgabe, plus Bonus für alle drei. Pool und Werte: `quests` in der config.yml.
- **Login-Serie (Teil des Einkommens):** Wer täglich (12:00 bis 12:00 Uhr) auf dem Server ist, baut eine Serie auf. Sie gibt einen Serien-Bonus (+5 pro Tag bis +30, jeder 7. Tag +100), der zusammen mit dem Grundeinkommen beim Shop-Wechsel ausgezahlt wird. Beim Beitreten gibt es keine separate Auszahlung. Einstellungen: `login-streak`.
- **Startbonus:** Neue Spieler bekommen einmalig 100 Coins (`coins.start-bonus`).
- **Titel** (Hauptmenü oder `/titel`): Titel stehen in Chat und Tab-Liste vor dem Namen. Platz 1 jeder Rangliste und der Reichste bekommen einen Titel, solange sie Erster sind. Dauerhafte Titel gibt es für Leistungen (Wächter des Reiches, Hüter der Reliquien, Wissender der Schatten, Held ohne Rast, Drache auf dem Goldhort, Legende des Servers). **Mehrere Titel:** Man kann beliebig viele besitzen, trägt aber immer nur einen. Im Menü wählt man einen aus, "Automatisch" nimmt den wichtigsten, "Kein Titel" schaltet ihn aus. Verliert man den gewählten Titel (jemand überholt einen), wird automatisch gewechselt.
- **Admin-Werkzeuge:** `/coinsadmin stats` (Wirtschaft: Coins im Umlauf, Durchschnitt, Reichster, Umsätze der letzten 24 Stunden), `/coinsadmin log [Spieler] [Anzahl]` (letzte Coin-Bewegungen, 60 Tage gespeichert), `/coinsadmin backup` (Sicherung jetzt).
- **Sicherungen:** Die Datenbank wird beim Start, alle 6 Stunden und beim Ausschalten nach `plugins/CoinShop/backups` kopiert (die neuesten 20 bleiben). Zurückspielen: Server aus, Sicherung nach `plugins/CoinShop` kopieren und in `coinshop.db` umbenennen.
- **Mehr Erfolge und Kollektionen:** 1 neuer geheimer Erfolg (20 Blöcke Fall überleben) und 4 neue Kollektionen (Betten, Teppiche, Beton, Glasierte Terrakotta). Menüs für Kollektionen und geheime Erfolge sind größer.
- Nach dem Update `plugins/CoinShop/messages.yml` UND `config.yml` löschen.

## Update auf Version 9.0
- **Spieler-Profil:** `/profil` (eigenes) oder `/profil Name` (auch für Spieler, die offline sind). Im Hauptmenü unter "Mein Profil", außerdem per Klick auf einen Spielerkopf in der Einkommens-Übersicht. Zeigt Kontostand und Platz in der Reichenliste, Titel, Spielzeit, Login-Serie, Einkommen, Erfolge, Kollektionen, Aufgaben, Statistiken (abgebaut, platziert, gewandert, Kreaturen, Bosse, Angeln, Handel, Tode), Ranglisten-Platzierungen (Top 10) und Investitions-Items. Die Börse bleibt privat. Die Zahlen von Offline-Spielern stammen vom letzten Speichern (beim Verlassen des Servers).

## Update auf Version 10.0
- **Kollektionen:** Wolle, Teppiche und Beton sind weg. Neu: **Eimer-Varianten** (11 Eimer, 2.500 Coins) und **Alle Axolotl-Farben** (5 Farben, 4.000 Coins). Eine Axolotl-Farbe zählt, wenn du einen Axolotl-Eimer dieser Farbe im Inventar oder in der Endertruhe hattest oder einen Axolotl dieser Farbe durchs Fernglas gesehen/getötet hast. Die Belohnung der Netherit-Kollektion ist von 20.000 auf 10.000 Coins gesunken. (Wer Wolle/Teppiche/Beton schon belohnt bekommen hat, behält die Belohnung.)
- **Einkommen per Klick:** Ein Klick auf deinen Kontostand (in jedem Menü) zeigt, woraus dein tägliches Einkommen besteht: Grundeinkommen, Login-Serie, jedes Set und jede Rangliste mit Bonus, dazu die Summe.
- **Casino:** Im Hauptmenü gibt es jetzt das **Casino** statt des Glücksrads. Dort: **Glücksrad**, **Spielautomat** (20 Coins, drei gleiche Symbole) und **Rubbellos** (5 Coins, sechs Felder aufdecken, dreimal gleich gewinnt). Alle drei zahlen vom Shop-Konto, haben ein paar Trostpreise und einen Jackpot (ganzes Shop-Konto) mit Chance 1 zu 10.000. Preise und Chancen stehen in der config.yml unter `casino` (Spielautomat, Rubbellos) und `wheel` (Glücksrad).

## Update auf Version 11.0
- **Epische Titel:** Alle Titel haben neue Namen (config.yml, Abschnitt `titles`), z. B. "Diamantenfürst der Tiefe", "Henker der Nacht", "Schrecken der Meere", "Herr über Gold und Gier". "Märtyrer" bleibt.
- **Casino neu abgestimmt:** Glücksrad 25 Coins (Jackpot 1:15.000), Spielautomat 50 Coins (Jackpot 1:20.000, Gewinne bis 8.000), Rubbellos 5 Coins (Jackpot 1:12.000, kleine Gewinne). Im Schnitt kommen rund 55 % des Einsatzes zurück.
- **Neues Spiel "Doppelt oder Nichts":** Einsatz 10 / 50 / 250 wählen, dann immer wieder verdoppeln (jede Verdopplung klappt zu 40 %) oder auszahlen. Höchststufe nach 7 Verdopplungen (Einsatz x 128, wird automatisch ausgezahlt). Ab Stufe 3 gibt es bei einer Niederlage 50 % des Einsatzes als Trostpreis. Wer den Server verlässt, bekommt seinen aktuellen Gewinn automatisch. Einstellungen: `casino.double` in der config.yml.
- **4 neue Kollektionen** (jetzt 28): "Rudel der Wölfe" (alle 9 Wolfsarten, 6.000), "Samtpfoten aller Art" (alle 11 Katzenarten, 6.000), "Meister der Verzauberung" (jede Verzauberung einmal besessen, 20.000), "Bunte Tierwelt" (alle Papageien, Frösche und Kaninchen, 5.000). Wolf-, Katzen-, Papagei-, Frosch- und Kaninchen-Arten zählen, wenn du ein Tier der Art tötest oder durch das Fernglas ansiehst.
- **Phantome:** Sie erscheinen nur noch bei Vollmond (Oberwelt) und im End. Im End lässt das Plugin in der Nähe von Spielern welche erscheinen (`phantoms` in der config.yml).

## Update auf Version 12.0
- **Titel:** Alle Titel haben höchstens zwei Wörter (außer "Henker der Nacht"): Goldkaiser, Diamantenfürst, Höllenschürfer, Waldschreck, Felsflüsterer, Köder-Casanova, Unsterblicher, Märtyrer, Smaragdbaron, Sohlen-Sultan, Titanenbezwinger, Bob, Erdenbrecher, Dauerbrenner; dauerhaft: Treues Stück, Hamster, Geheimnisträger, Rastloser Held, Scheich, Serverlegende.
- **Lizenzen** (`/lizenzen` oder Hauptmenü): 12 Lizenzen, einmal kaufen, für immer behalten. Steuerfrei, Gebührenfrei (Spieler-Shop), Casino-Freispiel (1 Spiel/Tag gratis), Stammkunde (10 % Rabatt im Server-Shop), Rentner (+40 Coins/Tag), Marktstand (+4 Slots), Treuekarte (+50 % Login-Serien-Bonus), Investor (+10 % Einkommen aus Item-Sets), Börsenhändler (5 % Börsen-Gebühr), Ruhmjäger (+25 % Ranglisten-Bonus), Fleißbiene (+25 % Aufgaben-Belohnung), Sammelwut (+15 % Kollektions-Belohnung). Preise und Werte: `licenses` in der config.yml.
- **Endkampf:** Bis zum Datum in `end-fight.start` (config.yml) ist das End gesperrt. Wer ins End-Portal geht, bekommt ein Anmelde-Menü. Zur Startzeit werden alle Angemeldeten, die online sind, ins End teleportiert; der Drache wird mit Strahlen, Blitzen und Geräuschen beschworen. Je mehr Kämpfer, desto mehr Leben und Schaden und desto häufiger die Angriffe. Neue Angriffe: Feuerball-Salven, Blitzschläge (mit Warnung), Phantom-Schwärme und ein Schrei, der zurückwirft. Bei 50 % wird er wütend, bei 25 % ruft er Verstärkung. Danach ist das End offen; alle, die beim Tod im End sind, bekommen 5.000 Coins. Befehle: `/endkampf` (Status), `/endkampf abmelden`, für Admins `/endkampf start|stop|reset` (zum Testen). Wer das End trotzdem betreten muss: Berechtigung `coinshop.endbypass`.

## Update auf Version 12.1
- **/farmwelt** (auch /farm): Teleport zu einem festen Punkt, mindestens 100.000 Blöcke vom Spawn entfernt (100.000 bis 120.000, in zufälliger Richtung). Der Punkt ist für alle gleich und wechselt jeden Tag zusammen mit dem Shop-Wechsel (12:00 Uhr). Liegt er im Wasser, wird in der Nähe ein sicherer Platz gesucht. Einstellungen: `farmworld` in der config.yml.
- **Titel:** Treues Stück (Login-Serie), Hamster (Kollektionen), Scheich (Coins).
- **Lizenzen:** Glückspilz und Eilreise sind ersetzt durch **Investor** (+10 % Einkommen aus Item-Sets, 25.000) und **Ruhmjäger** (+25 % Bonus aus den Ranglisten, 20.000). Die Beschreibung von Langzeitmarkt nennt jetzt ausdrücklich den Spieler-Shop.


## Update auf Version 13.0

- **Spieler-Shop:** Angebote bleiben dauerhaft im Shop, bis sie verkauft oder zurückgenommen werden. Alte Angebote werden beim Start ebenfalls dauerhaft gemacht.
- **Lizenz Treuekarte** (12.000 Coins) ersetzt den Langzeitmarkt: +50 % auf den Login-Serien-Bonus.
- **Serverstart 6. Dezember 2026, 12:00:** Vorher können nur Admins (OPs) beitreten, alle anderen sehen den Countdown. Einstellbar unter `schedule:` in der config.yml.
- **Nether** öffnet am 10. Dezember 2026, 12:00 (Auslegung von "nach dem 9. Dezember"). Vorher sind Nether-Portale und Teleports gesperrt; am Portal öffnet sich ein Countdown-Menü. Zur Öffnung gibt es eine Meldung an alle. Ausnahme: Recht `coinshop.netherbypass`.
- **/termine** zeigt Countdown zu Serverstart, Nether und Endkampf.
- **Endkampf** jetzt am 20. Dezember 2026, 12:00 (`end-fight.start`).


## Update auf Version 14.0 (Meinecraft Winterzeit 2)

- **Titel umbenannt:** Bau-Titan heißt jetzt Bob, Treuer Titan wird Treues Stück, Sammelgott wird Hamster, Münzmagnat wird Scheich.
- **Marktstand:** Die 4 Slots kommen zusätzlich zu Gratis- und gekauften Slots. Der Preis der nächsten gekauften Slots ändert sich nicht, die Gesamtgrenze von 32 Slots gilt aber weiter.
- **Freecam:** Radius 100 Blöcke (waagerecht), keine Grenze in Höhe und Tiefe (`freecam.max-distance`).
- **Testphase ab 3. Dezember 2026, 00:00** bis zum Start am 6. Dezember, 12:00 (`schedule:` in der config.yml): Alle dürfen beitreten, aber nur im Umkreis von 500 Blöcken um den Spawn. Es spawnen keine Monster/Tiere, niemand nimmt Schaden oder stirbt, nichts kann abgebaut, gebaut, eingesammelt oder hergestellt werden. Es zählen keine Coins, Erfolge, Aufgaben, Login-Serie, Kollektionen und geheimen Erfolge. Das Startguthaben gibt es erst zum offiziellen Start. Admins (OPs) sind ausgenommen.
- **Server-Icon und Logo** liegen als Dateien bei (server-icon.png gehört in den Server-Hauptordner).


## Update auf Version 15.0

- **Safe-Zone:** Im Umkreis von 500 Blöcken um den Spawn der Hauptwelt kann dauerhaft nichts abgebaut werden (Blöcke, Eimer, Rahmen/Bilder). Explosionen, Feuer und Monster verändern dort keine Blöcke. Bauen bleibt erlaubt (`safe-zone.block-place: true` sperrt es ebenfalls). Admins sind ausgenommen. Einstellungen: `safe-zone:` in der config.yml.
- **Minecraft 26.3:** Neue Kollektion "Neuheiten 2026" (Cushions, Golden Dandelion, Straw Bed, Shelf Mushroom, Sulfur Cube). Einträge, die es in der Server-Version nicht gibt, werden einfach übersprungen. Das Kollektions-Menü hat jetzt Platz für 35 Kollektionen.
- **Logo:** kleine Herbstbäume als Anspielung auf den Dappled Forest.


## Update auf Version 16.0

- **Kollektionen:** "Neuheiten 2026" ersetzt die Kollektion Betten, es sind wieder 28 Kollektionen.
- **Safe-Zone dauerhaft wie in der Testphase:** Im Umkreis von 500 Blöcken um den Spawn kann nichts abgebaut oder platziert werden, nichts explodiert/brennt, keine neuen Monster oder Tiere spawnen (vorhandene Monster werden entfernt), Spieler nehmen keinen Schaden und werden nicht hungrig. Shop, Coins, Inventar und Truhen funktionieren normal. Admins dürfen bauen. `safe-zone.block-place: false` erlaubt das Bauen für alle.
- **Border in der Testphase:** Testphase-Spieler sehen eine Grenze (500 Blöcke um den Spawn). Sie ist nur für sie sichtbar und verschwindet zum offiziellen Start (spätestens 10 Sekunden danach).
- **Logo:** mit Herbstbäumen, Cushions, Schwefelblock und Golden Dandelion. Das Handout ist bunter und nutzt die Logo-Farben.


## Update auf Version 17.0

- **Server-Shop:** Neben normalen Items gibt es jetzt Item-Variationen (`server-shop.variant-chance`, Standard 20 % der Angebote): alle Trank-Arten als Trank/Wurftrank/Verweiltrank/Pfeil, alle Verzauberungsbücher in allen Stufen (auch Flüche), Ziegenhörner, Verdächtiger Eintopf, Ominous Bottles, Feuerwerk, Axolotl- und Tropenfisch-Eimer, Rüstung mit Besatz, gefärbte Lederrüstung. Preise unter `variant-prices` und `potion-base`.
- **Preise für jedes Item:** Neue Preisrechnung: config-Eintrag, sonst Zutaten (Rezept + 10 %), sonst Standardpreis. Viele natürliche Items und Drops haben jetzt eigene Preise. `/coinsadmin preise` schreibt `preisliste.txt` und `preise-fehlen.txt` in den Plugin-Ordner (passiert auch 10 Sekunden nach dem Start). Items in `preise-fehlen.txt` haben nur den Standardpreis.
- **Voice-Chat-Mod:** Sprache löst Sculk-Vibrationen aus, Monster hören Sprechende im Radius 40 und laufen zur Quelle. Flüstern und Schleichen sind leiser. Alles unter `voicechat:` in der config.yml. Die Mod muss auf dem Server liegen (Simple Voice Chat, softdepend).

## Update auf Version 18.0

- **Warden und Voice-Chat:** Der Wächter hört jetzt auch den Proximity-Voice-Chat. Je öfter du sprichst, desto wütender wird er (ab 80 Wut greift er an). Einstellbar unter `voicechat.warden` (an/aus, Hörradius 32, Wut pro Impuls 30). Schleichen und Flüstern verkleinern den Radius wie bei anderen Monstern.
- **Ausverkaufte Slots werden ersetzt:** Ist ein Angebot im Server-Shop ausverkauft (alle 3 Stück gekauft), kommt an dessen Platz sofort ein neues Item (kein Duplikat der anderen Angebote). Alle Spieler bekommen eine Meldung. Nur dieser Slot wird erneuert, die anderen bleiben.
- **Jedes Item hat einen Preis:** ca. 140 weitere Preisregeln in der `config.yml` (Kupfer, Karten, Beton, Bündel, Knospen usw.).
- Neue Nachricht `server-slot-replaced` in der `messages.yml`.

Update: `src`, `README.md` auf GitHub ersetzen, auf dem Server `config.yml` und `messages.yml` im Ordner `plugins/CoinShop` löschen (Datenbank bleibt).

## Update auf Version 18.1

- **Tode in der Tab-Liste:** Neben jedem Namen steht die Zahl der Tode (aus der Minecraft-Statistik, also auch frühere Tode). Abschaltbar mit `tab-deaths.enabled: false`.

Update: `src`, `README.md` ersetzen, `config.yml` auf dem Server löschen.

## Update auf Version 18.2

- **Fehler behoben:** Die Preisberechnung hat Zwischenergebnisse nicht gemerkt und den Server beim Start praktisch endlos beschäftigt (Status blieb auf "starting"). Jetzt wird jedes Item nur einmal berechnet.

Update: nur `src` auf GitHub ersetzen und neu bauen lassen.

## Update auf Version 19.0

- **7 neue Kollektionen (jetzt 35):** Alle Ziegenhörner (jeder Ruf einzeln), Feinschmecker (Essen), Werkzeuge und Waffen, Rüstungsteile, Redstone-Technik, Alle Holzarten, Braustand-Zutaten. Das Kollektionsmenü hat genau 35 Plätze, mehr geht nicht.
- **Meine Platzierungen:** Im Ranglisten-Menü gibt es einen neuen Knopf (Fernglas). Dort siehst du in allen 14 Ranglisten auf einen Blick deinen Platz, deinen Wert, den Tagesbonus und den Abstand zum Spieler vor dir.
- **Nether-Sperre repariert:** Das Menü blitzte nur kurz auf, weil Minecraft jedes Menü sofort schließt, solange man im Portal steht. Jetzt wird man erst ein Stück aus dem Portal geschoben und das Menü öffnet sich danach.
- **Endkampf:** Der Drache hat jetzt eine eigene Lebensanzeige (Balken mit genauen Lebenspunkten, wird bei 50 % gelb und bei 25 % rot). Die Beschwörung läuft über Glocken: alle 3 Sekunden ein Schlag, nach dem 10. Schlag erscheint der Drache. Einstellbar unter `end-fight.bells` und `end-fight.bell-interval-seconds`.

Update: `src` und `README.md` ersetzen, auf dem Server `config.yml` und `messages.yml` im Ordner `plugins/CoinShop` löschen (Datenbank bleibt).
