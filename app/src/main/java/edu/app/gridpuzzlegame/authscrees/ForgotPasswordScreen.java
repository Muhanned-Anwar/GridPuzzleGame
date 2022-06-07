package edu.app.gridpuzzlegame.authscrees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import edu.app.gridpuzzlegame.R;
import edu.app.gridpuzzlegame.app.MainActivity;

public class ForgotPasswordScreen extends AppCompatActivity {

    ImageButton btnBack;
    EditText editTextEmail;
    Button btnSubMit;
    TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);


        btnBack = findViewById(R.id.btnBack);
        editTextEmail = findViewById(R.id.editTextForgotPasswordEmail);
        btnSubMit = findViewById(R.id.btnSubMit);
        createAccount = findViewById(R.id.createNewAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),RegisterScreen.class);
                startActivity(intent);
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSubMit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performForgotPassword();
            }
        });
    }

    public void performForgotPassword() {
        if (inputValidation()) {
            sendViaEmail();
        }
    }


    public boolean inputValidation() {
        boolean flag = true;
        if (editTextEmail.getText().toString().isEmpty()) {
            flag = false;
            editTextEmail.setError("Error, Can't be Empty");
        }

        return flag;
    }

    public void sendViaEmail() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(
                editTextEmail.getText().toString().trim()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getBaseContext(), "Send Link Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), LoginScreen.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Send Link Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}