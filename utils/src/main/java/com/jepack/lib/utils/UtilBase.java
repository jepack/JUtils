package com.jepack.lib.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by yang on 2017/3/20.
 */

public class UtilBase {
    private static String channel = null;
    private static Context appContext;
    private static boolean mIsTVPackage = false; //fyy 2017-11-14
    private static boolean mIsInitTV = false;
    private static Integer versionCode = null;
    private static String versionName = null;
    private static String packageName = null;

    public static String getMeituanChannel(Context context) {
        if (!TextUtil.isEmpty(channel)) {
            return channel;
        } else {
            ApplicationInfo appInfo = context.getApplicationInfo();
            String sourceDir = appInfo.sourceDir;
            String ret = "";
            ZipFile zipfile = null;
            try {
                zipfile = new ZipFile(sourceDir);
                Enumeration<?> entries = zipfile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String entryName = entry.getName();
                    //MLog.d("--------entryName:" + entryName);
                    if (entryName.contains("wmchannel_")) {
                        //MLog.d("--------match entryName:" + entryName);
                        ret = entryName;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (zipfile != null) {
                    try {
                        zipfile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String[] split = ret.split("_");
            if (split.length >= 2) {
                channel = ret.substring(split[0].length() + 1, ret.length());
                return channel;
            } else {
                return "";
            }
        }
    }

    public static String getAppName() {
        return "WMVideoPlayer";
    }

    public static void setAppContext(Context _appContext) {
        appContext = _appContext;
        syncIsDebug(appContext);
    }

    public static Context getAppContext() {
        return appContext;
    }


    private static Boolean isDebug = null;

    public static boolean isDebug() {
        return isDebug != null && isDebug;
    }

    public static void setIsDebug(Boolean isDebug) {
        UtilBase.isDebug = isDebug;
    }

    /**
     * Sync lib debug with app's debug value. Should be called in module Application
     *
     */
    public static void syncIsDebug(Context context) {
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }

    @Nullable
    public static String getMetaData(Context context, @NonNull String metaKey) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);

            if (applicationInfo != null && applicationInfo.metaData != null) {
                Object object = applicationInfo.metaData.get(metaKey);
                if (object != null) {
                    return object.toString();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object getValue(Map map, String key, Object defaultValue) {
        if (map == null) return defaultValue;
        if (!map.containsKey(key)) return defaultValue;
        return map.get(key);
    }

    public static String getVersionName() {
        if (versionName != null && !versionName.equals("UNKNOWN")) return versionName;
        try {
            PackageInfo packageInfo = getAppContext().getPackageManager()
                    .getPackageInfo(appContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "UNKNOWN";
            return versionName;
        }
    }


    @Nullable
    public static Integer getVersionCode() {
        if (versionCode != null) return versionCode;
        try {
            PackageInfo packageInfo = getAppContext().getPackageManager()
                    .getPackageInfo(appContext.getPackageName(), 0);
            versionCode = (packageInfo == null) ? null : packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String macAddress = null;
    private static String imei = null;
    private static String vid = null;

    public static Map<String, Object> getDeviceInfo(Context context) {
        Map<String, Object> map = new TreeMap<>();
        macAddress = macAddress == null ? ((WifiManager) (UtilBase.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE))).getConnectionInfo().getMacAddress() : macAddress;
        map.put("mac", macAddress == null ? "" : macAddress);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                imei = imei == null ? ((TelephonyManager) UtilBase.getAppContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(TelephonyManager.PHONE_TYPE_NONE) : imei;
            }
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                imei = imei == null ? ((TelephonyManager) UtilBase.getAppContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId() : imei;
            }

        }
        map.put("imei", imei == null? "": imei);
        vid = vid == null? Settings.System.getString(UtilBase.getAppContext().getContentResolver(), Settings.System.ANDROID_ID): vid;
        map.put("VID", vid == null? "":vid);
        map.put("PID", Build.PRODUCT);
        map.put("SN", Build.SERIAL);
        map.put("ro_product_device", Build.DEVICE);
        map.put("ro_product_model", Build.MODEL);
        map.put("ro_hardware", Build.HARDWARE);
        map.put("ro_product_board",  Build.BOARD);
        map.put("ro_product_brand",  Build.BRAND);
        map.put("ro_product_manufacturer", Build.MANUFACTURER);
        map.put("android_version", Build.VERSION.RELEASE);
        map.put("firmware_version", Build.RADIO);
        map.put("ro_build_description", "");//StringUtil.getDefaultEmpty(devInfo, propertysreader.KEY_DESCRIPTION)
        map.put("cpu_hardware",  Build.HARDWARE);
        map.put("core_version", "");//StringUtil.getDefaultEmpty(devInfo, propertysreader.KEY_CORE_VERSION)
        map.put("ro_build_version_sdk", Build.VERSION.SDK );//StringUtil.getDefaultEmpty(devInfo, propertysreader.KEY_SDK_VERSION)
        map.put("region", "");//StringUtil.getDefaultEmpty(devInfo, propertysreader.KEY_REGION)
        map.put("ro_build_fingerprint",  Build.FINGERPRINT);
        map.put("ro_product_productid", "");//StringUtil.getDefaultEmpty(devInfo, propertysreader.KEY_PRODUCT_ID)
        map.put("custom_props", "");
        return map;
    }

}
