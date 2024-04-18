package basic.zBasic.util.moduleExternal;

import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ.FLAGZ;

public interface ICreateRunnerZZZ {
	
	
	//##############################################################	
	public enum FLAGZ{
		DUMMY,END_ON_FILTER_FOUND,IMMEDIATE_END_ON_FILTER_FOUND
	}

	boolean getFlag(FLAGZ objEnumFlag);
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
	
	
	//##############################################################
}
