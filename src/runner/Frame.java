package runner;

import config.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zebullon on 23.11.17.
 */
public class Frame extends JFrame {

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JPanel mainPanel = new JPanel();
    private final JPanel overclockPanel = new JPanel();
    private final JPanel donatePanel = new JPanel();

    // Main panel components

    private final JTextField textWallet = new JTextField();
    private final JTextField textPaymentId = new JTextField();
    private final JTextField textPassword = new JTextField("x");
    private final JTextField textMiningIntencity = new JTextField();
    private final JTextField textHashCnt = new JTextField("1024", 4);
    private final JComboBox comboBoxPool = new JComboBox();
    private final JTextField textWorker = new JTextField();
    private final JTextField textPoolFormat = new JTextField();
    private final JComboBox comboBoxAlgo = new JComboBox();
    private final JCheckBox checkBoxAsmMode = new JCheckBox();
    private final JComboBox comboBoxCurrency = new JComboBox();
    private final JTextField textEnabledCards = new JTextField("ALL CARDS");
    private final JComboBox comboBoxAlgorythm = new JComboBox();
    private final JTextField textRestartIn = new JTextField("1");
    private final JCheckBox checkBoxNoFee = new JCheckBox();
    private final JCheckBox checkBoxStart = new JCheckBox();
    private final JCheckBox checkBoxLowIntensity = new JCheckBox();
    private final JButton buttonSave = new JButton("Save");
    private final JButton buttonRun = new JButton("Run");
    private final JButton buttonSavePool = new JButton("Save pool");
    private final JButton buttonRemovePool = new JButton("Delete pool");

    private final JComponent[] mainPanelComponents = new JComponent[]{
            new JLabel("Mining:"), comboBoxAlgorythm,
            new JLabel("Currency:"), comboBoxCurrency,
            new JLabel("Algorythm:"), comboBoxAlgo,
            new JLabel("ASM mode:"), checkBoxAsmMode,
            new JLabel("Wallet:"), textWallet,
            new JLabel("Payment ID:"), textPaymentId,
            new JLabel("Password (e-mail):"), textPassword,
            new JLabel("Mining intensity:"), textMiningIntencity,
            new JLabel("Hash count:"), textHashCnt,
            new JLabel("Pools:"), comboBoxPool,
            new JLabel("Worker:"), textWorker,
            new JLabel("Wallet format:"), textPoolFormat,
            new JLabel("Enabled cards:"), textEnabledCards,
            new JLabel("Restart in:"), textRestartIn,
            new JLabel("Low intensity mode:"), checkBoxLowIntensity,
            new JLabel("No fee:"), checkBoxNoFee,
            buttonSave, buttonRun, buttonSavePool, buttonRemovePool
    };

    // Overclock panel components

    private final JTextField textCoreClock = new JTextField();
    private final JTextField textMemoryClock = new JTextField();
    private final JTextField textCoreVoltage = new JTextField();
    private final JTextField textMemoryVoltage = new JTextField();
    private final JTextField textPowerLimit = new JTextField();
    private final JTextField textFanMin = new JTextField();
    private final JTextField textFanMax = new JTextField();
    private final JTextField textTargetTemp = new JTextField();
    private final JTextField textStopTemp = new JTextField();

    private final JComponent[] overclockPanelComponents = new JComponent[]{
        new JLabel("Core clock, MHz:"), textCoreClock,
        new JLabel("Memory clock, MHz:"), textMemoryClock,
        new JLabel("Core voltage, mV:"), textCoreVoltage,
        new JLabel("Memory voltage, mV:"), textMemoryVoltage,
        new JLabel("Pover limit:"), textPowerLimit,
        new JLabel("Fan, min %:"), textFanMin,
        new JLabel("Fan, max %:"), textFanMax,
        new JLabel("Target temperature:"), textTargetTemp,
        new JLabel("Stop temperature:"), textStopTemp
    };

    //Donate panel components
    private final JTextField textDonateXmr = new JTextField("4JUdGzvrMFDWrUUwY3toJATSeNwjn54LkCnKBPRzDuhzi5vSepHfUckJNxRL2gjkNrSqtCoRUrEDAgRwsQvVCjZbRym6aAbhwn8DiomDGe");
    private final JTextField textDonateEth = new JTextField("0x07599a0be04a356d3d5ab7a1eda8104ceab357f4");

