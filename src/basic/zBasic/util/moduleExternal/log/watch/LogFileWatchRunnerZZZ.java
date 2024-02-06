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
import basic.zBasic.component.AbstractProgramRunnableWithStatusMessageZZZ;
import basic.zBasic.component.IModuleZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.EventObject4LogFileWatchRunnerStatusLocalSetZZZ;
import basic.zKernel.status.IEventBrokerStatusLocalMessageSetUserZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchRunnerStatusLocalSetZZZ;
import basic.zKernel.status.IEventObjectStatusBasicZZZ;
import basic.zKernel.status.IEventObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.IEventObjectStatusLocalSetZZZ;
import basic.zKernel.status.IListenerObjectStatusBasicZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.ISenderObjectStatusBasicZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.KernelSenderObjectStatusLocalMessageSetZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerZZZ;

public class LogFileWatchRunnerZZZ extends AbstractLogFileWatchRunnerZZZ{
	private static final long serialVersionUID = 6586079955658760005L;
		
	//analog zu... ProcessWatchRunnerOVPN
	//Merke: der LogFileWatchRunner selbst wird dann irgendwann mal für die Server-Version im ProcessWatchRunnerOVPN genutzt werden.
	//       Momentan wird dort nur das Log der "StarterBatch" ausgwertet. Auch hinsichtlich von "hasOutput". Das ist aber nicht korrekt.
	
	//TODOGOON20240114: Baue zuerst die Status-Struktur fuer diese Klasse auf.
	//private ISenderObjectStatusLocalMessageSetZZZ objEventStatusLocalBroker=null;//Das Broker Objekt, an dem sich andere Objekte regristrieren können, um ueber Aenderung eines StatusLocal per Event informiert zu werden.
	// ISenderObjectStatusBasicZZZ objEventStatusLocalBroker=null;//Das Broker Objekt, an dem sich andere Objekte regristrieren können, um ueber Aenderung eines StatusLocal per Event informiert zu werden.
		
	public LogFileWatchRunnerZZZ() throws ExceptionZZZ {
		super();		
	}

	public LogFileWatchRunnerZZZ(File objLogFile) throws ExceptionZZZ {
		super(objLogFile);	
		LogFileWatchRunnerNew_(null);
	}
	
	public LogFileWatchRunnerZZZ(File objLogFile, String sFilterSentence) throws ExceptionZZZ {
		super(objLogFile, sFilterSentence);	
		LogFileWatchRunnerNew_(null);
	}
	
	public LogFileWatchRunnerZZZ(File objLogFile, String sFilterSentence, String[] saFlag) throws ExceptionZZZ {
		super(objLogFile, sFilterSentence);	
		LogFileWatchRunnerNew_(saFlag);
	}
	
