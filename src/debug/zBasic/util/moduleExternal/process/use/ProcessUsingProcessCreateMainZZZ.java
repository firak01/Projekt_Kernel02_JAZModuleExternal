package debug.zBasic.util.moduleExternal.process.use;

import java.io.IOException;

public class ProcessUsingProcessCreateMainZZZ {
	
	public static void main(String[] args) {
	try {
		
		//Process p = load.exec( "cmd /c C:\\Programme\\OpenVPN\\bin\\openvpn.exe --pause-exit --config C:\\Programme\\OpenVPN\\config\\client_itelligence.ovpn");
		//DAS GEHT: Process p = load.exec( "cmd /c C:\\Programme\\OpenVPN\\bin\\openvpn.exe --pause-exit --config C:\\Programme\\OpenVPN\\config\\client_itelligence.ovpn");
		
		String sCommandConcrete = "bat\\KernelZZZTest_ProcessStarter_ModuleExternal.bat > c:\\fglkernel\\kernellog\\ProcessUsing_StarterLog.txt";
		
		
		Runtime load = Runtime.getRuntime();
		Process objReturn = load.exec("cmd /c " + sCommandConcrete);
		
		Thread.sleep(5000);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
