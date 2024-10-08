package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.component.IProgramZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.IWatchListenerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;


/* Monitor Klasse, der selbst NICHT in einem eigenen Thread (NICHT runnable) laeuft.
 */
public class LogFileWatchMonitorZZZ extends AbstractLogFileWatchMonitorZZZ {
	private static final long serialVersionUID = 8209025974705509709L;

	public LogFileWatchMonitorZZZ() throws ExceptionZZZ{
		super();				
	}
	
	public LogFileWatchMonitorZZZ(File objFile) throws ExceptionZZZ{
		super();				
		ProcessWatchMonitorNew_(objFile);
	}

	
	public LogFileWatchMonitorZZZ(String[] saFlagControl) throws ExceptionZZZ{
		super(saFlagControl);		
		ProcessWatchMonitorNew_(null);
	}
	
	public LogFileWatchMonitorZZZ(File objFile, String[] saFlagControl) throws ExceptionZZZ{
		super(saFlagControl);		
		ProcessWatchMonitorNew_(objFile);
	}

	private boolean ProcessWatchMonitorNew_(File objFile) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{	
			if(this.getFlag("init")) break main;
			
			this.setLogFile(objFile);
		}//end main:
		return bReturn;
	}
				
	@Override
	public boolean startCustom() throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			try {	
				String sLog = ReflectCodeZZZ.getPositionCurrent()+"Starting Monitor, switching Status of Monitor.";				
				this.logProtocolString(sLog);
			
				//NUN DAS BACKEND-AUFRUFEN. Merke, dass muss in einem eigenen Thread geschehen, damit das Icon anclickbar bleibt.								
				//Merke: Wenn über das enum der setStatusLocal gemacht wird, dann kann über das enum auch weiteres uebergeben werden. Z.B. StatusMeldungen.				
				//besser ueber eine geworfenen Event... und nicht direkt: this.objMain.setStatusLocal(ClientMainOVPN.STATUSLOCAL.ISCONNECTING, true);
				//this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTNO, false);
				//boolean bStartNewGoon = this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTING, true);
				boolean bStatusLocalSet = this.switchStatusLocalForGroupTo(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTARTING, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
				if(!bStatusLocalSet) {
					sLog = ReflectCodeZZZ.getPositionCurrent()+"Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
					this.logProtocolString(sLog);
					break main;
				}			
				Thread.sleep(5000);
						
				//Merke: Im OVPN Projekt wurde nun anhand der OVPN-Konfigurationen die Prozesse und eine Liste der Prozesse gebaut.
				//       siehe: objServerMain.getServerConfigStarterList();

				//Vom Handling.. 
				//Hole im die List der Programme und starte diesen einzeln...
				//Merke: Je nach Programtyp erzeugt dann das Program beim Start für sich ggfs. einen eigenen Thread
				ArrayList<IProgramZZZ> listaProcessStarter = this.getProgramList(); 
				
				int iNumberOfProcessStarted=0;
				for(int icount=0; icount < listaProcessStarter.size(); icount++){
					iNumberOfProcessStarted++;
					IProgramZZZ objProgram = (IProgramZZZ) listaProcessStarter.get(icount);				
					if(objProgram==null){
						//Hier nicht abbrechen, sondern die Verarbeitung bei der naechsten Datei fortfuehren
						sLog = ReflectCodeZZZ.getPositionCurrent()+"Null as program for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size();
						this.logProtocolString(sLog);
					}else {						
						sLog = ReflectCodeZZZ.getPositionCurrent()+"Program found for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() +". Requesting thread start.";
						this.logProtocolString(sLog);
						
						objProgram.start(); //das hat eine doppelte Funktion. a) Einfache Programme werden gestartet. b) Runnable Programme werden im eigenen Thread gestartet
						
						sLog = ReflectCodeZZZ.getPositionCurrent()+"Finished starting program #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() + ".";
						this.logProtocolString(sLog);
	 				}
				}//END for
				if(iNumberOfProcessStarted==0) {
					//Hier nicht abbrechen, sondern den Status wieder zurücksetzen.
					sLog = ReflectCodeZZZ.getPositionCurrent()+"No program started.";										
					this.logProtocolString(sLog);
					
					bStatusLocalSet = this.switchStatusLocalForGroupTo(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTARTNO, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
					if(!bStatusLocalSet) {
						sLog = ReflectCodeZZZ.getPositionCurrent()+"Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
						this.logProtocolString(sLog);
						break main;
					}			
				}else if(iNumberOfProcessStarted>=1) {
					sLog = ReflectCodeZZZ.getPositionCurrent() + iNumberOfProcessStarted + " programs started.";
					this.logProtocolString(sLog);
					
					bStatusLocalSet = this.switchStatusLocalForGroupTo(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTARTED, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
					if(!bStatusLocalSet) {
						sLog = ReflectCodeZZZ.getPositionCurrent()+"Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
						this.logProtocolString(sLog);
						break main;
					}	
				}
			} catch (InterruptedException e) {								
				e.printStackTrace();
				this.setStatusLocal(ILogFileWatchMonitorZZZ.STATUSLOCAL.HASERROR,true);
				String sLog = ReflectCodeZZZ.getPositionCurrent() + "HASERROR Status gesetzt.";
				this.logLineDate(sLog);
			} finally {
				
	        }
		}//end main:
		return bReturn;
	}	
	
	//Methode wird in der ReactionHashMap angegeben....
		public boolean doStop(IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				
				String sLog = ReflectCodeZZZ.getPositionCurrent() + "Status='"+enumStatus.getName() +"', StatusValue="+bStatusValue+", EventMessage='" + sStatusMessage +"'";
				this.logProtocolString(sLog);
				
				sLog = ReflectCodeZZZ.getPositionCurrent() + "DOSTOP!!!";
				this.logProtocolString(sLog);
				
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
	
	
	
	//### Getter / Setter
		
	
	//###### FLAGS
	

	//###########################################################
	//### STATUS
	//###########################################################
			
		
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	@Override
	//Weil auf den Status anderer Thread gehoert wird und diese weitergeleitet werden sollen.
	public HashMap<IEnumSetMappedStatusZZZ, IEnumSetMappedStatusZZZ> createHashMapEnumSetForCascadingStatusLocalCustom() {
		
		//Es wird auf Events des ProcessWatchRunnerOVPN gehoert.
		//Die dort geworfenen Events werden hier auf LokaleEvents gemappt.
		//Aufbau der Map: Ankommender externer Event = Lokaler Event
		//Lokale Events, die keine externe Entsprechung haben, tauchen hier nicht auf
		HashMap<IEnumSetMappedStatusZZZ,IEnumSetMappedStatusZZZ>hmReturn = new HashMap<IEnumSetMappedStatusZZZ,IEnumSetMappedStatusZZZ>();
		main:{
			
			//Merke: Reine Lokale Statuswerte kommen nicht aus einem Event und werden daher nicht gemapped. 			
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTARTNEW, ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERSTARTNEW);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTARTING, ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERSTARTING);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTARTED, ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERSTARTED);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASOUTPUT, ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNEROUTPUT);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERFILTERFOUND);
			//hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASCONNECTIONLOST, IProgramRunnableMonitorZZZ.STATUSLOCAL.HASPROCESSCONNECTIONLOST);
			
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED, ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERSTOPPED);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR, ILogFileWatchMonitorZZZ.STATUSLOCAL.HASLOGFILEWATCHRUNNERERROR);
						
		}//end main:
		return hmReturn;	
	}
	
	@Override
	public boolean isEventRelevant2ChangeStatusLocalByClass(IEventObjectStatusLocalZZZ eventStatusLocalSet)	throws ExceptionZZZ {
		return true;
	}
	
	@Override
	public boolean isEventRelevant2ChangeStatusLocalByStatusLocalValue(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}


	@Override
	public HashMap createHashMapStatusLocal4ReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//Reagiere auf diee Events... mit dem angegebenen Alias.
		hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED, "doStop");
		hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR, "doStop");
		
		hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, "doFilterFound");
				
		return hmReturn;
	}

	@Override
	public boolean reactOnStatusLocal4ActionCustom(String sAction, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(!bStatusValue) break main;
			
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
	public boolean queryOfferStatusLocalCustom() throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean queryReactOnStatusLocalEventCustom(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean queryReactOnStatusLocal4ActionCustom(String sActionAlias, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		return true;
	}
}//END class