package edu.app.gridpuzzlegame.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.app.gridpuzzlegame.R;
import edu.app.gridpuzzlegame.adapters.GameAdapter;
import edu.app.gridpuzzlegame.authscrees.ChangePasswordScreen;
import edu.app.gridpuzzlegame.models.Game;

public class SettingsScreen extends AppCompatActivity {
    Button btnShowAllGames;
    Button btnShowLastGameTime;
    Button btnChangePassword;
    Button btnClearGameHistory;
    TextView tvShowLastGame;
    ImageButton btnBack;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;
    CollectionReference collectionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        userId = FirebaseAuth.getInstance().getUid();
        collectionReference = db.collection("games");

        btnBack = findViewById(R.id.btnBack);
        btnShowAllGames = findViewById(R.id.btnShowAllGame);
        btnShowLastGameTime = findViewById(R.id.btnShowLastGameTime);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnClearGameHistory = findViewById(R.id.btnClearGame);
        tvShowLastGame = findViewById(R.id.tvShowLastGame);

        btnShowAllGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvShowLastGame.setText("");
                replaceFragment(new ShowAllGames());
            }
        });

        btnShowLastGameTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Fragment());
                Query query = collectionReference.whereEqualTo("user_id", userId)
                        .orderBy("date", Query.Direction.DESCENDING).limit(1);
                collectionReference.whereEqualTo("user_id", userId).get().addOnCompleteListener(runnable -> {
                    if (runnable.isSuccessful() && runnable.getResult() != null && !runnable.getResult().isEmpty()) {
                        List<DocumentSnapshot> list = runnable.getResult().getDocuments();
                        DocumentSnapshot documentSnapshot = list.get(list.size() - 1);
                        tvShowLastGame.setText("Last Game Time: " + documentSnapshot.getString("date"));
                    } else {
                        tvShowLastGame.setText(R.string.not_has_any_game);
                    }
                });
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ChangePasswordScreen.class);
                startActivity(intent);
            }
        });

        btnClearGameHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvShowLastGame.setText("");
                replaceFragment(new Fragment());
                clearAllGames();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fcvDisplay, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void clearAllGames() {


        collectionReference.whereEqualTo("user_id", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                    String docId = documentSnapshot.getId();
                    collectionReference.document(docId).delete();
                }
                Toast.makeText(getBaseContext(), "All Games Are Deleted..", Toast.LENGTH_LONG).show();
            }
        });
    }

}