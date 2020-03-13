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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import es.dmoral.toasty.Toasty;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

import static android.view.View.GONE;

public class UploadLakeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String real_path_lake;
    private static String real_path_profileImage;

    private double lat,lng;
    private String TAG = "TAG";
    private String URLs="http://10.180.243.32:8000/api/user/update/1";
    int LOCATION_CONFIRM_NO_CYCLES = 7;
    private SharedPreferences sharedPreferences;
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

    private Spinner district;

    private CheckBox checkBox;

    private ImageView selectPhoto;
    private ImageView profileImageViewButton;

    GoogleApiClient mGoogleApiClient;
    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lake);

        sharedPreferences = this.getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        Log.e("TAG","My Token: "+sharedPreferences.getString("token",""));
        //district = findViewById(R.id.spinner_districrt);
        progressBar = findViewById(R.id.simpleProgressBar);
        locationConfirmProgressBar = findViewById(R.id.locationConfirmProgressBar);

        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        submitButton = findViewById(R.id.submitButton);
        locationConfirmButton = findViewById(R.id.locationConfirm);
        takePhotoButton = findViewById(R.id.takePhoto);

        progressBarLayout = findViewById(R.id.progressBarLayout);
        linearLayoutConfirmLocation = findViewById(R.id.linearLayoutConfirmLocation);
        linearLayoutMainForm = findViewById(R.id.linearLayoutMainForm);

        nameEditText = findViewById(R.id.editTextDataName);
        fathersNameEditText = findViewById(R.id.editTextDataFathersName);
        addressEditText= findViewById(R.id.editTextDataAddress);
        epicOrAadhaarEditText= findViewById(R.id.editTextDataEpicNo);
        contactEditText     = findViewById(R.id.editTextDataContact);
        areaEditText = findViewById(R.id.editTextDataArea);

        district = findViewById(R.id.spinner_district);

        checkBox = findViewById(R.id.checkbox);



        selectPhoto = findViewById(R.id.selectPhoto);
        profileImageViewButton = findViewById(R.id.imageViewDateProfilePicture);

        takePhotoButton.setEnabled(false);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(adapter);



        //  linearLayoutMainForm.addView(checkBoxScheme);
//        progressBar.setVisibility(GONE);
//        linearLayoutMainForm.setVisibility(GONE);
//        linearLayoutConfirmLocation.setVisibility(View.VISIBLE);

    }


    public void selectPhoto(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
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

//        if(lat==0.0 ||lng==0.0){
        if(false){

            Toast.makeText(this,"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();

        }else{
            try{
               // String mName = nameEditText.getText().toString();
                /* String mfathersName = fathersNameEditText.getText().toString();
                String mAddress = addressEditText.getText().toString();
                String mEpicOrAadhar = epicOrAadhaarEditText.getText().toString();
                String mContact = contactEditText.getText().toString();
                String mArea = areaEditText.getText().toString();*/

                String mDistrict = district.getSelectedItem().toString();
                Log.e("TAG","DISTRICT: "+mDistrict);
                String mToken = sharedPreferences.getString("token","");

                JsonObject json = new JsonObject();
                json.addProperty("district", mDistrict);
               // json.addProperty("profileImage", new File("sdcard"+real_path_profileImage.substring(19)));
               // json.addProperty("nme", "WHAT THE FUDGE");


                //String sp = district.getSelectedItem().toString();
                Ion.with(getApplicationContext())
                        .load("PUT",URLs)
                        .uploadProgressHandler(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
                                progressBar.setVisibility(View.VISIBLE);
                                progressBarLayout.setVisibility(View.VISIBLE);

                                //lakeName.setVisibility(View.INVISIBLE);
                              //  district.setVisibility(View.INVISIBLE);
                                selectPhotoButton.setVisibility(View.INVISIBLE);
                                submitButton.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setHeader("Accept","application/json")
                        .setHeader("Authorization","Bearer "+mToken)
                        .setHeader("Content-Type","application/x-www-form-urlencoded")
                        /*.setMultipartParameter("name", mName)
                        .setMultipartParameter("name", mName)
                        .setMultipartParameter("name", mName)
                        .setMultipartParameter("name", mName)
                        .setMultipartParameter("name", mName)

                        .setMultipartParameter("lat", String.valueOf(lat))
                        .setMultipartParameter("lng", String.valueOf(lng))
                      //  .setMultipartParameter("district", sp)
//                      .setMultipartFile("image", "application/image", new File("sdcard/DCIM/Camera/IMG_20200217_123440.jpg"))//IMAGE TESTINGINGINIGNIGNIGNIGIGN
                        .setMultipartFile("image", "application/image", new File("sdcard"+real_path_lake.substring(19)))//IMAGE path hi storage/something in a awm vang in ka siam chop, sdcard angkhian
*/


                        //json ang nilo in MULTIPART ANGIN HANDLE MAI RAWH SE SERVER AH. A HMA A DIK SA ANG KHAN. JSON BODY LEH MULTIPART A AWM KOP THEI SI LO
                        .setMultipartFile("",new File("sdcard"+real_path_profileImage.substring(19)))
                        .setMultipartParameter("district",mDistrict)
                      //  .setJsonObjectBody(json)

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {           ///sdcard/DCIM/Camera/IMG_20200217_123440.jpg
                                Log.e(TAG,"result: "+result);
//                                Log.e(TAG,"path:" +"sdcard"+real_path.substring(19));

                                Toasty.success(getApplicationContext(),"Upload Sucess!!",Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                                finish();

                            }
                        });
            }catch (Exception e){
                Toast.makeText(this,"Some error in data:"+e,Toast.LENGTH_LONG).show ();

            }
        }


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

    public void profilePictureButtonClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),2);


    }

}

