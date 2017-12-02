package config;

public enum Algorythm {
    CRYPTONIGHT("CryptoNight"),
    ETHASH("Ethash"),
    EQUIHASH("Equihash");

    private final String value;

    private Algorythm(String val){
        this.value = val;
    }

    @Override
    public String toString(){
        return value;
    }

    public static Algorythm getAlgorythm(String algorythm){
        switch (algorythm){
            case "CryptoNight" : return CRYPTONIGHT;
            case "Ethash" : return ETHASH;
            case "Equihash" : return EQUIHASH;
            default: throw new RuntimeException("Algorytm " + algorythm + "is not found");
        }
    }
}
