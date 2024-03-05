package debug.zBasic.util.moduleExternal.process.create;

public class ProcessCreateMainZZZ {

	public static void main(String[] args) {
	
		ProcessCreateRunnerMockZZZ runner = new ProcessCreateRunnerMockZZZ();
	
		
		Thread objThreadMonitor = new Thread(runner);
		objThreadMonitor.start();//Damit wird run() aufgerufen, was wiederum start_() als private Methode aufruft
		
	}

}
