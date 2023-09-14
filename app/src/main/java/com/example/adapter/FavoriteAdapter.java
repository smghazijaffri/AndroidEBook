package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.RowFavoriteBinding;
import com.example.item.SubCatListBook;
import com.example.util.AdInterstitialAds;
import com.example.util.FavouriteIF;
import com.example.util.Method;
import com.example.util.OnClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    Activity activity;
    List<SubCatListBook> favListBookList;
    OnClick onClick;
    int columnWidth;
    Method method;

    public FavoriteAdapter(Activity activity, List<SubCatListBook> favListBookList) {
        this.activity = activity;
        this.favListBookList = favListBookList;
        method = new Method(activity);
        Resources r = activity.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());
        columnWidth = (int) ((method.getScreenWidth() - ((3 + 1) * padding)));
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowFavoriteBinding.inflate(activity.getLayoutInflater()));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {

        SubCatListBook favListBookListPis=favListBookList.get(position);
        holder.rowFavoriteBinding.llHomeBook.setLayoutParams(new LinearLayout.LayoutParams(columnWidth / 3, columnWidth / 2));
        holder.rowFavoriteBinding.tvHomeConTitle.setText(favListBookListPis.getPost_title());

         if (!favListBookListPis.getPost_image().equals("")) {
            Glide.with(activity.getApplicationContext()).load(favListBookListPis.getPost_image())
                    .placeholder(R.drawable.placeholder_portable)
                    .into(holder.rowFavoriteBinding.ivHomeCont);
        }

        holder.rowFavoriteBinding.rlFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdInterstitialAds.ShowInterstitialAds(activity,holder.getBindingAdapterPosition(),onClick);
            }
        });

         holder.rowFavoriteBinding.mcvFav.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FavouriteIF favouriteIF = (isFavourite, message) -> {
                     if (isFavourite.equals("true")) {
                         holder.rowFavoriteBinding.ivFavList.setImageResource(R.drawable.img_fav_hover_profile);
                     } else {
                         holder.rowFavoriteBinding.ivFavList.setImageResource(R.drawable.img_fav_hover_profile);
                     }
                 };
                 method.addToFav(favListBookListPis.getPost_id(), method.getUserId(), "Book", favouriteIF);
             }
         });

    }

    @Override
    public int getItemCount() {
        return favListBookList.size();
    }

    public void setOnItemClickListener(OnClick clickListener) {
        this.onClick = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowFavoriteBinding rowFavoriteBinding;

        public ViewHolder(RowFavoriteBinding rowFavoriteBinding) {
            super(rowFavoriteBinding.getRoot());
            this.rowFavoriteBinding = rowFavoriteBinding;
        }
    }

}
