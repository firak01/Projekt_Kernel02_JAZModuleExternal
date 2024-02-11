package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramRunnableMonitorZZZ;
import basic.zBasic.component.IProgramRunnableMonitorZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.abstractList.ArrayListUniqueZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
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
	public void run() {		
		main:{
		try {	
			String sLog = ReflectCodeZZZ.getPositionCurrent()+": Trying to use an external thread.";
			System.out.println(sLog);
			this.logProtocolString(sLog);
		
			//NUN DAS BACKEND-AUFRUFEN. Merke, dass muss in einem eigenen Thread geschehen, damit das Icon anclickbar bleibt.								
			//Merke: Wenn über das enum der setStatusLocal gemacht wird, dann kann über das enum auch weiteres uebergeben werden. Z.B. StatusMeldungen.				
			//besser ueber eine geworfenen Event... und nicht direkt: this.objMain.setStatusLocal(ClientMainOVPN.STATUSLOCAL.ISCONNECTING, true);
			//this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTNO, false);
			//boolean bStartNewGoon = this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTING, true);
			boolean bStatusLocalSet = this.switchStatusLocalForGroupTo(IProcessWatchMonitorZZZ.STATUSLOCAL.ISSTARTING, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
			if(!bStatusLocalSet) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}			
			Thread.sleep(5000);
					
			TODOGOON20240210;
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
			ArrayList<IListenerObjectStatusLocalMessageReactRunnableZZZ> listaProcessStarter = this.getProgramRunnableList(); 
			
			
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
					System.out.println(sLog);
					this.logProtocolString(sLog);
				}else {
					//Merke: In OVPN wird an dieser Stelle die ovpn.exe gestartet.
					//       Das soll hier nicht passieren. Statt dessen den erzeugten Prozess an an eine Klasse wie XYZ...WatchProcess...ReactRunnable... uebergeben. !!!
					//       s. Process objProcess = objStarter.requestStart();
					//          ....
					//       und dieses Objekt dann hier in der Liste als "Watcher" Starten. 

					sLog = ReflectCodeZZZ.getPositionCurrent()+": Program found for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() +". Requesting thread start.";
					System.out.println(sLog);
					this.logProtocolString(sLog);
							
					iNumberOfProcessStarted++;
					sLog = ReflectCodeZZZ.getPositionCurrent()+": Finished starting thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() + ".";
					System.out.println(sLog);
					this.logProtocolString(sLog);
 				}
			}//END for
			if(iNumberOfProcessStarted==0) {
				//Hier nicht abbrechen, sondern den Status wieder zurücksetzen.
				sLog = ReflectCodeZZZ.getPositionCurrent()+": No thread started.";
				System.out.println(sLog);									
				this.logProtocolString(sLog);
				
				bStatusLocalSet = this.switchStatusLocalForGroupTo(LogFileWatchRunnerMonitorZZZ.STATUSLOCAL.ISSTARTNO, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
				if(!bStatusLocalSet) {
					sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
					System.out.println(sLog);
					this.logProtocolString(sLog);
					break main;
				}			
			}else if(iNumberOfProcessStarted>=1) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": " + iNumberOfProcessStarted + " threads started.";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				
				bStatusLocalSet = this.switchStatusLocalForGroupTo(LogFileWatchRunnerMonitorZZZ.STATUSLOCAL.ISSTARTED, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
				if(!bStatusLocalSet) {
					sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
					System.out.println(sLog);
					this.logProtocolString(sLog);
					break main;
				}	
			}
		} catch (ExceptionZZZ ez) {
			System.out.println(ez.getDetailAllLast());
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}//END main:
	
	}//END run
	
			
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
	
	
	
