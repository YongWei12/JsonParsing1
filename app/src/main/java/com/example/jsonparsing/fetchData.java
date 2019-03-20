package com.example.jsonparsing;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<Void, Void, Void> {
    String data="";
    String dataParsed = "";
    String singleParsed = "";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://geo.data.gov.sg/dengue-cluster/2019/03/15/geojson/dengue-cluster.geojson");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line ="";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject jsonObj = new JSONObject(data);
            JSONArray features = jsonObj.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties= feature.getJSONObject("properties");
                singleParsed = "Description: " + properties.get("Description") + "\n";
                dataParsed = dataParsed + singleParsed;

            }


            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

        @Override
        protected void onPostExecute (Void aVoid){
            super.onPostExecute(aVoid);
            MainActivity.data.setText(this.dataParsed);
        }
    }

