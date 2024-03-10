package basic.zBasic.util.moduleExternal.process.create;

import java.io.File;
import java.util.EnumSet;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.moduleExternal.ICreateRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ.FLAGZ;
import basic.zBasic.util.moduleExternal.process.watch.AbstractProcessWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ.STATUSLOCAL;

public interface IProcessCreateRunnerZZZ extends ICreateRunnerZZZ{
	public File getSourceFile();
	public void setSourceFile(File objSourceFile) ;

	public Process getProcess() ;
	public void setProcess(Process objProcess);
	
	public Process createProcessByBatchCustom() throws ExceptionZZZ;  //Erzeuge den Process	
	public boolean controlProcessByBatchCustom() throws ExceptionZZZ; //Beobachte Flags und versuche den Process zu steuern, bzw. in per Batch zu KILLEN.
	
	public void debugWriteOutputToLogPLUSanalyse() throws ExceptionZZZ;
	public void debugWriteOutputToLogPLUSanalyse(Process objProcess) throws ExceptionZZZ;
	public boolean analyseInputLineCustom(String sLine) throws ExceptionZZZ;
	
	
	//##############################################################	
	public enum FLAGZ{
		DUMMY
	}

	boolean getFlag(FLAGZ objEnumFlag);
	boolean setFlag(FLAGZ objEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean[] setFlag(FLAGZ[] objaEnumFlag, boolean bFlagValue) throws ExceptionZZZ;
	boolean proofFlagExists(FLAGZ objEnumFlag) throws ExceptionZZZ;
	boolean proofFlagSetBefore(FLAGZ objEnumFlag) throws ExceptionZZZ;
	
	
	//##############################################################
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//Die StatusId für Stati, aus dieser Klasse selbst. Nicht die Stati der anderen Klassen.
	public static int iSTATUSLOCAL_GROUPID=0;
		
	//++++++++++++++++++++++++
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//DIE INTERNE ENUM-KLASSE FUER STATUSLOCAL.
	//Merke1: Diese wird auch vererbt. So dass erbende Klassen auf dieses Enum ueber ihren eingene Klassennamen zugreifen können.
	//
	//Merke2: Diese könnte auch in eine extra Klasse ausgelagert werden (z.B. um es in einer Datenbank mit Hibernate zu persistieren.
	//       Für die Auslagerung als extra Klasse, s.: EnumSetMappedTestTypeZZZ
	//++++++++++++++++++++++++
			
	
	//Merke: Obwohl fullName und abbr nicht direkt abgefragt werden, müssen Sie im Konstruktor sein, um die Enumeration so zu definieren.
	//ALIAS("Uniquename","Statusmeldung","Beschreibung, wird nicht genutzt....",)
	public enum STATUSLOCAL implements IEnumSetMappedStatusZZZ{//Folgendes geht nicht, da alle Enums schon von einer Java BasisKlasse erben... extends EnumSetMappedBaseZZZ{	
		ISSTARTED(iSTATUSLOCAL_GROUPID,"isstarted","ProcessCreateRunner",""),
		HASCONNECTION(iSTATUSLOCAL_GROUPID,"hasconnection","ProcessWatchRunner ist mit dem Process verbunden",""),
		HASOUTPUT(iSTATUSLOCAL_GROUPID,"hasoutput","Prozess hat Output",""),
		HASINPUT(iSTATUSLOCAL_GROUPID,"hasinput","Prozess hat Input",""),
		ISSTOPPED(iSTATUSLOCAL_GROUPID,"isended","ProcessCreateRunner ist beendet",""),
		HASERROR(iSTATUSLOCAL_GROUPID,"haserror","Ein Fehler ist aufgetreten","");
			
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
			
			//Merke: In einer anderen Klasse, die dieses DesingPattern nutzt, befinden sich die Enums in einer anderen Klasse
			for(Object obj : AbstractProcessWatchRunnerZZZ.class.getEnumConstants()){
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
	}//End internal Enum Class
}
