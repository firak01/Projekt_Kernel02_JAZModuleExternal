package basic.zBasic.util.moduleExternal.monitor;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractList.ArrayListUniqueZZZ;
import basic.zKernel.status.IEventObjectStatusLocalSetZZZ;


/**Dieses Interface enthaelt Methoden, die von den Klassen implementiert werden muessen, 
 * die den Kernel eigenen Event verwalten sollen.
 * 
 * @author lindhaueradmin
 *
 */
public interface ISenderObjectStatusLocalSetZZZ{
	public abstract void fireEvent(IEventObjectStatusLocalSetZZZ event);
	public abstract IEventObjectStatusLocalSetZZZ getEventPrevious();
	public abstract void setEventPrevious(IEventObjectStatusLocalSetZZZ event);
	
	public abstract void removeListenerObjectStatusLocalSet(IListenerObjectStatusLocalSetZZZ objEventListener) throws ExceptionZZZ;
	public abstract void addListenerObjectStatusLocalSet(IListenerObjectStatusLocalSetZZZ objEventListener) throws ExceptionZZZ;
	public abstract ArrayListUniqueZZZ<IListenerObjectStatusLocalSetZZZ> getListenerRegisteredAll() throws ExceptionZZZ;
}
