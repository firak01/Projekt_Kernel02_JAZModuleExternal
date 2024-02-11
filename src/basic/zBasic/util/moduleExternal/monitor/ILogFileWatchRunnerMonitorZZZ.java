package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.EnumSet;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.IProgramRunnableMonitorZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;


public interface ILogFileWatchRunnerMonitorZZZ extends IProgramRunnableMonitorZZZ{
	public File getLogFile();
	public void setLogFile(File objFile);
	
	
	
}
