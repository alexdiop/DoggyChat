package com.example.doggychat;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<String> mUsers;
    private OnNoteListener mOnNoteListener;

    public ChatAdapter(Context context, List<String> mUsers, OnNoteListener mOnNoteListener){
        this.context = context;
        this.mUsers = mUsers;
        this.mOnNoteListener = mOnNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chat, parent, false);
        return new ChatAdapter.ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      //  final Utils toasty = new Utils();
        String displayName = mUsers.get(position);
        holder.username.setText(displayName);
        StorageReference picRef = FirebaseStorage.getInstance().getReference();
        StorageReference picRef2 = picRef.child(displayName);
        if(picRef2.child("/profile.jpeg") != null) {
            Glide.with(context).load(picRef2.child("/profile.jpeg")).into(holder.profileImage);
        }
        else{
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView username;
        public ImageView profileImage;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener){
            super(itemView);
            username = itemView.findViewById(R.id.chatUsername);
            profileImage = itemView.findViewById(R.id.chatProfileImage);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);

    }



}
