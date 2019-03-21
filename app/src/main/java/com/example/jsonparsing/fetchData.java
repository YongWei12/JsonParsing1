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
    String substr = "";
    String singleParsed = "";
    String dataParsed ="";

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
                singleParsed = "Description"+ properties.get("Description") + "\n";
                substr = singleParsed.substring(singleParsed.indexOf("CASE_SIZE")+19, singleParsed.indexOf("CASE_SIZE")+22)+"\n";
                // the below part is for the removal of all < and / characters in our case size
                int length = substr.length();
                int a, b, count = 0;
                char []substr1 =substr.toCharArray();
                for( a =  b =0; a < length; a++){
                    if ((substr1[a] != '/' )&& (substr1[a] != '<')) {
                        substr1[b++] = substr1[a];
                    }  else {
                        count++;
                    }

                }
                while(count>0){
                    substr1[b++] ='\0';
                    count--;
                }

                String substr2 = String.copyValueOf(substr1);
                dataParsed = dataParsed+ substr2;



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

