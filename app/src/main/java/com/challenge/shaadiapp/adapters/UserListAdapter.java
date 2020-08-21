package com.challenge.shaadiapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.challenge.shaadiapp.MainActivity;
import com.challenge.shaadiapp.R;
import com.challenge.shaadiapp.helpers.Utility;
import com.challenge.shaadiapp.modal.UserInfo;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    ArrayList<UserInfo> userDataSet;
    Context appContext;
    public UserListAdapter (ArrayList<UserInfo> arrListUsers, Context context){
        userDataSet = arrListUsers;
        appContext = context;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvFirstName;
        TextView tvDate;
        TextView tvDescription;
        ImageView ivUserProfile;

        ImageButton ibAccept, ibDecline;

        TextView tvStatus;

        View layout;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;

            tvFirstName = itemView.findViewById(R.id.text_name);
            tvDate = itemView.findViewById(R.id.text_date);
            tvDescription = itemView.findViewById(R.id.text_description);
            ivUserProfile = itemView.findViewById(R.id.iv_user_profile);

            ibAccept = itemView.findViewById(R.id.ib_accept);
            ibDecline = itemView.findViewById(R.id.ib_reject);

            tvStatus = itemView.findViewById(R.id.tv_status);

        }
    }

    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View userView = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_cell, parent, false);

        UserViewHolder vh = new UserViewHolder(userView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {

        final UserInfo userInfo = userDataSet.get(position);
        holder.tvFirstName.setText(userInfo.u_name);
        holder.tvDate.setText(userInfo.date);
        holder.tvDescription.setText(userInfo.description);

        if(userInfo.status.equalsIgnoreCase(Utility.USER_NA)) {
            holder.tvStatus.setVisibility(View.GONE);
            holder.ibAccept.setVisibility(View.VISIBLE);
            holder.ibDecline.setVisibility(View.VISIBLE);
        }
        else if(userInfo.status.equalsIgnoreCase(Utility.USER_DECLINED)) {
            indicateDeclined(holder);
        }
        else if(userInfo.status.equalsIgnoreCase(Utility.USER_ACCEPTED)) {
            indicateAccepted(holder);
        }

        Glide
                .with(appContext)
                .load(userInfo.profile_pic)
                .centerCrop()
                .placeholder(R.drawable.user_pic)
                .into(holder.ivUserProfile);

        holder.ibDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.status = Utility.USER_DECLINED;
                //Update in database over here and reflect in UI
                if (appContext instanceof MainActivity) {
                    ((MainActivity)appContext).updateStatusForUser(userInfo.u_name, userInfo.status);
                }
                indicateDeclined(holder);
            }
        });

        holder.ibAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.status = Utility.USER_ACCEPTED;
                //Update in database over here and reflect in UI
                if (appContext instanceof MainActivity) {
                    ((MainActivity)appContext).updateStatusForUser(userInfo.u_name, userInfo.status);
                }
                indicateAccepted(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDataSet.size();
    }

    public void indicateAccepted(UserViewHolder viewHolder){
        viewHolder.tvStatus.setText("Member Accepted");
        viewHolder.tvStatus.setTextColor(appContext.getResources().getColor(R.color.green_color));

        viewHolder.tvStatus.setVisibility(View.VISIBLE);

        viewHolder.ibAccept.setVisibility(View.INVISIBLE);
        viewHolder.ibDecline.setVisibility(View.INVISIBLE);
    }

    public void indicateDeclined(UserViewHolder viewHolder) {
        viewHolder.tvStatus.setText("Member Declined");
        viewHolder.tvStatus.setTextColor(appContext.getResources().getColor(R.color.image_gray));

        viewHolder.tvStatus.setVisibility(View.VISIBLE);

        viewHolder.ibAccept.setVisibility(View.INVISIBLE);
        viewHolder.ibDecline.setVisibility(View.INVISIBLE);
    }
}
