package debug.zBasic.util.moduleExternal.log.create;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramWithFlagOnStatusListeningRunnableZZZ;
import basic.zBasic.component.IModuleZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.process.create.IProcessCreateRunnerZZZ;
import basic.zKernel.flag.IFlagZEnabledZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;

/**Diese Klasse erzeugt laaangsam, Zeile fuer Zeile eine Log-Datei.
 * Der Inhalt der Log-Datei kommt aus einer anderen Dummy-Log-Datei, die fest im Projekt als Beispiel vorliegt.
 * 
 * Dieser Runner spielt zusammen mit einem Runner, der die Log Datei Zeile für Zeile ausliest 
 * und dann auf bestimmte Eintraeg filtert.
 * 
 * 
 * @author fl86kyvo
 *
 */
public class LogFileCreateRunnerMockZZZ extends AbstractProgramWithFlagOnStatusListeningRunnableZZZ implements ILogFileCreateRunnerZZZ {
	private static final long serialVersionUID = 6586079955658760005L;
	private File objSourceFile = null;
	private File objLogFile=null;
	
	public LogFileCreateRunnerMockZZZ() throws ExceptionZZZ {
		super();		
	}

	public LogFileCreateRunnerMockZZZ(File objSourceFile, File objLogFile) throws ExceptionZZZ {
		super();	
		LogFileCreateMockRunnerNew_(null, objSourceFile, objLogFile, null);
	}
	
