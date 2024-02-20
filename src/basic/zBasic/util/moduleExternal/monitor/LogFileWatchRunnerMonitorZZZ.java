package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramMonitorRunnablerZZZ;
import basic.zBasic.component.IProgramMonitorZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.abstractList.ArrayListUniqueZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.monitor.AbstractProcessWatchMonitorZZZ;
import basic.zKernel.AbstractKernelUseObjectWithStatusZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zKernel.flag.EventObjectFlagZsetZZZ;
import basic.zKernel.flag.IEventObjectFlagZsetZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.IEventObjectStatusBasicZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalMessageReactRunnableZZZ;


/**This class watches the ServerMainZZZ-class and the ServerConnectionListenerRuner-objects.
 * This class runs in a seperate thread, so the TrayIcon stays "clickable", that means that clicking on the icon will be processed.
 * 
 * @author 0823
 *
 */
public class LogFileWatchRunnerMonitorZZZ extends AbstractLogFileWatchRunnableMonitorZZZ {
	
	public LogFileWatchRunnerMonitorZZZ() throws ExceptionZZZ{
		super();				
	}
	
	public LogFileWatchRunnerMonitorZZZ(File objFile) throws ExceptionZZZ{
		super();				
		ProcessWatchMonitorNew_(objFile,null);
	}

	
	public LogFileWatchRunnerMonitorZZZ(String[] saFlagControl) throws ExceptionZZZ{
		super();		
		ProcessWatchMonitorNew_(null, saFlagControl);
	}

	private void ProcessWatchMonitorNew_(File objFile, String[] saFlagControl) throws ExceptionZZZ{
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
		
		this.setLogFile(objFile);
	}//END main
}
				
	//### Getter / Setter
		
	/**
	 * @param sStatusString
	 * @return
	 * @throws ExceptionZZZ
	 * @author Fritz Lindhauer, 23.10.2023, 11:48:47
	 */
	public boolean isStatusChanged(String sStatusString) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			if(sStatusString == null) {
				bReturn = this.getStatusLocalAbbreviation()==null;
				break main;
			}
			
			if(!sStatusString.equals(this.getStatusLocalAbbreviation())) {
				bReturn = true;
			}
		}//end main:
		if(bReturn) {
			String sLog = ReflectCodeZZZ.getPositionCurrent()+ ": Status changed to '"+sStatusString+"'";
			this.logProtocolString(sLog);		
		}
		return bReturn;
	}
	
	
	
	

	//###### FLAGS
	

	//###### STATUS		
	@Override
	public boolean isStatusLocalRelevant(IEnumSetMappedStatusZZZ objEnumStatusIn) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(objEnumStatusIn==null) break main;
			
			
			//Merke: enumStatus hat class='class use.openvpn.client.process.IProcessWatchRunnerOVPN$STATUSLOCAL'				
