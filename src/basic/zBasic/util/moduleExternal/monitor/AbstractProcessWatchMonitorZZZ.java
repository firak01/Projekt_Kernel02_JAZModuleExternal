package basic.zBasic.util.moduleExternal.monitor;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.AbstractProgramMonitorZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zKernel.flag.IFlagZEnabledZZZ;

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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IProcessWatchMonitorZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
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
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IWatchListenerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
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
	public boolean proofFlagExists(IWatchListenerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(IWatchListenerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagSetBefore(objEnumFlag.name());
	}	

}
