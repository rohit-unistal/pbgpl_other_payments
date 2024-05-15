package unistal.com.pbgplextrapipe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class HomeActivity extends AppCompatActivity {
RelativeLayout relextrapipe,relngc,relmodify,relrep;
ImageView img;
Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        context = this;
        relextrapipe = findViewById(R.id.relep);
        relngc = findViewById(R.id.relngc);
        relmodify = findViewById(R.id.relmodify);
        relrep = findViewById(R.id.relrep);
        relrep.setVisibility(View.INVISIBLE);
        img = findViewById(R.id.imglogout);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logOutDialog();
            }
        });
        relextrapipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });
        relngc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,NGCActivity.class));
            }
        });
        relmodify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ModificationListActivity.class));
            }
        });

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