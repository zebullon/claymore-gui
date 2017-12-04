package config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Configuration {
	private static final String ALL_POOLS = "-allpools";
	private static final String ALL_COINS = "-allcoins";
	private static final String ASM_MODE = "-asm";
	private static final String CORE_CLOCK = "-cclock";
	private static final String DEBUG = "-dbg";
	private static final String ENABLED_CARDS = "-di";
	private static final String FAN_MAX = "-fanmax";
	private static final String FAN_MIN = "-fanmin";
	private static final String LOW_INT = "-li";
	private static final String MEM_CLOCK = "-mclock";
	private static final String NO_FEE = "-nofee";
	private static final String POW_LIM = "-powlim";
	private static final String RESTART = "-r";
	private static final String TARGET_TEMP = "-tt";
	private static final String WATCH_DOG = "-wd";

	protected abstract String getWalletKey();
	protected abstract String getPasswordKey();
	protected abstract String getAlgoKey();
	protected abstract String getIntensityKey();

	private final String currencyName;
	protected final Map<String, String> options;
	private final ArrayList<String> pools;

	public static Configuration getNewConfig(String currencyName){
		switch (Environment.getCurrentAlgorythm()){
			case ETHASH: return new EthashConfiguration(currencyName);
			case EQUIHASH: return new EquihashConfiguration(currencyName);
			default: return new CryptoNightConfiguration(currencyName);
		}
	}

	protected Configuration(String currencyName){
		this.currencyName = currencyName;
		this.options = new HashMap<String, String>(){
			@Override
			public String put (String key, String value){
				if (value.equals("")){
					return this.remove(key);
				}
				return super.put(key, value);
			}
		};

		this.pools = new ArrayList<String>();
		setDefaultValues();

		Environment.getCachedConfigs().put(currencyName, this);
	}
	
	public void writeToFile(){
		FileProxy.writeConfigToFile(this);
	}
	
	public Map<String, String> getOptions(){
		return this.options;
	}
	
	public String getCurrencyName(){
		return this.currencyName;
	}
	
	public ArrayList<String> getPools(){
		return this.pools;
	}
	
	public String getWallet(){
		return this.options.get(getWalletKey());
	}
	
	public String getPassword(){
		return this.options.get(getPasswordKey());
	}

	public String getHashCnt() { return null; }

	public String getAlgo() { return this.options.get(getAlgoKey()); }

	public boolean isAsmMode() { return this.options.containsKey(ASM_MODE); }

	public String getIntensity() { return this.options.get(getIntensityKey());}

	public String getEnabledCards() { return this.options.get(ENABLED_CARDS); }

	public String getRestartIn() { return  this.options.get(RESTART); }

	public boolean isLowIntensity() { return this.options.containsKey(LOW_INT); }

	public boolean isNoFee() {
		return this.options.containsKey(NO_FEE);
	}
	
	// OPTIONS
	
	protected void setDefaultValues(){
		this.options.put(getPasswordKey(), "x");
		this.options.put(getWalletKey(), "");
		this.options.put(getAlgoKey(), "1");
		this.options.put(DEBUG, "-1");
		this.options.put(RESTART, "1");
		this.options.put(ALL_POOLS, "1");
		this.options.put(ALL_COINS, "1");
	}
	
	public Configuration walletAddr(String address){
		this.options.put(getWalletKey(), address);
		return this;
	}
	
	public Configuration password(String psw){
		this.options.put(getPasswordKey(), psw);
		return this;
	}
	
	public Configuration enableWatchDog(){
		this.options.put(WATCH_DOG, "1");
		return this;
	}
	
	public Configuration lowIntesity(boolean lowInt) {
		if(lowInt) {
			this.options.put(LOW_INT, "1");
		} else {
			this.options.remove(LOW_INT);
		}
		return this;
	}
	
	public Configuration hashCount(String hashCnt){
		return this;
	}

	public Configuration intensity(String intencity){
		this.options.put(getIntensityKey(), intencity);
		return this;
	}

	public Configuration asmMode(boolean asmMode){
		this.options.put(ASM_MODE, asmMode ? "1" : "0");
		return this;
	}
	
	public Configuration algorithm(String algo) {
		this.options.put(getAlgoKey(), algo);
		return this;
	}

	public Configuration enabledCards(String cards){
		if (! cards.equals("ALL CARDS")){
			this.options.put(ENABLED_CARDS, cards);
		}
		return this;
	}

	public Configuration restartIn(String time){
		this.options.put(RESTART, time);
		return this;
	}

	public Configuration noFee(boolean noFee){
		if (noFee) {
			this.options.put(NO_FEE, "1");
		} else {
			this.options.remove(NO_FEE);
		}
		return this;
	}

	//Boost
	
	public Configuration targetTemp(String temperature){
		this.options.put(TARGET_TEMP, temperature);
		return this;
	}
	
	public Configuration fanMin(String fanMin){
		this.options.put(FAN_MIN, fanMin);
		return this;
	}
	
	public Configuration fanMax(String fanMax){
		this.options.put(FAN_MAX, fanMax);
		return this;
	}
	
	public Configuration cClock(String cClock){
		this.options.put(CORE_CLOCK, cClock);
		return this;
	}
	
	public Configuration mClock(String mClock){
		this.options.put(MEM_CLOCK, mClock);
		return this;
	}
	
	public Configuration powLim(String powLim){
		this.options.put(POW_LIM, powLim);
		return this;
	}
	
	// POOLS
	
	public Configuration addPool(String poolAddrs){
		String[] poolAddresses = poolAddrs.trim().split(",");
		for (String poolAddr : poolAddresses) {
			this.pools.add(poolAddr);
		}
		return this;
	}
	

}
