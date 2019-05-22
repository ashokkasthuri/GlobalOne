package com.ustglobal.myustapp.Login;

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
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.ustglobal.myustapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Shubham Singhal.
 */

public class CreateProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView img_submit;
    private TextView txt_submit;
    private EditText edt_Fname, edt_Lname, edt_Number, edt_UserId, edt_Pass, edt_ConfirmPass;  //edt_email
    private RelativeLayout layout_button;
    private Handler handler = new Handler();
    private AlertDialog alertDNew;
    private USTSharedPreferences sp = null;
    private ProgressDialog progress;

    private String fName;
    private String lName;
    //  private String email;
    private String number;
    private String uid;
    private String pass;
    private String confirmPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        sp = new USTSharedPreferences(getApplicationContext());
        progress = new ProgressDialog(this);
        progress.setMessage("Signing Up.\nPlease Wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //  progress.setProgress(150);
        progress.setCancelable(false);
        init();
        setDimension();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Create Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        layout_button = (RelativeLayout) findViewById(R.id.layout_button);

        img_submit = (ImageView) findViewById(R.id.img_submit);

        //    edt_email = (EditText) findViewById(R.id.edt_email);
        edt_Fname = (EditText) findViewById(R.id.edt_Fname);
        edt_Lname = (EditText) findViewById(R.id.edt_Lname);
        edt_Number = (EditText) findViewById(R.id.edt_Number);
        edt_UserId = (EditText) findViewById(R.id.edt_UserId);
        edt_Pass = (EditText) findViewById(R.id.edt_Pass);
        edt_ConfirmPass = (EditText) findViewById(R.id.edt_ConfirmPass);
        edt_Pass.setTypeface(Typeface.DEFAULT);
        edt_Pass.setTransformationMethod(new PasswordTransformationMethod());
        edt_ConfirmPass.setTypeface(Typeface.DEFAULT);
        edt_ConfirmPass.setTransformationMethod(new PasswordTransformationMethod());

        txt_submit = (TextView) findViewById(R.id.txt_submit);

        layout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(CreateProfile.this);
        int height = size.heightPixels;
        int width = size.widthPixels;


        img_submit.getLayoutParams().height = (int) (height * 0.035);
        img_submit.getLayoutParams().width = (int) (height * 0.035);

        //    edt_email.getLayoutParams().height = (int) (height * 0.068);
        edt_Fname.getLayoutParams().height = (int) (height * 0.068);
        edt_Lname.getLayoutParams().height = (int) (height * 0.068);
        edt_Number.getLayoutParams().height = (int) (height * 0.068);
        edt_UserId.getLayoutParams().height = (int) (height * 0.068);
        edt_Pass.getLayoutParams().height = (int) (height * 0.068);
        edt_ConfirmPass.getLayoutParams().height = (int) (height * 0.068);

        //     edt_email.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_Fname.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_Lname.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_Number.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_UserId.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_Pass.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_ConfirmPass.setPadding((int) (width * 0.03), 0, 0, 0);

        layout_button.getLayoutParams().width = (int) (width * 0.35);

        txt_submit.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.024));
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean uidCheck(String uid) {

        if (uid.length() != 6)
            return false;
        else {
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
        fName = edt_Fname.getText().toString();
        lName = edt_Lname.getText().toString();
        //    email = edt_email.getText().toString();
        number = edt_Number.getText().toString();
        uid = edt_UserId.getText().toString();
        pass = edt_Pass.getText().toString();
        confirmPass = edt_ConfirmPass.getText().toString();

        if (fName.length() == 0)
            edt_Fname.setError("Invalid First Name");
        else if (lName.length() == 0)
            edt_Fname.setError("Invalid Last Name");
//        else if (!isEmailValid(email) || !email.contains("ust-global"))
//            edt_email.setError("Email Invalid");
        else if (number.length() == 0)
            edt_Number.setError("Invalid Number");
        else if (!uidCheck(uid))
            edt_UserId.setError("Invalid ID");
        else if (pass.length() == 0)
            edt_Pass.setError("Invalid Password");
        else if (!confirmPass.equalsIgnoreCase(pass))
            edt_ConfirmPass.setError("Passwords dont match");
        else {
            progress.show();
            //showAlertDialog("Signing Up.\nPlease Wait..");
            new Thread(new ThreadRegister()).start();
        }
    }

    class ThreadRegister implements Runnable {

        @Override
        public void run() {
            register();
        }
    }

    private void register() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = URL.register;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", uid + "@ust-global.com");
            jsonBody.put("firstname", fName);
            jsonBody.put("lastname", lName);
            jsonBody.put("uid", uid);
            jsonBody.put("password", pass);
            jsonBody.put("confirmpass", confirmPass);
            jsonBody.put("mobilenumber", number);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean success = obj.getBoolean("success");
                            final String message = obj.getString("message");
                            if (success) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //    alertDNew.cancel();
                                        progress.cancel();
                                        sp.setPrefrenceBooleandata(USTSharedPreferences.IE.user_loggedIn, true);
                                        sp.setPrefrenceStringdata(USTSharedPreferences.IE.email, uid + "@ust-global.com");
                                        showAlertDialogNew("Email Verification link has been sent to your registered email address.Please verify your email id to continue.", 0);

                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // alertDNew.cancel();
                                        progress.cancel();
                                        showAlertDialogNew(message, 1);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //  alertDNew.cancel();
                                    progress.cancel();
                                    showAlertDialogNew("Something went wrong.\nPlease try again.", 1);
                                }
                            });
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // alertDNew.cancel();
                                progress.cancel();
                                showAlertDialogNew("Something went wrong.\nPlease try again.", 1);
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
                            //  alertDNew.cancel();
                            progress.cancel();
                            showAlertDialogNew("Something went wrong.\nPlease try again.", 1);
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
        } catch (JSONException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // alertDNew.cancel();
                    progress.cancel();
                    showAlertDialogNew("Something went wrong.\nPlease try again.", 1);
                }
            });
        }

    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        alertDNew = builder.create();
        alertDNew.show();
    }

    private void showAlertDialogNew(String message, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (i == 0) {
                    Intent in = new Intent(CreateProfile.this, Login.class);
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
