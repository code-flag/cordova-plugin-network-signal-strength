/**
 * @author - Francis Olawumi Awe - <awefrancolaz@gmail.com>
 */
package org.apache.cordova.networksignalstrength;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
// import android.telephony.TelephonyCallback;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class NetworkSignalStrength extends CordovaPlugin {

  private static final int PERMISSION_REQUEST_CODE = 100;
  TelephonyManager telephonyManager;
//   TelephonyManager tmanager = (TelephonyManager) _context.getSystemService(
//       Context.TELEPHONY_SERVICE
//     );

    
  @Override
  public boolean execute(
    String action,
    JSONArray args,
    CallbackContext callbackContext
  )
    throws JSONException {

    if (action.equals("signalStrength")) {
      callbackContext.success(getSignalStrength());
      return true;
    }
    else if (action.equals("networkType")) {
        if(connectionStatus()) {
            callbackContext.success(String.valueOf(getCurrentNetworkType()));
        }else {
            callbackContext.success(String.valueOf(0));
        }
      return true;
    }
    else if (action.equals("connectionStatus")) {
      callbackContext.success(String.valueOf(connectionStatus()));
      return true;
    }
    else if (action.equals("networkDetails")) {
        if(connectionStatus()) {
            String networkType = String.valueOf(getCurrentNetworkType());
            String networkDetail = "1^" + networkType + "^" + getSignalStrength();
            callbackContext.success(networkDetail);
        }else {
            callbackContext.success(String.valueOf(0));
        }
      return true;
    }
    return false;
  }

/**
 * initial test method for signal strength
 *
 */

 public String getSignalStrength() {
   TelephonyManager tmanager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    return tmanager.getSignalStrength().toString();
 }
 
  /**
   * Check if the device is connected to the internet (mobile network or
   * WIFI).
   */
  public boolean connectionStatus() {
     TelephonyManager tmanager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
  ConnectivityManager cmanager = (ConnectivityManager) cordova.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

    boolean online = false;
    if (tmanager != null) {
      if (tmanager.getDataState() == TelephonyManager.DATA_CONNECTED) {
        // Mobile network
        online = true;
      } else {
        // WIFI
        
        if (cmanager != null) {
          NetworkInfo info = cmanager.getActiveNetworkInfo();
          if (info != null) online = info.isConnected();
        }
      }
    }

    return online;
  }

  /**
   * Evaluate the current network connection and return the
   * corresponding type, e.g. CONNECTION_WIFI.
   */
  public byte getCurrentNetworkType() {
     TelephonyManager tmanager = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
  ConnectivityManager cmanager = (ConnectivityManager) cordova.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

    // connection types
    byte CONNECTION_UNKNOWN = -1; 
    byte CONNECTION_OFFLINE = 0; 
    byte CONNECTION_ROAMING = 5;
    byte CONNECTION_WIFI = 1; 
    byte CONNECTION_2G = 2; 
    byte CONNECTION_3G = 3;
    byte CONNECTION_4G = 4; 

    NetworkInfo netInfo = cmanager.getActiveNetworkInfo();

    if (netInfo == null) return CONNECTION_OFFLINE;

    if (
      netInfo.getType() == ConnectivityManager.TYPE_WIFI
    ) return CONNECTION_WIFI;

    if (netInfo.isRoaming()) return CONNECTION_ROAMING;

    if (
      netInfo.getType() == ConnectivityManager.TYPE_MOBILE
    ) { // NETWORK_TYPE_HSPAP
      switch (netInfo.getSubtype()) {
        case TelephonyManager.NETWORK_TYPE_GPRS:
        case TelephonyManager.NETWORK_TYPE_EDGE:
        case TelephonyManager.NETWORK_TYPE_CDMA:
        case TelephonyManager.NETWORK_TYPE_1xRTT:
        case TelephonyManager.NETWORK_TYPE_IDEN:
          return CONNECTION_2G;
        case TelephonyManager.NETWORK_TYPE_UMTS:
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
        case TelephonyManager.NETWORK_TYPE_HSDPA:
        case TelephonyManager.NETWORK_TYPE_HSUPA:
        case TelephonyManager.NETWORK_TYPE_HSPA:
        case TelephonyManager.NETWORK_TYPE_EVDO_B:
        case TelephonyManager.NETWORK_TYPE_EHRPD:
        case TelephonyManager.NETWORK_TYPE_HSPAP:
          return CONNECTION_3G;
        case TelephonyManager.NETWORK_TYPE_LTE:
          return CONNECTION_4G;
        default:
          return CONNECTION_UNKNOWN;
      }
    }

    return CONNECTION_OFFLINE;
  }

    /**
     * initial test method for signal strength
     *
     */
    //  public static netSignalstrength(Context Context){
    //     // String message = args.getString(0);
    //   // this.coolMethod(message, callbackContext);
    //   // create object of class SignalStrengthStateListener
    //   // networkSignalListener = new SignalStrengthStateListener();
    //   TelephonyManager tm = (TelephonyManager) cordova
    //     .getActivity()
    //     .getSystemService(Context.TELEPHONY_SERVICE);
    //   /**
    //                 Callback invoked when network signal strengths changes on the registered subscription. Note, 
    //                 the registration subscription ID comes from TelephonyManager object which registers 
    //                 TelephonyCallback by TelephonyManager#registerTelephonyCallback(Executor, TelephonyCallback).
    //              */
    //   // tm.listen(networkSignalListener, TelephonyCallback.SignalStrengthsListener );
    //   // int counter = 0;
    //   // while ( dbm == -1) {
    //   //         try {
    //   //                 Thread.sleep(200);
    //   //         } catch (InterruptedException e) {
    //   //                 e.printStackTrace();
    //   //         }
    //   //         if (counter++ >= 5)
    //   //         {
    //   //                 break;
    //   //         }
    //   // }
    //   // Log.d("signal details", tm.getSignalStrength());
    //   // Log.d("signal dbm", dbm);
    //  }
    
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
