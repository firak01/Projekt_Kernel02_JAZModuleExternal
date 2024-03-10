package debug.zBasic.util.moduleExternal.log.watch;

import java.io.File;

import base.files.DateiUtil;
import base.io.IoUtil;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.LogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.monitor.LogFileWatchMonitorZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerOnMonitorListeningZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerZZZ;
import debug.zBasic.util.moduleExternal.log.create.LogFileCreateRunnerMockOnMonitorListeningZZZ;

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
public class LogFileWatch_2_LogWatchMonitorStyle_MainZZZ implements IConstantZZZ{

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
			System.out.println("###################################################################");
			System.out.println("### 1.  Starte einen Thread, der auf den 'Monitor-Thread' hört. ###");
			System.out.println("###     (LogFileWatchListenerOnMonitor_RunnerExampleZZZ)        ###");					
			System.out.println("### 2.  Starte den 'Monitor-Thread', der weiter Threads startet ###");
			System.out.println("###     und auf deren Events hoert.                             ###");
			System.out.println("###     Wird ein Event empfangen, wirf er ebenfalls einen Event.###");
			System.out.println("###     Der Thread aus 1 empfängt diesen, dito 2a, 2b.          ###");
		    System.out.println("###     Dabei ist der geworfene Event ein anderer (s. Mapping). ###");
			System.out.println("### 2a. Starte einen Thread, der Zeilen aus einer Text-         ###");
			System.out.println("###     datei in eine andere, neue schreibt und diese           ###");
			System.out.println("###     allmählich füllt.                                       ###");
			System.out.println("###     (LogFileCreateMockRunnerOnMonitorListeningZZZ)          ###");						
			System.out.println("### 2b. Starte einen weiteren Thread, der die neu gefuellte     ###");
			System.out.println("###     Datei beobachtet. (LogFileWatchRunner)                  ###");
			System.out.println("###     - Gib die neuen Zeilen aus.                             ###");
			System.out.println("###     - Reagiere auf die Ausgabe eines bestimmten Texts.      ###");
			System.out.println("###     - Wirf dann einen Event                                 ###");
			System.out.println("###################################################################");
		
											
			//Lies die Test-Datei aus und fülle damit das Ziel-Log.
			//Merke: Der Text kommt aus einem Log des OVPN Projekts.
			//       Es wurde urspruenglich erstellt durch den Start einer .ovpn - Konfigurationsdatei des KernelProjekts.
			
			
			//Merke: Einen TryoutCode gibt es hier: OpenVPNZZZ\\tryout\\basic\\zBasic\\util\\log\\watch\\TryoutOpenVpnLogWatcherOVPN.java				
			String sLogDirectory =  "c:\\fglkernel\\kernellog\\ovpnServer";
			
