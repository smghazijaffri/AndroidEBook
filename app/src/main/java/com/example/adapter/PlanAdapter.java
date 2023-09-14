package com.example.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidebookapps.R;
import com.example.androidebookapps.databinding.RowPlanBinding;
import com.example.response.PlanRP;
import com.example.util.OnClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    Activity activity;
    List<PlanRP.ItemPlanList> itemPlanLists;
    OnClick onClick;
    private int row_index = -1;

    public PlanAdapter(Activity activity, List<PlanRP.ItemPlanList> itemPlanLists) {
        this.activity = activity;
        this.itemPlanLists = itemPlanLists;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowPlanBinding.inflate(activity.getLayoutInflater()));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.rowPlanBinding.tvPlanName.setText(itemPlanLists.get(position).getPlan_name());
        holder.rowPlanBinding.tvAmount.setText(itemPlanLists.get(position).getPlan_price()+activity.getString(R.string.plan_line));
        holder.rowPlanBinding.tvCurrency.setText(itemPlanLists.get(position).getCurrency_code());
        holder.rowPlanBinding.tvPlanTime.setText(itemPlanLists.get(position).getPlan_duration());

        if (row_index > -1) {
            if (row_index == position) {
                holder.rowPlanBinding.cvPlan.setCardBackgroundColor(activity.getResources().getColor(R.color.plan_card_bg_select));
                holder.rowPlanBinding.tvPlanName.setTextColor(activity.getResources().getColor(R.color.plan_text_select));
                holder.rowPlanBinding.tvAmount.setTextColor(activity.getResources().getColor(R.color.plan_text_select));
                holder.rowPlanBinding.tvCurrency.setTextColor(activity.getResources().getColor(R.color.plan_text_select));
                holder.rowPlanBinding.tvPlanTime.setTextColor(activity.getResources().getColor(R.color.plan_days_select));
                holder.rowPlanBinding.rdCheck.setBackgroundResource(R.drawable.plan_circle_select);
            } else {
                holder.rowPlanBinding.cvPlan.setCardBackgroundColor(activity.getResources().getColor(R.color.plan_card_bg_unselect));
                holder.rowPlanBinding.tvPlanName.setTextColor(activity.getResources().getColor(R.color.plan_name_unselect));
                holder.rowPlanBinding.tvAmount.setTextColor(activity.getResources().getColor(R.color.plan_price_unselect));
                holder.rowPlanBinding.tvCurrency.setTextColor(activity.getResources().getColor(R.color.plan_price_unselect));
                holder.rowPlanBinding.tvPlanTime.setTextColor(activity.getResources().getColor(R.color.plan_days_unselect));
                holder.rowPlanBinding.rdCheck.setBackgroundResource(R.drawable.plan_circle_unselect);
            }
        }

        holder.rowPlanBinding.cvPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.position(holder.getBindingAdapterPosition());
            }
        });

    }
    public void select(int position) {
        row_index = position;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return itemPlanLists.size();
    }

    public void setOnItemClickListener(OnClick clickListener) {
        this.onClick = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowPlanBinding rowPlanBinding;

        public ViewHolder(RowPlanBinding rowPlanBinding) {
            super(rowPlanBinding.getRoot());
            this.rowPlanBinding = rowPlanBinding;
        }
    }
}
