package edu.app.gridpuzzlegame.authscrees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.app.gridpuzzlegame.R;

public class RegisterScreen extends AppCompatActivity {

    Button btnRegister;
    TextView btnLoginScreen;
    EditText editTextUsername;
    EditText editTextEmail;
    TextInputEditText editTextPassword;
    TextInputEditText editTextConfirmPassword;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        btnRegister = findViewById(R.id.btnSave);
        btnLoginScreen = findViewById(R.id.btnForgotPassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPasswordRegister);
        btnBack = findViewById(R.id.btnBack);

        btnLoginScreen.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), LoginScreen.class);
            startActivity(intent);
            finish();
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performRegister();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void performRegister() {
        if (inputValidation()) {
            validateUser();
        }
    }

    public boolean inputValidation() {
        boolean flag = true;

        if (editTextUsername.getText().toString().isEmpty()) {
            editTextUsername.setError("Error, Can't be Empty");
            flag = false;
        }

        if (editTextEmail.getText().toString().isEmpty()) {
            editTextEmail.setError("Error, Can't be Empty");
            flag = false;
        } else if (!editTextEmail.getText().toString().contains("@")) {
            editTextEmail.setError("Error, Enter correct Email");
            flag = false;
        }

        if (!checkPassword()) {
            flag = false;
        }

        return flag;
    }

    public boolean checkPassword() {
        if (editTextPassword.getText().toString().isEmpty() || editTextConfirmPassword.getText().toString().isEmpty()) {
            return false;
        } else {
            if (editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void validateUser() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextEmail.getText().toString().trim(),
                editTextPassword.getText().toString().trim()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveInFireStore(task.getResult().getUser().getUid());
                    Toast.makeText(getBaseContext(), "Created Account Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), LoginScreen.class);
                    startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), "Created Account Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveInFireStore(String user_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("user_id", user_id);
        user.put("user_name", editTextUsername.getText().toString());

        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getBaseContext(), "DocumentSnapshot added with ID: "
                        + documentReference.getId(), Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(),  "Error adding document", Toast.LENGTH_LONG).show();
            }
        });
    }
}