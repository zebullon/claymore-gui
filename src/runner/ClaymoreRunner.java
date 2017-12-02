package runner;

import java.io.IOException;
import java.io.File;

import config.Environment;
import config.FileProxy;

import javax.swing.*;

public class ClaymoreRunner {
	
	public static void main(String[] args) {
		try {
			Frame main = new Frame();
			main.setVisible(true);
		} catch (Exception e){
			ErrorFrame errorFrame = new ErrorFrame(e.getMessage());
			errorFrame.setVisible(true);
		}
	}

	private static Process claymoreProcess = null;

	public static void runMiner(String currencyName){
		String command;
		File minerPath = FileProxy.getMinerPath(Environment.getCurrentAlgorythm());

		try {
			if (claymoreProcess == null) {
				FileProxy.prepareConfigFiles(currencyName);
					command = String.format("cmd /c start start.bat");
					claymoreProcess = Runtime.getRuntime().exec(command, null, minerPath);
			} else {
				command = "taskkill /F /IM NsGpuCNMiner.exe";
				Runtime.getRuntime().exec(command, null, minerPath);
				claymoreProcess.destroy();
				claymoreProcess = null;
				runMiner(currencyName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
