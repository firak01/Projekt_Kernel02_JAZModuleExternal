FGL 20240412:
Mit dem Ant Script wird eine .jar Datei erstellt, die dann später über eine Batch gestartete werden kann.
Damit nicht bei jedem Build diese .jar Datei neu einbezogen wird, gibt es eine Datei, in der die auszuschliessenden Dateinamen vorhanden sind.

jar-in-jar-loader.zip wird bei jedem Build neu erstellt.
In dem Zielverzeichnis kommt eine weiteres Verzeichnis, mit dem Namen der .jar Datei und dem _lib als Verzeichnisnamen.
Dorthin werden die eingebundenen Bibliotheken ausgelagert.
In der .jar Datei selbst sind nur die compilierten .class Dateien (keine .java Dateien mehr).