package shradha.com.firebaseauthenticationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static shradha.com.firebaseauthenticationdemo.SignUpActivity.MINIMUM_PASSWORD_LENGTH;

public class SignInActivity extends AppCompatActivity {
    private EditText editEmailAddress;
    private EditText editPassWord;
    private Button buttonLogin;
    private TextView tvForgotPassword;
    private TextView tvRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        findViews();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        }
        buttonLogin.setOnClickListener(v -> signInUser());
        tvRegister.setOnClickListener(v -> {
            if (v.getId() == R.id.log_Btn_Register) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class)));
        Objects.requireNonNull(getSupportActionBar()).setTitle("SignIn Screen");
    }

    private void findViews() {
        editEmailAddress = findViewById(R.id.log_EmailAddress);
        editPassWord = findViewById(R.id.log_Password);
        buttonLogin = findViewById(R.id.btn_LogIn);
        tvForgotPassword = findViewById(R.id.log_Tv_ForgotPassword);
        tvRegister = findViewById(R.id.log_Btn_Register);
    }

    private void signInUser() {
        String email = editEmailAddress.getText().toString();
        String password = editPassWord.getText().toString();
        if (email.isEmpty()) {
            editEmailAddress.setError(getString(R.string.email_required));
            editEmailAddress.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmailAddress.setError(getString(R.string.please_enter_valid_email));
            editEmailAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPassWord.setError(getString(R.string.password_is_required));
            editPassWord.requestFocus();
            return;
        }
        if (password.length() < MINIMUM_PASSWORD_LENGTH) {
            editPassWord.setError(getString(R.string.minimum_password_length));
            editPassWord.requestFocus();
            return;
        }

        auth.signInWithEmailAndPassword(editEmailAddress.getText().toString(),
                editPassWord.getText().toString()).addOnCompleteListener(SignInActivity.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (Objects.requireNonNull(firebaseUser).isEmailVerified()) {
                    startActivity(new Intent(SignInActivity.this, WelcomeActivity.class));
                    finish();
                    Toast.makeText(SignInActivity.this, getString(R.string.log_in_success), Toast.LENGTH_SHORT).show();
                } else {
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(SignInActivity.this, getString(R.string.please_check_email), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignInActivity.this, getString(R.string.log_in_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }
}