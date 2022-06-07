package edu.app.gridpuzzlegame.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.app.gridpuzzlegame.R;

public class GameViewHolder extends RecyclerView.ViewHolder {

    public View view;
    public TextView game_id;
    public TextView win;
    public TextView realResult;
    public TextView time;
    public LinearLayout linearLayout;


    public GameViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        game_id = itemView.findViewById(R.id.game_id);
        win = itemView.findViewById(R.id.win);
        realResult = itemView.findViewById(R.id.realResult);
        time = itemView.findViewById(R.id.time);
        linearLayout = itemView.findViewWithTag(R.id.game_item);

    }
}
