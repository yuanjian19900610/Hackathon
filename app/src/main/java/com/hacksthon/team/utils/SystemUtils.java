package com.hacksthon.team.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author SCOTT
 * @version 1.0.3
 * @description:
 * @createTime 17/5/13
 */

public class SystemUtils {


//    public static String getIpAddress(Context mContext) {
//        //获取wifi服务
//        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
//        //判断wifi是否开启
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ipAddress = wifiInfo.getIpAddress();
//        String ip = intToIp(ipAddress);
//        return ip;
//    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 获取设备mac地址
     *
     * @return
     */
    public static String getDeviceIDByMac(Context context) {
        String wifiMac = getMacAddr();
        if (TextUtils.isEmpty(wifiMac)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toLowerCase().substring(0, 17);
            } catch (IOException e) {
                e.printStackTrace();
                wifiMac=getMacAddressByWlan0(context);
            }

            if(TextUtils.isEmpty(wifiMac)){
                wifiMac=getMacAddressByWlan0(context);
            }

            if(TextUtils.isEmpty(wifiMac)){
                wifiMac=getMacAddressByWifiManager(context);
            }
        }
        return wifiMac;

        /**
         String wifiMac = null;
         //获取有线的mac地址
         if (TextUtils.isEmpty(wifiMac)) {
         try {
         return loadFileAsString("/sys/class/net/eth0/address")
         .toLowerCase().substring(0, 17);
         } catch (IOException e) {
         e.printStackTrace();
         return "";
         }
         }

         //获取无线的mac地址
         if (TextUtils.isEmpty(wifiMac)) {
         try {
         Process pp = Runtime.getRuntime().exec(
         "cat /sys/class/net/wlan0/address ");
         InputStreamReader ir = new InputStreamReader(pp.getInputStream());
         LineNumberReader input = new LineNumberReader(ir);
         String line = null;
         while ((line = input.readLine()) != null) {
         wifiMac = line.trim();
         break;
         }
         } catch (Exception ex) {
         ex.printStackTrace();
         return "";
         }
         }


         if (TextUtils.isEmpty(wifiMac)) {
         try {
         WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
         WifiInfo info = wifi.getConnectionInfo();
         wifiMac = info.getMacAddress();
         } catch (Exception e) {
         e.printStackTrace();
         }
         }


         //        if(TextUtils.isEmpty(wifiMac)){
         //            wifiMac="74:51:ba:da:6e:50";
         //        }
         return wifiMac;
         */
    }

    private static String getMacAddressByWlan0(Context context) {
        String wifiMac = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = null;
            while ((line = input.readLine()) != null) {
                wifiMac = line.trim();
                break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            wifiMac=getMacAddressByWifiManager(context);

        }
        return wifiMac;
    }

    private static String getMacAddressByWifiManager(Context context) {
        String wifiMac = "";
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            wifiMac = info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiMac;
    }

    /**
     * 扫描各个网络接口获取mac地址
     *
     * @return
     */
    public static String getMacAddr() {
        String macAddress = "";
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                macAddress = res1.toString();
                return macAddress;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return macAddress;
    }



    private static String loadFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }


    public static String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                // 有限网络
                return getLocalIp();
            }
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";

    }

}
