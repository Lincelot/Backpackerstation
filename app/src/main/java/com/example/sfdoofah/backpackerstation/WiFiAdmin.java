package com.example.sfdoofah.backpackerstation;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by Minsheng on 2015/4/21.
 */
public class WiFiAdmin {
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private List<ScanResult> wifiList;
    private List<WifiConfiguration> wifiConfiguration;
    WifiManager.WifiLock wifiLock;

    public WiFiAdmin(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    //打開wifi
    public void openWiFi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    //關閉wifi
    public void closeWiFi() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    //檢查wifi狀態
    public int checkState() {
        return wifiManager.getWifiState();
    }

    //鎖定wifilock(防止wifi進入睡眠)
    public void wiFiLock() {
        wifiLock.acquire();
    }

    //解鎖wifilock(wifi沒使用的時候會進入睡眠)
    public void wiFiUnLock() {
        if (wifiLock.isHeld()) {
            wifiLock.acquire();
        }
    }

    //建立一個wifilock
    public void createWiFiLock() {
        wifiLock = wifiManager.createWifiLock("Test");
    }

    //獲取設定值
    public List<WifiConfiguration> getWifiConfiguration() {
        return wifiConfiguration;
    }

    //指定設定檔並進行網路連線
    public void connectConfiguration(int index) {
        //找不到參數
        if (index > wifiConfiguration.size()) {
            return;
        }
        //連線指定參數的網路
        wifiManager.enableNetwork(wifiConfiguration.get(index).networkId, true);
    }

    //開始掃描
    public void startScan() {
        wifiManager.startScan();
        //得到掃描結果
        wifiList = wifiManager.getScanResults();
        //獲取已儲存的網路連線資訊
        wifiConfiguration = wifiManager.getConfiguredNetworks();
    }

    //獲取目前附近的網路列表
    public List<ScanResult> getWiFiList() {
        return wifiList;
    }

    //查看掃描結果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < wifiList.size(); i++) {
            stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
            stringBuilder.append(wifiList.get(i).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    //取得MAC Address
    public String getMacAddress() {
        return (wifiInfo == null) ? "NULL" : wifiInfo.getMacAddress();
    }

    //取得AP的MAC Address
    public String getBSSID() {
        return (wifiInfo == null) ? "NULL" : wifiInfo.getBSSID();
    }

    //取得當前SSID
    public String getSSID() {
        return wifiInfo.getSSID();
    }

    //取得當前AP的RSSI
    public int getRssi() {
        return wifiInfo.getRssi();
    }

    //取得當前IP位置
    public int getIpAddress() {
        return (wifiInfo == null) ? 0 : wifiInfo.getIpAddress();
    }

    //得到連接的
    public int getNetWrokId() {
        return (wifiInfo == null) ? 0 : wifiInfo.getNetworkId();
    }

    //得到wifiInfo的所有訊息
    public String getWiFiInfo() {
        return (wifiInfo == null) ? "NULL" : wifiInfo.toString();
    }

    //增加一個網路連線並連接
    public void addNetWork(WifiConfiguration wifiConfiguration) {
        int wifiConfigurationID = wifiManager.addNetwork(wifiConfiguration);
        boolean b = wifiManager.enableNetwork(wifiConfigurationID, true);
    }

    //斷開指定ID的網路連線
    public void disConnectWiFi(int netId) {
        wifiManager.disableNetwork(netId);
        wifiManager.disconnect();
    }

    //新建一個wifi連接資料
    public WifiConfiguration createWiFiInfo(String ssid, String password, int type) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + ssid + "\"";
        //清除相同的SSID，避免無限新增
        WifiConfiguration wifiConfiguration_temp = this.isExits(ssid);
        if (wifiConfiguration_temp != null) {
            wifiManager.removeNetwork(wifiConfiguration_temp.networkId);
        }
        switch (type) {
            //無密碼
            case 1: {
                wifiConfiguration.wepKeys[0] = "";
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiConfiguration.wepTxKeyIndex = 0;
                break;
            }
            //wep
            case 2: {
                wifiConfiguration.hiddenSSID = true;
                wifiConfiguration.wepKeys[0] = "\"" + password + "\"";
                wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiConfiguration.wepTxKeyIndex = 0;
                break;
            }
            //wap
            case 3: {
                wifiConfiguration.preSharedKey = "\"" + password + "\"";
                wifiConfiguration.hiddenSSID = true;
                wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                break;
            }
        }
        return wifiConfiguration;
    }

    //搜尋是否有相同名稱的SSID，若有則回傳
    private WifiConfiguration isExits(String ssid) {
        List<WifiConfiguration> existingwifiConfigurations = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingwifiConfiguration : existingwifiConfigurations) {
            if (existingwifiConfiguration.SSID.equals("\"" + ssid + "\"")) {
                return existingwifiConfiguration;
            }
        }
        return null;
    }

    //移除指定SSID的連線密碼
    public void removeWiFiInfo(String SSID) {
        List<WifiConfiguration> list=wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID.contains(SSID)){
                wifiManager.removeNetwork(i.networkId);
                wifiManager.saveConfiguration();
            }
        }
    }
}
