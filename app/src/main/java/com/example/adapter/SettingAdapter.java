package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.RowSettingBinding;
import com.example.item.PageList;
import com.example.util.OnClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    Activity activity;
    List<PageList> pageLists;
    OnClick onClick;

    public SettingAdapter(Activity activity, List<PageList> pageLists) {
        this.activity = activity;
        this.pageLists = pageLists;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowSettingBinding.inflate(activity.getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        switch (pageLists.get(position).getPageId()) {
            case "1": //about
                Glide.with(activity.getApplicationContext()).load(R.drawable.img_about_us)
                        .placeholder(R.drawable.placeholder_portable)
                        .into(holder.rowSettingBinding.ivSettingIcon);
                break;
            case "2": //privacy
            default:
                Glide.with(activity.getApplicationContext()).load(R.drawable.img_privacy)
                        .placeholder(R.drawable.placeholder_portable)
                        .into(holder.rowSettingBinding.ivSettingIcon);
                break;
            case "3": //terms
                Glide.with(activity.getApplicationContext()).load(R.drawable.img_terms)
                        .placeholder(R.drawable.placeholder_portable)
                        .into(holder.rowSettingBinding.ivSettingIcon);
                break;
            case "4": //delete
                Glide.with(activity.getApplicationContext()).load(R.drawable.img_delete_instruction)
                        .placeholder(R.drawable.placeholder_portable)
                        .into(holder.rowSettingBinding.ivSettingIcon);
                break;
        }

        holder.rowSettingBinding.tvSettingTitle.setText(pageLists.get(position).getPageTitle());

        holder.rowSettingBinding.llSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.position(holder.getBindingAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return pageLists.size();
    }

    public void setOnItemClickListener(OnClick clickListener) {
        this.onClick = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RowSettingBinding rowSettingBinding;

        public ViewHolder(RowSettingBinding rowSettingBinding) {
            super(rowSettingBinding.getRoot());
            this.rowSettingBinding = rowSettingBinding;
        }
    }
}
