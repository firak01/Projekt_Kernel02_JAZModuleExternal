package debug.zBasic.util.moduleExternal.log.watch;

import java.io.File;

import base.files.DateiUtil;
import base.io.IoUtil;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.LogFileWatchRunnerZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerZZZ;
import debug.zBasic.util.moduleExternal.log.create.LogFileCreateMockRunnerZZZ;

/** In dieser Klasse wird ein LogFile von dem einen Thread erzeugt 
 *  und gleichzeitig von einem anderen Thread ausgewertet.*  
 *  Findet der Auswertungsthread einen bestimmten Ausdruck im LogFile, 
 *  wirft er einen Event.
 *  
 *  In dieser Variante wird ohne einen Monitor gearbeitet, der die Threads verwaltet
 *  und die entsprechenden Stati durchreicht.
 *  
 *  Die Methode .reactOnStatusLocalEvent(Event) der am EventBroker registrierten anderen Processe wird aufgerufen. 
 *  Als Beispielsreaktion auf den Event werden diese nun beendet.
 *  
 * @author fl86kyvo
 *
 */
public class LogFileWatch_1_LogWatchDirectStyle_MainZZZ implements IConstantZZZ{

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
			System.out.println("###    allmählich füllt. (LogFileCreateMockRunner)            ###");
			System.out.println("###    Dieser registriert sich am LogFileWatchRunner.         ###");
			System.out.println("### 2. Starte einen weiteren Thread, der die neu gefuellte    ###");
			System.out.println("###    Datei beobachtet. (LogFileWatchRunner)                 ###");
			System.out.println("###    - Gib die neuen Zeilen aus.                            ###");
			System.out.println("###    - Reagiere auf die Ausgabe eines bestimmten Texts.     ###");
			System.out.println("###    - Wirf dann einen Event                                ###");
			System.out.println("### 3. Starte einen weiteren Thread, der die neu gefuellte    ###");
			System.out.println("###    (LogFileWatchListener_RunnerExampleZZZ),               ###");
			System.out.println("###    der einfach nur am LogFileWatchRunner registiert ist   ###");
			System.out.println("#################################################################");
		
											
			//Lies die Test-Datei aus und fülle damit das Ziel-Log.
			//Merke: Der Text kommt aus einem Log des OVPN Projekts.
			//       Es wurde urspruenglich erstellt durch den Start einer .ovpn - Konfigurationsdatei des KernelProjekts.
			
			
			//Merke: Einen TryoutCode gibt es hier: OpenVPNZZZ\\tryout\\basic\\zBasic\\util\\log\\watch\\TryoutOpenVpnLogWatcherOVPN.java				
			String sLogDirectory =  "c:\\fglkernel\\kernellog\\ovpnServer";
			
			//Erstelle dieses Verzeichnis, falls noch nicht vorhanden
			boolean bCreated = FileEasyZZZ.createDirectory(sLogDirectory);
			if(!bCreated) {
				ExceptionZZZ ez = new ExceptionZZZ("unable to create directory: '" + sLogDirectory + "'.", iERROR_RUNTIME, LogFileWatch_1_LogWatchDirectStyle_MainZZZ.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			
			String sLogFile = "ovpn.log";
			String sLogFilePathTotalDefault =	FileEasyZZZ.joinFilePathName(sLogDirectory, sLogFile);		
							
		    String sFilePath;
		    if (args.length > 0) {
		    	sFilePath = args[0];
		    } else {
		    	System.out.print("\nLog Ziel-Datei auswaehlen (per Dialog). Merke: Sie wird geloescht und neu aufgebaut? (J/N): ");
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
		    
		    //Lösche zuerst die Zieldatei
		    FileEasyZZZ.removeFile(sFilePath);
		    
		    //Erstelle nun die Datei wieder neu, erst einmal als Objekt
		    File objLogFile = new File(sFilePath);
			
			//0. Schritt: Bereite den Listener vor, der als Beispiel für einen einfachen Listener fungiert.
		    LogFileWatchListener_RunnerExampleZZZ objListener = new LogFileWatchListener_RunnerExampleZZZ();
			
		   
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
			objWatcher.registerForStatusLocalEvent((IListenerObjectStatusLocalZZZ) objCreator);//Registriere den Creator nun am ProcessWatchRunner
				
			//3. Starte die Programme. Jedes erzeugt seinen eigenen Thread.
			objWatcher.startAsThread();
			objCreator.startAsThread();
						
//			try {
//				Thread.sleep(50000);
//				catch (InterruptedException e) {					
//				try {
//					String sLogIE = e.getMessage();
//					this.logProtocolString("An error happend: '" + sLogIE + "'");
//				} catch (ExceptionZZZ e1) {
//					System.out.println(e1.getDetailAllLast());
//					e1.printStackTrace();
//				}
//				System.out.println(e.getMessage());
//				e.printStackTrace();
//			}

// So kann man so den lauf anhalten...
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
			
			  } catch (ExceptionZZZ e1) {
					System.out.println(e1.getDetailAllLast());
					e1.printStackTrace();
			}				
	}//end main:
   }

}//end class
