package com.example.lemalabsample;

/*
 * Copyright (C) 2017 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.WorkerThread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// @formatter:off
/**
 * <p>Get the consumer friendly name of an Android device.</p>
 *
 * <p>On many popular devices the market name of the device is not available. For example, on the
 * Samsung Galaxy S6 the value of {@link Build#MODEL} could be "SM-G920F", "SM-G920I", "SM-G920W8",
 * etc.</p>
 *
 * <p>See the usages below to get the consumer friends name of a device:</p>
 *
 * <p><b>Get the name of the current device:</b></p>
 *
 * <pre>
 * String deviceName = DeviceName.getDeviceName();
 * </pre>
 *
 * <p>The above code will get the correct device name for the top 600 Android devices. If the
 * device is unrecognized, then Build.MODEL is returned.</p>
 *
 * <p><b>Get the name of a device using the device's codename:</b></p>
 *
 * <pre>
 * // Retruns "Moto X Style"
 * DeviceName.getDeviceName("clark", "Unknown device");
 * </pre>
 *
 * <p><b>Get information about the device:</b></p>
 *
 * <pre>
 * DeviceName.with(context).request(new DeviceName.Callback() {
 *
 *   &#64;Override public void onFinished(DeviceName.DeviceInfo info, Exception error) {
 *     String manufacturer = info.manufacturer;  // "Samsung"
 *     String name = info.marketName;            // "Galaxy S6 Edge"
 *     String model = info.model;                // "SM-G925I"
 *     String codename = info.codename;          // "zerolte"
 *     String deviceName = info.getName();       // "Galaxy S6 Edge"
 *     // FYI: We are on the UI thread.
 *   }
 * });
 * </pre>
 *
 * <p>The above code loads JSON from a generated list of device names based on Google's maintained
 * list. It will be up-to-date with Google's supported device list so that you will get the correct
 * name for new or unknown devices. This supports over 10,000 devices.</p>
 *
 * <p>This will only make a network call once. The value is saved to SharedPreferences for future
 * calls.</p>
 */
public class Devicename {
    // @formatter:on

    // JSON which is derived from Google's PDF document which contains all devices on Google Play.
    // To get the URL to the JSON file which contains information about the device name:
    // String url = String.format(DEVICE_JSON_URL, Build.DEVICE);
    private static final String DEVICE_JSON_URL =
            "https://raw.githubusercontent.com/jaredrummler/AndroidDeviceNames/master/json/devices/%s.json";

    // Preference filename for storing device info so we don't need to download it again.
    private static final String SHARED_PREF_NAME = "device_names";

    /**
     * Create a new request to get information about a device.
     *
     * @param context
     *     the application context
     * @return a new Request instance.
     */
    public static Request with(Context context) {
        return new Request(context.getApplicationContext());
    }

    /**
     * Get the consumer friendly name of the device.
     *
     * @return the market name of the current device.
     * @see #getDeviceName(String, String)
     */
    public static String getDeviceName() {
        return getDeviceName(Build.DEVICE, Build.MODEL, capitalize(Build.MODEL));
    }

    /**
     * Get the consumer friendly name of a device.
     *
     * @param codename
     *     the value of the system property "ro.product.device" ({@link Build#DEVICE})
     *     <i>or</i>
     *     the value of the system property "ro.product.model" ({@link Build#MODEL})
     * @param fallback
     *     the fallback name if the device is unknown. Usually the value of the system property
     *     "ro.product.model" ({@link Build#MODEL})
     * @return the market name of a device or {@code fallback} if the device is unknown.
     */
    public static String getDeviceName(String codename, String fallback) {
        return getDeviceName(codename, codename, fallback);
    }

