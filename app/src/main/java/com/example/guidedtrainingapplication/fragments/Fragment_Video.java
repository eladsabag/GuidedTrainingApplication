package com.example.guidedtrainingapplication.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import androidx.fragment.app.Fragment;
import com.example.guidedtrainingapplication.R;

public class Fragment_Video extends Fragment {
    // Fragment Video Views
    private VideoView videoView;
    private String uriPath;
    private Uri uri;

    // Fragment Video Flow
    private Activity activity;
    private String initialMuscle;
    private boolean isPlaying = true;
    private int currentPosition;

    public Fragment_Video(Activity activity,String initialMuscle) {
        this.activity = activity;
        this.initialMuscle = initialMuscle;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        findViews(view);
        initViews();
        return view;
    }

    /**
     * This function init all the views according to a given view.
     * @param view - The view needed to find all the views.
     */
    private void findViews(View view) { videoView = view.findViewById(R.id.video_view); }

    /**
     * This function init all the views.
     */
    private void initViews() { initUri(initialMuscle); }

    /**
     * This function init the video uri according to given muscle name.
     * @param muscleName - The muscle that need to be shown in the video.
     */
    public void initUri(String muscleName) {
        switch (muscleName) {
            case "Chest":
                uriPath = "android.resource://"+activity.getPackageName()+"/"+R.raw.chest;
                uri = Uri.parse(uriPath);
                break;
            case "Back":
                uriPath = "android.resource://"+activity.getPackageName()+"/"+R.raw.back;
                uri = Uri.parse(uriPath);
                break;
            case "Legs":
                uriPath = "android.resource://"+activity.getPackageName()+"/"+R.raw.legs;
                uri = Uri.parse(uriPath);
                break;
            case "Shoulders":
                uriPath = "android.resource://"+activity.getPackageName()+"/"+R.raw.shoulders;
                uri = Uri.parse(uriPath);
                break;
            case "Biceps":
                uriPath = "android.resource://"+activity.getPackageName()+"/"+R.raw.biceps;
                uri = Uri.parse(uriPath);
                break;
            case "Triceps":
                uriPath = "android.resource://"+activity.getPackageName()+"/"+R.raw.triceps;
                uri = Uri.parse(uriPath);
                break;
            case "Abs":
                uriPath = "android.resource://"+activity.getPackageName()+"/"+R.raw.abs;
                uri = Uri.parse(uriPath);
                break;
            default:
                uriPath = "android.resource://"+activity.getPackageName()+"/"+R.raw.rest;
                uri = Uri.parse(uriPath);
                break;
        }
        videoView.setVideoURI(uri);
    }

    /**
     * This function handles all the changes required when pause or resume is clicked.
     */
    public void pauseAndResumeVideo() {
        if(isPlaying) {
            currentPosition = videoView.getCurrentPosition();
            videoView.pause();
        } else {
            videoView.seekTo(currentPosition);
            videoView.start();
        }
        isPlaying = !isPlaying;
    }

    /**
     * This function restart a video when it ends if needed.
     */
    public void restartVideoIfNeeded() {
        if(!videoView.isPlaying())
            startVideo();
    }

    /**
     * This function starts the current video.
     */
    public void startVideo() { videoView.start(); }

    public boolean isPlaying() {
        return isPlaying;
    }

}
