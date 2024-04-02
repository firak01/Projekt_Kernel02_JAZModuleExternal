package debug.zBasic.util.moduleExternal.process.watch;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.IObjectWithStatusZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.process.watch.ProcessWatchRunnerZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalUserZZZ;
import basic.zKernel.status.StatusLocalAvailableHelperZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerZZZ;
import debug.zBasic.util.moduleExternal.log.watch.LogFileWatchListener_ExampleZZZ;
import debug.zBasic.util.moduleExternal.process.create.ProcessCreateMockRunnerZZZ;

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
public class ProcessWatch_1_ProcessWatchDirectStyle_MainZZZ implements IConstantZZZ{

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
			System.out.println("### 1. Starte einen Thread, der einen Process startet,        ###");
			System.out.println("###    der wiederum Ausgabe nach STDOUT schreibt.             ###");
			System.out.println("### 2. Starte einen weiteren Thread, der die neu erstellten   ###");
			System.out.println("###    STDOUT Ausgaben beobachtet ProcessWatchRunner)         ###");
			System.out.println("###    - Gib die neuen Zeilen aus.                            ###");
			System.out.println("###    - Reagiere auf die Ausgabe eines bestimmten Texts.     ###");
			System.out.println("###    - Wirf dann einen Event                                ###");
			System.out.println("### 3. Starte einen weiteren Thread, der die neu gefuellte    ###");
			System.out.println("###    (ProcessWatchListener_RunnerExampleZZZ),               ###");
			System.out.println("###    der einfach nur am ProcessWatchRunner registiert ist   ###");
			System.out.println("#################################################################");
		
								
			//0. Schritt: Bereite den Listener vor, der als Beispiel für einen einfachen Listener fungiert.
		    ProcessWatchListener_ExampleZZZ objListener = new ProcessWatchListener_ExampleZZZ();
			
			
			//Starte den Thread für den Process und seine Ausgaben.
			//TODOGOON: 
			//Momentan sind das nur Zähler-Ausgaben.
			//Das sollten aber Ausgaben sein, die aus einer Muster-Textdatei stammen.
			//z.B. einem Muster-Log des OVPN Servers.
			
		   
			//1. Schritt: Mache den STDOUT Creator
			String sSourceDirectory = "resourceZZZ\\file";
			String sSourceFile = "logExampleUsed.txt";
			String sSourceFilePathTotalDefault = FileEasyZZZ.joinFilePathName(sSourceDirectory, sSourceFile);
			File objSourceFile = new File(sSourceFilePathTotalDefault);
			
			//TODOGOON: Demnaechst mit einem File als Quelle der Ausgabe
			String[]saFlagCreate= {IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND.name()};
			ProcessCreateMockRunnerZZZ objCreator = new ProcessCreateMockRunnerZZZ();
			objCreator.createProcessByBatchCustom();
			
			//2. Schritt: Mache den Log Watcher mit dem "Reaktionsstring".
		    String sFilterSentence;    
		    if (args.length > 1) {
		    	sFilterSentence = (args[1]);
		    }else{    	    
		    	sFilterSentence = "Peer Connection Initiated with";
		    	//sFilterSentence = "local_port";
		    }
			
		    Process objProcess = objCreator.getProcess();
		    String[]saFlag= {IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND.name(),
		    		         IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_VALUECHANGED.name(),
		    		         IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_MESSAGECHANGED.name(),
		    		         ISenderObjectStatusLocalUserZZZ.FLAGZ.STATUSLOCAL_SEND_VALUEFALSE.name()
		    		         };
		    
			ProcessWatchRunnerZZZ objWatcher = new ProcessWatchRunnerZZZ(objProcess, sFilterSentence,saFlag);						
			ArrayList<IEnumSetMappedStatusZZZ> col = StatusLocalAvailableHelperZZZ.searchEnumMappedList(objWatcher);
			
			System.out.println("TODOGOON20240310");
			
			//Hole den Broker aus dem Watcher - Objekt und registriere den Monitor daran.						
			objWatcher.registerForStatusLocalEvent(objListener);//Registriere den Monitor nun am ProcessWatchRunner
				
			//Hole den Broker aus dem Watcher - Objekt und registriere den Creator daran.
			objWatcher.registerForStatusLocalEvent(objCreator);//Registriere den Creator nun am ProcessWatchRunner
				
			//3. Starte die Programme. Jedes erzeugt seinen eigenen Thread.
		    objWatcher.startAsThread();
			objCreator.startAsThread();
		    
		    
			
		    //Damit ohne einen Watcher auch etwas auf der Konsole ausgegeben wird
			Thread.sleep(1000);
			//objCreator.debugWriteOutputToLogPLUSanalyse();
			
		    
		    
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
//	objCreator.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, true);
//	System.out.println("Versuche anzuhalten... Erfolgreich?");
//			
//	System.out.println("Versuche anzuhalten: LogFileWatchRunnerZZZ");
//	objWatcher.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, true);
//	System.out.println("Versuche anzuhalten... Erfolgreich?");
			
//	IDEE: Flag END_ON_FILTERFOUND
//	Jetzt wird der CreatorThread und der MonitorThread an dem LogFileWatch thread registrier.
//	Wenn nun der Flag FLAGZ.END_ON_FILTERFOUND jeweiligen Thread-Objekt gesetzt ist, wird FLAGZ.REQUEST_STOP gesetzt.
//  Damit werden auch die anderen Threads angehalten.
			
		 } catch (ExceptionZZZ e1) {
			System.out.println(e1.getDetailAllLast());
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}//end main:
   }

}//end class
