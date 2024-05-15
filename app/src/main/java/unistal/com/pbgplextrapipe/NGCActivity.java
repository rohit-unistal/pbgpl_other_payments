package unistal.com.pbgplextrapipe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NGCActivity extends AppCompatActivity {
    Button searchButton;
    EditText edSearch,edName,edAddress,edQty,edPipeCharges,edPhone;
    Spinner spinpaymode;
    Button btnPay;
    ImageView img;
    ProgressLoading progressLoading;
    Context context;
    ArrayList<ActionModel> actionModelArrayList;
    public ArrayAdapter<ActionModel> actionAdapter;
    String tpa_id="",gas_deposit_amount = "",dma_id="",pay_mode = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngc);
        getSupportActionBar().hide();
        context = this;
        progressLoading = new ProgressLoading(context);
        searchButton = findViewById(R.id.btnSearch);
        edSearch = findViewById(R.id.edit_bpno);
        edName = findViewById(R.id.edit_name);
        edPhone = findViewById(R.id.edit_phone);
        edAddress = findViewById(R.id.edit_address);
        edQty = findViewById(R.id.edit_quantity);
        btnPay = findViewById(R.id.btnpay);
        edPipeCharges = findViewById(R.id.edit_charges);
        spinpaymode = findViewById(R.id.spin_pay_mode);
        actionModelArrayList = new ArrayList<>();
        //   edSearch.setText( "0103001788");
        img = findViewById(R.id.imglogout);
        sheetAction();
        actionAdapter = new ArrayAdapter<ActionModel>(context,R.layout.layoutspinner,actionModelArrayList);
        spinpaymode.setAdapter(actionAdapter);
        spinpaymode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pay_mode = actionModelArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutDialog();

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBPNo();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);
                builder.setTitle("Payment for ngc charges");

                //Setting message manually and performing action on button click
                builder.setMessage("Do customer want to pay for ngc charges ? " + "\n"+gas_deposit_amount)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Toast.makeText(getApplicationContext(),"Please Wait you choose yes action for payment",
                                        Toast.LENGTH_SHORT).show();
                               /* WebView webview = new WebView(context);
                                setContentView(webview);
                                byte[] post = EncodingUtils.getBytes("id="+dma_id+"&nextvar="+"", "BASE64");
                                webview.postUrl("http://142.79.231.30:8086/api/lmc-pay-bills.php", post);
                               *//* Uri uri = Uri.parse("http://142.79.231.30:8086/api/lmc-pay-bills.php");
                                startActivity(new Intent(Intent.ACTION_VIEW, uri));*/
                                payNow();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),"you choose no action for payment",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually

                alert.show();
            }
        });
    }
    public void sheetAction()
    {


        actionModelArrayList.add(new ActionModel("2","Online"));
        //    actionModelArrayList.add(new ActionModel("1","Cheque"));
    }
    public void searchBPNo() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            tpa_id = "";
            dma_id = "";
            edName.setText("");
            edPhone.setText("");
            edAddress.setText("");

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"getGasDepositbeforeNgc?user_id="+SessionUtil.getUserId(context)+"&schema="+SessionUtil.getGA(context)+"&bp_number="+edSearch.getText().toString();
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            try {
                                JSONObject respObject = new JSONObject(response);
                                String dataObject = respObject.get("data").toString();
                                if(dataObject.startsWith("{"))
                                {

                                    JSONObject jsonObject = new JSONObject(dataObject);
                                JSONArray rowArray = jsonObject.optJSONArray("rows");


                                JSONObject rowObject = rowArray.optJSONObject(0);
                                if(rowObject != null) {
                                    String name = rowObject.optString("first_name") + " " + rowObject.optString("last_name");
                                    String phone = rowObject.optString("mobile_number");
                                    String address = rowObject.optString("house_number") + ",\n" + rowObject.optString("locality") + "\n"
                                            + rowObject.optString("town") + "," + rowObject.optString("district") + "\n" +
                                            rowObject.optString("state") + "," + rowObject.optString("pin_code");
                                    tpa_id = rowObject.optString("tpa_id");
                                    dma_id = rowObject.optString("Dma");
                                    gas_deposit_amount =rowObject.optString("gas_deposit_amount");
                                    edName.setText(name);
                                    edPhone.setText(phone);
                                    edAddress.setText(address);

                                }else{
                                    Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();

                                }}else{
                                    Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException ignored) {
                                Log.e("Response JSONException",ignored.toString());
                                Toast.makeText(context, " "+ignored, Toast.LENGTH_SHORT).show();

                            }
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                    progressLoading.dismiss();
                    String body="",data="";
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        JSONObject jsonObject = new JSONObject(body);
                        data = jsonObject.optString("data");
                    } catch (UnsupportedEncodingException | JSONException e) {
                        // exception
                    }
                    //"Error code- " + error.networkResponse.statusCode +
                    Toast.makeText(context, "\n message-" + data, Toast.LENGTH_SHORT).show();
                }
            }) {

            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }

    }
    public void payNow() {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"generateNGCInvoice";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            //     if(response != null || !response.isEmpty()) {
                            if (response.contains("success")) {

                                dma_id = "";
                                edName.setText("");
                                edPhone.setText("");
                                edAddress.setText("");
                                gas_deposit_amount = "";
                            }

                            try {
                                JSONObject respObject = new JSONObject(response);
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(context);
                                builder.setMessage(respObject.optString("data"))
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                //Setting the title manually

                                alert.show();
                                //   Toast.makeText(getApplicationContext(), respObject.optString("data"), Toast.LENGTH_LONG).show();

                            } catch (JSONException ignored) {
                                Toast.makeText(getApplicationContext(), ignored.toString(), Toast.LENGTH_LONG).show();
                            }
                            //loadingDialog.dismiss();
                            //  }
                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error response"," "+error.getLocalizedMessage()+" " +error.getCause()
                            +" "+error.networkResponse);
                    error.getMessage();
                    progressLoading.dismiss();
                    Toast.makeText(context, error.networkResponse.statusCode+"", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id",SessionUtil.getUserId(context));
                    params.put("dma_id", dma_id);
                    params.put("tpa_id", tpa_id);
                    params.put("bp_number", edSearch.getText().toString());
                    params.put("mode_of_deposite", pay_mode);
                    params.put("gas_deposit_amount", gas_deposit_amount);

                    params.put("schema", SessionUtil.getGA(context));
                    Log.e("String Request",params.toString());
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }

    }
    private void logOutDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Are you sure?")
                .setMessage("Want to Logout")
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SessionUtil.removeUserDetails(context);


                                Intent logout = new Intent(context, LoginActivity.class);
                                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(logout);
                                finish();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                }).show();
    }
}