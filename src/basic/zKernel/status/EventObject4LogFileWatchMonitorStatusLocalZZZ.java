package basic.zKernel.status;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ;
import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ.STATUSLOCAL;

/** 
 * Merke: Der gleiche "Design Pattern" wird auch im UI - Bereich fuer Komponenten verwendet ( package basic.zKernelUI.component.model; )  
 *        Dann erweitert die Event-Klasse aber EventObjekt.
 *  
 *  Merke2: Auch wenn hier nur normale Objekte verwendet weden, kann man in der FLAG-Verarbeitung bestimmt EventObject verwenden.
 *  
 * @author Fritz Lindhauer, 02.04.2023, 12:00:33  
 */
public class EventObject4LogFileWatchMonitorStatusLocalZZZ  extends AbstractEventObjectStatusLocalZZZ implements IEventObject4LogFileWatchMonitorStatusLocalZZZ{
	private static final long serialVersionUID = 3358073038419016272L;

	/** In dem Konstruktor wird neben der ID dieses Events auch der identifizierende Name der neu gewaehlten Komponente �bergeben.
	 * @param source
	 * @param iID
	 * @param sComponentItemText, z.B. fuer einen DirectoryJTree ist es der Pfad, fuer eine JCombobox der Name des ausgew�hlten Items 
	 * @throws ExceptionZZZ 
	 */
	public EventObject4LogFileWatchMonitorStatusLocalZZZ(Object source,  String sStatusText, boolean bStatusValue) throws ExceptionZZZ {
		super(source,sStatusText,bStatusValue);		
	}
	
	public EventObject4LogFileWatchMonitorStatusLocalZZZ(Object source,  String sStatusAbbreviation, String sStatusText, boolean bStatusValue) throws ExceptionZZZ {
		super(source,sStatusText,bStatusValue);
	}
	
	public EventObject4LogFileWatchMonitorStatusLocalZZZ(Object source, STATUSLOCAL objStatusEnum, boolean bStatusValue) throws ExceptionZZZ {
		super(source,objStatusEnum,bStatusValue);
	}
	
	//### Aus Interface
	@Override
	public STATUSLOCAL getStatusEnum() {
		return (STATUSLOCAL) this.objStatusEnum;
	}	
}

