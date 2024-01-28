package basic.zBasic.util.moduleExternal.monitor;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zKernel.AbstractKernelUseObjectWithStatusListeningCascadedZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zKernel.status.IEventObjectStatusLocalMessageSetZZZ;

public abstract class AbstractProcessWatchMonitorZZZ  extends AbstractKernelUseObjectWithStatusListeningCascadedZZZ implements IProcessWatchMonitorZZZ, Runnable, IEventObjectStatusLocalMessageSetZZZ{
	
	public AbstractProcessWatchMonitorZZZ() throws ExceptionZZZ{
		super();
	}

	public AbstractProcessWatchMonitorZZZ(IKernelZZZ objKernel) throws ExceptionZZZ{
		super(objKernel);
	}
	
	@Override
	public boolean getFlag(basic.zBasic.util.moduleExternal.monitor.IProcessWatchMonitorZZZ.FLAGZ objEnumFlag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setFlag(basic.zBasic.util.moduleExternal.monitor.IProcessWatchMonitorZZZ.FLAGZ objEnumFlag,
			boolean bFlagValue) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean[] setFlag(basic.zBasic.util.moduleExternal.monitor.IProcessWatchMonitorZZZ.FLAGZ[] objaEnumFlag,
			boolean bFlagValue) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean proofFlagExists(basic.zBasic.util.moduleExternal.monitor.IProcessWatchMonitorZZZ.FLAGZ objEnumFlag)
			throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean proofFlagSetBefore(
			basic.zBasic.util.moduleExternal.monitor.IProcessWatchMonitorZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getStatusMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStatusMessage(String sStatusMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IEnumSetMappedStatusZZZ getStatusEnum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatusText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getStatusValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getProcessID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
