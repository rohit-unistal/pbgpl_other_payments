package unistal.com.pbgplextrapipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModificationActivity extends AppCompatActivity {
    Context context;
    LinearLayout lm;
    EditText edmaterialamt,edittotalcharge;
    EditText edit_modify,editBPNumber,editName,editAddress,editService,editServiceAmount;
    ProgressLoading progressLoading;
    JSONArray dataArray;
    String requestNo="",BPNumber="";
    Button btnSearch,btnSubmit;
    ArrayList materialIDList,materailNameList,materialQuantityList;
    RadioButton rbtgenerate,rbtnextgasbill;
    ImageView imgback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);
        getSupportActionBar().hide();
        context = this;
        Bundle extras = getIntent().getExtras();
        requestNo = extras.getString("RequestNo");
        BPNumber=extras.getString("BPNumber");

        init();
    }
    private void init()
    {
        lm = findViewById(R.id.linlaymaterial);
        edmaterialamt = findViewById(R.id.edit_material_amt);
        edit_modify = findViewById(R.id.edit_modify);
        editBPNumber = findViewById(R.id.edit_bpno);
        btnSearch = findViewById(R.id.btnSearch);
        btnSubmit = findViewById(R.id.btnpay);
        editName = findViewById(R.id.edit_name);
        editAddress= findViewById(R.id.edit_address);
        editService= findViewById(R.id.edit_service);
        editServiceAmount= findViewById(R.id.edit_service_amt);
        edittotalcharge = findViewById(R.id.edit_total_charge);
        rbtgenerate = findViewById(R.id.rbtgenerate);
        rbtnextgasbill = findViewById(R.id.rbtnext);
        imgback = findViewById(R.id.imgback);
        rbtgenerate.setChecked(true);
        edit_modify.setText(requestNo);
        editBPNumber.setText(BPNumber);
        editServiceAmount.setText("0");
        edmaterialamt.setText("0");edittotalcharge.setText("0");
        materialIDList = new ArrayList();
        materailNameList = new ArrayList();
        materialQuantityList = new ArrayList();
        progressLoading = new ProgressLoading(context);
        getMaterialList();
        getService();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getService();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getMaterialList(){
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }



            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"modification/materials?schema="+SessionUtil.getGA(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);

                            drawMaterialList(response);
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                    progressLoading.dismiss();
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {};

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }

    }
    private void getService(){
        editName.setText("");
        editAddress.setText("");
        editService.setText("");
        editServiceAmount.setText("");
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }



            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"modification/request/details";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                String first_name = dataObject.getString("first_name");
                                String last_name = dataObject.getString("last_name");
                                String house_number = dataObject.getString("house_number");
                                String locality = dataObject.getString("locality");
                                String town = dataObject.getString("town");
                                String pin_code = dataObject.getString("pin_code");
                                JSONObject serviceObject = jsonObject.getJSONObject("service");
                                String service = serviceObject.getString("service");
                                String service_amount = serviceObject.getString("final_amount");
                                editName.setText(first_name+" "+last_name);
                                editAddress.setText(house_number+"\n"+locality+"\n"+town+"\n"+pin_code);
                                editService.setText(service);
                                editServiceAmount.setText(service_amount);
                                edittotalcharge.setText(Double.valueOf(edmaterialamt.getText().toString())+Double.valueOf(editServiceAmount.getText().toString())+"");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //
                            //loadingDialog.dismiss();

                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                    progressLoading.dismiss();
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("schema", SessionUtil.getGA(context));
                params.put("requestNo", edit_modify.getText().toString().trim());



                //params.put("date_to", "0000-00-00");
                Log.e("Pppppppppppppppppparams",url+" "+ params.toString());
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
    private void drawMaterialList(String response){
        try {
            JSONObject respObject = new JSONObject(response);
             dataArray = respObject.getJSONArray("data");
            for (int j = 0;j< dataArray.length();j++)
            {

                JSONObject jsonObject = dataArray.getJSONObject(j);
                String id = jsonObject.optString("id");
                materialIDList.add(id);
                String material_name = jsonObject.optString("material_name");
                materailNameList.add(material_name);
                String material_cost = jsonObject.optString("material_cost");
                String tax_per = jsonObject.optString("tax_per");
                String uom_name = jsonObject.optString("uom_name");
                String final_cost = jsonObject.optString("final_amount");
                LinearLayout ll = new LinearLayout(this);
             //   ll.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams linearparams = new LinearLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120);
                ll.setLayoutParams(linearparams);
                LinearLayout.LayoutParams fieldparamstv = new LinearLayout.LayoutParams(80, 95);

                fieldparamstv.weight =0.6f;
                // Create TextView
                TextView product = new TextView(this);
                product.setText(material_name.toLowerCase()+"\n"+"(Quantity*Price=1*" +material_cost+ "+GST%="+tax_per+") ");
                product.setTextColor(getResources().getColor(R.color.black));
                product.setLayoutParams(fieldparamstv);
                ll.addView(product);

                // Create TextView
                TextView uom = new TextView(this);
                uom.setText("in " + uom_name + " ");
                ll.addView(uom);

                // Create Button
                final EditText edt = new EditText(this);
                // Give button an ID
                edt.setId(j + 1);
                edt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                edt.setBackgroundResource(R.drawable.edit_text_drawable);
                // set the layoutParams on the button
                LinearLayout.LayoutParams fieldparams = new LinearLayout.LayoutParams(80, 60);

                fieldparams.weight =0.1f;
                edt.setLayoutParams(fieldparams);
                edt.setText("0");
                //  btn.setLayoutParams(params);
                LinearLayout.LayoutParams fieldparamsfctv = new LinearLayout.LayoutParams(80, 60);

                fieldparamsfctv.weight =0.1f;
                TextView final_costtv = new TextView(this);
                final_costtv.setLayoutParams(fieldparamsfctv);
                final int index = j;
                // Set click listener for button
                edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!edt.getText().toString().trim().isEmpty())
                        { // final_costtv.setText(Double.valueOf(final_cost) * Double.valueOf(edt.getText().toString().trim()) + "");
                            final_costtv.setText(Math.round(Double.valueOf(final_cost) * Double.valueOf(edt.getText().toString().trim())*100.00)/100.00+"");
                           totalmaterialamount();
                        }
                        else{
                            edt.setText("0");
                            final_costtv.setText("0");
                            totalmaterialamount();
                        }

                    }
                });

                //Add button to LinearLayout
               // edt.setLayoutParams(fieldparams);
                ll.addView(edt);

                ll.addView(final_costtv);
                //Add button to LinearLayout defined in XML
                lm.addView(ll);
            }
        } catch (JSONException ignored) {
            Log.e("Response JSONException",ignored.toString());
            Toast.makeText(context, " "+ignored, Toast.LENGTH_SHORT).show();

        }
    }
    private void totalmaterialamount(){
        double totalfinalamount = 0.00;
        materialQuantityList.clear();
        for (int i =0 ;i<dataArray.length();i++)
        {
            View view = lm.getChildAt(i);
            EditText editText = view.findViewById(i+1);
            materialQuantityList.add(editText.getText().toString().trim());
            Log.e("quantity",editText.getText().toString());
            try {
              //  totalfinalamount = Math.round(totalfinalamount + (Double.valueOf(dataArray.getJSONObject(i).getString("final_amount"))* Double.valueOf(editText.getText().toString().trim())))*100.00/100.00;
                totalfinalamount = (Math.round((totalfinalamount + (Double.valueOf(dataArray.getJSONObject(i).getString("final_amount"))* Double.valueOf(editText.getText().toString().trim())))*100.00)/100.00);
                Log.e("totalfinalamount",totalfinalamount +"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("materialamount",totalfinalamount +"");
        edmaterialamt.setText(totalfinalamount+"");
        edittotalcharge.setText(Double.valueOf(totalfinalamount)+Double.valueOf(editServiceAmount.getText().toString())+"");
    }
    private void submit(){

        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }



            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"modification/request/save";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            if (response.contains("success"))
                            { Toast.makeText(getApplicationContext(),"Record submitted successfully",Toast.LENGTH_LONG).show();
                              clear();
                            }
                            progressLoading.dismiss();
                            // productModelList.addAll(pipeNo);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.getMessage();
                    progressLoading.dismiss();
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("schema", SessionUtil.getGA(context));
                params.put("modificationRequestNo", edit_modify.getText().toString().trim());
                params.put("materialCharge", edmaterialamt.getText().toString().trim());
                params.put("serviceCharge", editServiceAmount.getText().toString().trim());
                params.put("totalCharge", edittotalcharge.getText().toString().trim());
                params.put("materialId", materialIDList.toString());
                params.put("materialQty", materialQuantityList.toString());
                params.put("materialName", materailNameList.toString());
                if (rbtgenerate.isChecked())
                {params.put("billType", "2");}
                else {  params.put("billType", "1");}
                Log.e("Pppppppppppppppppparams",url+" "+ params.toString());
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
    void clear(){
        edittotalcharge.setText("0");edmaterialamt.setText("0");
        edit_modify.setText("");editName.setText("");editAddress.setText("");
        editBPNumber.setText("");editService.setText("");editServiceAmount.setText("0");
    }
}