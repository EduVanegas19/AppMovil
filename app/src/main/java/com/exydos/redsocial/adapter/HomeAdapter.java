package com.exydos.redsocial.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exydos.redsocial.R;
import com.exydos.redsocial.fragments.Home;
import com.exydos.redsocial.model.HomeModel;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private List<HomeModel> list;
    Context context;

    public HomeAdapter(List<HomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items, parent, false);

        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
         holder.tvUserName.setText(list.get(position).getUserName());
         holder.txTime.setText(list.get(position).getUserName());

         int count = list.get(position).getLikeCount();

         if (count == 0){
             holder.tbLikeCount.setVisibility(View.INVISIBLE);
         } else if (count == 1) {
             holder.tbLikeCount.setText(count+" like");
         }else {
             holder.tbLikeCount.setText(count + " likes");
        }

        Random random = new Random();

        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getPostImage())
                .placeholder(new ColorDrawable())
                .timeout(6500)
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HomeHolder extends RecyclerView.ViewHolder{

        private CircleImageView profileImage;
        private TextView tvUserName, txTime, tbLikeCount;
        private ImageView imageView;
        private ImageButton btnLike, btnComment, btnShare;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            imageView = itemView.findViewById(R.id.imageView);
            tvUserName = itemView.findViewById(R.id.tvName);
            txTime = itemView.findViewById(R.id.tvTime);
            tbLikeCount = itemView.findViewById(R.id.tvLikeCount);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnShare = itemView.findViewById(R.id.btnShare);

        }
    }
}
