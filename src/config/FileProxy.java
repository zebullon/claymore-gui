package config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map.Entry;
import java.util.ArrayList;

public class FileProxy {
	
	private static final File resourcesDir, configDir, poolDir;
	private static final File cryptoNightDir, ethashDir, equihashDir;
	private static final String config = "config.txt";
	private static final String epools = "epools.txt";
	private static final String currencies = "currencies.txt";
	private static final String lastCurrency = "lastCurrency.txt";
	private static final String claymore = "Claymore";

	static {
		resourcesDir = getFilePath(String.format("%s/resources", System.getProperty("user.dir")));
		configDir = getFilePath(String.format("%s/configs", resourcesDir.getAbsolutePath()));
		cryptoNightDir = getFilePath(String.format("%s/CryptoNight", resourcesDir.getAbsolutePath()));
		ethashDir = getFilePath(String.format("%s/Ethash", resourcesDir.getAbsolutePath()));
		equihashDir = getFilePath(String.format("%s/Equihash", resourcesDir.getAbsolutePath()));
		poolDir = getFilePath(String.format("%s/pools", resourcesDir.getAbsolutePath()));
	}
	
	private static File getFilePath(String directory){
		File dir = new File(directory);
		if (! dir.exists()){
			dir.mkdirs();
		}
		return dir;
	}

	private static String getConfigFileName(String currencyName){
		return String.format("%s/%s_%s", configDir, currencyName, config);
	}

	private static String getEpoolsFileName(String currencyName){
		return String.format("%s/%s_%s", poolDir, currencyName, epools);
	}

