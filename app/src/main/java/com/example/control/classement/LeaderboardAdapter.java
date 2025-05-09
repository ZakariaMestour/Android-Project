package com.example.control.classement;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.control.R;

import java.util.ArrayList;

public class LeaderboardAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private ArrayList<User> userList;
    private String currentUserId;

    public LeaderboardAdapter(Context context, ArrayList<User> list, String currentUserId) {
        super(context, 0, list);
        mContext = context;
        userList = list;
        this.currentUserId = currentUserId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_leaderboard, parent, false);
        }

        User currentUser = userList.get(position);

        TextView rankTextView = listItem.findViewById(R.id.textViewRank);
        TextView usernameTextView = listItem.findViewById(R.id.textViewUsername);
        TextView emailTextView = listItem.findViewById(R.id.textViewEmail);
        TextView scoreTextView = listItem.findViewById(R.id.textViewScore);
        RelativeLayout itemLayout = listItem.findViewById(R.id.leaderboard_item_layout);

        // Position + 1 car position commence à 0 mais le rang commence à 1
        rankTextView.setText(String.valueOf(position + 1));
        usernameTextView.setText(currentUser.getUsername());
        emailTextView.setText(currentUser.getEmail());
        scoreTextView.setText(String.valueOf(currentUser.getScore()));

        // Mettre en évidence l'utilisateur courant
        if (currentUser.getUserId().equals(currentUserId)) {
            // Couleur de fond pour mettre en évidence l'utilisateur actuel
            itemLayout.setBackgroundColor(Color.parseColor("#E0F7FA")); // Bleu clair

            // Vous pouvez aussi changer les couleurs de texte
            rankTextView.setTextColor(Color.parseColor("#0288D1"));
            usernameTextView.setTextColor(Color.parseColor("#01579B"));
            scoreTextView.setTextColor(Color.parseColor("#01579B"));
        } else {
            // Réinitialiser pour les autres utilisateurs
            itemLayout.setBackgroundColor(Color.TRANSPARENT);
            rankTextView.setTextColor(Color.BLACK);
            usernameTextView.setTextColor(Color.BLACK);
            scoreTextView.setTextColor(Color.parseColor("#4CAF50")); // Vert pour le score
        }

        return listItem;
    }
}