//	//### aus ISenderObjectStatusLocalSetUserOVPN
//	@Override
//	public ISenderObjectStatusLocalSetOVPN getSenderStatusLocalUsed() throws ExceptionZZZ {
//		return this.objServerMain.getSenderStatusLocalUsed();
//	}
//
//	@Override
//	public void setSenderStatusLocalUsed(ISenderObjectStatusLocalSetOVPN objEventSender) {
//		this.objServerMain.setSenderStatusLocalUsed(objEventSender);
//	}
	

	/* (non-Javadoc)
	 * @see basic.zBasic.AbstractObjectWithStatusZZZ#setStatusLocal(java.lang.Enum, java.lang.String, boolean)
	 */
	@Override
	public boolean offerStatusLocal(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			
			IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal_(-1, enumStatus, sStatusMessage, bStatusValue);				
		}//end main;
		return bFunction;
	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see use.openvpn.server.status.IListenerObjectStatusLocalSetOVPN#statusLocalChanged(use.openvpn.server.status.IEventObjectStatusLocalSetOVPN)
	 */
	@Override
	public boolean changeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		//Der Monitor ist am ProcessWatchRunner registriert.
		//Wenn ein Event geworfen wird, dann reagiert er darauf, hiermit....
		boolean bReturn = false;
		main:{
			if(eventStatusLocalSet==null)break main;
			
			String sLog = ReflectCodeZZZ.getPositionCurrent() + ": Event gefangen.";
			System.out.println(sLog);
			this.logProtocolString(sLog);
			
			boolean bRelevant = this.isEventRelevant(eventStatusLocalSet); 
			if(!bRelevant) {
				sLog = 	ReflectCodeZZZ.getPositionCurrent() + ": Event / Status nicht relevant. Breche ab.";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}
			
			//+++ Mappe nun die eingehenden Status-Enums auf die eigenen.
			IEnumSetMappedStatusZZZ enumStatus = eventStatusLocalSet.getStatusLocal();
			if(enumStatus==null) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Keinen Status aus dem Event-Objekt erhalten. Breche ab";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}
			
			//+++++++++++++++++++++
			HashMap<IEnumSetMappedStatusZZZ,IEnumSetMappedStatusZZZ>hmEnum = this.getHashMapEnumSetForCascadingStatusLocal();
			if(hmEnum==null) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Keine Mapping Hashmap fuer das StatusMapping vorhanden. Breche ab";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}
			
			//+++++++++++++++++++++
			IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL objEnum = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) hmEnum.get(enumStatus);							
			if(objEnum==null) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Keinen gemappten Status für en Status aus dem Event-Objekt erhalten. Breche ab";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}
			
			boolean bValue = eventStatusLocalSet.getStatusValue();
							
