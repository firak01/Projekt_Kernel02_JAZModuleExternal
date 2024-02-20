package basic.zBasic.util.moduleExternal.log.watch;

import java.io.File;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.EventObject4LogFileWatchRunnerStatusLocalZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchRunnerStatusLocalZZZ;
import basic.zKernel.status.IEventObjectStatusLocalMessageZZZ;
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
		super();	
		LogFileWatchRunnerNew_(saFlag);
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
	
		
	
	//###########################################################
	//### STATUS
	//###########################################################
	
	//####### aus IStatusLocalUserZZZ
	@Override
	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(objEnumStatusIn==null) break main;
			
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

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//+++ OFFER STATUS LOCAL, alle Varianten, gecasted auf dieses Objekt
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	@Override
	public boolean offerStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) break main;
			
			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal_(enumStatus, "", bStatusValue);				
		}//end main;
		return bFunction;
	}
	
	
	@Override
	public boolean offerStatusLocal(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) break main;
			
			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal_(enumStatus, sStatusMessage, bStatusValue);				
		}//end main;
		return bFunction;
	}
	
	private boolean offerStatusLocal_(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) break main;
			
		
			//Merke: In anderen Klassen, die dieses Design-Pattern anwenden ist das eine andere Klasse fuer das Enum
			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			String sStatusName = enumStatus.name();
			bFunction = this.proofStatusLocalExists(sStatusName);															
			if(!bFunction) {
				String sLog = ReflectCodeZZZ.getPositionCurrent() + " LogFileWatchRunner for Process would like to fire event, but this status is not available: '" + sStatusName + "'";
				this.logProtocolString(sLog);			
				break main;
			}
			
		bFunction = this.proofStatusLocalValueChanged(sStatusName, bStatusValue);
		if(!bFunction) {
			String sLog = ReflectCodeZZZ.getPositionCurrent() + " LogFileWatchRunner would like to fire event, but this status has not changed: '" + sStatusName + "'";
			this.logProtocolString(sLog);
			break main;
		}	
		
		//++++++++++++++++++++	
		//Setze den Status nun in die HashMap
		HashMap<String, Boolean> hmStatus = this.getHashMapStatusLocal();
		hmStatus.put(sStatusName.toUpperCase(), bStatusValue);
		
		//Den enumStatus als currentStatus im Objekt speichern...
		//                   dito mit dem "vorherigen Status"
		//Setze nun das Enum, und damit auch die Default-StatusMessage
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
		
		String sLog = ReflectCodeZZZ.getPositionCurrent() + " LogFileWatchRunner verarbeite sStatusMessageToSet='" + sStatusMessageToSet + "'";
		this.logProtocolString(sLog);

		//Falls eine Message extra uebergeben worden ist, ueberschreibe...
		if(sStatusMessageToSet!=null) {
			sLog = ReflectCodeZZZ.getPositionCurrent() + " LogFileWatchRunner setze sStatusMessageToSet='" + sStatusMessageToSet + "'";
			this.logProtocolString(sLog);
		}
		//Merke: Dabei wird die uebergebene Message in den speziellen "Ringspeicher" geschrieben, auch NULL Werte...
		this.offerStatusLocalEnum(enumStatus, bStatusValue, sStatusMessageToSet);
		
		
		
		//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
		//Dann erzeuge den Event und feuer ihn ab.	
		if(this.getSenderStatusLocalUsed()==null) {
			sLog = ReflectCodeZZZ.getPositionCurrent() + " LogFileWatchRunner for Process would like to fire event '" + enumStatus.getAbbreviation() + "', but no objEventStatusLocalBroker available, any registered?";
			this.logProtocolString(sLog);		
			break main;
		}
		
		//Erzeuge fuer das Enum einen eigenen Event. Die daran registrierten Klassen koennen in einer HashMap definieren, ob der Event fuer sie interessant ist.		
		sLog = ReflectCodeZZZ.getPositionCurrent() + ": LogFileWatchRunner erzeuge Event fuer '" + sStatusName + "'";		
		this.logProtocolString(sLog);
		IEventObject4LogFileWatchRunnerStatusLocalZZZ event = new EventObject4LogFileWatchRunnerStatusLocalZZZ(this,enumStatus, bStatusValue);			
		
		//### GGFS. noch weitere benoetigte Objekte hinzufuegen............
		//...
		
				
		//Feuere den Event ueber den Broker ab.
		sLog = ReflectCodeZZZ.getPositionCurrent() + " LogFileWatchRunner for Process fires event '" + enumStatus.getAbbreviation() + "'";
		this.logProtocolString(sLog);
		this.getSenderStatusLocalUsed().fireEvent(event);
				
		bFunction = true;				
	}	// end main:
	return bFunction;
	}
	
	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//+++ SET STATUS LOCAL, alle Varianten, gecasted auf dieses Objekt
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	@Override
	public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) break main;
			
			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
							
			bFunction = this.offerStatusLocal(enumStatus, null, bStatusValue);
		}//end main:
		return bFunction;
	}
		
	/* (non-Javadoc)
	 * @see basic.zBasic.AbstractObjectWithStatusZZZ#setStatusLocalEnum(basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ, boolean)
	 */
	@Override 
	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(enumStatusIn==null) break main;

			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bReturn = this.offerStatusLocal(enumStatus, null, bStatusValue);
		}//end main:
		return bReturn;
	}
	
	//+++ aus IStatusLocalUserMessageZZZ			
	@Override 
	public boolean setStatusLocal(Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) break main;
			
			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
		}//end main:
		return bFunction;
	}
		
	@Override 
	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(enumStatusIn==null) break main;
			
			LogFileWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
			
			bReturn = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
		}//end main:
		return bReturn;
	}				
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	//##############################################
	//### Aus IListenerObjectStatusBasicZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	//##############################################
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		String sLog=null;
		
		main:{
			sLog = ReflectCodeZZZ.getPositionCurrent() + ": Filter gefunden und mache den changeStatusLocal Event.";
			this.logProtocolString(sLog);
			
			if(eventStatusLocal instanceof IEventObjectStatusLocalMessageZZZ) {// .getClass().getSimpleName().equals("LogFileCreateMockRunnerZZZ")) {
				IEventObjectStatusLocalMessageZZZ event = (IEventObjectStatusLocalMessageZZZ) eventStatusLocal;
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

	//#######################################
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
	
	@Override
	public boolean isEventRelevant(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			
			if(!this.isEventRelevant2ChangeStatusLocal(eventStatusLocal)) break main;
			if(!this.isEventRelevantByClass2ChangeStatusLocal(eventStatusLocal)) break main;
			if(!this.isEventRelevantByStatusLocal2ChangeStatusLocal(eventStatusLocal)) break main;
			if(!this.isEventRelevantByStatusLocalValue2ChangeStatusLocal(eventStatusLocal)) break main;
			
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	@Override
	public boolean isStatusLocalDifferent(String sStatusString, boolean bStatusValue) throws ExceptionZZZ {
		return true;
	}
	
	@Override
	public boolean isEventRelevant2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocal2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}
}
