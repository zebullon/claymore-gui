package runner;

import config.Algorythm;
import config.Configuration;
import config.Environment;
import config.FileProxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by zebullon on 23.11.17.
 */
public class Frame extends JFrame {

    private JTabbedPane tabbedPane = new JTabbedPane();
    private JPanel mainPanel = new JPanel();
    private JPanel overclockPanel = new JPanel();

    // Main panel components

    private JTextField textWallet = new JTextField();
    private JTextField textPassword = new JTextField("x");
    private JTextField textMiningIntencity = new JTextField();
    private JTextField textHashCnt = new JTextField("1024", 4);
    private JComboBox comboBoxPool = new JComboBox();
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

    private final JComponent[] mainPanelComponents = new JComponent[]{
            new JLabel("Mining:"), comboBoxAlgorythm,
            new JLabel("Currency:"), comboBoxCurrency,
            new JLabel("Algorythm:"), comboBoxAlgo,
            new JLabel("ASM mode:"), checkBoxAsmMode,
            new JLabel("Wallet:"), textWallet,
            new JLabel("Password (e-mail):"), textPassword,
            new JLabel("Mining intensity:"), textMiningIntencity,
            new JLabel("Hash count:"), textHashCnt,
            new JLabel("Pools:"), comboBoxPool,
            new JLabel("Enabled cards:"), textEnabledCards,
            new JLabel("Restart in:"), textRestartIn,
            new JLabel("Low intensity mode:"), checkBoxLowIntensity,
            new JLabel("No fee:"), checkBoxNoFee,
            buttonSave, buttonRun
    };

    // Overclock panel components

    private JTextField textCoreClock = new JTextField();
    private JTextField textMemoryClock = new JTextField();
    private JTextField textCoreVoltage = new JTextField();
    private JTextField textMemoryVoltage = new JTextField();
    private JTextField textPowerLimit = new JTextField();
    private JTextField textFanMin = new JTextField();
    private JTextField textFanMax = new JTextField();
    private JTextField textTargetTemp = new JTextField();
    private JTextField textStopTemp = new JTextField();

    private final JComponent[] overclockPanelComponents = new JComponent[]{
        new JLabel("Core clock, MHz:"), textCoreClock,
        new JLabel("Memory clock, MHz:"), textMemoryClock,
        new JLabel("Core voltage, mV:"), textCoreVoltage,
        new JLabel("Memory voltage, mV:"), textMemoryVoltage,
        new JLabel("Pover limit:"), textPowerLimit,
        new JLabel("Fan, min rpm:"), textFanMin,
        new JLabel("Fan, max rpm:"), textFanMax,
        new JLabel("Target temperature:"), textTargetTemp,
        new JLabel("Stop temperature:"), textStopTemp,
    };

    public Frame(){
        super("Claymore Runner");
        this.setBounds(100,100,640,440);
        this.setBounds(100,100,640,440);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(tabbedPane);

        initMainPanel();
        initOverClockPanel();
    }

    private void initMainPanel(){
        initAlgorythm();
        lockForbiddenFields();
        initCurrencies();
        initButtonSave();
        initButtonRun();
        comboBoxPool.setEditable(true);

        this.mainPanel.setLayout(null);
        addComponentsToContainer(mainPanel, mainPanelComponents);
        this.tabbedPane.addTab("Main", mainPanel);
    }

    private void initOverClockPanel(){
        this.overclockPanel.setLayout(null);
        addComponentsToContainer(overclockPanel, overclockPanelComponents);
        this.tabbedPane.addTab("Overclock", overclockPanel);
    }

    private void initButtonSave(){
        buttonSave.setBounds(10, 600, BUTTON_WIDTH, ELEMENT_HEIGHT);
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
        ArrayList<String> pools = new ArrayList<String>();

        for (int i = 0; i < comboBoxPool.getItemCount(); i++){
            pools.add(comboBoxPool.getItemAt(i).toString());
        }

        Configuration config = Configuration.getNewConfig(currency)
                .algorithm(String.valueOf(comboBoxAlgo.getSelectedIndex() + 1))
                .walletAddr(textWallet.getText())
                .password(textPassword.getText())
                .intensity(textMiningIntencity.getText())
                .asmMode(checkBoxAsmMode.isSelected())
                .addPools(pools)
                .enabledCards(textEnabledCards.getText())
                .restartIn(textRestartIn.getText())
                .lowIntesity(checkBoxLowIntensity.isSelected())
                .noFee(checkBoxNoFee.isSelected())
                // Overclock
                .cClock(textCoreClock.getText())
                .mClock(textMemoryClock.getText())
                .cVoltage(textCoreVoltage.getText())
                .mVoltage(textMemoryVoltage.getText())
                .powLim(textPowerLimit.getText())
                .fanMin(textFanMin.getText())
                .fanMax(textFanMax.getText())
                .targetTemp(textTargetTemp.getText())
                .stopTemp(textStopTemp.getText());

        if (Environment.getCurrentAlgorythm() == Algorythm.CRYPTONIGHT){
            config.hashCount(textHashCnt.getText());
        }

        config.writeToFile();
        saveLastCurrency();
    }

