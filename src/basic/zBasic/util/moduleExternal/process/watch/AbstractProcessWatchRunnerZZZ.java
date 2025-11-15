package basic.zBasic.util.moduleExternal.process.watch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramWithStatusOnStatusListeningRunnableZZZ;
import basic.zBasic.component.IModuleUserZZZ;
import basic.zBasic.component.IModuleZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.component.IProgramZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zKernel.status.IStatusLocalMessageUserZZZ;

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
	
	public AbstractProcessWatchRunnerZZZ(String[]saFlagControl) throws ExceptionZZZ{
		super(saFlagControl);				
	}
	
	public AbstractProcessWatchRunnerZZZ(Process objProcess) throws ExceptionZZZ{
		super();		
		ProcessWatchRunnerNew_(objProcess, null);
	}
	public AbstractProcessWatchRunnerZZZ(Process objProcess, String sFlag) throws ExceptionZZZ{
		super(sFlag);
		ProcessWatchRunnerNew_(objProcess, null);
	}
	public AbstractProcessWatchRunnerZZZ(Process objProcess, String sLineFilter, String sFlag) throws ExceptionZZZ{
		super(sFlag);
		ProcessWatchRunnerNew_(objProcess, sLineFilter);
	}
	
	public AbstractProcessWatchRunnerZZZ(Process objProcess, String[] saFlag) throws ExceptionZZZ{
		super(saFlag);
		ProcessWatchRunnerNew_(objProcess,  null);
	}
	
	public AbstractProcessWatchRunnerZZZ(Process objProcess, String sLineFilter, String[] saFlag) throws ExceptionZZZ{
		super(saFlag);
		ProcessWatchRunnerNew_(objProcess,  sLineFilter);
	}
	
	private void ProcessWatchRunnerNew_(Process objProcess, String sLineFilter) throws ExceptionZZZ{		
		main:{			
			if(this.getFlag("init")) break main;
										
			if(objProcess==null){
				ExceptionZZZ ez = new ExceptionZZZ("Process - Object", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
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
		
		
	//#############################################
	//### aus IWatchRunnerZZZ
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
	@Override
	public boolean writeOutputToLogPLUSanalyse(int iLineCounter, String sLine, String sLineFilter) throws ExceptionZZZ{	
		boolean bReturn = false;
		main:{									
				if(StringZZZ.isEmpty(sLine)) break main;
				
				if(StringZZZ.isEmpty(sLineFilter)){
					ExceptionZZZ ez = new ExceptionZZZ("LineFilter-String", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				String sLog;
				
				//+++ Die Zeile ausgeben und analysieren					
                sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Gelesen aus InputStream - " + iLineCounter +"\t: '" + sLine + "'";
                this.logProtocol(sLog);
               		
				bReturn = this.analyseInputLineCustom(sLine, sLineFilter);												
		}//END main:
		return bReturn;
	}
	
	@Override
	public boolean writeErrorToLog(int iLineCounter, String sErrorLine) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{								   		    

			this.logProtocol("ERROR processing line (" + iLineCounter + ") : " + sErrorLine + "'");			
			
			bReturn = true;
		}//END Main:	
		return bReturn;
	}
	
	@Override
	public boolean writeErrorToLogWithStatus(Process objProcess) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{			
			try{
				check:{
					if(objProcess==null){
						ExceptionZZZ ez = new ExceptionZZZ("Process-Object", iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
				}//END check:
			   		
				int icounter = 0;
			    BufferedReader err = new BufferedReader(new InputStreamReader(objProcess.getErrorStream()) );			    
			    for ( String s; (s = err.readLine()) != null; ){
			    	if( this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP)==true) break main;
			    	
			    	this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR, true);
			    	
			    	icounter++;
			    	bReturn = this.writeErrorToLog(icounter, s);	
			    	if(!bReturn)break main;
			    	
			    	Thread.sleep(20);
				}
			} catch (IOException e) {
				ExceptionZZZ ez = new ExceptionZZZ("IOException happend: '" + e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			} catch (InterruptedException e) {
				ExceptionZZZ ez = new ExceptionZZZ("InterruptedException happend: '"+ e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			bReturn = true;
		}//END Main:	
		return bReturn;
	}
	

	@Override
	public boolean writeErrorToLogWithStatus(int iLineCounter, String sErrorLine) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{			
			try{
				bReturn = this.writeErrorToLog(iLineCounter, sErrorLine);
				if(!bReturn)break main;
			   		
				this.setStatusLocal(IWatchRunnerZZZ.STATUSLOCAL.HASERROR, true);
		    	Thread.sleep(20);

			} catch (InterruptedException e) {
				ExceptionZZZ ez = new ExceptionZZZ("InterruptedException happend: '"+ e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			bReturn = true;
		}//END Main:	
		return bReturn;
	}	


	//Merke: Die genaue Analyse muss im konkreten Process Watch Runner gemacht werden.
	//Merke: Die eine ggfs. von diesem Einfachsten Fall abweichende, genauere Analyse muss ggfs. im konkreten LogFile Watch Runner gemacht werden.
	@Override
	public boolean analyseInputLineCustom(String sLine, String sLineFilter) throws ExceptionZZZ {
		boolean bReturn = false; //true, wenn der Filter gefunden wurde
		main:{
			if(StringZZZ.isEmpty(sLineFilter)) break main;
					
			sLine = StringZZZ.trimAnyQuoteMarked(sLine);
			String sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Analysierte Zeile: + '" + sLine + "'";
    		this.logProtocol(sLog);
    		
			if(StringZZZ.isEmpty(sLine)) break main;
			
		
			if(StringZZZ.contains(sLine, sLineFilter)) {
        		sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Hat Zeilenfilter gefunden: '" + sLineFilter + "'";
        		this.logProtocol(sLog);
        		
        		bReturn = true;
			}       			
		}//end main:
		return bReturn;
	}



	

	//#############################################
	@Override
	public boolean startCustom() throws ExceptionZZZ {
		return startProcessWatch_();
	}
	
	private boolean startProcessWatch_() throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			String sLog;
			BufferedReader brin = null;
			try {
				sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> ProcessWatchRunner started.";
				this.logProtocol(sLog);
				
				String sLineFilter = this.getLineFilter();
				if(StringZZZ.isEmpty(sLineFilter)) {
					ExceptionZZZ ez = new ExceptionZZZ("ObjectWithStatusRunnable - Keine Zeilenfilter gesetzt.", this.iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				//Warte auf die Existenz einer auszulesenden Datei ist nicht notwendig.
				//... hier wird mit einem Process gearbeitet.
							
				Process objProcess = this.getProcessWatched();
				brin = new BufferedReader(new InputStreamReader(objProcess.getInputStream()) );
				if(!brin.ready()){
					ExceptionZZZ ez = new ExceptionZZZ("BufferdReader-Object not ready", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				//Merke: Darin ist ene Endlosschleife
				int icount=0;
				String sLine;
				
				do{
					//Solange laufen, bis ein Fehler auftritt oder ein Filter erkannt wird.
					boolean bHasError = this.getStatusLocal(ProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR);
					if(bHasError) break;//das wäre dann ein von mir selbst erzeugter Fehler, der nicht im STDERR auftaucht.
					
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP)) { //Merke: Das ist eine Anweisung und kein Status. Darum bleibt es beim Flag.
						sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Hat Flag gesetzt '" + IProgramRunnableZZZ.FLAGZ.REQUEST_STOP .name() + "'. Breche ab.";
						this.logProtocol(sLog);
						break;
					}
					
					icount++;
					sLine = brin.readLine();
					sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Gelesene Zeile: '" + sLine + "'";
					this.logProtocol(sLog);
					if(!StringZZZ.isEmpty(sLine)) {
	               		this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.HASOUTPUT, true);
	               	}
					
					boolean bFilterFound = this.writeOutputToLogPLUSanalyse(icount, sLine, sLineFilter);		//Man muss wohl erst den InputStream abgreifen, damit der Process weiterlaufen kann.
					if(bFilterFound) {
						sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Filter '" + sLineFilter + "' wurde gefunden in Zeile " + icount;
						this.logProtocol(sLog);
						
						//... ein Event soll auch beim Setzen des passenden Status erzeugt und geworfen werden.						
		        		this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND,true);
						sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Status '" + ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND.name() + "' gesetzt.";
						this.logProtocol(sLog);
						
						//Hier wird sofort abgebrochen. Es wird also nicht auf das Setzen von REQUEST_STOP per Event gewartet.
						//Das kann z.B. bei dem "Direkten" Test auch nicht erfolgen.
						boolean bFlagEndImmediate = this.getFlag(IWatchListenerZZZ.FLAGZ.IMMEDIATE_END_ON_FILTER_FOUND);
						if(bFlagEndImmediate){
						   if(this.getFlag(IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND)){
								sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Filter gefunden... Gemaess Flag '" + IWatchListenerZZZ.FLAGZ.IMMEDIATE_END_ON_FILTER_FOUND.name() +"', beende per Flag aber ohne auf den Event zu warten '" +IProgramRunnableZZZ.FLAGZ.REQUEST_STOP.name() + "'";
								this.logProtocol(sLog);
								this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, true);
							}					
							Thread.sleep(100);
						}else {
							//Warte darauf, dass der Monitor beendet wird und ende erst dann...
							
						}	
					}else{
						//Entsprechend hier wieder den Filter verloren
						//Einen Extra Event sollte man hier nicht erzeugen muessen. Den Status zu setzen sollte bereits einen entsprechenden Status Event abfeuern.
						this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, false);	
						//end bFilterFound
					}
					Thread.sleep(100);
				}while(true);
				this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED,true);
				sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Ended.";
				this.logProtocol(sLog);
				              	
	            bReturn = true;
              
			}catch (InterruptedException e) {
				e.printStackTrace();
				try {
					this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR,true);
					sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> HASERROR Status gesetzt.";
					this.logProtocol(sLog);
				} catch (ExceptionZZZ e1) {
					System.out.println(e1.getDetailAllLast());
					e1.printStackTrace();
				}
				ExceptionZZZ ez = new ExceptionZZZ("InterruptedException happend: '"+ e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
				
			} catch (IOException e) {
				e.printStackTrace();
				try {
					this.setStatusLocal(IProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR,true);
					sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> HASERROR Status gesetzt.";
					this.logProtocol(sLog);
				} catch (ExceptionZZZ e1) {
					System.out.println(e1.getDetailAllLast());
					e1.printStackTrace();
				}
				ExceptionZZZ ez = new ExceptionZZZ("IOException happend: '" + e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			} finally {
				if(brin!=null) {
					IOUtils.closeQuietly(brin);
				}
	        }
		}//END main
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
			
				String sLog = ReflectCodeZZZ.getPositionCurrent()+ this.getClass().getSimpleName() + "=> STRING SEND TO PROCESS: '"+ sOut + "'";
				this.logProtocol(sLog);
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
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
	
	//##########################################
	//### FLAG HANDLING: IWatchListenerZZZ
	@Override
	public boolean getFlag(IWatchListenerZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}
	@Override
	public boolean setFlag(IWatchListenerZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}
	
	@Override
	public boolean[] setFlag(IWatchListenerZZZ.FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IWatchListenerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}
	
	@Override
	public boolean proofFlagExists(IWatchListenerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
			return this.proofFlagExists(objEnumFlag.name());
		}
	
	@Override
	public boolean proofFlagSetBefore(IWatchListenerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
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
//	@Override
//	public boolean setStatusLocal(String sStatusName, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(StringZZZ.isEmpty(sStatusName)) {
//				bFunction = true;
//				break main;
//			}
//						
//			bFunction = this.proofStatusLocalExists(sStatusName);															
//			if(bFunction){
//				
//				bFunction = this.proofStatusLocalValueChanged(sStatusName, bStatusValue);
//				if(bFunction) {		
//					
//					//Holes die HashMap
//					HashMap<String, Boolean> hmStatus = this.getHashMapStatusLocal();
//					//Setze den erwiesenermassen geaenderten Status nun in die HashMap
//					hmStatus.put(sStatusName.toUpperCase(), bStatusValue);
//					
//					//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
//					//Dann erzeuge den Event und feuer ihn ab.
//					if(this.getSenderStatusLocalUsed()!=null) {
//						IEventObjectStatusLocalSetZZZ event = new EventObject4ProcessWatchStatusLocalSetZZZ(this,1,sStatusName.toUpperCase(), bStatusValue);
//						this.getSenderStatusLocalUsed().fireEvent(event);
//					}
//					
//					bFunction = true;
//				}
//			}										
//		}	// end main:
//		
//		return bFunction;
//	}
		
    //### Statische Methode (um einfacher darauf zugreifen zu können)
    public static Class getEnumStatusLocalClass(){
    	try{
    		System.out.println(ReflectCodeZZZ.getPositionCurrent() + "Diese Methode muss in den daraus erbenden Klassen überschrieben werden.");
    	}catch(ExceptionZZZ ez){
			String sError = "ExceptionZZZ: " + ez.getMessageLast() + "+\n ThreadID:" + Thread.currentThread().getId() +"\n";			
			System.out.println(sError);
		}
    	return STATUSLOCAL.class;    	
    }


  	
}//END class
