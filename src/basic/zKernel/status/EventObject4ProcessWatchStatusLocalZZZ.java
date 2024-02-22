package basic.zKernel.status;

import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.moduleExternal.process.watch.ProcessWatchRunnerZZZ;

/** 
 * Merke: Der gleiche "Design Pattern" wird auch im UI - Bereich fuer Komponenten verwendet ( package basic.zKernelUI.component.model; )  
 *        Dann erweitert die Event-Klasse aber EventObjekt.
 *  
 *  Merke2: Auch wenn hier nur normale Objekte verwendet weden, kann man in der FLAG-Verarbeitung bestimmt EventObject verwenden.
 *  
 * @author Fritz Lindhauer, 02.04.2023, 12:00:33  
 */
public class EventObject4ProcessWatchStatusLocalZZZ extends AbstractEventObjectStatusLocalZZZ implements Comparable<IEventObjectStatusLocalZZZ>{
	private static final long serialVersionUID = -5542596378774583040L;
	private ProcessWatchRunnerZZZ.STATUSLOCAL objStatusEnum=null;	
	
	/** In dem Konstruktor wird neben der ID dieses Events auch der identifizierende Name der neu gewaehlten Komponente �bergeben.
	 * @param source
	 * @param iID
	 * @param sComponentItemText, z.B. fuer einen DirectoryJTree ist es der Pfad, fuer eine JCombobox der Name des ausgew�hlten Items 
	 */
	public EventObject4ProcessWatchStatusLocalZZZ(Object source, String sStatusText, boolean bStatusValue) {
		super(source, sStatusText, bStatusValue);		
	}
	
	public EventObject4ProcessWatchStatusLocalZZZ(Object source, ProcessWatchRunnerZZZ.STATUSLOCAL objStatusEnum, boolean bStatusValue) {
		super(source, "", bStatusValue);		
		this.objStatusEnum=objStatusEnum;		
	}
		
	@Override
	public Enum getStatusEnum() {
		return this.objStatusEnum;
	}
	
	@Override
	public IEnumSetMappedStatusZZZ getStatusLocal() {
		return this.objStatusEnum;
	}
		
	@Override
	public String getStatusText(){
		if(this.objStatusEnum==null) {
			return this.getStatusMessage();
		}else {
			return this.objStatusEnum.name();
		}
	}
	

	
	//### Aus dem Interface Comparable
	@Override
	public int compareTo(IEventObjectStatusLocalZZZ o) {
		//Das macht lediglich .sort funktionsfähig und wird nicht bei .equals(...) verwendet.
		int iReturn = 0;
		main:{
			if(o==null)break main;
			
			String sTextToCompare = o.getStatusText();
			boolean bValueToCompare = o.getStatusValue();
			
			String sText = this.getStatusText();
			boolean bValue = this.getStatusValue();
			
			if(sTextToCompare.equals(sText) && bValueToCompare==bValue) iReturn = 1;		
			
		}
		return iReturn;
	}
	
	@Override 
	   public boolean equals(Object aThat) {
	     if (this == aThat) return true;
	     if (!(aThat instanceof EventObject4ProcessWatchStatusLocalZZZ)) return false;
	     EventObject4ProcessWatchStatusLocalZZZ that = (EventObject4ProcessWatchStatusLocalZZZ)aThat;

	     String sNameToCompare = that.getStatusEnum().name();
		 boolean bValueToCompare = that.getStatusValue();
			
		 String sName = this.getStatusEnum().name();
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