	private boolean LogFileWatchRunnerNew_(String[] saFlagControl) throws ExceptionZZZ {
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
	public boolean start() throws ExceptionZZZ, InterruptedException{
		boolean bReturn = false;
		main:{	
			
			//Lies die Datei Zeile für Zeile aus.
			bReturn = this.startServerProcessLogWatcher();

		}//end main:
		return bReturn;
	}
	
	
	/** Das klappt... man kann das LogFile auslesen,
	 *  welches immer weiter neu vom OVPN-Server gefüllt wird.
	 * @return
	 * @author Fritz Lindhauer, 10.12.2023, 16:04:55
	 */
	public boolean startServerProcessLogWatcher() throws ExceptionZZZ{
		boolean bReturn= false;
		main:{	
			String sLog = ReflectCodeZZZ.getPositionCurrent() + " LogFileWatchRunner started.";
			System.out.println(sLog);
			this.logLineDate(sLog);
			
			BufferedReader br=null;
			try {
				File objFileLog = this.getLogFileWatched();
				
				//Warte auf die Existenz der Datei.
				boolean bExists = false;
				do {
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) { //Merke: Das ist eine Anweisung und kein Status. Darum bleibt es beim Flag.
						break main;
					}
					bExists = FileEasyZZZ.exists(objFileLog);
					if(!bExists) {
						Thread.sleep(5000);
					}
				}while(!bExists);
				
				String sLineFilter = this.getLineFilter();
				if(StringZZZ.isEmpty(sLineFilter)) {
					ExceptionZZZ ez = new ExceptionZZZ("Keine Zeilenfilter gesetzt.", this.iERROR_PROPERTY_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				String sLine = null;
				InputStream objStream = new FileInputStream(objFileLog);
				br = new BufferedReader(new InputStreamReader(objStream));
				
				int icount=0;
                while (true){
                	Thread.sleep(100); //Bremse zum Debuggen ab. Sonst gehen mir die Zeilen aus... ;-))
                	
                	boolean bStopRequested = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP);//Merke: STOPREQUEST ist eine Anweisung.. bleibt also ein Flag und ist kein Status
					if( bStopRequested) {
						sLog = ReflectCodeZZZ.getPositionCurrent() + ": Breche Schleife ab.";
						this.logLineDate(sLog);
						break main;
					}
					
                    sLine = br.readLine();
                    if(sLine!=null)
                    {
                    	icount++;
                    	System.out.println(ReflectCodeZZZ.getPositionCurrent() + ": " + icount +"\t: " + sLine);	                    	
                    	if(StringZZZ.contains(sLine, sLineFilter)) {
                    		System.out.println(ReflectCodeZZZ.getPositionCurrent() + ": " + icount +"\t: Zeilenfilter gefunden: '" + sLineFilter + "'");
                    		
                    		IEventObject4LogFileWatchRunnerStatusLocalSetZZZ event = new EventObject4LogFileWatchRunnerStatusLocalSetZZZ(this,1,ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, true);			                			
                			this.getSenderStatusLocalUsed().fireEvent(event);
                			
                			//Das Anhalten wird dann im gemeinsamen Listener gemacht, an dem der WatchRunner registriert ist.
                			//bzw. in der entsprechenden reaktions-Methode in dieser Klasse.....
                			
                			if(this.getFlag(ILogFileWatchRunnerZZZ.FLAGZ.END_ON_FILTERFOUND)) {
                				sLog = ReflectCodeZZZ.getPositionCurrent() + ": Filter gefunden und END_ON_FILTERFOUND gesetzt. Beende Schleife.";
        						this.logLineDate(sLog);
        						break;
                			}
                    	}
                    }else{
                    	//Warte auf weiter Ausgaben
                        Thread.sleep(100);
                    }
                    
                    this.logLineDate("");
                    Thread.sleep(20);													
					boolean bError = this.getStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR);
					if(bError) break;
		
					//Nach irgendeiner Ausgabe enden ist hier falsch, in einer abstrakten Klasse vielleicht richtig, quasi als Muster.
					//if(this.getFlag("hasOutput")) break;
					//System.out.println("FGLTEST03");					
				}//end while
					
				this.setStatusLocal(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED,true);
				this.logLineDate(ReflectCodeZZZ.getPositionCurrent() + ": LogFileWatchRunner ended.");
				
              	
                bReturn = true;
			} catch (InterruptedException e) {				
				e.printStackTrace();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();								
			} finally {
				if(br!=null) {
					IOUtils.closeQuietly(br);
				}
	        }
		}//end main:
		return bReturn;
	}
		
	//###############################
	//### FLAG HANDLING
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

	//#########################################################
	//### aus IEventBrokerStatusLocalSetUserZZZ
	@Override
	public ISenderObjectStatusLocalMessageSetZZZ getSenderStatusLocalUsed() throws ExceptionZZZ {
	//public ISenderObjectStatusBasicZZZ getSenderStatusLocalUsed() throws ExceptionZZZ {
		if(this.objEventStatusLocalBroker==null) {
			//++++++++++++++++++++++++++++++
			//Nun geht es darum den Sender fuer Aenderungen an den Flags zu erstellen, der dann registrierte Objekte ueber Aenderung von Flags informiert
			ISenderObjectStatusLocalMessageSetZZZ objSenderStatusLocal = new KernelSenderObjectStatusLocalMessageSetZZZ();			
			this.objEventStatusLocalBroker = objSenderStatusLocal;
		}		
		//return (ISenderObjectStatusLocalMessageSetZZZ) this.objEventStatusLocalBroker;
		return this.objEventStatusLocalBroker;
	}

