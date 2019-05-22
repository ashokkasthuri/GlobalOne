package com.ustglobal.myustapp.Dashboard;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.Common.CredentialsManager;
import com.ustglobal.myustapp.Common.Database;
import com.ustglobal.myustapp.Common.URL;
import com.ustglobal.myustapp.Common.UST;
import com.ustglobal.myustapp.Common.USTSharedPreferences;
import com.ustglobal.myustapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Shubham Singhal.
 */

public class Profile extends AppCompatActivity {

    private View rootView;
    private Toolbar toolbar;
    private TextView txt_name, txt_email, txt_phone, txt_UID;
    private ImageView btn_linkedin;
    private Button btn_logout;
    private Auth0 auth0_new;
    private UserProfile userProfile;
    private String token;
    Database db;
    private Handler handler = new Handler();
    private AlertDialog alertDNew;
    private USTSharedPreferences sp = null;


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
//        return rootView;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        sp = new USTSharedPreferences(this);
        init();
        setDimension();
        db = new Database(this);
        auth0_new = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        auth0_new.setOIDCConformant(true);
        showAlertDialog("Fetching Profile Information.\nPlease Wait..");
        new Thread(new ThreadGetProfile()).start();

    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txt_name = (TextView) rootView.findViewById(R.id.txt_name);
        txt_email = (TextView) rootView.findViewById(R.id.txt_email);
        txt_phone = (TextView) rootView.findViewById(R.id.txt_phone);
        txt_UID = (TextView) rootView.findViewById(R.id.txt_UID);

        btn_logout = (Button) rootView.findViewById(R.id.btn_logout);

        btn_linkedin = (ImageView) rootView.findViewById(R.id.btn_linkedin);

        btn_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAgain();
            }
        });
    }

    private void setDimension() {
        DisplayMetrics size = CommonTasks.getScreenSize(this);
        int height = size.heightPixels;
        int width = size.widthPixels;


        txt_name.getLayoutParams().height = (int) (height * 0.0685);
        txt_name.setPadding((int) (width * 0.02), (int) (height * 0.018), 0, 0);

        txt_email.getLayoutParams().height = (int) (height * 0.0685);
        txt_email.setPadding((int) (width * 0.02), (int) (height * 0.018), 0, 0);

        txt_phone.getLayoutParams().height = (int) (height * 0.0685);
        txt_phone.setPadding((int) (width * 0.02), (int) (height * 0.018), 0, 0);

        txt_UID.getLayoutParams().height = (int) (height * 0.0685);
        txt_UID.setPadding((int) (width * 0.02), (int) (height * 0.018), 0, 0);

        btn_linkedin.getLayoutParams().height = (int) (height * 0.08);
        btn_linkedin.getLayoutParams().width = (int) (width * 0.8);

        btn_logout.getLayoutParams().height = (int) (height * 0.08);
        btn_logout.getLayoutParams().width = (int) (width * 0.8);

        txt_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
        txt_email.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
        txt_UID.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
        txt_phone.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
    }


    public void resumeIntent(Intent intent) {
        if (WebAuthProvider.resume(intent)) {
            return;
        }
    }


    private void login() {
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        auth0.setOIDCConformant(true);
        WebAuthProvider.init(auth0)
                .withScheme("demo")
                .start(this, new AuthCallback() {
                    @Override
                    public void onFailure(@NonNull final Dialog dialog) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(final AuthenticationException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Profile.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(@NonNull final Credentials credentials) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CredentialsManager.saveCredentials(Profile.this, credentials);
                                getUserData();
                                btn_linkedin.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
    }

    private void getUserData() {
        // The process to reclaim the User Information is preceded by an Authentication call.
        AuthenticationAPIClient authenticationClient = new AuthenticationAPIClient(auth0_new);
        authenticationClient.userInfo(CredentialsManager.getCredentials(this).getAccessToken()).start(new BaseCallback<UserProfile, AuthenticationException>() {


            @Override
            public void onFailure(AuthenticationException error) {
                System.out.println("Error" + error.toString());
            }

            @Override
            public void onSuccess(final UserProfile payload) {
                userProfile = payload;
                Map<String, Object> str = new HashMap<String, Object>();
                str = payload.getExtraInfo();

                final LinkedInProfileOBJ obj = new LinkedInProfileOBJ();
                obj.setName(payload.getName());
                obj.setEmail_id(payload.getEmail());
                obj.setLinkedin_id(payload.getId());
                obj.setPicture_url(payload.getPictureURL());
                obj.setPublic_profile(str.get("publicProfileUrl").toString());

                final Map<String, Object> finalStr = str;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //  db.addContact(new LinkedInProfileOBJ(payload.getName(),payload.getEmail(),payload.getId(),payload.getPictureURL(), finalStr.get("publicProfileUrl").toString()));
                        db.addContact(obj);
                        btn_logout.setVisibility(View.VISIBLE);
                        List<LinkedInProfileOBJ> contacts = db.getAllProfiles();
                        for (LinkedInProfileOBJ cn : contacts) {
                            String log = "Id: " + cn.getId() + " ,Name: " + cn.getName() + " ,Email: " + cn.getEmail_id();
                            // Writing Contacts to log
                            Log.e("Name: ", log);
                        }
                    }
                });


            }
        });
    }

    private void loginAgain() {
        CredentialsManager.deleteCredentials(this);
        btn_linkedin.setVisibility(View.VISIBLE);
        btn_logout.setVisibility(View.INVISIBLE);
    }


    class ThreadGetProfile implements Runnable {

        @Override
        public void run() {
            getProfile();
        }
    }


    private void getProfile() {
        try {
            String url = URL.getProfile + sp.getPrefrenceStringdata(USTSharedPreferences.IE.uid, null);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response + "");
                    if (response != null) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONObject new_obj = obj.getJSONObject("ustusers");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        alertDNew.cancel();
                                        txt_name.setText(new_obj.getString("firstname") + " " + new_obj.getString("lastname"));
                                        txt_email.setText(new_obj.getString("email"));
                                        txt_phone.setText(new_obj.getString("mobilenumber"));
                                        txt_UID.setText(new_obj.getString("uid"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    alertDNew.cancel();
                                }
                            });
                        }


                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                alertDNew.cancel();
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
                                    alertDNew.cancel();
                                    String error = volleyError.getMessage();
                                    Log.e("Error", error + "'");
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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDNew.cancel();
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
}
