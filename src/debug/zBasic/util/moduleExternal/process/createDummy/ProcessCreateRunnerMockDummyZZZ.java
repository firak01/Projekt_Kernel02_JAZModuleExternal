package debug.zBasic.util.moduleExternal.process.createDummy;

import basic.zBasic.AbstractObjectZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;

public class ProcessCreateRunnerMockDummyZZZ extends AbstractObjectZZZ implements Runnable{

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
			
			//Da man diesen per Batch gestareten Process nicht abbrechen kann, per Eclipse "STOP-Button", 
			//wird die Anzahl der Ausgaben begrenzt.
			if(lcount>=1010) {
				sLog = ReflectCodeZZZ.getPositionCurrent() + "Zaehlerende erreicht. Breche ab.";
				this.logProtocolString(sLog);
				break; //Für einen Test schaut sich niemand mehr als 1009 Zeilen an. 
			}
		}
		
		
		} catch (ExceptionZZZ | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
