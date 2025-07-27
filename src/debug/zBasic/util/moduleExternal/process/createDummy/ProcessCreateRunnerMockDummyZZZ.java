package debug.zBasic.util.moduleExternal.process.createDummy;

import basic.zBasic.AbstractObjectWithExceptionZZZ;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;

public class ProcessCreateRunnerMockDummyZZZ extends AbstractObjectWithExceptionZZZ implements Runnable{

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
		
		//Hier Beenden notwendig, damit auch die JVM fuer diesen Process beendet wird.
		//Merke: Das nicht in der aufrufenden ...main - Klasse machen, dann wird sofort auch dieser Process beendet.
		System.exit(0);
		
		
		} catch (ExceptionZZZ | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
