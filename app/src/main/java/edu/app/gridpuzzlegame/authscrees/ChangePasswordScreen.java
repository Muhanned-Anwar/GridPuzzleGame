package edu.app.gridpuzzlegame.authscrees;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.app.gridpuzzlegame.R;

public class ChangePasswordScreen extends AppCompatActivity {

    Button btnSave;
    TextView btnForgotPassword;
    TextInputEditText editTextOldPassword;
    TextInputEditText editTextNewPassword;
    TextInputEditText editTextConfirmNewPassword;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);

        btnSave = findViewById(R.id.btnSaveChangePassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnBack = findViewById(R.id.btnBack);

        editTextOldPassword = findViewById(R.id.etOldPassword);
        editTextNewPassword = findViewById(R.id.etNewPassword);
        editTextConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);

        btnForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), ForgotPasswordScreen.class);
            startActivity(intent);
            finish();
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performChangePassword();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void performChangePassword() {
        if (inputValidation()) {
            changePassword();
        }
    }

    public boolean inputValidation() {
        boolean flag = true;

        if (editTextOldPassword.getText().toString().isEmpty()) {
            editTextOldPassword.setError("Error, Can't be Empty");
            flag = false;
        }

        if (editTextNewPassword.getText().toString().isEmpty()) {
            editTextNewPassword.setError("Error, Can't be Empty");
            flag = false;
        }
        if (editTextConfirmNewPassword.getText().toString().isEmpty()) {
            editTextConfirmNewPassword.setError("Error, Can't be Empty");
            flag = false;
        }

        if (!checkPassword()) {
            flag = false;
        }

        return flag;
    }

    public boolean checkPassword() {
        if (editTextNewPassword.getText().toString().equals(editTextConfirmNewPassword.getText().toString())) {
            return true;
        } else {
            Toast.makeText(getBaseContext(),"Two password are not equals!",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void changePassword() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentEmail = firebaseUser.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, editTextOldPassword.getText().toString());

        firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                firebaseUser.updatePassword(editTextNewPassword.getText().toString()).addOnCompleteListener(runnable -> {
                    if(runnable.isSuccessful()){
                        Toast.makeText(getBaseContext(), "Change Password Successfully", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }else{
                        Toast.makeText(getBaseContext(), "Change Password Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Toast.makeText(getBaseContext(), "Authentication failed, wrong password?", Toast.LENGTH_LONG).show();
            }
        });
    }

}

