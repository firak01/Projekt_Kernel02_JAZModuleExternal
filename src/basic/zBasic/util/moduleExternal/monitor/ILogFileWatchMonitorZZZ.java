package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.EnumSet;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.AbstractProgramMonitorRunnableZZZ;
import basic.zBasic.component.IProgramMonitorZZZ;
import basic.zBasic.component.IProgramMonitorZZZ.FLAGZ;
import basic.zBasic.component.IProgramMonitorZZZ.STATUSLOCAL;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;


public interface ILogFileWatchMonitorZZZ{
	public File getLogFile();
	public void setLogFile(File objFile);
	
	
	//#############################################################
	//### FLAGZ
	//#############################################################
	public enum FLAGZ{
		DUMMY
	}
		
	boolean getFlag(FLAGZ objEnumFlag);
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
	
	//#######################################################################################
	// STATUS	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//Die StatusId für Stati, aus dieser Klasse selbst. Nicht die Stati der anderen Klassen.
	//Sollte dann irgendwie einzigartig sein.
	public static int iSTATUSLOCAL_GROUPID=1;
		
	//++++++++++++++++++++++++
	//ALIAS(Gruppenid der Meldung, "Uniquename","Statusmeldung","Beschreibung, wird nicht genutzt....",)
	public enum STATUSLOCAL implements IEnumSetMappedStatusZZZ{//Folgendes geht nicht, da alle Enums schon von einer Java BasisKlasse erben... extends EnumSetMappedBaseZZZ{
		ISSTARTNEW(iSTATUSLOCAL_GROUPID,"isstartnew","ZZZ: LogFileWatchMonitor nicht gestartet",""),
		ISSTARTING(iSTATUSLOCAL_GROUPID,"isstarting","ZZZ: LogFileWatchMonitor startet...",""),		
		ISSTARTED(iSTATUSLOCAL_GROUPID,"isstarted","ZZZ: LogFileWatchMonitor gestartet",""),
		ISSTARTNO(iSTATUSLOCAL_GROUPID,"isstartno","ZZZ: LogFileWatchMonitor nicht gestartet",""),

		HASLOGFILEWATCHRUNNERSTARTNEW(iSTATUSLOCAL_GROUPID,"haslogfilewatchrunnerstartnew","ZZZ: LogFileWatchRunner nicht gestartet",""),
		HASLOGFILEWATCHRUNNERSTARTING(iSTATUSLOCAL_GROUPID,"haslogfilewatchrunnerstarting","ZZZ: LogFileWatchRunner startet",""),
		HASLOGFILEWATCHRUNNERSTARTED(iSTATUSLOCAL_GROUPID,"haslogfilewatchrunnerstarted","ZZZ: LogFileWatchRunner gestartet",""),
		HASLOGFILEWATCHRUNNEROUTPUT(iSTATUSLOCAL_GROUPID,"haslogfilewatchrunneroutput","ZZZ: LogFileWatchRunner mit Ausgabe",""),
		
		HASLOGFILEWATCHRUNNERFILTERFOUND(iSTATUSLOCAL_GROUPID,"haslogfilewatchrunnerfilterfound","ZZZ: LogFileWatchRunner hat Filterwert gefunden",""),
		
		HASLOGFILEWATCHRUNNERERROR(iSTATUSLOCAL_GROUPID,"haslogfilewatchrunnererror","ZZZ: LogFileWatchRunner meldet Fehler",""),
		HASLOGFILEWATCHRUNNERSTOPPED(iSTATUSLOCAL_GROUPID,"haslogfilewatchrunnerstopped","ZZZ: LogFileWatchRunner gestoppt",""),
		
		ISSTOPPED(iSTATUSLOCAL_GROUPID,"isstopped","OVPN: Monitor beendet",""),
				
		HASERROR(iSTATUSLOCAL_GROUPID,"haserror","ZZZ: LogFileWatchMonitor meldet Fehler","");		
		
