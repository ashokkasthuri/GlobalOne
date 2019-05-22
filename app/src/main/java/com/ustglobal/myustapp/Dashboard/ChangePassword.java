package com.ustglobal.myustapp.Dashboard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Common.URL;
import com.ustglobal.myustapp.Common.USTSharedPreferences;
import com.ustglobal.myustapp.Login.Login;
import com.ustglobal.myustapp.Login.ResetPassword;
import com.ustglobal.myustapp.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Shubham Singhal.
 */

public class ChangePassword extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView logo, img;
    private TextView txt_ResetPassword, txt_powered;
    private EditText edt_NewPass, edt_ReNewPass, edt_oldPass;
    private Button btn_submit;
    private ProgressDialog progress;
    private Handler handler = new Handler();
    private USTSharedPreferences sp = null;

    private String oldPass, newPass, confirmNewPass, uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sp = new USTSharedPreferences(getApplicationContext());
        progress = new ProgressDialog(this);
        progress.setMessage("Processig your request.\nPlease wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //   progress.setProgress(150);
        progress.setCancelable(false);
        uid = sp.getPrefrenceStringdata(USTSharedPreferences.IE.uid, null);
        init();
        setDimension();
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        logo = (ImageView) findViewById(R.id.ust_logo);
        img = (ImageView) findViewById(R.id.img);

        txt_ResetPassword = (TextView) findViewById(R.id.txt_ResetPassword);
        txt_powered = (TextView) findViewById(R.id.txt_powered);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        edt_oldPass = (EditText) findViewById(R.id.edt_oldPass);
        edt_NewPass = (EditText) findViewById(R.id.edt_NewPass);
        edt_ReNewPass = (EditText) findViewById(R.id.edt_ReNewPass);

        edt_NewPass.setTypeface(Typeface.DEFAULT);
        edt_NewPass.setTransformationMethod(new PasswordTransformationMethod());
        edt_oldPass.setTypeface(Typeface.DEFAULT);
        edt_oldPass.setTransformationMethod(new PasswordTransformationMethod());
        edt_ReNewPass.setTypeface(Typeface.DEFAULT);
        edt_ReNewPass.setTransformationMethod(new PasswordTransformationMethod());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
//                Intent in = new Intent(ResetPassword.this,Login.class);
//                startActivity(in);
//                finish();
            }
        });
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(ChangePassword.this);
        int height = size.heightPixels;
        int width = size.widthPixels;

        logo.getLayoutParams().height = (int) (height * 0.14);
        logo.getLayoutParams().width = (int) (width * 0.6);

        img.getLayoutParams().height = (int) (height * 0.13);
        img.getLayoutParams().width = (int) (height * 0.13);

        btn_submit.getLayoutParams().height = (int) (height * 0.089);

        edt_oldPass.getLayoutParams().height = (int) (height * 0.06);
        edt_oldPass.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_NewPass.getLayoutParams().height = (int) (height * 0.06);
        edt_ReNewPass.getLayoutParams().height = (int) (height * 0.06);
        edt_NewPass.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_ReNewPass.setPadding((int) (width * 0.03), 0, 0, 0);

        txt_ResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.030));

        txt_powered.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
    }

    private void validateData() {
        oldPass = edt_oldPass.getText().toString().trim();
        newPass = edt_NewPass.getText().toString().trim();
        confirmNewPass = edt_ReNewPass.getText().toString().trim();

        if (oldPass.length() == 0)
            edt_oldPass.setError("Invalid Password");
        else if (newPass.length() == 0)
            edt_NewPass.setError("Invalid Password");
        else if (!confirmNewPass.equalsIgnoreCase(newPass))
            edt_ReNewPass.setError("Passwords do not match");
        else {
            progress.show();
            new Thread(new ThreadChangePassword()).start();
        }
    }


    class ThreadChangePassword implements Runnable {

        @Override
        public void run() {
            changePassword();
        }
    }

    private void changePassword() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = URL.changePassword;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", uid + "@ust-global.com");
            jsonBody.put("password", oldPass);
            jsonBody.put("newpassword", confirmNewPass);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String message = obj.getString("MSG");
                            if (message.equalsIgnoreCase("Sucess")) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.cancel();
                                        showAlertDialogNew("Password changed successfully.", 1);
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.cancel();
                                        showAlertDialogNew("Unable to change password.", 0);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progress.cancel();
                                    showAlertDialogNew("Unable to change password.", 0);
                                }
                            });
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progress.cancel();
                                showAlertDialogNew("Something went wrong.\nPlease try again.", 0);
                            }
                        });
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.cancel();
                            showAlertDialogNew("Something went wrong.\nPlease try again.", 0);
                        }
                    });
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progress.cancel();
                    showAlertDialogNew("Something went wrong.\nPlease try again.", 0);
                }
            });
        }
    }

    private void showAlertDialogNew(String message, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (id == 1) {
                    Intent in = new Intent(ChangePassword.this, Dashboard.class);
                    startActivity(in);
                    finish();
                }
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