    private final JComponent[] donatePanelComponents = new JComponent[]{
            new JLabel("Donate XMR:"), textDonateXmr,
            new JLabel("Donate ETH:"), textDonateEth
    };

    public Frame(){
        super("Claymore Runner");
        this.setBounds(100,100,640,520);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(tabbedPane);

        initMainPanel();
        initOverClockPanel();
        initDonatePanel();
    }

    private void initMainPanel(){
        initAlgorythm();
        lockForbiddenFields();
        initCurrencies();
        initComboBoxPool();
        initButtonSave();
        initButtonRun();
        initButtonSavePool();
        initButtonRemovePool();

        this.mainPanel.setLayout(null);
        addComponentsToContainer(mainPanel, mainPanelComponents);
        this.tabbedPane.addTab("Main", mainPanel);
    }

    private void initOverClockPanel(){
        this.overclockPanel.setLayout(null);
        addComponentsToContainer(overclockPanel, overclockPanelComponents);
        this.tabbedPane.addTab("Overclock", overclockPanel);
    }

    private void initDonatePanel(){
        textDonateEth.setEditable(false);
        textDonateXmr.setEditable(false);
        this.donatePanel.setLayout(null);
        addComponentsToContainer(donatePanel, donatePanelComponents);
        this.tabbedPane.addTab("Donate", donatePanel);
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

    private void initButtonSavePool(){
        buttonSavePool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePool();
            }
        });
    }

    private void savePool(){
        String currency = comboBoxCurrency.getSelectedItem().toString();
        int index = comboBoxPool.getSelectedIndex();

        if (index < 0){
            Pool newPool = new Pool();
            setPoolParams(newPool);
            Environment.getPools(currency).add(newPool);
        } else {
            setPoolParams(Environment.getPools(currency).get(index));
        }
        comboBoxPool.setModel(new DefaultComboBoxModel(loadPoolAddrs(currency).toArray()));
        comboBoxPool.setSelectedIndex(index >= 0 ? index : comboBoxPool.getItemCount() - 2);
    }

    private void initButtonRemovePool(){
        buttonRemovePool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePool();
            }
        });
    }

    private void removePool(){
        String currency = comboBoxCurrency.getSelectedItem().toString();
        int index = comboBoxPool.getSelectedIndex();

        if (index >= 0){
            Environment.getPools(currency).remove(index);
            comboBoxPool.setModel(new DefaultComboBoxModel(loadPoolAddrs(currency).toArray()));
        }
    }

    private void setPoolParams(Pool pool){
        pool.poolAddress(comboBoxPool.getSelectedItem().toString())
                .wallet(textWallet.getText())
                .paymentId(textPaymentId.getText())
                .password(textPassword.getText())
                .email(textPassword.getText())
                .worker(textWorker.getText())
                .format(textPoolFormat.getText());
    }

    private void initComboBoxPool(){
        comboBoxPool.setEditable(true);

        comboBoxPool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBoxPool.getSelectedIndex();
                Pool currentPool = index >= 0 && index < Environment.getPools(comboBoxCurrency.getSelectedItem().toString()).size()
                        ? Environment.getPools(comboBoxCurrency.getSelectedItem().toString()).get(index)
                        : null;
                if (currentPool != null) {
                    textWallet.setText(currentPool.getWallet());
                    textPaymentId.setText(currentPool.getPaymentId());
                    textPassword.setText(currentPool.getEmail().isEmpty() ? currentPool.getPassword() : currentPool.getEmail());
                    textWorker.setText(currentPool.getWorker());
                    textPoolFormat.setText(currentPool.getFormat());
                }
            }
        });
    }

    private void saveConfig(){
        savePool();
        String currency = comboBoxCurrency.getSelectedItem().toString();
        List<Pool> pools = Environment.getPools(currency);

        for (int i = 0; i < comboBoxPool.getItemCount(); i++){
            String poolAddress = comboBoxPool.getItemAt(i).toString();
            if (i >= pools.size()) {
                if (!poolAddress.isEmpty()) {
                    pools.add(new Pool(poolAddress).wallet(textWallet.getText())
                                                   .paymentId(textPaymentId.getText())
                                                   .password(textPassword.getText())
                                                   .email(textPassword.getText())
                                                   .worker(textWorker.getText())
                                                   .format(textPoolFormat.getText()));
                }
            }
        }

        Configuration config = Configuration.getNewConfig(currency)
                .algorithm(String.valueOf(comboBoxAlgo.getSelectedIndex() + 1))
                .walletAddr(textWallet.getText())
                .password(textPassword.getText())
                .intensity(textMiningIntencity.getText())
                .asmMode(checkBoxAsmMode.isSelected())
                //.addPools(pools)
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

    private void addComponentsToContainer(Container container, JComponent[] components){
        int x = 10, y = 10;

        for (JComponent component : components){
            setComponentSize(component, x, y);
            container.add(component);
            if ( !(component instanceof  JButton)) {
                y = x == 10 ? y : y + FrameConstants.ELEMENT_HEIGHT + 5;
            }

            if ( !(component instanceof  JButton)) {
                x = x == 10 ? x + FrameConstants.LABEL_WIDTH + 10 : 10;
            } else {
                x += FrameConstants.LABEL_WIDTH + 10;
            }
        }
    }

    private void setComponentSize(JComponent component, int x, int y){
        if (component instanceof JLabel){
            component.setBounds(x, y, FrameConstants.LABEL_WIDTH, FrameConstants.ELEMENT_HEIGHT);
            ((JLabel)component).setHorizontalAlignment(JLabel.RIGHT);
        }

        if (component instanceof JComboBox){
            if (component == comboBoxPool){
                component.setBounds(x, y, FrameConstants.TEXT_WIDTH * 2 / 3, FrameConstants.ELEMENT_HEIGHT);
            } else {
                component.setBounds(x, y, FrameConstants.COMBO_WIDTH, FrameConstants.ELEMENT_HEIGHT);
            }
        }

        if (component instanceof JCheckBox){
            component.setBounds(x, y, FrameConstants.ELEMENT_HEIGHT, FrameConstants.ELEMENT_HEIGHT);
        }

        if (component instanceof JTextField){
            if (component == textMiningIntencity || component == textHashCnt ||
                component == textWorker || component == textRestartIn) {
                component.setBounds(x, y, FrameConstants.SHORT_TEXT_WIDTH, FrameConstants.ELEMENT_HEIGHT);
            } else {
                component.setBounds(x, y, FrameConstants.TEXT_WIDTH, FrameConstants.ELEMENT_HEIGHT);
            }
        }

        if (component instanceof JButton){
            component.setBounds(x, y, FrameConstants.BUTTON_WIDTH, FrameConstants.ELEMENT_HEIGHT*2);
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
        textPaymentId.setEnabled(isCryptoNight);
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

        List<Pool> pools = Environment.getPools(selectedCurrency);
        Pool mainPool = pools.size() == 0 ? new Pool() : pools.get(0);
        this.textWallet.setText(mainPool.getWallet().isEmpty() && config != null
                ? config.getWallet()
                : mainPool.getWallet());
        this.textPaymentId.setText(mainPool.getPaymentId());
        this.textPassword.setText(mainPool.getPassword());
        this.textPoolFormat.setText(mainPool.getFormat());
        this.textWorker.setText(mainPool.getWorker());

        if (config != null) {
            this.textMiningIntencity.setText(config.getIntensity());
            this.checkBoxAsmMode.setSelected(config.isAsmMode());
            this.textHashCnt.setText(config.getHashCnt());
            this.comboBoxPool.setModel(new DefaultComboBoxModel(loadPoolAddrs(selectedCurrency).toArray()));
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
            this.textMiningIntencity.setText("");
            this.checkBoxAsmMode.setSelected(Environment.getCurrentAlgorythm() != Algorythm.CRYPTONIGHT);
            this.textHashCnt.setText("1024");
            this.comboBoxPool.setModel(new DefaultComboBoxModel(loadPoolAddrs(selectedCurrency).toArray()));
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

    private List<String> loadPoolAddrs(String currency){
        List<String> poolAddrs = Environment.getPools(currency)
                                        .stream()
                                        .map(pool -> pool.getPoolAddress())
                                        .collect(Collectors.toList());
        poolAddrs.add("");
        return poolAddrs;
    }

    private void saveLastCurrency(){
        FileProxy.saveLastCurrency(Environment.getCurrentAlgorythm(), comboBoxCurrency.getSelectedItem().toString());
    }
}