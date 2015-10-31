package com.example.cse5324.newdiary2;

import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class MapsActivity extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    double latitude;
    double longitude;
    private static String API_KEY = "AIzaSyBVlkkszs7guzsQn2rWp-0WPofvIMSyz7I";

    public static MapsActivity newInstance() {
        MapsActivity fragment;
        fragment = new MapsActivity();
        return fragment;
    }

    public MapsActivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if (!isGooglePlayServicesAvailable()){
            getActivity().finish();
        }
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        setUpMapIfNeeded(); // For setting up the MapFragment

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        Button zoomIn = (Button)view.findViewById(R.id.Bzoomin);
        zoomIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onZoom(v);
            }
        });
        Button zoomOut = (Button)view.findViewById(R.id.Bzoomout);
        zoomOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onZoom(v);
            }
        });
        Button changeType = (Button)view.findViewById(R.id.Btype);
        changeType.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeType();
            }
        });
        Button search = (Button)view.findViewById(R.id.Bsearch);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSearch();
            }
        });
        return view;
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

           mMap = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
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
        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
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
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Current Location");
        mMap.addMarker(options);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mMap != null)
            setUpMap();
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap(); // getMap is deprecated
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();
        }
    }

    /**** The mapfragment's id must be removed from the FragmentManager
     **** or else if the same it is passed on the next time then
     **** app will crash ****/
    @Override
    public void onDestroyView() {

        if (mMap != null) {
            //android.support.v4.app.FragmentManager man = getChildFragmentManager();
            //Fragment frag = man.findFragmentById(R.id.map);
            //FragmentTransaction tra = man.beginTransaction();
            //tra.remove(frag);
            //tra.commit();
            //mMap = null;
        }
        super.onDestroyView();
    }

    public void onSearch()
    {
        EditText location_tf = (EditText)getActivity().findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;

        if(!location.equals(""))
        {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int x=0;
            if (addressList == null){
                return;
            }

            while (x<addressList.size()) {
                Address address = addressList.get(x);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getFeatureName()));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                x++;
            }

            performSearch(location);
        }
    }

    public void changeType()
    {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void onZoom(View view)
    {
        if(view.getId() == R.id.Bzoomin) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId() == R.id.Bzoomout)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Current Location");
        mMap.addMarker(options);
    }

    public void performSearch(String searchString) {
        String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
        urlString += latitude;
        urlString += ",";
        urlString += longitude;
        urlString += "&radius=500&name=";
        urlString += searchString;
        urlString += "&key=";
        urlString += API_KEY;
        new FindPlacesTask().execute(urlString);
    }

    private void updateMap(String results){
        try {
            JSONObject resultObj = new JSONObject(results);
            JSONArray array = resultObj.getJSONArray("results");
            int x = 0;
            JSONObject obj = array.getJSONObject(x);
            while (obj != null){
                String name = obj.getString("name");
                JSONObject geometry = obj.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                String lat = location.getString("lat");
                double latitude = Double.parseDouble(lat);
                String lng = location.getString("lng");
                double longitude = Double.parseDouble(lng);
                LatLng latLng = new LatLng(latitude, longitude);

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(name);
                mMap.addMarker(options);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                x++;
                obj = array.getJSONObject(x);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class FindPlacesTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {

            String urlString = urls[0];
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urlString);
            InputStream in = null;
            BufferedReader reader = null;
            String text = "";
            BasicHttpResponse httpResponse;

            try {
                //URL url = new URL(urls[0]);
                httpResponse = (BasicHttpResponse)httpclient.execute(httpget);
                in = httpResponse.getEntity().getContent();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder e = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null) {
                    e.append(line);
                    e.append("\n");
                }

                text = e.toString();
                System.out.println(text);
            } catch (Exception var20) {
                var20.getMessage();
            } finally {
                try {
                    if(in != null) {
                        in.close();
                    }

                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException var19) {
                    var19.getMessage();
                }

            }
            return text;
        }

        protected void onPostExecute(String feed) {
            updateMap(feed);
        }
    }

}
