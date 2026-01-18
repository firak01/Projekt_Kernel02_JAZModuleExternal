package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramMonitorRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusLocalZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zKernel.flag.IFlagZEnabledZZZ;

/**Beachte: Es wird ILogFileWatchMonitorZZZ implementiert
 *          und nicht etwa ein ILogFileWatchMonitorRunnableZZZ.
 *          Ich gehe erst einmal von keinen Unterschieden aus. 
 *          
 * @author Fritz Lindhauer, 27.02.2024, 20:59:33
 * 
 */
public abstract class AbstractLogFileWatchMonitorRunnableZZZ  extends AbstractProgramMonitorRunnableZZZ implements ILogFileWatchMonitorRunnableZZZ{
	private static final long serialVersionUID = 968455281850239704L;
	protected volatile File objLogFile = null;	
		
	public AbstractLogFileWatchMonitorRunnableZZZ() throws ExceptionZZZ{
		super();
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
	//### FLAGS ILogFileWatchrMonitorZZZ ###########
	//###################################################
	
	@Override
	public boolean getFlag(ILogFileWatchMonitorZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileWatchMonitorZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
				
				//!!! Ein mögliches init-Flag ist beim direkten setzen der Flags unlogisch.
			//    Es wird entfernt.
			this.setFlag(IFlagZEnabledZZZ.FLAGZ.INIT, false);
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
	
	//###################################################
	//### FLAGS ILogFileWatchMonitorRunnableZZZ ###########
	//###################################################
	
	@Override
	public boolean getFlag(ILogFileWatchMonitorRunnableZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.getFlag(objEnumFlag.name());
	}
	
	@Override
	public boolean setFlag(ILogFileWatchMonitorRunnableZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}
	
	@Override
	public boolean[] setFlag(ILogFileWatchMonitorRunnableZZZ.FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileWatchMonitorRunnableZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
				
				//!!! Ein mögliches init-Flag ist beim direkten setzen der Flags unlogisch.
			//    Es wird entfernt.
			this.setFlag(IFlagZEnabledZZZ.FLAGZ.INIT, false);
		}
	}//end main:
		return baReturn;
	}
	
	@Override
	public boolean proofFlagExists(ILogFileWatchMonitorRunnableZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileWatchMonitorRunnableZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagSetBefore(objEnumFlag.name());
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
	
		
	
	//#########################################################################
	//####################################
	//### STATUS: ILogFileWatchMonitorRunanbleZZZ
	//####################################

	//####### aus IStatusLocalUserZZZ
	@Override
	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(objEnumStatusIn==null) break main;
			
			ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL) objEnumStatusIn;
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
			
			ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal_(enumStatus, "", bStatusValue);				
		}//end main;
		return bFunction;
	}
	
	
	@Override
	public boolean offerStatusLocal(Enum enumStatusIn, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) break main;
			
			ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal_(enumStatus, sStatusMessage, bStatusValue);				
		}//end main;
		return bFunction;
	}
	
	private boolean offerStatusLocal_(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) break main;
			
			String sLog;
		
			//Merke: In anderen Klassen, die dieses Design-Pattern anwenden ist das eine andere Klasse fuer das Enum
			ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL) enumStatusIn;
			String sStatusName = enumStatus.name();
			bFunction = this.proofStatusLocalExists(sStatusName);															
			if(!bFunction) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Would like to fire event, but this status is not available: '" + sStatusName + "'";
				this.logProtocol(sLog);			
				break main;
			}
			
		bFunction = this.proofStatusLocalValueChanged(sStatusName, bStatusValue);
		if(!bFunction) {
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Would like to fire event, but this status has not changed: '" + sStatusName + "'";
			this.logProtocol(sLog);
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
		
		sLog = ReflectCodeZZZ.getPositionCurrent() + "Verarbeitet sStatusMessageToSet='" + sStatusMessageToSet + "'";
		this.logProtocol(sLog);

		//Falls eine Message extra uebergeben worden ist, ueberschreibe...
		if(sStatusMessageToSet!=null) {
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Setzt sStatusMessageToSet='" + sStatusMessageToSet + "'";
			this.logProtocol(sLog);
		}
		//Merke: Dabei wird die uebergebene Message in den speziellen "Ringspeicher" geschrieben, auch NULL Werte
		//       und der Event wird ggfs. geworfen...
		bFunction = this.offerStatusLocalEnum(enumStatus, bStatusValue, sStatusMessageToSet);						
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
			
			ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL) enumStatusIn;
							
			bFunction = this.offerStatusLocal(enumStatus, bStatusValue, null);
		}//end main:
		return bFunction;
	}
		
	/* (non-Javadoc)
	 * @see basic.zBasic.AbstractObjectWithStatusZZZ#setStatusLocalEnum(basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ, boolean)
	 */
	@Override 
	public boolean setStatusLocalEnum(IEnumSetMappedStatusLocalZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(enumStatusIn==null) break main;

			ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL) enumStatusIn;
			
			bReturn = this.offerStatusLocal(enumStatus, bStatusValue, null);
		}//end main:
		return bReturn;
	}
	
	//+++ aus IStatusLocalUserMessageZZZ			
	@Override 
	public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue, String sMessage) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) break main;
			
			ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal(enumStatus, bStatusValue, sMessage);
		}//end main:
		return bFunction;
	}
		
	@Override 
	public boolean setStatusLocalEnum(IEnumSetMappedStatusLocalZZZ enumStatusIn, boolean bStatusValue, String sMessage) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(enumStatusIn==null) break main;
			
			ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorRunnableZZZ.STATUSLOCAL) enumStatusIn;
			
			bReturn = this.offerStatusLocal(enumStatus, bStatusValue, sMessage);
		}//end main:
		return bReturn;
	}				
			
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
