package basic.zBasic.util.moduleExternal.monitor;

import java.util.EventListener;

import basic.zBasic.ExceptionZZZ;
import basic.zKernel.status.IEventObjectStatusLocalSetZZZ;

public interface IListenerObjectStatusLocalSetZZZ extends EventListener{
	public boolean changeStatusLocal(IEventObjectStatusLocalSetZZZ eventStatusLocalSet) throws ExceptionZZZ;
	public boolean isEventRelevant(IEventObjectStatusLocalSetZZZ eventStatusLocalSet) throws ExceptionZZZ;
	public boolean isEventRelevantByClass(IEventObjectStatusLocalSetZZZ eventStatusLocalSet) throws ExceptionZZZ;
	public boolean isEventRelevantByStatusLocal(IEventObjectStatusLocalSetZZZ eventStatusLocalSet) throws ExceptionZZZ;
	public boolean isEventRelevantByStatusLocalValue(IEventObjectStatusLocalSetZZZ eventStatusLocalSet) throws ExceptionZZZ;
}