//			boolean bHasError = enumStatus.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASERROR)&& bValue;
//			boolean bEnded = enumStatus.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTOPPED) && bValue;
//			boolean bHasConnection = enumStatus.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTION) && bValue;
//			boolean bHasConnectionLost = enumStatus.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTIONLOST) && bValue;
//		
			boolean bEventHasError = enumStatus.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASERROR);
			boolean bEventEnded = enumStatus.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTOPPED);
			boolean bEventHasConnection = enumStatus.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTION);
			boolean bEventHasConnectionLost = enumStatus.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTIONLOST);
			
		
			//Wie an die sVpnIp rankommen.... Das geht über das ApplicationObjekt des erfolgreichen starters.								
			//ClientConfigStarterOVPN objStarter2 = (ClientConfigStarterOVPN) listaStarter.get(icount2);
			
			//Wie an die sVpnIp rankommen.... Das geht über das ApplicationObjekt des Events.
			IApplicationOVPN  objApplication = null;				
			ServerConfigStarterOVPN objStarter=null;
			int iIndex = -1;
		
			//Wie an die sVpnIp rankommen.... Das geht über das ApplicationObjekt des Events.
			objApplication = eventStatusLocalSet.getApplicationObjectUsed();
			if(objApplication==null) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": KEIN Application-Objekt aus dem Event-Objekt erhalten.";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Application-Objekt aus dem Event-Objekt erhalten.";
				System.out.println(sLog);
				this..logProtocolString(sLog);
				
			}
				
			objStarter = eventStatusLocalSet.getServerConfigStarterObjectUsed();
			if(objStarter==null) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": KEIN ConfigStarter-Objekt aus dem Event-Objekt erhalten.";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": ConfigStarter-Objekt aus dem Event-Objekt erhalten.";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				
				iIndex = objStarter.getIndex();
			}
						
			boolean bStatusLocalSet = this.setStatusLocalEnum(iIndex, objEnum, bValue);//Es wird ein Event gefeuert, an dem das Backend-Objekt registriert wird und dann sich passend einstellen kann.
			if(!bStatusLocalSet) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}
			//++++++++++++++
			
			if(bEventHasError && bEventEnded){
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status bEventHasError && bEventEnded";
				System.out.println(sLog);
				this.logProtocolString(sLog);					
			}else if((!bEventHasError) && bEventEnded){
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status !bEventHasError && bEventEnded";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				
			}else if(bEventHasConnection){
				//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status bEventHasConnection";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				
				String sVpnIp = objApplication.getVpnIpRemote();
				//int iId = eventStatusLocalSet.getProcessID();
				//sLog = ReflectCodeZZZ.getPositionCurrent()+": Thread # fuer Event mit der ID" + (iId) + " - Verbindung mit remote VPNIP='"+sVpnIp+"'";
				//System.out.println(sLog);
				//this.logProtocolString(sLog);										
				
				boolean bEndOnConnection = this.getFlag(IServerThreadProcessWatchMonitorOVPN.FLAGZ.END_ON_CONNECTION);
				if(bEndOnConnection) {
					sLog = ReflectCodeZZZ.getPositionCurrent()+": Beende den Monitor.";
					System.out.println(sLog);
					this.logProtocolString(sLog);
				}
				
			}else if(bEventHasConnectionLost) {
				//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status bEventHasConnectionLost";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				
				String sVpnIp = objApplication.getVpnIpRemote();
				//int iId = eventStatusLocalSet.getProcessID();
				//sLog = ReflectCodeZZZ.getPositionCurrent()+": Thread # fuer Event mit der ID" + (iId) + " - Verbindung verloren mit remote VPNIP='"+sVpnIp+"'";
				//System.out.println(sLog);
				//this.logProtocolString(sLog);										
	
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status '"+enumStatus.getAbbreviation()+"' nicht weiter behandelt";
				System.out.println(sLog);
				this.logProtocolString(sLog);	
			}
			
			bReturn = true;
		}//end main:			
		return bReturn;
	}
	
	
	//#########################################################
	/* (non-Javadoc)
	 * @see use.openvpn.client.status.IListenerObjectStatusLocalSetOVPN#isEventRelevant(use.openvpn.client.status.IEventObjectStatusLocalSetOVPN)
	 */
	@Override
	public boolean isEventRelevant(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(eventStatusLocal==null)break main;
			
			String sLog = ReflectCodeZZZ.getPositionCurrent()+": Pruefe Relevanz des Events.";
			System.out.println(sLog);
			this.logProtocolString(sLog);
			
			IEnumSetMappedZZZ enumStatus = eventStatusLocal.getStatusLocal();				
			if(enumStatus==null) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": KEINEN enumStatus empfangen. Beende.";
				System.out.println(sLog);
				this.logProtocolString(sLog);							
				break main;
			}
							
			sLog = ReflectCodeZZZ.getPositionCurrent()+": Einen enumStatus empfangen.";
			System.out.println(sLog);
			this.logProtocolString(sLog);
				
			sLog = ReflectCodeZZZ.getPositionCurrent()+": enumStatus hat class='"+enumStatus.getClass()+"'";
			System.out.println(sLog);
			this.logProtocolString(sLog);	
				
			sLog = ReflectCodeZZZ.getPositionCurrent()+": enumStatus='" + enumStatus.getAbbreviation()+"'";
			System.out.println(sLog);
			this.logProtocolString(sLog);
			
			//+++ Pruefungen
			bReturn = this.isEventRelevantByClass(eventStatusLocal);
			if(!bReturn) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Event werfenden Klasse ist fuer diese Klasse hinsichtlich eines Status nicht relevant. Breche ab.";
				System.out.println(sLog);
				this.logProtocolString(sLog);				
				break main;
			}
			
			bReturn = this.isStatusChanged(eventStatusLocal.getStatusText());
			if(!bReturn) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status nicht geaendert. Breche ab.";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				break main;
			}
						
			bReturn = this.isEventRelevantByStatusLocalValue(eventStatusLocal);
			if(!bReturn) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Statuswert nicht relevant. Breche ab.";
				System.out.println(sLog);
				this.logProtocolString(sLog);				
				break main;
			}
			
			bReturn = this.isEventRelevantByStatusLocal(eventStatusLocal);
			if(!bReturn) {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status an sich aus dem Event ist fuer diese Klasse nicht relevant. Breche ab.";
				System.out.println(sLog);
				this.logProtocolString(sLog);				
				break main;
			}
													
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	/* (non-Javadoc)
	 * @see use.openvpn.client.status.IListenerObjectStatusLocalSetOVPN#isEventRelevantByStatusLocalValue(use.openvpn.client.status.IEventObjectStatusLocalSetOVPN)
	 */
	@Override
	public boolean isEventRelevantByStatusLocalValue(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(eventStatusLocal==null)break main;
			
			//boolean bStatusValue = eventStatusLocalSet.getStatusValue();
			//Merke: Beim Monitor interessieren auch "false" Werte, um den Status ggfs. wieder zuruecksetzen zu koennen
			//if(bStatusValue==false)break main; //Hier interessieren nur "true" werte, die also etwas neues setzen.
			
			bReturn = true;
		}
		return bReturn;
	}
	
	@Override
	public boolean isEventRelevantByClass(IEventObjectStatusLocalZZZ eventStatusLocalSet) throws ExceptionZZZ {
		/* Loesung: DOWNCASTING mit instanceof , s.: https://www.positioniseverything.net/typeof-java/
	 	class Animal { }
		class Dog2 extends Animal {
			static void method(Animal j) {
			if(j instanceof Dog2){
			Dog2 d=(Dog2)j;//downcasting
			System.out.println(“downcasting done”);
			}
			}
			public static void main (String [] args) {
			Animal j=new Dog2();
			Dog2.method(j);
			}
		}
	 */
	
		boolean bReturn = false;
		main:{
			//Merke: enumStatus hat class='class use.openvpn.client.process.IProcessWatchRunnerOVPN$STATUSLOCAL'				
			if(eventStatusLocalSet.getStatusEnum() instanceof IProcessWatchRunnerOVPN.STATUSLOCAL){
				String sLog = ReflectCodeZZZ.getPositionCurrent()+": Enum Klasse ist instanceof IProcessWatchRunnerOVPN. Damit relevant.";
				System.out.println(sLog);
				this.logProtocolString(sLog);
				bReturn = true;
				break main;
			}		
			
			
		}//end main:
		return bReturn;
	}

	/* (non-Javadoc)
	 * @see use.openvpn.client.status.IListenerObjectStatusLocalSetOVPN#isEventRelevantByStatusLocal(use.openvpn.client.status.IEventObjectStatusLocalSetOVPN)
	 */
	@Override
	public boolean isEventRelevantByStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocal)	throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			IEnumSetMappedStatusZZZ enumStatus = eventStatusLocalSet.getStatusLocal();							
			bReturn = this.isStatusLocalRelevant(enumStatus);
			if(!bReturn) break main;
			
			
			String sAbr = eventStatusLocal.getStatusAbbreviation();
			if(!StringZZZ.startsWith(sAbr, "hasconnection")) break main;
			
			bReturn = true;
		}//end main:
		return bReturn;
	}

	@Override
	public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
							
			bFunction = this.offerStatusLocal(enumStatus, null, bStatusValue);
		}//end main:
		return bFunction;
	}

	@Override
	public boolean setStatusLocal(int iIndexOfProcess, Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bFunction = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			
			IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
			
			bFunction = this.offerStatusLocal_(iIndexOfProcess, enumStatus, null, bStatusValue);				
		}//end main;
		return bFunction;
	}
	
	@Override 
	public boolean setStatusLocalEnum(int iIndexOfProcess, IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
			
			bReturn = this.offerStatusLocal(iIndexOfProcess, enumStatus, null, bStatusValue);
		}//end main:
		return bReturn;
	}
	
	/* (non-Javadoc)
	 * @see basic.zBasic.AbstractObjectWithStatusZZZ#setStatusLocalEnum(basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ, boolean)
	 */
	@Override 
	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(enumStatusIn==null) {
				break main;
			}
			IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
			
			bReturn = this.offerStatusLocal(enumStatus, null, bStatusValue);
		}//end main:
		return bReturn;
	}
	
	//################################################
		//+++ aus IStatusLocalUserMessageZZZ			
		@Override 
		public boolean setStatusLocal(Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) {
					break main;
				}
				IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
				
				bFunction = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
			}//end main:
			return bFunction;
		}
		
		@Override 
		public boolean setStatusLocal(int iIndexOfProcess, Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) {
					break main;
				}
				IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
				
				bFunction = this.offerStatusLocal_(iIndexOfProcess, enumStatus, sMessage, bStatusValue);
			}//end main:
			return bFunction;
		}
		
		@Override 
		public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				if(enumStatusIn==null) {
					break main;
				}
				IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
				
				bReturn = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
			}//end main:
			return bReturn;
		}				
		
		@Override 
		public boolean setStatusLocalEnum(int iIndexOfProcess, IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				if(enumStatusIn==null) {
					break main;
				}
				IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL enumStatus = (IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL) enumStatusIn;
				
				bReturn = this.offerStatusLocal(iIndexOfProcess, enumStatus, null, bStatusValue);
			}//end main:
			return bReturn;
		}
		//++++++++++++++++++++++++++++++++++++++++

	
	//Weil auf den Status anderer Thread gehoert wird und diese weitergeleitet werden sollen.
	@Override
	public HashMap<IEnumSetMappedStatusZZZ, IEnumSetMappedStatusZZZ> createHashMapEnumSetForCascadingStatusLocalCustom() {
		
		//Es wird auf Events des ProcessWatchRunnerOVPN gehoert.
		//Die dort geworfenen Events werden hier auf LokaleEvents gemappt.
		//Aufbau der Map: Ankommender externer Event = Lokaler Event
		//Lokale Events, die keine externe Entsprechung haben, tauchen hier nicht auf
		HashMap<IEnumSetMappedStatusZZZ,IEnumSetMappedStatusZZZ>hmReturn = new HashMap<IEnumSetMappedStatusZZZ,IEnumSetMappedStatusZZZ>();
		main:{
			
			//Merke: Reine Lokale Statuswerte kommen nicht aus einem Event und werden daher nicht gemapped. 			
			hmReturn.put(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTARTNEW, IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPROCESSSTARTNEW);
			hmReturn.put(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTARTING, IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPROCESSSTARTING);
			hmReturn.put(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTARTED, IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPROCESSSTARTED);
			hmReturn.put(IProcessWatchRunnerOVPN.STATUSLOCAL.HASOUTPUT, IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPROCESSOUTPUT);
			hmReturn.put(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTION, IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPROCESSCONNECTION);
			hmReturn.put(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTIONLOST, IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPROCESSCONNECTIONLOST);
			
			hmReturn.put(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTOPPED, IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPROCESSSTOPPED);
			hmReturn.put(IProcessWatchRunnerOVPN.STATUSLOCAL.HASERROR, IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPROCESSERROR);
						
		}//end main:
		return hmReturn;	
	}

	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IEnumSetMappedStatusZZZ getStatusLocalEnumPrevious(int iIndexStepsBack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean start() throws ExceptionZZZ, InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStatusLocalDifferent(String sStatusString, boolean bStatusValue) throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEventRelevant2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocal)throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet)throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEventRelevantByStatusLocal2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet)throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet)throws ExceptionZZZ {
		// TODO Auto-generated method stub
		return false;
	}
}//END class