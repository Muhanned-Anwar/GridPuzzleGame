package edu.app.gridpuzzlegame.authscrees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import edu.app.gridpuzzlegame.app.MainActivity;
import edu.app.gridpuzzlegame.R;
import edu.app.gridpuzzlegame.models.User;

public class LoginScreen extends AppCompatActivity {

    Button btnLogin;
    TextView btnForgotPassword;
    TextView btnCreateNewAccount;
    EditText editTextEmail;
    TextInputEditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnCreateNewAccount = findViewById(R.id.btnCreateAccount);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin(getBaseContext());
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ForgotPasswordScreen.class);
                startActivity(intent);
            }
        });

        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterScreen.class);
                startActivity(intent);
            }
        });

    }

    public boolean inputValidation() {
        boolean flag = true;
        if (editTextEmail.getText().toString().isEmpty()) {
            flag = false;
            editTextEmail.setError("Error, Can't be Empty");
        }

        if (editTextPassword.getText().toString().isEmpty()) {
            flag = false;
            editTextPassword.setError("Error, Can't be Empty");
        }
        return flag;
    }

    public void performLogin(Context context) {
        if (inputValidation()) {
            validateUser(context);

        }
    }

    public void validateUser(Context context) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveDataInSharedPreferences(task.getResult().getUser().getUid());
                Toast.makeText(context, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void saveDataInSharedPreferences(String user_id) {
        getUserInfo(user_id);
    }



    public void getUserInfo(String user_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("user_id", user_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    User user = new User(user_id,
                            documentSnapshot.getString("user_name"));


                    // Create file in app files for store user information
                    SharedPreferences sharedPreferences = getSharedPreferences("user_information", MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("user_id", user.getId());
                    editor.putString("user_name", user.getUsername());
                    editor.commit(); //
                }
            }
        });


    }
}