package basic.zKernel.status;

import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ.STATUSLOCAL;
import basic.zKernel.status.IEventObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.IEventObjectStatusLocalSetZZZ;

public interface IEventObject4LogFileWatchRunnerStatusLocalSetZZZ extends IEventObjectStatusLocalMessageSetZZZ{
	public ILogFileWatchRunnerZZZ.STATUSLOCAL getStatusEnum();
	String getStatusAbbreviation();
}
