package basic.zBasic.util.moduleExternal.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.IProgramMonitorZZZ;
import basic.zBasic.component.IProgramZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.EventObject4LogFileWatchMonitorStatusLocalZZZ;
import basic.zKernel.status.IEventObject4LogFileWatchMonitorStatusLocalZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.IListenerProgramStatusLocalZZZ;


/**This class watches the ServerMainZZZ-class and the ServerConnectionListenerRuner-objects.
 * This class runs in a seperate thread, so the TrayIcon stays "clickable", that means that clicking on the icon will be processed.
 * 
 * @author 0823
 *
 */
public class LogFileWatchMonitorZZZ extends AbstractLogFileWatchMonitorZZZ {
	private static final long serialVersionUID = 8209025974705509709L;

	public LogFileWatchMonitorZZZ() throws ExceptionZZZ{
		super();				
	}
	
	public LogFileWatchMonitorZZZ(File objFile) throws ExceptionZZZ{
		super();				
		ProcessWatchMonitorNew_(objFile,null);
	}

	
	public LogFileWatchMonitorZZZ(String[] saFlagControl) throws ExceptionZZZ{
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
				
	@Override
	public boolean startCustom() throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			try {	
				String sLog = ReflectCodeZZZ.getPositionCurrent()+": Starting Monitor, switching Status of Monitor.";				
				this.logProtocolString(sLog);
			
				//NUN DAS BACKEND-AUFRUFEN. Merke, dass muss in einem eigenen Thread geschehen, damit das Icon anclickbar bleibt.								
				//Merke: Wenn über das enum der setStatusLocal gemacht wird, dann kann über das enum auch weiteres uebergeben werden. Z.B. StatusMeldungen.				
				//besser ueber eine geworfenen Event... und nicht direkt: this.objMain.setStatusLocal(ClientMainOVPN.STATUSLOCAL.ISCONNECTING, true);
				//this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTNO, false);
				//boolean bStartNewGoon = this.setStatusLocal(IServerThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISSTARTING, true);
				boolean bStatusLocalSet = this.switchStatusLocalForGroupTo(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTARTING, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
				if(!bStatusLocalSet) {
					sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
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
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Null as program for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size();
						this.logProtocolString(sLog);
					}else {						
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Program found for thread #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() +". Requesting thread start.";
						this.logProtocolString(sLog);
						
						objProgram.start(); //das hat eine doppelte Funktion. a) Einfache Programme werden gestartet. b) Runnable Programme werden im eigenen Thread gestartet
						
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Finished starting program #" + iNumberOfProcessStarted + " von " + listaProcessStarter.size() + ".";
						this.logProtocolString(sLog);
	 				}
				}//END for
				if(iNumberOfProcessStarted==0) {
					//Hier nicht abbrechen, sondern den Status wieder zurücksetzen.
					sLog = ReflectCodeZZZ.getPositionCurrent()+": No program started.";										
					this.logProtocolString(sLog);
					
					bStatusLocalSet = this.switchStatusLocalForGroupTo(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTARTNO, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
					if(!bStatusLocalSet) {
						sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
						this.logProtocolString(sLog);
						break main;
					}			
				}else if(iNumberOfProcessStarted>=1) {
					sLog = ReflectCodeZZZ.getPositionCurrent()+": " + iNumberOfProcessStarted + " programs started.";
					this.logProtocolString(sLog);
					
					bStatusLocalSet = this.switchStatusLocalForGroupTo(ILogFileWatchMonitorZZZ.STATUSLOCAL.ISSTARTED, true); //Damit der ISSTOPPED Wert auf jeden Fall auch beseitigt wird
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
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {		
		boolean bReturn = false;
		main:{
			
			if(eventStatusLocal==null) {
				  ExceptionZZZ ez = new ExceptionZZZ( "EventStatusObject not provided", this.iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 						
				  throw ez;
			}
		
			//Nun den einkommenden Event auf einen eigenen Event mappen.
			//Diesen eigenen Event werfen,
			//so dass andere Listener (die an dem Monitor registriert sind) auf den MonitorEvent reagieren können.
			HashMap<IEnumSetMappedStatusZZZ, IEnumSetMappedStatusZZZ> hmStatus = this.getHashMapEnumSetForCascadingStatusLocal();
			IEnumSetMappedStatusZZZ enumStatusIn = eventStatusLocal.getStatusLocal();
			if(enumStatusIn==null) {
				  ExceptionZZZ ez = new ExceptionZZZ( "EnumStatusObject not provided", this.iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName()); 						
				  throw ez;
			}
		
			IEnumSetMappedStatusZZZ enumStatusOut = hmStatus.get(enumStatusIn); 
			if(enumStatusOut==null) break main; //Wenn der Status nicht gemappt ist, wird auch nichts gesetzt.
			
			String sLog = "Gemappten Status gefunden... ("+ ReflectCodeZZZ.getPositionCurrent() + ")";
			this.logProtocolString(sLog);
			
			boolean bStatusValue = eventStatusLocal.getStatusValue();
			this.setStatusLocalEnum(enumStatusOut, bStatusValue);
			//this.offerStatusLocalEnum(enumStatusOut, bStatusValue);
			
			bReturn = true;
		}
		return bReturn;
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
	

	//###########################################################
		//### STATUS
		//###########################################################
		
		//####### aus IStatusLocalUserZZZ
		@Override
		public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(objEnumStatusIn==null) break main;
				
				ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) objEnumStatusIn;
				String sStatusName = enumStatus.name();
				if(StringZZZ.isEmpty(sStatusName)) break main;
											
				HashMap<String, Boolean> hmFlag = this.getHashMapStatusLocal();
				Boolean objBoolean = hmFlag.get(sStatusName.toUpperCase());
				if(objBoolean==null){
					bFunction = false;
				}else{
					bFunction = objBoolean.booleanValue();
				}
								
			}	// end main:
			
			return bFunction;	
		}

		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//+++ OFFER STATUS LOCAL, alle Varianten, gecasted auf dieses Objekt
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		@Override
		public boolean offerStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) break main;
				
				ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
				
				bFunction = this.offerStatusLocal_(enumStatus, "", bStatusValue);				
			}//end main;
			return bFunction;
		}
		
		
		@Override
		public boolean offerStatusLocal(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) break main;
				
				ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
				
