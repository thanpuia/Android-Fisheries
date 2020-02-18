package com.lalthanpuiachhangte.mapforfish;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lalthanpuiachhangte.mapforfish.adapter.ListViewAdapter;
import com.lalthanpuiachhangte.mapforfish.entity.LakeEntity;
import com.lalthanpuiachhangte.mapforfish.fragments.ItemListDialogFragment;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ItemListDialogFragment.Listener {

    private GoogleMap mMap;
    private List<LakeEntity> mLakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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


                            mMap.addMarker(new MarkerOptions().position(mCoordinate).title(lake.getName()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(mCoordinate));


                        }
                    }
                });

        // Add a marker in Sydney and move the camera
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //    Log.e("TAG","Marker: "+ marker.getId()+" " +marker.getSnippet()+" " +marker.getTitle());
                ItemListDialogFragment.newInstance(mLakes.get(Integer.parseInt(marker.getId().substring(1)))).show(getSupportFragmentManager(), "dialog");
                //Toast.makeText(getApplicationContext(),"tot!",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onItemClicked(int position) {

    }

    public void uploadLake(View view) {
        startActivity(new Intent(MapsActivity.this,UploadLakeActivity.class));
    }

    public void listLake(View view) {
        startActivity(new Intent(MapsActivity.this,ListLakeActivity.class));

    }
}
