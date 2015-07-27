package com.example.fidelbarcaya.easyparking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import com.example.fidelbarcaya.easyparking.model.Localization;
import com.example.fidelbarcaya.easyparking.model.Parking;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * Created by fidel.barcaya on 3/17/2015.
 */
public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static boolean isInternetAvailable(final Context context) {
//      boolean returnTemp = true;

        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conManager.getActiveNetworkInfo();
        if ((i == null) || (!i.isConnected()) || (!i.isAvailable())) {

            return false;
        }
        return true;
    }
    public static  void showDialogForInternetConection(Context mContext){

        AlertDialog.Builder netNotAvailable=new AlertDialog.Builder(mContext);
        netNotAvailable.setMessage("Could not find an Internet connection. Please check your settings and try again.");
        netNotAvailable.setTitle("Attention");
        netNotAvailable.setIcon(R.drawable.ic_car);


        netNotAvailable.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            //  @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        netNotAvailable.show();
    }

    public static String getJsonStringFromNetwork() {
        Log.d(LOG_TAG, "Starting network connection");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            final String PARKING_BASE_URL = "http://easyparking.azurewebsites.net/";
            final String API_PATH = "api";
            final String PARKING = "Parking";

            Uri builtUri = Uri.parse(PARKING_BASE_URL).buildUpon()
                    .appendPath(API_PATH)
                    .appendPath(PARKING)
                    .build();
            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null)
                return "";
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0)
                return "";

            return buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                    e.printStackTrace();
                }
            }
        }

        return "";
    }
    public static Parking[] parseParkingJson(String fixtureJson) throws JSONException {
        JSONArray jsonArray = new JSONArray(fixtureJson);
        ArrayList<Parking> result = new ArrayList<Parking>();
        final String LOCALIZILATION = "Local";
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject matchObject  = jsonArray.getJSONObject(i);
            String parkingName = matchObject.getString("ParkingName");
            String openingTime = matchObject.getString("OpeningTime");
            String closingTime = matchObject.getString("ClosingTime");
            double rate = Double.parseDouble(matchObject.getString("Rate"));
            int carSpacesTotal = Integer.parseInt(matchObject.getString("CarSpacesTotal"));
            int motorBikeSpacesTotal = Integer.parseInt(matchObject.getString("MotorBikeSpacesTotal"));
            int carSpacesAvailable = Integer.parseInt(matchObject.getString("CarSpacesAvailable"));
            int motorBikeSpacesAvailable = Integer.parseInt(matchObject.getString("MotorBikeSpacesAvailable"));
            double latitude = Double.parseDouble(matchObject.getJSONObject(LOCALIZILATION).getString("Latitude"));
            double longitude = Double.parseDouble(matchObject.getJSONObject(LOCALIZILATION).getString("Longitude"));

            Localization localization = new Localization();
            localization.setLatitude(latitude);
            localization.setLongitude(longitude);

            Parking parking = new Parking();
            parking.setParkingName(parkingName);
            parking.setOpeningTime(openingTime);
            parking.setClosingTime(closingTime);
            parking.setRate(rate);
            parking.setCarSpacesTotal(carSpacesTotal);
            parking.setMotorBikeSpacesTotal(motorBikeSpacesTotal);
            parking.setCarSpacesAvailable(carSpacesAvailable);
            parking.setMotorBikeSpacesAvailable(motorBikeSpacesAvailable);
            parking.setLocal(localization);
            result.add(parking);
        }
        return result.toArray(new Parking[result.size()]);
    }
}
