package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.RowDownloadBinding;
import com.example.androidebookapps.databinding.RowFavoriteBinding;
import com.example.item.DownloadList;
import com.example.util.AdInterstitialAds;
import com.example.util.DatabaseHandler;
import com.example.util.Method;
import com.example.util.OnClick;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    Method method;
    Activity activity;
    DatabaseHandler db;
    List<DownloadList> downloadLists;
    OnClick onClick;
    int columnWidth;

    public DownloadAdapter(Activity activity, List<DownloadList> downloadLists) {
        this.activity = activity;
        this.downloadLists = downloadLists;
        db = new DatabaseHandler(activity);
        method = new Method(activity);
        Resources r = activity.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());
        columnWidth = (int) ((method.getScreenWidth() - ((3 + 1) * padding)));
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowDownloadBinding.inflate(activity.getLayoutInflater()));

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {

        holder.rowDownloadBinding.llHomeBook.setLayoutParams(new LinearLayout.LayoutParams(columnWidth / 3, columnWidth / 2));
        holder.rowDownloadBinding.tvHomeConTitle.setText(downloadLists.get(position).getTitle());

            Glide.with(activity.getApplicationContext()).load("file://" +downloadLists.get(position).getImage())
                    .placeholder(R.drawable.placeholder_portable)
                    .into(holder.rowDownloadBinding.ivHomeCont);

        holder.rowDownloadBinding.rlFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdInterstitialAds.ShowInterstitialAds(activity,holder.getBindingAdapterPosition(),onClick);
            }
        });


        holder.rowDownloadBinding.cvDelete.setOnClickListener(v -> {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
            builder.setMessage(activity.getResources().getString(R.string.delete_msg));
            builder.setCancelable(false);
            builder.setPositiveButton(activity.getResources().getString(R.string.lbl_delete),
                    (arg0, arg1) -> {

                        if (!db.checkIdDownloadBook(downloadLists.get(position).getId())) {
                            db.deleteDownloadBook(downloadLists.get(position).getId());
                            File file = new File(downloadLists.get(position).getUrl());
                            File file_image = new File(downloadLists.get(position).getImage());
                            file.delete();
                            file_image.delete();
                            downloadLists.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
                        }

                    });
            builder.setNegativeButton(activity.getResources().getString(R.string.cancel),
                    (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        });

    }

    @Override
    public int getItemCount() {
        return downloadLists.size();
    }

    public void setOnItemClickListener(OnClick clickListener) {
        this.onClick = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowDownloadBinding rowDownloadBinding;

        public ViewHolder(RowDownloadBinding rowDownloadBinding) {
            super(rowDownloadBinding.getRoot());
            this.rowDownloadBinding = rowDownloadBinding;
        }
    }
}
