package com.lalthanpuiachhangte.mapforfish;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.BoringLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;
import com.lalthanpuiachhangte.mapforfish.fragments.ItemListDialogFragment;
import com.lalthanpuiachhangte.mapforfish.registration.LoginActivity;
import com.lalthanpuiachhangte.mapforfish.registration.RegistrationActivity;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,ItemListDialogFragment.Listener,LocationListener {

    private String mURL = String.valueOf(R.string.URL_PATH)+"api/getAll";

    private GoogleMap mMap;
    private static List<LakeEntity> mLakes;
    private MaterialSearchBar materialSearchBar;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    private String login="dd";

    public  Button uploadLakeButton, registrationButton, loginButton;
    public FloatingActionButton logoutButton;
    SharedPreferences sharedPreferences;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        materialSearchBar = findViewById(R.id.searchBar);
        uploadLakeButton = findViewById(R.id.uploadLakeButton);
        registrationButton = findViewById(R.id.registionButton);
        loginButton = findViewById(R.id.loginButton);
        logoutButton = findViewById(R.id.logoutButton);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        drawerLayout.openDrawer(Gravity.LEFT);
        sharedPreferences = this.getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        //CHECK THE LOGIN STATUS
        String login_status = sharedPreferences.getString("login_status","");
        if("login".equals(login_status)){
            //ALREADY LOG IN
            uploadLakeButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            registrationButton.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
        }else{
            //ALREADY LOG OUT
            uploadLakeButton.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            registrationButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
        }


        String sessionId = getIntent().getStringExtra("status");
        if("good".equals(sessionId)){
            //LOGIN IS SUCCESS
            uploadLakeButton.setVisibility(View.VISIBLE);

        }
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                Log.e("TAG","onSearchStateChanged: "+enabled);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Log.e("TAG","onSearchConfirmed: "+text);
                boolean foundOrNot = false;
                String textkey = String.valueOf(text).toLowerCase();//TEXT ENTER
                for(LakeEntity lakeEntity:mLakes){

                        String textMatch = String.valueOf(lakeEntity.getName()).toLowerCase();  //TEXT TOBE MATCH

                        Log.i("TAG","key enter: "+textkey+" ;nameDB: "+ textMatch);
                       // if(textMatch.equals(textkey)){

                        if(textMatch.matches("(.*)"+textkey+"(.*)")){
                         // Toasty.success(getApplicationContext(),"match",Toasty.LENGTH_SHORT).show();
                            Log.i("TAG","succ");
                            LatLng mll = new LatLng(lakeEntity.getLat(),lakeEntity.getLng());
                            float zoomLev = 19f;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mll,zoomLev));
                            foundOrNot = true;


                        } else{
                           // Toasty.error(getApplicationContext(),"not match",Toasty.LENGTH_SHORT).show();
                        }

                        //Log.e("TAGH",lakeEntity.getName());
                }
                if(!foundOrNot)
                    Toasty.error(getApplicationContext(),"not match",Toasty.LENGTH_SHORT).show();

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                Log.e("TAG","onButtonClicked: "+buttonCode);

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //CHECK FOR PERMISSION ON RUNTIME USING DEXTER LIBRARY
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
            }
        }).check();


        Ion.with(this)
                .load(mURL)
                .as(new TypeToken<List<LakeEntity>>(){})
                .setCallback(new FutureCallback<List<LakeEntity>>() {
                    @Override
                    public void onCompleted(Exception e, List<LakeEntity> result) {

                        Log.e("TAG", "AALL liST::"+ result.size());
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Ion.with(this)
                .load("http://fisheries.ap-south-1.elasticbeanstalk.com/api/getAll")
                .as(new TypeToken<List<LakeEntity>>(){})
                .setCallback(new FutureCallback<List<LakeEntity>>() {
                    @Override
                    public void onCompleted(Exception e, List<LakeEntity> lakes) {
//                      Log.e("TAG","tweets: "+lakes.get(0).getName());
                        mLakes = lakes;

                        for(final LakeEntity lake:mLakes){

                            LatLng mCoordinate = new LatLng(lake.getLat(), lake.getLng());

                            float zoomLevel = 9.0f; //This goes up to 21

                            mMap.addMarker(new MarkerOptions().position(mCoordinate).title(lake.getName()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCoordinate,zoomLevel));
                        }
                    }
                });

            // Add a marker and move the camera
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    ItemListDialogFragment.newInstance(mLakes.get(Integer.parseInt(marker.getId().substring(1)))).show(getSupportFragmentManager(), "dialog");
                    //Toast.makeText(getApplicationContext(),"tot!",Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    public void onItemClicked(int position) {

    }

    public void uploadLake(View view) {
        Log.e("TAG","BUTTON_SPEECH: "+materialSearchBar.getText());

        materialSearchBar.getText();
        startActivity(new Intent(MapsActivity.this,UploadLakeActivity.class));
    }

    public void listLake(View view) {
        startActivity(new Intent(MapsActivity.this,ListLakeActivity.class));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCurrentLocation(View view) {
        //Toasty.success(this,"CURRENT LOCATION",Toasty.LENGTH_SHORT).show();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MapsActivity.this);

        Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //nowDoSomethingWith(location.getLatitude(), location.getLongitude());
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Toast.makeText(this,"lat:"+lat+"  lng:"+lng ,Toast.LENGTH_LONG).show();
        LatLng currentLocation = new LatLng(lat,lng);

        LakeEntity myCurrentLocation = new LakeEntity("You are here",lat,lng,"district","desp","address","img");
        mLakes.add(myCurrentLocation);//    ADDING THIS NEW LIST WILL PREVENT THE ERROR BECAUSE WHEN WE CLICK THE CURRENT LOCATION IT ACCESS THE MLAKES LIST IN WHICH THE IS NO LIST FOR THE "YOU ARE HERE" PREVILUSLY

        Marker marker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here!"));

        //mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15f));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.current_location_update));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void registrationClick(View view) {
        startActivity(new Intent(MapsActivity.this, RegistrationActivity.class));
    }

    public void loginButtonClick(View view) {

        startActivity(new Intent(MapsActivity.this, LoginActivity.class));
        finish();
    }

    public void logOutButtonClick(View view) {
        sharedPreferences.edit().putString("login_status","logout").apply();
        uploadLakeButton.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        registrationButton.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.INVISIBLE);

        startActivity(new Intent(MapsActivity.this,MapsActivity.class));
        finish();
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