				bFunction = this.offerStatusLocal_(enumStatus, sStatusMessage, bStatusValue);				
			}//end main;
			return bFunction;
		}
		
		private boolean offerStatusLocal_(Enum enumStatusIn, String sStatusMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) break main;
				
			
				//Merke: In anderen Klassen, die dieses Design-Pattern anwenden ist das eine andere Klasse fuer das Enum
				ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
				String sStatusName = enumStatus.name();
				bFunction = this.proofStatusLocalExists(sStatusName);															
				if(!bFunction) {
					String sLog = ReflectCodeZZZ.getPositionCurrent() + "Would like to fire event, but this status is not available: '" + sStatusName + "'";
					this.logProtocolString(sLog);			
					break main;
				}
				
			bFunction = this.proofStatusLocalValueChanged(sStatusName, bStatusValue);
			if(!bFunction) {
				String sLog = ReflectCodeZZZ.getPositionCurrent() + "Would like to fire event, but this status has not changed: '" + sStatusName + "'";
				this.logProtocolString(sLog);
				break main;
			}	
			
			//++++++++++++++++++++	
			//Setze den Status nun in die HashMap
			HashMap<String, Boolean> hmStatus = this.getHashMapStatusLocal();
			hmStatus.put(sStatusName.toUpperCase(), bStatusValue);
			
			//Den enumStatus als currentStatus im Objekt speichern...
			//                   dito mit dem "vorherigen Status"
			//Setze nun das Enum, und damit auch die Default-StatusMessage
			String sStatusMessageToSet = null;
			if(StringZZZ.isEmpty(sStatusMessage)){
				if(bStatusValue) {
					sStatusMessageToSet = enumStatus.getStatusMessage();
				}else {
					sStatusMessageToSet = "NICHT " + enumStatus.getStatusMessage();
				}			
			}else {
				sStatusMessageToSet = sStatusMessage;
			}
			
			String sLog = ReflectCodeZZZ.getPositionCurrent() + "Verarbeite sStatusMessageToSet='" + sStatusMessageToSet + "'";
			this.logProtocolString(sLog);

			//Falls eine Message extra uebergeben worden ist, ueberschreibe...
			if(sStatusMessageToSet!=null) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Setze sStatusMessageToSet='" + sStatusMessageToSet + "'";
				this.logProtocolString(sLog);
			}
			//Merke: Dabei wird die uebergebene Message in den speziellen "Ringspeicher" geschrieben, auch NULL Werte...
			this.offerStatusLocalEnum(enumStatus, bStatusValue, sStatusMessageToSet);
			
			
			
			//Falls irgendwann ein Objekt sich fuer die Eventbenachrichtigung registriert hat, gibt es den EventBroker.
			//Dann erzeuge den Event und feuer ihn ab.	
			if(this.getSenderStatusLocalUsed()==null) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Would like to fire event '" + enumStatus.getAbbreviation() + "', but no objEventStatusLocalBroker available, any registered?";
				this.logProtocolString(sLog);		
				break main;
			}
			
			//Erzeuge fuer das Enum einen eigenen Event. Die daran registrierten Klassen koennen in einer HashMap definieren, ob der Event fuer sie interessant ist.		
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Erzeuge Event fuer '" + sStatusName + "'";		
			this.logProtocolString(sLog);
			IEventObject4LogFileWatchMonitorStatusLocalZZZ event = new EventObject4LogFileWatchMonitorStatusLocalZZZ(this,enumStatus, bStatusValue);			
			
			//### GGFS. noch weitere benoetigte Objekte hinzufuegen............
			//...
			
					
			//Feuere den Event ueber den Broker ab.
			sLog = ReflectCodeZZZ.getPositionCurrent() + "Fires event '" + enumStatus.getAbbreviation() + "'";
			this.logProtocolString(sLog);
			this.getSenderStatusLocalUsed().fireEvent(event);
					
			bFunction = true;				
		}	// end main:
		return bFunction;
		}
		
		
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//+++ SET STATUS LOCAL, alle Varianten, gecasted auf dieses Objekt
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		@Override
		public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) break main;
				
				ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
								
				bFunction = this.offerStatusLocal(enumStatus, null, bStatusValue);
			}//end main:
			return bFunction;
		}
			
		/* (non-Javadoc)
		 * @see basic.zBasic.AbstractObjectWithStatusZZZ#setStatusLocalEnum(basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ, boolean)
		 */
		@Override 
		public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				if(enumStatusIn==null) break main;

				ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
				
				bReturn = this.offerStatusLocal(enumStatus, null, bStatusValue);
			}//end main:
			return bReturn;
		}
		
		//+++ aus IStatusLocalUserMessageZZZ			
		@Override 
		public boolean setStatusLocal(Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bFunction = false;
			main:{
				if(enumStatusIn==null) break main;
				
				ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
				
				bFunction = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
			}//end main:
			return bFunction;
		}
			
		@Override 
		public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
			boolean bReturn = false;
			main:{
				if(enumStatusIn==null) break main;
				
				ILogFileWatchMonitorZZZ.STATUSLOCAL enumStatus = (ILogFileWatchMonitorZZZ.STATUSLOCAL) enumStatusIn;
				
				bReturn = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
			}//end main:
			return bReturn;
		}				
		
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
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocalSet)	throws ExceptionZZZ {
		return true;
	}
	
	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}

	@Override
	public HashMap createHashMapStatusLocalReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//reagiere noch auf alle Events
		//Werte lediglich die andere HashMap mit den gemappten Stati aus setzte dann den passenden Status in diesem Objekt.
		return hmReturn;
	}

	

	

}//END class