    /**
     * Get the consumer friendly name of a device.
     *
     * @param codename
     *     the value of the system property "ro.product.device" ({@link Build#DEVICE}).
     * @param model
     *     the value of the system property "ro.product.model" ({@link Build#MODEL}).
     * @param fallback
     *     the fallback name if the device is unknown. Usually the value of the system property
     *     "ro.product.model" ({@link Build#MODEL})
     * @return the market name of a device or {@code fallback} if the device is unknown.
     */
    public static String getDeviceName(String codename, String model, String fallback) {
        // ----------------------------------------------------------------------------
        // Acer
        if ((codename != null && codename.equals("acer_S57"))
                || (model != null && model.equals("S57"))) {
            return "Liquid Jade Z";
        }
        if ((codename != null && codename.equals("acer_t08"))
                || (model != null && model.equals("T08"))) {
            return "Liquid Zest Plus";
        }
        // ----------------------------------------------------------------------------
        // Asus
        if ((codename != null && (codename.equals("grouper")
                || codename.equals("tilapia")))) {
            return "Nexus 7 (2012)";
        }
        if ((codename != null && (codename.equals("deb")
                || codename.equals("flo")))) {
            return "Nexus 7 (2013)";
        }
        // ----------------------------------------------------------------------------
        // Google
        if ((codename != null && codename.equals("sailfish"))) {
            return "Pixel";
        }
        if ((codename != null && (codename.equals("walleye")))) {
            return "Pixel 2";
        }
        if ((codename != null && codename.equals("taimen"))) {
            return "Pixel 2 XL";
        }
        if ((codename != null && codename.equals("blueline"))) {
            return "Pixel 3";
        }
        if ((codename != null && codename.equals("crosshatch"))) {
            return "Pixel 3 XL";
        }
        if ((codename != null && codename.equals("dragon"))) {
            return "Pixel C";
        }
        if ((codename != null && codename.equals("marlin"))) {
            return "Pixel XL";
        }
        // ----------------------------------------------------------------------------
        // HTC
        if ((codename != null && codename.equals("flounder"))) {
            return "Nexus 9";
        }
        // ----------------------------------------------------------------------------
        // Huawei
        if ((codename != null && (codename.equals("HWBND-H")))
                || (model != null && (model.equals("BND-L21")
                || model.equals("BND-L24")
                || model.equals("BND-L31")))) {
            return "Honor 7X";
        }
        if ((codename != null && (codename.equals("HWBKL")))
                || (model != null && (model.equals("BKL-L04")
                || model.equals("BKL-L09")))) {
            return "Honor View 10";
        }
        if ((codename != null && (codename.equals("HWALP")))
                || (model != null && (model.equals("ALP-AL00")
                || model.equals("ALP-L09")
                || model.equals("ALP-L29")
                || model.equals("ALP-TL00")))) {
            return "Mate 10";
        }
        if ((codename != null && (codename.equals("HWMHA")))
                || (model != null && (model.equals("MHA-AL00")
                || model.equals("MHA-L09")
                || model.equals("MHA-L29")
                || model.equals("MHA-TL00")))) {
            return "Mate 9";
        }
        if ((codename != null && codename.equals("angler"))) {
            return "Nexus 6P";
        }
        // ----------------------------------------------------------------------------
        // LGE
        if ((codename != null && (codename.equals("g2")))
                || (model != null && (model.equals("LG-D800")
                || model.equals("LG-D801")
                || model.equals("LG-D802")
                || model.equals("LG-D802T")
                || model.equals("LG-D802TR")
                || model.equals("LG-D803")
                || model.equals("LG-D805")
                || model.equals("LG-D806")
                || model.equals("LG-F320K")
                || model.equals("LG-F320L")
                || model.equals("LG-F320S")
                || model.equals("LG-LS980")
                || model.equals("VS980 4G")))) {
            return "LG G2";
        }
        if ((codename != null && (codename.equals("g3")))
                || (model != null && (model.equals("AS985")
                || model.equals("LG-AS990")
                || model.equals("LG-D850")
                || model.equals("LG-D851")
                || model.equals("LG-D852")
                || model.equals("LG-D852G")
                || model.equals("LG-D855")
                || model.equals("LG-D856")
                || model.equals("LG-D857")
                || model.equals("LG-D858")
                || model.equals("LG-D858HK")
                || model.equals("LG-D859")
                || model.equals("LG-F400K")
                || model.equals("LG-F400L")
                || model.equals("LG-F400S")
                || model.equals("LGL24")
                || model.equals("LGLS990")
                || model.equals("LGUS990")
                || model.equals("LGV31")
                || model.equals("VS985 4G")))) {
            return "LG G3";
        }
        if ((codename != null && (codename.equals("p1")))
                || (model != null && (model.equals("AS986")
                || model.equals("LG-AS811")
                || model.equals("LG-AS991")
                || model.equals("LG-F500K")
                || model.equals("LG-F500L")
                || model.equals("LG-F500S")
                || model.equals("LG-H810")
                || model.equals("LG-H811")
                || model.equals("LG-H812")
                || model.equals("LG-H815")
                || model.equals("LG-H818")
                || model.equals("LG-H819")
                || model.equals("LGLS991")
                || model.equals("LGUS991")
                || model.equals("LGV32")
                || model.equals("VS986")))) {
            return "LG G4";
        }
        if ((codename != null && (codename.equals("h1")))
                || (model != null && (model.equals("LG-F700K")
                || model.equals("LG-F700L")
                || model.equals("LG-F700S")
                || model.equals("LG-H820")
                || model.equals("LG-H820PR")
                || model.equals("LG-H830")
                || model.equals("LG-H831")
                || model.equals("LG-H850")
                || model.equals("LG-H858")
                || model.equals("LG-H860")
                || model.equals("LG-H868")
                || model.equals("LGAS992")
                || model.equals("LGLS992")
                || model.equals("LGUS992")
                || model.equals("RS988")
                || model.equals("VS987")))) {
            return "LG G5";
        }
        if ((codename != null && (codename.equals("lucye")))
                || (model != null && (model.equals("LG-AS993")
                || model.equals("LG-H870")
                || model.equals("LG-H870AR")
                || model.equals("LG-H870DS")
                || model.equals("LG-H870I")
                || model.equals("LG-H870S")
                || model.equals("LG-H871")
                || model.equals("LG-H871S")
                || model.equals("LG-H872")
                || model.equals("LG-H872PR")
                || model.equals("LG-H873")
                || model.equals("LG-LS993")
                || model.equals("LGM-G600K")
                || model.equals("LGM-G600L")
                || model.equals("LGM-G600S")
                || model.equals("LGUS997")
                || model.equals("VS988")))) {
            return "LG G6";
        }
        if ((codename != null && codename.equals("mako"))) {
            return "Nexus 4";
        }
        if ((codename != null && codename.equals("hammerhead"))) {
            return "Nexus 5";
        }
        if ((codename != null && codename.equals("bullhead"))) {
            return "Nexus 5X";
        }
        // ----------------------------------------------------------------------------
        // Motorola
        if ((codename != null && codename.equals("shamu"))) {
            return "Nexus 6";
        }
        // ----------------------------------------------------------------------------
        // OnePlus
        if ((codename != null && codename.equals("OnePlus5"))
                || (model != null && model.equals("ONEPLUS A5000"))) {
            return "OnePlus 5";
        }
        if ((codename != null && codename.equals("OnePlus5T"))
                || (model != null && model.equals("ONEPLUS A5010"))) {
            return "OnePlus 5T";
        }
        if ((codename != null && codename.equals("OnePlus6"))
                || (model != null && model.equals("ONEPLUS A6003"))) {
            return "OnePlus 6";
        }
        if ((codename != null && (codename.equals("OnePlus6T")
                || codename.equals("OnePlus6TSingle")))
                || (model != null && (model.equals("ONEPLUS A6013")))) {
            return "OnePlus 6T";
        }
        // ----------------------------------------------------------------------------
        // Samsung
        if ((codename != null && (codename.equals("a53g")
                || codename.equals("a5lte")
                || codename.equals("a5ltechn")
                || codename.equals("a5ltectc")
                || codename.equals("a5ltezh")
                || codename.equals("a5ltezt")
                || codename.equals("a5ulte")
                || codename.equals("a5ultebmc")
                || codename.equals("a5ultektt")
                || codename.equals("a5ultelgt")
                || codename.equals("a5ulteskt")))
                || (model != null && (model.equals("SM-A5000")
                || model.equals("SM-A5009")
                || model.equals("SM-A500F")
                || model.equals("SM-A500F1")
                || model.equals("SM-A500FU")
                || model.equals("SM-A500G")
                || model.equals("SM-A500H")
                || model.equals("SM-A500K")
                || model.equals("SM-A500L")
                || model.equals("SM-A500M")
                || model.equals("SM-A500S")
                || model.equals("SM-A500W")
                || model.equals("SM-A500X")
                || model.equals("SM-A500XZ")
                || model.equals("SM-A500Y")
                || model.equals("SM-A500YZ")))) {
            return "Galaxy A5";
        }
        if ((codename != null && (codename.equals("vivaltods5m")))
                || (model != null && (model.equals("SM-G313HU")
                || model.equals("SM-G313HY")
                || model.equals("SM-G313M")
                || model.equals("SM-G313MY")))) {
            return "Galaxy Ace 4";
        }
        if ((codename != null && (codename.equals("GT-S6352")
                || codename.equals("GT-S6802")
                || codename.equals("GT-S6802B")
                || codename.equals("SCH-I579")
                || codename.equals("SCH-I589")
                || codename.equals("SCH-i579")
                || codename.equals("SCH-i589")))
                || (model != null && (model.equals("GT-S6352")
                || model.equals("GT-S6802")
                || model.equals("GT-S6802B")
                || model.equals("SCH-I589")
                || model.equals("SCH-i579")
                || model.equals("SCH-i589")))) {
            return "Galaxy Ace Duos";
        }
        if ((codename != null && (codename.equals("GT-S7500")
                || codename.equals("GT-S7500L")
                || codename.equals("GT-S7500T")
                || codename.equals("GT-S7500W")
                || codename.equals("GT-S7508")))
                || (model != null && (model.equals("GT-S7500")
                || model.equals("GT-S7500L")
                || model.equals("GT-S7500T")
                || model.equals("GT-S7500W")
                || model.equals("GT-S7508")))) {
            return "Galaxy Ace Plus";
        }
        if ((codename != null && (codename.equals("heat3gtfnvzw")
                || codename.equals("heatnfc3g")
                || codename.equals("heatqlte")))
                || (model != null && (model.equals("SM-G310HN")
                || model.equals("SM-G357FZ")
                || model.equals("SM-S765C")
                || model.equals("SM-S766C")))) {
            return "Galaxy Ace Style";
        }
        if ((codename != null && (codename.equals("vivalto3g")
                || codename.equals("vivalto3mve3g")
                || codename.equals("vivalto5mve3g")
                || codename.equals("vivaltolte")
                || codename.equals("vivaltonfc3g")))
                || (model != null && (model.equals("SM-G313F")
                || model.equals("SM-G313HN")
                || model.equals("SM-G313ML")
                || model.equals("SM-G313MU")
                || model.equals("SM-G316H")
                || model.equals("SM-G316HU")
                || model.equals("SM-G316M")
                || model.equals("SM-G316MY")))) {
            return "Galaxy Ace4";
        }
        if ((codename != null && (codename.equals("core33g")
                || codename.equals("coreprimelte")
                || codename.equals("coreprimelteaio")
                || codename.equals("coreprimeltelra")
                || codename.equals("coreprimeltespr")
                || codename.equals("coreprimeltetfnvzw")
                || codename.equals("coreprimeltevzw")
                || codename.equals("coreprimeve3g")
                || codename.equals("coreprimevelte")
                || codename.equals("cprimeltemtr")
                || codename.equals("cprimeltetmo")
                || codename.equals("rossalte")
                || codename.equals("rossaltectc")
                || codename.equals("rossaltexsa")))
                || (model != null && (model.equals("SAMSUNG-SM-G360AZ")
                || model.equals("SM-G3606")
                || model.equals("SM-G3608")
                || model.equals("SM-G3609")
                || model.equals("SM-G360F")
                || model.equals("SM-G360FY")
                || model.equals("SM-G360GY")
                || model.equals("SM-G360H")
                || model.equals("SM-G360HU")
                || model.equals("SM-G360M")
                || model.equals("SM-G360P")
                || model.equals("SM-G360R6")
                || model.equals("SM-G360T")
                || model.equals("SM-G360T1")
                || model.equals("SM-G360V")
                || model.equals("SM-G361F")
                || model.equals("SM-G361H")
                || model.equals("SM-G361HU")
                || model.equals("SM-G361M")
                || model.equals("SM-S820L")))) {
            return "Galaxy Core Prime";
        }
        if ((codename != null && (codename.equals("kanas")
                || codename.equals("kanas3g")
                || codename.equals("kanas3gcmcc")
                || codename.equals("kanas3gctc")
                || codename.equals("kanas3gnfc")))
                || (model != null && (model.equals("SM-G3556D")
                || model.equals("SM-G3558")
                || model.equals("SM-G3559")
                || model.equals("SM-G355H")
                || model.equals("SM-G355HN")
                || model.equals("SM-G355HQ")
                || model.equals("SM-G355M")))) {
            return "Galaxy Core2";
        }
        if ((codename != null && (codename.equals("e53g")
                || codename.equals("e5lte")
                || codename.equals("e5ltetfnvzw")
                || codename.equals("e5ltetw")))
                || (model != null && (model.equals("SM-E500F")
                || model.equals("SM-E500H")
                || model.equals("SM-E500M")
                || model.equals("SM-E500YZ")
                || model.equals("SM-S978L")))) {
            return "Galaxy E5";
        }
        if ((codename != null && (codename.equals("e73g")
                || codename.equals("e7lte")
                || codename.equals("e7ltechn")
                || codename.equals("e7ltectc")
                || codename.equals("e7ltehktw")))
                || (model != null && (model.equals("SM-E7000")
                || model.equals("SM-E7009")
                || model.equals("SM-E700F")
                || model.equals("SM-E700H")
                || model.equals("SM-E700M")))) {
            return "Galaxy E7";
        }
        if ((codename != null && (codename.equals("SCH-I629")
                || codename.equals("nevis")
                || codename.equals("nevis3g")
                || codename.equals("nevis3gcmcc")
                || codename.equals("nevisds")
                || codename.equals("nevisnvess")
                || codename.equals("nevisp")
                || codename.equals("nevisvess")
                || codename.equals("nevisw")))
                || (model != null && (model.equals("GT-S6790")
                || model.equals("GT-S6790E")
                || model.equals("GT-S6790L")
                || model.equals("GT-S6790N")
                || model.equals("GT-S6810")
                || model.equals("GT-S6810B")
                || model.equals("GT-S6810E")
                || model.equals("GT-S6810L")
                || model.equals("GT-S6810M")
                || model.equals("GT-S6810P")
                || model.equals("GT-S6812")
                || model.equals("GT-S6812B")
                || model.equals("GT-S6812C")
                || model.equals("GT-S6812i")
                || model.equals("GT-S6818")
                || model.equals("GT-S6818V")
                || model.equals("SCH-I629")))) {
            return "Galaxy Fame";
        }
        if ((codename != null && codename.equals("grandprimelteatt"))
                || (model != null && model.equals("SAMSUNG-SM-G530A"))) {
            return "Galaxy Go Prime";
        }
        if ((codename != null && (codename.equals("baffinlite")
                || codename.equals("baffinlitedtv")
                || codename.equals("baffinq3g")))
                || (model != null && (model.equals("GT-I9060")
                || model.equals("GT-I9060L")
                || model.equals("GT-I9063T")
                || model.equals("GT-I9082C")
                || model.equals("GT-I9168")
                || model.equals("GT-I9168I")))) {
            return "Galaxy Grand Neo";
        }
        if ((codename != null && (codename.equals("fortuna3g")
                || codename.equals("fortuna3gdtv")
                || codename.equals("fortunalte")
                || codename.equals("fortunaltectc")
                || codename.equals("fortunaltezh")
                || codename.equals("fortunaltezt")
                || codename.equals("fortunave3g")
                || codename.equals("gprimelteacg")
                || codename.equals("gprimeltecan")
                || codename.equals("gprimeltemtr")
                || codename.equals("gprimeltespr")
                || codename.equals("gprimeltetfnvzw")
                || codename.equals("gprimeltetmo")
                || codename.equals("gprimelteusc")
                || codename.equals("grandprimelte")
                || codename.equals("grandprimelteaio")
                || codename.equals("grandprimeve3g")
                || codename.equals("grandprimeve3gdtv")
                || codename.equals("grandprimevelte")
                || codename.equals("grandprimevelteltn")
                || codename.equals("grandprimeveltezt")))
                || (model != null && (model.equals("SAMSUNG-SM-G530AZ")
                || model.equals("SM-G5306W")
                || model.equals("SM-G5308W")
                || model.equals("SM-G5309W")
                || model.equals("SM-G530BT")
                || model.equals("SM-G530F")
                || model.equals("SM-G530FZ")
                || model.equals("SM-G530H")
                || model.equals("SM-G530M")
                || model.equals("SM-G530MU")
                || model.equals("SM-G530P")
                || model.equals("SM-G530R4")
                || model.equals("SM-G530R7")
                || model.equals("SM-G530T")
                || model.equals("SM-G530T1")
                || model.equals("SM-G530W")
                || model.equals("SM-G530Y")
                || model.equals("SM-G531BT")
                || model.equals("SM-G531F")
                || model.equals("SM-G531H")
                || model.equals("SM-G531M")
                || model.equals("SM-G531Y")
                || model.equals("SM-S920L")
                || model.equals("gprimelteacg")))) {
            return "Galaxy Grand Prime";
        }
        if ((codename != null && (codename.equals("ms013g")
                || codename.equals("ms013gdtv")
                || codename.equals("ms013gss")
                || codename.equals("ms01lte")
                || codename.equals("ms01ltektt")
                || codename.equals("ms01ltelgt")
                || codename.equals("ms01lteskt")))
                || (model != null && (model.equals("SM-G710")
                || model.equals("SM-G7102")
                || model.equals("SM-G7102T")
                || model.equals("SM-G7105")
                || model.equals("SM-G7105H")
                || model.equals("SM-G7105L")
                || model.equals("SM-G7106")
                || model.equals("SM-G7108")
                || model.equals("SM-G7109")
                || model.equals("SM-G710K")
                || model.equals("SM-G710L")
                || model.equals("SM-G710S")))) {
            return "Galaxy Grand2";
        }
        if ((codename != null && (codename.equals("j13g")
                || codename.equals("j13gtfnvzw")
                || codename.equals("j1lte")
                || codename.equals("j1nlte")
                || codename.equals("j1qltevzw")
                || codename.equals("j1xlte")
                || codename.equals("j1xlteaio")
                || codename.equals("j1xlteatt")
                || codename.equals("j1xltecan")
                || codename.equals("j1xqltespr")
                || codename.equals("j1xqltetfnvzw")))
                || (model != null && (model.equals("SAMSUNG-SM-J120A")
                || model.equals("SAMSUNG-SM-J120AZ")
                || model.equals("SM-J100F")
                || model.equals("SM-J100FN")
                || model.equals("SM-J100G")
                || model.equals("SM-J100H")
                || model.equals("SM-J100M")
                || model.equals("SM-J100ML")
                || model.equals("SM-J100MU")
                || model.equals("SM-J100VPP")
                || model.equals("SM-J100Y")
                || model.equals("SM-J120F")
                || model.equals("SM-J120FN")
                || model.equals("SM-J120M")
                || model.equals("SM-J120P")
                || model.equals("SM-J120W")
                || model.equals("SM-S120VL")
                || model.equals("SM-S777C")))) {
            return "Galaxy J1";
        }
        if ((codename != null && (codename.equals("j1acelte")
                || codename.equals("j1acelteltn")
                || codename.equals("j1acevelte")
                || codename.equals("j1pop3g")))
                || (model != null && (model.equals("SM-J110F")
                || model.equals("SM-J110G")
                || model.equals("SM-J110H")
                || model.equals("SM-J110L")
                || model.equals("SM-J110M")
                || model.equals("SM-J111F")
                || model.equals("SM-J111M")))) {
            return "Galaxy J1 Ace";
        }
        if ((codename != null && (codename.equals("j53g")
                || codename.equals("j5lte")
                || codename.equals("j5ltechn")
                || codename.equals("j5ltekx")
                || codename.equals("j5nlte")
                || codename.equals("j5y17lte")
                || codename.equals("j5y17ltektt")
                || codename.equals("j5y17ltelgt")
                || codename.equals("j5y17lteskt")
                || codename.equals("j5ylte")))
                || (model != null && (model.equals("SM-J5007")
                || model.equals("SM-J5008")
                || model.equals("SM-J500F")
                || model.equals("SM-J500FN")
                || model.equals("SM-J500G")
                || model.equals("SM-J500H")
                || model.equals("SM-J500M")
                || model.equals("SM-J500N0")
                || model.equals("SM-J500Y")
                || model.equals("SM-J530F")
                || model.equals("SM-J530FM")
                || model.equals("SM-J530K")
                || model.equals("SM-J530L")
                || model.equals("SM-J530S")))) {
            return "Galaxy J5";
        }
        if ((codename != null && (codename.equals("j75ltektt")
                || codename.equals("j7e3g")
                || codename.equals("j7elte")
                || codename.equals("j7ltechn")
                || codename.equals("j7popelteue")
                || codename.equals("j7poplteusc")
                || codename.equals("j7popltevzw")
                || codename.equals("j7topeltevzw")
                || codename.equals("j7toplteatt")
                || codename.equals("j7toplteue")
                || codename.equals("j7y17lte")))
                || (model != null && (model.equals("SM-J7008")
                || model.equals("SM-J700F")
                || model.equals("SM-J700H")
                || model.equals("SM-J700K")
                || model.equals("SM-J700M")
                || model.equals("SM-J727R4")
                || model.equals("SM-J727U")
                || model.equals("SM-J727VPP")
                || model.equals("SM-J730F")
                || model.equals("SM-J730FM")
                || model.equals("SM-J737A")
                || model.equals("SM-J737U")
                || model.equals("SM-J737VPP")))) {
            return "Galaxy J7";
        }
        if ((codename != null && (codename.equals("maguro")
                || codename.equals("toro")
                || codename.equals("toroplus")))
                || (model != null && (model.equals("Galaxy X")))) {
            return "Galaxy Nexus";
        }
        if ((codename != null && (codename.equals("lt033g")
                || codename.equals("lt03ltektt")
                || codename.equals("lt03ltelgt")
                || codename.equals("lt03lteskt")
                || codename.equals("p4notelte")
                || codename.equals("p4noteltektt")
                || codename.equals("p4noteltelgt")
                || codename.equals("p4notelteskt")
                || codename.equals("p4noteltespr")
                || codename.equals("p4notelteusc")
                || codename.equals("p4noteltevzw")
                || codename.equals("p4noterf")
                || codename.equals("p4noterfktt")
                || codename.equals("p4notewifi")
                || codename.equals("p4notewifi43241any")
                || codename.equals("p4notewifiany")
                || codename.equals("p4notewifiktt")
                || codename.equals("p4notewifiww")))
                || (model != null && (model.equals("GT-N8000")
                || model.equals("GT-N8005")
                || model.equals("GT-N8010")
                || model.equals("GT-N8013")
                || model.equals("GT-N8020")
                || model.equals("SCH-I925")
                || model.equals("SCH-I925U")
                || model.equals("SHV-E230K")
                || model.equals("SHV-E230L")
                || model.equals("SHV-E230S")
                || model.equals("SHW-M480K")
                || model.equals("SHW-M480W")
                || model.equals("SHW-M485W")
                || model.equals("SHW-M486W")
                || model.equals("SM-P601")
                || model.equals("SM-P602")
                || model.equals("SM-P605K")
                || model.equals("SM-P605L")
                || model.equals("SM-P605S")
                || model.equals("SPH-P600")))) {
            return "Galaxy Note 10.1";
        }
        if ((codename != null && (codename.equals("SC-01G")
                || codename.equals("SCL24")
                || codename.equals("tbeltektt")
                || codename.equals("tbeltelgt")
                || codename.equals("tbelteskt")
                || codename.equals("tblte")
                || codename.equals("tblteatt")
                || codename.equals("tbltecan")
                || codename.equals("tbltechn")
                || codename.equals("tbltespr")
                || codename.equals("tbltetmo")
                || codename.equals("tblteusc")
                || codename.equals("tbltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-N915A")
                || model.equals("SC-01G")
                || model.equals("SCL24")
                || model.equals("SM-N9150")
                || model.equals("SM-N915F")
                || model.equals("SM-N915FY")
                || model.equals("SM-N915G")
                || model.equals("SM-N915K")
                || model.equals("SM-N915L")
                || model.equals("SM-N915P")
                || model.equals("SM-N915R4")
                || model.equals("SM-N915S")
                || model.equals("SM-N915T")
                || model.equals("SM-N915T3")
                || model.equals("SM-N915V")
                || model.equals("SM-N915W8")
                || model.equals("SM-N915X")))) {
            return "Galaxy Note Edge";
        }
        if ((codename != null && (codename.equals("v1a3g")
                || codename.equals("v1awifi")
                || codename.equals("v1awifikx")
                || codename.equals("viennalte")
                || codename.equals("viennalteatt")
                || codename.equals("viennaltekx")
                || codename.equals("viennaltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-P907A")
                || model.equals("SM-P900")
                || model.equals("SM-P901")
                || model.equals("SM-P905")
                || model.equals("SM-P905F0")
                || model.equals("SM-P905M")
                || model.equals("SM-P905V")))) {
            return "Galaxy Note Pro 12.2";
        }
        if ((codename != null && (codename.equals("tre3caltektt")
                || codename.equals("tre3caltelgt")
                || codename.equals("tre3calteskt")
                || codename.equals("tre3g")
                || codename.equals("trelte")
                || codename.equals("treltektt")
                || codename.equals("treltelgt")
                || codename.equals("trelteskt")
                || codename.equals("trhplte")
                || codename.equals("trlte")
                || codename.equals("trlteatt")
                || codename.equals("trltecan")
                || codename.equals("trltechn")
                || codename.equals("trltechnzh")
                || codename.equals("trltespr")
                || codename.equals("trltetmo")
                || codename.equals("trlteusc")
                || codename.equals("trltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-N910A")
                || model.equals("SM-N9100")
                || model.equals("SM-N9106W")
                || model.equals("SM-N9108V")
                || model.equals("SM-N9109W")
                || model.equals("SM-N910C")
                || model.equals("SM-N910F")
                || model.equals("SM-N910G")
                || model.equals("SM-N910H")
                || model.equals("SM-N910K")
                || model.equals("SM-N910L")
                || model.equals("SM-N910P")
                || model.equals("SM-N910R4")
                || model.equals("SM-N910S")
                || model.equals("SM-N910T")
                || model.equals("SM-N910T2")
                || model.equals("SM-N910T3")
                || model.equals("SM-N910U")
                || model.equals("SM-N910V")
                || model.equals("SM-N910W8")
                || model.equals("SM-N910X")
                || model.equals("SM-N916K")
                || model.equals("SM-N916L")
                || model.equals("SM-N916S")))) {
            return "Galaxy Note4";
        }
        if ((codename != null && (codename.equals("noblelte")
                || codename.equals("noblelteacg")
                || codename.equals("noblelteatt")
                || codename.equals("nobleltebmc")
                || codename.equals("nobleltechn")
                || codename.equals("nobleltecmcc")
                || codename.equals("nobleltehk")
                || codename.equals("nobleltektt")
                || codename.equals("nobleltelgt")
                || codename.equals("nobleltelra")
                || codename.equals("noblelteskt")
                || codename.equals("nobleltespr")
                || codename.equals("nobleltetmo")
                || codename.equals("noblelteusc")
                || codename.equals("nobleltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-N920A")
                || model.equals("SM-N9200")
                || model.equals("SM-N9208")
                || model.equals("SM-N920C")
                || model.equals("SM-N920F")
                || model.equals("SM-N920G")
                || model.equals("SM-N920I")
                || model.equals("SM-N920K")
                || model.equals("SM-N920L")
                || model.equals("SM-N920P")
                || model.equals("SM-N920R4")
                || model.equals("SM-N920R6")
                || model.equals("SM-N920R7")
                || model.equals("SM-N920S")
                || model.equals("SM-N920T")
                || model.equals("SM-N920V")
                || model.equals("SM-N920W8")
                || model.equals("SM-N920X")))) {
            return "Galaxy Note5";
        }
        if ((codename != null && (codename.equals("SC-01J")
                || codename.equals("SCV34")
                || codename.equals("gracelte")
                || codename.equals("graceltektt")
                || codename.equals("graceltelgt")
                || codename.equals("gracelteskt")
                || codename.equals("graceqlteacg")
                || codename.equals("graceqlteatt")
                || codename.equals("graceqltebmc")
                || codename.equals("graceqltechn")
                || codename.equals("graceqltedcm")
                || codename.equals("graceqltelra")
                || codename.equals("graceqltespr")
                || codename.equals("graceqltetfnvzw")
                || codename.equals("graceqltetmo")
                || codename.equals("graceqlteue")
                || codename.equals("graceqlteusc")
                || codename.equals("graceqltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-N930A")
                || model.equals("SC-01J")
                || model.equals("SCV34")
                || model.equals("SGH-N037")
                || model.equals("SM-N9300")
                || model.equals("SM-N930F")
                || model.equals("SM-N930K")
                || model.equals("SM-N930L")
                || model.equals("SM-N930P")
                || model.equals("SM-N930R4")
                || model.equals("SM-N930R6")
                || model.equals("SM-N930R7")
                || model.equals("SM-N930S")
                || model.equals("SM-N930T")
                || model.equals("SM-N930U")
                || model.equals("SM-N930V")
                || model.equals("SM-N930VL")
                || model.equals("SM-N930W8")
                || model.equals("SM-N930X")))) {
            return "Galaxy Note7";
        }
        if ((codename != null && (codename.equals("SC-01K")
                || codename.equals("SCV37")
                || codename.equals("greatlte")
                || codename.equals("greatlteks")
                || codename.equals("greatqlte")
                || codename.equals("greatqltechn")
                || codename.equals("greatqltecmcc")
                || codename.equals("greatqltecs")
                || codename.equals("greatqlteue")))
                || (model != null && (model.equals("SC-01K")
                || model.equals("SCV37")
                || model.equals("SM-N9500")
                || model.equals("SM-N9508")
                || model.equals("SM-N950F")
                || model.equals("SM-N950N")
                || model.equals("SM-N950U")
                || model.equals("SM-N950U1")
                || model.equals("SM-N950W")
                || model.equals("SM-N950XN")))) {
            return "Galaxy Note8";
        }
        if ((codename != null && (codename.equals("o5lte")
                || codename.equals("o5ltechn")
                || codename.equals("on5ltemtr")
                || codename.equals("on5ltetfntmo")
                || codename.equals("on5ltetmo")))
                || (model != null && (model.equals("SM-G5500")
                || model.equals("SM-G550FY")
                || model.equals("SM-G550T")
                || model.equals("SM-G550T1")
                || model.equals("SM-G550T2")
                || model.equals("SM-S550TL")))) {
            return "Galaxy On5";
        }
        if ((codename != null && (codename.equals("o7lte")
                || codename.equals("o7ltechn")
                || codename.equals("on7elte")))
                || (model != null && (model.equals("SM-G6000")
                || model.equals("SM-G600F")
                || model.equals("SM-G600FY")))) {
            return "Galaxy On7";
        }
        if ((codename != null && (codename.equals("GT-I9000")
                || codename.equals("GT-I9000B")
                || codename.equals("GT-I9000M")
                || codename.equals("GT-I9000T")
                || codename.equals("GT-I9003")
                || codename.equals("GT-I9003L")
                || codename.equals("GT-I9008L")
                || codename.equals("GT-I9010")
                || codename.equals("GT-I9018")
                || codename.equals("GT-I9050")
                || codename.equals("SC-02B")
                || codename.equals("SCH-I500")
                || codename.equals("SCH-S950C")
                || codename.equals("SCH-i909")
                || codename.equals("SGH-I897")
                || codename.equals("SGH-T959V")
                || codename.equals("SGH-T959W")
                || codename.equals("SHW-M110S")
                || codename.equals("SHW-M190S")
                || codename.equals("SPH-D700")
                || codename.equals("loganlte")))
                || (model != null && (model.equals("GT-I9000")
                || model.equals("GT-I9000B")
                || model.equals("GT-I9000M")
                || model.equals("GT-I9000T")
                || model.equals("GT-I9003")
                || model.equals("GT-I9003L")
                || model.equals("GT-I9008L")
                || model.equals("GT-I9010")
                || model.equals("GT-I9018")
                || model.equals("GT-I9050")
                || model.equals("GT-S7275")
                || model.equals("SAMSUNG-SGH-I897")
                || model.equals("SC-02B")
                || model.equals("SCH-I500")
                || model.equals("SCH-S950C")
                || model.equals("SCH-i909")
                || model.equals("SGH-T959V")
                || model.equals("SGH-T959W")
                || model.equals("SHW-M110S")
                || model.equals("SHW-M190S")
                || model.equals("SPH-D700")))) {
            return "Galaxy S";
        }
        if ((codename != null && (codename.equals("kylechn")
                || codename.equals("kyleopen")
                || codename.equals("kyletdcmcc")))
                || (model != null && (model.equals("GT-S7562")
                || model.equals("GT-S7568")))) {
            return "Galaxy S Duos";
        }
        if ((codename != null && (codename.equals("kyleprods")))
                || (model != null && (model.equals("GT-S7582")
                || model.equals("GT-S7582L")))) {
            return "Galaxy S Duos2";
        }
        if ((codename != null && codename.equals("vivalto3gvn"))
                || (model != null && model.equals("SM-G313HZ"))) {
            return "Galaxy S Duos3";
        }
        if ((codename != null && (codename.equals("SC-03E")
                || codename.equals("c1att")
                || codename.equals("c1ktt")
                || codename.equals("c1lgt")
                || codename.equals("c1skt")
                || codename.equals("d2att")
                || codename.equals("d2can")
                || codename.equals("d2cri")
                || codename.equals("d2dcm")
                || codename.equals("d2lteMetroPCS")
                || codename.equals("d2lterefreshspr")
                || codename.equals("d2ltetmo")
                || codename.equals("d2mtr")
                || codename.equals("d2spi")
                || codename.equals("d2spr")
                || codename.equals("d2tfnspr")
                || codename.equals("d2tfnvzw")
                || codename.equals("d2tmo")
                || codename.equals("d2usc")
                || codename.equals("d2vmu")
                || codename.equals("d2vzw")
                || codename.equals("d2xar")
                || codename.equals("m0")
                || codename.equals("m0apt")
                || codename.equals("m0chn")
                || codename.equals("m0cmcc")
                || codename.equals("m0ctc")
                || codename.equals("m0ctcduos")
                || codename.equals("m0skt")
                || codename.equals("m3")
                || codename.equals("m3dcm")))
                || (model != null && (model.equals("GT-I9300")
                || model.equals("GT-I9300T")
                || model.equals("GT-I9305")
                || model.equals("GT-I9305N")
                || model.equals("GT-I9305T")
                || model.equals("GT-I9308")
                || model.equals("Gravity")
                || model.equals("GravityQuad")
                || model.equals("SAMSUNG-SGH-I747")
                || model.equals("SC-03E")
                || model.equals("SC-06D")
                || model.equals("SCH-I535")
                || model.equals("SCH-I535PP")
                || model.equals("SCH-I939")
                || model.equals("SCH-I939D")
                || model.equals("SCH-L710")
                || model.equals("SCH-R530C")
                || model.equals("SCH-R530M")
                || model.equals("SCH-R530U")
                || model.equals("SCH-R530X")
                || model.equals("SCH-S960L")
                || model.equals("SCH-S968C")
                || model.equals("SGH-I747M")
                || model.equals("SGH-I748")
                || model.equals("SGH-T999")
                || model.equals("SGH-T999L")
                || model.equals("SGH-T999N")
                || model.equals("SGH-T999V")
                || model.equals("SHV-E210K")
                || model.equals("SHV-E210L")
                || model.equals("SHV-E210S")
                || model.equals("SHW-M440S")
                || model.equals("SPH-L710")
                || model.equals("SPH-L710T")))) {
            return "Galaxy S3";
        }
        if ((codename != null && (codename.equals("golden")
                || codename.equals("goldenlteatt")
                || codename.equals("goldenltebmc")
                || codename.equals("goldenltevzw")
                || codename.equals("goldenve3g")))
                || (model != null && (model.equals("GT-I8190")
                || model.equals("GT-I8190L")
                || model.equals("GT-I8190N")
                || model.equals("GT-I8190T")
                || model.equals("GT-I8200L")
                || model.equals("SAMSUNG-SM-G730A")
                || model.equals("SM-G730V")
                || model.equals("SM-G730W8")))) {
            return "Galaxy S3 Mini";
        }
        if ((codename != null && (codename.equals("goldenve3g")
                || codename.equals("goldenvess3g")))
                || (model != null && (model.equals("GT-I8200")
                || model.equals("GT-I8200N")
                || model.equals("GT-I8200Q")))) {
            return "Galaxy S3 Mini Value Edition";
        }
        if ((codename != null && (codename.equals("s3ve3g")
                || codename.equals("s3ve3gdd")
                || codename.equals("s3ve3gds")
                || codename.equals("s3ve3gdsdd")))
                || (model != null && (model.equals("GT-I9300I")
                || model.equals("GT-I9301I")
                || model.equals("GT-I9301Q")))) {
            return "Galaxy S3 Neo";
        }
        if ((codename != null && (codename.equals("SC-04E")
                || codename.equals("ja3g")
                || codename.equals("ja3gduosctc")
                || codename.equals("jaltektt")
                || codename.equals("jaltelgt")
                || codename.equals("jalteskt")
                || codename.equals("jflte")
                || codename.equals("jflteMetroPCS")
                || codename.equals("jflteaio")
                || codename.equals("jflteatt")
                || codename.equals("jfltecan")
                || codename.equals("jfltecri")
                || codename.equals("jfltecsp")
                || codename.equals("jfltelra")
                || codename.equals("jflterefreshspr")
                || codename.equals("jfltespr")
                || codename.equals("jfltetfnatt")
                || codename.equals("jfltetfntmo")
                || codename.equals("jfltetmo")
                || codename.equals("jflteusc")
                || codename.equals("jfltevzw")
                || codename.equals("jfltevzwpp")
                || codename.equals("jftdd")
                || codename.equals("jfvelte")
                || codename.equals("jfwifi")
                || codename.equals("jsglte")
                || codename.equals("ks01lte")
                || codename.equals("ks01ltektt")
                || codename.equals("ks01ltelgt")))
                || (model != null && (model.equals("GT-I9500")
                || model.equals("GT-I9505")
                || model.equals("GT-I9505X")
                || model.equals("GT-I9506")
                || model.equals("GT-I9507")
                || model.equals("GT-I9507V")
                || model.equals("GT-I9508")
                || model.equals("GT-I9508C")
                || model.equals("GT-I9508V")
                || model.equals("GT-I9515")
                || model.equals("GT-I9515L")
                || model.equals("SAMSUNG-SGH-I337")
                || model.equals("SAMSUNG-SGH-I337Z")
                || model.equals("SC-04E")
                || model.equals("SCH-I545")
                || model.equals("SCH-I545L")
                || model.equals("SCH-I545PP")
                || model.equals("SCH-I959")
                || model.equals("SCH-R970")
                || model.equals("SCH-R970C")
                || model.equals("SCH-R970X")
                || model.equals("SGH-I337M")
                || model.equals("SGH-M919")
                || model.equals("SGH-M919N")
                || model.equals("SGH-M919V")
                || model.equals("SGH-S970G")
                || model.equals("SHV-E300K")
                || model.equals("SHV-E300L")
                || model.equals("SHV-E300S")
                || model.equals("SHV-E330K")
                || model.equals("SHV-E330L")
                || model.equals("SM-S975L")
                || model.equals("SPH-L720")
                || model.equals("SPH-L720T")))) {
            return "Galaxy S4";
        }
        if ((codename != null && (codename.equals("serrano3g")
                || codename.equals("serranods")
                || codename.equals("serranolte")
                || codename.equals("serranoltebmc")
                || codename.equals("serranoltektt")
                || codename.equals("serranoltekx")
                || codename.equals("serranoltelra")
                || codename.equals("serranoltespr")
                || codename.equals("serranolteusc")
                || codename.equals("serranoltevzw")
                || codename.equals("serranove3g")
                || codename.equals("serranovelte")
                || codename.equals("serranovolteatt")))
                || (model != null && (model.equals("GT-I9190")
                || model.equals("GT-I9192")
                || model.equals("GT-I9192I")
                || model.equals("GT-I9195")
                || model.equals("GT-I9195I")
                || model.equals("GT-I9195L")
                || model.equals("GT-I9195T")
                || model.equals("GT-I9195X")
                || model.equals("GT-I9197")
                || model.equals("SAMSUNG-SGH-I257")
                || model.equals("SCH-I435")
                || model.equals("SCH-I435L")
                || model.equals("SCH-R890")
                || model.equals("SGH-I257M")
                || model.equals("SHV-E370D")
                || model.equals("SHV-E370K")
                || model.equals("SPH-L520")))) {
            return "Galaxy S4 Mini";
        }
        if ((codename != null && (codename.equals("SC-04F")
                || codename.equals("SCL23")
                || codename.equals("k3g")
                || codename.equals("klte")
                || codename.equals("klteMetroPCS")
                || codename.equals("klteacg")
                || codename.equals("klteaio")
                || codename.equals("klteatt")
                || codename.equals("kltecan")
                || codename.equals("klteduoszn")
                || codename.equals("kltektt")
                || codename.equals("kltelgt")
                || codename.equals("kltelra")
                || codename.equals("klteskt")
                || codename.equals("kltespr")
                || codename.equals("kltetfnvzw")
                || codename.equals("kltetmo")
                || codename.equals("klteusc")
                || codename.equals("kltevzw")
                || codename.equals("kwifi")
                || codename.equals("lentisltektt")
                || codename.equals("lentisltelgt")
                || codename.equals("lentislteskt")))
                || (model != null && (model.equals("SAMSUNG-SM-G900A")
                || model.equals("SAMSUNG-SM-G900AZ")
                || model.equals("SC-04F")
                || model.equals("SCL23")
                || model.equals("SM-G9006W")
                || model.equals("SM-G9008W")
                || model.equals("SM-G9009W")
                || model.equals("SM-G900F")
                || model.equals("SM-G900FQ")
                || model.equals("SM-G900H")
                || model.equals("SM-G900I")
                || model.equals("SM-G900K")
                || model.equals("SM-G900L")
                || model.equals("SM-G900M")
                || model.equals("SM-G900MD")
                || model.equals("SM-G900P")
                || model.equals("SM-G900R4")
                || model.equals("SM-G900R6")
                || model.equals("SM-G900R7")
                || model.equals("SM-G900S")
                || model.equals("SM-G900T")
                || model.equals("SM-G900T1")
                || model.equals("SM-G900T3")
                || model.equals("SM-G900T4")
                || model.equals("SM-G900V")
                || model.equals("SM-G900W8")
                || model.equals("SM-G900X")
                || model.equals("SM-G906K")
                || model.equals("SM-G906L")
                || model.equals("SM-G906S")
                || model.equals("SM-S903VL")))) {
            return "Galaxy S5";
        }
        if ((codename != null && (codename.equals("s5neolte")
                || codename.equals("s5neoltecan")))
                || (model != null && (model.equals("SM-G903F")
                || model.equals("SM-G903M")
                || model.equals("SM-G903W")))) {
            return "Galaxy S5 Neo";
        }
        if ((codename != null && (codename.equals("SC-05G")
                || codename.equals("zeroflte")
                || codename.equals("zeroflteacg")
                || codename.equals("zeroflteaio")
                || codename.equals("zeroflteatt")
                || codename.equals("zerofltebmc")
                || codename.equals("zerofltechn")
                || codename.equals("zerofltectc")
                || codename.equals("zerofltektt")
                || codename.equals("zerofltelgt")
                || codename.equals("zerofltelra")
                || codename.equals("zerofltemtr")
                || codename.equals("zeroflteskt")
                || codename.equals("zerofltespr")
                || codename.equals("zerofltetfnvzw")
                || codename.equals("zerofltetmo")
                || codename.equals("zeroflteusc")
                || codename.equals("zerofltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-G920A")
                || model.equals("SAMSUNG-SM-G920AZ")
                || model.equals("SC-05G")
                || model.equals("SM-G9200")
                || model.equals("SM-G9208")
                || model.equals("SM-G9209")
                || model.equals("SM-G920F")
                || model.equals("SM-G920I")
                || model.equals("SM-G920K")
                || model.equals("SM-G920L")
                || model.equals("SM-G920P")
                || model.equals("SM-G920R4")
                || model.equals("SM-G920R6")
                || model.equals("SM-G920R7")
                || model.equals("SM-G920S")
                || model.equals("SM-G920T")
                || model.equals("SM-G920T1")
                || model.equals("SM-G920V")
                || model.equals("SM-G920W8")
                || model.equals("SM-G920X")
                || model.equals("SM-S906L")
                || model.equals("SM-S907VL")))) {
            return "Galaxy S6";
        }
        if ((codename != null && (codename.equals("404SC")
                || codename.equals("SC-04G")
                || codename.equals("SCV31")
                || codename.equals("zerolte")
                || codename.equals("zerolteacg")
                || codename.equals("zerolteatt")
                || codename.equals("zeroltebmc")
                || codename.equals("zeroltechn")
                || codename.equals("zeroltektt")
                || codename.equals("zeroltelra")
                || codename.equals("zerolteskt")
                || codename.equals("zeroltespr")
                || codename.equals("zeroltetmo")
                || codename.equals("zerolteusc")
                || codename.equals("zeroltevzw")))
                || (model != null && (model.equals("404SC")
                || model.equals("SAMSUNG-SM-G925A")
                || model.equals("SC-04G")
                || model.equals("SCV31")
                || model.equals("SM-G9250")
                || model.equals("SM-G925I")
                || model.equals("SM-G925K")
                || model.equals("SM-G925P")
                || model.equals("SM-G925R4")
                || model.equals("SM-G925R6")
                || model.equals("SM-G925R7")
                || model.equals("SM-G925S")
                || model.equals("SM-G925T")
                || model.equals("SM-G925V")
                || model.equals("SM-G925W8")
                || model.equals("SM-G925X")))) {
            return "Galaxy S6 Edge";
        }
        if ((codename != null && (codename.equals("zenlte")
                || codename.equals("zenlteatt")
                || codename.equals("zenltebmc")
                || codename.equals("zenltechn")
                || codename.equals("zenltektt")
                || codename.equals("zenltekx")
                || codename.equals("zenltelgt")
                || codename.equals("zenlteskt")
                || codename.equals("zenltespr")
                || codename.equals("zenltetmo")
                || codename.equals("zenlteusc")
                || codename.equals("zenltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-G928A")
                || model.equals("SM-G9280")
                || model.equals("SM-G9287C")
                || model.equals("SM-G928C")
                || model.equals("SM-G928G")
                || model.equals("SM-G928I")
                || model.equals("SM-G928K")
                || model.equals("SM-G928L")
                || model.equals("SM-G928N0")
                || model.equals("SM-G928P")
                || model.equals("SM-G928R4")
                || model.equals("SM-G928S")
                || model.equals("SM-G928T")
                || model.equals("SM-G928V")
                || model.equals("SM-G928W8")
                || model.equals("SM-G928X")))) {
            return "Galaxy S6 Edge+";
        }
        if ((codename != null && (codename.equals("herolte")
                || codename.equals("heroltebmc")
                || codename.equals("heroltektt")
                || codename.equals("heroltelgt")
                || codename.equals("herolteskt")
                || codename.equals("heroqlteacg")
                || codename.equals("heroqlteaio")
                || codename.equals("heroqlteatt")
                || codename.equals("heroqltecctvzw")
                || codename.equals("heroqltechn")
                || codename.equals("heroqltelra")
                || codename.equals("heroqltemtr")
                || codename.equals("heroqltespr")
                || codename.equals("heroqltetfnvzw")
                || codename.equals("heroqltetmo")
                || codename.equals("heroqlteue")
                || codename.equals("heroqlteusc")
                || codename.equals("heroqltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-G930A")
                || model.equals("SAMSUNG-SM-G930AZ")
                || model.equals("SM-G9300")
                || model.equals("SM-G9308")
                || model.equals("SM-G930F")
                || model.equals("SM-G930K")
                || model.equals("SM-G930L")
                || model.equals("SM-G930P")
                || model.equals("SM-G930R4")
                || model.equals("SM-G930R6")
                || model.equals("SM-G930R7")
                || model.equals("SM-G930S")
                || model.equals("SM-G930T")
                || model.equals("SM-G930T1")
                || model.equals("SM-G930U")
                || model.equals("SM-G930V")
                || model.equals("SM-G930VC")
                || model.equals("SM-G930VL")
                || model.equals("SM-G930W8")
                || model.equals("SM-G930X")))) {
            return "Galaxy S7";
        }
        if ((codename != null && (codename.equals("SC-02H")
                || codename.equals("SCV33")
                || codename.equals("hero2lte")
                || codename.equals("hero2ltebmc")
                || codename.equals("hero2ltektt")
                || codename.equals("hero2lteskt")
                || codename.equals("hero2qlteatt")
                || codename.equals("hero2qltecctvzw")
                || codename.equals("hero2qltespr")
                || codename.equals("hero2qltetmo")
                || codename.equals("hero2qlteusc")
                || codename.equals("hero2qltevzw")))
                || (model != null && (model.equals("SAMSUNG-SM-G935A")
                || model.equals("SC-02H")
                || model.equals("SCV33")
                || model.equals("SM-G935K")
                || model.equals("SM-G935P")
                || model.equals("SM-G935R4")
                || model.equals("SM-G935S")
                || model.equals("SM-G935T")
                || model.equals("SM-G935V")
                || model.equals("SM-G935VC")
                || model.equals("SM-G935W8")
                || model.equals("SM-G935X")))) {
            return "Galaxy S7 Edge";
        }
        if ((codename != null && (codename.equals("SC-02J")
                || codename.equals("SCV36")
                || codename.equals("dreamlte")
                || codename.equals("dreamlteks")
                || codename.equals("dreamqltecan")
                || codename.equals("dreamqltechn")
                || codename.equals("dreamqltecmcc")
                || codename.equals("dreamqltesq")
                || codename.equals("dreamqlteue")))
                || (model != null && (model.equals("SC-02J")
                || model.equals("SCV36")
                || model.equals("SM-G9500")
                || model.equals("SM-G9508")
                || model.equals("SM-G950F")
                || model.equals("SM-G950N")
                || model.equals("SM-G950U")
                || model.equals("SM-G950U1")
                || model.equals("SM-G950W")))) {
            return "Galaxy S8";
        }
        if ((codename != null && (codename.equals("SC-03J")
                || codename.equals("SCV35")
                || codename.equals("dream2lte")
                || codename.equals("dream2lteks")
                || codename.equals("dream2qltecan")
                || codename.equals("dream2qltechn")
                || codename.equals("dream2qltesq")
                || codename.equals("dream2qlteue")))
                || (model != null && (model.equals("SC-03J")
                || model.equals("SCV35")
                || model.equals("SM-G9550")
                || model.equals("SM-G955F")
                || model.equals("SM-G955N")
                || model.equals("SM-G955U")
                || model.equals("SM-G955U1")
                || model.equals("SM-G955W")))) {
            return "Galaxy S8+";
        }
        if ((codename != null && (codename.equals("SC-02K")
                || codename.equals("SCV38")
                || codename.equals("starlte")
                || codename.equals("starlteks")
                || codename.equals("starqltechn")
                || codename.equals("starqltecmcc")
                || codename.equals("starqltecs")
                || codename.equals("starqltesq")
                || codename.equals("starqlteue")))
                || (model != null && (model.equals("SC-02K")
                || model.equals("SCV38")
                || model.equals("SM-G9600")
                || model.equals("SM-G9608")
                || model.equals("SM-G960F")
                || model.equals("SM-G960N")
                || model.equals("SM-G960U")
                || model.equals("SM-G960U1")
                || model.equals("SM-G960W")))) {
            return "Galaxy S9";
        }
        if ((codename != null && (codename.equals("SC-03K")
                || codename.equals("SCV39")
                || codename.equals("star2lte")
                || codename.equals("star2lteks")
                || codename.equals("star2qltechn")
                || codename.equals("star2qltecs")
                || codename.equals("star2qltesq")
                || codename.equals("star2qlteue")))
                || (model != null && (model.equals("SC-03K")
                || model.equals("SCV39")
                || model.equals("SM-G9650")
                || model.equals("SM-G965F")
                || model.equals("SM-G965N")
                || model.equals("SM-G965U")
                || model.equals("SM-G965U1")
                || model.equals("SM-G965W")))) {
            return "Galaxy S9+";
        }
        if ((codename != null && (codename.equals("GT-P7500")
                || codename.equals("GT-P7500D")
                || codename.equals("GT-P7503")
                || codename.equals("GT-P7510")
                || codename.equals("SC-01D")
                || codename.equals("SCH-I905")
                || codename.equals("SGH-T859")
                || codename.equals("SHW-M300W")
                || codename.equals("SHW-M380K")
                || codename.equals("SHW-M380S")
                || codename.equals("SHW-M380W")))
                || (model != null && (model.equals("GT-P7500")
                || model.equals("GT-P7500D")
                || model.equals("GT-P7503")
                || model.equals("GT-P7510")
                || model.equals("SC-01D")
                || model.equals("SCH-I905")
                || model.equals("SGH-T859")
                || model.equals("SHW-M300W")
                || model.equals("SHW-M380K")
                || model.equals("SHW-M380S")
                || model.equals("SHW-M380W")))) {
            return "Galaxy Tab 10.1";
        }
        if ((codename != null && (codename.equals("GT-P6200")
                || codename.equals("GT-P6200L")
                || codename.equals("GT-P6201")
                || codename.equals("GT-P6210")
                || codename.equals("GT-P6211")
                || codename.equals("SC-02D")
                || codename.equals("SGH-T869")
                || codename.equals("SHW-M430W")))
                || (model != null && (model.equals("GT-P6200")
                || model.equals("GT-P6200L")
                || model.equals("GT-P6201")
                || model.equals("GT-P6210")
                || model.equals("GT-P6211")
                || model.equals("SC-02D")
                || model.equals("SGH-T869")
                || model.equals("SHW-M430W")))) {
            return "Galaxy Tab 7.0 Plus";
        }
        if ((codename != null && (codename.equals("gteslteatt")
                || codename.equals("gtesltebmc")
                || codename.equals("gtesltelgt")
                || codename.equals("gteslteskt")
                || codename.equals("gtesltetmo")
                || codename.equals("gtesltetw")
                || codename.equals("gtesltevzw")
                || codename.equals("gtesqltespr")
                || codename.equals("gtesqlteusc")))
                || (model != null && (model.equals("SAMSUNG-SM-T377A")
                || model.equals("SM-T375L")
                || model.equals("SM-T375S")
                || model.equals("SM-T3777")
                || model.equals("SM-T377P")
                || model.equals("SM-T377R4")
                || model.equals("SM-T377T")
                || model.equals("SM-T377V")
                || model.equals("SM-T377W")))) {
            return "Galaxy Tab E 8.0";
        }
        if ((codename != null && (codename.equals("gtel3g")
                || codename.equals("gtelltevzw")
                || codename.equals("gtelwifi")
                || codename.equals("gtelwifichn")
                || codename.equals("gtelwifiue")))
                || (model != null && (model.equals("SM-T560")
                || model.equals("SM-T560NU")
                || model.equals("SM-T561")
                || model.equals("SM-T561M")
                || model.equals("SM-T561Y")
                || model.equals("SM-T562")
                || model.equals("SM-T567V")))) {
            return "Galaxy Tab E 9.6";
        }
        if ((codename != null && (codename.equals("403SC")
                || codename.equals("degas2wifi")
                || codename.equals("degas2wifibmwchn")
                || codename.equals("degas3g")
                || codename.equals("degaslte")
                || codename.equals("degasltespr")
                || codename.equals("degasltevzw")
                || codename.equals("degasvelte")
                || codename.equals("degasveltechn")
                || codename.equals("degaswifi")
                || codename.equals("degaswifibmwzc")
                || codename.equals("degaswifidtv")
                || codename.equals("degaswifiopenbnn")
                || codename.equals("degaswifiue")))
                || (model != null && (model.equals("403SC")
                || model.equals("SM-T230")
                || model.equals("SM-T230NT")
                || model.equals("SM-T230NU")
                || model.equals("SM-T230NW")
                || model.equals("SM-T230NY")
                || model.equals("SM-T230X")
                || model.equals("SM-T231")
                || model.equals("SM-T232")
                || model.equals("SM-T235")
                || model.equals("SM-T235Y")
                || model.equals("SM-T237P")
                || model.equals("SM-T237V")
                || model.equals("SM-T239")
                || model.equals("SM-T2397")
                || model.equals("SM-T239C")
                || model.equals("SM-T239M")))) {
            return "Galaxy Tab4 7.0";
        }
        if ((codename != null && (codename.equals("gvlte")
                || codename.equals("gvlteatt")
                || codename.equals("gvltevzw")
                || codename.equals("gvltexsp")
                || codename.equals("gvwifijpn")
                || codename.equals("gvwifiue")))
                || (model != null && (model.equals("SAMSUNG-SM-T677A")
                || model.equals("SM-T670")
                || model.equals("SM-T677")
                || model.equals("SM-T677V")))) {
            return "Galaxy View";
        }
        if ((codename != null && codename.equals("manta"))) {
            return "Nexus 10";
        }
        // ----------------------------------------------------------------------------
        // Sony
        if ((codename != null && (codename.equals("D2104")
                || codename.equals("D2105")))
                || (model != null && (model.equals("D2104")
                || model.equals("D2105")))) {
            return "Xperia E1 dual";
        }
        if ((codename != null && (codename.equals("D2202")
                || codename.equals("D2203")
                || codename.equals("D2206")
                || codename.equals("D2243")))
                || (model != null && (model.equals("D2202")
                || model.equals("D2203")
                || model.equals("D2206")
                || model.equals("D2243")))) {
            return "Xperia E3";
        }
        if ((codename != null && (codename.equals("E5603")
                || codename.equals("E5606")
                || codename.equals("E5653")))
                || (model != null && (model.equals("E5603")
                || model.equals("E5606")
                || model.equals("E5653")))) {
            return "Xperia M5";
        }
        if ((codename != null && (codename.equals("E5633")
                || codename.equals("E5643")
                || codename.equals("E5663")))
                || (model != null && (model.equals("E5633")
                || model.equals("E5643")
                || model.equals("E5663")))) {
            return "Xperia M5 Dual";
        }
        if ((codename != null && codename.equals("LT26i"))
                || (model != null && model.equals("LT26i"))) {
            return "Xperia S";
        }
        if ((codename != null && (codename.equals("D5303")
                || codename.equals("D5306")
                || codename.equals("D5316")
                || codename.equals("D5316N")
                || codename.equals("D5322")))
                || (model != null && (model.equals("D5303")
                || model.equals("D5306")
                || model.equals("D5316")
                || model.equals("D5316N")
                || model.equals("D5322")))) {
            return "Xperia T2 Ultra";
        }
        if ((codename != null && (codename.equals("txs03")))
                || (model != null && (model.equals("SGPT12")
                || model.equals("SGPT13")))) {
            return "Xperia Tablet S";
        }
        if ((codename != null && (codename.equals("SGP311")
                || codename.equals("SGP312")
                || codename.equals("SGP321")
                || codename.equals("SGP351")))
                || (model != null && (model.equals("SGP311")
                || model.equals("SGP312")
                || model.equals("SGP321")
                || model.equals("SGP351")))) {
            return "Xperia Tablet Z";
        }
        if ((codename != null && (codename.equals("D6502")
                || codename.equals("D6503")
                || codename.equals("D6543")
                || codename.equals("SO-03F")))
                || (model != null && (model.equals("D6502")
                || model.equals("D6503")
                || model.equals("D6543")
                || model.equals("SO-03F")))) {
            return "Xperia Z2";
        }
        if ((codename != null && (codename.equals("401SO")
                || codename.equals("D6603")
                || codename.equals("D6616")
                || codename.equals("D6643")
                || codename.equals("D6646")
                || codename.equals("D6653")
                || codename.equals("SO-01G")
                || codename.equals("SOL26")
                || codename.equals("leo")))
                || (model != null && (model.equals("401SO")
                || model.equals("D6603")
                || model.equals("D6616")
                || model.equals("D6643")
                || model.equals("D6646")
                || model.equals("D6653")
                || model.equals("SO-01G")
                || model.equals("SOL26")))) {
            return "Xperia Z3";
        }
        if ((codename != null && (codename.equals("402SO")
                || codename.equals("SO-03G")
                || codename.equals("SOV31")))
                || (model != null && (model.equals("402SO")
                || model.equals("SO-03G")
                || model.equals("SOV31")))) {
            return "Xperia Z4";
        }
        if ((codename != null && (codename.equals("E5803")
                || codename.equals("E5823")
                || codename.equals("SO-02H")))
                || (model != null && (model.equals("E5803")
                || model.equals("E5823")
                || model.equals("SO-02H")))) {
            return "Xperia Z5 Compact";
        }
        // ----------------------------------------------------------------------------
        // Sony Ericsson
        if ((codename != null && (codename.equals("LT26i")
                || codename.equals("SO-02D")))
                || (model != null && (model.equals("LT26i")
                || model.equals("SO-02D")))) {
            return "Xperia S";
        }
        if ((codename != null && (codename.equals("SGP311")
                || codename.equals("SGP321")
                || codename.equals("SGP341")
                || codename.equals("SO-03E")))
                || (model != null && (model.equals("SGP311")
                || model.equals("SGP321")
                || model.equals("SGP341")
                || model.equals("SO-03E")))) {
            return "Xperia Tablet Z";
        }
        return fallback;
    }

