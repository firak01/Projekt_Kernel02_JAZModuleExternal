package basic.zBasic.util.log.watch;

import java.io.File;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;

public class LogFileWatchRunnerMainZZZ {

	public static void main(String[] args) {
		String sFile = "";
		File objLogFile = new File(sFile);
		
		try {
			LogFileWatchRunnerZZZ objRunner = new LogFileWatchRunnerZZZ(objLogFile);
			
			Thread objThreadRunner = new Thread(objRunner);
			objThreadRunner.start();
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Test: Kann man so den lauf anhalten...
			System.out.println("Versuche anzuhalten");
			objRunner.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);
			System.out.println("Versuche anzuhalten... Erfolgreich?");
		} catch (ExceptionZZZ e) {
			
			e.printStackTrace();
		}
		
	}

}
