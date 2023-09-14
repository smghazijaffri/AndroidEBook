package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidebookapps.databinding.RowAuthorNameBinding;
import com.example.item.CategoryList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CatNameAdapter extends RecyclerView.Adapter<CatNameAdapter.ViewHolder> {

    Activity activity;
    List<CategoryList> categoryLists,mDataList;

    public CatNameAdapter(Activity activity, List<CategoryList> categoryLists) {
        this.activity = activity;
        this.categoryLists = categoryLists;
        mDataList = new ArrayList<>();
        mDataList.addAll(categoryLists);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowAuthorNameBinding.inflate(activity.getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.rowAuthorBinding.tvAuthorName.setText(categoryLists.get(position).getPost_title());
        holder.rowAuthorBinding.cbAuthorId.setChecked(categoryLists.get(position).isSelectedCat());
        holder.rowAuthorBinding.cbAuthorId.setTag(categoryLists.get(position));

        holder.rowAuthorBinding.cbAuthorId.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v;
                CategoryList itemFilter = (CategoryList) cb.getTag();
                itemFilter.setSelectedCat(cb.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

      RowAuthorNameBinding rowAuthorBinding;

        public ViewHolder(RowAuthorNameBinding rowAuthorBinding) {
            super(rowAuthorBinding.getRoot());
            this.rowAuthorBinding=rowAuthorBinding;
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        categoryLists.clear();
        if (charText.length() == 0) {
            categoryLists.addAll(mDataList);
        } else {
            for (CategoryList wp : mDataList) {
                if (wp.getPost_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                    categoryLists.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
