package basic.zBasic.util.moduleExternal.process.watch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.AbstractKernelUseObjectZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zKernel.status.EventObject4ProcessWatchStatusLocalZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalZZZ;
import basic.zKernel.status.KernelSenderObjectStatusLocalZZZ;

/**This class receives the stream from a process, which was started by the ConfigStarterZZZ class.
 * This is necessary, because the process will only goon working, if the streams were "catched" by a target.
 * This "catching" will be done in a special thread (one Thread per process).  
 * @author 0823
 *
 */
public class ProcessWatchRunnerZZZ extends AbstractProcessWatchRunnerZZZ {	
	protected ISenderObjectStatusLocalZZZ objEventStatusLocalBroker=null;//Das Broker Objekt, an dem sich andere Objekte regristrieren können, um ueber Aenderung eines StatusLocal per Event informiert zu werden.
	
	public ProcessWatchRunnerZZZ(IKernelZZZ objKernel, Process objProcess, int iNumber, String[] saFlag) throws ExceptionZZZ{
		super(objKernel, objProcess, iNumber, saFlag);		
	}

	//##########################################################
	public boolean start() {
		boolean bReturn = false;
		main:{
			String sLog = "ProcessWatchRunner #"+ this.getNumber() + " started.";
			this.logLineDate(sLog);
			
			//Solange laufen, bis ein Fehler auftritt oder eine Verbindung erkannt wird.
			do{
				this.writeErrorToLog();				
				boolean bHasError = this.getStatusLocal(ProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR);
				if(bHasError) break;

				this.writeOutputToLogPLUSanalyse();		//Man muss wohl erst den InputStream abgreifen, damit der Process weiterlaufen kann.				
				boolean bHasConnection = this.getStatusLocal(ProcessWatchRunnerZZZ.STATUSLOCAL.HASCONNECTION);
				if(bHasConnection) {
					sLog = "Connection wurde erstellt. Beende ProcessWatchRunner #"+this.getNumber();
					this.logLineDate(sLog);						
					break;
				}
				
				//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
				//Dann erzeuge den Event und feuer ihn ab.
				//Merke: Nun aber ueber das enum			
				if(this.getSenderStatusLocalUsed()!=null) {
					//IEventObjectStatusLocalSetZZZ event = new EventObjectStatusLocalSetZZZ(this,1,ClientMainOVPN.STATUSLOCAL.ISCONNECTED, true);
					//TODOGOON20230914: Woher kommt nun das Enum? Es gibt ja kein konkretes Beispiel
					IEventObjectStatusLocalZZZ event = new EventObject4ProcessWatchStatusLocalZZZ(this,1,(ProcessWatchRunnerZZZ.STATUSLOCAL)null, true);
					this.getSenderStatusLocalUsed().fireEvent(event);
				}			
			
				//Nach irgendeiner Ausgabe enden ist hier falsch, in einer abstrakten Klasse vielleicht richtig, quasi als Muster.
				//if(this.getFlag("hasOutput")) break;
				Thread.sleep(10);

				boolean bStopRequested = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP);
				if(bStopRequested) break;					
		}while(true);
		this.setStatusLocal(ProcessWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED, true);
		this.getLogObject().WriteLineDate("ProcessWatchRunner #"+ this.getNumber() + " ended.");
					
			bReturn = true;
		}//END main
		return bReturn;
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
					 
