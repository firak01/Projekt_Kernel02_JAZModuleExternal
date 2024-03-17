package basic.zKernel.status;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ.STATUSLOCAL;
import basic.zBasic.util.moduleExternal.process.watch.ProcessWatchRunnerZZZ;

/** 
 * Merke: Der gleiche "Design Pattern" wird auch im UI - Bereich fuer Komponenten verwendet ( package basic.zKernelUI.component.model; )  
 *        Dann erweitert die Event-Klasse aber EventObjekt.
 *  
 *  Merke2: Auch wenn hier nur normale Objekte verwendet weden, kann man in der FLAG-Verarbeitung bestimmt EventObject verwenden.
 *  
 * @author Fritz Lindhauer, 02.04.2023, 12:00:33  
 */
public class EventObject4ProcessWatchStatusLocalZZZ extends AbstractEventObjectStatusLocalZZZ implements IEventObject4ProcessRunnerStatusLocalZZZ{
	private static final long serialVersionUID = -5542596378774583040L;
	
	/** In dem Konstruktor wird neben der ID dieses Events auch der identifizierende Name der neu gewaehlten Komponente �bergeben.
	 * @param source
	 * @param iID
	 * @param sComponentItemText, z.B. fuer einen DirectoryJTree ist es der Pfad, fuer eine JCombobox der Name des ausgew�hlten Items 
	 * @throws ExceptionZZZ 
	 */
	public EventObject4ProcessWatchStatusLocalZZZ(Object source, String sStatusText, boolean bStatusValue) throws ExceptionZZZ {
		super(source, sStatusText, bStatusValue);		
	}
	
	public EventObject4ProcessWatchStatusLocalZZZ(Object source, ProcessWatchRunnerZZZ.STATUSLOCAL objStatusEnum, boolean bStatusValue) throws ExceptionZZZ {
		super(source, "", bStatusValue);		
		this.objStatusEnum=objStatusEnum;		
	}
		
	//### Aus Interface
	@Override
	public STATUSLOCAL getStatusEnum() {
		return (STATUSLOCAL) this.getStatusLocal();
	}
}

