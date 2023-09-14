package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidebookapps.databinding.RowAuthorNameBinding;
import com.example.item.AuthorList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AuthorNameAdapter extends RecyclerView.Adapter<AuthorNameAdapter.ViewHolder> {

    Activity activity;
    List<AuthorList> authorLists,mDataList;

    public AuthorNameAdapter(Activity activity, List<AuthorList> authorLists) {
        this.activity = activity;
        this.authorLists = authorLists;
        mDataList = new ArrayList<>();
        mDataList.addAll(authorLists);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowAuthorNameBinding.inflate(activity.getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.rowAuthorBinding.tvAuthorName.setText(authorLists.get(position).getPost_title());
        holder.rowAuthorBinding.cbAuthorId.setChecked(authorLists.get(position).isSelected());
        holder.rowAuthorBinding.cbAuthorId.setTag(authorLists.get(position));

        holder.rowAuthorBinding.cbAuthorId.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v;
                AuthorList itemFilter = (AuthorList) cb.getTag();
                itemFilter.setSelected(cb.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return authorLists.size();
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
        authorLists.clear();
        if (charText.length() == 0) {
            authorLists.addAll(mDataList);
        } else {
            for (AuthorList wp : mDataList) {
                if (wp.getPost_title().toLowerCase(Locale.getDefault()).contains(charText)) {
                    authorLists.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
