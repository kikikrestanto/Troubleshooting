package com.example.troubleshooting.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.troubleshooting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAct extends AppCompatActivity {

    EditText emailET, passET;
    TextView gotoRegister;
    LinearLayout linearLog;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        linearLog = findViewById(R.id.linearLog);
        gotoRegister = findViewById(R.id.gotoRegister);

        mAuth = FirebaseAuth.getInstance();

        //bar transparant

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReg = new Intent(LoginAct.this,RegisterAct.class);
                startActivity(gotoReg);
            }
        });

        linearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String password = passET.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //invalid email patterns set error
                    emailET.setError("Invalid Email");
                    emailET.setFocusable(true);
                }
                else{
                    //valid email pattern
                    loginUser(email,password);
                }
            }
        });

    }

    private void loginUser(String email, String password) {
        final ProgressDialog pd = new ProgressDialog(LoginAct.this);
        pd.setMessage("Logging in ....");
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginAct.this, "Logging in...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginAct.this,DashboardAct.class));
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(LoginAct.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error, get and error show message
                pd.dismiss();
                Toast.makeText(LoginAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go previous activty
        return super.onSupportNavigateUp();
    }
}

