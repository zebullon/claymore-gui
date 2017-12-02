package config;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

public class Environment {
	private static final Map<String, Configuration> cachedConfigs;
	private static final Map<Algorythm, File> minerPaths;
	private static final String[] algorythmsList;
	private static Algorythm currentAlgorythm;

	static {
		cachedConfigs = new HashMap<String, Configuration>();
		minerPaths = new HashMap<Algorythm, File>();
		algorythmsList =  new String[] { Algorythm.CRYPTONIGHT.toString(), Algorythm.ETHASH.toString(), Algorythm.EQUIHASH.toString()};
	}
	
	public static Map<String, Configuration> getCachedConfigs(){
		return cachedConfigs;
	}

	public static final String[] getAlgorythmsList(){
		return algorythmsList;
	}

	public static void setCurrentAlgorythm(String algorythm){
		currentAlgorythm = Algorythm.getAlgorythm(algorythm);
	}

	public static Algorythm getCurrentAlgorythm(){
		return currentAlgorythm;
	}

	public static final Map<Algorythm, File> getMinerPaths(){
		return minerPaths;
	}
}
