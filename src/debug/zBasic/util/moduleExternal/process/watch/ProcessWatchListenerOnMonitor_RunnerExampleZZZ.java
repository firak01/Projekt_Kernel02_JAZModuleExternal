package debug.zBasic.util.moduleExternal.process.watch;

import java.util.HashMap;

import basic.zBasic.AbstractObjectWithFlagOnStatusListeningZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramWithFlagOnStatusListeningRunnableZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ;
import basic.zBasic.util.moduleExternal.monitor.IProcessWatchMonitorZZZ;
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ.STATUSLOCAL;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import debug.zBasic.util.moduleExternal.log.watch.ILogFileWatchOnMonitorListenerRunnerExampleZZZ;

/** Ein Beispiel-Broker, an dem sich die "hoerenden" Klassen registieren.
 * @author fl86kyvo
 *
 */
public class ProcessWatchListenerOnMonitor_RunnerExampleZZZ extends AbstractProgramWithFlagOnStatusListeningRunnableZZZ implements IProcessWatchOnMonitorListenerRunnerExampleZZZ {
	private static final long serialVersionUID = -2338056174362726426L;

	public ProcessWatchListenerOnMonitor_RunnerExampleZZZ() throws ExceptionZZZ {
		super();
	}
	
	public ProcessWatchListenerOnMonitor_RunnerExampleZZZ(String[] saFlag) throws ExceptionZZZ {
		super(saFlag);
		ProcessWatchOnMonitorListenerRunnerExampleNew_();
	}
	
