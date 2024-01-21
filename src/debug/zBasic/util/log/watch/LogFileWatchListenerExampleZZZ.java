package debug.zBasic.util.log.watch;

import basic.zBasic.AbstractObjectZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.log.watch.ILogFileWatchRunnerZZZ.STATUSLOCAL;
import basic.zKernel.status.IEventObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalMessageSetZZZ;

public class LogFileWatchListenerExampleZZZ extends AbstractObjectZZZ implements IListenerObjectStatusLocalMessageSetZZZ{
	private static final long serialVersionUID = -2338056174362726426L;

	public LogFileWatchListenerExampleZZZ() throws ExceptionZZZ {
		super();
	}

	@Override
	public boolean changeStatusLocal(IEventObjectStatusLocalMessageSetZZZ eventStatusLocalSet) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			String sLog = ReflectCodeZZZ.getPositionCurrent() + ": Event als Input.";
			System.out.println(sLog);
			
			//+++ Mappe nun die eingehenden Status-Enums auf die eigenen.
			
	//		if(eventStatusLocalSet.getStatusEnum() instanceof IClientMainOVPN.STATUSLOCAL){
	//			System.out.println(ReflectCodeZZZ.getPositionCurrent() +" :FGLTEST 01");
	//			bReturn = this.statusLocalChangedMainEvent_(eventStatusLocalSet);
	//			break main;
	//			
	//		}else 
	
			
			
	//		if (eventStatusLocalSet.getStatusEnum() instanceof IClientThreadProcessWatchMonitorOVPN.STATUSLOCAL) {
	//			System.out.println(ReflectCodeZZZ.getPositionCurrent() +"TYP FGLTEST 12");
	//			bReturn = this.changeStatusLocalMonitorEvent_(eventStatusLocalSet);
	//			break main;
	//			
	//		}else if(eventStatusLocalSet.getStatusEnum() instanceof IClientThreadVpnIpPingerOVPN.STATUSLOCAL) {
	//			System.out.println(ReflectCodeZZZ.getPositionCurrent() +"TYP FGLTEST 13");
	//			bReturn = this.changeStatusLocalPingerEvent_(eventStatusLocalSet);
	//			break main;
			
			
			
			if (eventStatusLocalSet.getStatusEnum() instanceof ILogFileWatchRunnerZZZ.STATUSLOCAL) {
				System.out.println(ReflectCodeZZZ.getPositionCurrent() +"TYP FGLTEST 14");
				bReturn = this.changeStatusLocalLogFileWatchEvent_(eventStatusLocalSet);
				break main;
				
			}else{	
				System.out.println(ReflectCodeZZZ.getPositionCurrent() +"TYP FGLTEST 00 ELSE");
				
			}		
		
		}//end main
		return bReturn;
	}
	
	/** Merke: Diese private Methode wird nach ausführlicher Prüfung aufgerufen, daher hier mehr noetig z.B.:
	 * - Keine Pruefung auf NULLL
	 * - kein instanceof 
	 * @param eventStatusLocalSet
	 * @return
	 * @throws ExceptionZZZ
	 * @author Fritz Lindhauer, 19.10.2023, 09:43:19
	 */
	private boolean changeStatusLocalLogFileWatchEvent_(IEventObjectStatusLocalMessageSetZZZ eventStatusLocalSet) throws ExceptionZZZ {
		boolean bReturn=false;
		main:{	
			if(eventStatusLocalSet==null)break main;
						
			String sLog = ReflectCodeZZZ.getPositionCurrent()+": Fuer PingerEvent.";
			System.out.println(sLog);
			this.logLineDate(sLog);
			
			IEnumSetMappedZZZ enumStatus = eventStatusLocalSet.getStatusEnum();				
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++			
//			boolean bRelevant = this.isEventRelevant(eventStatusLocalSet); 
//			if(!bRelevant) {
//				sLog = 	ReflectCodeZZZ.getPositionCurrent() + ": Event / Status nicht relevant. Breche ab.";
//				System.out.println(sLog);
//				this.logProtocolString(sLog);
//				break main;
//			}
			
			//Weiter Daten holen... im OVPN - Projekt z.B. die IPAdresse...
			
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			
			//+++++++++++++++++++++
			//Falls der empfangene Status in einen anderen, eigenen, lokalen Status uebersetzt werden muss.
//			HashMap<IEnumSetMappedStatusZZZ,IEnumSetMappedStatusZZZ>hmEnum = this.getHashMapEnumSetForCascadingStatusLocal();
//			IClientMainOVPN.STATUSLOCAL objEnum = (IClientMainOVPN.STATUSLOCAL) hmEnum.get(enumStatus);			
//			if(objEnum==null) {
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": Keinen gemappten Status aus dem Event-Objekt erhalten. Breche ab";
//				System.out.println(sLog);
//				this.logLineDate(sLog);
//				break main;
//			}
			
			//Aber da wir den Status nicht weitersenden, sondern direkt verarbeiten ist solch ein Mapping nicht notwendig.
			//Also da wir mit objEnum als Variable weiterarbeiten wollen:
			ILogFileWatchRunnerZZZ.STATUSLOCAL objEnum = (STATUSLOCAL) enumStatus;
			//++++++++++++++++++++##
			
			
			boolean bValue = eventStatusLocalSet.getStatusValue();
			
//			boolean bHasError = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASERROR)&& bValue;
//			boolean bEnded = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTOPPED) && bValue;
//			boolean bHasConnection = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTION) && bValue;
//			boolean bHasConnectionLost = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTIONLOST) && bValue;
//		
			boolean bEventHasError = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR)&& bValue;
			boolean bEventEnded = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED)&& bValue;
			
	//		boolean bEventHasConnection = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTED);
	//		boolean bEventHasConnectionLost = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTINTERUPTED);
			
			
			boolean bEventHasFilterFound = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND)&& bValue;

				
			int iIndex = eventStatusLocalSet.getProcessID();
			String sStatusMessage = eventStatusLocalSet.getStatusMessage();	

			//Einen Status anzunehmen ist nicht notwendig.
			//In dem Debug Fall, wird hier einfach nur etwas zurückgegeben...
			sLog = ReflectCodeZZZ.getPositionCurrent() + ": '" + sStatusMessage + "'";
			System.out.println(sLog);
			this.logLineDate(sLog);
			
