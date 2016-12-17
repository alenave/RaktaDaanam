package com.hack.blackhawk.raktadaanam.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {

    public static JSONObject post(JSONObject object){
        JSONObject requestBody = new JSONObject();
        JSONObject agrsBody = new JSONObject();
        try {
            agrsBody.put("table", "People");
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(object);
            agrsBody.put("objects", jsonArray);
            requestBody.put("type", "insert");
            requestBody.put("args", agrsBody);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.d("REQUEST BODY", requestBody.toString());
        Log.d("Came here", "Yeah");
        System.out.print("I came here");

        int httpResult = 0;
        HttpURLConnection con = null;
        JSONObject jo = null;
        try {
            URL url = new URL("https://data.cassette88.hasura-app.io/v1/query");
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            //con.setConnectTimeout(5000);
            //con.setReadTimeout(30000);
            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(object.toString());
            wr.flush();
            httpResult = con.getResponseCode();
            Log.d("Result XXX", String.valueOf(httpResult));
            String msg = "";
            Object json = null;
            if (httpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder resp = new StringBuilder();
                String respline = "";
                while ((respline = in.readLine()) != null) {
                    resp.append(respline);
                }
                msg = resp.toString();
                in.close();
                //System.out.println("Response: "+msg);
                json = new JSONTokener(msg).nextValue();
                jo = (JSONObject) json;
                if (!jo.has("success")) {
                    jo.put("success", true);
                }
            }
        }catch(Exception e) {
            Log.d("Exception caught" , e.toString());
        }
        return jo;
    }

}
