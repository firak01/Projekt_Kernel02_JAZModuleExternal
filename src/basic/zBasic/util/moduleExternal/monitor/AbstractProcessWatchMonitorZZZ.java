package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import basic.zBasic.AbstractObjectWithStatusOnStatusListeningZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.AbstractProgramMonitorZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.flag.event.EventObjectFlagZsetZZZ;
import basic.zKernel.flag.event.IEventObjectFlagZsetZZZ;
import basic.zKernel.status.IEventBrokerStatusLocalUserZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalZZZ;
import basic.zKernel.status.SenderObjectStatusLocalZZZ;

public abstract class AbstractProcessWatchMonitorZZZ  extends AbstractProgramMonitorZZZ implements IProcessWatchMonitorZZZ{
	private static final long serialVersionUID = 4532365489539285236L;
	protected volatile Process objProcess = null;
	
	public AbstractProcessWatchMonitorZZZ() throws ExceptionZZZ{
		super();
	}
	
	public AbstractProcessWatchMonitorZZZ(String[] saFlagControl) throws ExceptionZZZ{
		super(saFlagControl);
	}
	
	@Override
	public Process getProcess() {
		return this.objProcess;
	}
	
	@Override
	public void setProcess(Process objProcess) {
		this.objProcess = objProcess;
	}
	
		
	//###################################################
	//### FLAGS #########################################
	//###################################################
		
	@Override
	public boolean getFlag(IProcessWatchMonitorZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}	
	
	@Override
	public boolean setFlag(IProcessWatchMonitorZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(IProcessWatchMonitorZZZ.FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IProcessWatchMonitorZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
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
	public boolean proofFlagExists(IProcessWatchMonitorZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(IProcessWatchMonitorZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
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

		
		
	//########################################################################
	//####################################
	//### STATUS: IProcessWatchMonitorZZZ
	//####################################
	
//	//####### aus ISenderObjectStatusLocalSetUserZZZ
//	/* (non-Javadoc)
//	 * @see basic.zKernel.status.ISenderObjectStatusLocalSetUserZZZ#getSenderStatusLocalUsed()
//	 */
//	@Override
//	public ISenderObjectStatusLocalZZZ getSenderStatusLocalUsed() throws ExceptionZZZ {
//		if(this.objEventStatusLocalBroker==null) {
//			//++++++++++++++++++++++++++++++
//			//Nun geht es darum den Sender fuer Aenderungen am Status zu erstellen, der dann registrierte Objekte ueber Aenderung von Flags informiert
//			ISenderObjectStatusLocalZZZ objSenderStatusLocal = new SenderObjectStatusLocalZZZ();
//			this.objEventStatusLocalBroker = objSenderStatusLocal;
//		}
//		return this.objEventStatusLocalBroker;
//	}
//
//	@Override
//	public void setSenderStatusLocalUsed(ISenderObjectStatusLocalZZZ objEventSender) {
//		this.objEventStatusLocalBroker = objEventSender;
//	}
//		
//	
//	//### aus IEventBrokerStatusLocalSetUserZZZ
//	@Override
//	public void registerForStatusLocalEvent(IListenerObjectStatusLocalZZZ objEventListener)throws ExceptionZZZ {
//		this.getSenderStatusLocalUsed().addListenerObject(objEventListener);		
//	}
//	
//	@Override
//	public void unregisterForStatusLocalEvent(IListenerObjectStatusLocalZZZ objEventListener) throws ExceptionZZZ {
//		this.getSenderStatusLocalUsed().removeListenerObject(objEventListener);;
//	}
	

//	@Override
//	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(objEnumStatusIn==null) {
//				break main;
//			}
//			
//			//IProcessWatchMonitorZZZ.STATUSLOCAL enumStatus = (IProcessWatchMonitorZZZ.STATUSLOCAL) objEnumStatusIn;
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
		//+++ SET STATUS LOCAL, alle Varianten, gecasted auf dieses Objekt
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//		@Override
//		public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
//			boolean bFunction = false;
//			main:{
//				if(enumStatusIn==null) break main;
//				
//				//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
//								
//				bFunction = this.offerStatusLocal(enumStatusIn, null, bStatusValue);
//			}//end main:
//			return bFunction;
//		}
//			
//		/* (non-Javadoc)
//		 * @see basic.zBasic.AbstractObjectWithStatusZZZ#setStatusLocalEnum(basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ, boolean)
//		 */
//		@Override 
//		public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
//			boolean bReturn = false;
//			main:{
//				if(enumStatusIn==null) break main;
//
//				//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
//				
//				bReturn = this.offerStatusLocal(enumStatusIn.getName(), null, bStatusValue);
//			}//end main:
//			return bReturn;
//		}
//		
//		//+++ aus IStatusLocalUserMessageZZZ			
//		@Override 
//		public boolean setStatusLocal(Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
//			boolean bFunction = false;
//			main:{
//				if(enumStatusIn==null) break main;
//				
//				//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
//				
//				bFunction = this.offerStatusLocal(enumStatusIn.name(), sMessage, bStatusValue);
//			}//end main:
//			return bFunction;
//		}
//			
//		@Override 
//		public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
//			boolean bReturn = false;
//			main:{
//				if(enumStatusIn==null) break main;
//				
//				//ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
//				
//				bReturn = this.offerStatusLocal(enumStatusIn.getName(), sMessage, bStatusValue);
//			}//end main:
//			return bReturn;
//		}				
//				
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}
