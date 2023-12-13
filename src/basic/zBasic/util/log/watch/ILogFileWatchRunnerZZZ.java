package basic.zBasic.util.log.watch;

import java.io.File;

import basic.zBasic.ExceptionZZZ;

public interface ILogFileWatchRunnerZZZ {
	public enum FLAGZ{
		DUMMY
	}

	boolean getFlag(FLAGZ objEnumFlag);
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
	
	public File getLogFileWatched();
	public void setLogFileWatched(File objFile);
}
