package edu.app.gridpuzzlegame.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.app.gridpuzzlegame.R;
import edu.app.gridpuzzlegame.authscrees.LoginScreen;
import edu.app.gridpuzzlegame.databinding.ActivityMainBinding;
import edu.app.gridpuzzlegame.models.Game;
import edu.app.gridpuzzlegame.models.Question;
import edu.app.gridpuzzlegame.models.User;
import edu.app.gridpuzzlegame.models.Util;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Question question;
    int gameTimes;
    int score;
    boolean isFirstSuccess = true;
    String user_id;
    boolean win = false;

    int allGames = 0;
    int successGames = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolBar);
        getUserInformation();
        game();



    }

    public void getUserInformation() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_information", MODE_PRIVATE);

        user_id = sharedPreferences.getString("user_id", "");

        binding.tvUserName.setText(sharedPreferences.getString("user_name", getString(R.string.not_available_username)));

    }


    public void game() {
        getGameTimes();

        generateGame();

        binding.btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFirstSuccess) {
                    saveGame();
                }
                generateGame();
                gameTimes++;
                binding.score.setText(score + "/" + gameTimes);
                binding.EtNumber.setText("");
                isFirstSuccess = true;

            }
        });


        binding.btnCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!binding.EtNumber.getText().toString().isEmpty()) {
                    if (binding.EtNumber.getText().toString().equals(String.valueOf(question.getHiddenNumber()))) {
                        Toast.makeText(getBaseContext(), "Correct Answer", Toast.LENGTH_LONG).show();
                        if (isFirstSuccess) {
                            win = true;
                            saveGame();
                            win = false;
                            score++;
                            binding.score.setText(score + "/" + gameTimes);
                        }
                        isFirstSuccess = false;
                    } else {
                        Toast.makeText(getBaseContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
                    }
                } else {
                    binding.EtNumber.setError("Enter Required Data!");
                }


            }
        });


    }


    public void getGameTimes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("games").whereEqualTo("user_id", user_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                    allGames = task.getResult().getDocuments().size();
                    gameTimes = allGames;
                    gameTimes++;
                    getSuccessGameTimes();
//                    Toast.makeText(getBaseContext(), "All Games = " + allGames, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Not Has Any game in fire store", Toast.LENGTH_LONG).show();
                    gameTimes = 1;
                    score = 0;
                    binding.score.setText(score + "/" + gameTimes);
                }
            }
        }).addOnFailureListener(runnable -> {
            Toast.makeText(getBaseContext(), "Something went wrong, try again", Toast.LENGTH_LONG).show();
            gameTimes = 1;
            score = 0;
            binding.score.setText(score + "/" + gameTimes);
        });
    }


    public void getSuccessGameTimes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("games").whereEqualTo("user_id", user_id)
                .whereEqualTo("win", "true").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            successGames = task.getResult().getDocuments().size();
//                            Toast.makeText(getBaseContext(), "Success Games = " + successGames, Toast.LENGTH_LONG).show();
                            score = successGames;
                            binding.score.setText(score + "/" + gameTimes);
                        } else {
                            score = 0;
                            binding.score.setText(score + "/" + gameTimes);
                        }
                    }
                }).addOnFailureListener(runnable -> {
            gameTimes = 1;
            score = 0;
            binding.score.setText(score + "/" + gameTimes);
        });
    }

    public void saveGame() {
        saveInFireStore(readData());
    }

    public void saveInFireStore(Map map) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("games").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getBaseContext(), "Correct Answer", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
            }
        });
    }

    public HashMap<String, Object> readData() {
        Calendar dateAndTime = Calendar.getInstance();

        String am_pm;
        if (dateAndTime.get(Calendar.AM_PM) == 1) {
            am_pm = " PM";
        } else {
            am_pm = " AM";
        }

        System.out.println("Hour = " + dateAndTime.get(Calendar.HOUR));
        String date = String.valueOf(dateAndTime.get(Calendar.YEAR)) +
                "/" + String.valueOf(dateAndTime.get(Calendar.MONTH) + 1) + "/" +
                String.valueOf(dateAndTime.get(Calendar.DAY_OF_MONTH)) + "  " +
                String.valueOf(dateAndTime.get(Calendar.HOUR)) + ":" +
                String.valueOf(dateAndTime.get(Calendar.MINUTE)) +
                am_pm;
        Game game = new Game(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                date,
                String.valueOf(question.getHiddenNumber()),
                String.valueOf(win));

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("date", game.getDate());
        map.put("result", game.getResult());
        map.put("win", game.getWin());

        return map;
    }


    public void generateGame() {
        question = Util.generateQuestion();
        String[][] data = question.getData();
//        Toast.makeText(getBaseContext(),"Result = " + question.getHiddenNumber(),Toast.LENGTH_LONG).show();
        binding.tvBoxGame1.setText(data[0][0]);
        binding.tvBoxGame2.setText(data[0][1]);
        binding.tvBoxGame3.setText(data[0][2]);
        binding.tvBoxGame4.setText(data[1][0]);
        binding.tvBoxGame5.setText(data[1][1]);
        binding.tvBoxGame6.setText(data[1][2]);
        binding.tvBoxGame7.setText(data[2][0]);
        binding.tvBoxGame8.setText(data[2][1]);
        binding.tvBoxGame9.setText(data[2][2]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            //الid تبع العنصر الاول الي بالتول بار
            case R.id.menu_settings:
                intent = new Intent(getBaseContext(), SettingsScreen.class);
                startActivity(intent);
                return true;
            //الid تبع العنصر التاني شاشة all_games
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(getBaseContext(), LoginScreen.class);
                startActivity(intent);
                finish();
                Toast.makeText(getBaseContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }


}