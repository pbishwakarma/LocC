package disruptdc.locc.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import disruptdc.locc.R;
import disruptdc.locc.components.DataStorage;

import static disruptdc.locc.R.id.addFriendsButton;
import static disruptdc.locc.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker otherUserLocation;
    Marker otherUserLocation1;
    Marker otherUserLocation2;
    Marker otherUserLocation3;
    Marker otherUserLocation4;
    private LatLng latLngVariable;
    private Marker markerVariable;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        geocoder = new Geocoder(this, Locale.getDefault());
        mapFragment.getMapAsync(this);
        GEOFENCE_RADIUS = DataStorage.radius;

        ImageButton friendsButton = (ImageButton) findViewById(R.id.addFriendsButton);

        if (DataStorage.pinDroppable || DataStorage.leader != null) {
            friendsButton.setVisibility(View.GONE);
        }

        // Button to navigate to menu to add friends to LocC
        ImageButton iconButton = (ImageButton) findViewById(R.id.addFriendsButton);

        iconButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, FriendsActivity.class));
            }

        });

        /*//Button to navigate to menu to set center
        ImageButton setCenterButton = (ImageButton) findViewById(R.id.pinButton);

        setCenterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MapsActivity.this, SettingsActivity.class));
            }
        });*/


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }
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
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        //This is really good
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    private static float GEOFENCE_RADIUS = DataStorage.radius; // in meters


    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;
    private void drawGeoFence() {

        if ( geoFenceLimits != null )
            geoFenceLimits.remove();
        if(DataStorage.pinDroppable == false) {
            if (DataStorage.shouldDraw == true) {
                CircleOptions circleOptions = new CircleOptions()
                        .center(mCurrLocationMarker.getPosition())
                        .strokeColor(Color.argb(50, 70, 70, 70))
                        .fillColor(Color.argb(100, 150, 150, 150))
                        .radius(GEOFENCE_RADIUS);
                geoFenceLimits = mMap.addCircle(circleOptions);
            }
        }
        else{
            CircleOptions circleOptions = new CircleOptions()
                    .center( markerVariable.getPosition())
                    .strokeColor(Color.argb(50, 70,70,70))
                    .fillColor( Color.argb(100, 150,150,150) )
                    .radius( GEOFENCE_RADIUS );
            geoFenceLimits = mMap.addCircle( circleOptions );
        }

    }


    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Sid Anche");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        markerOptions.alpha(0.6f);
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        if (DataStorage.pinDroppable == false){
            drawGeoFence();
        }

        for(int i = 0; i < DataStorage.friends.size(); i++){
            if (DataStorage.friends.get(i).getChecked()) {
                double lat = DataStorage.friends.get(i).getLatitude();
                double lng = DataStorage.friends.get(i).getLongitude();
                LatLng latLng1 = new LatLng(lat, lng);
                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(latLng1);
                markerOptions1.title(DataStorage.friends.get(i).getName());
                markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                markerOptions1.alpha(0.6f);
                otherUserLocation = mMap.addMarker(markerOptions1);
            }
        }
        /*
        if(DataStorage.BenBoolean == true){
            LatLng latLng1 = new LatLng(38.905361,-77.03797 );
            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position(latLng1);
            markerOptions1.title("Ben Dykstra");
            markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            markerOptions1.alpha(0.6f);
            otherUserLocation = mMap.addMarker(markerOptions1);
        }

        if(DataStorage.SallyBoolean == true){
            LatLng latLng2 = new LatLng(38.9035410,-77.033987 );
            MarkerOptions markerOptions2 = new MarkerOptions();
            markerOptions2.position(latLng2);
            markerOptions2.title("Sally");
            markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            markerOptions2.alpha(0.6f);
            otherUserLocation1 = mMap.addMarker(markerOptions2);
        }

        if(DataStorage.TimmyBoolean == true) {
            LatLng latLng3 = new LatLng(38.906346354, -77.035177946);
            MarkerOptions markerOptions3 = new MarkerOptions();
            markerOptions3.position(latLng3);
            markerOptions3.title("Timmy");
            markerOptions3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            markerOptions3.alpha(0.6f);
            otherUserLocation2 = mMap.addMarker(markerOptions3);
        }

        if(DataStorage.PrajalBoolean == true) {
            LatLng latLng4 = new LatLng(38.90729812, -77.03649759);
            MarkerOptions markerOptions4 = new MarkerOptions();
            markerOptions4.position(latLng4);
            markerOptions4.title("Prajal");
            markerOptions4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            markerOptions4.alpha(0.6f);
            otherUserLocation3 = mMap.addMarker(markerOptions4);
        }

        if(DataStorage.TurnerBoolean == true) {
            LatLng latLng5 = new LatLng(38.9080662064, -77.009224891);
            MarkerOptions markerOptions5 = new MarkerOptions();
            markerOptions5.position(latLng5);
            markerOptions5.title("Turner");
            markerOptions5.icon(BitmapDescriptorFactory.fromResource(R.drawable.turnerpic));
            markerOptions5.alpha(0.9f);
            otherUserLocation4 = mMap.addMarker(markerOptions5);
        }
*/
        if(DataStorage.pinDroppable == true)
        {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
            {

                @Override
                public void onMapClick(LatLng point) {
                    //save current location
                    latLngVariable = point;

                    List<android.location.Address> addresses = new ArrayList<>();

                    try {
                        addresses = geocoder.getFromLocation(point.latitude, point.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    android.location.Address address = addresses.get(0);

                    if (address != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                            sb.append(address.getAddressLine(i) + "\n");
                        }
                        Toast.makeText(MapsActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                    }

                    //remove previously placed Marker
                    if (markerVariable != null) {
                        markerVariable.remove();
                    }

                    //place marker where user just clicked
                    markerVariable = mMap.addMarker(new MarkerOptions().position(point).title("LocCenter")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    drawGeoFence();
                }
            });

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

}
