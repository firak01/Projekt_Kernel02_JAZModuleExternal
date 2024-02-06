package debug.zBasic.util.moduleExternal.log.watch;

import java.io.File;

import base.files.DateiUtil;
import base.io.IoUtil;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.crypt.code.Vigenere256ZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.LogFileWatchRunnerZZZ;
import basic.zKernel.status.IListenerObjectStatusBasicZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalMessageReactZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalSetZZZ;
import basic.zKernel.status.ISenderObjectStatusBasicZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalMessageReactZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalMessageSetZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerZZZ;
import debug.zBasic.util.moduleExternal.log.create.LogFileCreateMockRunnerZZZ;

/** In dieser Klasse wird ein LogFile von dem einen Thread erzeugt 
 *  und gleichzeitig von einem anderen Thread ausgewertet.*  
 *  Findet der Auswertungsthread einen bestimmten Ausdruck im LogFile, 
 *  wirft er einen Event.
 *
 *  Die Methode .reactOnStatusLocalEvent(Event) der am EventBroker registrierten anderen Processe wird aufgerufen. 
 *  Als Beispielsreaktion auf den Event werden diese nun beendet.  
 *  
 *  
 *  TODOGOON20240204; 
 *  In dieser Variante soll mit einem Monitor gearbeitet werden, der die Threads verwaltet
 *  und die entsprechenden Stati durchreicht an die jeweilige Hauptklasse.
 *  Das passiert durch einen Event, der vom Monitor geworfen wird. 
 *  Dabei wird der Event auf einen anderen Event gemappt.
 *  
 *  Auf eben diesen anderen Event "horcht" die Hauptklasse.
 *  
 *  
 * @author fl86kyvo
 *
 */
public class LogFileWatch_2_MonitorProcessStyle_MainZZZ implements IConstantZZZ{

