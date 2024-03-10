REM ALS AUSFÜHRBARE DATE MIT EINEM FESTGELEGTEN STARTPUNKT: java -jar OpenVPNZZZ_V20200613.jar -d . -f ZKernelConfig_OVPNServer.ini > c:\fglkernel\kernellog\OpenVPNZZZ_StarterLog.txt
REM Beliebige Klasse mit einer Methode: public static void main(String[] args) {} starten. 
REM Merke: Wenn die JAR - Datei nicht in dem Verzeichnis der Batch liegt, den Pfad vorneweg ergänzen.
REM TEST IST HIER: JAZKernel\test\basic\zBasic\util\file\JarEasyZZZTestMain.java

REM Startet die Javaklasse direkt. Darin wird dann der Prozess ausgefuehrt.
REM java -cp C:\1fgl\client\JAZKernel\JAZModuleExternalDummy.jar debug.zBasic.util.moduleExternal.process.createDummy.ProcessCreateMockDummyMainZZZ > c:\fglkernel\kernellog\ProcessCreate_StarterLog.txt
REM java -cp JAZModuleExternalDummy.jar debug.zBasic.util.moduleExternal.process.createDummy.ProcessCreateMockDummyMainZZZ > c:\fglkernel\kernellog\ProcessCreate_StarterLog.txt

REM Startet ein Konsolenfenster mit dem Namen. Darin wird dann der Prozess ausgefuehrt.
REM Leider wird die .jar Datei ohne den absoluten Pfad nicht gefunden, auch wenn sie im gleichen Verzeichnis wie diese Batch liegt.
REM start "fglProcessCreateMainZZZ" java -cp C:\1fgl\client\JAZKernel\JAZModuleExternalDummy.jar debug.zBasic.util.moduleExternal.process.createDummy.ProcessCreateMockDummyMainZZZ
REM auf dem Notebook der TUBAF: start "fglProcessCreateMainZZZ" java -cp C:\HIS-Workspace\1fgl\repo\EclipseOxygen\Projekt_Kernel02_JAZModuleExternal\src\bat\JAZModuleExternalDummy.jar debug.zBasic.util.moduleExternal.process.createDummy.ProcessCreateMockDummyMainZZZ
start "fglProcessCreateMainZZZ" java -cp C:\1fgl\repo\EclipseOxygen_V01\Projekt_Kernel02_JAZModuleExternal\src\bat\JAZModuleExternalDummy.jar debug.zBasic.util.moduleExternal.process.createDummy.ProcessCreateMockDummyMainZZZ
pause