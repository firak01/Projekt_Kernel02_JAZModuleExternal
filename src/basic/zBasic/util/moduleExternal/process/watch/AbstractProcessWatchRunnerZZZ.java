package basic.zBasic.util.moduleExternal.process.watch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

import base.files.EncodingMaintypeZZZ.TypeZZZ;
import basic.zKernel.KernelZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.EventObject4ProcessWatchStatusLocalZZZ;
import basic.zKernel.status.IEventBrokerStatusLocalUserZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalZZZ;
import basic.zKernel.status.IStatusLocalMessageUserZZZ;
import basic.zKernel.status.StatusLocalAvailableHelperZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramWithStatusOnStatusListeningRunnableZZZ;
import basic.zBasic.component.IModuleUserZZZ;
import basic.zBasic.component.IModuleZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.component.IProgramZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.datatype.string.StringArrayZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zKernel.AbstractKernelUseObjectWithStatusZZZ;
import basic.zKernel.AbstractKernelUseObjectZZZ;

/**This class receives the stream from a process, which was started by the ConfigStarterZZZ class.
 * This is necessary, because the process will only goon working, if the streams were "catched" by a target.
 * This "catching" will be done in a special thread (one Thread per process).  
 * @author 0823
 *
 */
public abstract class AbstractProcessWatchRunnerZZZ extends AbstractProgramWithStatusOnStatusListeningRunnableZZZ implements IProcessWatchRunnerZZZ{
	private static final long serialVersionUID = 6586079955658760005L;
	protected volatile Process objProcess=null; //Der externe process, der hierdurch "gemonitored" werden soll
	protected volatile String sLineFilter = null;

	public AbstractProcessWatchRunnerZZZ() throws ExceptionZZZ{
		super();				
	}
	
	public AbstractProcessWatchRunnerZZZ(Process objProcess) throws ExceptionZZZ{
		super();		
		ProcessWatchRunnerNew_(objProcess, null, null);
	}
	public AbstractProcessWatchRunnerZZZ(Process objProcess, String sFlag) throws ExceptionZZZ{
		super();
		String[]saFlag=new String[1];
		saFlag[0]=sFlag;
		ProcessWatchRunnerNew_(objProcess, null, saFlag);
	}
	public AbstractProcessWatchRunnerZZZ(Process objProcess, String sLineFilter, String sFlag) throws ExceptionZZZ{
		super();
		String[]saFlag=new String[1];
		saFlag[0]=sFlag;
		ProcessWatchRunnerNew_(objProcess, sLineFilter, saFlag);
	}
	
	public AbstractProcessWatchRunnerZZZ(Process objProcess, String[] saFlag) throws ExceptionZZZ{
		super();
		ProcessWatchRunnerNew_(objProcess,  null, saFlag);
	}
	
	public AbstractProcessWatchRunnerZZZ(Process objProcess, String sLineFilter, String[] saFlag) throws ExceptionZZZ{
		super();
		ProcessWatchRunnerNew_(objProcess,  sLineFilter, saFlag);
	}
	
