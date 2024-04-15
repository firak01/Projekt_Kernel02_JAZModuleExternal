package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramMonitorZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.EventObject4LogFileWatchMonitorStatusLocalZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchMonitorStatusLocalZZZ;

public abstract class AbstractLogFileWatchMonitorZZZ  extends AbstractProgramMonitorZZZ implements ILogFileWatchMonitorZZZ{
	private static final long serialVersionUID = 968455281850239704L;
	protected volatile File objLogFile = null;	
		
	public AbstractLogFileWatchMonitorZZZ() throws ExceptionZZZ{
		super();
	}
	
	public AbstractLogFileWatchMonitorZZZ(String[] saFlagControl) throws ExceptionZZZ{
		super(saFlagControl);
	}
	
	@Override
	public File getLogFile() {
		return this.objLogFile;
	}
	
	@Override
	public void setLogFile(File objFile) {
		this.objLogFile = objFile;
	}
	
	//###################################################
	//### FLAGS ILogFileWatchRunnerMonitorZZZ ###########
	//###################################################
	
	@Override
	public boolean getFlag(ILogFileWatchMonitorZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}
	
	@Override
	public boolean setFlag(ILogFileWatchMonitorZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}
	
	@Override
	public boolean[] setFlag(ILogFileWatchMonitorZZZ.FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileWatchMonitorZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
				
				//!!! Ein mögliches init-Flag ist beim direkten setzen der Flags unlogisch.
			//    Es wird entfernt.
			this.setFlag(IFlagZUserZZZ.FLAGZ.INIT, false);
		}
	}//end main:
		return baReturn;
	}
	
	@Override
	public boolean proofFlagExists(ILogFileWatchMonitorZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileWatchMonitorZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagSetBefore(objEnumFlag.name());
	}
	
	//##################################################
	//### FLAGS IWatchListenerZZZ			 ###########
	//##################################################
		
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
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IWatchListenerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
				
				//!!! Ein mögliches init-Flag ist beim direkten setzen der Flags unlogisch.
			//    Es wird entfernt.
			this.setFlag(IFlagZUserZZZ.FLAGZ.INIT, false);
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
	
	//###########################################################################
	//####################################
	//### STATUS: ILogFileWatchMonitorZZZ
	//####################################

	//####### aus IStatusLocalUserZZZ
//	@Override
//	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(objEnumStatusIn==null) break main;
//			
//			//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) objEnumStatusIn;
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
//			ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
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
//			ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
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
//			ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
//			String sStatusName = enumStatus.name();
//			bFunction = this.proofStatusLocalExists(sStatusName);															
//			if(!bFunction) {
//				String sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatus ("+this.getClass().getName()+") would like to fire event, but this status is not available: '" + sStatusName + "'";
//				this.logProtocolString(sLog);			
//				break main;
//			}
//			
//		bFunction = this.proofStatusLocalValueChanged(sStatusName, bStatusValue);
//		if(!bFunction) {
//			String sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatus ("+this.getClass().getName()+")  would like to fire event, but this status has not changed: '" + sStatusName + "'";
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
//		String sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatus ("+this.getClass().getName()+") verarbeitet sStatusMessageToSet='" + sStatusMessageToSet + "'";
//		this.logProtocolString(sLog);
//
//		//Falls eine Message extra uebergeben worden ist, ueberschreibe...
//		if(sStatusMessageToSet!=null) {
//			sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatus (\"+this.getClass().getName()+\")  setzt sStatusMessageToSet='" + sStatusMessageToSet + "'";
//			this.logProtocolString(sLog);
//		}
//		//Merke: Dabei wird die uebergebene Message in den speziellen "Ringspeicher" geschrieben, auch NULL Werte...
//		this.offerStatusLocalEnum(enumStatus, bStatusValue, sStatusMessageToSet);
//		
//		
//		
//		//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
//		//Dann erzeuge den Event und feuer ihn ab.	
//		if(this.getSenderStatusLocalUsed()==null) {
//			sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatus ("+this.getClass().getName()+")  would like to fire event '" + enumStatus.getAbbreviation() + "', but no objEventStatusLocalBroker available, any registered?";
//			this.logProtocolString(sLog);		
//			break main;
//		}
//		
//		//Erzeuge fuer das Enum einen eigenen Event. Die daran registrierten Klassen koennen in einer HashMap definieren, ob der Event fuer sie interessant ist.		
//		sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatus ("+this.getClass().getName()+") erzeugt Event fuer '" + sStatusName + "'";		
//		this.logProtocolString(sLog);
//		IEventObject4LogFileWatchMonitorStatusLocalZZZ event = new EventObject4LogFileWatchMonitorStatusLocalZZZ(this,enumStatus, bStatusValue);			
//		
//		//### GGFS. noch weitere benoetigte Objekte hinzufuegen............
//		//...
//		
//				
//		//Feuere den Event ueber den Broker ab.
//		sLog = ReflectCodeZZZ.getPositionCurrent() + "ObjectWithStatus ("+this.getClass().getName()+") fires event '" + enumStatus.getAbbreviation() + "'";
//		this.logProtocolString(sLog);
//		this.getSenderStatusLocalUsed().fireEvent(event);
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
//			//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
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
//			//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
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
//			//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
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
//			//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
//			
//			bReturn = this.offerStatusLocal(enumStatusIn.getName(), sMessage, bStatusValue);
//		}//end main:
//		return bReturn;
//	}				
//			
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
