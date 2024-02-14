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
import basic.zBasic.component.AbstractProgramRunnableWithStatusMessageListeningZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.EventObject4LogFileWatchRunnerStatusLocalZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchRunnerStatusLocalZZZ;
import basic.zKernel.status.IListenerObjectStatusBasicZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalMessageZZZ;
import basic.zKernel.status.KernelSenderObjectStatusLocalMessageZZZ;

public abstract class AbstractLogFileWatchRunnerZZZ extends AbstractProgramRunnableWithStatusMessageListeningZZZ implements ILogFileWatchRunnerZZZ{
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
	public boolean start() throws ExceptionZZZ{
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
                    		
                    		IEventObject4LogFileWatchRunnerStatusLocalZZZ event = new EventObject4LogFileWatchRunnerStatusLocalZZZ(this,ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, true);			                			
                			this.getSenderStatusLocalUsed().fireEvent(event);
                			
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();	
//			catch (InterruptedException e) {					
//					try {
//						String sLog = e.getMessage();
//						this.logLineDate("An error happend: '" + sLog + "'");
//					} catch (ExceptionZZZ e1) {
//						System.out.println(e1.getDetailAllLast());
//						e1.printStackTrace();
//					}
//					System.out.println(e.getMessage());
//					e.printStackTrace();
//				}
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
	public ISenderObjectStatusLocalMessageZZZ getSenderStatusLocalUsed() throws ExceptionZZZ {
		if(this.objEventStatusLocalBroker==null) {
			//Nun geht es darum den Sender fuer Aenderungen an den Flags zu erstellen, der dann registrierte Objekte ueber Aenderung von Flags informiert
			ISenderObjectStatusLocalMessageZZZ objSenderStatusLocal = new KernelSenderObjectStatusLocalMessageZZZ();			
			this.objEventStatusLocalBroker = objSenderStatusLocal;
		}		
		return this.objEventStatusLocalBroker;
	}

	@Override
	public void setSenderStatusLocalUsed(ISenderObjectStatusLocalMessageZZZ objEventSender) {
		this.objEventStatusLocalBroker = objEventSender;
	}

	@Override
	public void registerForStatusLocalEvent(IListenerObjectStatusLocalZZZ objEventListener) throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().addListenerObject(objEventListener);
	}

	@Override
	public void unregisterForStatusLocalEvent(IListenerObjectStatusLocalZZZ objEventListener) throws ExceptionZZZ {
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

	@Override
	public IEnumSetMappedStatusZZZ getStatusLocalEnumPrevious(int iIndexStepsBack) {
		// TODO Auto-generated method stub
		return null;
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
}
