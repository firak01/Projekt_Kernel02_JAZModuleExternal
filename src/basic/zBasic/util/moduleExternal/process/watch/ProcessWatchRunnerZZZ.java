package basic.zBasic.util.moduleExternal.process.watch;

import java.util.HashMap;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractEnum.IEnumSetMappedStatusZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.flag.IFlagZUserZZZ;
import basic.zKernel.status.IEventObjectStatusLocalZZZ;
import basic.zKernel.status.ISenderObjectStatusLocalZZZ;

/**This class receives the stream from a process, which was started by the ConfigStarterZZZ class.
 * This is necessary, because the process will only goon working, if the streams were "catched" by a target.
 * This "catching" will be done in a special thread (one Thread per process).  
 * @author 0823
 *
 */
public class ProcessWatchRunnerZZZ extends AbstractProcessWatchRunnerZZZ {	
	private static final long serialVersionUID = -5171184089306717332L;
	protected ISenderObjectStatusLocalZZZ objEventStatusLocalBroker=null;//Das Broker Objekt, an dem sich andere Objekte regristrieren können, um ueber Aenderung eines StatusLocal per Event informiert zu werden.
	
	public ProcessWatchRunnerZZZ() throws ExceptionZZZ{
		super();		
	}
	
	public ProcessWatchRunnerZZZ(String[] saFlag) throws ExceptionZZZ{
		super(saFlag);		
	}
	
	public ProcessWatchRunnerZZZ(Process objProcess, String[] saFlag) throws ExceptionZZZ{
		super(objProcess, saFlag);		
	}
	
	public ProcessWatchRunnerZZZ(Process objProcess, String sLineFilter, String[] saFlag) throws ExceptionZZZ{
		super(objProcess, sLineFilter, saFlag);
	}
	

	//### Statische Methode (um einfacher darauf zugreifen zu können)
    public static Class getEnumStatusLocalClass(){    	
    	return STATUSLOCAL.class;    	
    }
	
	//### Getter / Setter ########

