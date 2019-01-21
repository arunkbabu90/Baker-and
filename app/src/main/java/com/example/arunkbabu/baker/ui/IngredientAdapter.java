package com.example.arunkbabu.baker.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Ingredient;
import com.example.arunkbabu.baker.utils.BakerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>
{
    private List<Ingredient> mIngredientList;

    public void setIngredientList(List<Ingredient> ingredientList) {
        mIngredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.mIngredientNo.setText(String.valueOf(position + 1));
        holder.mIngredientName.setText(BakerUtils.capitalizeFirstLetter(mIngredientList.get(position).getIngredient()));
        holder.mIngredientQuantity.setText(formatQuantityMeasure(position, mIngredientList));
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null) return 0;
        return mIngredientList.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_ingredient_no) TextView mIngredientNo;
        @BindView(R.id.tv_ingredient_name) TextView mIngredientName;
        @BindView(R.id.tv_ingredient_qty) TextView mIngredientQuantity;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * Joins the quantity with measure to get a formatted output for display.   Ex: 2 Cups
     * @param position The position of the ingredient
     * @param ingredientList The List of Ingredients
     * @return String   Formatted Quantity measure
     */
    private String formatQuantityMeasure(int position, List<Ingredient> ingredientList) {
        double quantity = ingredientList.get(position).getQuantity();

        // Get the suffix for measure for creating meaningful measures. Ex: 2 Cups | 1 Cup
        String suffix;
        if (quantity >= 2) suffix = "s";
        else suffix = "";

        // Capitalize only the first letter of Measure. Ex: CUP to Cup
        String measure = ingredientList.get(position).getMeasure();
        String capitalizedMeasure = BakerUtils.capitalizeFirstLetter(measure);

        return quantity + " " + capitalizedMeasure + suffix;
    }

}
