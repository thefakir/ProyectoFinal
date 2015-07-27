package com.example.fidelbarcaya.easyparking;

import android.content.Context;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fidelbarcaya.easyparking.model.Localization;
import com.example.fidelbarcaya.easyparking.model.Parking;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private HashMap<Integer, Parking> markerParking = new HashMap<Integer, Parking>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);




                 LinearLayout r = (LinearLayout) findViewById(R.id.info);
                 r.setVisibility(View.INVISIBLE);
                 setUpMapIfNeeded();
                 mGoogleApiClient = new GoogleApiClient.Builder(this)
                         .addConnectionCallbacks(this)
                         .addOnConnectionFailedListener(this)
                         .addApi(LocationServices.API)
                         .build();


                 // Create the LocationRequest object
                 mLocationRequest = LocationRequest.create()
                         .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                         .setInterval(10 * 5000)        // 10 seconds, in milliseconds
                         .setFastestInterval(1 * 1000); // 1 second, in milliseconds
             }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if(!Utility.isInternetAvailable(this))
        { LinearLayout r = (LinearLayout) findViewById(R.id.info);
            r.setVisibility(View.INVISIBLE);
            Utility.showDialogForInternetConection(this);
        }else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
            }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if(control())
        {
            Log.d(TAG, "it is enabled");
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
        GetResultTask task = new GetResultTask();
        task.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {


        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(options);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        mMap.setMyLocationEnabled(true);
        // mMap.getUiSettings().setMyLocationButtonEnabled(true);


    }
    public  void hide(View v){
        //Load animation
        Animation slide_down     = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        LinearLayout r = (LinearLayout)findViewById(R.id.info);
        r.startAnimation(slide_down);
        r.setVisibility(View.INVISIBLE);
    }
    public  void show(View v){
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        LinearLayout r = (LinearLayout)findViewById(R.id.info);
        r.startAnimation(slide_up);
        r.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public  boolean control() {

        LocationManager locManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //GPS enabled
            return true;
        } else {
            //GPS disabled
            return false;
        }
    }

    public  void addMarkers(Parking parking )
    {
        Localization location = parking.getLocal();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float colorMarker = BitmapDescriptorFactory.HUE_YELLOW;

        // x = (disponible_actual * 100) / total_espacios
        double carSpacesAvailablePercent = (parking.getCarSpacesAvailable() * 100) / parking.getCarSpacesTotal();
        if(carSpacesAvailablePercent == 0)
           colorMarker = BitmapDescriptorFactory.HUE_RED;
        else{
            if(carSpacesAvailablePercent > 0 &&  carSpacesAvailablePercent <= 50)
                colorMarker = BitmapDescriptorFactory.HUE_ORANGE;
            else
                colorMarker = BitmapDescriptorFactory.HUE_GREEN;
        }

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(parking.getParkingName())
                .icon(BitmapDescriptorFactory.defaultMarker(colorMarker));
       Marker marker =  mMap.addMarker(options);
        markerParking.put(marker.hashCode(), parking);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_up);
                LinearLayout r = (LinearLayout) findViewById(R.id.info);
                int hashCode = arg0.hashCode();
                Parking parking = markerParking.get(hashCode);
                if (parking != null)
                {

                    TextView parkingNameView = (TextView) r.findViewById(R.id.slide_text);
                TextView spacesCarView = (TextView) r.findViewById(R.id.spacesCarView);
                TextView spacesMotoView = (TextView) r.findViewById(R.id.spacesMotoView);
                TextView tarifaView = (TextView) r.findViewById(R.id.rateView);


                String remainingSpacesCar = parking.getCarSpacesAvailable() + "/" + parking.getCarSpacesTotal();
                String remainingSpacesMoto = parking.getMotorBikeSpacesAvailable() + "/" + parking.getMotorBikeSpacesTotal();
                String rate = "Tarifa: " + parking.getRate() + "Bs";

                parkingNameView.setText(parking.getParkingName());
                spacesCarView.setText(remainingSpacesCar);
                spacesMotoView.setText(remainingSpacesMoto);
                tarifaView.setText(rate);
                r.startAnimation(slide_up);
                r.setVisibility(View.VISIBLE);
                // Toast.makeText(MainActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
            }
                return true;
            }

        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                //Load animation
                Animation slide_down     = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_down);
                LinearLayout r = (LinearLayout)findViewById(R.id.info);
               if(r.getVisibility() == View.VISIBLE) {
                   r.startAnimation(slide_down);
                   r.setVisibility(View.INVISIBLE);
               }

            }
        });
    }

    class GetResultTask extends AsyncTask<Void,Void,Parking[]> {


        @Override
        protected Parking[] doInBackground(Void... params) {

            String resultString = Utility.getJsonStringFromNetwork();
            Log.v(TAG, resultString);

            try {
                Parking[] res = Utility.parseParkingJson(resultString);
                return res;

            }catch (JSONException e)
            {
                Log.v(TAG,"Error parsing" + e.getMessage(), e);
                e.printStackTrace();
                return new Parking[] {new Parking()};
            }

        }
        @Override
        protected void onPostExecute(Parking[] parkings) {
            // arrayAdapter.clear();
            for(Parking parking : parkings)
            {
                addMarkers(parking);

            }
        }

    }
}