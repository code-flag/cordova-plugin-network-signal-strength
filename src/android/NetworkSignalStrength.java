/**
 * @author - Francis Olawumi Awe - <awefrancolaz@gmail.com>
 */
package org.apache.cordova.networksignalstrength;

import android.content.Context;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.telephony.SignalStrength;

import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;

import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellInfoGsm;

import android.net.NetworkInfo;
import java.util.*;
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
  int rssiStrength = -1;
  int signalLevel = 0;
  SignalStrengthStateListener networkSignalListener;
  CellSignalStrength cellsig;
    
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
   String details;
   int dbm = 0;
   int level = 0;
   int asu = 0;
   // api 30 above
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
          //  Build.VERSION.SDK_INT >= Build.VERSION_CODES.S || 
         //  Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2 || 
         //  Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU 
    ){
      details =  "SignalStrength_M_1:" + tmanager.getSignalStrength().toString();
    }
   // android version 5.0 to version 10 api 21 to api 29
    else if(
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP 
      // Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 || 
      // Build.VERSION.SDK_INT == Build.VERSION_CODES.M || 
      // Build.VERSION.SDK_INT == Build.VERSION_CODES.N || 
      // Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1 || 
      // Build.VERSION.SDK_INT == Build.VERSION_CODES.O || 
      // Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1 || 
      // Build.VERSION.SDK_INT == Build.VERSION_CODES.P ||
      // Build.VERSION.SDK_INT == Build.VERSION_CODES.Q 
      ){
        
        List<CellInfo> cellInfoList;
       
            cellInfoList = tmanager.getAllCellInfo();
            if (cellInfoList != null) {
                for (CellInfo cellInfo : cellInfoList) {
                    if (cellInfo instanceof CellInfoGsm) {
                        CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthGsm.getDbm();
                        level = cellSignalStrengthGsm.getLevel();
                        asu = cellSignalStrengthGsm.getAsuLevel();
                    } else if (cellInfo instanceof CellInfoCdma) {
                        CellSignalStrengthCdma cellSignalStrengthCdma =
                                ((CellInfoCdma) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthCdma.getDbm();
                        level = cellSignalStrengthCdma.getLevel();
                        asu = cellSignalStrengthCdma.getAsuLevel();
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthLte.getDbm();
                        level = cellSignalStrengthLte.getLevel();
                        asu = cellSignalStrengthLte.getAsuLevel();
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if (cellInfo instanceof CellInfoWcdma) {
                            CellSignalStrengthWcdma cellSignalStrengthWcdma =
                                    ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                            dbm = cellSignalStrengthWcdma.getDbm();
                            level = cellSignalStrengthWcdma.getLevel();
                            asu = cellSignalStrengthWcdma.getAsuLevel();
                        }
                    }
                }
            }
             
  
        String signalDetail = "SignalStrength_M_2:" + "rssi=" + String.valueOf(dbm) + " " + "level=" + String.valueOf(level);
      details = signalDetail;
    }  
    else {
      details = "0";
    }
    
    return details;
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

   
  class SignalStrengthStateListener extends PhoneStateListener {

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int mainSignalStrength = signalStrength.getGsmSignalStrength();
            rssiStrength = (2 * mainSignalStrength) - 113;  
            signalLevel = signalStrength.getLevel();
    }

  }

}
