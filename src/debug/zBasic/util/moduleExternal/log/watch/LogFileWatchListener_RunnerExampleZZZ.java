package debug.zBasic.util.moduleExternal.log.watch;

import basic.zBasic.AbstractObjectZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.log.watch.ILogFileWatchRunnerZZZ.STATUSLOCAL;
import basic.zKernel.status.IEventObjectStatusBasicZZZ;
import basic.zKernel.status.IEventObjectStatusLocalMessageReactZZZ;
import basic.zKernel.status.IEventObjectStatusLocalMessageSetZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalMessageReactZZZ;
import basic.zKernel.status.IListenerObjectStatusLocalMessageSetZZZ;

/** Ein Beispiel-Broker, an dem sich die "hoerenden" Klassen registieren.
 * @author fl86kyvo
 *
 */
public class LogFileWatchListener_RunnerExampleZZZ extends AbstractObjectZZZ implements IListenerObjectStatusLocalMessageSetZZZ, IListenerObjectStatusLocalMessageReactZZZ{
	private static final long serialVersionUID = -2338056174362726426L;

	public LogFileWatchListener_RunnerExampleZZZ() throws ExceptionZZZ {
		super();
	}
	
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusBasicZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn=false;
		main:{	
			if(eventStatusLocal==null)break main;
						
			String sLog = ReflectCodeZZZ.getPositionCurrent()+": Fuer LogFileWatchEvent.";
			System.out.println(sLog);
			this.logLineDate(sLog);
			
			if(eventStatusLocal instanceof IEventObjectStatusLocalMessageSetZZZ) {
				
				boolean bRelevant = this.isEventRelevant(eventStatusLocal); 
				if(!bRelevant) {
					sLog = 	ReflectCodeZZZ.getPositionCurrent() + ": Event / Status nicht relevant. Breche ab.";
					System.out.println(sLog);
					this.logProtocolString(sLog);
					break main;
				}
				
				IEventObjectStatusLocalMessageSetZZZ eventStatusLocalSet = (IEventObjectStatusLocalMessageSetZZZ) eventStatusLocal;
				IEnumSetMappedZZZ enumStatus = eventStatusLocalSet.getStatusEnum();				
				
				
			
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
			
			//Wenn wir den Status nicht weitersenden, sondern direkt verarbeiten ist solch ein Mapping nicht notwendig.
			//Also da wir mit objEnum als Variable weiterarbeiten wollen:
			ILogFileWatchRunnerZZZ.STATUSLOCAL objEnum = (STATUSLOCAL) enumStatus;
			//++++++++++++++++++++##
			
			boolean bValue = eventStatusLocalSet.getStatusValue();
			
			
			//Merke: Ein Interface ILogFileWatchListenerExampleZZZ mit dem STATUSLOCAL ist nicht implementiert.
//			boolean bHasError = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASERROR)&& bValue;
//			boolean bEnded = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTOPPED) && bValue;
//			boolean bHasConnection = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTION) && bValue;
//			boolean bHasConnectionLost = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTIONLOST) && bValue;
		
			boolean bEventHasError = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASERROR)&& bValue;
			boolean bEventEnded = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED)&& bValue;
			
			//boolean bEventHasConnection = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTED);
			//boolean bEventHasConnectionLost = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTINTERUPTED);
			
			
			boolean bEventHasFilterFound = objEnum.equals(ILogFileWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND)&& bValue;
				
			int iIndex = eventStatusLocalSet.getProcessID();
			String sStatusMessage = eventStatusLocalSet.getStatusMessage();	
			sLog = ReflectCodeZZZ.getPositionCurrent() + ": StatusMessage ist = '" + sStatusMessage + "'";
			this.logProtocolString(sLog);
			

			//Einen Status Anzunehmen ist hier nicht implementiert			
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
		
			
			}//end if instanceof ...MessageSetZZZ
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	
	@Override
	public boolean isStatusLocalDifferent(String sStatusString, boolean bStatusValue) throws ExceptionZZZ {
		String sLog = ReflectCodeZZZ.getPositionCurrent() + ": Input Statusstring '" + sStatusString + "', bStatusValue '" + bStatusValue + "'";
		System.out.println(sLog);
		return true;
	}


	@Override
	public boolean isEventRelevant(IEventObjectStatusBasicZZZ eventStatusBasic) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			
			if(!this.isEventRelevant2ChangeStatusLocal(eventStatusBasic)) break main;
			if(!this.isEventRelevantByClass2ChangeStatusLocal(eventStatusBasic)) break main;
			if(!this.isEventRelevantByStatusLocal2ChangeStatusLocal(eventStatusBasic)) break main;
			if(!this.isEventRelevantByStatusLocalValue2ChangeStatusLocal(eventStatusBasic)) break main;
			
			bReturn = true;
		}//end main:
		return bReturn;
	}

	@Override
	public boolean isEventRelevant2ChangeStatusLocal(IEventObjectStatusBasicZZZ eventStatusBasic) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByClass2ChangeStatusLocal(IEventObjectStatusBasicZZZ eventStatusBasic) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocal2ChangeStatusLocal(IEventObjectStatusBasicZZZ eventStatusBasic) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevantByStatusLocalValue2ChangeStatusLocal(IEventObjectStatusBasicZZZ eventStatusBasic) throws ExceptionZZZ {
	return true;
	}
}
