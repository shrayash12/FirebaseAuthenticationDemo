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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText reg_Email;
    EditText reg_Password;
    Button btn_SignUp;
    TextView already_account;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        User user;
        btn_SignUp = findViewById(R.id.btn_SignUp);
        already_account = findViewById(R.id.already_account);

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistration();
            }

        });
        already_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });
    }
    private void userRegistration() {
        mAuth = FirebaseAuth.getInstance();
        reg_Email = findViewById(R.id.reg_Email);
        reg_Password = findViewById(R.id.reg_Password);
        String userEmail = reg_Email.getText().toString();
        String userPassword = reg_Password.getText().toString();

        if (userEmail.isEmpty()) {
            reg_Email.setError("Email is require");
            reg_Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            reg_Email.setError("Please provide valid Email");
            reg_Email.requestFocus();
            return;
        }

        if (userPassword.isEmpty()) {
            reg_Password.setError("Password is require");
            reg_Password.requestFocus();
            return;
        }
        if (userPassword.length() < 6) {
            reg_Password.setError("Minimum password length should be 6 characters");
            reg_Password.requestFocus();
            return;
        }
        // Log.d(SignUpActivity.class.getSimpleName(), "SignUp Button Clicked");
        mAuth.createUserWithEmailAndPassword(reg_Email.getText().toString(),
                reg_Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(userEmail);
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "USER HAS BEEN SUCCESSFULLY REGISTERED", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed to register! Please try again ", Toast.LENGTH_LONG).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_LONG).show();

                        }
                    });
                    ;

                } else {
                    Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_LONG).show();

            }
        });
    }

}