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

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private Button buttonSignUp;
    private TextView tvAlreadyAccount;
    static final int MINIMUM_PASSWORD_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
        buttonSignUp.setOnClickListener(v -> userRegistration());
        tvAlreadyAccount.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, SignInActivity.class)));
        Objects.requireNonNull(getSupportActionBar()).setTitle("SignUp Screen");
    }

    private void findViews() {
        buttonSignUp = findViewById(R.id.btn_SignUp);
        tvAlreadyAccount = findViewById(R.id.already_account);
        editEmail = findViewById(R.id.reg_Email);
        editPassword = findViewById(R.id.reg_Password);
    }

    private void userRegistration() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userEmail = editEmail.getText().toString();
        String userPassword = editPassword.getText().toString();

        if (userEmail.isEmpty()) {
            editEmail.setError(getString(R.string.email_is_require));
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            editEmail.setError(getString(R.string.please_enter_valid_email));
            editEmail.requestFocus();
            return;
        }

        if (userPassword.isEmpty()) {
            editPassword.setError(getString(R.string.password_is_required));
            editPassword.requestFocus();
            return;
        }
        if (userPassword.length() < MINIMUM_PASSWORD_LENGTH) {
            editPassword.setError(getString(R.string.minimum_password_length));
            editPassword.requestFocus();
            return;
        }
        auth.createUserWithEmailAndPassword(editEmail.getText().toString(),
                editPassword.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                Toast.makeText(SignUpActivity.this, getString(R.string.user_sign_up_success), Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, getString(R.string.try_again), Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
    }

}