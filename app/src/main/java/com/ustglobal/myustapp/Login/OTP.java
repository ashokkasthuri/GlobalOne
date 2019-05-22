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
import com.ustglobal.myustapp.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Shubham Singhal.
 */

public class OTP extends AppCompatActivity {

    private ImageView logo, img;
    private TextView txt_otp, txt, txt_powered;
    private EditText edt_otp;
    private Button btn_submit;
    private ProgressDialog progress;
    private Handler handler = new Handler();

    private String otp,uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        progress = new ProgressDialog(this);
        progress.setMessage("Processig your request.\nPlease wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        uid = getIntent().getStringExtra("uid");
        init();
        setDimension();
    }

    private void init() {

        logo = (ImageView) findViewById(R.id.ust_logo);
        img = (ImageView) findViewById(R.id.img);

        txt = (TextView) findViewById(R.id.txt);
        txt_otp = (TextView) findViewById(R.id.txt_otp);
        txt_powered = (TextView) findViewById(R.id.txt_powered);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        edt_otp = (EditText) findViewById(R.id.edt_otp);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
//                Intent in = new Intent(OTP.this, ResetPassword.class);
//                startActivity(in);
//                finish();
            }
        });

    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(OTP.this);
        int height = size.heightPixels;
        int width = size.widthPixels;

        logo.getLayoutParams().height = (int) (height * 0.14);
        logo.getLayoutParams().width = (int) (width * 0.6);

        img.getLayoutParams().height = (int) (height * 0.1);
        img.getLayoutParams().width = (int) (height * 0.1);

        btn_submit.getLayoutParams().height = (int) (height * 0.089);

        edt_otp.getLayoutParams().height = (int) (height * 0.06);
        //  edt_otp.setPadding((int) (width * 0.03), 0, 0, 0);

        txt_otp.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.030));
        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.025));

        txt_powered.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
    }


    private void validateData() {
        otp = edt_otp.getText().toString().trim();

        if (otp.length() == 0)
            edt_otp.setError("Invalid OTP");
        else {
            progress.show();
            new Thread(new ThreadValidateOTP()).start();
        }
    }

    class ThreadValidateOTP implements Runnable {

        @Override
        public void run() {
            validateOTP();
        }
    }

    private void validateOTP() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = URL.validate_otp;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", uid + "@ust-global.com");
            jsonBody.put("OTP",otp);
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
                                        Intent in = new Intent(OTP.this, ResetPassword.class);
                                        in.putExtra("uid", uid);
                                        startActivity(in);
                                        finish();
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.cancel();
                                        showAlertDialogNew("Invalid OTP");
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progress.cancel();
                                    showAlertDialogNew("Invalid OTP");
                                }
                            });
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progress.cancel();
                                showAlertDialogNew("Something went wrong.\nPlease try again.");
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
                            showAlertDialogNew("Something went wrong.\nPlease try again.");
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
                    showAlertDialogNew("Something went wrong.\nPlease try again.");
                }
            });
        }
    }


    private void showAlertDialogNew(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
