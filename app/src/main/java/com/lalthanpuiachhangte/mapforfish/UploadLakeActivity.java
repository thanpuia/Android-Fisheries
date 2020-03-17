package com.lalthanpuiachhangte.mapforfish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

import static android.view.View.GONE;

public class UploadLakeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    /*
    *START
     * this is for the scheme list
    */
    String[] name = {"NFDB", "RKVY", "NLUP", "Blue Revolution"};
    ArrayList<String> schemeCheckLists = new ArrayList<>();
    int j=0;
    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // return 0;
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView= getLayoutInflater().inflate(R.layout.row_scheme,null);

            final CheckBox checkBox_= convertView.findViewById(R.id.rowCheckBox);

            checkBox_.setText(name[position]);

            checkBox_.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked){
                        schemeCheckLists.add(checkBox_.getText().toString());
                    }else{
                        schemeCheckLists.remove(checkBox_.getText().toString());
                    }
                }
            });


            return convertView;
        }
    }


    /*
    ENDS
    * this is for the scheme list
    * */




    private SharedPreferences sharedPreferences;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String real_path_lake;
    private static String real_path_profileImage;

    private double lat,lng;
    private String TAG = "TAG";
    private String URL1="http://10.180.243.6:8000/api/fishponds/create/";

    private String URL2="";

    int LOCATION_CONFIRM_NO_CYCLES = 7;
    private ArrayList<String> nameOfSchemes;

    private LatLng formPicture;
    private LatLng fromLastKnowLocation;

    private ProgressBar progressBar;
    private ProgressBar locationConfirmProgressBar;

    private Button selectPhotoButton;
    private Button submitButton;
    private Button locationConfirmButton;
    private Button takePhotoButton;

    private LinearLayout progressBarLayout;
    private LinearLayout linearLayoutConfirmLocation;
    private LinearLayout linearLayoutMainForm;

    private MaterialEditText nameEditText;
    private MaterialEditText fathersNameEditText;
    private MaterialEditText addressEditText;
    private MaterialEditText epicOrAadhaarEditText;
    private MaterialEditText contactEditText;
    private MaterialEditText areaEditText;
    private MaterialEditText tehsilEditText;

    private Spinner districtSpinner;

    private CheckBox checkBox;

    private ImageView selectPhoto;
    private ImageView profileImageViewButton;

    private ListView listOfScheme;

    GoogleApiClient mGoogleApiClient;
    protected LocationManager locationManager;

    String mToken;
    String mContact;
    String mName;
    int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lake);

        sharedPreferences = this.getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",mId);


        Log.e("TAG","My Token: "+sharedPreferences.getString("token",""));
        //district = findViewById(R.id.spinner_districrt);
        progressBar = findViewById(R.id.simpleProgressBar);
        locationConfirmProgressBar = findViewById(R.id.locationConfirmProgressBar);

        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        submitButton = findViewById(R.id.submitButton);
        locationConfirmButton = findViewById(R.id.locationConfirm);
        takePhotoButton = findViewById(R.id.takePhoto);

        //progressBarLayout = findViewById(R.id.progressBarLayout);
        linearLayoutConfirmLocation = findViewById(R.id.linearLayoutConfirmLocation);
        linearLayoutMainForm = findViewById(R.id.linearLayoutMainForm);

        nameEditText = findViewById(R.id.editTextDataName);
        fathersNameEditText = findViewById(R.id.editTextDataFathersName);
        addressEditText= findViewById(R.id.editTextDataAddress);
        epicOrAadhaarEditText= findViewById(R.id.editTextDataEpicNo);
        contactEditText = findViewById(R.id.editTextDataContact);
        areaEditText = findViewById(R.id.editTextDataArea);
        tehsilEditText = findViewById(R.id.editTextTehsil);
        districtSpinner = findViewById(R.id.spinner_district);

        checkBox = findViewById(R.id.checkbox);
        listOfScheme = findViewById(R.id.list_of_scheme);

        selectPhoto = findViewById(R.id.selectPhoto);
        profileImageViewButton = findViewById(R.id.imageViewDateProfilePicture);

        takePhotoButton.setEnabled(false);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        //LIST ADAPTER FOR LIST-OF-SCHEME
        CustomAdapter customAdapter = new CustomAdapter();
        listOfScheme.setAdapter(customAdapter);
        listOfScheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG","LIST TIST: "+i);
            }
        });
    }

    public void selectPhoto(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }

    public void profilePictureButtonClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case 1://LAKE PICTURE SELECT
                    if (resultCode == Activity.RESULT_OK) {

                        //data gives you the image uri. Try to convert that to bitmap
                        Uri file_uri = data.getData(); // parse to Uri if your videoURI is string
                        real_path_lake = getRealPathFromURI(getApplicationContext(), file_uri);                        //Log.e(TAG, "data: ") ;
                        //path = getRealPathFromURI(data.getData());
                        Log.e(TAG, "Path: "+real_path_lake) ;
                        ExifInterface exif = new ExifInterface(String.valueOf(real_path_lake));
                        float[] latLong = new float[2];
                        boolean hasLatLong = exif.getLatLong(latLong);
                        if (hasLatLong) {
                            System.out.println("Latitude: " + latLong[0]);
                            System.out.println("Longitude: " + latLong[1]);
                            Log.e(TAG,"lat: "+ latLong[0]);
                            lat=latLong[0];
                            lng=latLong[1];

                            formPicture = new LatLng(lat,lng);
                            if(lat==0.0 ||lng==0.0){
                                Toast.makeText(this,"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();
                            }
                        }else formPicture = new LatLng(0.0,0.0);

                        //Display the photo start
                        Bitmap bitmap = BitmapFactory.decodeFile(real_path_lake);
                        selectPhoto.setImageBitmap(bitmap);
                        //End

                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                        Toast.makeText(this,"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();
                    }
                    break;
                case 2://PROFILE PICTURE SELECT
                    Uri fileUri = data.getData();
                    real_path_profileImage = getRealPathFromURI(getApplicationContext(),fileUri);
                    Bitmap profilePictureBitmap = BitmapFactory.decodeFile(real_path_profileImage);
                    profileImageViewButton.setImageBitmap(profilePictureBitmap);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    public void submitClick(View view) {
        submitButton.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        apiFirst();
        apiSecond();
        Log.e("TAG",schemeCheckLists+"{}{}");
    }

    /* Get the real path from the URI */
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public void getLastKnowLocation(View view) {
        locationConfirmButton.setEnabled(false);

        locationConfirmProgressBar.setVisibility(View.VISIBLE);
        EasyCountDownTextview countDownTextview = new EasyCountDownTextview(this);//= (EasyCountDownTextview) findViewById(R.id.easyCountDownTextview);
        countDownTextview.setTime(0, 0, 0, LOCATION_CONFIRM_NO_CYCLES);
        countDownTextview.setOnTick(new CountDownInterface() {
            @Override public void onTick(long time) {

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

                Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Double lat = location.getLatitude(); Double lng = location.getLongitude();
                time = Math.round(time/1000);
                int countTime = (int) (LOCATION_CONFIRM_NO_CYCLES - time)* 10;

                if(time==0)
                        countTime = 100;
                Toast.makeText(getApplicationContext(),"Complete: "+countTime+"%" ,Toast.LENGTH_SHORT).show();

                locationConfirmProgressBar.setProgress(countTime);
               // Toast.makeText(getApplicationContext(),"lat:"+lat+"  lng:"+lng +"\nClick:"+time+" time(s)" ,Toast.LENGTH_SHORT).show();



            }
            @Override
            public void onFinish() {

                locationConfirmButton.setEnabled(true);
                takePhotoButton.setEnabled(true);
                linearLayoutMainForm.setVisibility(View.VISIBLE);
                linearLayoutConfirmLocation.setVisibility(GONE);

                fromLastKnowLocation = new LatLng(lat,lng);

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void takePhotoClick(View view) {
    }

    public void apiFirst(){

        String fname = fathersNameEditText.getText().toString();
        String address= addressEditText.getText().toString();
        String district= districtSpinner.getSelectedItem().toString();;
        String location_of_pond= "TESTING";//HEI HI ENG KAN CIAG LO
        String tehsil = tehsilEditText.getText().toString();
        String area= areaEditText.getText().toString();
        String epic_no= epicOrAadhaarEditText.getText().toString();
        String name_of_scheme= String.join(",",schemeCheckLists);
        String lat="TESTIGN";
        String lng="TESTING";

        try{
            Ion.with(getApplicationContext())
                    .load("POST",URL1)
                    .setHeader("Authorization","Bearer "+mToken)

                    .setMultipartParameter("fname",fname)
                    .setMultipartParameter("address",address)
                    .setMultipartParameter("district",district)
                    .setMultipartParameter("location_of_pond",location_of_pond)
                    .setMultipartParameter("tehsil",tehsil)
                    .setMultipartParameter("area",area)
                    .setMultipartParameter("epic_no",epic_no)
                    .setMultipartParameter("name_of_scheme",name_of_scheme)
                    .setMultipartParameter("lat",lat)
                    .setMultipartParameter("lng",lng)

                    .setMultipartFile("image","multipart/form-data",new File(real_path_profileImage))

                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.e("TAG","FILE "+real_path_profileImage);

                            Log.e("TAG","URL 1 URL 1 URL 1: "+result);
                        }
                    });
        }catch (Exception e){
            Log.e("TAG","ERROR IN URL1");

        }
    }

    public void apiSecond(){

        // if(lat==0.0 ||lng==0.0){
        if(false){
            Toast.makeText(this,"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();
        }else{
            try{
                Ion.with(getApplicationContext())
                        .load("PUT","http://10.180.243.6:8000/api/fishponds/uploadpond/"+mId)
                        .uploadProgressHandler(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
//                                progressBar.setVisibility(View.VISIBLE);
//                                progressBarLayout.setVisibility(View.VISIBLE);
//
//                                //lakeName.setVisibility(View.INVISIBLE);
//                              //  district.setVisibility(View.INVISIBLE);
//                                selectPhotoButton.setVisibility(View.INVISIBLE);
//                                submitButton.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setHeader("Authorization","Bearer "+mToken)
                        .setHeader("Content-Type","multipart/form-data")

                        .setMultipartParameter("_method", "PUT")
                        .setMultipartFile("pondImages[]","multipart/form-data",new File(real_path_lake))
                        //json ang nilo in MULTIPART ANGIN HANDLE MAI RAWH SE SERVER AH. A HMA A DIK SA ANG KHAN. JSON BODY LEH MULTIPART A AWM KOP THEI SI LO

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {           ///sdcard/DCIM/Camera/IMG_20200217_123440.jpg
                                submitButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(GONE);
                                Log.e(TAG,"result: "+result);

                                Toasty.success(getApplicationContext(),"Upload Success!!",Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                                finish();
                            }
                        });


            }catch (Exception e){
                Toast.makeText(this,"Some error in data:"+e,Toast.LENGTH_LONG).show ();

            }
        }

    }
}