	//##########################################################
	//+++ aus IProcessWatchRunnerZZZ
	/** In dieser Methode werden die Ausgabezeilen eines Batch-Prozesses ( cmd.exe ) 
	 *  aus dem Standard - Output gelesen.
	 *  - Sie werden in das Kernel-Log geschrieben.
	 *  - Sie werden hinsichtlich bestimmter Schluesselsaetze analysiert,
	 *    um z.B. den erfolgreichen Verbindungsaufbau mitzubekommen.
	 *  
	 *  Merke: 
	 *  Merke1: Der aufgerufene OVPN-Prozess stellt irgendwann das schreiben ein
					//Das ist dann z.B. der letzte Eintrag
					//0#Sat Sep 02 07:39:48 2023 us=571873 NOTE: --mute triggered... 
					//Der wert wird in der OVPN-Konfiguration eingestellt, z.B.:
					//mute=20  
				
	 * Merke2: Wie über einen Erfolg benachrichtigen?
			   Wenn die Verbindung erstellt wird, steht folgendes im Log.
			   
TCP connection established with [AF_INET]192.168.3.116:4999
0#Sat Sep 02 12:53:10 2023 us=223095 TCPv4_CLIENT link local: [undef]
0#Sat Sep 02 12:53:10 2023 us=223095 TCPv4_CLIENT link remote: [AF_INET]192.168.3.116:4999
0#Sat Sep 02 12:53:10 2023 us=223095 TLS: Initial packet from [AF_INET]192.168.3.116:4999, sid=75fbf19d 73f20fdc
0#Sat Sep 02 12:53:10 2023 us=363726 VERIFY OK: depth=1, C=DE, ST=PREUSSEN, L=BERLIN, O=OpenVPN, OU=TEST, CN=PAUL.HINDENBURG, name=PAUL.HINDENBURG, emailAddress=paul.hindenburg@mailinator.com\09
0#Sat Sep 02 12:53:10 2023 us=363726 VERIFY OK: depth=0, C=DE, ST=PREUSSEN, L=BERLIN, O=OpenVPN, OU=TEST, CN=HANNIBALDEV06VM_SERVER, name=HANNIBALDEV06VM, emailAddress=paul.hindenburg@mailinator.com\09
0#Sat Sep 02 12:53:10 2023 us=551235 Data Channel Encrypt: Cipher 'BF-CBC' initialized with 128 bit key
0#Sat Sep 02 12:53:10 2023 us=551235 WARNING: INSECURE cipher with block size less than 128 bit (64 bit).  This allows attacks like SWEET32.  Mitigate by using a --cipher with a larger block size (e.g. AES-256-CBC).
0#Sat Sep 02 12:53:10 2023 us=551235 Data Channel Encrypt: Using 160 bit message hash 'SHA1' for HMAC authentication
0#Sat Sep 02 12:53:10 2023 us=551235 Data Channel Decrypt: Cipher 'BF-CBC' initialized with 128 bit key
0#Sat Sep 02 12:53:10 2023 us=551235 WARNING: INSECURE cipher with block size less than 128 bit (64 bit).  This allows attacks like SWEET32.  Mitigate by using a --cipher with a larger block size (e.g. AES-256-CBC).
0#Sat Sep 02 12:53:10 2023 us=551235 Data Channel Decrypt: Using 160 bit message hash 'SHA1' for HMAC authentication
0#Sat Sep 02 12:53:10 2023 us=551235 Control Channel: TLSv1.2, cipher TLSv1/SSLv3 DHE-RSA-AES256-GCM-SHA384, 1024 bit RSA
0#Sat Sep 02 12:53:10 2023 us=551235 [HANNIBALDEV06VM_SERVER] Peer Connection Initiated with [AF_INET]192.168.3.116:4999
0#Sat Sep 02 12:53:13 2023 us=20060 SENT CONTROL [HANNIBALDEV06VM_SERVER]: 'PUSH_REQUEST' (status=1)
0#Sat Sep 02 12:53:13 2023 us=176313 PUSH: Received control message: 'PUSH_REPLY,ping 10,ping-restart 240,ifconfig 10.0.0.2 10.0.0.1'
0#Sat Sep 02 12:53:13 2023 us=176313 OPTIONS IMPORT: timers and/or timeouts modified
0#Sat Sep 02 12:53:13 2023 us=176313 OPTIONS IMPORT: --ifconfig/up options modified
0#Sat Sep 02 12:53:13 2023 us=176313 do_ifconfig, tt->ipv6=0, tt->did_ifconfig_ipv6_setup=0
0#Sat Sep 02 12:53:13 2023 us=176313 ******** NOTE:  Please manually set the IP/netmask of 'OpenVPN2' to 10.0.0.2/255.255.255.252 (if it is not already set)
0#Sat Sep 02 12:53:13 2023 us=176313 open_tun, tt->ipv6=0
0#Sat Sep 02 12:53:13 2023 us=176313 TAP-WIN32 device [OpenVPN2] opened: \\.\Global\{9B00449E-0F90-4137-A063-CEA05D846AD8}.tap
0#Sat Sep 02 12:53:13 2023 us=176313 TAP-Windows Driver Version 9.9 
0#Sat Sep 02 12:53:13 2023 us=176313 TAP-Windows MTU=1500
0#Sat Sep 02 12:53:13 2023 us=176313 Sleeping for 3 seconds...
2023-9-2_12_53: Thread # 0 not jet ended or has reported an error.
0#Sat Sep 02 12:53:16 2023 us=176370 Successful ARP Flush on interface [4] {9B00449E-0F90-4137-A063-CEA05D846AD8}
0#Sat Sep 02 12:53:17 2023 us=410769 TEST ROUTES: 0/0 succeeded len=0 ret=0 a=0 u/d=down
0#Sat Sep 02 12:53:17 2023 us=410769 Route: Waiting for TUN/TAP interface to come up...
0#Sat Sep 02 12:53:18 2023 us=645168 TEST ROUTES: 0/0 succeeded len=0 ret=1 a=0 u/d=up
0#Sat Sep 02 12:53:18 2023 us=645168 WARNING: this configuration may cache passwords in memory -- use the auth-nocache option to prevent this
0#Sat Sep 02 12:53:18 2023 us=645168 Initialization Sequence Completed
*/
	@Override
	public boolean analyseInputLineCustom(String sLine, String sLinefilter) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{				
			sLine = StringZZZ.trimAnyQuoteMarked(sLine);
			bReturn = super.analyseInputLineCustom(sLine, sLinefilter);
			if(bReturn) break main;
			
			//+++ Mache nun die spezielle Analyse ohne den statischen Filter, zugeschintten auf den Beispielprocess
			String sZaehler = StringZZZ.right(sLine, "Zaehler");			
			String[] saQuotation = {"'","\""};			
			sZaehler = StringZZZ.stripRight(sZaehler,saQuotation);
	        sZaehler = sZaehler.trim();
			int iZaehler = StringZZZ.toInteger(sZaehler);
			if(iZaehler % 10 == 0 && iZaehler > 0) {
				System.out.println(ReflectCodeZZZ.getPositionCurrent() + "TESTFGL PROCESS STRING ANALYSE - 10er Zaehler gefunden.");				
				break main;				
			}else if(iZaehler >= 100) {
				System.out.println(ReflectCodeZZZ.getPositionCurrent() + "TESTFGL PROCESS STRING ANALYSE - Zaehler ueber 100. Breche ab.");
				//this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUEST_STOP, false);//Merke: STOPREQUEST ist eine Anweisung.. bleibt also ein Flag und ist kein Status
				bReturn = true;
				break main;
			}			
		}//end main:
		return bReturn;
	}

	
	//###### GETTER / SETTER
	//sind eher in der abstrakten Klasse
	
		
	//########################################
	//### STATUS
	//########################################
	
	//Wirf beim Setzen des StatusLocal einen Event	
