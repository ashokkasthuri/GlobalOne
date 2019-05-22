package com.ustglobal.myustapp.Login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ustglobal.myustapp.Common.CommonTasks;
import com.ustglobal.myustapp.R;

/**
 * Created by Shubham Singhal.
 */

public class Register extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView img_register,img_submit;
    private TextView txt_submit,txt_register;
    private EditText edt_Fname,edt_Lname,edt_email,edt_Number,edt_UserId;
    private RelativeLayout layout_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setDimension();
    }

    private void init(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        layout_button = (RelativeLayout)findViewById(R.id.layout_button);

        img_register = (ImageView)findViewById(R.id.img_register);
        img_submit = (ImageView)findViewById(R.id.img_submit);

        edt_email = (EditText)findViewById(R.id.edt_email);
        edt_Fname = (EditText)findViewById(R.id.edt_Fname);
        edt_Lname = (EditText)findViewById(R.id.edt_Lname);
        edt_Number = (EditText)findViewById(R.id.edt_Number);
        edt_UserId = (EditText)findViewById(R.id.edt_UserId);


        txt_register = (TextView)findViewById(R.id.txt_register);
        txt_submit = (TextView)findViewById(R.id.txt_submit);

    }

    private void setDimension(){
        DisplayMetrics size = CommonTasks.getScreenSize(Register.this);
        int height = size.heightPixels;
        int width = size.widthPixels;


        img_register.getLayoutParams().height = (int)(height * 0.05);
        img_register.getLayoutParams().width = (int)(height * 0.05);

        img_submit.getLayoutParams().height = (int) (height * 0.035);
        img_submit.getLayoutParams().width = (int) (height * 0.035);

        edt_email.getLayoutParams().height = (int) (height * 0.06);
        edt_Fname.getLayoutParams().height = (int) (height * 0.06);
        edt_Lname.getLayoutParams().height = (int) (height * 0.06);
        edt_Number.getLayoutParams().height = (int) (height * 0.06);
        edt_UserId.getLayoutParams().height = (int) (height * 0.06);

        edt_email.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_Fname.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_Lname.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_Number.setPadding((int) (width * 0.03), 0, 0, 0);
        edt_UserId.setPadding((int) (width * 0.03), 0, 0, 0);

        layout_button.getLayoutParams().width = (int)(width*0.35);

        txt_register.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.026));
        txt_submit.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (int) (height * 0.024));
    }
}
