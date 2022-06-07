package edu.app.gridpuzzlegame.controller.firebase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireAuthController {
    private FirebaseAuth instance = FirebaseAuth.getInstance();

    boolean statusLogin = false;

    public boolean signIn(Context context, String email, String password) {
        instance.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                statusLogin = true;
            } else {
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show();
                statusLogin = false;
            }
        });
        return  statusLogin;
    }

    public void signOut() {
        instance.signOut();
    }

    boolean statusCreate = false;

    public boolean createAccount(Context context, String email, String password) {
        instance.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Created Account Successfully", Toast.LENGTH_SHORT).show();

                statusCreate = true;
            } else {
                Toast.makeText(context, "Created Account Failed", Toast.LENGTH_SHORT).show();
                statusCreate = false;
            }
        });

        return statusCreate;
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
//                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
//                            finish();
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
//                            overridePendingTransition(0, 0);
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());

                        }
                    }
                });
    }

    public void resetPassword(Context context, String email) {
        instance.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Password recovery link has been sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Send Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