//	@Override 
//	public boolean setStatusLocal(Enum enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bReturn = false;
//		main:{
//			if(enumStatusIn==null) {
//				break main;
//			}
//			//IWatchRunnerZZZ.STATUSLOCAL enumStatus =  (basic.zBasic.util.moduleExternal.IWatchRunnerZZZ.STATUSLOCAL) enumStatusIn;
//			
//			//bFunction = this.offerStatusLocal(enumStatus, null, bStatusValue);
//			bReturn = this.setStatusLocal(enumStatusIn, null, bStatusValue);
//		}//end main:
//		return bReturn;
//	}
//		
//	@Override 
//	public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, boolean bStatusValue) throws ExceptionZZZ {
//		boolean bReturn = false;
//		main:{
//			if(enumStatusIn==null) {
//				break main;
//			}
//			ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) enumStatusIn;
//			
//			//bReturn = this.offerStatusLocal(enumStatus, null, bStatusValue);
//			bReturn = this.setStatusLocal(enumStatus.getName(), null, bStatusValue);
//		}//end main:
//		return bReturn;
//	}
//		
//	//################################################
//		//+++ aus IStatusLocalUserMessageZZZ			
//		@Override 
//		public boolean setStatusLocal(Enum enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
//			boolean bReturn = false;
//			main:{
//				if(enumStatusIn==null) {
//					break main;
//				}
//				//ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (ProcessWatchRunnerZZZ.STATUSLOCAL) enumStatusIn;
//				
//				//bFunction = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
//				bReturn = this.setStatusLocal(enumStatusIn.name(), sMessage, bStatusValue);
//			}//end main:
//			return bReturn;
//		}
//				
//		@Override 
//		public boolean setStatusLocalEnum(IEnumSetMappedStatusZZZ enumStatusIn, String sMessage, boolean bStatusValue) throws ExceptionZZZ {
//			boolean bReturn = false;
//			main:{
//				if(enumStatusIn==null) {
//					break main;
//				}
//				//.STATUSLOCAL enumStatus = (ProcessWatchRunnerZZZ.STATUSLOCAL) enumStatusIn;
//				
//				//bReturn = this.offerStatusLocal(enumStatus, sMessage, bStatusValue);
//				bReturn = this.setStatusLocal(enumStatusIn.getName(), sMessage, bStatusValue);
//			}//end main:
//			return bReturn;
//		}				
//		
				
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	    
	//### aus iStatusLocalUser	
//	public boolean getStatusLocal(Enum objEnumStatusIn) throws ExceptionZZZ {
//		boolean bFunction = false;
//		main:{
//			if(objEnumStatusIn==null) {
//				break main;
//			}
//			
//			//Merke: Bei einer anderen Klasse, die dieses DesingPattern nutzt, befindet sich der STATUSLOCAL in einer anderen Klasse
//			//ProcessWatchRunnerZZZ.STATUSLOCAL enumStatus = (STATUSLOCAL) objEnumStatusIn;
//			String sStatusName = objEnumStatusIn.name();
//			if(StringZZZ.isEmpty(sStatusName)) break main;
//										
//			HashMap<String, Boolean> hmFlag = this.getHashMapStatusLocal();
//			Boolean objBoolean = hmFlag.get(sStatusName.toUpperCase());
//			if(objBoolean==null){
//				bFunction = false;
//			}else{
//				bFunction = objBoolean.booleanValue();
//			}
//							
//		}	// end main:
//		
//		return bFunction;	
//	}

	
	//###########################################################

	@Override
	public boolean isEventRelevant2ChangeStatusLocalByClass(IEventObjectStatusLocalZZZ eventStatusLocal)throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean isEventRelevant2ChangeStatusLocalByStatusLocalValue(IEventObjectStatusLocalZZZ eventStatusLocal)throws ExceptionZZZ {
		return true;
	}

	

	@Override
	public HashMap createHashMapStatusLocal4ReactionCustom() {
		return null;
	}

	@Override
	public boolean reactOnStatusLocal4ActionCustom(String sAction, IEnumSetMappedStatusZZZ enumStatus,		boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {	
		return false;
	}

	@Override
	public boolean queryReactOnStatusLocalEventCustom(IEventObjectStatusLocalZZZ eventStatusLocal) throws ExceptionZZZ {
		return true;
	}

	@Override
	public boolean queryOfferStatusLocalCustom() throws ExceptionZZZ{
		//Diese Methode wird vor dem ...offerStatusLocal... aufgerufen.
		//Dadurch kann alsow verhindert werden, dass weitere Events geworfen werden.
		boolean bReturn=false;
		String sLog;
		main:{
			
			bReturn = true;
		}//end main
		return bReturn;
	}

	@Override
	public boolean queryReactOnStatusLocal4ActionCustom(String sActionAlias, IEnumSetMappedStatusZZZ enumStatus, boolean bStatusValue, String sStatusMessage) throws ExceptionZZZ {
		return true;
	}
	
	}//END class
