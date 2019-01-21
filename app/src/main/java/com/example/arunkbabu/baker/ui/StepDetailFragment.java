package com.example.arunkbabu.baker.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.arunkbabu.baker.R;
import com.example.arunkbabu.baker.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment implements View.OnClickListener
{
    @BindView(R.id.tv_steps_desc) TextView mDescriptionTextView;
    @BindView(R.id.tv_video_err) TextView mVideoErrorTextView;
    @BindView(R.id.btn_next_step) Button mNextButton;
    @BindView(R.id.btn_previous_step) Button mPreviousButton;
    @BindView(R.id.exoplayer_view) SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;

    private int mStepPosition;
    private long cSeekPosition;
    private boolean isLandscapeMode;
    private String mVideoURL;
    private String mThumbnailURL;
    private ArrayList<Step> mStepList;
    private Context mContext;

    private static final String STEP_POSITION_SAV = "step_pos_sav";
    private static final String SEEK_POSITION_SAV = "seek_pos_sav";
    private static final String VIDEO_URL_SAV = "video_url_sav";
    private static final String THUMBNAIL_URL_SAV = "thumbnail_url_sav";
    private static final String STEP_LIST_SAV = "step_list_sav";


    public StepDetailFragment() {
        // Required public constructor for initialization
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);
        if (view.findViewById(R.id.step_detail_landscape) != null) {
            isLandscapeMode = true;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        } else {
            isLandscapeMode = false;
        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mStepPosition = savedInstanceState.getInt(STEP_POSITION_SAV);
            cSeekPosition = savedInstanceState.getLong(SEEK_POSITION_SAV);
            mVideoURL = savedInstanceState.getString(VIDEO_URL_SAV);
            mThumbnailURL = savedInstanceState.getString(THUMBNAIL_URL_SAV);
            mStepList = savedInstanceState.getParcelableArrayList(STEP_LIST_SAV);
        }

        changeStepPage(mStepPosition);

        // Seek to the last seek position & resume playback when the screen rotates
        //  or during config changes
        if (savedInstanceState != null && mExoPlayer != null) {
            mExoPlayer.seekTo(cSeekPosition);
            mExoPlayer.setPlayWhenReady(mExoPlayer.getPlayWhenReady());
        }

        mNextButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_next_step:
                // Go to next step
                changeStepPage(mStepPosition += 1);
                break;
            case R.id.btn_previous_step:
                // Go to previous step
                changeStepPage(mStepPosition -= 1);
                break;
        }
    }


    /**
     * Navigates the current step page to the given position
     * @param position The page position to be loaded
     */
    private void changeStepPage(int position) {
        Step stepDetail = mStepList.get(position);

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }

        if (!isLandscapeMode) {
            if (mStepPosition == mStepList.size() - 1) {
                mNextButton.setVisibility(View.INVISIBLE);
            } else if (mStepPosition == 0) {
                mPreviousButton.setVisibility(View.INVISIBLE);
            } else {
                mNextButton.setVisibility(View.VISIBLE);
                mPreviousButton.setVisibility(View.VISIBLE);
            }
        }

        mDescriptionTextView.setText(stepDetail.getDescription());
        mVideoURL = stepDetail.getVideoURL();
        mThumbnailURL = stepDetail.getThumbnailURL();

        // Use the videoURL to play the video if it's NOT Empty
        if (!mVideoURL.equals("")) {
            mPlayerView.setVisibility(View.VISIBLE);
            mVideoErrorTextView.setVisibility(View.GONE);
            initializeMediaPlayer(Uri.parse(mVideoURL));
        } else if (!mThumbnailURL.equals("")) {
            // Use the thumbnailURL to play the video only if it's NOT Empty
            mPlayerView.setVisibility(View.VISIBLE);
            mVideoErrorTextView.setVisibility(View.GONE);
            initializeMediaPlayer(Uri.parse(mThumbnailURL));
        } else {
            // If there's not data then show No Video Found text view
            mVideoErrorTextView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        }
    }



    /**
     * Initializes the media player for video playback
     * @param mediaUri The Uri of the media to be played
     */
    private void initializeMediaPlayer(Uri mediaUri) {

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory trackSelFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelFactory);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, new DefaultLoadControl());

        String userAg = Util.getUserAgent(mContext, "Baker");
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAg, bandwidthMeter);
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                dataSourceFactory,
                new DefaultExtractorsFactory(), null, null);
        mPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }


    /**
     * Set the List of steps to the calling fragment
     * @param context The Activity context
     * @param stepList The List of Steps
     * @param stepPosition The current selected position of the step in the List
     */
    public void setStepList(Context context, ArrayList<Step> stepList, int stepPosition) {
        mStepList = stepList;
        mContext = context;
        mStepPosition = stepPosition;
    }



    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            cSeekPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.getPlayWhenReady();
            // On Devices lower than nougat; there's no guarantee that the onStop() will get called
            //  so the player needs to be released as early as possible
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                mExoPlayer.stop();
                mExoPlayer.release();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if ((mVideoURL != null || mThumbnailURL != null) && mStepList != null) {
            outState.putInt(STEP_POSITION_SAV, mStepPosition);
            outState.putLong(SEEK_POSITION_SAV, cSeekPosition);
            outState.putString(VIDEO_URL_SAV, mVideoURL);
            outState.putString(THUMBNAIL_URL_SAV, mThumbnailURL);
            outState.putParcelableArrayList(STEP_LIST_SAV, mStepList);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // onStop() is guaranteed to be called on Devices greater than Marshmallow
        //  so it's safe to release it here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mStepPosition = savedInstanceState.getInt(STEP_POSITION_SAV);
            cSeekPosition = savedInstanceState.getLong(SEEK_POSITION_SAV);
            mVideoURL = savedInstanceState.getString(VIDEO_URL_SAV);
            mThumbnailURL = savedInstanceState.getString(THUMBNAIL_URL_SAV);
            mStepList = savedInstanceState.getParcelableArrayList(STEP_LIST_SAV);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mExoPlayer == null || Util.SDK_INT >= Build.VERSION_CODES.N) {
            changeStepPage(mStepPosition);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null || Util.SDK_INT <= Build.VERSION_CODES.M) {
            changeStepPage(mStepPosition);
        }
        if (mExoPlayer != null) {
            mExoPlayer.getPlayWhenReady();
            mExoPlayer.seekTo(cSeekPosition);
        }
    }
}
