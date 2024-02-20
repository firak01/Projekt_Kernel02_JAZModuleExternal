package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.EnumSet;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.IProgramMonitorZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;


public interface ILogFileWatchRunnerMonitorZZZ extends IProgramMonitorZZZ{
	public File getLogFile();
	public void setLogFile(File objFile);
	
	
	
}
