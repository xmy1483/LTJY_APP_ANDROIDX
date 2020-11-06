package com.wjz.weexlib.weex.delegate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public interface ActivityWeexDelegate {

    /**
     * This method must be called from {@link AppCompatActivity#onCreate(Bundle)}.
     * This method internally creates the presenter and attaches the view to it.
     */
    void onCreate(Bundle bundle);

    /**
     * This method must be called from {@link AppCompatActivity#onDestroy()}}.
     * This method internally detaches the view from presenter
     */
    void onDestroy();

    /**
     * This method must be called from {@link AppCompatActivity#onPause()}
     */
    void onPause();

    /**
     * This method must be called from {@link AppCompatActivity#onResume()}
     */
    void onResume();

    /**
     * This method must be called from {@link AppCompatActivity#onStart()}
     */
    void onStart();

    /**
     * This method must be called from {@link AppCompatActivity#onStop()}
     */
    void onStop();

    /**
     * This method must be called from {@link AppCompatActivity#onRestart()}
     */
    void onRestart();

    /**
     * This method must be called from {@link AppCompatActivity#onContentChanged()}
     */
    void onContentChanged();

    /**
     * This method must be called from {@link AppCompatActivity#onSaveInstanceState(Bundle)}
     */
    void onSaveInstanceState(Bundle outState);

    /**
     * Must be called from {@link AppCompatActivity#onBackPressed()}
     */
    boolean onBackPressed();

}
