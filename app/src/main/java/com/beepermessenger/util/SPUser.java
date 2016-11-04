package com.beepermessenger.util;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.content.Context;

public class SPUser {
	private static final String SP = "user";

	public static final String PHONE = "phone";
	public static final String COUNTRY = "country";
	public static final String STATE = "state";
	public static final String USER_ID = "user_id";
	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String ABOUT = "about";
	public static final String GENDER = "gender";
	public static final String D_O_B = "dob";
	public static final String PASSWORD = "password";
	public static final String PROFILE_IMAGE = "profile_image";

	public static String getString(Context context, String key) {

		return context.getSharedPreferences(SP, Context.MODE_PRIVATE)
				.getString(key, "");
	}

	public static void setString(Context context, String key, String value) {
		if(value.trim().length()>0&&!value.equalsIgnoreCase("null")) {
			context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
					.putString(key, value).commit();
		}
	}

	public static boolean getBooleanValue(Context context, String key) {

		return context.getSharedPreferences(SP, Context.MODE_PRIVATE)
				.getBoolean(key, false);
	}

	public static void setBooleanValue(Context context, String key,
			boolean value) {

		context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
				.putBoolean(key, value).commit();
	}

	public static long getLong(Context context, String key) {

		return context.getSharedPreferences(SP, Context.MODE_PRIVATE).getLong(
				key, 0);
	}

	public static void setLong(Context context, String key, long value) {

		context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
				.putLong(key, value).commit();
	}

	public static int getInt(Context context, String key) {

		return context.getSharedPreferences(SP, Context.MODE_PRIVATE).getInt(
				key, 0);
	}

	public static void setInt(Context context, String key, int value) {

		context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
				.putInt(key, value).commit();
	}
	public static void clear(Context context) {
		context.getSharedPreferences(SP, Context.MODE_PRIVATE).edit()
				.clear().commit();
	}
}