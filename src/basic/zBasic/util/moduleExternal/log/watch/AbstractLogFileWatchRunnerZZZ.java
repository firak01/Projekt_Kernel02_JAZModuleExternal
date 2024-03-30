package basic.zBasic.util.moduleExternal.log.watch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramWithStatusOnStatusListeningRunnableZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.process.watch.ProcessWatchRunnerZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.EventObject4LogFileWatchRunnerStatusLocalZZZ;
import basic.zKernel.status.EventObject4ProcessWatchStatusLocalZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchRunnerStatusLocalZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.IListenerObjectStatusBasicZZZ;
import basic.zKernel.status.IStatusLocalMessageUserZZZ;

public abstract class AbstractLogFileWatchRunnerZZZ extends AbstractProgramWithStatusOnStatusListeningRunnableZZZ implements ILogFileWatchRunnerZZZ{
	private static final long serialVersionUID = 6586079955658760005L;
	protected volatile File objLogFile=null;
	protected volatile String sLineFilter = null;

	//Also: Gib dem LogFileWatchRunner einen Status mit.
	//      Lass andere Objekte an diesem Runner sich registrieren
	//      Der Runner wirft bei Änderung des Status (z.B. "Suchtext gefunden") einen entsprechenden Event.

	public AbstractLogFileWatchRunnerZZZ() throws ExceptionZZZ {
		super();		
	}

	public AbstractLogFileWatchRunnerZZZ(File objLogFile) throws ExceptionZZZ {
		super();	
		LogFileWatchRunnerNew_(objLogFile, null, null);
	}
	
	public AbstractLogFileWatchRunnerZZZ(File objLogFile, String sFilterSentence) throws ExceptionZZZ {
		super();	
		LogFileWatchRunnerNew_(objLogFile, sFilterSentence, null);
	}
	
	public AbstractLogFileWatchRunnerZZZ(File objLogFile, String sFilterSentence, String[] saFlag) throws ExceptionZZZ {
		super();	
		LogFileWatchRunnerNew_( objLogFile, sFilterSentence, saFlag);
	}
	
