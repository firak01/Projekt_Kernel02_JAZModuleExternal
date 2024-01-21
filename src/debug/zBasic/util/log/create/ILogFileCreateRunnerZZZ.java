package debug.zBasic.util.log.create;

import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.log.watch.ILogFileWatchRunnerZZZ.FLAGZ;

public interface ILogFileCreateRunnerZZZ {
	public File getSourceFile();
	public void setSourceFile(File objSourceFile) ;

	public File getLogFile() ;
	public void setLogFile(File objLogFile);
	
	
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
