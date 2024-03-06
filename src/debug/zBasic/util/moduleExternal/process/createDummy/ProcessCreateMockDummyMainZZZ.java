package debug.zBasic.util.moduleExternal.process.createDummy;

public class ProcessCreateMockDummyMainZZZ {

	public static void main(String[] args) {
	
		ProcessCreateRunnerMockDummyZZZ runner = new ProcessCreateRunnerMockDummyZZZ();
	
		
		Thread objThreadMonitor = new Thread(runner);
		objThreadMonitor.start();//Damit wird run() aufgerufen, was wiederum start_() als private Methode aufruft
		
	}

}
