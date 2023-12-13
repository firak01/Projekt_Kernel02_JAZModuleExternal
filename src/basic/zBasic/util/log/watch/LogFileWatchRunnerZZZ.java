package basic.zBasic.util.log.watch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.component.AbstractProgramRunnableZZZ;
import basic.zBasic.component.AbstractProgramZZZ;
import basic.zBasic.component.IModuleZZZ;
import basic.zBasic.component.IProgramRunnableZZZ;
import basic.zBasic.util.abstractArray.ArrayUtilZZZ;
import basic.zBasic.util.file.FileEasyZZZ;

public class LogFileWatchRunnerZZZ extends AbstractProgramRunnableZZZ implements ILogFileWatchRunnerZZZ {
	private static final long serialVersionUID = 6586079955658760005L;
	public File objLogFile=null;
	
	public LogFileWatchRunnerZZZ() throws ExceptionZZZ {
		super();		
	}

	public LogFileWatchRunnerZZZ(File objLogFile) throws ExceptionZZZ {
		super();	
		LogFileWatchRunnerNew_(null, objLogFile);
	}
	
	private boolean LogFileWatchRunnerNew_(IModuleZZZ objModule, File objLogFile) {
		boolean bReturn = false;
		main:{
			this.objLogFile = objLogFile;
			this.objModule = objModule;
		}//end main:
		return bReturn;
	}

	//#### GETTER / SETTER
	@Override
	public File getLogFileWatched() {
		return this.objLogFile;
	}

	@Override
	public void setLogFileWatched(File objLogFile) {
		this.objLogFile = objLogFile;
	}
	
	@Override
	public boolean start() throws ExceptionZZZ, InterruptedException{
		boolean bReturn = false;
		main:{
//			this.setFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP, true);
//			
//			 boolean bValue = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP);
//			 System.out.println("bValue = " + bValue);
//				
//			this.reset();
//			
//		    bValue = this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP);
//			System.out.println("bValue = " + bValue);
//		    
			
			int icount=0;
			while(true) {
				icount++;
				System.out.println("mache was " + icount);
				Thread.sleep(100);				
				if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
					break;
				}
				this.startServerProcessLogWacher();
			}
			
			bReturn = true;
		}//end main:
		return bReturn;
	}
	
	
	/** Das klappt... man kann das LogFile auslesen,
	 *  welches immer weiter neu vom OVPN-Server gefüllt wird.
	 * @return
	 * @author Fritz Lindhauer, 10.12.2023, 16:04:55
	 */
	public boolean startServerProcessLogWacher() {
		boolean bReturn= false;
		main:{			
			BufferedReader br=null;
			try {
				String sLogDirectory =  "c:\\fglkernel\\kernellog\\ovpnServer";
				String sLogFile = "ovpn.log";
				String sLogFilePathTotal =	FileEasyZZZ.joinFilePathName(sLogDirectory, sLogFile);		
				File objFileLog = new File(sLogFilePathTotal);
								
				boolean bExists = false;
				do {
					if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
						break main;
					}
					bExists = FileEasyZZZ.exists(objFileLog);
					if(!bExists) {
						Thread.sleep(5000);
					}
				}while(!bExists);
				
				String sLine = null;
				InputStream objStream = new FileInputStream(objFileLog);
				br = new BufferedReader(new InputStreamReader(objStream));
                while (true){
                	if(this.getFlag(IProgramRunnableZZZ.FLAGZ.REQUESTSTOP)) {
    					break main;
    				}
                    sLine = br.readLine();
                    if(sLine!=null)
                    {
                        System.out.println(sLine);
                    }else{
                        Thread.sleep(100);
                    }
                }
			} catch (ExceptionZZZ e) {				
				e.printStackTrace();				
				
			} catch (InterruptedException e) {				
				e.printStackTrace();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(br!=null) {
					IOUtils.closeQuietly(br);
				}
	        }
		}//end main:
		return bReturn;
	}
	
	//###############################
	//### FLAG HANDLING
	//###############################
	@Override
	public boolean getFlag(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag) {
		return this.getFlag(objEnumFlag.name());
	}

	@Override
	public boolean setFlag(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag, boolean bFlagValue)throws ExceptionZZZ {
		return this.setFlag(objEnumFlag.name(), bFlagValue);
	}

	@Override
	public boolean[] setFlag(ILogFileWatchRunnerZZZ.FLAGZ[] objaEnumFlag,boolean bFlagValue) throws ExceptionZZZ {
		boolean[] baReturn=null;
		main:{
			if(!ArrayUtilZZZ.isEmpty(objaEnumFlag)) {
				baReturn = new boolean[objaEnumFlag.length];
				int iCounter=-1;
				for(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag:objaEnumFlag) {
					iCounter++;
					boolean bReturn = this.setFlag(objEnumFlag, bFlagValue);
					baReturn[iCounter]=bReturn;
				}
			}
		}//end main:
		return baReturn;
	}

	@Override
	public boolean proofFlagExists(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag) throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}

	@Override
	public boolean proofFlagSetBefore(ILogFileWatchRunnerZZZ.FLAGZ objEnumFlag)	throws ExceptionZZZ {
		return this.proofFlagExists(objEnumFlag.name());
	}	
}
