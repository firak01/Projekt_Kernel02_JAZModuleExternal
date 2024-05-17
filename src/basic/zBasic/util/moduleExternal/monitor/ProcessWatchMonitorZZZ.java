package basic.zBasic.util.moduleExternal.monitor;

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
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;


/**This class watches the ServerMainZZZ-class and the ServerConnectionListenerRuner-objects.
 * This class runs in a seperate thread, so the TrayIcon stays "clickable", that means that clicking on the icon will be processed.
 * 
 * @author 0823
 *
 */
//public class ServerThreadProcessWatchMonitorOVPN extends AbstractKernelUseObjectWithStatusListeningCascadedZZZ implements IServerThreadProcessWatchMonitorOVPN, Runnable, IListenerObjectStatusLocalSetOVPN, IEventBrokerStatusLocalSetUserOVPN{
public class ProcessWatchMonitorZZZ extends AbstractProcessWatchMonitorZZZ {
	
	public ProcessWatchMonitorZZZ() throws ExceptionZZZ{
		super();				
	}
		
	public ProcessWatchMonitorZZZ(Process objProcess) throws ExceptionZZZ{
		super();				
		ProcessWatchMonitorNew_(objProcess);
	}
	
	public ProcessWatchMonitorZZZ(String[] saFlagControl) throws ExceptionZZZ{
		super(saFlagControl);	
		ProcessWatchMonitorNew_(null);
	}
	
	public ProcessWatchMonitorZZZ(Process objProcess, String[] saFlagControl) throws ExceptionZZZ{
		super(saFlagControl);	
		ProcessWatchMonitorNew_(objProcess);
	}

	private boolean ProcessWatchMonitorNew_(Process objProcess) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{	
			if(this.getFlag("init")) break main;
			
			this.setProcess(objProcess);
		}//end main:
		return bReturn;
	}

