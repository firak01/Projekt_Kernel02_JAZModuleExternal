package debug.zBasic.util.moduleExternal.process.create;

import basic.zBasic.AbstractObjectZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;

public class ProcessCreateRunnerMockZZZ extends AbstractObjectZZZ implements Runnable{

	@Override
	public void run() {
		
		try {
		String sLog;
		long lcount = 0;
		
		while(true) {

			sLog = ReflectCodeZZZ.getPositionCurrent() + "Zaehler " + lcount;
			this.logProtocolString(sLog);
			
			Thread.sleep(500);
			lcount++;
		}
		
		
		} catch (ExceptionZZZ | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
