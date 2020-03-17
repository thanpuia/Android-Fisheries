package com.lalthanpuiachhangte.mapforfish.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.lalthanpuiachhangte.mapforfish.MapsActivity;
import com.lalthanpuiachhangte.mapforfish.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {

    private MaterialEditText name, password, phone;
    private Button registerNowButton;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    private String URLs= String.valueOf(R.string.IP_ADDRESS)+"api/register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.userName);
        password = findViewById(R.id.userPassword);
        phone = findViewById(R.id.userPhone);

        registerNowButton = findViewById(R.id.registerNowButton);
        progressBar = findViewById(R.id.simpleProgressBarRegistration);

    }

    public void registerNowClick(View view) {
        Toasty.info(this,"REGISTER click",Toasty.LENGTH_SHORT).show();
        try{

            String mName = name.getText().toString();
            String mPassword = password.getText().toString();
            String contact = phone.getText().toString();

            Ion.with(getApplicationContext())
                    .load("http://10.180.243.6:8000/api/register")
                    .uploadProgressHandler(new ProgressCallback() {
                        @Override
                        public void onProgress(long downloaded, long total) {
                            progressBar.setVisibility(View.VISIBLE);
                            registerNowButton.setVisibility(View.INVISIBLE);
                        }
                    })
                    .setMultipartParameter("name",mName)
                    .setMultipartParameter("contact",contact)
                    .setMultipartParameter("password",mPassword)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            Log.e("TAG","RESULT::"+result);
                            if(result!=null){
                                JsonElement statusAsJson = result.get("success");
                                JsonElement tokenAsJson = result.get("token");

                                Log.e("TAG","RESULT::"+result.get("success"));
                                Log.e("TAG","TOKEN::"+result.get("token"));

                                sharedPreferences = getApplication().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("mToken",result.get("token").toString()).apply();

                                if(result.get("success").getAsBoolean()==true){
                                Toasty.success(getApplicationContext(),"Register Successfully!",Toasty.LENGTH_SHORT).show();

                                Log.e("TAG","RESULT::"+result);
                                startActivity(new Intent(RegistrationActivity.this, MapsActivity.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                registerNowButton.setVisibility(View.VISIBLE);
                            }else{
                                  Toasty.error(getApplicationContext(),"Email already taken or password should be more than 8 char",Toasty.LENGTH_SHORT).show();
                                  progressBar.setVisibility(View.INVISIBLE);
                                  registerNowButton.setVisibility(View.VISIBLE);
                              }
                          }else

                            progressBar.setVisibility(View.INVISIBLE);
                            registerNowButton.setVisibility(View.VISIBLE);


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
