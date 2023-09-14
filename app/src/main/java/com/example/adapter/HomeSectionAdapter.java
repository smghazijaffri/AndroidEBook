package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidebookapps.AuthorDetailsActivity;
import com.example.androidebookapps.BookDetailsActivity;
import com.example.androidebookapps.BookListBySubCatActivity;
import com.example.androidebookapps.HomeSectionListActivity;
import com.example.androidebookapps.SubCategoryActivity;
import com.example.androidebookapps.databinding.RowHomeSectionBinding;
import com.example.item.HomeSection;
import com.example.util.OnClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeSectionAdapter extends RecyclerView.Adapter<HomeSectionAdapter.ViewHolder> {

    Activity activity;
    List<HomeSection> homeSections;

    public HomeSectionAdapter(Activity activity, List<HomeSection> homeSections) {
        this.activity = activity;
        this.homeSections = homeSections;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowHomeSectionBinding.inflate(activity.getLayoutInflater()));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        HomeSection homeSectionPos = homeSections.get(position);
        holder.rowHomeSecBinding.ivHomeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(activity, HomeSectionListActivity.class);
                intentHome.putExtra("ID", homeSectionPos.getHomeId());
                intentHome.putExtra("TITLE", homeSectionPos.getHomeTitle());
                intentHome.putExtra("TYPE", homeSectionPos.getHomeType());
                activity.startActivity(intentHome);
            }
        });

        holder.rowHomeSecBinding.tvHomeTitle.setText(homeSectionPos.getHomeTitle());
        holder.rowHomeSecBinding.rvHomeBook.setHasFixedSize(true);
        holder.rowHomeSecBinding.rvHomeBook.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        holder.rowHomeSecBinding.rvHomeBook.setNestedScrollingEnabled(false);
        HomeContentAdapter homeContentAdapter = new HomeContentAdapter(activity, homeSectionPos.getHomeContent(), "");
        holder.rowHomeSecBinding.rvHomeBook.setAdapter(homeContentAdapter);
        homeContentAdapter.setOnItemClickListener(new OnClick() {
            @Override
            public void position(int positionContent) {
                switch (homeSectionPos.getHomeType()) {
                    case "book":
                        Intent intentDetail = new Intent(activity, BookDetailsActivity.class);
                        intentDetail.putExtra("BOOK_ID", homeSectionPos.getHomeContent().get(positionContent).getPostId());
                        activity.startActivity(intentDetail);
                        break;
                    case "author": {
                        Intent intentSubCat = new Intent(activity, AuthorDetailsActivity.class);
                        intentSubCat.putExtra("AUTHOR_ID", homeSectionPos.getHomeContent().get(positionContent).getPostId());
                        activity.startActivity(intentSubCat);
                        break;
                    }
                    case "category": {
                        if (homeSectionPos.getHomeContent().get(positionContent).getSub_cat_status().equals("true")) {
                            Intent intentSubCat = new Intent(activity, SubCategoryActivity.class);
                            intentSubCat.putExtra("postCatId", homeSectionPos.getHomeContent().get(positionContent).getPostId());
                            intentSubCat.putExtra("postCatName", homeSectionPos.getHomeContent().get(positionContent).getPostTitle());
                            activity.startActivity(intentSubCat);
                        } else {
                            Intent intentSubCat = new Intent(activity, BookListBySubCatActivity.class);
                            intentSubCat.putExtra("postSubCatId", homeSectionPos.getHomeContent().get(positionContent).getPostId());
                            intentSubCat.putExtra("postSubCatName", homeSectionPos.getHomeContent().get(positionContent).getPostTitle());
                            intentSubCat.putExtra("type", "Cat");
                            activity.startActivity(intentSubCat);
                        }
                        break;
                    }
                    case "subcategory": {
                        Intent intentSubCat = new Intent(activity, BookListBySubCatActivity.class);
                        intentSubCat.putExtra("postSubCatId", homeSectionPos.getHomeContent().get(positionContent).getPostId());
                        intentSubCat.putExtra("postSubCatName", homeSectionPos.getHomeContent().get(positionContent).getPostTitle());
                        intentSubCat.putExtra("type", "");
                        activity.startActivity(intentSubCat);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeSections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowHomeSectionBinding rowHomeSecBinding;

        public ViewHolder(RowHomeSectionBinding rowHomeSecBinding) {
            super(rowHomeSecBinding.getRoot());
            this.rowHomeSecBinding = rowHomeSecBinding;
        }
    }

}