	private void ProcessWatchRunnerNew_(Process objProcess, String sLineFilter, String[] saFlagControl) throws ExceptionZZZ{		
		main:{			
			check:{
				if(saFlagControl != null){
					String stemp; boolean btemp;
					for(int iCount = 0;iCount<=saFlagControl.length-1;iCount++){
						stemp = saFlagControl[iCount];
						btemp = setFlag(stemp, true);
						if(btemp==false){ 								   
							   ExceptionZZZ ez = new ExceptionZZZ(stemp, IFlagZUserZZZ.iERROR_FLAG_UNAVAILABLE, this, ReflectCodeZZZ.getMethodCurrentName()); 							  
							   throw ez;		 
						}
					}
					if(this.getFlag("init")) break main;
				}
								
				if(objProcess==null){
					ExceptionZZZ ez = new ExceptionZZZ("Process - Object", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
			}//END check
	
			this.objProcess = objProcess;
			this.sLineFilter = sLineFilter;
		}//END main:
	}
	
	
	//#### GETTER / SETTER
		
	//+++ aus IProcessWatchRunnerZZZ
	@Override
	public String getLineFilter() {
		return this.sLineFilter;
	}

	@Override
	public void setLineFilter(String sLineFilter) {
		this.sLineFilter = sLineFilter;
	}
		
	/**Returns the process - object passed as a parameter of the constructor 
	 * Hint: Therefore is no setter-method available
	 * @return Process
	 *
	 * javadoc created by: 0823, 06.07.2006 - 16:48:57
	 */
	@Override
	public Process getProcessWatched(){
		return this.objProcess;
	}
	
	@Override
	public void setProcessWatched(Process objProcess) {
		this.objProcess = objProcess;
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
				String sLog;
				check:{
					Process objProcess = this.getProcessWatched();
					if(objProcess==null){
						ExceptionZZZ ez = new ExceptionZZZ("Process-Object", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
				}//END check:
			
				BufferedReader in = new BufferedReader( new InputStreamReader(objProcess.getInputStream()) );
				for ( String s; (s = in.readLine()) != null; ){
					sLog="gelesen aus InputStream: '" + s + "'";
					this.logProtocolString(sLog);
					this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.HASOUTPUT, true);
				
					boolean bAny = this.analyseInputLineCustom(s);
														
					Thread.sleep(20);
					boolean bStopRequested = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP);//Merke: STOPREQUEST ist eine Anweisung.. bleibt also ein Flag und ist kein Status
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

	//MErke: Die genaue Analyse muss im konkreten Process Watch Runner gemacht werden.
	@Override
	public abstract boolean analyseInputLineCustom(String sLine) throws ExceptionZZZ;


	
	@Override
	public boolean writeErrorToLog() throws ExceptionZZZ{
		boolean bReturn = false;
		main:{			
			try{
				check:{
					if(this.objProcess==null){
						ExceptionZZZ ez = new ExceptionZZZ("Process-Object", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
				}//END check:
			   		
			    BufferedReader err = new BufferedReader(new InputStreamReader(objProcess.getErrorStream()) );
			    for ( String s; (s = err.readLine()) != null; ){				    
			    	this.logProtocolString("ERROR: "+ s);			
			    	if( this.getFlag("stoprequested")==true) break main;
				}
			} catch (IOException e) {
				ExceptionZZZ ez = new ExceptionZZZ("IOException happend: '" + e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			bReturn = true;
		}//END Main:	
		return bReturn;
	}
	
	
	@Override
	public boolean startCustom() throws ExceptionZZZ {
		return startProcessWatch_();
	}
	
	private boolean startProcessWatch_() throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			try {
			String sLog = "ProcessWatchRunner started.";
			this.logLineDate(sLog);
			
			//Solange laufen, bis ein Fehler auftritt oder eine Verbindung erkannt wird.
			do{							
				boolean bHasError = this.getStatusLocal(ProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR);
				if(bHasError) break;//das wäre dann ein von mir selbst erzeugter Fehler, der nicht im STDERR auftaucht.
				
				this.writeOutputToLogPLUSanalyse();		//Man muss wohl erst den InputStream abgreifen, damit der Process weiterlaufen kann.				
				boolean bHasFilerFound = this.getStatusLocal(ProcessWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND);
				if(bHasFilerFound) {
					sLog = ReflectCodeZZZ.getPositionCurrent() + "Filter wurde gefunden.";
					this.logProtocolString(sLog);
					if(this.getFlag(IWatchRunnerZZZ.FLAGZ.END_ON_FILTER_FOUND)) {
						sLog = "Filter gefunden... Gemaess Flag, beende.";
						this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, true);
					}
				}
				
				//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
				//Dann erzeuge den Event und feuer ihn ab.
				//Merke: Nun aber ueber das enum			
				if(this.getSenderStatusLocalUsed()!=null) {
					//IEventObjectStatusLocalSetZZZ event = new EventObjectStatusLocalSetZZZ(this,1,ClientMainOVPN.STATUSLOCAL.ISCONNECTED, true);
					//TODOGOON20230914: Woher kommt nun das Enum? Es gibt ja kein konkretes Beispiel
					IEventObjectStatusLocalZZZ event = new EventObject4ProcessWatchStatusLocalZZZ(this,(ProcessWatchRunnerZZZ.STATUSLOCAL)null, true);
					this.getSenderStatusLocalUsed().fireEvent(event);
				}			
			
				//Nach irgendeiner Ausgabe enden ist hier falsch, in einer abstrakten Klasse vielleicht richtig, quasi als Muster.
				//if(this.getFlag("hasOutput")) break;
				Thread.sleep(10);

				boolean bStopRequested = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP);
				if(bStopRequested) break;					
			}while(true);
			this.setStatusLocal(ProcessWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED, true);
			sLog = "ProcessWatchRunner ended.";
			this.logProtocolString(sLog);
						
			bReturn = true;
			}catch (InterruptedException e) {					
				try {
					String sLogIE = e.getMessage();
					this.logProtocolString("An error happend: '" + sLogIE + "'");
					this.writeErrorToLog();
				} catch (ExceptionZZZ e1) {
					System.out.println(e1.getDetailAllLast());
					e1.printStackTrace();
				}
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}//END main
		return bReturn;
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
	
	/**TODO Ich weiss noch garnicht, was ich senden soll und wie es gehen kann.
	 * Ziel w�re es z.B. mit der Test F4 den Process herunterzufahren.
	 * @param sOut
	 * @throws ExceptionZZZ, 
	 *
	 * @return void
	 *
	 * javadoc created by: 0823, 07.07.2006 - 17:29:31
	 */
	public void sendStringToProcess(String sOut) throws ExceptionZZZ{
		main:{
			try{
				check:{
					if(this.objProcess==null){
						ExceptionZZZ ez = new ExceptionZZZ("Process-Object", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					if(sOut==null)  break main; //Absichltich keine Exception					
				}//END check:
			
				BufferedWriter out = new BufferedWriter( new OutputStreamWriter(objProcess.getOutputStream()) );
				out.write(sOut);
			
				this.logProtocolString("STRING SEND TO PROCESS: "+ sOut);
				this.setFlag("hasInput", true);
				
			} catch (IOException e) {
				ExceptionZZZ ez = new ExceptionZZZ("IOException happend: '" + e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//END main:
	}

	
	//###############################
	//### FLAG HANDLING aus: IWatchRunnerZZZ
	//###############################
	@Override
	public boolean getFlag(IWatchRunnerZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(IWatchRunnerZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(IWatchRunnerZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IWatchRunnerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(IWatchRunnerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(IWatchRunnerZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}
	
	
	//###############################
	//### FLAG HANDLING aus: IProcessWatchRunnerZZZ
	//###############################
	@Override
	public boolean getFlag(IProcessWatchRunnerZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(IProcessWatchRunnerZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(IProcessWatchRunnerZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IProcessWatchRunnerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(IProcessWatchRunnerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(IProcessWatchRunnerZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}
	
	//###############################
	//### FLAG HANDLING aus: IStatusLocalMessageUserZZZ
	//###############################
	@Override
	public boolean getFlag(IStatusLocalMessageUserZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(IStatusLocalMessageUserZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(IStatusLocalMessageUserZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IStatusLocalMessageUserZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(IStatusLocalMessageUserZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(IStatusLocalMessageUserZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}
	
			
	//##########################################
	//### FLAG HANDLING: IProgramRunnableZZZ
	@Override
	public boolean getFlag(IProgramRunnableZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}
	@Override
	public boolean setFlag(IProgramRunnableZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}
	
	@Override
	public boolean[] setFlag(IProgramRunnableZZZ.FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IProgramRunnableZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}
	
	@Override
	public boolean proofFlagExists(IProgramRunnableZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
			return this.proofFlagExists(objEnumFlag.name());
		}
	
	@Override
	public boolean proofFlagSetBefore(IProgramRunnableZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagSetBefore(objEnumFlag.name());
	}	
	
	
	//### Aus IProgramZZZ
	@Override
	public String getProgramName(){
		if(StringZZZ.isEmpty(this.sProgramName)) {
			if(this.getFlag(IProgramZZZ.FLAGZ.ISPROGRAM.name())) {
				this.sProgramName = this.getClass().getName();
			}
		}
		return this.sProgramName;
	}
	
	@Override
	public String getProgramAlias() throws ExceptionZZZ {		
		return null;
	}
		
	@Override
	public void resetProgramUsed() {
		this.sProgramName = null;
	}
	
	@Override
	public boolean getFlag(IProgramZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}
	@Override
	public boolean setFlag(IProgramZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}
	
	@Override
	public boolean[] setFlag(IProgramZZZ.FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IProgramZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}
	
	@Override
	public boolean proofFlagExists(IProgramZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
			return this.proofFlagExists(objEnumFlag.name());
		}
	
	@Override
	public boolean proofFlagSetBefore(IProgramZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagSetBefore(objEnumFlag.name());
	}	


	
	
	//### Aus IModuleUserZZZ	
	@Override
	public IModuleZZZ getModule() {
		return this.objModule;
	}
	
	@Override
	public void setModule(IModuleZZZ objModule) {
		this.objModule = objModule;
	}
	
	//### Aus IModuleZZZ
		public String readModuleName() throws ExceptionZZZ {
			String sReturn = null;
			main:{
				sReturn = this.getClass().getName();
			}//end main:
			return sReturn;
		}
		
		@Override
		public String getModuleName() throws ExceptionZZZ{
			if(StringZZZ.isEmpty(this.sModuleName)) {
				this.sModuleName = this.readModuleName();
			}
			return this.sModuleName;
		}
		
		@Override
		public void setModuleName(String sModuleName){
			this.sModuleName=sModuleName;
		}
		
		@Override
		public void resetModuleUsed() {
			this.setModuleName(null);
		}
	
	
	@Override
	public boolean getFlag(IModuleUserZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}
	@Override
	public boolean setFlag(IModuleUserZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}
	
	@Override
	public boolean[] setFlag(IModuleUserZZZ.FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IModuleUserZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}
	
	@Override
	public boolean proofFlagExists(IModuleUserZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
			return this.proofFlagExists(objEnumFlag.name());
	}
	
	@Override
	public boolean proofFlagSetBefore(IModuleUserZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}		
	//##########################
	
	//#############################################
	//### STATUS
	//#############################################
	
	//### Aus IStatusLocalUserZZZ
	
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
//					if(this.getSenderStatusLocalUsed()!=null) {
//						IEventObjectStatusLocalSetZZZ event = new EventObject4ProcessWatchStatusLocalSetZZZ(this,1,sStatusName.toUpperCase(), bStatusValue);
//						this.getSenderStatusLocalUsed().fireEvent(event);
//					}
					
					bFunction = true;
				}
			}										
		}	// end main:
		
		return bFunction;
	}
		
    //### Statische Methode (um einfacher darauf zugreifen zu können)
    public static Class getEnumStatusLocalClass(){
    	try{
    		System.out.println(ReflectCodeZZZ.getPositionCurrent() + ": Diese Methode muss in den daraus erbenden Klassen überschrieben werden.");
    	}catch(ExceptionZZZ ez){
			String sError = "ExceptionZZZ: " + ez.getMessageLast() + "+\n ThreadID:" + Thread.currentThread().getId() +"\n";			
			System.out.println(sError);
		}
    	return STATUSLOCAL.class;    	
    }


  	
}//END class