	public static Configuration readConfigFromFile(String currencyName){

		BufferedReader configReader = null;
		BufferedReader epoolsReader = null;
		
		Configuration newConfig = null;
		
		try {
			configReader = getReader(getConfigFileName(currencyName));
			epoolsReader = getReader(getEpoolsFileName(currencyName));

			if (configReader == null) {
				return null;
			}

			newConfig = Configuration.getNewConfig(currencyName);

			while (configReader.ready()) {
				String line = configReader.readLine();
				if (! line.startsWith("#")) {
					String[] option = line.split("\\s+");

					newConfig.getOptions().put(option[0], option[1]);
				}
			}

			while (epoolsReader.ready()) {
				String line = epoolsReader.readLine();
				if (!line.startsWith("#")) {
					String[] pool = line.split(",");
					pool = pool[0].split("\\s+");
					if (pool[1].startsWith("stratum+tcp://")) {
						newConfig.getPools().add(pool[1]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            closeReader(configReader);
            closeReader(epoolsReader);
		}

		return newConfig;
	}
	
    public static void writeConfigToFile(Configuration conf){
		BufferedWriter configWriter = null;
    	BufferedWriter epoolsWriter = null;
    	try {
    		configWriter = getWriter(String.format("%s/%s_%s", configDir.getAbsolutePath(), conf.getCurrencyName(), config));
        	epoolsWriter = getWriter(String.format("%s/%s_%s", poolDir.getAbsolutePath(), conf.getCurrencyName(), epools));
    		
    		for (Entry<String, String> entry : conf.getOptions().entrySet()){
    			if (entry.getKey() != null) {
					configWriter.write(String.format("%s %s", entry.getKey(), entry.getValue()));
					configWriter.newLine();
				}
			}
    		    		
    		for (String pool : conf.getPools()){
    			epoolsWriter.write(String.format("POOL: %s", pool));
    			epoolsWriter.newLine();
    		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeWriter(configWriter);
			closeWriter(epoolsWriter);
		}
    }

    public static String[] getCurrenciesList(){
    	String[] currenciesList = null;
		String lastUsedCurrency = loadLastCurrency(Environment.getCurrentAlgorythm());
		ArrayList<String> sortedList = new ArrayList();

    	BufferedReader reader = getReader(String.format("%s/%s", getCurrencyPath(Environment.getCurrentAlgorythm()), currencies));
    	try {
			currenciesList = reader.readLine().split(",\\s+");
			if (! lastUsedCurrency.equals("")) {
				sortedList.add(lastUsedCurrency);
			}

			for (String currency : currenciesList) {
				if (! currency.equals(lastUsedCurrency)) {
					sortedList.add(currency);
				}
			}

		} catch (IOException e){
    		e.printStackTrace();
		} finally {
			closeReader(reader);
		}
		return sortedList.toArray(new String[sortedList.size()]);
	}

    private static BufferedReader getReader(String fileName){
    	File file = new File(fileName);    	
    	
		try {
			FileInputStream input = new FileInputStream(file);
	    	InputStreamReader isr = new InputStreamReader(input);
	    	return new BufferedReader(isr);
		} catch (FileNotFoundException e) {
			return null;
		}
    }
    
    private static BufferedWriter getWriter(String fileName){
    	File file = new File(fileName);
    	
    	try {
			file.createNewFile();
			FileOutputStream output = new FileOutputStream(file);		
			OutputStreamWriter osw = new OutputStreamWriter(output);
			return new BufferedWriter(osw);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

    public static void prepareConfigFiles(String currencyName){
    	Path sourceConfigPath = new File(getConfigFileName(currencyName)).toPath();
		Path sourceEpoolsPath = new File(getEpoolsFileName(currencyName)).toPath();

    	Path targetConfigPath = new File(String.format("%s/%s/%s", getCurrencyPath(Environment.getCurrentAlgorythm()), claymore, config)).toPath();
    	Path targetEpoolsPath = new File(String.format("%s/%s/%s", getCurrencyPath(Environment.getCurrentAlgorythm()), claymore, epools)).toPath();

    	try {
			Files.copy(sourceConfigPath, targetConfigPath, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(sourceEpoolsPath, targetEpoolsPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e){
    		throw new RuntimeException(e);
		}
	}
    
    private static void closeReader(Reader reader){
    	try {
	    	reader.close();
	    } catch (NullPointerException e) { 
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
    }
    
    private static void closeWriter(Writer writer){
    	try {
		    writer.close();
		} catch (NullPointerException e) { 
		} catch (IOException e) {
		    e.printStackTrace();
		}    	
    }
    
    public static String getCurrencyPath(Algorythm algorythm){
    	switch (algorythm){
			case CRYPTONIGHT : return cryptoNightDir.getAbsolutePath();
			case ETHASH : return ethashDir.getAbsolutePath();
			case EQUIHASH : return equihashDir.getAbsolutePath();
			default: throw new RuntimeException("Selected algorythm is not found");
		}
    }

	public static File getMinerPath(Algorythm algorythm){
		File path = Environment.getMinerPaths().get(algorythm);

		if (path == null){
			path = new File(String.format("%s/%s", FileProxy.getCurrencyPath(algorythm), claymore));
			Environment.getMinerPaths().put(algorythm, path);
		}
		return path;
	}

    public static void saveLastCurrency(Algorythm algorythm, String configName){
		Writer writer = getWriter(String.format("%s/%s", getCurrencyPath(algorythm), lastCurrency));
		try {
			writer.write(configName);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeWriter(writer);
		}
	}

	public static String loadLastCurrency(Algorythm algorythm){
		BufferedReader reader = getReader(String.format("%s/%s", getCurrencyPath(algorythm), lastCurrency));
		String result = null;

		try {
			result = reader == null ? "" : reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			closeReader(reader);
		}
		return result;
	}

	public static void logError(Exception ex){
		PrintWriter errLogWriter = null;
		try {
			errLogWriter = new PrintWriter(String.format("%s/%s", System.getProperty("user.dir"), "logError.txt"));
			errLogWriter.println(ex.getMessage());
			ex.printStackTrace(errLogWriter);
		} catch (IOException e){
			System.console().writer().println(e.getMessage());
		} finally {
			errLogWriter.close();
		}
	}

//	public static void setStartToAutoLoad(boolean start){
//		Path link = new File(getStartupFolder()).toPath();
//		Path existing = new File(String.format("%s/%s", getMinerPath(Environment.getCurrentAlgorythm()).toString(), "start.lnk")).toPath();
//		java.util.Set<PosixFilePermission> permissions = new HashSet<>();
//		permissions.add(PosixFilePermission.OTHERS_WRITE);
//
//
//		try {
//			Files.setPosixFilePermissions(link, permissions);
//			Files.copy(existing, link, StandardCopyOption.REPLACE_EXISTING);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	private static String getStartupFolder() {
//		return System.getProperty("java.io.tmpdir").replace("Local\\Temp\\", "Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup");
//	}
}
