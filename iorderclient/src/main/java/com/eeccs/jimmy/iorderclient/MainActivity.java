package com.eeccs.jimmy.iorderclient;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private AccessToken accessToken;
    int isLogin = 0;
    TextView show_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        Log.d("FB", "onCreate!");
        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();
        //找到login button
        show_status = (TextView)findViewById(R.id.tv1);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        if(isLogin == 0){
            //幫loginButton增加callback function
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                //登入成功
                @Override
                public void onSuccess(LoginResult loginResult) {
                    accessToken = loginResult.getAccessToken();
                    Log.d("FB", "access token got.");
                    //send request and call graph api
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                //當RESPONSE回來的時候
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {

                                    //讀出姓名 ID FB個人頁面連結
                                    try {
                                        String name = object.getString("name");
                                        String id = object.getString("id");
                                        Log.d("FB", "complete");
                                        //show_status.setText("Hello" );
                                        Log.d("FB", object.optString("name"));
                                        Log.d("FB", object.optString("link"));
                                        Log.d("FB", object.optString("id"));

                                    } catch (JSONException e) {
                                        Log.d("Error", e.toString());
                                    }
                                }
                            });
                    isLogin = 1;
                    //包入你想要得到的資料 送出request
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", " id , name , link");
                    request.setParameters(parameters);
                    request.executeAsync();
                    //Go to order_list
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, order_list.class);
                    startActivity(intent);
                    //MainActivity.this.finish();
                }

                //登入取消
                @Override
                public void onCancel() {
                    // App code
                    Log.d("FB","CANCEL");

                }
                //登入失敗

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_LONG).show();
                    Log.d("FB",exception.toString());
                    show_status.setText("Error" );
                }

            });
            isLogin = 1;
        }else {//isLogin = 1
            //Go to order_list
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, order_list.class);
            startActivity(intent);
            //MainActivity.this.finish();

        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}