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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterAct extends AppCompatActivity {

    EditText nameTV, emailTV, passTV;

    TextView registerTV,gotoLogin;
    LinearLayout linearReg;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        passTV = findViewById(R.id.passTV);
        registerTV = findViewById(R.id.registerTV);
        linearReg = findViewById(R.id.linearReg);
        gotoLogin = findViewById(R.id.gotoLogin);

        mAuth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterAct.this, LoginAct.class);
                startActivity(intent);
            }
        });

        linearReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterAct.this);
                pd.setMessage("Please Wait ...");
                pd.show();

                String str_name = nameTV.getText().toString().trim();
                String str_email = emailTV.getText().toString().trim();
                String str_pass = passTV.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
                    emailTV.setError("Invalid Email");
                    emailTV.setFocusable(true);
                }
                else if (str_pass.length()<6){
                    passTV.setError("Password length at least 6 characters");
                    passTV.setFocusable(true);
                }
                else {
                    register(str_name,str_email, str_pass);
                }
            }
        });
    }

    private void register(final String username,final String email,final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterAct.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference().child("Users").child(uid);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("uid",uid);
                            hashMap.put("name", username);
                            hashMap.put("mail", email);
                            hashMap.put("phone", "");
                            hashMap.put("image", "");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterAct.this,DashboardAct.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                }
                            });

                        }else {
                            pd.dismiss();
                            Toast.makeText(RegisterAct.this, "you can't register with name, email and password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go previous activty
        return super.onSupportNavigateUp();
    }
}
