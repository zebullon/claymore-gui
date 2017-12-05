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

- <b>Mining:</b> Select mining algorythm
- <b>Currency:</b> Select mining currency
  - <b>!!! NOTE:</b> if your preferred currency doesn't exist in list, you may just add it to file "resources/ALGORYTHM/currencies.txt"
- <b>Algorythm:</b> Optimization algorythm. It corresponds to parameter -a in CryptoNight and Equihash Claymore miners. 
- <b>ASM:</b> Enable or disable ASM mode in Ethash and Equihash Claymore miners. 
- <b>Wallet:</b> Wallet address for mining
- <b>Password (e-mail):</b> Password for mining pool
- <b>Mining intencity:</b> Parameter corresponds -ethi parameter in Ethash miner or -i parameter in Equihash miner.
- <b>Hash count:</b> Parameter corresponds -h parameter in CryptoNight miner.
- <b>Pools:</b> list of pools for mining. Should be written by comma, like: "stratum+tcp://monero.crypto-pool.fr:3333, stratum+tcp://xmr.dwarfpool.com:80"
The first pool is set as main.
- <b>Enabled cards:</b> Parameter corresponds -di parameter in all Claymore miners. Set this field empty, if all cards should mine this currency
or set number sequence as "012", if you want to mine this currency by GPU#0, GPU#1 and GPU#2 only.
- <b>Restart in:</b> Parameter corresponds -r parameter in all Claymore miners. "0" or empty value disable restart miner, >21 value set time to force restart miner.
- <b>Low intensity mode:</b> Parameter corresponds -li parameter in all Claymore miners. Enable or disable low intensity mode.
- <b>No fee:</b> Parameter corresponds -nofee parameter in all Claymore miners. Enable or disable no fee mode.
- <b>Save Button</b> Save configuration for this currency with current parameters. 
Parameters will be automatically loaded to corresponding fields at next time choosing this currency.
- <b>Run Button</b> Save configuration for this currency with current parameters and run corresponding Claymore miner from "resources/ALGORYTHM/Claymore" directory. 
Parameters will be automatically loaded to corresponding fields at next time choosing this currency.
  - <b>!!! NOTE:</b> If you want to add some other parameters to configuration, do not edit config.txt and epools.txt in "resources/Algorythm/Claymore directory". 
  They will be overridden at the next run by stored config file. Edit file "resources/configs/CURRENCY_config.txt".
  
You may set link of "resources/ALGORYTHM/Claymore/start.bat" file to Windows Startup directory for autorun miner with last runned configuration after Windows reboot.
This feature will be added to GUI later.
  
You may read more about Claymore parameters in the file "resources/ALGORYTHM/Claymore/Readme.txt"
