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
    private JTextField textWallet = new JTextField("", 20);
    private JTextField textHashCnt = new JTextField("1024", 4);
    private JTextField textPool = new JTextField("", 20);
    private JComboBox comboBoxAlgo = new JComboBox();
    private JComboBox comboBoxCurrency = new JComboBox();
    private JTextField textEnabledCards = new JTextField("");
    private JComboBox comboBoxAlgorythm = new JComboBox();
    private JTextField textRestartIn = new JTextField("1");
    private JCheckBox checkBoxNoFee = new JCheckBox();
    private JCheckBox checkBoxStart = new JCheckBox();
    private JButton buttonSave = new JButton("Save");
    private JButton buttonRun = new JButton("Run");

    private final JComponent[] components = new JComponent[]{
            new JLabel("Algorythm:"), comboBoxAlgorythm,
            new JLabel("Currency:"), comboBoxCurrency,
            new JLabel("Algorythm:"), comboBoxAlgo,
            new JLabel("Wallet:"), textWallet,
            new JLabel("Hash count:"), textHashCnt,
            new JLabel("Pools:"), textPool,
            new JLabel("Enabled cards:"), textEnabledCards,
            new JLabel("Restart in:"), textRestartIn,
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
                //FileProxy.setStartToAutoLoad(checkBoxStart.isSelected());
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
                .restartIn(textRestartIn.getText())
                .noFee(checkBoxNoFee.isSelected());

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
            this.checkBoxNoFee.setSelected(config.isNoFee());
        } else {
            this.textWallet.setText("");
            this.textHashCnt.setText("1024");
            this.textPool.setText("");
            this.comboBoxAlgo.setSelectedIndex(0);
            this.textEnabledCards.setText("");
            this.textRestartIn.setText("1");
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