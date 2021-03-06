package config;

public class CryptoNightConfiguration extends Configuration {

    private static final String HASH_COUNT = "-h";

    protected CryptoNightConfiguration(String currencyName){
        super(currencyName);
    }

    @Override
    protected String getWalletKey() {
        return "-u";
    }

    @Override
    protected String getPasswordKey() {
        return "-p";
    }

    @Override
    protected String getAlgoKey() {
        return "-a";
    }

    @Override
    protected String getIntensityKey() {
        return null;
    }

    @Override
    public String getHashCnt(){
        return this.options.get(HASH_COUNT);
    }

    @Override
    public boolean isAsmMode() {
        return false;
    }

    @Override
    protected void setDefaultValues(){
        super.setDefaultValues();
        this.options.put(getAlgoKey(), "1");
    }

    @Override
    public Configuration hashCount(String hashCnt){
        this.options.put(HASH_COUNT, hashCnt);
        return this;
    }

    @Override
    public Configuration intensity(String intencity){
        return this;
    }

    @Override
    public Configuration asmMode(boolean asmMode){
        return this;
    }
}
