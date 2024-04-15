package debug.zBasic.util.moduleExternal.process.watch;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;

public interface IProcessWatchOnMonitorListenerRunnerExampleZZZ extends IWatchListenerZZZ{
		
	//##############################################################	
	public enum FLAGZ{
		DUMMY
	}

	boolean getFlag(FLAGZ objEnumFlag);
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
	
	
	//##############################################################
}
