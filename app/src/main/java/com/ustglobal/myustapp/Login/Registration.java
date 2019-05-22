package com.ustglobal.myustapp.Login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.R;

/**
 * Created by Shubham Singhal.
 */

public class Registration extends AppCompatActivity {
    private ImageView logo, img;
    private ImageView img_email,img_password;
    private EditText edt_email,edt_pass;
    private TextView txt_Already,txt_Already1,txt_powered;
    private Button btn_login;
    private RelativeLayout layout_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        setDimension();
    }

    private void init() {
        layout_login = (RelativeLayout)findViewById(R.id.layout_login);

        logo = (ImageView) findViewById(R.id.ust_logo);
        img = (ImageView) findViewById(R.id.img);
        img_email = (ImageView) findViewById(R.id.img_email);
        img_password = (ImageView) findViewById(R.id.img_password);

        btn_login = (Button) findViewById(R.id.btn_login);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_pass = (EditText) findViewById(R.id.edt_pass);
        edt_pass.setTypeface(Typeface.DEFAULT);
        edt_pass.setTransformationMethod(new PasswordTransformationMethod());

        txt_Already = (TextView) findViewById(R.id.txt_Already);
        txt_Already1 = (TextView) findViewById(R.id.txt_Already1);
        txt_powered = (TextView) findViewById(R.id.txt_powered);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Registration.this,CreateProfile.class);
                startActivity(in);
            }
        });

        layout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Registration.this,Login.class);
                startActivity(in);
            }
        });

    }

    private void setDimension() {

        DisplayMetrics size = CommonTasks.getScreenSize(Registration.this);
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

        txt_Already.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.023));
        txt_Already1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.023));
        txt_powered.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
    }
}
