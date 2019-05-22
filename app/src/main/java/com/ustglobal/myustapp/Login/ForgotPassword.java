package com.ustglobal.myustapp.Login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.ustglobal.myustapp.Dashboard.Dashboard;
import com.ustglobal.myustapp.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Shubham Singhal.
 */

public class ForgotPassword extends AppCompatActivity {

    private ImageView logo, img, btn_cross;
    private ImageView img_email;
    private EditText edt_email;
    private TextView txt_powered, txt_forgot;
    private Button btn_submit;
    private ProgressDialog progress;
    private Handler handler = new Handler();

    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        progress = new ProgressDialog(this);
        progress.setMessage("Processig your request.\nPlease wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //   progress.setProgress(150);
        progress.setCancelable(false);
        init();
        setDimension();
    }

    private void init() {

        logo = (ImageView) findViewById(R.id.ust_logo);
        img = (ImageView) findViewById(R.id.img);
        img_email = (ImageView) findViewById(R.id.img_email);
        btn_cross = (ImageView) findViewById(R.id.btn_cross);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        edt_email = (EditText) findViewById(R.id.edt_email);

        txt_powered = (TextView) findViewById(R.id.txt_powered);
        txt_forgot = (TextView) findViewById(R.id.txt_forgot);

        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ForgotPassword.this, Login.class);
                startActivity(in);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
//                Intent in = new Intent(ForgotPassword.this,OTP.class);
//                startActivity(in);
//                finish();
            }
        });

    }

    private void setDimension() {

        DisplayMetrics size = CommonTasks.getScreenSize(ForgotPassword.this);
        int height = size.heightPixels;
        int width = size.widthPixels;

        logo.getLayoutParams().height = (int) (height * 0.16);
        logo.getLayoutParams().width = (int) (width * 0.7);

        btn_cross.getLayoutParams().height = (int) (height * 0.038);
        btn_cross.getLayoutParams().width = (int) (height * 0.038);

        img.getLayoutParams().height = (int) (height * 0.15);
        img.getLayoutParams().width = (int) (height * 0.15);

        btn_submit.getLayoutParams().height = (int) (height * 0.089);

        img_email.getLayoutParams().height = (int) (height * 0.06);
        img_email.getLayoutParams().width = (int) (height * 0.068);

        edt_email.getLayoutParams().height = (int) (height * 0.06);

        edt_email.setPadding((int) (width * 0.03), 0, 0, 0);

        txt_powered.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
        txt_forgot.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
    }


    private boolean uidCheck(String uid) {

        if (uid.length() != 6) {
            return false;
        } else {
            String str = Character.toString(uid.charAt(0));
            if (str.equalsIgnoreCase("U")) {
                String str1 = uid.substring(1, uid.length());
                if (str1.matches("^[0-9]*$")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private void validateData() {

        uid = edt_email.getText().toString().trim();

        if (!uidCheck(uid))
            edt_email.setError("Invalid ID");
        else {
            progress.show();
            new Thread(new ThreadForgot()).start();
        }

    }


    class ThreadForgot implements Runnable {

        @Override
        public void run() {
            forgotPass();
        }
    }


    private void forgotPass() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = URL.forgotPassword;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", uid + "@ust-global.com");
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
                                        showAlertDialogNew("OTP has been sent to your UST Email ID.", 1);
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.cancel();
                                        showAlertDialogNew("UST ID not registered.\nPlease register first.", 0);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progress.cancel();
                                    showAlertDialogNew("Incorrect UST ID", 0);
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
                    Intent in = new Intent(ForgotPassword.this, OTP.class);
                    in.putExtra("uid", uid);
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
