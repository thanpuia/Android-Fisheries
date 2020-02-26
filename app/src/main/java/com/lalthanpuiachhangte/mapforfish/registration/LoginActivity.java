package com.lalthanpuiachhangte.mapforfish.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.lalthanpuiachhangte.mapforfish.MapsActivity;
import com.lalthanpuiachhangte.mapforfish.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public SharedPreferences sharedPreferences;
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
                        /*
                            Login Success
                        * result{
                        * "success":"true"
                        * }

                        Login Unsuccess
                        * result{
                        * "success":"false"
                        * }


                        *
                        * */
                        Log.e("TAG","result:"+result);
                        JsonElement success = result.get("status");

                        if( success.getAsBoolean()){

                            sharedPreferences = getApplication().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("login_status","login").apply();

                            Log.e("TAG","LOGIN");
                            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                            intent.putExtra("status","good");
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public void notYetRegisterClick(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
        finish();
    }
}