	 *  Obiges ist ein Beispiel für die Ausgabe eines Openvpn.exe Processes
	 * @throws ExceptionZZZ
	 * @author Fritz Lindhauer, 03.09.2023, 07:35:31
	 */
	public void writeOutputToLogPLUSanalyse() throws ExceptionZZZ{	
		main:{
			try{
				check:{
					if(this.objProcess==null){
						ExceptionZZZ ez = new ExceptionZZZ("Process-Object", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
				}//END check:
			
				BufferedReader in = new BufferedReader( new InputStreamReader(objProcess.getInputStream()) );
				for ( String s; (s = in.readLine()) != null; ){
				    //System.out.println( s );
					this.getLogObject().WriteLine(this.getNumber() +"#"+ s);
					this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.HASOUTPUT, true);
					
					boolean bAny = this.analyseInputLineCustom(s);
														
					Thread.sleep(20);
					boolean bStopRequested = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP);//Merke: STOPREQUEST ist eine Anweisung.. bleibt also ein Flag und ist kein Status
					if( bStopRequested) break main;
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
	
	
	@Override
	public boolean writeErrorToLogWithStatus() throws ExceptionZZZ {
		boolean bReturn = false;
		main:{			
			try{
				bReturn = this.writeErrorToLog();
				if(!bReturn)break main;
			   		
				this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR, true);
		    	Thread.sleep(20);

			} catch (InterruptedException e) {
				ExceptionZZZ ez = new ExceptionZZZ("InterruptedException happend: '"+ e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			bReturn = true;
		}//END Main:	
		return bReturn;
	}		





	//###### FLAGS
	/* (non-Javadoc)
	@see zzzKernel.basic.KernelObjectZZZ#getFlag(java.lang.String)
	Flags used: 
	- hasError
	- hasOutput
	- hasInput
	- stoprequested
	 */
	public boolean getFlag(String sFlagName){
		boolean bFunction = false;
		main:{
			if(StringZZZ.isEmpty(sFlagName)) break main;
			bFunction = super.getFlag(sFlagName);
			if(bFunction==true) break main;
		
			//getting the flags of this object
//			String stemp = sFlagName.toLowerCase();
//			if(stemp.equals("haserror")){
//				bFunction = bFlagHasError;
//				break main;
//			}else if(stemp.equals("hasconnection")) {
//				bFunction = bFlagHasConnection;
//				break main;
//			}
			
		}//end main:
		return bFunction;
	}

	/**
	 * @see AbstractKernelUseObjectZZZ.basic.KernelUseObjectZZZ#setFlag(java.lang.String, boolean)
	 * @param sFlagName
	 * Flags used:<CR>
	 	- hasError
	- hasOutput
	- hasInput
	- stoprequested
	 * @throws ExceptionZZZ 
	 */
	public boolean setFlag(String sFlagName, boolean bFlagValue) throws ExceptionZZZ{
		boolean bFunction = false;
		main:{			
			if(StringZZZ.isEmpty(sFlagName)) break main;
			bFunction = super.setFlag(sFlagName, bFlagValue);
			if(bFunction==true) break main;
		
		//setting the flags of this object
//		String stemp = sFlagName.toLowerCase();
//		if(stemp.equals("haserror")){
//			bFlagHasError = bFlagValue;
//			bFunction = true;
//			break main;
//		}else if(stemp.equals("hasconnection")) {
//			bFlagHasConnection = bFlagValue;
//			bFunction = true;
//			break main;
//		}

		}//end main:
		return bFunction;
	}
	
	//+++ aus IProcessWatchRunnerZZZ
	
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
						 
		 *  
		 * @throws ExceptionZZZ
		 * @author Fritz Lindhauer, 03.09.2023, 07:35:31
		 */
		@Override
		public boolean analyseInputLineCustom(String sLine) throws ExceptionZZZ {
			boolean bReturn = false;
			
			int iProcess = this.getNumber();
			String sLog = " Process#: " + iProcess + " - sLine=" + sLine;
			System.out.println(ReflectCodeZZZ.getPositionCurrent() + ": " + sLog);
			if(StringZZZ.contains(sLine,"TCP connection established with")) {				
				boolean bHasConnection = this.switchStatusLocalAllGroupTo(ProcessWatchRunnerZZZ.STATUSLOCAL.HASCONNECTION);
				bReturn = bHasConnection;
			}
			
			return bReturn;
		}


	
	//###### GETTER / SETTER
	//sind eher in der abstrakten Klasse
	
		
	//+++++ StatusLocal
	//Wirf beim Setzen des StatusLocal einen Event	
	@Override 
	public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal(enumStatus, null, bStatusValue);
		}//end main:
		return bFunction;
	}
	
	@Override 
	public boolean setStatusLocal(int iIndexOfProcess, Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal(iIndexOfProcess, enumStatus, null, bStatusValue);
		}//end main:
		return bFunction;
	}
	
	@Override 
	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bReturn = this.offerStatusLocal(enumStatus, null, bStatusValue);
		}//end main:
		return bReturn;
	}
	
