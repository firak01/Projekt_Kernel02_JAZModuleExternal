package debug.zBasic.util.log.watch;

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
import basic.zBasic.util.log.watch.LogFileWatchRunnerZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalSetZZZ;
import debug.zBasic.util.log.create.LogFileCreateMockRunnerZZZ;

public class LogFileProcessMainZZZ implements IConstantZZZ{

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
				ExceptionZZZ ez = new ExceptionZZZ("unable to create directory: '" + sLogDirectory + "'.", iERROR_RUNTIME, LogFileProcessMainZZZ.class, ReflectCodeZZZ.getPositionCurrent());
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
			
			    
			//1. Schritt: Starte den Log Creator
			String sSourceDirectory = "resourceZZZ\\file";
			String sSourceFile = "logExampleUsed.txt";
			String sSourceFilePathTotalDefault = FileEasyZZZ.joinFilePathName(sSourceDirectory, sSourceFile);		
			
			File objSourceFile = new File(sSourceFilePathTotalDefault); 
			LogFileCreateMockRunnerZZZ objCreator = new LogFileCreateMockRunnerZZZ(objSourceFile, objLogFile);
			
			Thread objThreadCreator = new Thread(objCreator);
			objThreadCreator.start();
			
			    
			//2. Schritt: Starte den Log Watcher mit dem "Reaktionsstring".
		    String sFilterSentence;    
		    if (args.length > 1) {
		    	sFilterSentence = (args[1]);
		    }else{    	    
		    	sFilterSentence = "Peer Connection Initiated with";
		    	//sFilterSentence = "local_port";
		    }
			
			LogFileWatchRunnerZZZ objWatcher = new LogFileWatchRunnerZZZ(objLogFile, sFilterSentence);
			LogFileWatchListenerExampleZZZ objListener = new LogFileWatchListenerExampleZZZ();
			objWatcher.registerForStatusLocalEvent((IListenerObjectStatusLocalMessageSetZZZ)objListener);//Registriere den Monitor nun am ProcessWatchRunner
			
			Thread objThreadWatcher = new Thread(objWatcher);
			objThreadWatcher.start();
			
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			TODOGOON20240120;
//			IDEE: Flag StopWhenFound
//			Und dann beide Threads registrieren
//			und dann beide registerierte Objeteke benachrichtigen
//			und wenn Flag in den Objekten StopWhenFound gesetzt ist.. anhalten.
			
			//Test: Kann man so den lauf anhalten...
			System.out.println("Versuche anzuhalten: LogFileCreateMockRunnerZZZ");
			objCreator.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);
			System.out.println("Versuche anzuhalten... Erfolgreich?");
			
			System.out.println("Versuche anzuhalten: LogFileWatchRunnerZZZ");
			objWatcher.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);
			System.out.println("Versuche anzuhalten... Erfolgreich?");
		} catch (ExceptionZZZ e) {
			
			e.printStackTrace();
		}
	}//end main:
   }

}//end class
