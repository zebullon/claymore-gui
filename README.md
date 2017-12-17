# claymore-gui

Simple GUI for Claymore's miners.

<b>System requirements:</b>

- Operating system Windows 7/8/10
- Installed JDK (may be loaded from here: http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html)

<b>Features:</b>

- Easy editing config.txt and epools.txt files
- Support storing multiple configurations for different currencies
- Fast loading prepared configuration
- Turning between currencies in one click


<b>Graphic interface fields manual:</b>

<b>Main</b> tab:

- <b>Mining:</b> Select mining algorythm
- <b>Currency:</b> Select mining currency
  - <b>!!! NOTE:</b> if your preferred currency doesn't exist in list, you may just add it to file "resources/ALGORYTHM/currencies.txt"
- <b>Algorythm:</b> Optimization algorythm. It corresponds to parameter -a in CryptoNight and Equihash Claymore miners. 
- <b>ASM:</b> Enable or disable ASM mode in Ethash and Equihash Claymore miners. 
- <b>Wallet:</b> Wallet address for mining
- <b>Payment ID:</b> Payment ID, used for payments at CryptoNight-based currencies
- <b>Password (e-mail):</b> Password for mining pool
- <b>Mining intencity:</b> Parameter corresponds -ethi parameter in Ethash miner or -i parameter in Equihash miner.
- <b>Hash count:</b> Parameter corresponds -h parameter in CryptoNight miner.
- <b>Pools:</b> list of pool addresses  for mining. Should be written as: "stratum+tcp://monero.crypto-pool.fr:3333. The last element of the list is always empty, you may edit it to specify new pool. You may edit existed pool addresses. Save changes using <b>Save</b> or <b>Save pool</b> buttons. You may specify <b>Wallet</b>, <b>Payment ID</b>, <b>Worker</b>, <b>Password(e-mail)</b> and <b>Wallet format</b> parameters for each pool. These parameters will be loaded at the next select this pool. You may change each values of these parameters and save it using <b>Save</b> or <b>Save pool</b> buttons.
The first pool is set as main.
- <b>Worker:</b> rig's name (specified for each pool separately).
- <b>Wallet format:</b> specifying format of wallet address for each pool according the pool requirements. 
For example: WALLET.PAYMENT-ID.WORKER/EMAIL - miner will send to pool stricg of corresponding parameters in this format. If field is empty, default format is WALLET.
- <b>Enabled cards:</b> Parameter corresponds -di parameter in all Claymore miners. Set this field empty, if all cards should mine this currency
or set number sequence as "012", if you want to mine this currency by GPU#0, GPU#1 and GPU#2 only.
- <b>Restart in:</b> Parameter corresponds -r parameter in all Claymore miners. "0" or empty value disable restart miner, >21 value set time to force restart miner.
- <b>Low intensity mode:</b> Parameter corresponds -li parameter in all Claymore miners. Enable or disable low intensity mode.
- <b>No fee:</b> Parameter corresponds -nofee parameter in all Claymore miners. Enable or disable no fee mode.
- <b>Save</b> Save configuration for this currency with current parameters. 
Parameters will be automatically loaded to corresponding fields at next time choosing this currency.
- <b>Run</b> Save configuration for this currency with current parameters and run corresponding Claymore miner from "resources/ALGORYTHM/Claymore" directory. 
Parameters will be automatically loaded to corresponding fields at next time choosing this currency.
  - <b>!!! NOTE:</b> If you want to add some other parameters to configuration, do not edit config.txt and epools.txt in "resources/ALGORYTHM/Claymore directory". 
  They will be ovewritten at the next run by stored config file. Edit file "resources/configs/CURRENCY_config.txt".
- <b>Save pool</b> - add new pool or change existed pool to pools list with defined <b>Wallet</b>, <b>Payment ID</b>, <b>Worker</b>, <b>Password(e-mail)</b> and <b>Wallet format</b> parameters. This button doesn't save pool to the file epools.txt. Use <b>Save button</b> to do all these actions.
- <b>Remove pool</b> - removes selected pool from the pools list. This button doesn't remove pool from the file epools.txt. Use <b>Save button</b> to confirm remove from file.

-allpools and -alcoins options are enabled for all miners by default

<b>Overclock</b> tab:

- <b>Core clock, MHz:</b> set default core clock, one value for all cards or values by comma for every card, a.e.: 1100,1129,1303,1100.
- <b>Memory clock, MHz:</b> set default memory clock, one value for all cards or values by comma for every card, a.e.: 2000,2050,2050,2050.
- <b>Core voltage, mV:</b> set default core voltage in millivolts, one value for all cards or values by comma for every card.
- <b>Memory voltage, mV:</b> set default memory voltage in millivolts, one value for all cards or values by comma for every card.
- <b>Power limit</b> set power limit, from -50 to 50. If not specified, miner will not change power limit. You can also specify values for every card, for example  20,-20,0,10.
- <b>Fan, min rpm</b> set minimal fan speed, in percents, for example, 50 will set maximal fans speed to 50%. You can also specify values for every card, for example 50,60,70. Default value is 0.
- <b>Fan, max rpm</b> set minimal fan speed, in percents, for example, 50 will set maximal fans speed to 50%. You can also specify values for every card, for example 50,60,70. Default value is 0.
- <b>Target temperature</b> set target GPU temperature. For example, 80 means 80C temperature. You can also specify values for every card, for example 70,80,75. You can also set static fan speed if you specify negative values, for example -50 sets 50% fan speed. Specify zero to disable control and hide GPU statistics. Default 1 does not manage fans but shows GPU temperature and fan status every 30 seconds. Specify values 2..5 if it is too often.
- <b>Stop temperature</b> set stop GPU temperature, miner will stop mining if GPU reaches specified temperature. For example, 95 means 95C temperature. You can also specify values for every card, for example 95,85,90.	This feature is disabled by default 0. You also should specify non-zero value for <b>Target temperature</b> option to enable this option.



You may set link of "resources/ALGORYTHM/Claymore/start.bat" file to Windows Startup directory for autorun miner with last runned configuration after Windows reboot.
This feature will be added to GUI later.
  
You may read more about Claymore parameters in the file "resources/ALGORYTHM/Claymore/Readme.txt"
