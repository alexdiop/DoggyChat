package com.example.doggychat;

import android.content.Context;
import android.os.Message;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_RIGHT = 0;
    public static final int MSG_TYPE_LEFT = 1;
    private Context context;
    FirebaseUser user;
    private List<String> users;
    private List<String> messages;
    Utils toasty = new Utils();

    public MessageAdapter(Context context, List<String> messages, List<String> users){
        this.context = context;
        this.messages = messages;
        this.users = users;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        String tempContent = messages.get(position);
        holder.messages.setText(tempContent);
        holder.profilePic.setImageResource(R.mipmap.ic_launcher);


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView messages;
        public ImageView profilePic;

        public ViewHolder(View itemView){
            super(itemView);

            messages = itemView.findViewById(R.id.Messages);
            profilePic = itemView.findViewById(R.id.ProfileImage);

        }



    }


    @Override
    public int getItemViewType(int position){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(users.get(position).equals(user.getDisplayName())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }




}
