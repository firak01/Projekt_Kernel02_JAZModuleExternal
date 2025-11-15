package debug.zBasic.util.moduleExternal.process.watchDummy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import basic.zBasic.AbstractObjectWithExceptionZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ;

public class ProcessUsingProcessCreateAndWatchDummyZZZ extends AbstractObjectWithExceptionZZZ{
	private Process objProcess=null;
	
	public Process getProcessUsed() {
		return this.objProcess;
	}
	public void setProcessUsed(Process objProcess) {
		this.objProcess = objProcess;
	}
	
	public Process requestStart() throws ExceptionZZZ {
		Process objReturn = null;
		main:{
			
			try {
				String sLog;
				//Beispielse aus OVPN Projekt, dort wird ein Process ueber die Konfigurationdatei und die damit verknuepfte Applikation gestartet.
				//Process p = load.exec( "cmd /c C:\\Programme\\OpenVPN\\bin\\openvpn.exe --pause-exit --config C:\\Programme\\OpenVPN\\config\\client_itelligence.ovpn");
				//DAS GEHT: Process p = load.exec( "cmd /c C:\\Programme\\OpenVPN\\bin\\openvpn.exe --pause-exit --config C:\\Programme\\OpenVPN\\config\\client_itelligence.ovpn");
				

				
				//Merke: Beim Starten der Batch, gibt es keine Fehlermeldung, fall die ueberhaupt nicht vorhanden ist. Es funktioniert halt nicht....
				//auf dem Notebook der TUBAF String sDirectory = "C:\\HIS-Workspace\\1fgl\\repo\\EclipseOxygen\\Projekt_Kernel02_JAZModuleExternal\\src\\bat";
				String sDirectory = "C:\\1fgl\\repo\\EclipseOxygen_V01\\Projekt_Kernel02_JAZModuleExternal\\src\\bat";
				//String sDirectory = "\\src\\bat";
				
				
				//### DUMMY TEST #######
				//Befüllt einen LogDatei
				//String sCommandConcrete = sDirectory + "\\KernelZZZTest_ProcessStarter_Dummy.bat > c:\\fglkernel\\kernellog\\ProcessUsing_StarterLog.txt";
				
				//Ohne die Logdatei kann der InputStream ausgelesen und ausgegeben werden:
				//String sCommandConcrete = sDirectory + "\\KernelZZZTest_ProcessStarter_Dummy.bat";
				
				
				//### Java als Thread runnable in Endlosschleife TEST: Verwende start und Fensternamen und Starte darueber die JavaKlasse ######################
				//String sCommandConcrete = "bat\\KernelZZZTest_ProcessStarter_DummyModuleExternal.bat > c:\\fglkernel\\kernellog\\ProcessUsing_StarterLog.txt";
				//String sCommandConcrete = sDirectory + "\\KernelZZZTest_ProcessStarter_DummyModuleExternalAsWindow2Log.bat";
				//String sCommandConcrete = sDirectory + "\\KernelZZZTest_ProcessStarter_DummyModuleExternalAsWindow.bat";
				
				//### Java als Thread runnable in Endlosschleife TEST: Starte die Java-Klasse direkt ######################
				String sCommandConcrete = sDirectory + "\\KernelZZZTest_ProcessStarter_DummyModuleExternal.bat";
				
				
				Runtime load = Runtime.getRuntime();
				objReturn = load.exec("cmd /c " + sCommandConcrete);
				
				if(objReturn==null){
						//Hier nicht abbrechen, sondern die Verarbeitung bei der naechsten Datei fortfuehren
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Unable to create process, using command: '"+ sCommandConcrete +"'";
						System.out.println(sLog);
						this.logProtocol(sLog); 						
					}else{	
						
						//NEU: Einen anderen Thread zum "Monitoren" des Inputstreams des Processes verwenden. Dadurch werden die anderen Prozesse nicht angehalten.
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Successfull process created, using command: '"+ sCommandConcrete +"'";
						System.out.println(sLog);
						this.logProtocol(sLog);			
					}
				
				
				
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			
			this.setProcessUsed(objReturn);
		}//end main:
		return objReturn;
	}
	
	
	/** In dieser Methode werden die Ausgabezeilen eines Batch-Prozesses ( cmd.exe ) 
	 *  aus dem Standard - Output gelesen.
	 *  - Sie werden in das Kernel-Log geschrieben.
	 *  - Sie werden hinsichtlich bestimmter Schluesselsaetze analysiert,
	 *    um z.B. den erfolgreichen Verbindungsaufbau mitzubekommen.
	 *  
	 *  Merke: 
	 *  Merke1: Der aufgerufene OVPN-Prozess stellt irgendwann das schreiben ein
					//Das ist dann z.B. der letzte Eintrag
					//0#Sat Sep 02 07:39:48 2023 us=571873 NOTE: --mute triggered... 
					//Der wert wird in der OVPN-Konfiguration eingestellt, z.B.:
					//mute=20  
				
	 * Merke2: Wie über einen Erfolg benachrichtigen?
			   Wenn die Verbindung erstellt wird, steht folgendes im Log.
			   
TCP connection established with [AF_INET]192.168.3.116:4999
0#Sat Sep 02 12:53:10 2023 us=223095 TCPv4_CLIENT link local: [undef]
0#Sat Sep 02 12:53:10 2023 us=223095 TCPv4_CLIENT link remote: [AF_INET]192.168.3.116:4999
0#Sat Sep 02 12:53:10 2023 us=223095 TLS: Initial packet from [AF_INET]192.168.3.116:4999, sid=75fbf19d 73f20fdc
0#Sat Sep 02 12:53:10 2023 us=363726 VERIFY OK: depth=1, C=DE, ST=PREUSSEN, L=BERLIN, O=OpenVPN, OU=TEST, CN=PAUL.HINDENBURG, name=PAUL.HINDENBURG, emailAddress=paul.hindenburg@mailinator.com\09
0#Sat Sep 02 12:53:10 2023 us=363726 VERIFY OK: depth=0, C=DE, ST=PREUSSEN, L=BERLIN, O=OpenVPN, OU=TEST, CN=HANNIBALDEV06VM_SERVER, name=HANNIBALDEV06VM, emailAddress=paul.hindenburg@mailinator.com\09
0#Sat Sep 02 12:53:10 2023 us=551235 Data Channel Encrypt: Cipher 'BF-CBC' initialized with 128 bit key
0#Sat Sep 02 12:53:10 2023 us=551235 WARNING: INSECURE cipher with block size less than 128 bit (64 bit).  This allows attacks like SWEET32.  Mitigate by using a --cipher with a larger block size (e.g. AES-256-CBC).
0#Sat Sep 02 12:53:10 2023 us=551235 Data Channel Encrypt: Using 160 bit message hash 'SHA1' for HMAC authentication
0#Sat Sep 02 12:53:10 2023 us=551235 Data Channel Decrypt: Cipher 'BF-CBC' initialized with 128 bit key
0#Sat Sep 02 12:53:10 2023 us=551235 WARNING: INSECURE cipher with block size less than 128 bit (64 bit).  This allows attacks like SWEET32.  Mitigate by using a --cipher with a larger block size (e.g. AES-256-CBC).
0#Sat Sep 02 12:53:10 2023 us=551235 Data Channel Decrypt: Using 160 bit message hash 'SHA1' for HMAC authentication
0#Sat Sep 02 12:53:10 2023 us=551235 Control Channel: TLSv1.2, cipher TLSv1/SSLv3 DHE-RSA-AES256-GCM-SHA384, 1024 bit RSA
0#Sat Sep 02 12:53:10 2023 us=551235 [HANNIBALDEV06VM_SERVER] Peer Connection Initiated with [AF_INET]192.168.3.116:4999
0#Sat Sep 02 12:53:13 2023 us=20060 SENT CONTROL [HANNIBALDEV06VM_SERVER]: 'PUSH_REQUEST' (status=1)
0#Sat Sep 02 12:53:13 2023 us=176313 PUSH: Received control message: 'PUSH_REPLY,ping 10,ping-restart 240,ifconfig 10.0.0.2 10.0.0.1'
0#Sat Sep 02 12:53:13 2023 us=176313 OPTIONS IMPORT: timers and/or timeouts modified
0#Sat Sep 02 12:53:13 2023 us=176313 OPTIONS IMPORT: --ifconfig/up options modified
0#Sat Sep 02 12:53:13 2023 us=176313 do_ifconfig, tt->ipv6=0, tt->did_ifconfig_ipv6_setup=0
0#Sat Sep 02 12:53:13 2023 us=176313 ******** NOTE:  Please manually set the IP/netmask of 'OpenVPN2' to 10.0.0.2/255.255.255.252 (if it is not already set)
0#Sat Sep 02 12:53:13 2023 us=176313 open_tun, tt->ipv6=0
0#Sat Sep 02 12:53:13 2023 us=176313 TAP-WIN32 device [OpenVPN2] opened: \\.\Global\{9B00449E-0F90-4137-A063-CEA05D846AD8}.tap
0#Sat Sep 02 12:53:13 2023 us=176313 TAP-Windows Driver Version 9.9 
0#Sat Sep 02 12:53:13 2023 us=176313 TAP-Windows MTU=1500
0#Sat Sep 02 12:53:13 2023 us=176313 Sleeping for 3 seconds...
2023-9-2_12_53: Thread # 0 not jet ended or has reported an error.
0#Sat Sep 02 12:53:16 2023 us=176370 Successful ARP Flush on interface [4] {9B00449E-0F90-4137-A063-CEA05D846AD8}
0#Sat Sep 02 12:53:17 2023 us=410769 TEST ROUTES: 0/0 succeeded len=0 ret=0 a=0 u/d=down
0#Sat Sep 02 12:53:17 2023 us=410769 Route: Waiting for TUN/TAP interface to come up...
0#Sat Sep 02 12:53:18 2023 us=645168 TEST ROUTES: 0/0 succeeded len=0 ret=1 a=0 u/d=up
0#Sat Sep 02 12:53:18 2023 us=645168 WARNING: this configuration may cache passwords in memory -- use the auth-nocache option to prevent this
0#Sat Sep 02 12:53:18 2023 us=645168 Initialization Sequence Completed
*/
	public boolean analyseInputLineCustom(String sLine) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{				
			sLine = StringZZZ.trimAnyQuoteMarked(sLine);
			
			String sZaehler = StringZZZ.right(sLine, "Zaehler");
			
			String[] saQuotation = {"'","\""};			
			sZaehler = StringZZZ.stripRight(sZaehler,saQuotation);
	        sZaehler = sZaehler.trim();
			int iZaehler = StringZZZ.toInteger(sZaehler);
			if(iZaehler % 10 == 0 && iZaehler > 0) {
				System.out.println(ReflectCodeZZZ.getPositionCurrent() + "TESTFGL PROCESS STRING ANALYSE - 10er Zaehler gefunden.");
			}else if(iZaehler >= 1000) {
				System.out.println(ReflectCodeZZZ.getPositionCurrent() + "TESTFGL PROCESS STRING ANALYSE - Zaehler ueber 1000. Breche ab.");
				//this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, false);//Merke: STOPREQUEST ist eine Anweisung.. bleibt also ein Flag und ist kein Status
				bReturn = false;
				break main;
			}
			bReturn = true;
		}//end main:
		return bReturn;
	}
					 
