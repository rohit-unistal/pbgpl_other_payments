package unistal.com.pbgplextrapipe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    EditText userName;

    EditText passWord;

    Button btnLogIn;
   /* @BindView(R.id.imglogout)
    ImageView imgLogout;*/
    Map dbparams = new HashMap();
    private ProgressLoading progressLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // getSupportActionBar().hide();

       // imgLogout.setVisibility(View.GONE);
        progressLoading = new ProgressLoading(this);
        init();
        //registerReceiver();
    }/**
         * This method is responsible to register receiver with NETWORK_CHANGE_ACTION.
         * */


        @Override
        protected void onDestroy()
        {
            super.onDestroy();
        }

        /**
         * This is internal BroadcastReceiver which get status from external receiver(NetworkChangeReceiver)
         * */
        InternalNetworkChangeReceiver internalNetworkChangeReceiver = new InternalNetworkChangeReceiver();
        class InternalNetworkChangeReceiver extends BroadcastReceiver
        {
            @Override
            public void onReceive(Context context, Intent intent) {


                    userName.setText(intent.getStringExtra("status"));
            }
        }
    private void init(){
       userName = findViewById(R.id.enter_user_name);
       passWord = findViewById(R.id.enter_password);
       btnLogIn =  findViewById(R.id.btn_log_in);
       btnLogIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               logIn();
           }
       });
        SharedPreferences prefs = SessionUtil.getUserSessionPreferences(LoginActivity.this);
        if (prefs.contains(SessionUtil.USER_ID)) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }


    }





    public void logIn() {
       // if(DialogUtil.checkInternetConnection(this)) {
            progressLoading.onShow();
            RequestQueue queue = Volley.newRequestQueue(this);
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


            final String url = AppConstants.APP_BASE_URL+"auth";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                         //   Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                            Log.e("Response ", response);
                            progressLoading.dismiss();
                            if(!response.equals("0")) {
                            try {

                                    JSONObject respObject = new JSONObject(response);
                                    JSONObject jsonObject = respObject.getJSONObject("user");

                                    progressLoading.dismiss();
                               if (jsonObject.getString("role").equals("dma"))
                                {
                                    SessionUtil.saveUserId(jsonObject.getString("id"), LoginActivity.this);
                                    SessionUtil.saveGA(jsonObject.getString("schema"), LoginActivity.this);
                                    SessionUtil.saveModules(jsonObject.getString("modules"), LoginActivity.this);

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);}
                                else{
                                    Toast.makeText(LoginActivity.this, "invalid user", Toast.LENGTH_LONG).show();

                                }

                            }
                            catch (JSONException e)
                            {

                            }

                        }else if(response.equals("0")){
                            Toast.makeText(LoginActivity.this, "Username and Password wrong", Toast.LENGTH_LONG).show();
                                progressLoading.dismiss();
                            }
                        else{
                                Toast.makeText(LoginActivity.this, "Username and Password wrong", Toast.LENGTH_LONG).show();

                                progressLoading.dismiss();
                            }

                        }


                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse.statusCode != 200)
                    {
                        Toast.makeText(LoginActivity.this, "Username and Password wrong", Toast.LENGTH_LONG).show();

                    }
                  //  Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    progressLoading.dismiss();
                }
            })





 {
        @Override
        public String getBodyContentType() {
            return "application/json; charset=utf-8";
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            String mRequestBody=null;
              progressLoading.dismiss();
            try {

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email",  userName.getText().toString().trim());
                jsonBody.put("password", passWord.getText().toString().trim());
                jsonBody.put("device", deviceId);
                mRequestBody = jsonBody.toString();

                } catch (JSONException e) {
            } /*catch (UnsupportedEncodingException uee) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                return null;
            }*/
            try {
                return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
return null;
        }
      /*          @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("email",);
                    params.put("password", );
                    dbparams = params;
                    return params;
                }
*/
            };

      /*  stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });*/
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        /*}else{
            DialogUtil.showNoConnectionDialog(this);
        }*/
    }


}