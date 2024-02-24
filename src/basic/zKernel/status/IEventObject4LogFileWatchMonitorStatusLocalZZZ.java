package basic.zKernel.status;

import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ;

public interface IEventObject4LogFileWatchMonitorStatusLocalZZZ extends IEventObjectStatusLocalZZZ{
	public ILogFileWatchMonitorZZZ.STATUSLOCAL getStatusEnum();
	String getStatusAbbreviation();
}
