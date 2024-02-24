package basic.zKernel.status;

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
	private STATUSLOCAL objStatusEnum=null;
	                                             
	//Merke: Diese Strings sind wichtig für das Interface und kommen nicht aus der abstrakten Klasse
	private String sStatusAbbreviation=null;
	private String sStatusMessage=null;
	
	/** In dem Konstruktor wird neben der ID dieses Events auch der identifizierende Name der neu gewaehlten Komponente �bergeben.
	 * @param source
	 * @param iID
	 * @param sComponentItemText, z.B. fuer einen DirectoryJTree ist es der Pfad, fuer eine JCombobox der Name des ausgew�hlten Items 
	 */
	public EventObject4LogFileWatchMonitorStatusLocalZZZ(Object source,  String sStatusText, boolean bStatusValue) {
		super(source,sStatusText,bStatusValue);		
	}
	
	public EventObject4LogFileWatchMonitorStatusLocalZZZ(Object source,  String sStatusAbbreviation, String sStatusText, boolean bStatusValue) {
		super(source,sStatusText,bStatusValue);
		this.sStatusAbbreviation = sStatusAbbreviation;
	}
	
	public EventObject4LogFileWatchMonitorStatusLocalZZZ(Object source, STATUSLOCAL objStatusEnum, boolean bStatusValue) {
		super(source,"",bStatusValue);
		this.objStatusEnum=objStatusEnum;
	}
	
	//### Aus Interface
	/* (non-Javadoc)
	 * @see basic.zKernel.status.AbstractEventObjectStatusLocalSetZZZ#getStatusEnum()
	 */
	@Override
	public ILogFileWatchMonitorZZZ.STATUSLOCAL getStatusEnum() {
		return this.objStatusEnum;
	}
	
	@Override
	public IEnumSetMappedStatusZZZ getStatusLocal() {
		return this.objStatusEnum;
	}
	
	/* (non-Javadoc)
	 * @see use.openvpn.client.status.IEventObjectStatusLocalSetOVPN#getStatusAbbreviation()
	 */
	@Override
	public String getStatusAbbreviation(){
		if(this.objStatusEnum==null) {
			return this.sStatusAbbreviation;
		}else {
			return this.objStatusEnum.getAbbreviation();
		}
	}
	
	@Override
	public String getStatusText(){
		if(this.objStatusEnum==null) {
			return this.sStatusMessage;
		}else {
			return this.objStatusEnum.name();
		}
	}

	@Override
	public String getStatusMessage(){
		if(this.objStatusEnum==null) {
			return this.sStatusMessage;
		}else {
			return this.objStatusEnum.getStatusMessage();
		}
	}

	
	//### Aus dem Interface Comparable	
	@Override 
   public boolean equals(Object aThat) {
     if (this == aThat) return true;
     if (!(aThat instanceof EventObject4LogFileWatchMonitorStatusLocalZZZ)) return false;
     EventObject4LogFileWatchMonitorStatusLocalZZZ that = (EventObject4LogFileWatchMonitorStatusLocalZZZ)aThat;
     
     String sNameToCompare = that.getStatusEnum().getName();
	 boolean bValueToCompare = that.getStatusValue();
		
	 String sName = this.getStatusEnum().getName();
	boolean bValue = this.getStatusValue();
     
	if(sNameToCompare.equals(sName) && bValueToCompare==bValue) return true;
		
     return false;     
   }

   /** A class that overrides equals must also override hashCode.*/
   @Override 
   public int hashCode() {
	   return this.getStatusText().hashCode();
   }
}

