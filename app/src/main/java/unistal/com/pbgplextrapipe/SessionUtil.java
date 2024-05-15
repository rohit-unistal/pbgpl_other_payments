package unistal.com.pbgplextrapipe;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionUtil {
    private static final String USER_SESSION_PREF = "userSessionPrefHPCL";
    public static final String USER_ID = "userID";
    public static final String USER_TYPE_ID = "userTypeID";
    private static final String USER_NAME = "userName";
    private static final String DEVICE_ID = "deviceId";
    private static final String BP_NUMBER = "bpNumber";
    private static final String METER_NUMBER = "meterNumber";
    private static final String MODULES = "modules";

    public static SharedPreferences getUserSessionPreferences(Context context) {
        return context.getSharedPreferences(USER_SESSION_PREF, Context.MODE_PRIVATE);
    }

    public static void removeUserDetails(Context context) {
        final SharedPreferences prefs = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
       /* editor.remove(USER_ID);
        editor.remove(USER_TYPE_ID);
        editor.remove(USER_NAME);
        editor.remove(BP_NUMBER);
        editor.remove(METER_NUMBER);*/
       editor.clear();
       editor.commit();
    }

    public static void saveUserId(String userId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(USER_ID, "");
    }


    public static void saveGA(String userId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("GA", userId);
        editor.commit();
    }

    public static String getGA(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString("GA", "");
    }



    public static void saveUserTypeId(String userId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TYPE_ID, userId);
        editor.commit();
    }

    public static String getUserTypeId(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(USER_TYPE_ID, "");
    }

    public static void saveUserName(String userName, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(USER_NAME, "");
    }


    public static void saveDeviceId(String deviceId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }

    public static String getDeviceId(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(DEVICE_ID, "");
    }
    public static void saveBPNumber(String bpNumber, Context context)
    {
        SharedPreferences sharedPreferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BP_NUMBER,bpNumber);
        editor.commit();
    }

    public static String getBpNumber(Context context)
    {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(BP_NUMBER,"");
    }
    public static String getMeterNumber(Context context)
    {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(METER_NUMBER, "");

    }
    public static void saveMeterNumber(String meterNumber, Context context)
    {
        SharedPreferences sharedPreferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(METER_NUMBER,meterNumber);
        editor.commit();
    }
    public static String getModules(Context context)
    {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(MODULES, "");

    }
    public static void saveModules(String modules, Context context)
    {
        SharedPreferences sharedPreferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MODULES,modules);
        editor.commit();
    }
}
