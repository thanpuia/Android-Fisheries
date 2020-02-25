package com.lalthanpuiachhangte.mapforfish.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.lalthanpuiachhangte.mapforfish.MapsActivity;
import com.lalthanpuiachhangte.mapforfish.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText loginEmail,loginPassword;
    private String URL="http://fisheries.ap-south-1.elasticbeanstalk.com/api/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
    }

    public void mLoginButtonClick(View view) {

        String mLoginEmail = loginEmail.getText().toString();
        String mLoginPassword = loginPassword.getText().toString();

        Ion.with(getApplicationContext())
                .load(URL)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {

                    }
                })
                .setMultipartParameter("email",mLoginEmail)
                .setMultipartParameter("password",mLoginPassword)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        Log.e("TAG","result:"+result);
                     /*   String token = result.get("token").toString();
                        if(token!=null){
                            Log.e("TAG","LOGIN");
                            MapsActivity.uploadLakeButton.setVisibility(View.VISIBLE);

                            startActivity(new Intent(LoginActivity.this,MapsActivity.class));
                        }*/
                    }
                });
    }

    public void notYetRegisterClick(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }
}
