package shradha.com.firebaseauthenticationdemo;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText editResetPasswordEmail;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editResetPasswordEmail = findViewById(R.id.editResetPassword);
        Button buttonResetPassword = findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(v -> resetPassword());
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.forget_password_screen);
    }

    private void resetPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = editResetPasswordEmail.getText().toString();
        if (email.isEmpty()) {
            editResetPasswordEmail.setError(getString(R.string.email_is_require));
            editResetPasswordEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editResetPasswordEmail.setError(getString(R.string.please_provide_email));
            editResetPasswordEmail.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(ForgetPasswordActivity.this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgetPasswordActivity.this, getString(R.string.check_your_email_reset), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ForgetPasswordActivity.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(this, e ->
                Toast.makeText(
                        ForgetPasswordActivity.this,
                        e.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show()
        );
    }
}
