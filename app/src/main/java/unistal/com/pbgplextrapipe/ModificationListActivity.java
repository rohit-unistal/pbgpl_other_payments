package unistal.com.pbgplextrapipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

public class ModificationListActivity extends AppCompatActivity {
Context context;
Button btnGetList,btnRefreshList,btnRequest;
ImageView imgback;
ListView listViewModificationRequest;
CustomAdapter customAdapter;
ArrayList<ModificationServiceModel> ModificationServiceList;
ProgressLoading progressLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_list);
        getSupportActionBar().hide();
        context = this;
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getModificationRequestList();
    }

    private void init(){
        progressLoading = new ProgressLoading(context);
        btnGetList = findViewById(R.id.btngetlist);
        btnRefreshList = findViewById(R.id.btnrefreshlist);
        btnRequest = findViewById(R.id.btnrequest);
        imgback = findViewById(R.id.imgback);
        ModificationServiceList = new ArrayList<>();
        listViewModificationRequest = findViewById(R.id.lvmodificationrequest);
        btnGetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getModificationRequestList();
            }
        });
        btnRefreshList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getModificationRequestList();
            }
        });
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModificationListActivity.this,RequestActivity.class));
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
      //  getModificationJSONRequestList();
    }

    private void getModificationRequestList(){
        ModificationServiceList.clear();
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }



            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = AppConstants.APP_BASE_URL+"modification/request/lists?schema="+SessionUtil.getGA(context);
            // Request a string response from the provided URL.http://142.79.231.30:8084/api/modification/request/lists?schema=sonipat
            Log.e("url",url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i =0 ; i <jsonArray.length();i++)
                                {
                                    ModificationServiceList.add(new ModificationServiceModel(jsonArray.getJSONObject(i).getString("id"),
                                            jsonArray.getJSONObject(i).getString("service_type"),jsonArray.getJSONObject(i).getString("date_of_request"),
                                            jsonArray.getJSONObject(i).getString("message"),jsonArray.getJSONObject(i).optString("file"),
                                            jsonArray.getJSONObject(i).getString("status"),jsonArray.getJSONObject(i).getString("request_number"),
                                            jsonArray.getJSONObject(i).getString("dma_id"),jsonArray.getJSONObject(i).getString("maintenance_type"),
                                            jsonArray.getJSONObject(i).getString("service_name"),jsonArray.getJSONObject(i).getString("bp_number")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //loadingDialog.dismiss();
                            customAdapter= new CustomAdapter(ModificationServiceList,getApplicationContext());

                            listViewModificationRequest.setAdapter(customAdapter);
                            listViewModificationRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    ModificationServiceModel dataModel= ModificationServiceList.get(position);
                                    if (ModificationServiceList.get(position).getStatus().equals("0"))
                                    {startActivity(new Intent(ModificationListActivity.this,ModificationActivity.class)
                                            .putExtra("RequestNo",ModificationServiceList.get(position).getRequestNumber())
                                            .putExtra("BPNumber",ModificationServiceList.get(position).getBp_number()));}
                                    else{
                                        Toast.makeText(getApplicationContext(),ModificationServiceList.get(position).getRequestNumber() +"is already submitted",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
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
            }) {
                };

            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);
        } else {
            DialogUtil.showNoConnectionDialog(this);
        }

    }

    public class CustomAdapter extends ArrayAdapter<ModificationServiceModel> implements View.OnClickListener{

        private ArrayList<ModificationServiceModel> dataSet;
        Context mContext;

        // View lookup cache
        private  class ViewHolder {
            TextView txtName;
            TextView txtType;
            TextView txtVersion;
            TextView txtStatus;
            TextView txtBPNumber;

        }

        public CustomAdapter(ArrayList<ModificationServiceModel> data, Context context) {
            super(context, R.layout.requestlist, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public void onClick(View v) {

            int position=(Integer) v.getTag();
            Log.e("position", position+"");
            Object object= getItem(position);
           // ModificationServiceModel dataModel=(ModificationServiceModel) object;
            Toast.makeText(getApplicationContext(),ModificationServiceList.get(position).getRequestNumber(),Toast.LENGTH_LONG).show();
            /*switch (v.getId())
            {
                case R.id.item_info:
                    Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    break;
            }*/
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ModificationServiceModel dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.requestlist, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.tvRequestID);
                viewHolder.txtType = (TextView) convertView.findViewById(R.id.tvServiceType);
             //   viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.tvMessage);
                viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.tvStatus);
                viewHolder.txtBPNumber = (TextView) convertView.findViewById(R.id.tvBPNo);
             //   viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

           /* Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);*/
            lastPosition = position;

            viewHolder.txtName.setText(dataModel.getRequestNumber());
            viewHolder.txtBPNumber.setText(dataModel.getBp_number());
            viewHolder.txtType.setText(dataModel.getServiceName());
           // viewHolder.txtVersion.setText(dataModel.getMessage());
            if(dataModel.getStatus().equals("0"))
            { viewHolder.txtStatus.setText("pending");
               viewHolder.txtStatus.setBackgroundColor(getResources().getColor(R.color.orange));}
            else{
                viewHolder.txtStatus.setText("submitted");
                viewHolder.txtStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        }
            // Return the completed view to render on screen
            return convertView;
        }
    }

}