		private int iStatusGroupId;
		private String sAbbreviation,sStatusMessage,sDescription;
	
		//#############################################
		//#### Konstruktoren
		//Merke: Enums haben keinen public Konstruktor, können also nicht intiantiiert werden, z.B. durch Java-Reflektion.
		//In der Util-Klasse habe ich aber einen Workaround gefunden.
		STATUSLOCAL(int iStatusGroupId, String sAbbreviation, String sStatusMessage, String sDescription) {
			this.iStatusGroupId = iStatusGroupId;
		    this.sAbbreviation = sAbbreviation;
		    this.sStatusMessage = sStatusMessage;
		    this.sDescription = sDescription;
		}
	
		public int getStatusGroupId() {
			return this.iStatusGroupId;
		}
		
		public String getAbbreviation() {
		 return this.sAbbreviation;
		}
		
		public String getStatusMessage() {
			 return this.sStatusMessage;
		}
		
		public EnumSet<?>getEnumSetUsed(){
			return STATUSLOCAL.getEnumSet();
		}
	
		/* Die in dieser Methode verwendete Klasse für den ...TypeZZZ muss immer angepasst werden. */
		@SuppressWarnings("rawtypes")
		public static <E> EnumSet getEnumSet() {
			
		 //Merke: Das wird anders behandelt als FLAGZ Enumeration.
			//String sFilterName = "FLAGZ"; /
			//...
			//ArrayList<Class<?>> listEmbedded = ReflectClassZZZ.getEmbeddedClasses(this.getClass(), sFilterName);
			
			//Erstelle nun ein EnumSet, speziell für diese Klasse, basierend auf  allen Enumrations  dieser Klasse.
			Class<STATUSLOCAL> enumClass = STATUSLOCAL.class;
			EnumSet<STATUSLOCAL> set = EnumSet.noneOf(enumClass);//Erstelle ein leeres EnumSet
			
			for(Object obj : AbstractProgramMonitorRunnableZZZ.class.getEnumConstants()){
				//System.out.println(obj + "; "+obj.getClass().getName());
				set.add((STATUSLOCAL) obj);
			}
			return set;
			
		}
	
		//TODO: Mal ausprobieren was das bringt
		//Convert Enumeration to a Set/List
		private static <E extends Enum<E>>EnumSet<E> toEnumSet(Class<E> enumClass,long vector){
			  EnumSet<E> set=EnumSet.noneOf(enumClass);
			  long mask=1;
			  for (  E e : enumClass.getEnumConstants()) {
			    if ((mask & vector) == mask) {
			      set.add(e);
			    }
			    mask<<=1;
			  }
			  return set;
			}
	
		//+++ Das könnte auch in einer Utility-Klasse sein.
		//the valueOfMethod <--- Translating from DB
		public static STATUSLOCAL fromAbbreviation(String s) {
		for (STATUSLOCAL state : values()) {
		   if (s.equals(state.getAbbreviation()))
		       return state;
		}
		throw new IllegalArgumentException("Not a correct abbreviation: " + s);
		}
	
		//##################################################
		//#### Folgende Methoden bring Enumeration von Hause aus mit. 
				//Merke: Diese Methoden können aber nicht in eine abstrakte Klasse verschoben werden, zum daraus Erben. Grund: Enum erweitert schon eine Klasse.
		@Override
		public String getName() {	
			return super.name();
		}
	
		@Override
		public String toString() {//Mehrere Werte mit # abtennen
		    return this.sAbbreviation+"="+this.sDescription;
		}
	
		@Override
		public int getIndex() {
			return ordinal();
		}
	
		//### Folgende Methoden sind zum komfortablen Arbeiten gedacht.
		@Override
		public int getPosition() {
			return getIndex()+1; 
		}
	
		@Override
		public String getDescription() {
			return this.sDescription;
		}
		//+++++++++++++++++++++++++
	}//End internal Class
}