	public static void main(String[] args) {
			main:{
			  try {	  
			  //TODOGOON; //Mache Utility-Methoden im Consolen - Output
			  //          //- Erstelle eine Box mit Anzahl Breite Zeichen insgesamt, Rahmenzeichen,
			              //  Rahmenbreite links, Rahmenbreite rechts
			              //  Übergabe des Textinhalts per ArrayList
			              //  Automatischem Zeilenumbruch wg. Länge und nach <BR>
			              //  Auflistungszeichen <li>
			 
			//Merke: Consolencode aus: JAZKernel\\tryout\\basic\\zBasic\\util\\crypt\\decrypt\\Vig_Decode256ZZZmain.java
			System.out.println("#################################################################");
			System.out.println("### 1. Starte einen Thread, der Zeilen aus einer Text-        ###");
			System.out.println("###    datei in eine andere, neue schreibt und diese          ###");
			System.out.println("###    allmählich n"
					+ "füllt. (LogFileCreateMockRunner)            ###");
			System.out.println("### 2. Starte einen weiteren Thread, der die neu gefuellte    ###");
			System.out.println("###    Datei beobachtet. (LogFileWatchRunner)                 ###");
			System.out.println("###    - Gib die neuen Zeilen aus.                            ###");
			System.out.println("###    - Reagiere auf die Ausgabe eines bestimmten Texts.     ###");
			System.out.println("###    - Wirf dann einen Event                                ###");
			System.out.println("### 3. Verwende den LogFileWatchListener,                     ###");
			System.out.println("###    der auf den Event aus Schritt 2 hoert und reagiert.    ###");
			System.out.println("###    Dieser Listener ist kein eigener Thread,               ###");
			System.out.println("###    sondern lediglich an dem LogFileWatchRunner registiert.###");
			System.out.println("#################################################################");
		
											
			//Lies die Test-Datei aus und fülle damit das Ziel-Log.
			//Merke: Der Text kommt aus einem Log des OVPN Projekts.
			//       Es wurde urspruenglich erstellt durch den Start einer .ovpn - Konfigurationsdatei des KernelProjekts.
			
			
			//Merke: Einen TryoutCode gibt es hier: OpenVPNZZZ\\tryout\\basic\\zBasic\\util\\log\\watch\\TryoutOpenVpnLogWatcherOVPN.java				
			String sLogDirectory =  "c:\\fglkernel\\kernellog\\ovpnServer";
			
			//Erstelle dieses Verzeichnis, falls noch nicht vorhanden
			boolean bCreated = FileEasyZZZ.createDirectory(sLogDirectory);
			if(!bCreated) {
				ExceptionZZZ ez = new ExceptionZZZ("unable to create directory: '" + sLogDirectory + "'.", iERROR_RUNTIME, LogFileWatch_MonitorProcessStyle_MainZZZ.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			
			String sLogFile = "ovpn.log";
			String sLogFilePathTotalDefault =	FileEasyZZZ.joinFilePathName(sLogDirectory, sLogFile);		
							
		    String sFilePath;
		    if (args.length > 0) {
		    	sFilePath = args[0];
		    } else {
		    	System.out.print("\nLog Ziel-Datei auswaehlen (per Dialog)? (J/N): ");
			    if (IoUtil.JaNein()) {	
			    	DateiUtil objUtilFileLog = new DateiUtil();
			    	objUtilFileLog.selectLoad();
			    	sFilePath = objUtilFileLog.computeFilePath();
			    	if(StringZZZ.isEmpty(sFilePath)) {
			    		break main;
			    	}	
			    }else {
			    	sFilePath = sLogFilePathTotalDefault;
			    }	    	
		    }
		    File objLogFile = new File(sFilePath);
			
		    TODOGOON20240204; 
		    //Mache eine Hauptklasse, die sich am Objekt-Broker der Monitorklasse registriert.
		    		    
		    //Der Monitor wird nun gestartet.
		    //Beim Start des Monitors werden die anderen Threads (Creator, Watcher) gestartet.
		    //Der Monitor registriert sich nun am Watcher-Thread. Beim Werfen des Events "Filterwert gefunden" 
		    //wird der Monitor die an ihm registrierte Hauptklasse per neuem Event informieren.
		    
			//0. Schritt: Bereite den Listener vor, der als Beispiel für einen "Monitor" fungiert.
		    LogFileWatchListenerMonitorExampleZZZ objListener = new LogFileWatchListenerMonitorExampleZZZ();
			
		   
			//1. Schritt: Mache den Log Creator
			String sSourceDirectory = "resourceZZZ\\file";
			String sSourceFile = "logExampleUsed.txt";
			String sSourceFilePathTotalDefault = FileEasyZZZ.joinFilePathName(sSourceDirectory, sSourceFile);
			
			
			File objSourceFile = new File(sSourceFilePathTotalDefault); 
			String[]saFlagCreate= {ILogFileCreateRunnerZZZ.FLAGZ.END_ON_FILTERFOUND.name()};
			LogFileCreateMockRunnerZZZ objCreator = new LogFileCreateMockRunnerZZZ(objSourceFile, objLogFile, saFlagCreate);
			
			//2. Schritt: Mache den Log Watcher mit dem "Reaktionsstring".
		    String sFilterSentence;    
		    if (args.length > 1) {
		    	sFilterSentence = (args[1]);
		    }else{    	    
		    	sFilterSentence = "Peer Connection Initiated with";
		    	//sFilterSentence = "local_port";
		    }
			
		    String[]saFlag= {ILogFileWatchRunnerZZZ.FLAGZ.END_ON_FILTERFOUND.name()};
			LogFileWatchRunnerZZZ objWatcher = new LogFileWatchRunnerZZZ(objLogFile, sFilterSentence,saFlag);			
			
			//Hole den Broker aus dem Watcher - Objekt und registriere den Monitor daran.						
			objWatcher.registerForStatusLocalEvent(objListener);//Registriere den Monitor nun am ProcessWatchRunner
				
			//Hole den Broker aus dem Watcher - Objekt und registriere den Creator daran.
			objWatcher.registerForStatusLocalEvent((IListenerObjectStatusBasicZZZ) objCreator);//Registriere den Creator nun am ProcessWatchRunner
					
			
			//3. Starte die Threads
			Thread objThreadWatcher = new Thread(objWatcher);
			objThreadWatcher.start();
			
			
			Thread objThreadCreator = new Thread(objCreator);
			objThreadCreator.start();
			
			
			
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

// So kann man so den Lauf anhalten...
//	System.out.println("Versuche anzuhalten: LogFileCreateMockRunnerZZZ");
//	objCreator.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);
//	System.out.println("Versuche anzuhalten... Erfolgreich?");
//			
//	System.out.println("Versuche anzuhalten: LogFileWatchRunnerZZZ");
//	objWatcher.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);
//	System.out.println("Versuche anzuhalten... Erfolgreich?");
			
//	IDEE: Flag END_ON_FILTERFOUND
//	Jetzt wird der CreatorThread und der MonitorThread an dem LogFileWatch thread registrier.
//	Wenn nun der Flag FLAGZ.END_ON_FILTERFOUND jeweiligen Thread-Objekt gesetzt ist, wird FLAGZ.REQUESTSTOP gesetzt.
//  Damit werden auch die anderen Threads angehalten.
			
			
		} catch (ExceptionZZZ e) {
			
			e.printStackTrace();
		}
	}//end main:
   }

}//end class
