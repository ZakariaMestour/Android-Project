package com.example.control.classement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.control.R;

import java.util.ArrayList;

public class LeaderboardAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private ArrayList<User> userList;

    public LeaderboardAdapter(Context context, ArrayList<User> list) {
        super(context, 0, list);
        mContext = context;
        userList = list;
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
        TextView scoreTextView = listItem.findViewById(R.id.textViewScore);

        // Position + 1 car position commence à 0 mais le rang commence à 1
        rankTextView.setText(String.valueOf(position + 1));
        usernameTextView.setText(currentUser.getUsername());
        scoreTextView.setText(String.valueOf(currentUser.getScore()));

        return listItem;
    }
}