//	/* (non-Javadoc)
//	 * @see java.lang.Runnable#run()
//	 *TODO: Die Fehler ins Log-Schreiben.
//	 */
//	public void run() {		
//		main:{
//		try {
//			//if(this.getMainObject()==null) break main;
//				
//			String sLog = ReflectCodeZZZ.getPositionCurrent()+": Trying to use an external thread.";
//			System.out.println(sLog);
//			this.logProtocolString(sLog);
//		
//			//NUN DAS BACKEND-AUFRUFEN. Merke, dass muss in einem eigenen Thread geschehen, damit das Icon anclickbar bleibt.								
//			//Merke: Wenn über das enum der setStatusLocal gemacht wird, dann kann über das enum auch weiteres uebergeben werden. Z.B. StatusMeldungen.				
//			//besser ueber eine geworfenen Event... und nicht direkt: this.objMain.setStatusLocal(ClientMainOVPN.STATUSLOCAL.ISCONNECTING, true);
//			//this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTNO, false);
//			//boolean bStartNewGoon = this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTING, true);
//			boolean bStatusLocalSet = this.switchStatusLocalForGroupTo(IProcessWatchMonitorZZZ.STATUSLOCAL.ISSTARTING, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
//			if(!bStatusLocalSet) {
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
//				System.out.println(sLog);
//				this.logProtocolString(sLog);
//				break main;
//			}			
//			Thread.sleep(5000);
//					
//			TODOGOON:
//			//Vom Handling.. 
//			//Hole im Konsturktor die List der Processe
//			//Mache dann für jeden Process einen ProcessWatchrunnerZZZ
//			//und starte diesen einzeln...
//			
//			//Erst mal sehn, ob ueberhaupt was da ist.			
//			//ArrayList<ServerConfigStarterOVPN> listaProcessStarter = objServerMain.getServerConfigStarterList();
//			
//			//Vorbereitend: Process bereitstellen, der die Ausgabe des gestarteten Processes ueberwacht.
//			//              Das funktioniert beim Client per direktem Standard.out.
//			//              Beim Server aber nur per Beobachten des Log Files
//			ProcessWatchRunnerZZZ[] runneraProcessOVPN = null;
//			boolean bUseLogFileWatch = this.getFlag(IProcessWatchMonitorZZZ.FLAGZ.USE_LOGFILE_WATCHRUNNER); 
//			if(bUseLogFileWatch) {
//				//TODOGOON20240127;
//			}else {
//				//Die Liste der Processe einfach abholen.
//				//TODOGOON20240131;//Sie muss vorher gefüllt worden sein.
//				
//				runneraProcessOVPN = this.getProcessList(); new ProcessWatchRunnerZZZ[listaProcessStarter.size()];
//			}
//			//Nun fuer alle in ServerMain bereitgestellten Konfigurationen einen OpenVPN.exe - Process bereitstellen.
//			//Den passenden WatchRunner starten den Monitor Prozess daran registrieren.				
//			Thread[] threadaOVPN = new Thread[listaProcessStarter.size()];			
//			int iNumberOfProcessStarted = 0;
//			for(int icount=0; icount < listaProcessStarter.size(); icount++){
//				iNumberOfProcessStarted++;
//				ServerConfigStarterOVPN objStarter = (ServerConfigStarterOVPN) listaProcessStarter.get(icount);				
//				if(objStarter==null){
//					//Hier nicht abbrechen, sondern die Verarbeitung bei der naechsten Datei fortfuehren
//					sLog = ReflectCodeZZZ.getPositionCurrent()+": Unable to create process, using file: '"+ objStarter.getFileConfigOvpn().getPath()+"' for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size();
//					System.out.println(sLog);
//					this.getMainObject().logProtocolString(sLog);
//				}else {
//					Process objProcess = objStarter.requestStart();	
//					if(objProcess==null){
// 						//Hier nicht abbrechen, sondern die Verarbeitung bei der naechsten Datei fortfuehren
// 						sLog = ReflectCodeZZZ.getPositionCurrent()+": Unable to create process, using file: '"+ objStarter.getFileConfigOvpn().getPath()+"' for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size();
// 						System.out.println(sLog);
// 						this.getMainObject().logProtocolString(sLog); 						
// 					}else{	
//						//NEU: Einen anderen Thread zum "Monitoren" des Inputstreams des Processes verwenden. Dadurch werden die anderen Prozesse nicht angehalten.
//						sLog = ReflectCodeZZZ.getPositionCurrent()+": Successfull process created, using file: '"+ objStarter.getFileConfigOvpn().getPath()+"' for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() +". Starting Thread as Monitor for this process.";
//						System.out.println(sLog);
//						this.getMainObject().logProtocolString(sLog);
//							
//						//TEST, Flagübergabe: Ohne, z.B. die Pruefung auf vorherige Werte wird immer ein Event geworfen fuer "HASOUTPUT"
//						//runneraOVPN[icount] =new ProcessWatchRunnerOVPN(objKernel, objProcess,iNumberOfProcessStarted, IProcessWatchRunnerZZZ.FLAGZ.END_ON_CONNECTION.name());
//						//runneraOVPN[icount] =new ProcessWatchRunnerOVPN(objKernel, objProcess,iNumberOfProcessStarted);
//						//String[]saFlagControl = {IProcessWatchRunnerZZZ.FLAGZ.END_ON_CONNECTION.name(), IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_VALUE.name(), IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_VALUECHANGED.name()};
//						//String[]saFlagControl = {IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_VALUE.name(), IObjectWithStatusZZZ.FLAGZ.STATUSLOCAL_PROOF_VALUECHANGED.name()};
//						
//						if(bUseLogFileWatch) {
//							TODOGOON20240127;//Projekt einbinden
//							String[]saFlagControl = {ILogFileWatchRunnerZZZ.FLAGZ.END_ON_FILTER_FOUND.name()};
//							runneraProcessOVPN[icount] =new ProcessWatchRunnerOVPN(objKernel, objProcess,iNumberOfProcessStarted, saFlagControl);
//							
//							runneraProcessOVPN[icount].setServerBackendObject(this.getMainObject());
//							runneraProcessOVPN[icount].setServerConfigStarterObject(objStarter);
//							
//							//Wichtig, den ProcessWatchMonitorOVPN an den ProcessWatchRunnerOVPN Listener registrieren.
//							//Dafuer gibt es dann auch ein Mapping, in dem steht wie mit den empfangenen Events umgegangen wird, bzw. welche eigenen Events geworfen werden sollen.
//							runneraProcessOVPN[icount].registerForStatusLocalEvent((IListenerObjectStatusLocalSetOVPN)this);//Registriere den Monitor nun am ProcessWatchRunner
//							 						
//							threadaOVPN[icount] = new Thread(runneraProcessOVPN[icount]);//Starte den ProcessWatchRunner					
//							threadaOVPN[icount].start();	 	
//							sLog = ReflectCodeZZZ.getPositionCurrent()+": ProcessWatchRunner started for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() + ".";
//							this.getMainObject().logProtocolString(sLog);
//						}else {
//							String[]saFlagControl = {IProcessWatchRunnerZZZ.FLAGZ.END_ON_CONNECTION.name()};
//							runneraProcessOVPN[icount] =new ProcessWatchRunnerOVPN(objKernel, objProcess,iNumberOfProcessStarted, saFlagControl);
//							
//							runneraProcessOVPN[icount].setServerBackendObject(this.getMainObject());
//							runneraProcessOVPN[icount].setServerConfigStarterObject(objStarter);
//							
//							//Wichtig, den ProcessWatchMonitorOVPN an den ProcessWatchRunnerOVPN Listener registrieren.
//							//Dafuer gibt es dann auch ein Mapping, in dem steht wie mit den empfangenen Events umgegangen wird, bzw. welche eigenen Events geworfen werden sollen.
//							runneraProcessOVPN[icount].registerForStatusLocalEvent((IListenerObjectStatusLocalSetOVPN)this);//Registriere den Monitor nun am ProcessWatchRunner
//							 						
//							threadaOVPN[icount] = new Thread(runneraProcessOVPN[icount]);//Starte den ProcessWatchRunner					
//							threadaOVPN[icount].start();	 	
//							sLog = ReflectCodeZZZ.getPositionCurrent()+": ProcessWatchRunner started for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() + ".";
//							this.getMainObject().logProtocolString(sLog);
//						}
//						sLog = ReflectCodeZZZ.getPositionCurrent()+": Finished starting thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() + " for watching connection.";
//						System.out.println(sLog);
//						this.getMainObject().logProtocolString(sLog);
// 					}
//				}
//			}//END for
//			if(iNumberOfProcessStarted==0) {
//				//Hier nicht abbrechen, sondern den Status wieder zurücksetzen.
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": No process started.";
//				System.out.println(sLog);									
//				this.getMainObject().logProtocolString(sLog);
//				
//				bStatusLocalSet = this.switchStatusLocalForGroupTo(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTNO, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
//				if(!bStatusLocalSet) {
//					sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
//					System.out.println(sLog);
//					this.getMainObject().logProtocolString(sLog);
//					break main;
//				}			
//			}else if(iNumberOfProcessStarted>=1) {
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": " + iNumberOfProcessStarted + " process started.";
//				System.out.println(sLog);
//				this.getMainObject().logProtocolString(sLog);
//				
//				bStatusLocalSet = this.switchStatusLocalForGroupTo(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTED, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
//				if(!bStatusLocalSet) {
//					sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
//					System.out.println(sLog);
//					this.getMainObject().logProtocolString(sLog);
//					break main;
//				}	
//			}
//		} catch (ExceptionZZZ ez) {
//			System.out.println(ez.getDetailAllLast());
//		} catch (InterruptedException e) {			
//			e.printStackTrace();
//		}
//	}//END main:
//	
//	}//END run
	
			
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
					this.setStatusLocal(IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERFILTERFOUND, bStatusValue);
					
					
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
			hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.ISSTARTNEW, IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERSTARTNEW);
			hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.ISSTARTING, IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERSTARTING);
			hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.ISSTARTED, IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERSTARTED);
			hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.HASOUTPUT, IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNEROUTPUT);
			hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERFILTERFOUND);
			//hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASCONNECTIONLOST, IProgramRunnableMonitorZZZ.STATUSLOCAL.HASPROCESSCONNECTIONLOST);
			
			hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED, IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERSTOPPED);
			hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR, IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERERROR);
						
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
		hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED, "doStop");
		hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.HASERROR, "doStop");
		
		hmReturn.put(IProcessWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, "doFilterFound");
				
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
	public boolean queryReactOnStatusLocalEventCustom(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean queryOfferStatusLocalCustom() throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean queryReactOnStatusLocal4ActionCustom(String sActionAlias, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		return true;
	}
}//END class