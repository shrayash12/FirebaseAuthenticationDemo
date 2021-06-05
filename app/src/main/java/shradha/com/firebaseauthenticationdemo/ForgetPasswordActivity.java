package shradha.com.firebaseauthenticationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText resetPassword_Email;
    Button btn_ResetPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        resetPassword_Email = findViewById(R.id.email_ResetPass);
        btn_ResetPassword = findViewById(R.id.btn_ResetPassword);
        progressBar = findViewById(R.id.progressBar);
        btn_ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        mAuth = FirebaseAuth.getInstance();

        String email = resetPassword_Email.getText().toString();
        if (email.isEmpty()) {
            resetPassword_Email.setError("email is require");
            resetPassword_Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            resetPassword_Email.setError("please provide valid email");
            resetPassword_Email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(ForgetPasswordActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetPasswordActivity.this, "check your email to reset the password", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Try again! something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
