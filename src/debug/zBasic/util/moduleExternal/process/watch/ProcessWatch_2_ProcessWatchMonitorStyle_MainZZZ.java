package debug.zBasic.util.moduleExternal.process.watch;

import java.io.File;

import base.files.DateiUtil;
import base.io.IoUtil;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.IObjectWithStatusZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.LogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.monitor.LogFileWatchMonitorZZZ;
import basic.zBasic.util.moduleExternal.monitor.ProcessWatchMonitorZZZ;
import basic.zBasic.util.moduleExternal.process.watch.ProcessWatchRunnerZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalUserZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerOnMonitorListeningZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerZZZ;
import debug.zBasic.util.moduleExternal.log.create.LogFileCreateRunnerMockOnMonitorListeningZZZ;
import debug.zBasic.util.moduleExternal.log.watch.LogFileWatchListenerOnMonitor_RunnerExampleZZZ;
import debug.zBasic.util.moduleExternal.process.create.ProcessCreateMockRunnerZZZ;

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
public class ProcessWatch_2_ProcessWatchMonitorStyle_MainZZZ implements IConstantZZZ{

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
			System.out.println("###     (ProcessWatchListenerOnMonitor_RunnerExampleZZZ)        ###");					
			System.out.println("### 2.  Starte den 'Monitor-Thread', der weiter Threads startet ###");
			System.out.println("###     und auf deren Events hoert.                             ###");
			System.out.println("###     Wird ein Event empfangen, wirf er ebenfalls einen Event.###");
			System.out.println("###     Der Thread aus 1 empfängt diesen, dito 2a, 2b.          ###");
		    System.out.println("###     Dabei ist der geworfene Event ein anderer (s. Mapping). ###");
			System.out.println("### 2a. Starte einen Thread, der (TODO) Zeilen aus einer Text-  ###");
			System.out.println("###     datei ausliest und die als Processs nach STSOUT         ###");
			System.out.println("###     schreibt.(Momentan nur Zahlen eines Counters ausgegeben)###");
			System.out.println("###     (ProcessCreateMockRunnerZZZ)                            ###");						
			System.out.println("### 2b. Starte einen weiteren Thread, der dieseen Process und   ###");
			System.out.println("###     den STDOUT von im beobachtet (ProsessWatchhRunner)      ###");
			System.out.println("###     - Gib die neuen Zeilen aus.                             ###");
			System.out.println("###     - Reagiere auf die Ausgabe eines bestimmten Texts.      ###");
			System.out.println("###     - Wirf dann einen Event                                 ###");
			System.out.println("###################################################################");
											
			//0. Schritt: Bereite den Listener vor, der als Beispiel für einen einfachen Listener fungiert.
		    ProcessWatchListenerOnMonitor_RunnerExampleZZZ objListenerOnMonitor = new ProcessWatchListenerOnMonitor_RunnerExampleZZZ();
			
			//1. Starte den Thread für den Process und seine Ausgaben.
			
		    //TODOGOON: 
		    //1a. Schritt: Vorbereitende Datei fuer den STDOUT Creator
		    
			//Merke: Momentan sind das nur Zähler-Ausgaben.
			//Das sollten aber Ausgaben sein, die aus einer Muster-Textdatei stammen.
			//z.B. einem Muster-Log des OVPN Servers.
			
			//TODOGOON: Demnaechst mit einem File als Quelle der Ausgabe
			String sSourceDirectory = "resourceZZZ\\file";
			String sSourceFile = "logExampleUsed.txt";
			String sSourceFilePathTotalDefault = FileEasyZZZ.joinFilePathName(sSourceDirectory, sSourceFile);
			File objSourceFile = new File(sSourceFilePathTotalDefault);
			
			//1b. Schritt: Mache den STDOUT Creator
			String[]saFlagCreate= {IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND.name()};
			ProcessCreateMockRunnerZZZ objCreator = new ProcessCreateMockRunnerZZZ(saFlagCreate);
			Process objProcess = objCreator.createProcessByBatchCustom();
			
			//2. Schritt: Mache den Process Watcher mit dem "Reaktionsstring".
		    String sFilterSentence;    
		    if (args.length > 1) {
		    	sFilterSentence = (args[1]);
		    }else{    	    
		    	sFilterSentence = "Peer Connection Initiated with";
		    	//sFilterSentence = "local_port";
		    }
			
		    String[]saFlag= {IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND.name(),
		    		         IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_VALUECHANGED.name(),
		    		         IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_MESSAGECHANGED.name(),
		    		         ISenderObjectStatusLocalUserZZZ.FLAGZ.STATUSLOCAL_SEND_VALUEFALSE.name()
		    		         };
		    
			ProcessWatchRunnerZZZ objWatcher = new ProcessWatchRunnerZZZ(objProcess, sFilterSentence,saFlag);						
		  
			
			//3. Schritt: Mache den Monitor
					//	IDEE: Flag END_ON_FILTERFOUND
					//	Jetzt wird der CreatorThread und der ProcessWatch thread am monitor registriert.
					//	Wenn nun der Flag FLAGZ.END_ON_FILTERFOUND jeweiligen Thread-Objekt gesetzt ist, wird FLAGZ.REQUEST_STOP gesetzt.
					//  Damit werden auch die anderen Threads angehalten.
//		    String[] saFlagMonitor = {IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND.name(),
//		    		 ISenderObjectStatusLocalUserZZZ.FLAGZ.STATUSLOCAL_SEND_VALUEFALSE.name(),
//		    		 IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_VALUECHANGED.name(),
//    		         IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_MESSAGECHANGED.name()};
		    
			//Logausgaben reduzieren. Keine False-Werte mehr versenden
		    String[] saFlagMonitor = {IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND.name(),
		    		 IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_VALUECHANGED.name(),
   		             IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_MESSAGECHANGED.name()};
		    ProcessWatchMonitorZZZ objMonitor = new ProcessWatchMonitorZZZ(objProcess, saFlagMonitor);//, sFilterSequence, saFlagMonitor);
		    
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
