package config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
	
	private final String currencyName;
	private final Map<String, String> options;	
	private final ArrayList<String> pools;
	
	public Configuration(String currencyName){
		this.currencyName = currencyName;
		this.options = new HashMap<String, String>();
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
		return this.options.get(ParameterConstants.wallet);
	}
	
	public String getPassword(){
		return this.options.get(ParameterConstants.psw);
	}

	public String getHashCnt() { return this.options.get(ParameterConstants.hashCnt); }

	public String getAlgo() { return this.options.get(ParameterConstants.algo); }

	public String getEnabledCards() { return this.options.get(ParameterConstants.enabledCards); }

	public String getRestartIn() { return  this.options.get(ParameterConstants.restart ); }
	
	// OPTIONS
	
	private void setDefaultValues(){
		this.options.put(ParameterConstants.psw, "x");
		this.options.put(ParameterConstants.wallet, "");
		this.options.put(ParameterConstants.algo, "1");
		this.options.put(ParameterConstants.debug, "-1");
		this.options.put(ParameterConstants.restart, "1");
		this.options.put(ParameterConstants.allpools, "1");
		this.options.put(ParameterConstants.allcoins, "1");
	}
	
	public Configuration walletAddr(String address){
		this.options.put(ParameterConstants.wallet, address);
		return this;
	}
	
	public Configuration password(String psw){
		this.options.put(ParameterConstants.psw, psw);
		return this;
	}
	
	public Configuration enableWatchDog(){
		this.options.put(ParameterConstants.watchDog, "1");
		return this;
	}
	
	public Configuration lowIntesity() {
		this.options.put(ParameterConstants.lowint, "1");
		return this;
	}
	
	public Configuration hashCount(String hashCnt){
		this.options.put(ParameterConstants.hashCnt, hashCnt);
		return this;
	}
	
	public Configuration algorithm(String algo) {
		this.options.put(ParameterConstants.algo, algo);
		return this;
	}

	public Configuration enabledCards(String cards){
		if (! cards.equals("")) {
			this.options.put(ParameterConstants.enabledCards, cards);
		}
		return this;
	}

	public Configuration restartIn(String time){
		this.options.put(ParameterConstants.restart, time);
		return this;
	}

	//Boost
	
	public Configuration targetTemp(String temperature){
		this.options.put(ParameterConstants.targetTemp, temperature);
		return this;
	}
	
	public Configuration fanMin(String fanMin){
		this.options.put(ParameterConstants.fanmin, fanMin);
		return this;
	}
	
	public Configuration fanMax(String fanMax){
		this.options.put(ParameterConstants.fanmax, fanMax);
		return this;
	}
	
	public Configuration cClock(String cClock){
		this.options.put(ParameterConstants.cclock, cClock);
		return this;
	}
	
	public Configuration mClock(String mClock){
		this.options.put(ParameterConstants.mclock, mClock);
		return this;
	}
	
	public Configuration powLim(String powLim){
		this.options.put(ParameterConstants.powlim, powLim);
		return this;
	}
	
	// POOLS
	
	public Configuration addPool(String poolAddr){
		this.pools.add(poolAddr);
		return this;
	}
	
	private static class ParameterConstants {
		static final String algo = "-a";
		static final String allcoins = "-allcoins";
		static final String allpools = "-allpools";
		static final String cclock = "-cclock";
		static final String debug = "-dbg";
		static final String enabledCards = "-di";
		static final String fanmax = "-fanmax";
		static final String fanmin = "-fanmin";
		static final String hashCnt = "-h";
		static final String lowint = "-li";
		static final String mclock = "-mclock";
		static final String pool = "-o";
		static final String powlim = "-powlim";
		static final String psw = "-p";
		static final String restart = "-r";
		static final String targetTemp = "-tt";
		static final String wallet = "-u";
		static final String watchDog = "-wd";	
	}
}
