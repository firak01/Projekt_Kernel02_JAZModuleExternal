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
public class EventObject4ProcessWatchStatusLocalSetZZZ extends AbstractEventObjectStatusLocalSetZZZ implements Comparable<IEventObjectStatusLocalSetZZZ>{
	private ProcessWatchRunnerZZZ.STATUSLOCAL objStatusEnum=null;	
	
	/** In dem Konstruktor wird neben der ID dieses Events auch der identifizierende Name der neu gewaehlten Komponente �bergeben.
	 * @param source
	 * @param iID
	 * @param sComponentItemText, z.B. fuer einen DirectoryJTree ist es der Pfad, fuer eine JCombobox der Name des ausgew�hlten Items 
	 */
	public EventObject4ProcessWatchStatusLocalSetZZZ(Object source, int iID,  String sStatusText, boolean bStatusValue) {
		super(source, iID, sStatusText, bStatusValue);		
		this.sStatusText = sStatusText;
		this.iId = iID;
		this.bStatusValue = bStatusValue;
	}
	
	public EventObject4ProcessWatchStatusLocalSetZZZ(Object source, int iID, ProcessWatchRunnerZZZ.STATUSLOCAL objStatusEnum, boolean bStatusValue) {
		super(source, iID, "", bStatusValue);		
		this.objStatusEnum=objStatusEnum;		
		this.iId = iID;
		this.bStatusValue = bStatusValue;
	}
		
	@Override
	public IEnumSetMappedStatusZZZ getStatusEnum() {
		return this.objStatusEnum;
	}
		
	@Override
	public String getStatusText(){
		if(this.objStatusEnum==null) {
			return this.sStatusText;
		}else {
			return this.objStatusEnum.name();
		}
	}
	

	
	//### Aus dem Interface Comparable
	@Override
	public int compareTo(IEventObjectStatusLocalSetZZZ o) {
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
	     if (!(aThat instanceof EventObject4ProcessWatchStatusLocalSetZZZ)) return false;
	     EventObject4ProcessWatchStatusLocalSetZZZ that = (EventObject4ProcessWatchStatusLocalSetZZZ)aThat;

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

