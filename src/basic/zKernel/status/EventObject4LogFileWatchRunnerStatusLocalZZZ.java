package basic.zKernel.status;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ.STATUSLOCAL;


/** 
 * Merke: Der gleiche "Design Pattern" wird auch im UI - Bereich fuer Komponenten verwendet ( package basic.zKernelUI.component.model; )  
 *        Dann erweitert die Event-Klasse aber EventObjekt.
 *  
 *  Merke2: Auch wenn hier nur normale Objekte verwendet weden, kann man in der FLAG-Verarbeitung bestimmt EventObject verwenden.
 *  
 * @author Fritz Lindhauer, 02.04.2023, 12:00:33  
 */
public class EventObject4LogFileWatchRunnerStatusLocalZZZ  extends AbstractEventObjectStatusLocalZZZ implements IEventObject4LogFileWatchRunnerStatusLocalZZZ{
	private static final long serialVersionUID = -8093937392928650388L;

	/** In dem Konstruktor wird neben der ID dieses Events auch der identifizierende Name der neu gewaehlten Komponente �bergeben.
	 * @param source
	 * @param iID
	 * @param sComponentItemText, z.B. fuer einen DirectoryJTree ist es der Pfad, fuer eine JCombobox der Name des ausgew�hlten Items 
	 * @throws ExceptionZZZ 
	 */
	public EventObject4LogFileWatchRunnerStatusLocalZZZ(Object source,  String sEnumName, String sStatusText, boolean bStatusValue) throws ExceptionZZZ {
		super(source, sEnumName, sStatusText, bStatusValue);		
	}
	
	public EventObject4LogFileWatchRunnerStatusLocalZZZ(Object source,  Enum objEnum, String sStatusText, boolean bStatusValue) throws ExceptionZZZ {
		super(source,objEnum, sStatusText,bStatusValue);
	}
	
	public EventObject4LogFileWatchRunnerStatusLocalZZZ(Object source, STATUSLOCAL objStatusEnum, boolean bStatusValue) throws ExceptionZZZ {
		super(source,objStatusEnum,bStatusValue);		
	}

	//### Aus Interface
	@Override
	public STATUSLOCAL getStatusEnum() {
		return (STATUSLOCAL) this.getStatusLocal();
	}
}

