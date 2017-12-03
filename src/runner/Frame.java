package runner;

import config.Algorythm;
import config.Configuration;
import config.Environment;
import config.FileProxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zebullon on 23.11.17.
 */
public class Frame extends JFrame {
    private JTextField textWallet = new JTextField();
    private JTextField textPassword = new JTextField("x");
    private JTextField textMiningIntencity = new JTextField();
    private JTextField textHashCnt = new JTextField("1024", 4);
    private JTextField textPool = new JTextField("", 20);
    private JComboBox comboBoxAlgo = new JComboBox();
    private JCheckBox checkBoxAsmMode = new JCheckBox();
    private JComboBox comboBoxCurrency = new JComboBox();
    private JTextField textEnabledCards = new JTextField("ALL CARDS");
    private JComboBox comboBoxAlgorythm = new JComboBox();
    private JTextField textRestartIn = new JTextField("1");
    private JCheckBox checkBoxNoFee = new JCheckBox();
    private JCheckBox checkBoxStart = new JCheckBox();
    private JCheckBox checkBoxLowIntensity = new JCheckBox();
    private JButton buttonSave = new JButton("Save");
    private JButton buttonRun = new JButton("Run");

    private final JComponent[] components = new JComponent[]{
            new JLabel("Mining:"), comboBoxAlgorythm,
            new JLabel("Currency:"), comboBoxCurrency,
            new JLabel("Algorythm:"), comboBoxAlgo,
            new JLabel("ASM mode:"), checkBoxAsmMode,
            new JLabel("Wallet:"), textWallet,
            new JLabel("Password (e-mail):"), textPassword,
            new JLabel("Mining intensity:"), textMiningIntencity,
            new JLabel("Hash count:"), textHashCnt,
            new JLabel("Pools:"), textPool,
            new JLabel("Enabled cards:"), textEnabledCards,
            new JLabel("Restart in:"), textRestartIn,
            new JLabel("Low intensity mode:"), checkBoxLowIntensity,
            new JLabel("No fee:"), checkBoxNoFee,
            buttonSave, buttonRun
    };

    public Frame(){
        super("Claymore Runner");
        this.setBounds(100,100,800,300);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridLayout mainLayout = new GridLayout(components.length / 2,2,2,2);

        Container container = this.getContentPane();
        container.setLayout(mainLayout);

        initAlgorythm();
        lockForbiddenFields();
        initCurrencies();
        initButtonSave();
        initButtonRun();

        addComponentsToContainer(container);
    }

