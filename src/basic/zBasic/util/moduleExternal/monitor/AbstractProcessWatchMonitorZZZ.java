package basic.zBasic.util.moduleExternal.monitor;

import java.util.ArrayList;
import java.util.HashMap;

import basic.zBasic.AbstractObjectWithStatusOnStatusListeningZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.flag.event.EventObjectFlagZsetZZZ;
import basic.zKernel.flag.event.IEventObjectFlagZsetZZZ;
import basic.zKernel.status.IEventBrokerStatusLocalUserZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalZZZ;
import basic.zKernel.status.SenderObjectStatusLocalZZZ;

public abstract class AbstractProcessWatchMonitorZZZ  extends AbstractObjectWithStatusOnStatusListeningZZZ implements IProcessWatchMonitorZZZ, IListenerObjectStatusLocalZZZ, IEventBrokerStatusLocalUserZZZ{
	private static final long serialVersionUID = 4532365489539285236L;
	protected volatile ArrayList<Process> listaProcess = new ArrayList<Process>(); //Hierueber werden alle zu beobachtenden Processe verwaltet.
	private ISenderObjectStatusLocalZZZ objEventStatusLocalBroker=null;//Das Broker Objekt, an dem sich andere Objekte regristrieren können, um ueber Aenderung eines StatusLocal per Event informiert zu werden.
	
	public AbstractProcessWatchMonitorZZZ() throws ExceptionZZZ{
		super();
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
	
	//ALTE VERSION
		/**
		 * @see zzzKernel.basic.KernelUseObjectZZZ#setFlag(java.lang.String, boolean)
		 * @param sFlagName
		 * Flags used:<CR>
		 	- ConnectionRunnerStarted.
		 * @throws ExceptionZZZ 
		 */
//		public boolean setFlag(String sFlagName, boolean bFlagValue) throws ExceptionZZZ{
//			boolean bFunction = false;
//			main:{			
//				if(StringZZZ.isEmpty(sFlagName)) break main;
//				bFunction = super.setFlag(sFlagName, bFlagValue);
//				if(bFunction==true) break main;
//				
				//setting the flags of this object
//				String stemp = sFlagName.toLowerCase();
//				if(stemp.equals("connectionrunnerstarted")){
//					bFlagConnectionRunnerStarted = bFlagValue;
//					bFunction = true;
//					break main;
	//	
//				}
//			}//end main:
//			return bFunction;
//		}
	//	

	

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

	//################################################
	//### STATUS #####################################
	//################################################
	
	//####### aus ISenderObjectStatusLocalSetUserZZZ
	/* (non-Javadoc)
	 * @see basic.zKernel.status.ISenderObjectStatusLocalSetUserZZZ#getSenderStatusLocalUsed()
	 */
	@Override
	public ISenderObjectStatusLocalZZZ getSenderStatusLocalUsed() throws ExceptionZZZ {
		if(this.objEventStatusLocalBroker==null) {
			//++++++++++++++++++++++++++++++
			//Nun geht es darum den Sender fuer Aenderungen am Status zu erstellen, der dann registrierte Objekte ueber Aenderung von Flags informiert
			ISenderObjectStatusLocalZZZ objSenderStatusLocal = new SenderObjectStatusLocalZZZ();
			this.objEventStatusLocalBroker = objSenderStatusLocal;
		}
		return this.objEventStatusLocalBroker;
	}

	@Override
	public void setSenderStatusLocalUsed(ISenderObjectStatusLocalZZZ objEventSender) {
		this.objEventStatusLocalBroker = objEventSender;
	}
		
	
	//### aus IEventBrokerStatusLocalSetUserZZZ
	@Override
	public void registerForStatusLocalEvent(IListenerObjectStatusLocalZZZ objEventListener)throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().addListenerObject(objEventListener);		
	}
	
	@Override
	public void unregisterForStatusLocalEvent(IListenerObjectStatusLocalZZZ objEventListener) throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().removeListenerObject(objEventListener);;
	}
	
	@Override
	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(objEnumStatusIn==null) {
				break main;
			}
			
			//Merke: Bei einer anderen Klasse, die dieses DesingPattern nutzt, befindet sich der STATUSLOCAL in einer anderen Klasse
			IProcessWatchMonitorZZZ.STATUSLOCAL enumStatus = (IProcessWatchMonitorZZZ.STATUSLOCAL) objEnumStatusIn;
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
