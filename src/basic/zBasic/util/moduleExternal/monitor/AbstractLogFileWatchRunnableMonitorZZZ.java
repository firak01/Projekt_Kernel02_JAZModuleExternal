package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.AbstractProgramMonitoRunnablerZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.flag.EventObjectFlagZsetZZZ;
import basic.zKernel.flag.IEventObjectFlagZsetZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalMessageZZZ;
import basic.zKernel.status.KernelSenderObjectStatusLocalMessageZZZ;

public abstract class AbstractLogFileWatchRunnableMonitorZZZ  extends AbstractProgramMonitoRunnablerZZZ implements ILogFileWatchRunnerMonitorZZZ{
	private static final long serialVersionUID = 968455281850239704L;
	protected volatile File objLogFile = null;	
		
	public AbstractLogFileWatchRunnableMonitorZZZ() throws ExceptionZZZ{
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
	//### FLAGS #########################################
	//###################################################
	
	@Override
	public boolean getFlag(ILogFileWatchRunnerMonitorZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}
	
	@Override
	public boolean setFlag(ILogFileWatchRunnerMonitorZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}
	
	@Override
	public boolean[] setFlag(ILogFileWatchRunnerMonitorZZZ.FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileWatchRunnerMonitorZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
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
	public boolean proofFlagExists(ILogFileWatchRunnerMonitorZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileWatchRunnerMonitorZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagSetBefore(objEnumFlag.name());
	}

	//################################################
	//### STATUS #####################################
	//################################################
	
	//####### aus ISenderObjectStatusLocalSetUserZZZ
	/* (non-Javadoc)
	 * @see basic.zKernel.status.ISenderObjectStatusLocalSetUserZZZ#getSenderStatusLocalUsed()
	 */
	@Override
	public ISenderObjectStatusLocalMessageZZZ getSenderStatusLocalUsed() throws ExceptionZZZ {
		if(this.objEventStatusLocalBroker==null) {
			//++++++++++++++++++++++++++++++
			//Nun geht es darum den Sender fuer Aenderungen am Status zu erstellen, der dann registrierte Objekte ueber Aenderung von Flags informiert
			ISenderObjectStatusLocalMessageZZZ objSenderStatusLocal = new KernelSenderObjectStatusLocalMessageZZZ();
			this.objEventStatusLocalBroker = objSenderStatusLocal;
		}
		return this.objEventStatusLocalBroker;
	}

	@Override
	public void setSenderStatusLocalUsed(ISenderObjectStatusLocalMessageZZZ objEventSender) {
		this.objEventStatusLocalBroker = objEventSender;
	}
		
	
	//### aus IEventBrokerStatusLocalMessageSetUserZZZ
//	@Override
//	public void registerForStatusLocalEvent(IListenerObjectStatusBasicZZZ objEventListener)throws ExceptionZZZ {
//		this.getSenderStatusLocalUsed().addListenerObject(objEventListener);		
//	}
//	
//	@Override
//	public void unregisterForStatusLocalEvent(IListenerObjectStatusBasicZZZ objEventListener) throws ExceptionZZZ {
//		this.getSenderStatusLocalUsed().removeListenerObject(objEventListener);;
//	}
	
	@Override
	public abstract boolean isStatusLocalRelevant(IEnumSetMappedStatusZZZ objEnumStatusIn) throws ExceptionZZZ;
	
	@Override
	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(objEnumStatusIn==null) {
				break main;
			}
			
			//Merke: Bei einer anderen Klasse, die dieses DesingPattern nutzt, befindet sich der STATUSLOCAL in einer anderen Klasse
			ILogFileWatchRunnerMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchRunnerMonitorZZZ.STATUSLOCAL) objEnumStatusIn;
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
