package shradha.com.firebaseauthenticationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText log_EmailAddress;
    EditText log_Password;
    Button btn_LogIn;
    TextView log_Tv_ForgotPassword;
    TextView log_Btn_Register;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log_EmailAddress = findViewById(R.id.log_EmailAddress);
        log_Password = findViewById(R.id.log_Password);
        btn_LogIn = findViewById(R.id.btn_LogIn);
        log_Tv_ForgotPassword = findViewById(R.id.log_Tv_ForgotPassword);
        log_Btn_Register = findViewById(R.id.log_Btn_Register);


        mAuth = FirebaseAuth.getInstance();

        btn_LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
                // Log.d(MainActivity.class.getSimpleName(), "btn_LogIn Clicked");
            }
        });

        log_Btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.log_Btn_Register:
                        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                        //Log.d(MainActivity.class.getSimpleName(), "btn Register clicked");
                        break;
                }
            }
        });
        log_Tv_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgetPasswordActivity.class));
            }
        });
    }

    private void signInUser() {
        String email = log_EmailAddress.getText().toString();
        String password = log_Password.getText().toString();
        if (email.isEmpty()) {
            log_EmailAddress.setError("Email is require");
            log_EmailAddress.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            log_EmailAddress.setError("plese enter valid email");
            log_EmailAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            log_Password.setError("Password is require");
            log_Password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            log_Password.setError("Minimum password length should be 6 characters");
            log_Password.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(log_EmailAddress.getText().toString(),
                log_Password.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(MainActivity.this, "log in successful", Toast.LENGTH_SHORT).show();

                    } else {
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "CHECK YOUR MAIL TO VERIFY YOUR EMAIL ACCOUNT", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Log in failed You are not a registered user please Register first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}