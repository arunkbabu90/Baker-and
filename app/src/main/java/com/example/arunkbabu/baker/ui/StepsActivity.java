package com.example.arunkbabu.baker.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Step;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity
{
    private int mStepPosition;
    private ArrayList<Step> mStepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mStepList = getIntent().getParcelableArrayListExtra(RecipeMasterListFragment.STEP_DETAIL_KEY);
        mStepPosition = getIntent().getIntExtra(RecipeMasterListFragment.STEP_DETAIL_POSITION_KEY, -1);

        if (savedInstanceState == null) {
            StepDetailFragment stepFragment = new StepDetailFragment();
            stepFragment.setStepList(this, mStepList, mStepPosition);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_steps_container, stepFragment)
                    .commit();
        }
    }
}
