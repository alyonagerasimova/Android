package com.umbrella.android.ui.network;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.umbrella.android.R;
import com.umbrella.android.auth.User;

import java.util.Objects;

public class LoginRegistrationActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    EditText email;
    EditText password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login_register);

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnLogin.setOnClickListener(view -> {
            try {
                login(email, password);
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
        });

        btnRegister.setOnClickListener(view -> registration(email, password));
    }

    private void login(EditText email, EditText password) throws FirebaseAuthException {
        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(LoginRegistrationActivity.this, R.string.enter_email,
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(LoginRegistrationActivity.this, R.string.enter_password,
                    Toast.LENGTH_LONG).show();
            return;
        }
        auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(authResult -> {
                    Intent intent = new Intent(LoginRegistrationActivity.this, NetworkActivity.class);
                    startActivity(intent);
                }).addOnFailureListener(e ->
                Toast.makeText(LoginRegistrationActivity.this,
                        getString(R.string.error_signin) + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void registration(EditText email, EditText password) {
        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(LoginRegistrationActivity.this, R.string.enter_email,
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(LoginRegistrationActivity.this, R.string.enter_password,
                    Toast.LENGTH_LONG).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email.getText().toString(),
                password.getText().toString())
                .addOnSuccessListener(authResult -> {
                    User user = new User(email.getText().toString(),
                            password.getText().toString());
                    users.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .setValue(user)
                            .addOnSuccessListener(unused -> Toast.makeText(
                                    LoginRegistrationActivity.this,
                                    R.string.add_user,
                                    Toast.LENGTH_LONG).show());
                })
                .addOnFailureListener(e -> Toast.makeText(LoginRegistrationActivity.this,
                        getString(R.string.error_register) + e.getMessage(), Toast.LENGTH_LONG).show());
        ;
    }

}


