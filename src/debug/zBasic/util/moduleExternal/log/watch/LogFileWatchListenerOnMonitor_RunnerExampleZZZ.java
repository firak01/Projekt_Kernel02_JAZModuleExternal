package debug.zBasic.util.moduleExternal.log.watch;

import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.component.AbstractProgramWithFlagOnStatusListeningRunnableZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;

/**Diese Klasse läuft einfach und ist an einem Monitor registriert.
 * Wirft der Monitor Events, reagiert sie darauf.
 *  
 * @author fl86kyvo
 *
 */
public class LogFileWatchListenerOnMonitor_RunnerExampleZZZ extends AbstractProgramWithFlagOnStatusListeningRunnableZZZ implements ILogFileWatchOnMonitorListenerRunnerExampleZZZ {
	private static final long serialVersionUID = 6586079955658760005L;
		
	public LogFileWatchListenerOnMonitor_RunnerExampleZZZ() throws ExceptionZZZ {
		super();		
	}
	
	public LogFileWatchListenerOnMonitor_RunnerExampleZZZ(String[] saFlag) throws ExceptionZZZ {
		super();	
		LogFileWatchOnMonitorListenerRunnerExampleNew_(saFlag);
	}
		
	
	private boolean LogFileWatchOnMonitorListenerRunnerExampleNew_(String[] saFlagControl) throws ExceptionZZZ {
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
		}//end main:
		return bReturn;
	}
	
	

	//#### GETTER / SETTER
		
	@Override
	public boolean startCustom() throws ExceptionZZZ{
		boolean bReturn = false;
		main:{	
			
			//Einfach eine Endlosschleife und ggfs. mit einem Status abschliessen.
			bReturn = this.startLogFileOnMonitorListenerRunner();

		}//end main:
		return bReturn;
	}
	
	
	/** Einfach nur laufen und auf das im "Empfangen eines Events" gesetzte Flag auswerten.
	 * @return
	 * @author Fritz Lindhauer, 10.12.2023, 16:04:55
	 */
	public boolean startLogFileOnMonitorListenerRunner() throws ExceptionZZZ{
		boolean bReturn= false;
		main:{			
			try {
				
				//Warte auf ein Flag aufzuhoeren.
				long lcounter = 0;
				do {
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
						break main;
					}

					lcounter++;
					
					System.out.println(ReflectCodeZZZ.getPositionCurrent() +": Running waiting for Requeststop-Flag. (" + lcounter +")");
					Thread.sleep(5000);
					
				}while(true);
				
			}catch (InterruptedException e) {					
				try {
					String sLogIE = e.getMessage();
					this.logProtocolString("An error happend: '" + sLogIE + "'");
				} catch (ExceptionZZZ e1) {
					System.out.println(e1.getDetailAllLast());
					e1.printStackTrace();
				}
				System.out.println(e.getMessage());
				e.printStackTrace();
			} finally {
				
	        }
		}//end main:
		return bReturn;
	}
		
	//###############################
	//### FLAG HANDLING
	//###############################
	@Override
	public boolean getFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	//### aus IListenerObjectStatusLocalMessageReactZZZ
	//### Reaktion darauf, wenn ein Event aufgefangen wurde
	@Override
	public boolean reactOnStatusLocalEvent(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		boolean bReturn = false;
		String sLog=null;
		
		main:{
			sLog = ReflectCodeZZZ.getPositionCurrent() + ": Filter gefunden und mache den changeStatusLocal Event.";
			this.logProtocolString(sLog);
			
			if(eventStatusLocal instanceof IEventObjectStatusLocalZZZ) {// .getClass().getSimpleName().equals("LogFileCreateMockRunnerZZZ")) {
				IEventObjectStatusLocalZZZ event = (IEventObjectStatusLocalZZZ) eventStatusLocal;
				boolean bStatusValue = event.getStatusValue();
				if(bStatusValue!=true) break main;
				
				if(this.getFlag(ILogFileWatchOnMonitorListenerRunnerExampleZZZ.FLAGZ.END_ON_FILTERFOUND)) {
					sLog = ReflectCodeZZZ.getPositionCurrent() + ": Filter gefunden und END_ON_FILTERFOUND gesetzt. Beende Schleife.";
					this.logProtocolString(sLog);
					
					this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);								
				}
			}else {
				sLog = ReflectCodeZZZ.getPositionCurrent() + ": Event ist nicht instanceof IEventObjectStatusLocalZZZ und wird nicht behandelt. Klasse: " + eventStatusLocal.getClass().getName();
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
	public HashMap createHashMapStatusLocalReactionCustom() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
