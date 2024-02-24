package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.AbstractProgramMonitorZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;

public abstract class AbstractLogFileWatchMonitorZZZ  extends AbstractProgramMonitorZZZ implements ILogFileWatchMonitorZZZ{
	private static final long serialVersionUID = 968455281850239704L;
	protected volatile File objLogFile = null;	
		
	public AbstractLogFileWatchMonitorZZZ() throws ExceptionZZZ{
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
}
