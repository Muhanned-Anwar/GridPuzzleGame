package edu.app.gridpuzzlegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.app.gridpuzzlegame.app.MainActivity;
import edu.app.gridpuzzlegame.authscrees.LoginScreen;
import edu.app.gridpuzzlegame.outboardingfragments.OutBoardingScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        System.out.println("\n\n\n\n\n\n\n---------------------------------------------------------------------");
                        System.out.println("\n\n\n\n\n\n\n---------------------------------------------------------------------");
                        System.out.println("\n\n\n\n\n\n\n---------------------------------------------------------------------");
                        System.out.println(token);
                        Toast.makeText(getApplicationContext(), "Your device registration token is " +
                                token, Toast.LENGTH_SHORT).show();
                    }
                });


        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Intent intent;
                    if (!isFirstApp()) {

                        intent = new Intent(getApplicationContext(), OutBoardingScreen.class);
                    } else {
                        if( FirebaseAuth.getInstance().getCurrentUser() != null){
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                        }else{
                            intent = new Intent(getApplicationContext(), LoginScreen.class);
                        }
                    }
                    startActivity(intent);
                    finish();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }

    public boolean isFirstApp() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isFirst", false);
    }


}