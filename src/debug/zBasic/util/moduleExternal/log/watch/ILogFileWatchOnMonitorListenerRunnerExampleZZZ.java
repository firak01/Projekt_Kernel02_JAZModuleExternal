package debug.zBasic.util.moduleExternal.log.watch;

import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ.FLAGZ;

public interface ILogFileWatchOnMonitorListenerRunnerExampleZZZ extends IWatchListenerZZZ{
		
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
