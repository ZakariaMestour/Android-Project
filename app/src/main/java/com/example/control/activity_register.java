package com.example.control;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_register extends AppCompatActivity {
    EditText etMail, etPassword, etPassword1;
    Button bRegister;
    FirebaseAuth myAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etMail=(EditText) findViewById(R.id.email);
        etPassword=(EditText) findViewById(R.id.password);
        etPassword1=(EditText)findViewById(R.id.confirm_password);
        bRegister=(Button)findViewById(R.id.button);
        myAuth=FirebaseAuth.getInstance();
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=etMail.getText().toString();
                String password=etPassword.getText().toString();
                String password1=etPassword1.getText().toString();
                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields&quot",
                    Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields&quot",
                    Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    Toast.makeText(getApplicationContext(),"Please confirm your password",
                    Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",

                    Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(password1)){
                    Toast.makeText(getApplicationContext(),"Please enter correct password",
                    Toast.LENGTH_SHORT).show();
                    return;
                }
                signUp(mail,password);

            }
        });


    }
    public void signUp(String mail, String password){
        myAuth.createUserWithEmailAndPassword(mail,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Registration Successful! inscription réussi! التسجيل ناجح! 註冊成功 ",
                    Toast.LENGTH_LONG).show();
                    startActivity(new
                            Intent(activity_register.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Sign up faile" + task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}