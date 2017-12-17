package config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

public class Environment {
	private static final Map<String, Configuration> cachedConfigs;
	private static final Map<String, List<Pool>> pools;
	private static final Map<Algorythm, File> minerPaths;
	private static final String[] algorythmsList;
	private static Algorythm currentAlgorythm;

	static {
		cachedConfigs = new HashMap<String, Configuration>();
		pools = new HashMap<String, List<Pool>>();
		minerPaths = new HashMap<Algorythm, File>();
		algorythmsList =  new String[] { Algorythm.CRYPTONIGHT.toString(), Algorythm.ETHASH.toString(), Algorythm.EQUIHASH.toString()};
	}
	
	public static Map<String, Configuration> getCachedConfigs(){
		return cachedConfigs;
	}

	public static List<Pool> getPools(String currency) {
		if (!pools.containsKey(currency)) {
			pools.put(currency, new ArrayList<Pool>());
		}
		return pools.get(currency);
	}

	public static Pool addPool(String currency, Pool pool){
		getPools(currency).add(pool);
		return pool;
	}

	public static void removePool(String currency, String poolAddr){
		getPools(currency).removeIf(p -> p.getPoolAddress().equals(poolAddr));
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
