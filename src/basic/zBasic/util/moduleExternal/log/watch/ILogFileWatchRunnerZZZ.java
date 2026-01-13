package basic.zBasic.util.moduleExternal.log.watch;

import java.io.File;
import java.util.EnumSet;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;

public interface ILogFileWatchRunnerZZZ extends IWatchRunnerZZZ {
	public File getLogFileWatched();
	public void setLogFileWatched(File objFile);
	
	//##############################################################	
	
	public enum FLAGZ{
		DUMMY
	}
	boolean getFlag(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
}