	@Override
	public void setSenderStatusLocalUsed(ISenderObjectStatusLocalMessageSetZZZ objEventSender) {
	//public void setSenderStatusLocalUsed(ISenderObjectStatusBasicZZZ objEventSender) {
		//this.objEventStatusLocalBroker = (ISenderObjectStatusLocalMessageSetZZZ) objEventSender;
		this.objEventStatusLocalBroker = objEventSender;
	}
	
	
	@Override
	//public void registerForStatusLocalEvent(IListenerObjectStatusLocalMessageSetZZZ objEventListener) throws ExceptionZZZ {
	public void registerForStatusLocalEvent(IListenerObjectStatusBasicZZZ objEventListener) throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().addListenerObject(objEventListener);
	}

	@Override
	//public void unregisterForStatusLocalEvent(IListenerObjectStatusLocalMessageSetZZZ objEventListener) throws ExceptionZZZ {
	public void unregisterForStatusLocalEvent(IListenerObjectStatusBasicZZZ objEventListener) throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().removeListenerObject(objEventListener);
	}
	
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
			
			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) objEnumStatusIn;
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

	@Override
	public boolean offerStatusLocal(int iIndexOfProcess, Enum enumStatusIn, String sStatusMessage, boolean bStatusValue)
			throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offerStatusLocal(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue)
			throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setStatusLocal(Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setStatusLocal(int iIndexOfProcess, Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setStatusLocal(int iIndexOfProcess, Enum enumStatusIn, String sMessage, boolean bStatusValue)
			throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusMapped, boolean bStatusValue)
			throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusMapped, String sMessage, boolean bStatusValue)
			throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setStatusLocalEnum(int iIndexOfProcess, IEnumSetMappedStatusZZZ enumStatusMapped,
			boolean bStatusValue) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setStatusLocalEnum(int iIndexOfProcess, IEnumSetMappedStatusZZZ enumStatusMapped, String sMessage,
			boolean bStatusValue) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}	
	
	//#######################################
	/* (non-Javadoc)
	 * @see basic.zBasic.AbstractObjectWithStatusZZZ#isStatusLocalRelevant(basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ)
	 */
	@Override
	public boolean isStatusLocalRelevant(IEnumSetMappedStatusZZZ objEnumStatusIn) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(objEnumStatusIn==null) break main;
				
			//Fuer das Main-Objekt ist erst einmal jeder Status relevant
			bReturn = true;
		}//end main:
		return bReturn;
	}

	//### Aus IListenerObjectStatusBasicZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusBasicZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		String sLog=null;
		
		main:{
			sLog = ReflectCodeZZZ.getPositionCurrent() + ": Filter gefunden und mache den changeStatusLocal Event.";
			this.logProtocolString(sLog);
			
			if(eventStatusLocal instanceof IEventObjectStatusLocalMessageSetZZZ) {// .getClass().getSimpleName().equals("LogFileCreateMockRunnerZZZ")) {
				IEventObjectStatusLocalMessageSetZZZ event = (IEventObjectStatusLocalMessageSetZZZ) eventStatusLocal;
				boolean bStatusValue = event.getStatusValue();
				if(bStatusValue!=true) break main;
				
				
			}
			
			if(this.getFlag(ILogFileWatchRunnerZZZ.FLAGZ.END_ON_FILTERFOUND)) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + ": Filter gefunden und END_ON_FILTERFOUND gesetzt. Beende Schleife.";
				this.logProtocolString(sLog);
				
				this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);								
			}
			
			bReturn = true;
		}//end main:
		return bReturn;		
	}

	@Override
	public boolean isEventRelevant(IEventObjectStatusBasicZZZ eventStatusBasic) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			
			if(!this.isEventRelevant2ChangeStatusLocal(eventStatusBasic)) break main;
			if(!this.isEventRelevantByClass2ChangeStatusLocal(eventStatusBasic)) break main;
			if(!this.isEventRelevantByStatusLocal2ChangeStatusLocal(eventStatusBasic)) break main;
			if(!this.isEventRelevantByStatusLocalValue2ChangeStatusLocal(eventStatusBasic)) break main;
			
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	@Override
	public boolean isStatusLocalDifferent(String sStatusString, boolean bStatusValue) throws ExceptionZZZ {
		return true;
	}
	
	@Override
	public boolean isEventRelevant2ChangeStatusLocal(IEventObjectStatusBasicZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusBasicZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocal2ChangeStatusLocal(IEventObjectStatusBasicZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusBasicZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	
}
