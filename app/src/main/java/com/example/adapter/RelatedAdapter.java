package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.RowRelatedBinding;
import com.example.item.BookRelatedList;
import com.example.util.AdInterstitialAds;
import com.example.util.Constant;
import com.example.util.OnClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.ViewHolder> {

    Activity activity;
    List<BookRelatedList> bookRelatedLists;
    OnClick onClick;

    public RelatedAdapter(Activity activity, List<BookRelatedList> bookRelatedLists) {
        this.activity = activity;
        this.bookRelatedLists = bookRelatedLists;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowRelatedBinding.inflate(activity.getLayoutInflater()));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {

        BookRelatedList bookRelatedListPos=bookRelatedLists.get(position);
        holder.relatedBinding.tvHomeBookName.setText(bookRelatedListPos.getPost_title());
        if (!bookRelatedListPos.getPost_image().equals("")) {
            Glide.with(activity.getApplicationContext()).load(bookRelatedListPos.getPost_image())
                    .placeholder(R.drawable.placeholder_portable)
                    .into(holder.relatedBinding.ivHomePopular);
        }
        if (bookRelatedListPos.getBook_on_rent().equals("1")) {
            holder.relatedBinding.llPremium.setVisibility(View.VISIBLE);
            holder.relatedBinding.tvHomeBookPrice.setText(activity.getString(R.string.currency_code, Constant.constantCurrency, bookRelatedListPos.getBook_rent_price()));
        }else if(bookRelatedListPos.getPost_access().equals("Paid")){
            holder.relatedBinding.llPremium.setVisibility(View.VISIBLE);
            holder.relatedBinding.tvHomeBookPrice.setText(activity.getString(R.string.lbl_paid));
        } else {
            holder.relatedBinding.llPremium.setVisibility(View.GONE);
            holder.relatedBinding.tvHomeBookPrice.setText(activity.getString(R.string.lbl_free));
        }
        holder.relatedBinding.tvHomeBookName.setText(bookRelatedListPos.getPost_title());
        holder.relatedBinding.tvHomeBookStar.setText(bookRelatedListPos.getTotal_rate());
        holder.relatedBinding.tvHomeByAuthor.setText(activity.getString(R.string.by_author,bookRelatedListPos.getListRelatedAuthor().isEmpty()?"":bookRelatedListPos.getListRelatedAuthor().get(0).getAuthor_name()));
        holder.relatedBinding.llHomeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdInterstitialAds.ShowInterstitialAds(activity,holder.getBindingAdapterPosition(),onClick);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookRelatedLists.size();
    }

    public void setOnItemClickListener(OnClick clickListener) {
        this.onClick = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowRelatedBinding relatedBinding;

        public ViewHolder(RowRelatedBinding relatedBinding) {
            super(relatedBinding.getRoot());
            this.relatedBinding = relatedBinding;
        }
    }

}
