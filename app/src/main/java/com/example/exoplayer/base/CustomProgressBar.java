package com.example.exoplayer.base;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by Ashik on 23/5/19 5:28 PM.
 * ashik.ka@mobiotics.com
 * Mobiotics
 */
public class CustomProgressBar {
  private ProgressBar mProgressBar;

  public CustomProgressBar(Context context) {
    ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content)
        .getRootView();

    mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
    mProgressBar.setIndeterminate(true);



    RelativeLayout.LayoutParams params = new
        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.MATCH_PARENT);

    RelativeLayout rl = new RelativeLayout(context);

    rl.setGravity(Gravity.CENTER);
    rl.addView(mProgressBar);

    layout.addView(rl, params);



  }
  public void show() {
    mProgressBar.setVisibility(View.VISIBLE);
  }

  public void hide() {
    mProgressBar.setVisibility(View.INVISIBLE);
  }
}
