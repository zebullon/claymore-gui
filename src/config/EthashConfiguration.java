package config;

public class EthashConfiguration extends Configuration {

    protected EthashConfiguration(String currencyName){
        super(currencyName);
    }

    @Override
    protected String getWalletKey() {
        return "-ewal";
    }

    @Override
    protected String getPasswordKey() {
        return "-epsw";
    }

    @Override
    protected String getAlgoKey() {
        return null;
    }

    @Override
    protected String getIntensityKey() {
        return "-ethi";
    }
}
