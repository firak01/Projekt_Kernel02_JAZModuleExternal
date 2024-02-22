package basic.zKernel.status;

import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ.STATUSLOCAL;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;

public interface IEventObject4LogFileWatchRunnerStatusLocalZZZ extends IEventObjectStatusLocalZZZ{
	public ILogFileWatchRunnerZZZ.STATUSLOCAL getStatusEnum();
	String getStatusAbbreviation();
}