			//Erstelle dieses Verzeichnis, falls noch nicht vorhanden
			boolean bCreated = FileEasyZZZ.createDirectory(sLogDirectory);
			if(!bCreated) {
				ExceptionZZZ ez = new ExceptionZZZ("unable to create directory: '" + sLogDirectory + "'.", iERROR_RUNTIME, LogFileWatch_2_LogWatchMonitorStyle_MainZZZ.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			
			String sLogFile = "ovpn.log";
			String sLogFilePathTotalDefault =	FileEasyZZZ.joinFilePathName(sLogDirectory, sLogFile);		
							
		    String sFilePath;
		    if (args.length > 0) {
		    	sFilePath = args[0];
		    } else {
		    	System.out.print("\nLog Ziel-Datei auswaehlen (per Dialog)? Sie wird dann zunaechst geloescht und danach aus einer Vorlage neu aufgebaut (J/N/A): ");
		    	int iProof = IoUtil.JaNeinAbbrechen();
			    if (IoUtil.isJa(iProof)) {	
			    	DateiUtil objUtilFileLog = new DateiUtil();
			    	objUtilFileLog.selectLoad();
			    	sFilePath = objUtilFileLog.computeFilePath();
			    	if(StringZZZ.isEmpty(sFilePath)) {
			    		System.out.println("Keine Datei ausgewählt. Program wird abgebrochen.");
			    		break main;
			    	}
			    }else if(IoUtil.isAbbrechen(iProof)) {
			    	System.out.println("Program wird abgebrochen.");
			    	break main;
			    }else {
			    	sFilePath = sLogFilePathTotalDefault;
			    }	    	
		    }
		    
		    //Lösche zuerst die Zieldatei
		    FileEasyZZZ.removeFile(sFilePath);
		    
		    //Erstelle nun die Datei wieder neu, erst einmal als Objekt
		    File objLogFile = new File(sFilePath);
			
		    		    
		    //Der Monitor wird nun gestartet.
		    //Beim Start des Monitors werden die anderen Threads (Creator, Watcher) gestartet.
		    //Der Monitor registriert sich nun am Watcher-Thread. Beim Werfen des Events "Filterwert gefunden" 
		    //wird der Monitor die an ihm registrierte Hauptklasse per neuem Event informieren.
		    
			//0. Schritt: Bereite den Listener vor, der als Beispiel für einen "Listener am Monitor" fungiert.
		    LogFileWatchListenerOnMonitor_RunnerExampleZZZ objListenerOnMonitor = new LogFileWatchListenerOnMonitor_RunnerExampleZZZ();
			
		   
			//1. Schritt: Mache den Log Creator, 
			String sSourceDirectory = "resourceZZZ\\file";
			String sSourceFile = "logExampleUsed.txt";
			String sSourceFilePathTotalDefault = FileEasyZZZ.joinFilePathName(sSourceDirectory, sSourceFile);
			
			
			File objSourceFile = new File(sSourceFilePathTotalDefault); 
			String[]saFlagCreate= {ILogFileCreateRunnerZZZ.FLAGZ.END_ON_FILTER_FOUND.name()};
			LogFileCreateRunnerMockOnMonitorListeningZZZ objCreator = new LogFileCreateRunnerMockOnMonitorListeningZZZ(objSourceFile, objLogFile, saFlagCreate);
			
			//2. Mache den Log Watcher mit dem "Reaktionsstring".
		    String sFilterSentence;    
		    if (args.length > 1) {
		    	sFilterSentence = (args[1]);
		    }else{    	    
		    	sFilterSentence = "Peer Connection Initiated with";
		    	//sFilterSentence = "local_port";
		    }
		    String[]saFlag= {IWatchRunnerZZZ.FLAGZ.END_ON_FILTER_FOUND.name()};
			LogFileWatchRunnerZZZ objWatcher = new LogFileWatchRunnerZZZ(objLogFile, sFilterSentence, saFlag);			
			
			
			//3. Schritt: Mache den Monitor
					//	IDEE: Flag END_ON_FILTERFOUND
					//	Jetzt wird der CreatorThread und der LogFileWatch thread am monitor registriert.
					//	Wenn nun der Flag FLAGZ.END_ON_FILTERFOUND jeweiligen Thread-Objekt gesetzt ist, wird FLAGZ.REQUEST_STOP gesetzt.
					//  Damit werden auch die anderen Threads angehalten.
		    //String[] saFlagMonitor = {ILogFileWatchRunnerMonitorZZZ.FLAGZ.END_ON_FILTERFOUND.name()};		    
		    LogFileWatchMonitorZZZ objMonitor = new LogFileWatchMonitorZZZ(objLogFile);//, sFilterSequence, saFlagMonitor);
		    
		    //4. Schritt: Statt im Konstruktor des Monitors alles zu definieren...
		    //            übergib die Objekte an den Monitor
		    		    
		    //Merke: Beim Übergeben der Objekte an den Monitor... diese dabei sofort am Monitor registrieren....
		    objMonitor.addProgram(objWatcher);
		    objMonitor.addProgram(objCreator);
		    
			//Registriere auch den Beispiellistener am Monitor, der dann quasi auf die Events des Monitors reagieren kann.
		    //Diese Events des Monitors sind dasn weitergeleitetet Events der Programme
			objMonitor.registerForStatusLocalEvent(objListenerOnMonitor);
			
			//+++++++++++++++++++++++++++++++++++
			//5. Schritt: Starte den Monitor
			//Merke: Beim Starten des Monitor-Threads die übergebenen Runner auch starten.	
			objMonitor.start();
			
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}

			//20240207: TODOGOON
			//Idee: Teste den Monitor Thread anzuhalten.
			//      Dann sollte er einen Event werfen "ich stoppe"
			//      Dann sollten alle anderen Threads/Programme ebenfalls aufhören.
			
			
			// So kann man so den Lauf der einzelnen Programme anhalten...
			//	System.out.println("Versuche anzuhalten: LogFileCreateMockRunnerZZZ");
			//	objCreator.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, true);
			//	System.out.println("Versuche anzuhalten... Erfolgreich?");
			//			
			//	System.out.println("Versuche anzuhalten: LogFileWatchRunnerZZZ");
			//	objWatcher.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, true);
			//	System.out.println("Versuche anzuhalten... Erfolgreich?");
								
		} catch (ExceptionZZZ e) {
			
			e.printStackTrace();
		}
	}//end main:
   }

}//end class
