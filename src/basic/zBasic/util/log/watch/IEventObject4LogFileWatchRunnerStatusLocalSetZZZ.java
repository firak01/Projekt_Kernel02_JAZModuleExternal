package basic.zBasic.util.log.watch;

import basic.zKernel.status.IEventObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.IEventObjectStatusLocalSetZZZ;

public interface IEventObject4LogFileWatchRunnerStatusLocalSetZZZ extends IEventObjectStatusLocalMessageSetZZZ{
	public ILogFileWatchRunnerZZZ.STATUSLOCAL getStatusEnum();
	String getStatusAbbreviation();
}