	@Override 
	public boolean setStatusLocalEnum(int iIndexOfProcess, IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (ProcessWatchRunnerZZZ.STATUSLOCAL) enumStatusIn;
			
			bReturn = this.offerStatusLocal(iIndexOfProcess, enumStatus, null, bStatusValue);
		}//end main:
		return bReturn;
	}
	
	//################################################
		//+++ aus IStatusLocalUserMessageZZZ			
		@Override 
		public boolean setStatusLocal(Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) {
					break main;
				}
				ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (ProcessWatchRunnerZZZ.STATUSLOCAL) enumStatusIn;
				
				bFunction = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
			}//end main:
			return bFunction;
		}
		
		@Override 
		public boolean setStatusLocal(int iIndexOfProcess, Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) {
					break main;
				}
				ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (ProcessWatchRunnerZZZ.STATUSLOCAL) enumStatusIn;
				
				bFunction = this.offerStatusLocal_(iIndexOfProcess, enumStatus, sMessage, bStatusValue);
			}//end main:
			return bFunction;
		}
		
		@Override 
		public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				if(enumStatusIn==null) {
					break main;
				}
				ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (ProcessWatchRunnerZZZ.STATUSLOCAL) enumStatusIn;
				
				bReturn = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
			}//end main:
			return bReturn;
		}				
		
		@Override 
		public boolean setStatusLocalEnum(int iIndexOfProcess, IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				if(enumStatusIn==null) {
					break main;
				}
				ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (ProcessWatchRunnerZZZ.STATUSLOCAL) enumStatusIn;
				
				bReturn = this.offerStatusLocal(iIndexOfProcess, enumStatus, null, bStatusValue);
			}//end main:
			return bReturn;
		}
		
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	@Override 
	public boolean offerStatusLocal(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal_(-1, enumStatus, sStatusMessage, bStatusValue);
		}//end main:
		return bFunction;
	}
		
	@Override
	public boolean offerStatusLocal(int iIndexOfProcess, Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal_(iIndexOfProcess, enumStatus, sStatusMessage, bStatusValue);
		}//end main:
		return bFunction;
	}
	
	
	private boolean offerStatusLocal_(int iIndexOfProcess, Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
		//return this.getStatusLocal(objEnumStatus.name());
		//Nein, trotz der Redundanz nicht machen, da nun der Event anders gefeuert wird, nämlich über das enum
		
	    //Merke: In anderen Klassen, die dieses Design-Pattern anwenden ist das eine andere Klasse fuer das Enum
	    ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
		String sStatusName = enumStatus.name();
		bFunction = this.proofStatusLocalExists(sStatusName);															
		if(bFunction == true){
			
			//Setze das Flag nun in die HashMap
			HashMap<String, Boolean> hmStatus = this.getHashMapStatusLocal();
			hmStatus.put(sStatusName.toUpperCase(), bStatusValue);
			
			String sStatusMessageToSet = null;
			if(StringZZZ.isEmpty(sStatusMessage)){
				if(bStatusValue) {
					sStatusMessageToSet = enumStatus.getStatusMessage();
				}else {
					sStatusMessageToSet = "NICHT " + enumStatus.getStatusMessage();
				}			
			}else {
				sStatusMessageToSet = sStatusMessage;
			}	
			String sLog = ReflectCodeZZZ.getPositionCurrent() + " ProcessWatchRunner verarbeite sStatusMessageToSet='" + sStatusMessageToSet + "'";
			System.out.println(sLog);
			this.logLineDate(sLog);
					
			//Falls eine Message extra uebergeben worden ist, ueberschreibe...
			if(sStatusMessage!=null) {
				sStatusMessageToSet = sStatusMessage;
				sLog = ReflectCodeZZZ.getPositionCurrent() + " ProcessWatchRunner uebersteuere sStatusMessageToSet='" + sStatusMessage + "'";
				System.out.println(sLog);
				this.logLineDate(sLog);					
			}	
			//Merke: Dabei wird die uebergebene Message in den speziellen "Ringspeicher" geschrieben, auch NULL Werte...
			this.offerStatusLocalEnum(enumStatus, bStatusValue, sStatusMessageToSet);
			
		
			//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
			//Dann erzeuge den Event und feuer ihn ab.
			//Merke: Nun aber ueber das enum			
			if(this.getSenderStatusLocalUsed()!=null) {
				IEventObjectStatusLocalZZZ event = new EventObject4ProcessWatchStatusLocalZZZ(this,enumStatus, bStatusValue);
				this.getSenderStatusLocalUsed().fireEvent(event);
			}			
			bFunction = true;								
		}										
	}	// end main:
	return bFunction;
	}
	
	//Merke: Hier wird zusätzlich noch ein Event gefeuert
		@Override
		public boolean setStatusLocal(String sStatusName, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(StringZZZ.isEmpty(sStatusName)) {
					bFunction = true;
					break main;
				}
							
				bFunction = this.proofStatusLocalExists(sStatusName);															
				if(bFunction){
					
					bFunction = this.proofStatusLocalValueChanged(sStatusName, bStatusValue);
					if(bFunction) {		
						
						//Holes die HashMap
						HashMap<String, Boolean> hmStatus = this.getHashMapStatusLocal();
						//Setze den erwiesenermassen geaenderten Status nun in die HashMap
						hmStatus.put(sStatusName.toUpperCase(), bStatusValue);
						
						//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
						//Dann erzeuge den Event und feuer ihn ab.
						if(this.getSenderStatusLocalUsed()!=null) {
							IEventObjectStatusLocalZZZ event = new EventObject4ProcessWatchStatusLocalZZZ(this,sStatusName.toUpperCase(), bStatusValue);
							this.getSenderStatusLocalUsed().fireEvent(event);
						}
						
						bFunction = true;
					}
				}										
			}	// end main:
			
			return bFunction;
		}

	@Override
	public void registerForStatusLocalEvent(IListenerObjectStatusLocalZZZ objEventListener) throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().addListenerObjectStatusLocal(objEventListener);
	}

	@Override
	public void unregisterForStatusLocalEvent(IListenerObjectStatusLocalZZZ objEventListener) throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().removeListenerObjectStatusLocal(objEventListener);
	}
	
	//### Aus ISenderObjectStatusLocalSetUserZZZ
	/* (non-Javadoc)
	 * @see basic.zKernel.status.ISenderObjectStatusLocalSetUserZZZ#getSenderStatusLocalUsed()
	 */
	@Override
	public ISenderObjectStatusLocalZZZ getSenderStatusLocalUsed() throws ExceptionZZZ {
		if(this.objEventStatusLocalBroker==null) {
			//++++++++++++++++++++++++++++++
			//Nun geht es darum den Sender fuer Aenderungen am Status zu erstellen, der dann registrierte Objekte ueber Aenderung von Flags informiert
			ISenderObjectStatusLocalZZZ objSenderStatusLocal = new KernelSenderObjectStatusLocalZZZ();
			this.objEventStatusLocalBroker = objSenderStatusLocal;
		}
		return this.objEventStatusLocalBroker;
	}

	@Override
	public void setSenderStatusLocalUsed(ISenderObjectStatusLocalZZZ objEventSender) {
		this.objEventStatusLocalBroker = objEventSender;
	}

	//### aus iStatusLocalUser
	@Override
	public boolean isStatusLocalRelevant(IEnumSetMappedStatusZZZ objEnumStatusIn) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			//Merke: enumStatus hat class='class use.openvpn.client.process.IProcessWatchRunnerOVPN$STATUSLOCAL'				
			if(!(objEnumStatusIn instanceof IProcessWatchRunnerZZZ.STATUSLOCAL) ){
				String sLog = ReflectCodeZZZ.getPositionCurrent()+": enumStatus wird wg. unpassender Klasse ignoriert.";
				System.out.println(sLog);
				//this.objMain.logMessageString(sLog);
				break main;
			}		
			bReturn = true;

		}//end main:
		return bReturn;
	}
	
	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(objEnumStatusIn==null) {
				break main;
			}
			
			//Merke: Bei einer anderen Klasse, die dieses DesingPattern nutzt, befindet sich der STATUSLOCAL in einer anderen Klasse
			ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) objEnumStatusIn;
			String sStatusName = enumStatus.name();
			if(StringZZZ.isEmpty(sStatusName)) break main;
										
			HashMap<String, Boolean> hmFlag = this.getHashMapStatusLocal();
			Boolean objBoolean = hmFlag.get(sStatusName.toUpperCase());
			if(objBoolean==null){
				bFunction = false;
			}else{
				bFunction = objBoolean.booleanValue();
			}
							
		}	// end main:
		
		return bFunction;	
	}

	//### aus IEventBrokerStatusLocalSetUserZZZ
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ objEventListener) throws ExceptionZZZ {
		TODOGOON20240201;//Den Status so setzen, das der Monitor damit was anfangen kann		
		//return this.setStatusLocal(objEventListener);
	}
	}//END class
