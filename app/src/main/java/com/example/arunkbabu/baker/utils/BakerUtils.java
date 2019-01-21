package com.example.arunkbabu.baker.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.example.arunkbabu.baker.R;

public class BakerUtils
{
    /**
     * Supplies the correct recipe image to the imageView
     * @param recipeName The name of the recipe
     * @return The image resource id
     */
    public static int supplyCorrectImage(String recipeName) {
        int imageResId;
        switch (recipeName)
        {
            case "Nutella Pie":
                imageResId = R.drawable.nutella_pie;
                break;
            case "Brownies":
                imageResId = R.drawable.brownies;
                break;
            case "Yellow Cake":
                imageResId = R.drawable.yellow_cake;
                break;
            case "Cheesecake":
                imageResId = R.drawable.cheese_cake;
                break;
            default:
                imageResId = R.drawable.twotone_fastfood_black_48dp;
        }
        return imageResId;
    }


    /**
     * Capitalizes the first letter of the given String text
     * @param text The text in which the first letter is to be capitalized
     * @return String containing the capitalized first letter
     */
    public static String capitalizeFirstLetter(String text) {
        String lowercaseSuffix = text.substring(1).toLowerCase();
        return text.substring(0,1).toUpperCase() + lowercaseSuffix;
    }




    /*
     * Code adapted from: https://stackoverflow.com/questions/16784101/how-to-find-tablet-or-phone-in-android-programmatically
     */
    /**
     * Checks whether the device is a Tablet
     * @param context The Activity Context
     * @return True if it's a tablet & False otherwise
     */
    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}
