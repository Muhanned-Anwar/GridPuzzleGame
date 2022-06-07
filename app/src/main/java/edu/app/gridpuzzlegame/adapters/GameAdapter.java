package edu.app.gridpuzzlegame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.app.gridpuzzlegame.R;
import edu.app.gridpuzzlegame.holders.GameViewHolder;
import edu.app.gridpuzzlegame.models.Game;

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> {
    private ArrayList<Game> games;

    public GameAdapter(ArrayList<Game> games) {
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.game_id.setText(game.getResult());

        if(Boolean.parseBoolean(game.getWin())){
            holder.win.setText("Winner");
        }else{
            holder.win.setText("Loser");
        }

        holder.realResult.setText("Result Game = " + game.getResult());
        holder.time.setText(game.getDate());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
