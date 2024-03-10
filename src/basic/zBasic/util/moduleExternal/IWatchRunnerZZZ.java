package basic.zBasic.util.moduleExternal;

import basic.zBasic.ExceptionZZZ;

public interface IWatchRunnerZZZ {
	public String getLineFilter();
	public void setLineFilter(String sLineFilter);
	
	//##############################################################	
	
	public enum FLAGZ{
		DUMMY,REQUEST_STOP,END_ON_FILTER_FOUND
	}
	boolean getFlag(FLAGZ objEnumFlag);
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
	
}