	 /*  
	 * @throws ExceptionZZZ
	 * @author Fritz Lindhauer, 03.09.2023, 07:35:31
	 */
	public void writeOutputToLogPLUSanalyse() throws ExceptionZZZ{
		Process objProcess = this.getProcessUsed();
		this.writeOutputToLogPLUSanalyse_(objProcess);
	}
	
	public void writeOutputToLogPLUSanalyse(Process objProcess) throws ExceptionZZZ{
		this.setProcessUsed(objProcess);
		this.writeOutputToLogPLUSanalyse_(objProcess);
	}
	
	
	private void writeOutputToLogPLUSanalyse_(Process objProcess) throws ExceptionZZZ{	
		main:{
			try{
				check:{					
					if(objProcess==null){
						ExceptionZZZ ez = new ExceptionZZZ("Process-Object", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
				}//END check:
			
				BufferedReader in = new BufferedReader( new InputStreamReader(objProcess.getInputStream()) );
				for ( String s; (s = in.readLine()) != null; ){
				    //System.out.println( s );
					
					//this.getLogObject().WriteLine(this.getNumber() +"#"+ s);
					//this.setStatusLocal(IProcessWatchRunnerOVPN.STATUSLOCAL.HASOUTPUT, true);
					
					s="gelesen aus InputStream: '" + s + "'";
					
					this.logProtocol(s);
					boolean bContinue = this.analyseInputLineCustom(s);
					if(!bContinue) break;
					Thread.sleep(20);
				}								
			} catch (IOException e) {
				ExceptionZZZ ez = new ExceptionZZZ("IOException happend: '" + e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			} catch (InterruptedException e) {
				ExceptionZZZ ez = new ExceptionZZZ("InterruptedException happend: '"+ e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//END main:
	}
}
