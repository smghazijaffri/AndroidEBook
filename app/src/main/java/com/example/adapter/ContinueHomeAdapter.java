package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.RowContinueHomeBinding;
import com.example.item.HomeContent;
import com.example.util.AdInterstitialAds;
import com.example.util.OnClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContinueHomeAdapter extends RecyclerView.Adapter<ContinueHomeAdapter.ViewHolder> {

    Activity activity;
    List<HomeContent> homeContentsContinue;
    OnClick onClick;

    public ContinueHomeAdapter(Activity activity, List<HomeContent> homeContentsContinue) {
        this.activity = activity;
        this.homeContentsContinue = homeContentsContinue;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowContinueHomeBinding.inflate(activity.getLayoutInflater()));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {

         holder.rowContinueHomeBinding.tvHomeConTitle.setText(homeContentsContinue.get(position).getPostTitle());


        if (!homeContentsContinue.get(position).getPostImage().equals("")) {
            Glide.with(activity.getApplicationContext()).load(homeContentsContinue.get(position).getPostImage())
                    .placeholder(R.drawable.placeholder_portable)
                    .into(holder.rowContinueHomeBinding.ivHomeCont);
        }

        holder.rowContinueHomeBinding.llContinueHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdInterstitialAds.ShowInterstitialAds(activity,holder.getBindingAdapterPosition(),onClick);
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeContentsContinue.size();
    }

    public void setOnItemClickListener(OnClick clickListener) {
        this.onClick = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       RowContinueHomeBinding rowContinueHomeBinding;

        public ViewHolder(RowContinueHomeBinding rowContinueHomeBinding) {
            super(rowContinueHomeBinding.getRoot());
          this.rowContinueHomeBinding=rowContinueHomeBinding;
        }
    }

}
