package basic.zBasic.util.moduleExternal.monitor;

import java.util.ArrayList;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.AbstractKernelUseObjectWithStatusListeningCascadedZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zKernel.flag.EventObjectFlagZsetZZZ;
import basic.zKernel.flag.IEventObjectFlagZsetZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.IEventBrokerStatusLocalSetUserZZZ;
import basic.zKernel.status.IEventObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalSetZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalSetZZZ;
import basic.zKernel.status.KernelSenderObjectStatusLocalSetZZZ;

public abstract class AbstractProcessWatchMonitorZZZ  extends AbstractKernelUseObjectWithStatusListeningCascadedZZZ implements IProcessWatchMonitorZZZ, Runnable, IListenerObjectStatusLocalSetZZZ, IEventObjectStatusLocalMessageSetZZZ, IEventBrokerStatusLocalSetUserZZZ{
	protected volatile ArrayList<Process> listaProcess = new ArrayList<Process>(); //Hierueber werden alle zu beobachtenden Processe verwaltet.
	private ISenderObjectStatusLocalSetZZZ objEventStatusLocalBroker=null;//Das Broker Objekt, an dem sich andere Objekte regristrieren können, um ueber Aenderung eines StatusLocal per Event informiert zu werden.
	
	
	public AbstractProcessWatchMonitorZZZ() throws ExceptionZZZ{
		super();
	}

	public AbstractProcessWatchMonitorZZZ(IKernelZZZ objKernel) throws ExceptionZZZ{
		super(objKernel);
	}
	
	
	//###################################################
	//### FLAGS #########################################
	//###################################################
	/* @see basic.zBasic.IFlagZZZ#getFlagZ(java.lang.String)
	 * 	 Weitere Voraussetzungen:
	 * - Public Default Konstruktor der Klasse, damit die Klasse instanziiert werden kann.
	 * - Innere Klassen muessen auch public deklariert werden.(non-Javadoc)
	 */
	public boolean getFlag(String sFlagName) {
		boolean bFunction = false;
		main:{
			if(StringZZZ.isEmpty(sFlagName)) break main;
										
			HashMap<String, Boolean> hmFlag = this.getHashMapFlag();
			Boolean objBoolean = hmFlag.get(sFlagName.toUpperCase());
			if(objBoolean==null){
				bFunction = false;
			}else{
				bFunction = objBoolean.booleanValue();
			}
							
		}	// end main:
		
		return bFunction;	
	}
	
	@Override
	public boolean getFlag(IProcessWatchMonitorZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}
	
	//ALTE VERSION
	/* (non-Javadoc)
	@see zzzKernel.basic.KernelObjectZZZ#getFlag(java.lang.String)
	Flags used: 
	- connectionrunnerstarted	 */
//	public boolean getFlag(String sFlagName){
//		boolean bFunction = false;
//		main:{
//			if(StringZZZ.isEmpty(sFlagName)) break main;
//			bFunction = super.getFlag(sFlagName);
//			if(bFunction==true) break main;
//		
			//getting the flags of this object
//			String stemp = sFlagName.toLowerCase();
//			if(stemp.equals("connectionrunnerstarted")){
//				bFunction = bFlagConnectionRunnerStarted;
//				break main;
//			}		
//		}//end main:
//		return bFunction;
//	}
	
	/** DIESE METHODE MUSS IN ALLEN KLASSEN VORHANDEN SEIN - über Vererbung -, DIE IHRE FLAGS SETZEN WOLLEN
	 * Weitere Voraussetzungen:
	 * - Public Default Konstruktor der Klasse, damit die Klasse instanziiert werden kann.
	 * - Innere Klassen müssen auch public deklariert werden.
	 * @param objClassParent
	 * @param sFlagName
	 * @param bFlagValue
	 * @return
	 * lindhaueradmin, 23.07.2013
	 */
	@Override
	public boolean setFlag(String sFlagName, boolean bFlagValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(StringZZZ.isEmpty(sFlagName)) {
				bFunction = true;
				break main;
			}
						
			bFunction = this.proofFlagExists(sFlagName);															
			if(bFunction == true){
				
				//Setze das Flag nun in die HashMap
				HashMap<String, Boolean> hmFlag = this.getHashMapFlag();
				hmFlag.put(sFlagName.toUpperCase(), bFlagValue);								
				
				//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
				//Dann erzeuge den Event und feuer ihn ab.
				if(this.objEventFlagZBroker!=null) {
					IEventObjectFlagZsetZZZ event = new EventObjectFlagZsetZZZ(this,1,sFlagName.toUpperCase(), bFlagValue);
					this.objEventFlagZBroker.fireEvent(event);
				}
				
				bFunction = true;								
			}										
		}	// end main:
		
		return bFunction;	
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
	public ISenderObjectStatusLocalSetZZZ getSenderStatusLocalUsed() throws ExceptionZZZ {
		if(this.objEventStatusLocalBroker==null) {
			//++++++++++++++++++++++++++++++
			//Nun geht es darum den Sender fuer Aenderungen am Status zu erstellen, der dann registrierte Objekte ueber Aenderung von Flags informiert
			ISenderObjectStatusLocalSetZZZ objSenderStatusLocal = new KernelSenderObjectStatusLocalSetZZZ();
			this.objEventStatusLocalBroker = objSenderStatusLocal;
		}
		return this.objEventStatusLocalBroker;
	}

	@Override
	public void setSenderStatusLocalUsed(ISenderObjectStatusLocalSetZZZ objEventSender) {
		this.objEventStatusLocalBroker = objEventSender;
	}
		
	
	//### aus IEventBrokerStatusLocalSetUserZZZ
	@Override
	public void registerForStatusLocalEvent(IListenerObjectStatusLocalSetZZZ objEventListener)throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().addListenerObjectStatusLocalSet(objEventListener);		
	}
	
	@Override
	public void unregisterForStatusLocalEvent(IListenerObjectStatusLocalSetZZZ objEventListener) throws ExceptionZZZ {
		this.getSenderStatusLocalUsed().removeListenerObjectStatusLocalSet(objEventListener);;
	}
	
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