	public LogFileCreateRunnerMockZZZ(File objSourceFile, File objLogFile, String[] saFlag) throws ExceptionZZZ {
		super();	
		LogFileCreateMockRunnerNew_(null, objSourceFile, objLogFile, saFlag);
	}
		
	
	private boolean LogFileCreateMockRunnerNew_(IModuleZZZ objModule, File objSourceFile, File objLogFile, String[] saFlagControl) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{			
			if(saFlagControl != null){
				String stemp; boolean btemp;
				for(int iCount = 0;iCount<=saFlagControl.length-1;iCount++){
					stemp = saFlagControl[iCount];
					btemp = setFlag(stemp, true);
					if(btemp==false){ 								   
						   ExceptionZZZ ez = new ExceptionZZZ( stemp, IFlagZEnabledZZZ.iERROR_FLAG_UNAVAILABLE, this, ReflectCodeZZZ.getMethodCurrentName()); 						
						   throw ez;		 
					}
				}
				if(this.getFlag("init")) break main;
			}
			
			this.objSourceFile = objSourceFile;			
			this.objLogFile = objLogFile;
			
			this.objModule = objModule;
		}//end main:
		return bReturn;
	}
	
	

	//#### GETTER / SETTER
	@Override
	public File getSourceFile() {
		return this.objSourceFile;
	}

	@Override
	public void setSourceFile(File objSourceFile) {
		this.objSourceFile = objSourceFile;
	}
	
	@Override
	public File getLogFile() {
		return this.objLogFile;
	}

	@Override
	public void setLogFile(File objLogFile) {
		this.objLogFile = objLogFile;
	}
	
	@Override
	public boolean startCustom() throws ExceptionZZZ{
		boolean bReturn = false;
		main:{	
			
			//Lies die Datei Source Datei Zeile für Zeile aus.
			//und fülle damit die Ziel Datei
			bReturn = this.startLogFileCreateMockRunner();

		}//end main:
		return bReturn;
	}
	
	//Methode wird in der ReactionHashMap angegeben....
	public boolean doStop(IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			
			String sLog = ReflectCodeZZZ.getPositionCurrent() + "Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
			this.logProtocolString(sLog);
			
			bReturn = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP);
			if(bReturn) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName()+ "=> STOP FLAG SCHON GESETZT. Breche ab. Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
				this.logProtocolString(sLog);
				break main;
			}
			
			bReturn = this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, bStatusValue);
		}//end main
		return bReturn;
	}
	
	//Methode wird in der ReactionHashMap angegeben....
	public boolean doFilterFound(IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			
			String sLog = ReflectCodeZZZ.getPositionCurrent() + "Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
			this.logProtocolString(sLog);
			
			bReturn = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP);
			if(bReturn) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + this.getClass().getSimpleName()+ "=> STOP FLAG SCHON GESETZT. Breche ab. Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
				this.logProtocolString(sLog);
				break main;
			}
			
			
			if(bStatusValue) {//nur im true Fall
				if(this.getFlag(IWatchListenerZZZ.FLAGZ.END_ON_FILTER_FOUND)){
				   if(this.getFlag(IWatchListenerZZZ.FLAGZ.IMMEDIATE_END_ON_FILTER_FOUND)) {
					   System.exit(1);
				   }else {
					   bReturn = this.doStop(enumStatus,bStatusValue,sStatusMessage);
				   }												
				}
			}						
		}//end main
		return bReturn;
	}

	
	
	
	
	
	/** Ein Dummy-Logfile auslesen und langsam(!) Zeile fuer Zeile ausgeben
	 * @return
	 * @author Fritz Lindhauer, 10.12.2023, 16:04:55
	 */
	public boolean startLogFileCreateMockRunner() throws ExceptionZZZ{
		boolean bReturn= false;
		main:{			
			BufferedReader br=null;
			OutputStream objLogStream=null;
			try {
				//Die Quelldatei muss übergeben sein.
				File objFileSource = this.getSourceFile();
				if(objFileSource==null) {
					ExceptionZZZ ez = new ExceptionZZZ("Source File missing", iERROR_PROPERTY_MISSING, this);
					throw ez;
				}
				
				//Warte auf die Existenz der Quell-Datei.
				boolean bExists = false;
				do {
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP)) {
						String sLog = ReflectCodeZZZ.getPositionCurrent() + "Flag gesetzt: '" + IProgramRunnableZZZ.FLAGZ.REQUEST_STOP.name() + "'. Breche ab.";
						this.logProtocolString(sLog);
						break main;
					}
					bExists = FileEasyZZZ.exists(objFileSource);
					if(!bExists) {
						String sLog = ReflectCodeZZZ.getPositionCurrent() + "File not exists, waiting for: '" + objFileSource.getAbsolutePath() + "'.";
						this.logProtocolString(sLog);
						Thread.sleep(5000);
					}else {
						String sLog = ReflectCodeZZZ.getPositionCurrent() + "File exists: '" + objFileSource.getAbsolutePath() + "'.";
						this.logProtocolString(sLog);
					}
				}while(!bExists);
								
				InputStream objSourceStream = new FileInputStream(objFileSource);
				br = new BufferedReader(new InputStreamReader(objSourceStream));
				
				File objFileLog = this.getLogFile();
				if(objFileLog==null) {
					ExceptionZZZ ez = new ExceptionZZZ("Target File (as Log File) missing", iERROR_PROPERTY_MISSING, this);
					throw ez;
				}
				
				objLogStream = new FileOutputStream(objFileLog);
				
				//Merke: Der Ansatz mit dem Buffered Writer funktioniert nicht,
				//       da die Zeilen nicht sofort da sind, sondern vermutlich erst wenn der Stream geschlossen wird.
				//OutputStreamWriter oswriter = new OutputStreamWriter(objLogStream);				
				//BufferedWriter writer = new BufferedWriter(oswriter);
				
				
				String sLine = null;
				int icount=0;
                while (true){
                	Thread.sleep(300); //Bremse zum Debuggen ab. Sonst gehen mir die Zeilen aus... ;-))
                	if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP)) {
                		String sLog = ReflectCodeZZZ.getPositionCurrent() + "Flag gesetzt: '" + IProgramRunnableZZZ.FLAGZ.REQUEST_STOP.name() + "'. Breche ab.";
						this.logProtocolString(sLog);
    					break main;
    				}
                    sLine = br.readLine();
                    if(sLine!=null)
                    {
                    	icount++;          
                    	String sLog = ReflectCodeZZZ.getPositionCurrent() + icount +"\t: " + sLine;
                    	this.logProtocolString(sLog);
                    	objLogStream.write(sLine.getBytes());
                    	objLogStream.write(StringZZZ.crlf().getBytes());//Merke: Ohne diese explizite neue Zeile wird alles hintereinander geschrieben.
                    	
                    	//verwende statt dessen den direkten FileOutputStream. Er schreibt die Daten sofort.
                    	//Das merkt man, wenn man das Program einfach anhaelt.
                    	//Dann sind die Datein beim BufferedWriter noch nicht da.
                    	//writer.write(sLine);
                    	//writer.newLine();
                    	//oswriter.append(sLine);
                    	//Merke: Beim Buffered Writer erst den Stream schliessen, damit die Daten da sind.
                    	
                    }else{
                    	//Warte auf weiter Ausgaben
                        Thread.sleep(300);
                    }
                }								
			} catch (InterruptedException e) {				
				e.printStackTrace();				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();								
			} finally {
				if(br!=null) {
					IOUtils.closeQuietly(br);
				}
				if(objLogStream!=null) {
					IOUtils.closeQuietly(objLogStream);
				}
	        }
		}//end main:
		return bReturn;
	}
		
	//###############################
	//### FLAG HANDLING
	//###############################
	@Override
	public boolean getFlag(ILogFileCreateRunnerZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(ILogFileCreateRunnerZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(ILogFileCreateRunnerZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileCreateRunnerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(ILogFileCreateRunnerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileCreateRunnerZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	//##############################################
	//### Aus IListenerObjectStatusLocalZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	//##############################################
		
	@Override
	public boolean isEventRelevant2ChangeStatusLocalByClass(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevant2ChangeStatusLocalByStatusLocalValue(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	
	@Override
	public boolean reactOnStatusLocal4ActionCustom(String sAction, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(!bStatusValue)break main;
			
			String sLog;
	
			//TODO Idee: Per Reflection API die so genannte Methode aufrufen... aber dann sollte das Event-Objekt als Parameter mit uebergeben werden.
			if(!StringZZZ.isEmpty(sAction)) {
				switch(sAction) {
				case "doStop":
					bReturn = this.doStop(enumStatus, bStatusValue, sStatusMessage);	
					break;
				case "doFilterFound":
					bReturn = this.doFilterFound(enumStatus, bStatusValue, sStatusMessage);		
					break;
				default:
					sLog = ReflectCodeZZZ.getPositionCurrent() + "ActionAlias wird noch nicht behandelt. '" + sAction + "'";
					this.logProtocolString(sLog);
				}
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Kein ActionAlias ermittelt. Fuehre keine Aktion aus.";
				this.logProtocolString(sLog);
			}
	
	}//end main:
	return bReturn;	
	}
	
	@Override
	public HashMap<IEnumSetMappedStatusZZZ, String> createHashMapStatusLocal4ReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//Merke: Bei der "direkten" verbindung zwischen creator und watchRunner ohne Monitor arbeiten...
		//       Mit Monitor wuerden soch die STATUSLOCAL Werte auf den Monitor beziehen.
		//Reagiere nur auf den "Filter" gefunden Event
		hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, "doFilterFound");
		
		//und den "Runner beendet" Event, bzw. Fehler
		hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED, "doStop");
		hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR, "doStop");
		
		
		return hmReturn;
		
	}
	
	//###############################
	//### FLAG HANDLING AUS: IWatchListenerZZZ
	//###############################
	@Override
	public boolean getFlag(IWatchListenerZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(IWatchListenerZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(IWatchListenerZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isNull(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(IWatchListenerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(IWatchListenerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(IWatchListenerZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean queryReactOnStatusLocalEventCustom(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean queryReactOnStatusLocal4ActionCustom(String sActionAlias, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		return true;
	}

	
}
