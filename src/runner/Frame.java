package runner;

import config.Configuration;
import config.Environment;
import config.FileProxy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by kkostya on 23.11.17.
 */
public class Frame extends JFrame {

    private final int buttonWidth = 100;
    private final int buttonHeight = 50;
    private final Dimension buttonSize = new Dimension(buttonWidth, buttonHeight);

    private JButton buttonSave = new JButton("Save");
    private JButton buttonRun = new JButton("Run");
    private JLabel labelWallet = new JLabel("Wallet:");
    private JTextField textWallet = new JTextField("", 20);
    private JLabel labelHashCnt = new JLabel("Hash count:");
    private JTextField textHashCnt = new JTextField("1024", 4);
    private JLabel labelPool = new JLabel("Main pool:");
    private JTextField textPool = new JTextField("", 20);
    private JLabel labelAlgo = new JLabel("Algorythm:");
    private JComboBox comboBoxAlgo = new JComboBox();
    private JLabel labelCurrency = new JLabel("Currency:");
    private JComboBox comboBoxCurrency = new JComboBox();
    private JLabel labelEnabledCards = new JLabel("Enabled cards:");
    private JTextField textEnabledCards = new JTextField("");
    private JLabel labelAlgorythm = new JLabel("Algorythm:");
    private JComboBox comboBoxAlgorythm = new JComboBox();
    private JLabel labelRestartIn = new JLabel("Restart in:");
    private JTextField textRestartIn = new JTextField("1");

    public Frame(){
        super("Claymore Runner");
        this.setBounds(100,100,800,200);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridLayout mainLayout = new GridLayout(9,2,2,2);

        Container container = this.getContentPane();
        container.setLayout(mainLayout);

        initAlgorythm();
        initCurrencies();
        initAlgo();
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
                ClaymoreRunner.runMiner(comboBoxCurrency.getSelectedItem().toString());
            }
        });
    }

    private void saveConfig(){
        String currency = comboBoxCurrency.getSelectedItem().toString();
        Configuration config = new Configuration(currency)
                .algorithm(String.valueOf(comboBoxAlgo.getSelectedIndex() + 1))
                .walletAddr(textWallet.getText())
                .addPool(textPool.getText())
                .hashCount(textHashCnt.getText())
                .enabledCards(textEnabledCards.getText())
                .restartIn(textRestartIn.getText());
        config.writeToFile();
        saveLastCurrency();
    }

    private void addComponentsToContainer(Container container){
        setAlignment();

        container.add(labelAlgorythm);
        container.add(comboBoxAlgorythm);

        container.add(labelCurrency);
        container.add(comboBoxCurrency);

        container.add(labelAlgo);
        container.add(comboBoxAlgo);

        container.add(labelWallet);
        container.add(textWallet);

        container.add(labelHashCnt);
        container.add(textHashCnt);

        container.add(labelPool);
        container.add(textPool);

        container.add(labelEnabledCards);
        container.add(textEnabledCards);

        container.add(labelRestartIn);
        container.add(textRestartIn);

        container.add(buttonSave);
        container.add(buttonRun);
    }

    private void setAlignment(){
        labelWallet.setHorizontalAlignment(JLabel.RIGHT);
        labelHashCnt.setHorizontalAlignment(JLabel.RIGHT);
        labelCurrency.setHorizontalAlignment(JLabel.RIGHT);
        labelPool.setHorizontalAlignment(JLabel.RIGHT);
        labelAlgo.setHorizontalAlignment(JLabel.RIGHT);
        labelEnabledCards.setHorizontalAlignment(JLabel.RIGHT);
        labelAlgorythm.setHorizontalAlignment(JLabel.RIGHT);
        labelRestartIn.setHorizontalAlignment(JLabel.RIGHT);
    }

    private void initAlgorythm(){
        comboBoxAlgorythm.setModel(new DefaultComboBoxModel(Environment.getAlgorythmsList()));
        Environment.setCurrentAlgorythm(comboBoxAlgorythm.getSelectedItem().toString());
        comboBoxAlgorythm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxCurrency.setModel(new DefaultComboBoxModel(FileProxy.getCurrenciesList()));
                Environment.setCurrentAlgorythm(comboBoxAlgorythm.getSelectedItem().toString());
                initCurrencies();
                loadConfigData();
            }
        });
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
        if (comboBoxAlgo.getModel().getSize() < 4) {
            comboBoxAlgo.setModel(new DefaultComboBoxModel(new String[]{"a1", "a2", "a3", "a4"}));
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
            this.textHashCnt.setText(config.getHashCnt());
            this.textPool.setText(loadPools(config));
            this.comboBoxAlgo.setSelectedIndex(Integer.parseInt(config.getAlgo()) - 1 );
            this.textEnabledCards.setText(config.getEnabledCards());
            this.textRestartIn.setText(config.getRestartIn());
        } else {
            this.textWallet.setText("");
            this.textHashCnt.setText("1024");
            this.textPool.setText("");
            this.comboBoxAlgo.setSelectedIndex(0);
            this.textEnabledCards.setText("");
            this.textRestartIn.setText("1");
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
