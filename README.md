# cordova-plugin-network-signal-strength
Apache cordova plugin to detect network signal strength and other network updates such as network types and connection state
## Supported Platforms
Android
## Installation
from npm repo: $ cordova plugin add cordova-plugin-network-signal-strength

or from git repo: $ cordova plugin add https://github.com/code-flag/cordova-plugin-network-signal-strength.git

## Usage
The API has only one method `window["networkSignalStrength"].networkInfo(infoType, callback)`. 
* *infoType* parameter could either be:
   - *null* : to get all the network details including signal strength, network type and connection state
   - *CONNECTION_STATE* : to get only the connection state which will return 0 or 1. 
      - 0 : disconnected
      - 1 : connected
   - *SIGNAL_STRENGTH* : to get only the signal strength information. 
      - This return an object which include *** rssi, rssq, rssnr, rsrp, signal level and lte level ***
        ```javascript
          const signalTypeRange = {
           RSSI: [-113, -51, "dBm"],
           RSSNR: [-20, +30, "dB"],
           RSRQ: [-34, 3, "dB"],
           RSRP: [-140, -43, "dBm"],
           };
        ```
   - *NETWORK_TYPE* : to get type of connected network. Return integer -1 to 4 
      - -1 : unknown
      - 0 : disconnected
      - 1 : wifi
      - 2 : 2g
      - 3 : 3g
      - 4 : 4g
      - 5 : 5g
      - 10 : roaming
  
  *code snippet*
  ```javascript
     window['networkSignalStrength']?.networkInfo(null, (networkInfoData) => {
     console.log('network signal data :', networkInfoData)
     });
  ```
 * You receive -1 as a result if the device is unable to get a known network type and 0 for offline or disconnected state.
  
  ```javascript
     window['networkSignalStrength']?.networkInfo(SIGNAL_STRENGTH, (networkInfoData) => {
     console.log('network signal data :', networkInfoData)
     });
  ```
 * You should call the window.SignalStrength.dbm only after cordova platform is ready. Example in Ionic framework:
  ```javascript
    // ensure platform is ready before calling the plugin method
    $ionicPlatform.ready().then(() => {
      // Platform now ready, execute any required native code
      setInterval(() => {
        window['networkSignalStrength']?.networkInfo(null, (networkInfoData) => {console.log('network signal data :', networkInfoData)});
      }, 5000)
    });
  ```
  
## Notes
  * `window['networkSignalStrength']` is undefined when testing your app on your PC while using ionic serve.
  * when you call `window['networkSignalStrength'].networkInfo` for the first time the device may respond with -1 or 0. Try calling the function again after some delay.
## How to Contribute
  Use pull request.
  
  
  
  
  
  
