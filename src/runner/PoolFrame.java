package runner;

import config.Pool;

import javax.swing.*;

public class PoolFrame extends JFrame {
    private JTextField textPoolAddress;
    private JTextField textPoolWallet;
    private JTextField textPoolPaymentId;
    private JTextField textPoolWorker;
    private JTextField textPoolEmail;
    private JTextField textPoolPassword;
    private JTextField textPoolFormat;

    private JButton buttonSave;
    private JButton buttonCancel;

    public PoolFrame(Pool pool){
        this.textPoolAddress = new JTextField(pool.getPoolAddress());
        this.textPoolWallet = new JTextField(pool.getWallet());
        this.textPoolPaymentId = new JTextField(pool.getPaymentId());
        this.textPoolWorker = new JTextField(pool.getWorker());
        this.textPoolEmail = new JTextField(pool.getEmail());
        this.textPoolPassword = new JTextField(pool.getPassword());
        this.textPoolFormat = new JTextField(pool.getFormat());

        this.buttonSave = new JButton("Save");
        this.buttonCancel = new JButton("Cancel");
    }
}
