package com.example.ticketbot;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetReplyTask extends AsyncTask<String, Void, String> {
    private static final String WIT_AI_API_URL = "https://api.wit.ai/message?v=20210608&q=";
    private static final String WIT_AI_ACCESS_TOKEN = "4PSUKNQF4TFDXVK4V3BSEJNG33EHUMAC";

    private AsyncResponse delegate;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public GetReplyTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        String userMessage = params[0];
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(WIT_AI_API_URL + userMessage);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + WIT_AI_ACCESS_TOKEN);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
