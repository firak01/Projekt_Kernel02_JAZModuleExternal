REM ALS AUSFÜHRBARE DATE MIT EINEM FESTGELEGTEN STARTPUNKT: java -jar OpenVPNZZZ_V20200613.jar -d . -f ZKernelConfig_OVPNServer.ini > c:\fglkernel\kernellog\OpenVPNZZZ_StarterLog.txt
REM Beliebige Klasse mit einer Methode: public static void main(String[] args) {} starten. 
REM Merke: Wenn die JAR - Datei nicht in dem Verzeichnis der Batch liegt, den Pfad vorneweg ergänzen.
REM TEST IST HIER: JAZKernel\test\basic\zBasic\util\file\JarEasyZZZTestMain.java

REM java -cp C:\1fgl\client\JAZKernel\JAZModuleExternal.jar debug.zBasic.util.moduleExternal.process.create.ProcessCreateMainZZZ > c:\fglkernel\kernellog\ProcessCreate_StarterLog.txt

REM Startet ein Konsolenfenster mit dem Namen. Darin wird dann der Prozess ausgefuehrt.
start "fglProcessCreateMainZZZ" java -cp C:\1fgl\client\JAZKernel\JAZModuleExternal.jar debug.zBasic.util.moduleExternal.process.create.ProcessCreateMainZZZ > c:\fglkernel\kernellog\ProcessCreate_StarterLog.txt
pause