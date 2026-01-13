package debug.zBasic.util.moduleExternal.log.watch;

import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramWithFlagOnStatusListeningRunnableZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ;
import basic.zKernel.flag.IFlagZEnabledZZZ;
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
		super(saFlag);	
		LogFileWatchOnMonitorListenerRunnerExampleNew_();
	}
		
	
	private boolean LogFileWatchOnMonitorListenerRunnerExampleNew_() throws ExceptionZZZ {
		boolean bReturn = false;
		main:{			
			if(this.getFlag("init")) break main;
			
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	

	//#### GETTER / SETTER
		
	//### INTERFACES ###################
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
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP)) {
						System.out.println(ReflectCodeZZZ.getPositionCurrent() +": Stop requested by Flag at LoopCounter=" + lcounter);						
						break main;
					}

					lcounter++;
					
					System.out.println(ReflectCodeZZZ.getPositionCurrent() +": Running waiting for Request_stop-Flag. LoopCountr=" + lcounter);
					Thread.sleep(5000);
					
				}while(true);
				
			}catch (InterruptedException e) {					
				try {
					String sLogIE = e.getMessage();
					this.logProtocol("An error happend: '" + sLogIE + "'");
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
	public boolean doStop(IEnumSetMappedZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			
			String sLog = ReflectCodeZZZ.getPositionCurrent() + "Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
			this.logProtocol(sLog);
			
			bReturn = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP);
			if(bReturn) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName()+ "=> STOP FLAG SCHON GESETZT. Breche ab. Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
				this.logProtocol(sLog);
				break main;
			}
			
			bReturn = this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, bStatusValue);
		}//end main
		return bReturn;
	}
	
	//Methode wird in der ReactionHashMap angegeben....
		public boolean doFilterFound(IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				
				String sLog = ReflectCodeZZZ.getPositionCurrent() + "Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
				this.logProtocol(sLog);
				
				bReturn = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP);
				if(bReturn) {
					sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName()+ "=> STOP FLAG SCHON GESETZT. Breche ab. Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
					this.logProtocol(sLog);
					break main;
				}
				
				if(bStatusValue) {//nur im true Fall
					if(this.getFlag(IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND)){
					   if(this.getFlag(IWatchListenerZZZ.FLAGZ.IMMEDIATE_END_ON_FILTER_FOUND)) {
						   System.exit(1);
					   }else {
						   bReturn = this.doStop(enumStatus,bStatusValue,sStatusMessage);
					   }												
					}
				}						
			}//end main
			return bReturn;
		}
		
	//###############################
	//### FLAG AUS: ILogFileWatchOnMonitorListenerRunnerExampleZZZ
	//###############################
	@Override
	public boolean getFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
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

	
	//####################################################
	//### FLAG aus IWatchListenerZZZ
	//####################################################
	@Override
	public boolean getFlag(IWatchListenerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
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
	public boolean proofFlagSetBefore(IWatchListenerZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	
	//##############################################
	//### Aus IListenerObjectStatusLocalZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	//##############################################
		
	@Override
	public boolean isEventRelevant2ChangeStatusLocalByClass(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevant2ChangeStatusLocalByStatusLocalValue(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	@Override
	public HashMap<IEnumSetMappedStatusZZZ, String> createHashMapStatusLocal4ReactionCustom_String() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//Reagiere nur auf den "Filter" gefunden Event
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERFILTERFOUND, "doFilterFound");
		
		//und den "Monitor beendet" Event, bzw. Fehler
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTOPPED, "doStop");
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASERROR, "doStop");
		
		
		return hmReturn;
	}
	
	@Override
	public HashMap createHashMapStatusLocal4ReactionCustom_Enum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap createHashMapStatusLocal4ReactionCustom_EnumStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reactOnStatusLocal4ActionCustom(String sAction, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ{
			boolean bReturn = false;
			main:{
				if(!bStatusValue)break main;
				
				String sLog;
			
			
				//TODO Idee: Per Reflection API die so genannte Methode aufrufen... aber dann sollte das Event-Objekt als Parameter mit uebergeben werden.
				if(!StringZZZ.isEmpty(sAction)) {
					switch(sAction) {
					case "doStop":
						bReturn = doStop(enumStatus, bStatusValue, sStatusMessage);	
						break;	
					case "doFilterFound":
						bReturn = doFilterFound(enumStatus, bStatusValue, sStatusMessage);	
						break;	
					default:
						sLog = ReflectCodeZZZ.getPositionCurrent() + "ActionAlias wird noch nicht behandelt. '" + sAction + "'";
						this.logProtocol(sLog);
					}
				}else {
					sLog = ReflectCodeZZZ.getPositionCurrent() + "Kein ActionAlias ermittelt. Fuehre keine Aktion aus.";
					this.logProtocol(sLog);
				}
		
		}//end main:
		return bReturn;		
	}

	@Override
	public boolean queryReactOnStatusLocalEventCustom(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean queryReactOnStatusLocal4ActionCustom(String sActionAlias, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		return true;
	}
}
