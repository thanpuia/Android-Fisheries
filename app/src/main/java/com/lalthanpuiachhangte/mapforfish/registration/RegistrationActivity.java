package com.lalthanpuiachhangte.mapforfish.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.lalthanpuiachhangte.mapforfish.MapsActivity;
import com.lalthanpuiachhangte.mapforfish.R;
import com.rengwuxian.materialedittext.MaterialEditText;

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

                            startActivity(new Intent(RegistrationActivity.this, MapsActivity.class));
                        }
                    });
        }catch (Exception e){}

    }

    public void loginInTextClick(View view) {
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }
}
