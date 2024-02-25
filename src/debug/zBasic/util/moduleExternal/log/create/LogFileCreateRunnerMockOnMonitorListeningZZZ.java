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
import basic.zBasic.component.IProgramMonitorZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchMonitorStatusLocalZZZ;
import basic.zKernel.status.IEventObjectStatusBasicZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.IListenerObjectStatusBasicZZZ;

/**Diese Klasse erzeugt laaangsam, Zeile fuer Zeile eine Log-Datei.
 * Der Inhalt der Log-Datei kommt aus einer anderen Dummy-Log-Datei, die fest im Projekt als Beispiel vorliegt.
 * 
 * Dieser Runner spielt zusammen mit einem Runner, der die Log Datei Zeile für Zeile ausliest 
 * und dann auf bestimmte Eintraeg filtert. 
 * 
 * ABER: Er registriert sich an einem Monitor und hört auch nur auf die Events von dem Monitor.
 *    
 * @author fl86kyvo
 *
 */
public class LogFileCreateRunnerMockOnMonitorListeningZZZ extends AbstractProgramWithFlagOnStatusListeningRunnableZZZ implements ILogFileCreateRunnerOnMonitorListeningZZZ {
	private static final long serialVersionUID = 6586079955658760005L;
	private File objSourceFile = null;
	private File objLogFile=null;
	
	public LogFileCreateRunnerMockOnMonitorListeningZZZ() throws ExceptionZZZ {
		super();		
	}

	public LogFileCreateRunnerMockOnMonitorListeningZZZ(File objSourceFile, File objLogFile) throws ExceptionZZZ {
		super();	
		LogFileCreateMockRunnerNew_(null, objSourceFile, objLogFile, null);
	}
	
	public LogFileCreateRunnerMockOnMonitorListeningZZZ(File objSourceFile, File objLogFile, String[] saFlag) throws ExceptionZZZ {
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
						   ExceptionZZZ ez = new ExceptionZZZ( stemp, IFlagZUserZZZ.iERROR_FLAG_UNAVAILABLE, this, ReflectCodeZZZ.getMethodCurrentName()); 						
						   throw ez;		 
					}
				}
				if(this.getFlag("init")) break main;
			}
			
			this.objModule = objModule;
			this.objSourceFile= objSourceFile;
			this.objLogFile = objLogFile;
		}//end main:
		return bReturn;
	}
	
	
	//Methode wird in der ReactionHashMap angegeben....
	public boolean doStop(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(eventStatusLocal==null) break main;
			
			String sLog = ReflectCodeZZZ.getPositionCurrent() + "EventMessage: " + eventStatusLocal.getStatusMessage();
			this.logProtocolString(sLog);
			
			bReturn = this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);
		}//end main
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
				
				//Warte auf die Existenz der Datei.
				File objFileSource = this.getSourceFile();
				boolean bExists = false;
				do {
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
						break main;
					}
					bExists = FileEasyZZZ.exists(objFileSource);
					if(!bExists) {
						System.out.println("File not exists, waiting for: '" + objFileSource.getAbsolutePath() + "'.");
						Thread.sleep(5000);
					}
				}while(!bExists);
								
				InputStream objSourceStream = new FileInputStream(objFileSource);
				br = new BufferedReader(new InputStreamReader(objSourceStream));
				
				File objFileLog = this.getLogFile();
				objLogStream = new FileOutputStream(objFileLog);
				
				//Merke: Der Ansatz mit dem Buffered Writer funktioniert nicht,
				//       da die Zeilen nicht sofort da sind, sondern vermutlich erst wenn der Stream geschlossen wird.
				//OutputStreamWriter oswriter = new OutputStreamWriter(objLogStream);				
				//BufferedWriter writer = new BufferedWriter(oswriter);
				
				
				String sLine = null;
				int icount=0;
                while (true){
                	Thread.sleep(300); //Bremse zum Debuggen ab. Sonst gehen mir die Zeilen aus... ;-))
                	if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
    					break main;
    				}
                    sLine = br.readLine();
                    if(sLine!=null)
                    {
                    	icount++;                    	
                    	System.out.println(ReflectCodeZZZ.getPositionCurrent() + ": " + icount +"\t: " + sLine);
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
//			catch (InterruptedException e) {					
//					try {
//						String sLog = e.getMessage();
//						this.logLineDate("An error happend: '" + sLog + "'");
//					} catch (ExceptionZZZ e1) {
//						System.out.println(e1.getDetailAllLast());
//						e1.printStackTrace();
//					}
//					System.out.println(e.getMessage());
//					e.printStackTrace();
//				}
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
	public boolean getFlag(ILogFileCreateRunnerOnMonitorListeningZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(ILogFileCreateRunnerOnMonitorListeningZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(ILogFileCreateRunnerOnMonitorListeningZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileCreateRunnerOnMonitorListeningZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(ILogFileCreateRunnerOnMonitorListeningZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileCreateRunnerOnMonitorListeningZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	//### aus IListenerObjectStatusLocalMessageReactZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		String sLog=null;
		
		main:{
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Einen Event von einem Objekt, an dem registriert worden ist empfangen.";
			this.logProtocolString(sLog);
			
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Event="+eventStatusLocal.toString();
			this.logProtocolString(sLog);
			
			boolean bEventRelevant = this.isEventRelevant(eventStatusLocal);
			if(!bEventRelevant) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Event ist NICHT relevant.";
				this.logProtocolString(sLog);
				break main;
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Event ist relevant.";
				this.logProtocolString(sLog);
			}
			
			String sAction = null;
			if(eventStatusLocal instanceof IEventObject4LogFileWatchMonitorStatusLocalZZZ) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Relevanter Event vom Monitor!!!";
				this.logProtocolString(sLog);
				
				if(eventStatusLocal.getStatusEnum().equals(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASERROR)){
					sLog = ReflectCodeZZZ.getPositionCurrent() + "Es gibt im Monitor einen Fehler.";
					this.logProtocolString(sLog);
					
					sAction = (String) this.getHashMapStatusLocalReaction().get(eventStatusLocal.getStatusLocal());
					
				}else if(eventStatusLocal.getStatusEnum().equals(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTOPPED)){
					sLog = ReflectCodeZZZ.getPositionCurrent() + "Monitor stoppt.";
					this.logProtocolString(sLog);
					
				}else {
					sLog = ReflectCodeZZZ.getPositionCurrent() + "Monitor Event nicht beachtet. Sollte nicht in der Reaction HashMap sein.";
					this.logProtocolString(sLog);
					
				}				
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "instanceof Event nicht behandelt. ("+ eventStatusLocal.getClass().getName() + ").";
				this.logProtocolString(sLog);
			}
			
			
			//TODO Idee: Per Reflection API die so genannte Methode aufrufen... aber dann sollte das Event-Objekt als Parameter mit uebergeben werden.
			switch(sAction) {
			case "doStop":
				bReturn = doStop(eventStatusLocal);	
				break;
			default:
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Kein ActionAlias ermittelt. Fuehre keine Aktion aus.";
				this.logProtocolString(sLog);
			}
		
		}//end main:
		return bReturn;		
	}


	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	@Override
	public HashMap<IEnumSetMappedStatusZZZ, String> createHashMapStatusLocalReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//Reagiere auf diee Events... mit dem angegebenen Alias.
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTOPPED, "doStop");
		hmReturn.put(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASERROR, "doStop");
			
		return hmReturn;
	}
}
