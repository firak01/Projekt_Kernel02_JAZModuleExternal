package debug.zBasic.util.moduleExternal.process.watch;

import java.util.HashMap;

import basic.zBasic.AbstractObjectWithFlagOnStatusListeningZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedZZZ;
import basic.zBasic.util.moduleExternal.IWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.monitor.ILogFileWatchMonitorZZZ;
import basic.zBasic.util.moduleExternal.monitor.IProcessWatchMonitorZZZ;
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ;
import basic.zBasic.util.moduleExternal.process.watch.IProcessWatchRunnerZZZ.STATUSLOCAL;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;

/** Ein Beispiel-Broker, an dem sich die "hoerenden" Klassen registieren.
 * @author fl86kyvo
 *
 */
public class ProcessWatchListener_ExampleZZZ extends AbstractObjectWithFlagOnStatusListeningZZZ {//implements IListenerObjectStatusLocalMessageZZZ, IListenerObjectStatusLocalMessageReactZZZ{
	private static final long serialVersionUID = -2338056174362726426L;

	public ProcessWatchListener_ExampleZZZ() throws ExceptionZZZ {
		super();
	}
	
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn=false;
		main:{	
			if(eventStatusLocal==null)break main;
						
			String sLog = ReflectCodeZZZ.getPositionCurrent()+": Fuer ProcessWatchEvent.";
			this.logLineDate(sLog);
			
			if(eventStatusLocal instanceof IEventObjectStatusLocalZZZ) {
				
				boolean bRelevant = this.isEventRelevantAny((IEventObjectStatusLocalZZZ) eventStatusLocal); 
				if(!bRelevant) {
					sLog = 	ReflectCodeZZZ.getPositionCurrent() + ": Event / Status nicht relevant. Breche ab.";
					this.logProtocolString(sLog);
					break main;
				}
				
				IEventObjectStatusLocalZZZ eventStatusLocalSet = (IEventObjectStatusLocalZZZ) eventStatusLocal;
				IEnumSetMappedZZZ enumStatus = eventStatusLocalSet.getStatusLocal();				
				
				
			
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
			//IProcessWatchRunnerZZZ.STATUSLOCAL objEnum = (STATUSLOCAL) enumStatus;
			IWatchRunnerZZZ.STATUSLOCAL objEnum = (basic.zBasic.util.moduleExternal.IWatchRunnerZZZ.STATUSLOCAL) enumStatus;
			//++++++++++++++++++++##
			
			boolean bValue = eventStatusLocalSet.getStatusValue();
			
			
			//Merke: Ein Interface ILogFileWatchListenerExampleZZZ mit dem STATUSLOCAL ist nicht implementiert.
//			boolean bHasError = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASERROR)&& bValue;
//			boolean bEnded = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.ISSTOPPED) && bValue;
//			boolean bHasConnection = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTION) && bValue;
//			boolean bHasConnectionLost = objEnum.equals(IProcessWatchRunnerOVPN.STATUSLOCAL.HASCONNECTIONLOST) && bValue;
		
			boolean bEventHasError = objEnum.equals(IWatchRunnerZZZ.STATUSLOCAL.HASERROR)&& bValue;
			boolean bEventEnded = objEnum.equals(IWatchRunnerZZZ.STATUSLOCAL.ISSTOPPED)&& bValue;
			
			//boolean bEventHasConnection = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTED);
			//boolean bEventHasConnectionLost = objEnum.equals(IClientMainOVPN.STATUSLOCAL.ISCONNECTINTERUPTED);
			
			
			boolean bEventHasFilterFound = objEnum.equals(IWatchRunnerZZZ.STATUSLOCAL.HASFILTERFOUND)&& bValue;
				
			//int iIndex = eventStatusLocalSet.getProcessID();
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
				this.logLineDate(sLog);					
			}else if((!bEventHasError) && bEventEnded){
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Status !bEventHasError && bEventEnded";
				this.logLineDate(sLog);
				
			}
		
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent()+": Event ist kein instanceof IEventObjectStatusLocalZZZ. Klasse: " + eventStatusLocal.getClass().getName();
				this.logLineDate(sLog);
			}//end if instanceof ...MessageSetZZZ
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	@Override
	public boolean isEventRelevant2ChangeStatusLocalByClass(IEventObjectStatusLocalZZZ eventStatusBasic) throws ExceptionZZZ {
		return true;
	}


	@Override
	public boolean isEventRelevant2ChangeStatusLocalByStatusLocalValue(IEventObjectStatusLocalZZZ eventStatusBasic) throws ExceptionZZZ {
	return true;
	}

	@Override
	public HashMap createHashMapStatusLocal4ReactionCustom() {
		HashMap<IEnumSetMappedStatusZZZ, String> hmReturn = new HashMap<IEnumSetMappedStatusZZZ, String>();
		
		//Reagiere auf diee Events... mit dem angegebenen Alias.
		hmReturn.put(IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERFILTERFOUND, "doFilterFound");
		hmReturn.put(IProcessWatchMonitorZZZ.STATUSLOCAL.HASERROR, "doStop");
		
		hmReturn.put(IProcessWatchMonitorZZZ.STATUSLOCAL.HASPROCESSWATCHRUNNERSTOPPED, "doStop");
				
		return hmReturn;
	}

	@Override
	public boolean reactOnStatusLocal4ActionCustom(String sAction, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		main:{
		if(!bStatusValue)break main;
		}

		return false;
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
