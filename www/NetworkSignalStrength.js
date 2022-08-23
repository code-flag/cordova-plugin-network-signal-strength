var exec = require("cordova/exec");

var mainCallbackFunction = () => {};
/**
 * @var {object} signalTypeRange - defines signal strength properties with ranges
 */
const signalTypeRange = {
  RSSI: [-113, -51, "dBm"],
  RSSNR: [-20, +30, "dB"],
  RSRQ: [-34, 3, "dB"],
  RSRP: [-140, -43, "dBm"],
};

/**
 * @var {object} signalType - defines signal strength properties
 */
const signalType = ["RSSI", "RSSNR", "RSRQ", "RSRP", "LEVEL", "LTELEVEL"];

/**
 * This method extract signal strength needed information from the return value
 * such as rssi rsrq rsnnr level lte level - all in dBm unit
 * SignalStrength_M_xx signify the return type based on android api version
 * @param {string} signals - signal strength returned value
 * @returns object
 * 
 */
const getSignalList = (signals) => {
  if (
    signals != 0 &&
    signals != -1 &&
    signals != null &&
    signals != undefined
  ) {
    var netSignal = [];
    let newDataObj;
    signals = signals.split(":");
    if (signals[0] == "SignalStrength_M_1") {
      signals[3]
      .split(" ")
      .filter((data) => {
        if (data !== "" && data !== null && data !== undefined) {
          newDataObj = data.split("=");
          if (signalType.includes(newDataObj[0]?.toUpperCase())) {
            newDataObj = { [newDataObj[0]]: newDataObj[1] };
            netSignal.push(newDataObj);
          }
        }
      });
    return netSignal;
    }
    if(signals[0] == "SignalStrength_M_2") {
      signals[1]
      .split(" ")
      .filter((data) => {
        if (data !== "" && data !== null && data !== undefined) {
          newDataObj = data.split("=");
          if (signalType.includes(newDataObj[0]?.toUpperCase())) {
            newDataObj = { [newDataObj[0]]: newDataObj[1] };
            netSignal.push(newDataObj);
          }
        }
      });
    return netSignal;
    }
    
  }
  return signals;
};

/**
 * This function gets the return value, format it to a meaningful object data and
 * pass it to a callback function
 * @param {string} networkDetails - returned value
 * @param {function} callback - callback function
 */
const returnNetworkDetails = (networkDetails) => {
  if (
    networkDetails != 0 &&
    networkDetails != -1 &&
    networkDetails != null &&
    networkDetails != undefined
  ) {
    let incomingDataArr = networkDetails.split("^");
    let connectionState =
      incomingDataArr[0] == 1 ? "CONNECTED" : "DISCONNECTED";
    let networkType = incomingDataArr[1];
    let signalStrength = getSignalList(incomingDataArr[2]);
    const networkDetailsObj = {
      CONNECTION_STATE: connectionState,
      SIGNAL_STRENGTH: signalStrength,
      NETWORK_TYPE: networkType,
    };

    mainCallbackFunction(networkDetailsObj);
  } else {
    mainCallbackFunction(networkDetails);
  }
};

/**
 * This function gets extracted signal strength details and
 * invoke callback function
 * @param {string} data - incoming data or return data
 * @param {function} callback - callback function to invoke
 */
const returnSignalList = (data) => {
  // invoke call back
  mainCallbackFunction(getSignalList(data));
};

/**
 * This function gets unextracted signal strength details and
 * invoke callback function
 * @param {string} data - incoming data or return data
 * @param {function} callback - callback function to invoke
 */
 const returnRawSignalListAsString = (data) => {
  // invoke call back
  mainCallbackFunction(data);
};

function SignalStrength() {
  this.networkInfo = function (infoType, callback) {
    if (infoType === "CONNECTION_STATE") {
      return cordova.exec(
        callback,
        function (err) {
          callback(-1);
        },
        "NetworkSignalStrength",
        "connectionStatus",
        []
      );
    } else if (infoType === "NETWORK_TYPE") {
      return cordova.exec(
        callback,
        function (err) {
          callback(-1);
        },
        "NetworkSignalStrength",
        "networkType",
        []
      );
    } else if (infoType === "SIGNAL_STRENGTH") {
      mainCallbackFunction = callback;
      return cordova.exec(
        returnSignalList,
        function (err) {
          callback(-1);
        },
        "NetworkSignalStrength",
        "signalStrength",
        []
      );
    } else if (infoType === "SIGNAL_STRENGTH_RAW") {
      mainCallbackFunction = callback;
      return cordova.exec(
        returnRawSignalListAsString,
        function (err) {
          callback(-1);
        },
        "NetworkSignalStrength",
        "signalStrength",
        []
      );
    } else {
      mainCallbackFunction = callback;
      return cordova.exec(
        returnNetworkDetails,
        function (err) {
          callback(-1);
        },
        "NetworkSignalStrength",
        "networkDetails",
        []
      );
    }
  };
}

window.networkSignalStrength = new SignalStrength();
