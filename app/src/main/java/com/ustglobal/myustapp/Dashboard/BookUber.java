package com.ustglobal.myustapp.Dashboard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ustglobal.myustapp.R;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.core.auth.AuthenticationError;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivity;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.android.rides.RideRequestViewError;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Created by Shubham Singhal.
 */

public class BookUber extends AppCompatActivity implements RideRequestButtonCallback, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private View rootView;
    private Toolbar toolbar;
    private TextView txt_where;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker, mDestinationMarker;
    private LocationRequest mLocationRequest;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private ArrayList markerPoints = new ArrayList();
    private LatLng latLng_cuurent;
    private Activity act;

    private static final String ERROR_LOG_TAG = "UberSDK-SampleActivity";
    private String PICKUP_ADDR, PICKUP_NICK;
    private static final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";
    private static final int WIDGET_REQUEST_CODE = 1234;

    private static final String CLIENT_ID = "DrRsU4YHwlu241s6JK85nq7kPRbdl-Q-";
    private static final String REDIRECT_URI = "https://www.google.com";
    private static final String SERVER_TOKEN = "cOYHoj9cvDgwoQ2VUHFIqHabNr5hV2uJyLcOh3id";
    //
    private RideRequestButton blackButton;
    private SessionConfiguration configuration;
    private ServerTokenSession session;
    private AlertDialog alertDNew;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_book_uber);

        init();
        setDimension();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        txt_where = (TextView) findViewById(R.id.txt_where);
        blackButton = (RideRequestButton) findViewById(R.id.uber_button_black);


        txt_where.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(BookUber.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        configuration = new SessionConfiguration.Builder()
                .setRedirectUri(REDIRECT_URI)
                .setClientId(CLIENT_ID)
                .setServerToken(SERVER_TOKEN)
                .build();
        UberSdk.initialize(configuration);
//        //   validateConfiguration(configuration);
        session = new ServerTokenSession(configuration);
//


//        RideParameters rideParametersCheapestProduct = new RideParameters.Builder()
//                .setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
//                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
//                .build();
//
//        // This button demonstrates launching the RideRequestActivity (customized button behavior).
//        // You can optionally setRideParameters for pre-filled pickup and dropoff locations.
//        RideRequestButton uberButtonWhite = (RideRequestButton) rootView.findViewById(R.id.uber_button_white);
//        RideRequestActivityBehavior rideRequestActivityBehavior = new RideRequestActivityBehavior(this, WIDGET_REQUEST_CODE, configuration);
//        uberButtonWhite.setRequestBehavior(rideRequestActivityBehavior);
//        uberButtonWhite.setRideParameters(rideParametersForProduct);
//        uberButtonWhite.setSession(session);
//        uberButtonWhite.loadRideInformation();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Book An Uber");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setDimension() {

    }

    @Override
    public void onRideInformationLoaded() {
        Toast.makeText(this, "Estimates have been refreshed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(ApiError apiError) {
        Toast.makeText(this, apiError.getClientErrors().get(0).getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Throwable throwable) {
        Log.e("SampleActivity", "Error obtaining Metadata", throwable);
        Toast.makeText(this, "Connection error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WIDGET_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED && data != null) {
            if (data.getSerializableExtra(RideRequestActivity.AUTHENTICATION_ERROR) != null) {
                AuthenticationError error = (AuthenticationError) data.getSerializableExtra(RideRequestActivity
                        .AUTHENTICATION_ERROR);
                Toast.makeText(this, "Auth error " + error.name(), Toast.LENGTH_SHORT).show();
                Log.d(ERROR_LOG_TAG, "Error occurred during authentication: " + error.toString
                        ().toLowerCase());
            } else if (data.getSerializableExtra(RideRequestActivity.RIDE_REQUEST_ERROR) != null) {
                RideRequestViewError error = (RideRequestViewError) data.getSerializableExtra(RideRequestActivity
                        .RIDE_REQUEST_ERROR);
                Toast.makeText(this, "RideRequest error " + error.name(), Toast.LENGTH_SHORT).show();
                Log.d(ERROR_LOG_TAG, "Error occurred in the Ride Request Widget: " + error.toString().toLowerCase());
            }
        }


        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == this.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng_cuurent);
                    markerOptions.title("Current Position");
                    markerPoints.add(latLng_cuurent);
                    //   markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);

                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_cuurent));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(2));
                }
                setmDestinationMarker(place.getLatLng(), place);
                txt_where.setText(place.getName());
                Log.i("PLACE", "DATA: " + place.getLatLng());
                Log.i("PLACE", "Place: " + place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("PLACE", status.getStatusMessage());

            } else if (resultCode == this.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


//    private void validateConfiguration(SessionConfiguration configuration) {
//        String nullError = "%s must not be null";
//        String sampleError = "Please update your %s in the gradle.properties of the project before " +
//                "using the Uber SDK Sample app. For a more secure storage location, " +
//                "please investigate storing in your user img_home gradle.properties ";
//
//        checkNotNull(configuration, String.format(nullError, "SessionConfiguration"));
//        checkNotNull(configuration.getClientId(), String.format(nullError, "Client ID"));
//        checkNotNull(configuration.getRedirectUri(), String.format(nullError, "Redirect URI"));
//        checkNotNull(configuration.getServerToken(), String.format(nullError, "Server Token"));
//
//        Log.e("*********VALUE********",configuration.getClientId().toString());
//
//        checkState(!configuration.getClientId().equals("DrRsU4YHwlu241s6JK85nq7kPRbdl-Q-"), String.format(sampleError,"Client id"));
//
//        checkState(!configuration.getRedirectUri().equals("https://www.google.com"), String.format(sampleError, "Redirect URI"));
//
//        checkState(!configuration.getRedirectUri().equals("cOYHoj9cvDgwoQ2VUHFIqHabNr5hV2uJyLcOh3id"), String.format(sampleError, "Server Token"));
//    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            latLng_cuurent = new LatLng(location.getLatitude(), location.getLongitude());
            if (mLastLocation != null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PICKUP_ADDR = addresses.get(0).getAddressLine(1);
                PICKUP_NICK = addresses.get(0).getFeatureName();
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng_cuurent);
            markerOptions.title("Current Position");
            markerPoints.add(latLng_cuurent);
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_cuurent));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            //   mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            //  buildGoogleApiClient();

            //Initialize Google Play Services
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                } else {
                    checkLocationPermission();
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private void setmDestinationMarker(LatLng latlng, Place place) {
        try {
            markerPoints.add(latlng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlng);
            markerOptions.title("Current Position");
            //   markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.));
            mDestinationMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

            if (markerPoints.size() >= 2) {
                LatLng origin = (LatLng) markerPoints.get(0);
                LatLng dest = (LatLng) markerPoints.get(1);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }

            if (markerPoints.size() >= 2) {
                LatLng origin = (LatLng) markerPoints.get(0);
                LatLng dest = (LatLng) markerPoints.get(1);

                RideParameters rideParametersForProduct = new RideParameters.Builder()
                        .setPickupLocation(origin.latitude, origin.longitude, PICKUP_NICK, PICKUP_ADDR)
                        .setDropoffLocation(dest.latitude, dest.longitude, place.getName().toString(), place.getAddress().toString())
                        .build();
                RideRequestActivityBehavior rideRequestActivityBehavior = new RideRequestActivityBehavior(this, WIDGET_REQUEST_CODE, configuration);

                // This button demonstrates deep-linking to the Uber app (default button behavior).
                blackButton.setRequestBehavior(rideRequestActivityBehavior);
                blackButton.setRideParameters(rideParametersForProduct);
                blackButton.setSession(session);
                blackButton.setCallback(this);
                blackButton.loadRideInformation();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                ParserTask parserTask = new ParserTask();
                parserTask.execute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            try {

                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList();
                    lineOptions = new PolylineOptions();

                    List<HashMap<String, String>> path = result.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(Color.BLUE);
                    lineOptions.geodesic(true);
// Drawing polyline in the Google Map for the i-th route
                    mMap.addPolyline(lineOptions);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        alertDNew = builder.create();
        alertDNew.show();
    }
}
