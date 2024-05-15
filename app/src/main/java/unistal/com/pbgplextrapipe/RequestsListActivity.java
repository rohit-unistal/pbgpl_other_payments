package unistal.com.pbgplextrapipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RequestsListActivity extends AppCompatActivity {
Context context;
Button btnRequest;
ProgressLoading progressLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_list);
        context = this;
        btnRequest = findViewById(R.id.btnreqlist);
        progressLoading = new ProgressLoading(this);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
    }
    private void getList()
    {
        if (DialogUtil.checkInternetConnection(this)) {
            // loadingDialog.onShow();
            if (!progressLoading.isShowing()) {
                progressLoading.onShow();
            }



            RequestQueue queue = Volley.newRequestQueue(this);

            final String url = "http://142.79.231.30:8084/api/modification/request/lists?schema=sonipat";//+SessionUtil.getGA(context);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);



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
}