	private boolean ProcessWatchOnMonitorListenerRunnerExampleNew_() throws ExceptionZZZ {
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
			bReturn = this.startProcessOnMonitorListenerRunner();

		}//end main:
		return bReturn;
	}
	
	/** Einfach nur laufen und auf das im "Empfangen eines Events" gesetzte Flag auswerten.
	 * @return
	 * @author Fritz Lindhauer, 10.12.2023, 16:04:55
	 */
	public boolean startProcessOnMonitorListenerRunner() throws ExceptionZZZ{
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
	public boolean doStop(IEnumSetMappedZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			
			String sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
			this.logProtocolString(sLog);
			
			bReturn = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP);
			if(bReturn) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName()+ "=> STOP FLAG SCHON GESETZT. Breche ab. Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
				this.logProtocolString(sLog);
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
				
				String sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName() + "=> Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
				this.logProtocolString(sLog);
				
				bReturn = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP);
				if(bReturn) {
					sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName()+ "=> STOP FLAG SCHON GESETZT. Breche ab. Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
					this.logProtocolString(sLog);
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
	//### FLAG AUS: IProcessWatchOnMonitorListenerRunnerExampleZZZ
	//###############################
	@Override
	public boolean getFlag(IProcessWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(IProcessWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(IProcessWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IProcessWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(IProcessWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(IProcessWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	//####################################################
	//### FLAG aus IWatchListenerZZZ
	//####################################################
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
	public HashMap<IEnumSetMappedStatusZZZ, String> createHashMapStatusLocal4ReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//Reagiere nur auf den "Filter" gefunden Event
		hmReturn.put(IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERFILTERFOUND, "doFilterFound");
		
		//und den "Monitor beendet" Event, bzw. Fehler
		hmReturn.put(IProcessWatchMonitorZZZ.STATUSLOCAL.ISSTOPPED, "doStop");
		hmReturn.put(IProcessWatchMonitorZZZ.STATUSLOCAL.HASERROR, "doStop");
		
		
		return hmReturn;
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
	public boolean queryReactOnStatusLocalEventCustom(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean queryReactOnStatusLocal4ActionCustom(String sActionAlias, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		return true;
	}

//#############################
	
	
//	@Override
//	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
//		boolean bReturn=false;
//		main:{	
//			if(eventStatusLocal==null)break main;
//						
//			String sLog = ReflectCodeZZZ.getPositionCurrent()+": Fuer ProcessWatchEvent.";
//			this.logLineDate(sLog);
//			
//			if(eventStatusLocal instanceof IEventObjectStatusLocalZZZ) {
//				
//				boolean bRelevant = this.isEventRelevantAny((IEventObjectStatusLocalZZZ) eventStatusLocal); 
//				if(!bRelevant) {
//					sLog = 	ReflectCodeZZZ.getPositionCurrent() + ": Event / Status nicht relevant. Breche ab.";
//					this.logProtocolString(sLog);
//					break main;
//				}
//				
//				IEventObjectStatusLocalZZZ eventStatusLocalSet = (IEventObjectStatusLocalZZZ) eventStatusLocal;
//				IEnumSetMappedZZZ enumStatus = eventStatusLocalSet.getStatusLocal();				
//				
//				
//			
//				//Weiter Daten holen... im OVPN - Projekt z.B. die IPAdresse...
//			
////+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//			
//			//+++++++++++++++++++++
//			//Falls der empfangene Status in einen anderen, eigenen, lokalen Status uebersetzt werden muss.
////			HashMap<IEnumSetMappedStatusZZZ,IEnumSetMappedStatusZZZ>hmEnum = this.getHashMapEnumSetForCascadingStatusLocal();
////			IClientMainOVPN.STATUSLOCAL objEnum = (IClientMainOVPN.STATUSLOCAL) hmEnum.get(enumStatus);			
////			if(objEnum==null) {
////				sLog = ReflectCodeZZZ.getPositionCurrent()+": Keinen gemappten Status aus dem Event-Objekt erhalten. Breche ab";
////				System.out.println(sLog);
////				this.logLineDate(sLog);
////				break main;
////			}
//			
//			//Wenn wir den Status nicht weitersenden, sondern direkt verarbeiten ist solch ein Mapping nicht notwendig.
//			//Also da wir mit objEnum als Variable weiterarbeiten wollen:
//			//IProcessWatchRunnerZZZ.STATUSLOCAL objEnum = (STATUSLOCAL) enumStatus;
//			IWatchRunnerZZZ.STATUSLOCAL objEnum = (basic.zBasic.util.moduleExternal.IWatchRunnerZZZ.STATUSLOCAL) enumStatus;
//			//++++++++++++++++++++##
//			
//			boolean bValue = eventStatusLocalSet.getStatusValue();
//			
//			
//			//Merke: Ein Interface ILogFileWatchListenerExampleZZZ mit dem STATUSLOCAL ist nicht implementiert.
////			boolean bHasError = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASERROR)&& bValue;
////			boolean bEnded = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTOPPED) && bValue;
////			boolean bHasConnection = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTION) && bValue;
////			boolean bHasConnectionLost = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTIONLOST) && bValue;
//		
//			boolean bEventHasError = objEnum.equals(IWatchRunnerZZZ.STATUSLOCAL.HASERROR)&& bValue;
//			boolean bEventEnded = objEnum.equals(IWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED)&& bValue;
//			
//			//boolean bEventHasConnection = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTED);
//			//boolean bEventHasConnectionLost = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTINTERUPTED);
//			
//			
//			boolean bEventHasFilterFound = objEnum.equals(IWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND)&& bValue;
//				
//			//int iIndex = eventStatusLocalSet.getProcessID();
//			String sStatusMessage = eventStatusLocalSet.getStatusMessage();	
//			sLog = ReflectCodeZZZ.getPositionCurrent() + ": StatusMessage ist = '" + sStatusMessage + "'";
//			this.logProtocolString(sLog);
//			
//
//			//Einen Status Anzunehmen ist hier nicht implementiert			
////			boolean bStatusLocalSet = this.offerStatusLocal(iIndex, objEnum, sStatusMessage, bValue);//Es wird ein Event gefeuert, an dem das Tray-Objekt und andere registriert sind und dann sich passend einstellen kann.
////			if(!bStatusLocalSet) {
////				sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
////				System.out.println(sLog);
////				this.logLineDate(sLog);
////				break main;
////			}
//			//++++++++++++++
//			
//			//Die Stati vom Monitor-Objekt mit dem Backend-Objekt mappen
//			//if(IClientThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPRO==objStatusEnum) {
//			//	this.setStatusLocal(IClientMainOVPN.STATUSLOCAL.ISCONNECTING, eventStatusLocalSet.getStatusValue());				
//			//}else if(IClientThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISCONNECTED==objStatusEnum) {
//			
//			if(bEventHasError && bEventEnded){
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status bEventHasError && bEventEnded";
//				this.logLineDate(sLog);					
//			}else if((!bEventHasError) && bEventEnded){
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status !bEventHasError && bEventEnded";
//				this.logLineDate(sLog);
//				
//			}
//		
//			}else {
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": Event ist kein instanceof IEventObjectStatusLocalZZZ. Klasse: " + eventStatusLocal.getClass().getName();
//				this.logLineDate(sLog);
//			}//end if instanceof ...MessageSetZZZ
//			bReturn = true;
//		}//end main:
//		return bReturn;
//	}
	
}
