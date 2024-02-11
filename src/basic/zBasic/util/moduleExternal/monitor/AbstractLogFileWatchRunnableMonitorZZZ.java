package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.AbstractProgramRunnableMonitorZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.flag.EventObjectFlagZsetZZZ;
import basic.zKernel.flag.IEventObjectFlagZsetZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalMessageZZZ;
import basic.zKernel.status.KernelSenderObjectStatusLocalMessageZZZ;

public abstract class AbstractLogFileWatchRunnableMonitorZZZ  extends AbstractProgramRunnableMonitorZZZ implements ILogFileWatchRunnerMonitorZZZ{
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
	public boolean getFlag(ILogFileWatchRunnerMonitorZZZ.FLAGZ objEnumFlag) {
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
	public boolean setFlag(ILogFileWatchRunnerMonitorZZZ.FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ {
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
