/**
 * @author - Francis Olawumi Awe - <awefrancolaz@gmail.com>
 */
package org.apache.cordova.networksignalstrength;

import java.util.ArrayList;

import android.content.Context;

import android.telephony.TelephonyManager;
// import android.telephony.TelephonyCallback;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class echoes a string called from JavaScript.
 */
public class NetworkSignalStrength extends CordovaPlugin {

    private static final int PERMISSION_REQUEST_CODE = 100;
    TelephonyManager telephonyManager;
    

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("netSignalStrength")) {
            // String message = args.getString(0);
            // this.coolMethod(message, callbackContext);
            // create object of class SignalStrengthStateListener 
            // networkSignalListener = new SignalStrengthStateListener();
                TelephonyManager tm = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                /**
                    Callback invoked when network signal strengths changes on the registered subscription. Note, 
                    the registration subscription ID comes from TelephonyManager object which registers 
                    TelephonyCallback by TelephonyManager#registerTelephonyCallback(Executor, TelephonyCallback).
                 */
                // tm.listen(networkSignalListener, TelephonyCallback.SignalStrengthsListener );
                // int counter = 0;
                // while ( dbm == -1) {
                //         try {
                //                 Thread.sleep(200);
                //         } catch (InterruptedException e) {
                //                 e.printStackTrace();
                //         }
                //         if (counter++ >= 5)
                //         {
                //                 break;
                //         }
                // }
                // Log.d("signal details", tm.getSignalStrength());
                // Log.d("signal dbm", dbm);
                callbackContext.success(tm.getSignalStrength().toString());
            return true;
        }
        return false;
    }

    // private void coolMethod(String message, CallbackContext callbackContext) {
    //     if (message != null && message.length() > 0) {
    //         callbackContext.success(message);
    //     } else {
    //         callbackContext.error("Expected one non-empty string argument.");
    //     }
    // }


// class SignalStrengthStateListener extends TelephonyCallback {

// @Override
// public void onSignalStrengthsChanged(SignalStrength signalStrength) {
//         super.onSignalStrengthsChanged(signalStrength);
//         int tsNormSignalStrength = signalStrength.getDbm();
//         dbm = tsNormSignalStrength;     // -> dBm
//         // Log.d("signal details tsNormSignalStrength", tsNormSignalStrength);
        
// }


// }

// create object of class SignalStrengthStateListener 
    // SignalStrengthStateListener networkSignalListener;
    // int dbm = -1;
}
