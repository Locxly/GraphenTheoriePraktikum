ReadMe Start GKAPraktikum JAR
-----------------------------

Das JAR wird wie folgt gestartet:

java -jar GKAPraktikum.jar howto

Dadurch erhält man folgendes Manual:

Please note the following use instruction : Type arg[0] as ...
0 -> default use of params.
1 -> arg[1] contains filename.
2 -> arg[1] start vertex, arg[2] destination vertex.
3 -> arg[1] filename, arg[2] start vertex, arg[3] destination vertex.

Die Steuerung wird das erste Argument der Argumentenliste durchgeführt. Das erste Argument steuert die Verwendung der folgenden.

Als default-Einstellung für den Filenamen (Start-Mode 0 und 2) wird vorrausgesetzt, dass im Verzeichnis, in dem die JAR Datei gestartet wird, die Datei graph_01.graph zur Verfügung steht.

Ein Start ohne Argumente führt zu Fehlern.