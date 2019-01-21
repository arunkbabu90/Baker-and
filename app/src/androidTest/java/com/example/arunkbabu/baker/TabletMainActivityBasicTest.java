package com.example.arunkbabu.baker;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.arunkbabu.baker.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Basic UI test for this app. This test is for TABLETS only
 */
@RunWith(AndroidJUnit4.class)
public class TabletMainActivityBasicTest
{
    private final int INGREDIENT_SIZE = 9;
    private final int ITEM_POSITION = 0;
    private final String STEP_DESC_TEXT = "Recipe Introduction";

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mMainActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpensRecipeMasterActivity_checkIngredients() {
        // Click on the item at the given position of the recycler view in MainActivity
        onView(withId(R.id.rv_main)).perform(actionOnItemAtPosition(ITEM_POSITION, click()));

        // Click on the Ingredients TextView to display ingredients
        onView(withId(R.id.tv_ingredients)).perform(click());

        // Check that all the ingredients are loaded and go back
        onView(withId(R.id.rv_ingredients))
                .perform(actionOnItemAtPosition(INGREDIENT_SIZE - 1, scrollTo()));

        // Then click on the item at the given position inside steps recycler view
        onView(withId(R.id.rv_recipe_options)).perform(actionOnItemAtPosition(ITEM_POSITION, click()));

        // Then click the next & previous button in the activity twice respectively
        onView(withId(R.id.btn_next_step))
                .perform(click())
                .perform(click());
        onView(withId(R.id.btn_previous_step))
                .perform(click())
                .perform(click());

        // Then check whether the description text is the one given
        onView(withId(R.id.tv_steps_desc)).check(matches(withText(STEP_DESC_TEXT)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
