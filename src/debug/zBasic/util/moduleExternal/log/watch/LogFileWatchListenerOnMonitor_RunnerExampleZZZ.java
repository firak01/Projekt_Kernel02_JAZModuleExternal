package debug.zBasic.util.moduleExternal.log.watch;

import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramWithFlagOnStatusListeningRunnableZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchMonitorStatusLocalZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import debug.zBasic.util.moduleExternal.log.create.ILogFileCreateRunnerOnMonitorListeningZZZ;

/**Diese Klasse läuft einfach und ist an einem Monitor registriert.
 * Wirft der Monitor Events, reagiert sie darauf.
 *  
 * @author fl86kyvo
 *
 */
public class LogFileWatchListenerOnMonitor_RunnerExampleZZZ extends AbstractProgramWithFlagOnStatusListeningRunnableZZZ implements ILogFileWatchOnMonitorListenerRunnerExampleZZZ {
	private static final long serialVersionUID = 6586079955658760005L;
		
	public LogFileWatchListenerOnMonitor_RunnerExampleZZZ() throws ExceptionZZZ {
		super();		
	}
	
	public LogFileWatchListenerOnMonitor_RunnerExampleZZZ(String[] saFlag) throws ExceptionZZZ {
		super();	
		LogFileWatchOnMonitorListenerRunnerExampleNew_(saFlag);
	}
		
	
	private boolean LogFileWatchOnMonitorListenerRunnerExampleNew_(String[] saFlagControl) throws ExceptionZZZ {
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
	public boolean startCustom() throws ExceptionZZZ{
		boolean bReturn = false;
		main:{	
			
			//Einfach eine Endlosschleife und ggfs. mit einem Status abschliessen.
			bReturn = this.startLogFileOnMonitorListenerRunner();

		}//end main:
		return bReturn;
	}
	
	
	/** Einfach nur laufen und auf das im "Empfangen eines Events" gesetzte Flag auswerten.
	 * @return
	 * @author Fritz Lindhauer, 10.12.2023, 16:04:55
	 */
	public boolean startLogFileOnMonitorListenerRunner() throws ExceptionZZZ{
		boolean bReturn= false;
		main:{			
			try {
				
				//Warte auf ein Flag aufzuhoeren.
				long lcounter = 0;
				do {
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
						break main;
					}

					lcounter++;
					
					System.out.println(ReflectCodeZZZ.getPositionCurrent() +": Running waiting for Requeststop-Flag. (" + lcounter +")");
					Thread.sleep(5000);
					
				}while(true);
				
			}catch (InterruptedException e) {					
				try {
					String sLogIE = e.getMessage();
					this.logProtocolString("An error happend: '" + sLogIE + "'");
				} catch (ExceptionZZZ e1) {
					System.out.println(e1.getDetailAllLast());
					e1.printStackTrace();
				}
				System.out.println(e.getMessage());
				e.printStackTrace();
			} finally {
				
	        }
		}//end main:
		return bReturn;
	}
	
		//Methode wird in der ReactionHashMap angegeben....
		public boolean doStop(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				if(eventStatusLocal==null) break main;
				
				String sLog = ReflectCodeZZZ.getPositionCurrent() + "EventMessage: " + eventStatusLocal.getStatusMessage();
				this.logProtocolString(sLog);
				
				bReturn = this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);
			}//end main
			return bReturn;
		}
		
		//Methode wird in der ReactionHashMap angegeben....
		public boolean doFilterFound(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				if(eventStatusLocal==null) break main;
				
				String sLog = ReflectCodeZZZ.getPositionCurrent() + "EventMessage: " + eventStatusLocal.getStatusMessage();
				this.logProtocolString(sLog);
				
				if(this.getFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ.END_ON_FILTERFOUND)) {
					bReturn = this.doStop(eventStatusLocal);
				}
			}//end main
			return bReturn;
		}
		
	//###############################
	//### FLAG HANDLING
	//###############################
	@Override
	public boolean getFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	//### aus IListenerObjectStatusLocalMessageReactZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		String sLog=null;
		
		main:{
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Einen Event von einem Objekt, an dem registriert worden ist empfangen.";
			this.logProtocolString(sLog);
			
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Event="+eventStatusLocal.toString();
			this.logProtocolString(sLog);
			
			boolean bEventRelevant = this.isEventRelevant(eventStatusLocal);
			if(!bEventRelevant) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Event ist NICHT relevant.";
				this.logProtocolString(sLog);
				break main;
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Event ist relevant.";
				this.logProtocolString(sLog);
			}
			
			String sAction = (String) this.getHashMapStatusLocalReaction().get(eventStatusLocal.getStatusLocal());
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Gefundenen Action: '" + sAction + "'";
			this.logProtocolString(sLog);
			
			//TODO Idee: Per Reflection API die so genannte Methode aufrufen... aber dann sollte das Event-Objekt als Parameter mit uebergeben werden.			
			if(!StringZZZ.isEmpty(sAction)) {
				switch(sAction) {
				case "doStop":
					bReturn = doStop(eventStatusLocal);	
					break;
				case "doFilterFound":
					bReturn = doFilterFound(eventStatusLocal);	
					break;
				default:
					sLog = ReflectCodeZZZ.getPositionCurrent() + "ActionAlias wird noch nicht behandelt. '" + sAction + "'";
					this.logProtocolString(sLog);
				}
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Kein ActionAlias ermittelt. Fuehre keine Aktion aus.";
				this.logProtocolString(sLog);
			}
		
		}//end main:
		return bReturn;	
	}

	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	@Override
	public HashMap<IEnumSetMappedStatusZZZ, String> createHashMapStatusLocalReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//Reagiere nur auf den "Filter" gefunden Event
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERFILTERFOUND, "filterFound");
		
		//und den "Monitor beendet" Event, bzw. Fehler
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTOPPED, "doStop");
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASERROR, "doStop");
		
		
		return hmReturn;
	}

	
}
