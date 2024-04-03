package basic.zBasic.util.moduleExternal;

import basic.zBasic.ExceptionZZZ;

public interface IWatchListenerZZZ {
	
	
	//##############################################################	
	
	public enum FLAGZ{
		DUMMY,END_ON_FILTER_FOUND,IMMIDIATE_END_ON_FILTER_FOUND
	}
	boolean getFlag(FLAGZ objEnumFlag);
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
	
}