var exec = require('cordova/exec');
/*
networkSignalStrength = function (arg0, success, error) {
    exec(success, error, 'NetworkSignalStrength', 'netSignalStrength', [arg0]);
};
*/

function SignalStrength() {
    this.networkSignalStrength = function (callback) {
        return cordova.exec(callback, function(err) {
            callback(-1);
          }, 'NetworkSignalStrength', 'netSignalStrength', []);
    };
  }
  
  window.netSignalStrength = new SignalStrength()