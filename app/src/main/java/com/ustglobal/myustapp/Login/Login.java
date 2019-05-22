package com.ustglobal.myustapp.Login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Common.Constants;
import com.ustglobal.myustapp.Common.URL;
import com.ustglobal.myustapp.Common.UST;
import com.ustglobal.myustapp.Common.USTSharedPreferences;
import com.ustglobal.myustapp.Dashboard.BlogsOBJ;
import com.ustglobal.myustapp.Dashboard.Dashboard;
import com.ustglobal.myustapp.Dashboard.HomeAdapter;
import com.ustglobal.myustapp.Dashboard.SubCategoryOBJ;
import com.ustglobal.myustapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Shubham Singhal.
 */

public class Login extends AppCompatActivity {

    private ImageView logo, img;
    private ImageView img_email, img_password;
    private EditText edt_email, edt_pass;
    private TextView txt_forgotPass, txt_powered, txt_Account, txt_Account1;
    private Button btn_login;
    private RelativeLayout layout_signUp;
    private Handler handler = new Handler();
    private AlertDialog alertDNew;
    private USTSharedPreferences sp = null;
    private ProgressDialog progress;

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = new USTSharedPreferences(getApplicationContext());
        progress = new ProgressDialog(this);
        progress.setMessage("Logging In\nPlease wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //   progress.setProgress(150);
        progress.setCancelable(false);
     //   new Thread(new ThreadGetVersion()).start();
        init();
        setDimension();
    }

    private void init() {
        layout_signUp = (RelativeLayout) findViewById(R.id.layout_signUp);

        logo = (ImageView) findViewById(R.id.ust_logo);
        img = (ImageView) findViewById(R.id.img);
        img_email = (ImageView) findViewById(R.id.img_email);
        img_password = (ImageView) findViewById(R.id.img_password);

        btn_login = (Button) findViewById(R.id.btn_login);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_pass = (EditText) findViewById(R.id.edt_pass);
        edt_pass.setTypeface(Typeface.DEFAULT);
        edt_pass.setTransformationMethod(new PasswordTransformationMethod());

        txt_forgotPass = (TextView) findViewById(R.id.txt_forgotPass);
        txt_powered = (TextView) findViewById(R.id.txt_powered);
        txt_Account = (TextView) findViewById(R.id.txt_Account);
        txt_Account1 = (TextView) findViewById(R.id.txt_Account1);

        SpannableString str = new SpannableString("Sign Up");
        str.setSpan(new UnderlineSpan(), 0, str.length(), Spanned.SPAN_PARAGRAPH);
        txt_Account1.setText(str);

        layout_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Login.this, CreateProfile.class);
                startActivity(in);
            }
        });

        txt_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Login.this, ForgotPassword.class);
                startActivity(in);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
//                Intent in = new Intent(Login.this, Dashboard.class);
//                startActivity(in);
//                finish();
            }
        });

    }

    private void setDimension() {

        DisplayMetrics size = CommonTasks.getScreenSize(Login.this);
        int height = size.heightPixels;
        int width = size.widthPixels;

        logo.getLayoutParams().height = (int) (height * 0.2);
        logo.getLayoutParams().width = (int) (width * 0.8);

        img.getLayoutParams().height = (int) (height * 0.2);
        img.getLayoutParams().width = (int) (height * 0.2);

        btn_login.getLayoutParams().height = (int) (height * 0.089);

        img_email.getLayoutParams().height = (int) (height * 0.06);
        img_email.getLayoutParams().width = (int) (height * 0.068);
        img_password.getLayoutParams().height = (int) (height * 0.06);
        img_password.getLayoutParams().width = (int) (height * 0.068);

        edt_email.getLayoutParams().height = (int) (height * 0.06);
        edt_pass.getLayoutParams().height = (int) (height * 0.06);

        edt_email.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_pass.setPadding((int) (width * 0.03), 0, 0, 0);

        txt_forgotPass.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.023));
        txt_powered.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
        txt_Account.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.023));
        txt_Account1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.023));
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
        email = edt_email.getText().toString();
        password = edt_pass.getText().toString();


        if (!uidCheck(email)) {
            edt_email.setError("Invalid ID");
        } else if (password.length() == 0 || password.length() > 50)
            edt_pass.setError("Invalid password");
        else {
            progress.show();
            // showAlertDialog("Logging In\nPlease wait..");
            new Thread(new ThreadLogin()).start();
        }
    }

    class ThreadLogin implements Runnable {

        @Override
        public void run() {
            login();
        }
    }

    private void login() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = URL.login;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email + "@ust-global.com");
            jsonBody.put("password", password);
            jsonBody.put("usertokenid", sp.getPrefrenceStringdata(USTSharedPreferences.IE.firebase_token, null));
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
                                final String uid = obj.getString("uid");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //  alertDNew.cancel();
                                        progress.cancel();
                                        sp.setPrefrenceStringdata(USTSharedPreferences.IE.uid, uid);
                                        sp.setPrefrenceBooleandata(USTSharedPreferences.IE.user_loggedIn, true);
                                        sp.setPrefrenceStringdata(USTSharedPreferences.IE.email, email + "@ust-global.com");
                                        Intent in = new Intent(Login.this, Dashboard.class);
                                        startActivity(in);
                                        finish();
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //alertDNew.cancel();
                                        progress.cancel();
                                        showAlertDialogNew(message, 0);
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
                                    showAlertDialogNew("Incorrect username or password.\nPlease try again.", 0);
                                }
                            });
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //  alertDNew.cancel();
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
                            // alertDNew.cancel();
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
        } catch (JSONException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //  alertDNew.cancel();
                    progress.cancel();
                    showAlertDialogNew("Something went wrong.\nPlease try again.", 0);
                }
            });
        }
    }


    class ThreadGetVersion implements Runnable {

        @Override
        public void run() {
            getVersion();
        }
    }

    private void getVersion() {
        try {
            String url = URL.getAppVersion;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response + "");
                    if (response != null) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("versionctrl");
                            JSONObject obj_new = arr.getJSONObject(0);
                            String number = obj_new.getString("number");
                            PackageManager manager = getPackageManager();
                            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                            String app_version_name = String.valueOf(info.versionName);
                            if (!app_version_name.equalsIgnoreCase(number)) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAlertDialogNew1("This version is not supported anymore. Please update to latest version.", 1);
                                    }
                                });
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // alertDNew.cancel();

                            }
                        });
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError volleyError) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    20 * 1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            UST.getInstance().addToRequestQueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        alertDNew = builder.create();
        alertDNew.show();
    }

    private void showAlertDialogNew1(String message, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                if (i == 1) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.internal.ustglobal")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.internal.ustglobal")));
                    }
                }
                return;
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
                System.exit(0);
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void showAlertDialogNew(String message, final int i) {
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

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new ThreadGetVersion()).start();
    }
}
