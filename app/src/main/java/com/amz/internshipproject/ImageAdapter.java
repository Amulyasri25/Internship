package com.amz.internshipproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<Post> stories;
    private OnCardListener onCardListener;
    private DatabaseReference post;
    private String uid,name;
   private FirebaseUser usr;
   private DatabaseReference userRef;
   private FirebaseAuth fAuth;
    View card;
public interface OnItemClickListener{
    void OnItemClick(int position);
}

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {

    }

    public ImageAdapter(Context mContext, ArrayList<Post> stories, OnCardListener onCardListener){
        this.context=mContext;
        this.stories=stories;
        this.onCardListener = onCardListener;
    }


    public class ImageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTitle,tvDescription,nouser,address;
        public ImageView imageView;
        public Button donate;
        OnCardListener onCardListener;

        public ImageViewHolder(@NonNull View itemView, final OnCardListener onCardListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title_textView);
            tvDescription = itemView.findViewById(R.id.description_textView);
            imageView = itemView.findViewById(R.id.feed_imageView);
            donate=itemView.findViewById(R.id.donate);
            nouser=itemView.findViewById(R.id.nouser);
            address=itemView.findViewById(R.id.tv_address);
           // donate=itemView.findViewById(R.id.donate);
           // username=itemView.findViewById(R.id.username1);
            card = itemView.findViewById(R.id.card);
            fAuth=FirebaseAuth.getInstance();
            this.onCardListener = onCardListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if(viewType==0){
        View view=LayoutInflater.from(context).inflate(R.layout.no_user,parent,false);
        return new ImageViewHolder(view,onCardListener);
    }
    else {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view, onCardListener);
    }
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        final Post story = stories.get(position);
        //holder.username.setText(story.getName());
        holder.tvTitle.setText(story.getSelectedType());
        holder.tvDescription.setText(story.getDescription());
        if(story!=null) {
            Picasso.with(context).load(story.getImageUrl()).fit().into(holder.imageView);

            holder.donate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.donate.getContext(), UserProfile.class);
                    intent.putExtra(holder.donate.getContext().getString(R.string.user_id), story.getUid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    holder.donate.getContext().startActivity(intent);

                }
            });
        }
        else{
            holder.nouser.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount()
    {
        if(stories.size()==0){
            return 1;
        }
        return stories.size();
    }
      public int getItemViewType(int position){
    if(stories.size()==0){
        return  0;
    }
    return  1;
      }
    public interface OnCardListener {
        void onCardClick(int position);
    }

}