    /**
     * Get the {@link DeviceInfo} for the current device. Do not run on the UI thread, as this may
     * download JSON to retrieve the {@link DeviceInfo}. JSON is only downloaded once and then
     * stored to {@link SharedPreferences}.
     *
     * @param context
     *     the application context.
     * @return {@link DeviceInfo} for the current device.
     */
    @WorkerThread
    public static DeviceInfo getDeviceInfo(Context context) {
        return getDeviceInfo(context.getApplicationContext(), Build.DEVICE, Build.MODEL);
    }

    /**
     * Get the {@link DeviceInfo} for the current device. Do not run on the UI thread, as this may
     * download JSON to retrieve the {@link DeviceInfo}. JSON is only downloaded once and then
     * stored to {@link SharedPreferences}.
     *
     * @param context
     *     the application context.
     * @param codename
     *     the codename of the device
     * @return {@link DeviceInfo} for the current device.
     */
    @WorkerThread
    public static DeviceInfo getDeviceInfo(Context context, String codename) {
        return getDeviceInfo(context, codename, null);
    }

    /**
     * Get the {@link DeviceInfo} for the current device. Do not run on the UI thread, as this may
     * download JSON to retrieve the {@link DeviceInfo}. JSON is only downloaded once and then
     * stored to {@link SharedPreferences}.
     *
     * @param context
     *     the application context.
     * @param codename
     *     the codename of the device
     * @param model
     *     the model of the device
     * @return {@link DeviceInfo} for the current device.
     */
    @WorkerThread
    public static DeviceInfo getDeviceInfo(Context context, String codename, String model) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String key = String.format("%s:%s", codename, model);
        String savedJson = prefs.getString(key, null);
        if (savedJson != null) {
            try {
                return new DeviceInfo(new JSONObject(savedJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // check if we have an internet connection
        int ret = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
        boolean isConnectedToNetwork = false;
        if (ret == PackageManager.PERMISSION_GRANTED) {
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission")
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                isConnectedToNetwork = true;
            }
        } else {
            // assume we are connected.
            isConnectedToNetwork = true;
        }

        if (isConnectedToNetwork) {
            try {
                // Get the device name from the generated JSON files created from Google's device list.
                String url = String.format(DEVICE_JSON_URL, codename.toLowerCase(Locale.ENGLISH));
                String jsonString = downloadJson(url);
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0, len = jsonArray.length(); i < len; i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    DeviceInfo info = new DeviceInfo(json);
                    if ((codename.equalsIgnoreCase(info.codename) && model == null)
                            || codename.equalsIgnoreCase(info.codename) && model.equalsIgnoreCase(info.model)) {
                        // Save to SharedPreferences so we don't need to make another request.
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(key, json.toString());
                        editor.apply();
                        return info;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (codename.equals(Build.DEVICE) && Build.MODEL.equals(model)) {
            return new DeviceInfo(Build.MANUFACTURER, getDeviceName(), codename, model); // current device
        }

        return new DeviceInfo(null, null, codename, model); // unknown device
    }

    /**
     * <p>Capitalizes getAllProcesses the whitespace separated words in a String. Only the first
     * letter of each word is changed.</p>
     *
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     *
     * @param str
     *     the String to capitalize
     * @return capitalized The capitalized String
     */
    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    /** Download URL to String */
    @WorkerThread
    private static String downloadJson(String myurl) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static final class Request {

        final Context context;
        final Handler handler;
        String codename;
        String model;

        private Request(Context ctx) {
            context = ctx;
            handler = new Handler(ctx.getMainLooper());
        }

        /**
         * Set the device codename to query. You should also set the model.
         *
         * @param codename
         *     the value of the system property "ro.product.device"
         * @return This Request object to allow for chaining of calls to set methods.
         * @see Build#DEVICE
         */
        public Request setCodename(String codename) {
            this.codename = codename;
            return this;
        }

        /**
         * Set the device model to query. You should also set the codename.
         *
         * @param model
         *     the value of the system property "ro.product.model"
         * @return This Request object to allow for chaining of calls to set methods.
         * @see Build#MODEL
         */
        public Request setModel(String model) {
            this.model = model;
            return this;
        }

        /**
         * Download information about the device. This saves the results in shared-preferences so
         * future requests will not need a network connection.
         *
         * @param callback
         *     the callback to retrieve the {@link DeviceName.DeviceInfo}
         */
        public void request(Callback callback) {
            if (codename == null && model == null) {
                codename = Build.DEVICE;
                model = Build.MODEL;
            }
            GetDeviceRunnable runnable = new GetDeviceRunnable(callback);
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(runnable).start();
            } else {
                runnable.run(); // already running in background thread.
            }
        }

        private final class GetDeviceRunnable implements Runnable {

            final Callback callback;
            DeviceInfo deviceInfo;
            Exception error;

            public GetDeviceRunnable(Callback callback) {
                this.callback = callback;
            }

            @Override public void run() {
                try {
                    deviceInfo = getDeviceInfo(context, codename, model);
                } catch (Exception e) {
                    error = e;
                }
                handler.post(new Runnable() {

                    @Override public void run() {
                        callback.onFinished(deviceInfo, error);
                    }
                });
            }
        }

    }

    /**
     * Callback which is invoked when the {@link DeviceName.DeviceInfo} is finished loading.
     */
    public interface Callback {

        /**
         * Callback to get the device info. This is run on the UI thread.
         *
         * @param info
         *     the requested {@link DeviceName.DeviceInfo}
         * @param error
         *     {@code null} if nothing went wrong.
         */
        void onFinished(DeviceInfo info, Exception error);
    }

    /**
     * Device information based on
     * <a href="https://support.google.com/googleplay/answer/1727131">Google's maintained list</a>.
     */
    public static final class DeviceInfo {

        /** Retail branding */
        public final String manufacturer;

        /** Marketing name */
        public final String marketName;

        /** the value of the system property "ro.product.device" */
        public final String codename;

        /** the value of the system property "ro.product.model" */
        public final String model;

        public DeviceInfo(String manufacturer, String marketName, String codename, String model) {
            this.manufacturer = manufacturer;
            this.marketName = marketName;
            this.codename = codename;
            this.model = model;
        }

        private DeviceInfo(JSONObject jsonObject) throws JSONException {
            manufacturer = jsonObject.getString("manufacturer");
            marketName = jsonObject.getString("market_name");
            codename = jsonObject.getString("codename");
            model = jsonObject.getString("model");
        }

        /**
         * @return the consumer friendly name of the device.
         */
        public String getName() {
            if (!TextUtils.isEmpty(marketName)) {
                return marketName;
            }
            return capitalize(model);
        }
    }

}
