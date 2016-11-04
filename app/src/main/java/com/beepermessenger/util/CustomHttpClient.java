package com.beepermessenger.util;

/**
 * Class : CustomHttpClient
 * Task : This class is design for all server side request and response.
 * Author: playstore.apps.android@gmail.com
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CustomHttpClient {
    private Context context;

    public CustomHttpClient(Context context) {
        this.context = context;
    }

    static ProgressDialog progress;

    public void executeHttpPost(String strUrl, HashMap<String, String> postParameters, final ProgressDialog progress, final OnSuccess success, final OnFailure failure) {
        try {

            if (NetworkConnection.getInstance(context).isOnline(context)) {
                if (progress != null) {
                    progress.show();
                }
                final String url =strUrl=strUrl + getQuery(postParameters);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (progress != null) {
                                    progress.cancel();
                                }

                                if (response != null) {
                                    System.out.println("From " + url + " Response: - "
                                            + response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.optBoolean("status")) {
                                            if (jsonObject.optString("message").trim().length() > 0) {
                                                Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                            }
                                            if (success != null) {
                                                success.onSucess(response);
                                            }
                                        } else {
                                            Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                            if (failure != null) {
                                                failure.onFailure(response);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                        if (failure != null) {
                                            failure.onFailure("{\"status\":false,\"message\":\"" + e.getMessage() + "\"}");
                                        }
                                    }

                                } else {
                                    Toast.makeText(context, "Network Error.", Toast.LENGTH_LONG).show();
                                    if (failure != null) {
                                        failure.onFailure("{\"status\":false,\"message\":\"Network Error.\"}");
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (progress != null) {
                                    progress.cancel();
                                }
                                if (failure != null) {
                                    failure.onFailure("{\"status\":false,\"message\":\"" + error.getMessage() + "\"}");
                                }
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                request.setShouldCache(false);
                Volley.newRequestQueue(context).add(request);

            } else {
                if (failure != null) {
                    failure.onFailure("{\"status\":false,\"message\":\"Please Provide Internet.\"}");
                }
                Toast.makeText(context, "Please Provide Internet.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (failure != null) {
                failure.onFailure("{\"status\":false,\"message\":\"" + e.getMessage() + "\"}");
            }
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private static String getQuery(HashMap<String, String> params)
            throws UnsupportedEncodingException {
        String result = "";

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result + "&";
            result = result + entry.getKey();
            result = result + "=";
            result = result + (URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result;
    }

    public interface OnSuccess {
        void onSucess(String result);
    }

    public interface OnFailure {
        void onFailure(String result);
    }
}
