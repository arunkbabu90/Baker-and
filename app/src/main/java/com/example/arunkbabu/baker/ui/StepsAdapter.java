package com.example.arunkbabu.baker.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>
{
    private ItemClickListener mItemClickListener;
    private ArrayList<Step> mStepList;


    public void setStepsList(ArrayList<Step> stepsList) {
        mStepList = stepsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipe_steps, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        holder.stepTextView.setText(mStepList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mStepList == null) return 0;
        return mStepList.size();
    }



    /**
     * ViewHolder for StepAdapter
     */
    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tv_step) TextView stepTextView;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onStepItemClick(v, mStepList, getAdapterPosition());
            }
        }
    }


    /**
     * Sets the ItemClickListener for the adapter
     * @param itemClickListener The ItemClickListener
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onStepItemClick(View view, ArrayList<Step> stepList, int position);
    }

}