    private void initButtonSave(){
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveConfig();
            }
        });
    }

    private void initButtonRun(){
        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveConfig();
                //FileProxy.setStartToAutoLoad(checkBoxStart.isSelected());
                ClaymoreRunner.runMiner(comboBoxCurrency.getSelectedItem().toString());
            }
        });
    }

    private void saveConfig(){
        String currency = comboBoxCurrency.getSelectedItem().toString();
        Configuration config = Configuration.getNewConfig(currency)
                .algorithm(String.valueOf(comboBoxAlgo.getSelectedIndex() + 1))
                .walletAddr(textWallet.getText())
                .password(textPassword.getText())
                .intensity(textMiningIntencity.getText())
                .asmMode(checkBoxAsmMode.isSelected())
                .addPool(textPool.getText())
                .enabledCards(textEnabledCards.getText())
                .restartIn(textRestartIn.getText())
                .lowIntesity(checkBoxLowIntensity.isSelected())
                .noFee(checkBoxNoFee.isSelected());

        if (Environment.getCurrentAlgorythm() == Algorythm.CRYPTONIGHT){
            config.hashCount(textHashCnt.getText());
        }

        config.writeToFile();
        saveLastCurrency();
    }

    private void addComponentsToContainer(Container container){
        for (JComponent component : components){
            if (component instanceof JLabel){
                ((JLabel)component).setHorizontalAlignment(JLabel.RIGHT);
            }
            container.add(component);
        }
    }

    private void initAlgorythm(){
        comboBoxAlgorythm.setModel(new DefaultComboBoxModel(Environment.getAlgorythmsList()));
        Environment.setCurrentAlgorythm(comboBoxAlgorythm.getSelectedItem().toString());
        comboBoxAlgorythm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxCurrency.setModel(new DefaultComboBoxModel(FileProxy.getCurrenciesList()));
                Environment.setCurrentAlgorythm(comboBoxAlgorythm.getSelectedItem().toString());
                lockForbiddenFields();
                initCurrencies();
                loadConfigData();
            }
        });
    }

    private void lockForbiddenFields(){
        boolean isCryptoNight = Environment.getCurrentAlgorythm() == Algorythm.CRYPTONIGHT;

        checkBoxAsmMode.setEnabled(! isCryptoNight);
        textHashCnt.setEnabled(isCryptoNight);
        textMiningIntencity.setEnabled(! isCryptoNight);
    }

    private void initCurrencies(){
        comboBoxCurrency.setModel(new DefaultComboBoxModel(FileProxy.getCurrenciesList()));
        loadConfigData();
        comboBoxCurrency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                loadConfigData();
            }
        });
    }

    private void initAlgo(){
        comboBoxAlgo.setEnabled(true);

        switch (Environment.getCurrentAlgorythm()){
            case CRYPTONIGHT: comboBoxAlgo.setModel(new DefaultComboBoxModel(new String[]{"a1", "a2", "a3", "a4"})); break;
            case EQUIHASH: comboBoxAlgo.setModel(new DefaultComboBoxModel(new String[]{"a1", "a2", "a3"})); break;
            default: comboBoxAlgo.setEnabled(false);
        }
    }

    private void loadConfigData(){
        String selectedCurrency = comboBoxCurrency.getSelectedItem().toString();
        Configuration config = Environment.getCachedConfigs().get(selectedCurrency);

        if (config == null){
            config = FileProxy.readConfigFromFile(selectedCurrency);
        }

        initAlgo();

        if (config != null) {
            this.textWallet.setText(config.getWallet());
            this.textPassword.setText(config.getPassword());
            this.textMiningIntencity.setText(config.getIntensity());
            this.checkBoxAsmMode.setSelected(config.isAsmMode());
            this.textHashCnt.setText(config.getHashCnt());
            this.textPool.setText(loadPools(config));
            this.comboBoxAlgo.setSelectedIndex(Integer.parseInt(config.getAlgo()) - 1 );
            if (config.getEnabledCards() != null) {
                this.textEnabledCards.setText(config.getEnabledCards());
            } else {
                this.textEnabledCards.setText("ALL CARDS");
            }
            this.textRestartIn.setText(config.getRestartIn());
            this.checkBoxLowIntensity.setSelected(config.isLowIntensity());
            this.checkBoxNoFee.setSelected(config.isNoFee());
        } else {
            this.textWallet.setText("");
            this.textPassword.setText("x");
            this.textMiningIntencity.setText("");
            this.checkBoxAsmMode.setSelected(Environment.getCurrentAlgorythm() != Algorythm.CRYPTONIGHT);
            this.textHashCnt.setText("1024");
            this.textPool.setText("");
            this.comboBoxAlgo.setSelectedIndex(0);
            this.textEnabledCards.setText("ALL CARDS");
            this.textRestartIn.setText("1");
            this.checkBoxLowIntensity.setSelected(false);
            this.checkBoxNoFee.setSelected(false);
        }
    }

    private String loadPools(Configuration config){
        StringBuilder builder = new StringBuilder();
        int poolsCount = config.getPools().size();

        for (int i = 0; i < poolsCount; i++){
            builder.append(config.getPools().get(i));
            if (i < poolsCount - 1){
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private void saveLastCurrency(){
        FileProxy.saveLastCurrency(Environment.getCurrentAlgorythm(), comboBoxCurrency.getSelectedItem().toString());
    }
}