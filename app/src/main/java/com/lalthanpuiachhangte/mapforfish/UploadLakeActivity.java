package com.lalthanpuiachhangte.mapforfish;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class UploadLakeActivity extends AppCompatActivity {

    Spinner district;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String real_path;
    private double lat,lng;
    String TAG = "TAG";
    private ProgressBar progressBar;
    Button selectPhotoButton, submitButton;
    LinearLayout progressBarLayout;
    private ImageView selectPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lake);

        district = findViewById(R.id.spinner_districrt);
        progressBar = findViewById(R.id.simpleProgressBar);
        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        submitButton = findViewById(R.id.submitButton);
        progressBarLayout = findViewById(R.id.progressBarLayout);
        selectPhoto = findViewById(R.id.selectPhoto);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(adapter);
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

                case 1:
                    if (resultCode == Activity.RESULT_OK) {

                        //data gives you the image uri. Try to convert that to bitmap
                        Uri file_uri = data.getData(); // parse to Uri if your videoURI is string
                        real_path = getRealPathFromURI(getApplicationContext(), file_uri);                        //Log.e(TAG, "data: ") ;
                         //path = getRealPathFromURI(data.getData());
                         Log.e(TAG, "Path: "+real_path) ;
                        ExifInterface exif = new ExifInterface(String.valueOf(real_path));
                        float[] latLong = new float[2];
                        boolean hasLatLong = exif.getLatLong(latLong);
                        if (hasLatLong) {
                            System.out.println("Latitude: " + latLong[0]);
                            System.out.println("Longitude: " + latLong[1]);
                            Log.e(TAG,"lat: "+ latLong[0]);
                            lat=latLong[0];
                            lng=latLong[1];

                            if(lat==0.0 ||lng==0.0){
                                Toast.makeText(this,"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();

                            }
                        }
                        //Display the photo start
                        Bitmap bitmap = BitmapFactory.decodeFile(real_path);
                        selectPhoto.setImageBitmap(bitmap);
                        //End

                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                        Toast.makeText(this,"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    public void submitClick(View view) {

        final EditText lakeName,lakeAddress,lakeDescription;
        lakeName =findViewById(R.id.lake_name);
        lakeAddress =findViewById(R.id.lake_address);
        lakeDescription = findViewById(R.id.lake_description);

        if(lat==0.0 ||lng==0.0){
            Toast.makeText(this,"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();

        }else{
            try{

                String sp = district.getSelectedItem().toString();
                Ion.with(getApplicationContext())
                        .load("http://fisheries.ap-south-1.elasticbeanstalk.com/api/save")
                        .uploadProgressHandler(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
                                progressBar.setVisibility(View.VISIBLE);
                                progressBarLayout.setVisibility(View.VISIBLE);

                                lakeName.setVisibility(View.INVISIBLE);
                                district.setVisibility(View.INVISIBLE);
                                selectPhotoButton.setVisibility(View.INVISIBLE);
                                submitButton.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setMultipartParameter("name", lakeName.getText().toString())
                        .setMultipartParameter("description", lakeDescription.getText().toString())
                        .setMultipartParameter("address", lakeAddress.getText().toString())

                        .setMultipartParameter("lat", String.valueOf(lat))
                        .setMultipartParameter("lng", String.valueOf(lng))
                        .setMultipartParameter("district", sp)
//                .setMultipartFile("image", "application/image", new File("sdcard/DCIM/Camera/IMG_20200217_123440.jpg"))//IMAGE TESTINGINGINIGNIGNIGNIGIGN
                        .setMultipartFile("image", "application/image", new File("sdcard"+real_path.substring(19)))//IMAGE path hi storage/something in a awm vang in ka siam chop, sdcard angkhian


                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {           ///sdcard/DCIM/Camera/IMG_20200217_123440.jpg
                                Log.e(TAG,"result: "+result);
                                Log.e(TAG,"path:" +"sdcard"+real_path.substring(19));

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
}