//			boolean bStatusLocalSet = this.offerStatusLocal(iIndex, objEnum, sStatusMessage, bValue);//Es wird ein Event gefeuert, an dem das Tray-Objekt und andere registriert sind und dann sich passend einstellen kann.
//			if(!bStatusLocalSet) {
//				sLog = ReflectCodeZZZ.getPositionCurrent()+": Lokaler Status nicht gesetzt, aus Gruenden. Breche ab";
//				System.out.println(sLog);
//				this.logLineDate(sLog);
//				break main;
//			}
			//++++++++++++++
			
			//Die Stati vom Monitor-Objekt mit dem Backend-Objekt mappen
			//if(IClientThreadProcessWatchMonitorOVPN.STATUSLOCAL.HASPRO==objStatusEnum) {
			//	this.setStatusLocal(IClientMainOVPN.STATUSLOCAL.ISCONNECTING, eventStatusLocalSet.getStatusValue());				
			//}else if(IClientThreadProcessWatchMonitorOVPN.STATUSLOCAL.ISCONNECTED==objStatusEnum) {
			
			if(bEventHasError && bEventEnded){
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status bEventHasError && bEventEnded";
				System.out.println(sLog);
				this.logLineDate(sLog);					
			}else if((!bEventHasError) && bEventEnded){
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status !bEventHasError && bEventEnded";
				System.out.println(sLog);
				this.logLineDate(sLog);
				
			}

			bReturn = true;
		}//end main:
		return bReturn;
	}

	
	
	//###########################
	@Override
	public boolean isStatusLocalDifferent(String sStatusString, boolean bStatusValue) throws ExceptionZZZ {
		String sLog = ReflectCodeZZZ.getPositionCurrent() + ": Input Statusstring '" + sStatusString + "', bStatusValue '" + bStatusValue + "'";
		System.out.println(sLog);
		return true;
	}

	@Override
	public boolean isEventRelevant2ChangeStatusLocal(IEventObjectStatusLocalMessageSetZZZ eventStatusLocalSet) throws ExceptionZZZ {
		String sLog = ReflectCodeZZZ.getPositionCurrent() + ": Event als Input.";
		System.out.println(sLog);
		return true;
	}

	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusLocalMessageSetZZZ eventStatusLocalSet) throws ExceptionZZZ {
		String sLog = ReflectCodeZZZ.getPositionCurrent() + ": Event als Input.";
		System.out.println(sLog);
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocal2ChangeStatusLocal(IEventObjectStatusLocalMessageSetZZZ eventStatusLocalSet) throws ExceptionZZZ {
		String sLog = ReflectCodeZZZ.getPositionCurrent() + ": Event als Input.";
		System.out.println(sLog);
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusLocalMessageSetZZZ eventStatusLocalSet) throws ExceptionZZZ {
		String sLog = ReflectCodeZZZ.getPositionCurrent() + ": Event als Input.";
		System.out.println(sLog);
		return true;
	}
}
