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
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ.STATUSLOCAL;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.IEventObjectStatusBasicZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.IListenerObjectStatusBasicZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalZZZ;

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
public class LogFileCreateMockRunnerZZZ extends AbstractProgramWithFlagOnStatusListeningRunnableZZZ implements ILogFileCreateRunnerZZZ {
	private static final long serialVersionUID = 6586079955658760005L;
	private File objSourceFile = null;
	private File objLogFile=null;
	
	public LogFileCreateMockRunnerZZZ() throws ExceptionZZZ {
		super();		
	}

	public LogFileCreateMockRunnerZZZ(File objSourceFile, File objLogFile) throws ExceptionZZZ {
		super();	
		LogFileCreateMockRunnerNew_(null, objSourceFile, objLogFile, null);
	}
	
	public LogFileCreateMockRunnerZZZ(File objSourceFile, File objLogFile, String[] saFlag) throws ExceptionZZZ {
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
	
	
	//Methode wird in der ReactionHashMap angegeben....
	public boolean doFilterFound(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(eventStatusLocal==null) break main;
			
			String sLog = ReflectCodeZZZ.getPositionCurrent() + "EventMessage: " + eventStatusLocal.getStatusMessage();
			this.logProtocolString(sLog);
			
			if(this.getFlag(ILogFileCreateRunnerZZZ.FLAGZ.END_ON_FILTERFOUND)) {
				bReturn = this.doStop(eventStatusLocal);
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
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
						String sLog = "Flag gesetzt: '" + IProgramRunnableZZZ.FLAGZ.REQUESTSTOP.name() + "'. Breche ab.";
						this.logProtocolString(sLog);
						break main;
					}
					bExists = FileEasyZZZ.exists(objFileSource);
					if(!bExists) {
						String sLog = "File not exists, waiting for: '" + objFileSource.getAbsolutePath() + "'.";
						this.logProtocolString(sLog);
						Thread.sleep(5000);
					}else {
						String sLog = "File exists: '" + objFileSource.getAbsolutePath() + "'.";
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
                	if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
                		String sLog = "Flag gesetzt: '" + IProgramRunnableZZZ.FLAGZ.REQUESTSTOP.name() + "'. Breche ab.";
						this.logProtocolString(sLog);
    					break main;
    				}
                    sLine = br.readLine();
                    if(sLine!=null)
                    {
                    	icount++;          
                    	String sLog = ReflectCodeZZZ.getPositionCurrent() + ": " + icount +"\t: " + sLine;
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
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
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

	//### aus IListenerObjectStatusLocalMessageReactZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	/* (non-Javadoc)
	 * @see basic.zBasic.component.AbstractProgramWithFlagRunnableOnStatusMessageListeningZZZ#reactOnStatusLocalEvent(basic.zKernel.status.IEventObjectStatusBasicZZZ)
	 */
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
//		boolean bReturn = false;
//		String sLog=null;
//		
//		main:{
//			sLog = ReflectCodeZZZ.getPositionCurrent() + ": Filter gefunden und mache den changeStatusLocal Event.";
//			this.logProtocolString(sLog);
//			
//			if(eventStatusLocal instanceof IEventObjectStatusLocalZZZ) {// .getClass().getSimpleName().equals("LogFileCreateMockRunnerZZZ")) {
//				IEventObjectStatusLocalZZZ event = (IEventObjectStatusLocalZZZ) eventStatusLocal;
//				boolean bStatusValue = event.getStatusValue();
//				if(bStatusValue!=true) break main;
//				
//				if(this.getFlag(ILogFileCreateRunnerZZZ.FLAGZ.END_ON_FILTERFOUND)) {
//					sLog = ReflectCodeZZZ.getPositionCurrent() + ": Filter gefunden und END_ON_FILTERFOUND gesetzt. Beende Schleife.";
//					this.logProtocolString(sLog);
//					
//					this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);								
//				}
//				
//				
//			}else {
//				sLog = ReflectCodeZZZ.getPositionCurrent() + ": Event wird nicht behandelt, instanceof " + eventStatusLocal.getClass().getName();
//				this.logProtocolString(sLog);
//			}			
//		}//end main:
//		return bReturn;	
//		
//		
//		
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
			
			String sAction = (String) this.getHashMapStatusLocalReaction().get(eventStatusLocal.getStatusLocal());
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Gefundenen Action: '" + sAction + "'";
			this.logProtocolString(sLog);
			
			//TODO Idee: Per Reflection API die so genannte Methode aufrufen... aber dann sollte das Event-Objekt als Parameter mit uebergeben werden.
			switch(sAction) {
			case "doStop":
				bReturn = doStop(eventStatusLocal);	
				break;
			case "doFilterFound":
				bReturn = doFilterFound(eventStatusLocal);	
				break;
			default:
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Kein ActionAlias ermittelt. Fuehre keine Aktion aus.";
				this.logProtocolString(sLog);
			}
					
		}//end main:
		return bReturn;		
	}
	
	/* (non-Javadoc)
	 * @see basic.zKernel.status.IListenerObjectStatusLocalZZZ#reactOnStatusLocalEvent(basic.zKernel.status.IEventObjectStatusLocalZZZ)
	 */
//	@Override
//	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
//		boolean bReturn=false;
//		main:{	
//			if(eventStatusLocal==null)break main;
//						
//			String sLog = ReflectCodeZZZ.getPositionCurrent()+": Fuer LogFileWatchEvent.";
//			this.logLineDate(sLog);
//			
//			if(eventStatusLocal instanceof IEventObjectStatusLocalMessageZZZ) {
//				
//				boolean bRelevant = this.isEventRelevant((IEventObjectStatusLocalZZZ) eventStatusLocal); 
//				if(!bRelevant) {
//					sLog = 	ReflectCodeZZZ.getPositionCurrent() + ": Event / Status nicht relevant. Breche ab.";
//					this.logProtocolString(sLog);
//					break main;
//				}
//				
//				IEventObjectStatusLocalMessageZZZ eventStatusLocalSet = (IEventObjectStatusLocalMessageZZZ) eventStatusLocal;
//				IEnumSetMappedZZZ enumStatus = eventStatusLocalSet.getStatusLocal();				
//				
//				
//			
//				//Weiter Daten holen... im OVPN - Projekt z.B. die IPAdresse...
//			
////+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//			
//			//+++++++++++++++++++++
//			//Falls der empfangene Status in einen anderen, eigenen, lokalen Status uebersetzt werden muss.
////			HashMap<IEnumSetMappedStatusZZZ,IEnumSetMappedStatusZZZ>hmEnum = this.getHashMapEnumSetForCascadingStatusLocal();
////			IClientMainOVPN.STATUSLOCAL objEnum = (IClientMainOVPN.STATUSLOCAL) hmEnum.get(enumStatus);			
////			if(objEnum==null) {
////				sLog = ReflectCodeZZZ.getPositionCurrent()+": Keinen gemappten Status aus dem Event-Objekt erhalten. Breche ab";
////				System.out.println(sLog);
////				this.logLineDate(sLog);
////				break main;
////			}
//			
//			//Wenn wir den Status nicht weitersenden, sondern direkt verarbeiten ist solch ein Mapping nicht notwendig.
//			//Also da wir mit objEnum als Variable weiterarbeiten wollen:
//			ILogFileWatchRunnerZZZ.STATUSLOCAL objEnum = (STATUSLOCAL) enumStatus;
//			//++++++++++++++++++++##
//			
//			boolean bValue = eventStatusLocalSet.getStatusValue();
//			
//			
//			//Merke: Ein Interface ILogFileWatchListenerExampleZZZ mit dem STATUSLOCAL ist nicht implementiert.
////			boolean bHasError = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASERROR)&& bValue;
////			boolean bEnded = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTOPPED) && bValue;
////			boolean bHasConnection = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTION) && bValue;
////			boolean bHasConnectionLost = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTIONLOST) && bValue;
//		
//			boolean bEventHasError = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR)&& bValue;
//			boolean bEventEnded = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED)&& bValue;
//			
//			//boolean bEventHasConnection = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTED);
//			//boolean bEventHasConnectionLost = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTINTERUPTED);
//			
//			
//			boolean bEventHasFilterFound = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND)&& bValue;
//				
//			//int iIndex = eventStatusLocalSet.getProcessID();
//			String sStatusMessage = eventStatusLocalSet.getStatusMessage();	
//			sLog = ReflectCodeZZZ.getPositionCurrent() + ": StatusMessage ist = '" + sStatusMessage + "'";
//			this.logProtocolString(sLog);
//			
//
//			//Einen Status Anzunehmen ist hier nicht implementiert			
////			boolean bStatusLocalSet = this.offerStatusLocal(iIndex, objEnum, sStatusMessage, bValue);//Es wird ein Event gefeuert, an dem das Tray-Objekt und andere registriert sind und dann sich passend einstellen kann.
////			if(!bStatusLocalSet) {
////				sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
////				System.out.println(sLog);
////				this.logLineDate(sLog);
////				break main;
////			}
//			//++++++++++++++
//			
//			//Die Stati vom Monitor-Objekt mit dem Backend-Objekt mappen
//			//if(IClientThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPRO==objStatusEnum) {
//			//	this.setStatusLocal(IClientMainOVPN.STATUSLOCAL.ISCONNECTING, eventStatusLocalSet.getStatusValue());				
//			//}else if(IClientThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISCONNECTED==objStatusEnum) {
//			
//			if(bEventHasError && bEventEnded){
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status bEventHasError && bEventEnded";
//				this.logLineDate(sLog);					
//			}else if((!bEventHasError) && bEventEnded){
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status !bEventHasError && bEventEnded";
//				this.logLineDate(sLog);
//				
//			}
//		
//			
//			}//end if instanceof ...MessageSetZZZ
//			bReturn = true;
//		}//end main:
//		return bReturn;
//	}

	
	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalReact) throws ExceptionZZZ {
		return true;
	}

//	@Override
//	public boolean isEventRelevant(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
//		boolean bReturn = false;
//		main:{
//			
//			if(!this.isEventRelevant2ChangeStatusLocal(eventStatusLocal)) break main;
//			if(!this.isEventRelevantByClass2ChangeStatusLocal(eventStatusLocal)) break main;
//			if(!this.isEventRelevantByReactionHashMap2ChangeStatusLocal(eventStatusLocal)) break main;
//			if(!this.isEventRelevantByStatusLocalValue2ChangeStatusLocal(eventStatusLocal)) break main;
//			
//			bReturn = true;
//		}//end main:
//		return bReturn;
//	}

	@Override
	public HashMap createHashMapStatusLocalReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		main:{
			
		}//end main:
		return hmReturn;
		
	}
}
