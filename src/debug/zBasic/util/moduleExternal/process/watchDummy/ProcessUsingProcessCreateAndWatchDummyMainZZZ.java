package debug.zBasic.util.moduleExternal.process.watchDummy;

import basic.zBasic.AbstractObjectWithExceptionZZZ;
import basic.zBasic.ExceptionZZZ;

public class ProcessUsingProcessCreateAndWatchDummyMainZZZ extends AbstractObjectWithExceptionZZZ {
	
	public static void main(String[] args) {
	try {
		
		ProcessUsingProcessCreateAndWatchDummyZZZ objProcessStarter = new ProcessUsingProcessCreateAndWatchDummyZZZ();
		objProcessStarter.requestStart();
			
		Thread.sleep(1000);
		
		//Kommt ueberhaupt was zurueck... also InputStream auslesen.
		objProcessStarter.writeOutputToLogPLUSanalyse();
		
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExceptionZZZ e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
