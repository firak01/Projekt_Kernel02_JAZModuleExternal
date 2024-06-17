package basic.zBasic.util.moduleExternal.log.watch;

import java.io.File;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchMonitorStatusLocalZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;

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
	
	public LogFileWatchRunnerZZZ(String[] saFlag) throws ExceptionZZZ {
		super(saFlag);	
	}

	public LogFileWatchRunnerZZZ(File objLogFile) throws ExceptionZZZ {
		super(objLogFile);	
	}
	
	public LogFileWatchRunnerZZZ(File objLogFile, String sFilterSentence) throws ExceptionZZZ {
		super(objLogFile, sFilterSentence);	
	}
	
	public LogFileWatchRunnerZZZ(File objLogFile, String sFilterSentence, String[] saFlag) throws ExceptionZZZ {
		super(objLogFile, sFilterSentence, saFlag);	
	}
		
	//### Statische Methode (um einfacher darauf zugreifen zu können)
    public static Class getEnumStatusLocalClass(){    	
    	return STATUSLOCAL.class;    	
    }
	
	//### Getter / Setter ########
	
	//##########################################################
	//+++ aus IProcessWatchRunnerZZZ
	@Override
	public boolean analyseInputLineCustom(String sLine, String sLinefilter) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{				
			sLine = StringZZZ.trimAnyQuoteMarked(sLine);
			bReturn = super.analyseInputLineCustom(sLine, sLinefilter);			
		}//end main
		return bReturn;
	}

	//Methode wird in der ReactionHashMap angegeben....
	public boolean doStop(IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			String sLog = ReflectCodeZZZ.getPositionCurrent() + "Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", StatusMessage='" + sStatusMessage +"'";
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
				String sLog = ReflectCodeZZZ.getPositionCurrent() +  "Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", StatusMessage='" + sStatusMessage +"'";
				this.logProtocolString(sLog);//!!! hier nicht noch mal eine Methode mit "ProtocolString" aufrufen, sonst wird damit erneut ein LogString gebastelt. D.h. man hat z.B. 2x die Datumsangabe oder 2x die Threadangabe 
						
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
	
	
	//#### GETTER / SETTER
	
		
	
	//###########################################################
	//### STATUS
	//###########################################################
	
	//####### aus IStatusLocalUserZZZ
//	@Override
//	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(objEnumStatusIn==null) break main;
//			
//			//LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) objEnumStatusIn;
//			String sStatusName = objEnumStatusIn.name();
//			if(StringZZZ.isEmpty(sStatusName)) break main;
//										
//			HashMap<String, Boolean> hmFlag = this.getHashMapStatusLocal();
//			Boolean objBoolean = hmFlag.get(sStatusName.toUpperCase());
//			if(objBoolean==null){
//				bFunction = false;
//			}else{
//				bFunction = objBoolean.booleanValue();
//			}
//							
//		}	// end main:
//		
//		return bFunction;	
//	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//+++ OFFER STATUS LOCAL, alle Varianten, gecasted auf dieses Objekt
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
//	@Override
//	public boolean offerStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(enumStatusIn==null) break main;
//			
//			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
//			
//			bFunction = this.offerStatusLocal_(enumStatus, "", bStatusValue);				
//		}//end main;
//		return bFunction;
//	}
//	
//	
//	@Override
//	public boolean offerStatusLocal(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(enumStatusIn==null) break main;
//			
//			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
//			
//			bFunction = this.offerStatusLocal_(enumStatus, sStatusMessage, bStatusValue);				
//		}//end main;
//		return bFunction;
//	}
//	
//	private boolean offerStatusLocal_(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(enumStatusIn==null) break main;
//			
//		
//			//Merke: In anderen Klassen, die dieses Design-Pattern anwenden ist das eine andere Klasse fuer das Enum
//			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
//			String sStatusName = enumStatus.name();
//			bFunction = this.proofStatusLocalExists(sStatusName);															
//			if(!bFunction) {
//				String sLog = ReflectCodeZZZ.getPositionCurrent() + "Would like to fire event, but this status is not available: '" + sStatusName + "'";
//				this.logProtocolString(sLog);			
//				break main;
//			}
//			
//		bFunction = this.proofStatusLocalValueChanged(sStatusName, bStatusValue);
//		if(!bFunction) {
//			String sLog = ReflectCodeZZZ.getPositionCurrent() + "Would like to fire event, but this status has not changed: '" + sStatusName + "'";
//			this.logProtocolString(sLog);
//			break main;
//		}	
//		
//		//++++++++++++++++++++	
//		//Setze den Status nun in die HashMap
//		HashMap<String, Boolean> hmStatus = this.getHashMapStatusLocal();
//		hmStatus.put(sStatusName.toUpperCase(), bStatusValue);
//		
//		//Den enumStatus als currentStatus im Objekt speichern...
//		//                   dito mit dem "vorherigen Status"
//		//Setze nun das Enum, und damit auch die Default-StatusMessage
//		String sStatusMessageToSet = null;
//		if(StringZZZ.isEmpty(sStatusMessage)){
//			if(bStatusValue) {
//				sStatusMessageToSet = enumStatus.getStatusMessage();
//			}else {
//				sStatusMessageToSet = "NICHT " + enumStatus.getStatusMessage();
//			}			
//		}else {
//			sStatusMessageToSet = sStatusMessage;
//		}
//		
//		String sLog = ReflectCodeZZZ.getPositionCurrent() + "Verarbeite sStatusMessageToSet='" + sStatusMessageToSet + "'";
//		this.logProtocolString(sLog);
//
//		//Falls eine Message extra uebergeben worden ist, ueberschreibe...
//		if(sStatusMessageToSet!=null) {
//			sLog = ReflectCodeZZZ.getPositionCurrent() + "Setze sStatusMessageToSet='" + sStatusMessageToSet + "'";
//			this.logProtocolString(sLog);
//		}
//		//Merke: Dabei wird die uebergebene Message in den speziellen "Ringspeicher" geschrieben, auch NULL Werte...
//		//       und es wird ein Event erzeugt und ggfs. wird an registrierte Listener der Event geworfen.
//		this.offerStatusLocalEnum(enumStatus, bStatusValue, sStatusMessageToSet);
//
//		bFunction = true;				
//	}	// end main:
//	return bFunction;
//	}
	
	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//+++ SET STATUS LOCAL, alle Varianten, gecasted auf dieses Objekt
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//	@Override
//	public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(enumStatusIn==null) break main;
//			
//			//LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
//							
//			bFunction = this.offerStatusLocal(enumStatusIn, null, bStatusValue);
//		}//end main:
//		return bFunction;
//	}
//		
//	/* (non-Javadoc)
//	 * @see basic.zBasic.AbstractObjectWithStatusZZZ#setStatusLocalEnum(basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ, boolean)
//	 */
//	@Override 
//	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bReturn = false;
//		main:{
//			if(enumStatusIn==null) break main;
//
//			//LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
//			
//			bReturn = this.offerStatusLocal(enumStatusIn.getName(), null, bStatusValue);
//		}//end main:
//		return bReturn;
//	}
//	
//	//+++ aus IStatusLocalUserMessageZZZ			
//	@Override 
//	public boolean setStatusLocal(Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(enumStatusIn==null) break main;
//			
//			//LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
//			
//			bFunction = this.offerStatusLocal(enumStatusIn.name(), sMessage, bStatusValue);
//		}//end main:
//		return bFunction;
//	}
//		
//	@Override 
//	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bReturn = false;
//		main:{
//			if(enumStatusIn==null) break main;
//			
//			//LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
//			
//			bReturn = this.offerStatusLocal(enumStatusIn.getName(), sMessage, bStatusValue);
//		}//end main:
//		return bReturn;
//	}				
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	//##############################################
	//### Aus IListenerObjectStatusLocalZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	//##############################################
	
	@Override
	public boolean isEventRelevant2ChangeStatusLocalByClass(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(eventStatusLocalSet==null)break main;
			
			if(eventStatusLocalSet instanceof IEventObject4LogFileWatchMonitorStatusLocalZZZ) {
				String sLog = ReflectCodeZZZ.getPositionCurrent() + "Event vom Monitor!!!";
				this.logProtocolString(sLog);
				
				bReturn = true;
			}	
		}
		return bReturn;
	}
	
	@Override
	public boolean isEventRelevant2ChangeStatusLocalByStatusLocalValue(IEventObjectStatusLocalZZZ eventStatusLocal)	throws ExceptionZZZ {
		return true;
	}

	@Override
	public HashMap<IEnumSetMappedStatusZZZ, String>createHashMapStatusLocal4ReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//Merke:
		//Sich selbst darin aufzunehmen, ermöglicht es ebenfalls auf diesen Status mit der genannten Methode (Alias) zu reagieren.
		hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, "doFilterFound");
		
		//Das würde nur Sinn machen, wenn z.B. am Monitor mehrere LogFileWatchRunner registriert sind
		//Dann könnte dieser LogFileWatchRunner auch beendet werden, wenn einer der anderen seinen Filterwert gefunden hat.		
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERFILTERFOUND,"doFilterFound");
		
		//Reagiere auf diee Events... mit dem angegebenen Alias.
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTOPPED, "doStop");
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASERROR, "doStop");
				
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
						bReturn = this.doFilterFound(enumStatus, bStatusValue, sStatusMessage);		
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
	public boolean queryReactOnStatusLocalEventCustom(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ{
		boolean bReturn=false;
		String sLog;
		main:{
									
			bReturn = true;
		}//end main
		return bReturn;
	}

	@Override
	public boolean queryOfferStatusLocalCustom() throws ExceptionZZZ{
		//Diese Methode wird vor dem ...offerStatusLocal... aufgerufen.
		//Dadurch kann alsow verhindert werden, dass weitere Events geworfen werden.
		boolean bReturn=false;
		String sLog;
		main:{
			
			bReturn = true;
		}//end main
		return bReturn;
	}

	@Override
	public boolean queryReactOnStatusLocal4ActionCustom(String sActionAlias, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		return true;
	}
}
