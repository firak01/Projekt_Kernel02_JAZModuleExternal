package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.EnumSet;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.AbstractProgramMonitorRunnableZZZ;
import basic.zBasic.component.IProgramMonitorZZZ;
import basic.zBasic.component.IProgramMonitorZZZ.FLAGZ;
import basic.zBasic.component.IProgramMonitorZZZ.STATUSLOCAL;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;


public interface ILogFileWatchMonitorRunnableZZZ extends ILogFileWatchMonitorZZZ, Runnable {
	public File getLogFile();
	public void setLogFile(File objFile);
	
	
	//#############################################################
	//### FLAGZ
	//#############################################################
	public enum FLAGZ{
		DUMMY,REQUEST_STOP,END_ON_FILTER_FOUND
	}
		
	boolean getFlag(FLAGZ objEnumFlag);
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
	

}