//			if(!(objEnum instanceof IServerMainOVPN.STATUSLOCAL) ){
//				String sLog = ReflectCodeZZZ.getPositionCurrent()+": enumStatus wird wg. unpassender Klasse ignoriert.";
//				System.out.println(sLog);
//				//this.objMain.logMessageString(sLog);
//				break main;
//		}	
			
			//Erst einmal ist jeder Status relevant
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	
	
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
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTARTNEW, IProgramMonitorZZZ.STATUSLOCAL.HASPROCESSSTARTNEW);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTARTING, IProgramMonitorZZZ.STATUSLOCAL.HASPROCESSSTARTING);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTARTED, IProgramMonitorZZZ.STATUSLOCAL.HASPROCESSSTARTED);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASOUTPUT, IProgramMonitorZZZ.STATUSLOCAL.HASPROCESSOUTPUT);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND, IProgramMonitorZZZ.STATUSLOCAL.HASPROCESSCONNECTION);
			//hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASCONNECTIONLOST, IProgramRunnableMonitorZZZ.STATUSLOCAL.HASPROCESSCONNECTIONLOST);
			
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED, IProgramMonitorZZZ.STATUSLOCAL.HASPROCESSSTOPPED);
			hmReturn.put(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR, IProgramMonitorZZZ.STATUSLOCAL.HASPROCESSERROR);
						
		}//end main:
		return hmReturn;	
	}

	@Override
	public boolean startCustom() throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			try {	
				String sLog = ReflectCodeZZZ.getPositionCurrent()+": Trying to use an external thread.";				
				this.logProtocolString(sLog);
			
				//NUN DAS BACKEND-AUFRUFEN. Merke, dass muss in einem eigenen Thread geschehen, damit das Icon anclickbar bleibt.								
				//Merke: Wenn über das enum der setStatusLocal gemacht wird, dann kann über das enum auch weiteres uebergeben werden. Z.B. StatusMeldungen.				
				//besser ueber eine geworfenen Event... und nicht direkt: this.objMain.setStatusLocal(ClientMainOVPN.STATUSLOCAL.ISCONNECTING, true);
				//this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTNO, false);
				//boolean bStartNewGoon = this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTING, true);
				boolean bStatusLocalSet = this.switchStatusLocalForGroupTo(IProcessWatchMonitorZZZ.STATUSLOCAL.ISSTARTING, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
				if(!bStatusLocalSet) {
					sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
					this.logProtocolString(sLog);
					break main;
				}			
				Thread.sleep(5000);
						
				
				//Vom Handling.. 
				//Hole im die List der ProcessXYZWatchRunnerZZZ 
				//und starte diesen einzeln...
				
				//Merke: Im OVPN Projekt wurde nun anhand der OVPN-Konfigurationen die Prozesse und eine Liste der Prozesse gebaut.
				//       siehe: objServerMain.getServerConfigStarterList();
				/*
				
				//Vorbereitend: Process bereitstellen, der die Ausgabe des gestarteten Processes ueberwacht.
				//              Das funktioniert beim Client per direktem Standard.out.
				//              Beim Server aber nur per Beobachten des Log Files
				ProcessWatchRunnerZZZ[] runneraProcessOVPN = null;
				boolean bUseLogFileWatch = this.getFlag(ILogFileWatchRunnerMonitorZZZ.FLAGZ.USE_LOGFILE_WATCHRUNNER); 
				if(bUseLogFileWatch) {
					//TODOGOON20240127;
				}else {
					//Die Liste der Processe einfach abholen.
					//TODOGOON20240131;//Sie muss vorher gefüllt worden sein.
					
					runneraProcessOVPN = this.getProcessList();
					new ProcessWatchRunnerZZZ[listaProcessStarter.size()];
				}
				
				 */
				
				
				//       DAS MUSS NUN VORHER PASSIEREN UND ENTSPRECHEND AN DEN MONITOR UEBERGEBEN WERDEN.
				
				//Erst mal sehn, was an Prozessen da ist.			
				ArrayList<IProgramRunnableZZZ> listaProcessStarter = this.getProgramRunnableList(); 
				
				
				//Nun fuer alle in ServerMain bereitgestellten Konfigurationen einen OpenVPN.exe - Process bereitstellen.
				//Den passenden WatchRunner starten den Monitor Prozess daran registrieren.				
				Thread[] threadaOVPN = new Thread[listaProcessStarter.size()];			
				int iNumberOfProcessStarted = 0;
				for(int icount=0; icount < listaProcessStarter.size(); icount++){
					iNumberOfProcessStarted++;
					IListenerObjectStatusLocalMessageReactRunnableZZZ objStarter = (IListenerObjectStatusLocalMessageReactRunnableZZZ) listaProcessStarter.get(icount);				
					if(objStarter==null){
						//Hier nicht abbrechen, sondern die Verarbeitung bei der naechsten Datei fortfuehren
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Null as program for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size();
						this.logProtocolString(sLog);
					}else {
						//Merke: In OVPN wird an dieser Stelle die ovpn.exe gestartet.
						//       Das soll hier nicht passieren. Statt dessen den erzeugten Prozess an an eine Klasse wie XYZ...WatchProcess...ReactRunnable... uebergeben. !!!
						//       s. Process objProcess = objStarter.requestStart();
						//          ....
						//       und dieses Objekt dann hier in der Liste als "Watcher" Starten. 

						sLog = ReflectCodeZZZ.getPositionCurrent()+": Program found for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() +". Requesting thread start.";
						this.logProtocolString(sLog);
						
						Thread objThreadStarter = new Thread(objStarter);
						threadaOVPN[iNumberOfProcessStarted]=objThreadStarter; //Vielleicht als globale Variable erreichbar machen?
						objThreadStarter.start();
						
						iNumberOfProcessStarted++;
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Finished starting thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() + ".";
						this.logProtocolString(sLog);
	 				}
				}//END for
				if(iNumberOfProcessStarted==0) {
					//Hier nicht abbrechen, sondern den Status wieder zurücksetzen.
					sLog = ReflectCodeZZZ.getPositionCurrent()+": No thread started.";										
					this.logProtocolString(sLog);
					
					bStatusLocalSet = this.switchStatusLocalForGroupTo(LogFileWatchRunnerMonitorZZZ.STATUSLOCAL.ISSTARTNO, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
					if(!bStatusLocalSet) {
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
						this.logProtocolString(sLog);
						break main;
					}			
				}else if(iNumberOfProcessStarted>=1) {
					sLog = ReflectCodeZZZ.getPositionCurrent()+": " + iNumberOfProcessStarted + " threads started.";
					this.logProtocolString(sLog);
					
					bStatusLocalSet = this.switchStatusLocalForGroupTo(LogFileWatchRunnerMonitorZZZ.STATUSLOCAL.ISSTARTED, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
					if(!bStatusLocalSet) {
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
						this.logProtocolString(sLog);
						break main;
					}	
				}
			} catch (ExceptionZZZ ez) {
				System.out.println(ez.getDetailAllLast());
			} catch (InterruptedException e) {			
				e.printStackTrace();
			}
		}//end main:
		return bReturn;
	}	
	
	@Override
	public boolean isEventRelevant2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet)	throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocal2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		System.out.println("TODOGOON 20240218");
		return false;
	}

	

}//END class