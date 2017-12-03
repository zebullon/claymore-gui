package config;

public class EquihashConfiguration extends Configuration {

    protected EquihashConfiguration(String currencyName){
        super(currencyName);
    }

    @Override
    protected String getWalletKey() {
        return "-zwal";
    }

    @Override
    protected String getPasswordKey() {
        return "-zpsw";
    }

    @Override
    protected String getAlgoKey() {
        return "-a";
    }

    @Override
    protected String getIntensityKey() {
        return "-i";
    }
}