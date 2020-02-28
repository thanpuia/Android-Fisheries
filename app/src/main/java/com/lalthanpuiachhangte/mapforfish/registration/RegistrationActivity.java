package com.lalthanpuiachhangte.mapforfish.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.lalthanpuiachhangte.mapforfish.MapsActivity;
import com.lalthanpuiachhangte.mapforfish.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {

    private MaterialEditText name, password, email;
    private Button registerNowButton;
    private ProgressBar progressBar;
    private String URL="http://fisheries.ap-south-1.elasticbeanstalk.com/api/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.userName);
        password = findViewById(R.id.userPassword);
        email = findViewById(R.id.userEmail);

        registerNowButton = findViewById(R.id.registerNowButton);
        progressBar = findViewById(R.id.simpleProgressBarRegistration);

    }

    public void registerNowClick(View view) {
        try{

            String mName = name.getText().toString();
            String mPassword = password.getText().toString();
            String mEmail = email.getText().toString();

            Ion.with(getApplicationContext())
                    .load(URL)
                    .uploadProgressHandler(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            progressBar.setVisibility(View.VISIBLE);
                            registerNowButton.setVisibility(View.INVISIBLE);
                        }
                    })
                    .setMultipartParameter("name",mName)
                    .setMultipartParameter("email",mEmail)
                    .setMultipartParameter("password",mPassword)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.e("TAG","RESULT::"+result);

                            if(result==null){
                                Toasty.error(getApplicationContext(),"email already taken or password should be more than 8 char",Toasty.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                registerNowButton.setVisibility(View.VISIBLE);
                            }else{
                                Log.e("TAG","RESULT::"+result);
                                startActivity(new Intent(RegistrationActivity.this, MapsActivity.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                registerNowButton.setVisibility(View.VISIBLE);
                            }



                        }
                    });
        }catch (Exception e){}

    }

    public void loginInTextClick(View view) {
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MapsActivity.class));
        finish();

    }
}
