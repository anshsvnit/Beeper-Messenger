package com.beepermessenger.CommonFiles;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import com.beepermessenger.model.CountryDTO;
import com.beepermessenger.model.StateDTO;
import com.beepermessenger.model.UserMessageDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shree on 2/21/2016.
 */
public class Parser {



    public static ArrayList<CountryDTO> parsingCountry(String result) {
        ArrayList<CountryDTO> alCountry = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            alCountry = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<CountryDTO>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alCountry;
    }
    public static ArrayList<StateDTO> parsingState(String result) {
        ArrayList<StateDTO> alState = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            alState = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<StateDTO>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alState;
    }
    public static ArrayList<UserMessageDTO> parsingUserMessage(String result) {
        ArrayList<UserMessageDTO> alData = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.optJSONArray("data");
            alData = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<UserMessageDTO>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alData;
    }
}