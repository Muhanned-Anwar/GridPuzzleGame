package edu.app.gridpuzzlegame.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.app.gridpuzzlegame.R;
import edu.app.gridpuzzlegame.adapters.GameAdapter;
import edu.app.gridpuzzlegame.models.Game;


public class ShowAllGames extends Fragment {

    public ShowAllGames() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_all_games, container, false);
        System.out.println("On Show");

        String user_id = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("games").whereEqualTo("user_id", user_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Game> gameList = new ArrayList<>();

                if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Game game = new Game(user_id, document.getString("date"), document.getString("result"), document.getString("win"));
                        game.setGame_id(document.getString(document.getId()));
                        gameList.add(game);
                    }
                } else {
                    gameList = null;
                }
                if (gameList != null) {
                    RecyclerView recyclerView = view.findViewById(R.id.SHOW_ALL);

                    GameAdapter gameAdapter = new GameAdapter(gameList);
                    recyclerView.setAdapter(gameAdapter);
                    recyclerView.hasFixedSize();

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                    recyclerView.setLayoutManager(layoutManager);
                } else {
                    TextView noData = view.findViewById(R.id.tvNoData);
                    noData.setText(R.string.not_has_any_game);
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getAllGames() {

    }
}