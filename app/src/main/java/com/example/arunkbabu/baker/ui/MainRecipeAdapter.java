package com.example.arunkbabu.baker.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Recipe;
import com.example.arunkbabu.baker.utils.BakerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecipeAdapter extends RecyclerView.Adapter<MainRecipeAdapter.RecipesAdapterViewHolder>
{
    private ItemClickListener mItemClickListener;
    private List<Recipe> mRecipeList;
    private Context mContext;

    public void setRecipeList(Context context, List<Recipe> recipes) {
        mRecipeList = recipes;
        mContext = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipes_main, parent, false);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        String recipeName = mRecipeList.get(position).getRecipeName();
        Glide.with(mContext).load(BakerUtils.supplyCorrectImage(recipeName)).into(holder.mRecipeImageView);
        holder.mRecipeTextView.setText(recipeName);
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) return 0;
        return mRecipeList.size();
    }



    /**
     * ViewHolder for Recipe Adapter
     */
    class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.iv_recipe_image)  ImageView mRecipeImageView;
        @BindView(R.id.tv_recipe_name)  TextView mRecipeTextView;

        public RecipesAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, mRecipeList, getAdapterPosition());
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
        void onItemClick(View view, List<Recipe> recipes, int position);
    }
}