	private boolean LogFileWatchRunnerNew_(File objLogFile, String sFilterSentence, String[] saFlagControl) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{			
			if(saFlagControl != null){
				String stemp; boolean btemp;
				for(int iCount = 0;iCount<=saFlagControl.length-1;iCount++){
					stemp = saFlagControl[iCount];
					btemp = setFlag(stemp, true);
					if(btemp==false){ 								   
						   ExceptionZZZ ez = new ExceptionZZZ( stemp, IFlagZUserZZZ.iERROR_FLAG_UNAVAILABLE, this, ReflectCodeZZZ.getMethodCurrentName()); 						
						   throw ez;		 
					}
				}
				if(this.getFlag("init")) break main;
			}
			
			this.objLogFile = objLogFile;
			this.objModule = objModule;
			this.sLineFilter = sFilterSentence;
		}//end main:
		return bReturn;
	}

	//#### GETTER / SETTER
	@Override
	public File getLogFileWatched() {
		return this.objLogFile;
	}

	@Override
	public void setLogFileWatched(File objLogFile) {
		this.objLogFile = objLogFile;
	}
	
	@Override
	public String getLineFilter() {
		return this.sLineFilter;
	}
	
	@Override
	public void setLineFilter(String sLineFilter) {
		this.sLineFilter=sLineFilter;
	}
	
	@Override
	public boolean startCustom() throws ExceptionZZZ{
		boolean bReturn = false;
		main:{	
			
			//Lies die Datei Zeile für Zeile aus.
			bReturn = this.startServerProcessLogWatcher_();

		}//end main:
		return bReturn;
	}
	
	
	/** Das klappt... man kann das LogFile auslesen,
	 *  welches immer weiter neu vom OVPN-Server gefüllt wird.
	 * @return
	 * @author Fritz Lindhauer, 10.12.2023, 16:04:55
	 */
	private boolean startServerProcessLogWatcher_() throws ExceptionZZZ{
		boolean bReturn= false;
		main:{	
			String sLog = ReflectCodeZZZ.getPositionCurrent() + " LogFileWatchRunner started.";
			this.logLineDate(sLog);
			
			BufferedReader br=null;
			try {
				File objFileLog = this.getLogFileWatched();
				
				//Warte auf die Existenz der Datei.
				boolean bExists = false;
				do {
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP)) { //Merke: Das ist eine Anweisung und kein Status. Darum bleibt es beim Flag.
						sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+") hat Flag gesetzt ('" + IProgramRunnableZZZ.FLAGZ.REQUEST_STOP .name() + "'. Breche ab.";
						this.logProtocolString(sLog);
						break main;
					}
					bExists = FileEasyZZZ.exists(objFileLog);
					if(!bExists) {
						sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+") wartet auf Existenz der Datei  ('"+ objFileLog.getAbsolutePath() +"') ...";
						this.logProtocolString(sLog);
						Thread.sleep(5000);
					}else {
						sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+"): Datei existiert ('"+ objFileLog.getAbsolutePath() +"')";
						this.logProtocolString(sLog);
						Thread.sleep(5000);
					}
				}while(!bExists);
				
				String sLineFilter = this.getLineFilter();
				if(StringZZZ.isEmpty(sLineFilter)) {
					ExceptionZZZ ez = new ExceptionZZZ("ObjectWithStatusRunnable ("+this.getClass().getName()+"): Keine Zeilenfilter gesetzt.", this.iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				InputStream objStream = new FileInputStream(objFileLog);
				BufferedReader in = new BufferedReader(new InputStreamReader(objStream));
				if(!in.ready()){
					ExceptionZZZ ez = new ExceptionZZZ("BufferdReader-Object not ready", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				//Merke: Darin ist ene Endlosschleife
				int icount=0;
				String sLine;
				//for ( String sLine; (sLine = in.readLine()) != null; ){
				do {
					boolean bHasError = this.getStatusLocal(LogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR);
					if(bHasError) break;//das wäre dann ein von mir selbst erzeugter Fehler, der nicht im STDERR auftaucht.
					
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP)) { //Merke: Das ist eine Anweisung und kein Status. Darum bleibt es beim Flag.
						sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+") hat Flag gesetzt '" + IProgramRunnableZZZ.FLAGZ.REQUEST_STOP .name() + "'. Breche ab.";
						this.logProtocolString(sLog);
						break;
					}
					
					icount++;
					sLine = in.readLine();
					if(!StringZZZ.isEmpty(sLine)) {
	               		this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASOUTPUT, true);
	               	}
										
					boolean bFilterFound = this.writeOutputToLogPLUSanalyse(icount, sLine, sLineFilter);		//Man muss wohl erst den InputStream abgreifen, damit der Process weiterlaufen kann.
					if(bFilterFound) {																								
						sLog = ReflectCodeZZZ.getPositionCurrent() + "Filter '" + sLineFilter + "' wurde gefunden in Zeile " + icount;
						this.logProtocolString(sLog);
						
						//... ein Event soll auch beim Setzen des passenden Flags erzeugt und geworfen werden.
		        		this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND,true);
						sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+") Status '" + ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND.name() + "' gesetzt.";
						this.logProtocolString(sLog);
						
						//... ggfs. sofortiges Abbrechen nach dem Finden eines Filters						
						//if(this.getFlag(IWatchRunnerZZZ.FLAGZ.END_ON_FILTER_FOUND)) { //Merke: Es wird ein Status gesetzt und dann ein Event geworfen (auch an andere registrierte Objekte) und dann abgebrochen.
						
						//Hier wird sofort abgebrochen. Es wird also nicht auf das Setzen von REQUEST_STOP per Event gewartet.
						//Das kann z.B. bei dem "Direkten" Test auch nicht erfolgen.
						if(this.getFlag(IWatchListenerZZZ.FLAGZ.IMMIDIATE_END_ON_FILTER_FOUND)|
						   this.getFlag(IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND)){
							sLog = "Filter gefunden... Gemaess Flag '" + IWatchListenerZZZ.FLAGZ.IMMIDIATE_END_ON_FILTER_FOUND.name() +"', beende per Flag aber ohne auf den Event zu warten '" +IProgramRunnableZZZ.FLAGZ.REQUEST_STOP.name() + "'";
							this.logProtocolString(sLog);
							this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, true);
						}
					
						Thread.sleep(100);
					}else{
						//Entsprechend hier wieder den Filter verloren
						//Einen Extra Event sollte man hier nicht erzeugen muessen. Den Status zu setzen sollte bereits einen entsprechenden Status Event abfeuern.
						this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, false);	
						
					}//end bFilterFound	
					
					Thread.sleep(100);
				}while(true);
				this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED,true);
				sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+") ended.";
				this.logProtocolString(sLog);
				              	
                bReturn = true;
                  
			} catch (InterruptedException e) {				
				e.printStackTrace();
				this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR,true);
				sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+") HASERROR Status gesetzt.";
				this.logProtocolString(sLog);
				
				ExceptionZZZ ez = new ExceptionZZZ("InterruptedException happend: '"+ e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR,true);
				sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+") HASERROR Status gesetzt.";
				this.logProtocolString(sLog);
				
				ExceptionZZZ ez = new ExceptionZZZ("FileNotFoundException happend: '"+ e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			} catch (IOException e) {
				e.printStackTrace();	
				this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR,true);
				sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatusRunnable ("+this.getClass().getName()+") HASERROR Status gesetzt.";
				this.logProtocolString(sLog);
				
				ExceptionZZZ ez = new ExceptionZZZ("IOException happend: '" + e.getMessage() + "'", iERROR_RUNTIME, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			} finally {
				if(br!=null) {
					IOUtils.closeQuietly(br);
				}
	        }
		}//end main:
		return bReturn;
	}
	
	//##############################
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
                sLog = ReflectCodeZZZ.getPositionCurrent() + "Gelesen aus InputStream - " + iLineCounter +"\t: '" + sLine + "'";
                this.logProtocolString(sLog);
               		
				bReturn = this.analyseInputLineCustom(sLine, sLineFilter);												
		}//END main:
		return bReturn;
	}
	
	//Merke: Die eine ggfs. von diesem Einfachsten Fall abweichende, genauere Analyse muss ggfs. im konkreten LogFile Watch Runner gemacht werden.
	@Override
	public boolean analyseInputLineCustom(String sLine, String sLineFilter) throws ExceptionZZZ {
		boolean bReturn = false; //true, wenn der Filter gefunden wurde
		main:{
			if(StringZZZ.isEmpty(sLine)) break main;
			if(StringZZZ.isEmpty(sLineFilter)) break main;
			
			String sLog;
			if(StringZZZ.contains(sLine, sLineFilter)) {
        		sLog = ReflectCodeZZZ.getPositionCurrent() + " " +"\t: ObjectWithStatusRunnable ("+this.getClass().getName()+") hat Zeilenfilter gefunden: '" + sLineFilter + "'";
        		this.logProtocolString(sLog);
        		
        		bReturn = true;
			}       			
		}//end main:
		return bReturn;
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
	//### FLAG aus: IWatchListenerZZZ
	//###############################
	@Override
	public boolean getFlag(IWatchListenerZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(IWatchListenerZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(IWatchListenerZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
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
	public boolean proofFlagSetBefore(IWatchListenerZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}
	
	//###############################
	//### FLAG HANDLING aus: ILogFileWatchRunnerZZZ
	//###############################
	@Override
	public boolean getFlag(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(ILogFileWatchRunnerZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}
	
	
	//###############################
	//### FLAG aus: IStatusLocalMessageUserZZZ
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



	//###########################################################
	//### STATUS
	//###########################################################
	
	//####### aus IStatusLocalUserZZZ
	/* (non-Javadoc)
	 * @see basic.zKernel.status.IStatusLocalUserZZZ#getStatusLocal(java.lang.Enum)
	 */
	@Override
	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(objEnumStatusIn==null) {
				break main;
			}
			
			AbstractLogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) objEnumStatusIn;
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
}