    private final int ELEMENT_HEIGHT = 20;
    private final int BUTTON_WIDTH = 120;
    private final int LABEL_WIDTH = 120;
    private final int COMBO_WIDTH = 120;
    private final int TEXT_WIDTH = 480;
    private final int SHORT_TEXT_WIDTH = 100;

    private void addComponentsToContainer(Container container, JComponent[] components){
        int x = 10, y = 10;

        for (JComponent component : components){
            setComponentSize(component, x, y);
            container.add(component);
            if ( !(component instanceof  JButton)) {
                y = x == 10 ? y : y + ELEMENT_HEIGHT + 5;
            }

            x = x == 10 ? x + LABEL_WIDTH + 10 : 10;
        }
    }

    private void setComponentSize(JComponent component, int x, int y){
        if (component instanceof JLabel){
            component.setBounds(x, y, LABEL_WIDTH, ELEMENT_HEIGHT);
            ((JLabel)component).setHorizontalAlignment(JLabel.RIGHT);
        }

        if (component instanceof JComboBox){
            if (component == comboBoxPool){
                component.setBounds(x, y, TEXT_WIDTH * 2 / 3, ELEMENT_HEIGHT);
            } else {
                component.setBounds(x, y, COMBO_WIDTH, ELEMENT_HEIGHT);
            }
        }

        if (component instanceof JCheckBox){
            component.setBounds(x, y, ELEMENT_HEIGHT, ELEMENT_HEIGHT);
        }

        if (component instanceof JTextField){
            if (component == textWallet || component == textPassword){
                component.setBounds(x, y, TEXT_WIDTH, ELEMENT_HEIGHT);
            } else {
                component.setBounds(x, y, SHORT_TEXT_WIDTH, ELEMENT_HEIGHT);
            }
        }

        if (component instanceof JButton){
            component.setBounds(x, y, BUTTON_WIDTH, ELEMENT_HEIGHT*2);
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
            this.comboBoxPool.setModel(new DefaultComboBoxModel(loadPools(config)));
            this.comboBoxAlgo.setSelectedIndex(Integer.parseInt(config.getAlgo()) - 1 );
            if (config.getEnabledCards() != null) {
                this.textEnabledCards.setText(config.getEnabledCards());
            } else {
                this.textEnabledCards.setText("ALL CARDS");
            }
            this.textRestartIn.setText(config.getRestartIn());
            this.checkBoxLowIntensity.setSelected(config.isLowIntensity());
            this.checkBoxNoFee.setSelected(config.isNoFee());
            //Overclock
            this.textCoreClock.setText(config.cClock());
            this.textMemoryClock.setText(config.mClock());
            this.textCoreVoltage.setText(config.cVoltage());
            this.textMemoryVoltage.setText(config.mVoltage());
            this.textPowerLimit.setText(config.powLim());
            this.textTargetTemp.setText(config.targetTemp());
            this.textStopTemp.setText(config.stopTemp());
            this.textFanMin.setText(config.fanMin());
            this.textFanMax.setText(config.fanMax());
        } else {
            this.textWallet.setText("");
            this.textPassword.setText("x");
            this.textMiningIntencity.setText("");
            this.checkBoxAsmMode.setSelected(Environment.getCurrentAlgorythm() != Algorythm.CRYPTONIGHT);
            this.textHashCnt.setText("1024");
            this.comboBoxPool.setModel(new DefaultComboBoxModel());
            this.comboBoxAlgo.setSelectedIndex(0);
            this.textEnabledCards.setText("ALL CARDS");
            this.textRestartIn.setText("1");
            this.checkBoxLowIntensity.setSelected(false);
            this.checkBoxNoFee.setSelected(false);

            //Overclock
            this.textCoreClock.setText("");
            this.textMemoryClock.setText("");
            this.textCoreVoltage.setText("");
            this.textMemoryVoltage.setText("");
            this.textPowerLimit.setText("");
            this.textTargetTemp.setText("");
            this.textStopTemp.setText("");
            this.textFanMin.setText("");
            this.textFanMax.setText("");
        }
    }

    private String[] loadPools(Configuration config){
        ArrayList<String> pools = config.getPools();
        return pools.toArray(new String[pools.size()]);
    }

    private void saveLastCurrency(){
        FileProxy.saveLastCurrency(Environment.getCurrentAlgorythm(), comboBoxCurrency.getSelectedItem().toString());
    }
}