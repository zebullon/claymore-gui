package config;

public class Pool {

    private String poolAddress = "";
    private String wallet = "";
    private String paymentId = "";
    private String worker = "";
    private String email = "";
    private String password = "x";
    private String format = "WALLET";

    public Pool() {}

    public Pool(String poolAddress){
        this.poolAddress = poolAddress;
    }

    public Pool poolAddress(String poolAddress) {
        this.poolAddress = poolAddress;
        return this;
    }

    public Pool wallet(String wallet) {
        this.wallet = wallet;
        return this;
    }

    public Pool paymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public Pool worker(String worker) {
        this.worker = worker;
        return this;
    }

    public Pool email(String email) {
        this.email = email;
        return this;
    }

    public Pool password(String password){
        this.password = password;
        return this;
    }

    public Pool format(String format) {
        this.format = format;
        return this;
    }

    public String getPoolAddress() {
        return poolAddress.trim();
    }

    public String getWorker() {
        return worker.trim();
    }

    public String getWallet() {
        return wallet.trim();
    }

    public String getPaymentId() {
        return paymentId.trim();
    }

    public String getEmail() {
        return email.trim();
    }

    public String getPassword() { return password.trim(); }

    public String getFormat() { return format.trim(); }
}
