package unistal.com.pbgplextrapipe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {
    EditText editDate, editBPNo, editMessage;
    Spinner spinRequestType;
    ImageView imgcustomerphoto,imgback;
    Button btnSubmit;
    ArrayList<ServiceModel> serviceModelArrayList;
    ArrayAdapter serviceAdapter;
    String serviceID = "";
    ProgressLoading progressLoading;
    Context context;
    Boolean flag=true;
    private Bitmap customerBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        init();

    }

    private void init() {
        context = this;
        progressLoading = new ProgressLoading(context);
        imgcustomerphoto = findViewById(R.id.imgcustomerphoto);
        imgback = findViewById(R.id.imgback);
        editDate = findViewById(R.id.edit_date);
        editBPNo = findViewById(R.id.edit_bpno);
        editMessage = findViewById(R.id.edit_message);
        spinRequestType = findViewById(R.id.spin_service_type);
        btnSubmit = findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        imgcustomerphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureCustomerPhoto();
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serviceModelArrayList = new ArrayList<ServiceModel>();
        getCurrentDate();
        getServiceData();
    }

    public void getCurrentDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        editDate.setText( mDay + "-" + (mMonth + 1) + "-" +mYear);
    }

    private void getServiceData() {
        serviceModelArrayList.clear();
        serviceModelArrayList.add(new ServiceModel("0","select",
               "description","0",
                "0", "0",
                "0", "0",
                "0", "0",
                "0", "0"));

        progressLoading.onShow();
        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = AppConstants.APP_BASE_URL + "service/lists?schema=" + SessionUtil.getGA(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.e("Response ", response);
                        progressLoading.dismiss();
                        if (!response.equals("0")) {
                            try {
                                JSONObject djob = new JSONObject(response);

                                JSONArray darr = djob.optJSONArray("data");
                                if (darr != null) {
                                    Log.e("service", darr.toString());
                                    for (int i = 0; i < darr.length(); i++) {

                                        serviceModelArrayList.add(new ServiceModel(darr.getJSONObject(i).getString("id"), darr.getJSONObject(i).getString("service"),
                                                darr.getJSONObject(i).getString("description"), darr.getJSONObject(i).getString("amount"),
                                                darr.getJSONObject(i).getString("tax"), darr.getJSONObject(i).getString("final_amount"),
                                                darr.getJSONObject(i).getString("maintenance_type"), darr.getJSONObject(i).getString("status"),
                                                darr.getJSONObject(i).getString("created_at"), darr.getJSONObject(i).getString("updated_at"),
                                                darr.getJSONObject(i).getString("status_datetime"), darr.getJSONObject(i).getString("tax_id")));

                                    }
                                }


                            } catch (JSONException ignored) {
                             Log.e("Exception",   ignored.toString());
                            }
                        }
                        Log.e("arraylist",serviceModelArrayList.toString());
                        serviceAdapter = new ArrayAdapter(context, R.layout.layoutspinner, serviceModelArrayList);


                        spinRequestType.setAdapter(serviceAdapter);

                        serviceAdapter.notifyDataSetChanged();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {
                            DialogUtil.showMessageDialog(context, "Internet connection break");

                        } else if (error != null && !error.toString().isEmpty()) {
                            //  DialogUtil.showMessageDialog(context, " Unknown Error from server side" + error.networkResponse.statusCode);

                            Toast.makeText(context, "Error from server side" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressLoading.dismiss();

                    }
                });


        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(600000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
        spinRequestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serviceID = serviceModelArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                serviceID ="0";
            }
        });
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
private void submit(){
    if (validate()) {
        if (DialogUtil.checkInternetConnection(this)) {
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }
            flag = false;
            final String url = AppConstants.APP_BASE_URL + "modification/request";
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            progressLoading.dismiss();
                            flag = true;
                            Log.e("RRRR response", String.valueOf(response));
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                if (message.contains("success")) {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    clear();

                                } else {
                                    Toast.makeText(context, "status = " + status + " messages = " + message, Toast.LENGTH_LONG).show();

                                }


                            } catch (JSONException e) {
                                Toast.makeText(context, e.getMessage() + " " + e.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("Not GotError response", e.toString());
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressLoading.dismiss();
                            flag = true;
                            if (error == null) {
                                if (error != null && error.networkResponse.data != null) {
                                    try {
                                        String responseString = new String(error.networkResponse.data);
                                        JSONObject obj = new JSONObject(responseString);
                                        if (responseString.contains("success")) {
                                            // Toast.makeText(getApplicationContext(), obj.getString("data"), Toast.LENGTH_SHORT).show();
                                            Log.e("Not GotError response", "" + obj.getString("success"));


                                            Log.e("GotError", "" + obj.getString("data"));
                                            Toast.makeText(context, "status = " + obj.getString("success") + " messages = " + obj.getString("data"), Toast.LENGTH_LONG).show();
                                        }
                                        if (responseString.contains("success")) {
                                            // Toast.makeText(getApplicationContext(), obj.getString("data"), Toast.LENGTH_SHORT).show();
                                            Log.e("Not GotError response", "" + obj.getString("success"));


                                            Log.e("GotError", "" + obj.getString("data"));
                                            Toast.makeText(context, "status = " + obj.getString("success") + " messages = " + obj.getString("data"), Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(context, e.getMessage() + " " + e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("Not GotError response", e.toString());
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Error in connectivity or server side error-" + error.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("Not GotError response", error.toString());
                            }
                        }
                    }) {


                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    if (customerBitmap != null) {
                        params.put("supportFile", new VolleyMultipartRequest.DataPart("request" + imagename + ".png", getFileDataFromDrawable(customerBitmap)));
                    }


                    return params;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("schema", SessionUtil.getGA(context));
                    params.put("user_id", SessionUtil.getUserId(context));
                    params.put("bpNumber", editBPNo.getText().toString().trim());
                    params.put("serviceType", serviceID);
                    params.put("message", editMessage.getText().toString().trim());



                    //params.put("date_to", "0000-00-00");
                    Log.e("Pppppppppppppppppparams",url+" "+ params.toString());
                    return params;

                }


            };

            //adding the request to volley
            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    1000000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(volleyMultipartRequest);
        }
    }
}
    public boolean validate() {
        boolean valid = true;
        if (editBPNo.getText().toString().trim().equals("")) {
            valid = false;
            Toast.makeText(context, "Enter bp number", Toast.LENGTH_SHORT).show();
        }
        if (editMessage.getText().toString().trim().equals("")) {
            valid = false;
            Toast.makeText(context, "Enter message", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

    private void clear() {
        editBPNo.setText("");
        editMessage.setText("");
        customerBitmap = null;
        imgcustomerphoto.setImageResource(android.R.color.transparent);
    }
    public void captureCustomerPhoto() {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(RequestActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(RequestActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))) {

        } else {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RequestActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        100);

            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, 101);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                customerBitmap = photo;

                imgcustomerphoto.setImageBitmap(photo);

            } else {
                // Log.e("filePath",filePath);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

            }

        }


    }

}


