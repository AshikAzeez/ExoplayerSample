package com.example.exoplayer;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.exoplayer.base.CustomProgressBar;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.ext.ima.ImaAdsMediaSource;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.io.IOException;

public class UiCustomizeActivity extends AppCompatActivity implements VideoRendererEventListener {
  private static final String TAG = "UiCustomizeActivity";
  private PlayerView playerView;
  private SimpleExoPlayer simpleExoPlayer;
  private final String BASE_URL="http://blueappsoftware.in/";
  private final String USER_AGENT="user_agent";



  Uri mp4VideoUri = Uri.parse("http://live.field59.com/wwsb/ngrp:wwsb1_all/playlist.m3u8");

  private PlayerControlView playerControlView;
  // private ImageButton exoFullScree;
  private final String STATE_RESUME_WINDOW = "resumeWindow";
  private final String STATE_RESUME_POSITION = "resumePosition";
  private final String STATE_PLAYER_FULL_SCREEN = "playerFullScreen";
  private boolean mExoPlayerFullScreen = false;
  private FrameLayout mFullScreenButton;
  ImageView mFullScreenIcon;
  private int mResumeWindow;
  private long mResumePosition;

  DataSource.Factory dataSourceFactory;

  private static final String ACTION_VIEW="com.google.android.exoplayer.demo.action.VIEW";
  private static final String EXTENSTION_EXTRA="extension";
  private static final String ACTION_VIEW_LIST= "com.google.android.exoplayer.demo.action.VIEW_LIST";
  private static final String URI_LIST_EXTRA="url_list";
  private static final String EXTENSION_LIST_EXTRA="extension_list";
final private String AD_TAG_URI="https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&amp;iu=/124319096/external/ad_rule_samples&amp;ciu_szs=300x250&amp;ad_rule=1&amp;impl=s&amp;gdfp_req=1&amp;env=vp&amp;output=vmap&amp;unviewed_position_start=1&amp;cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpostoptimizedpodbumper&amp;cmsid=496&amp;vid=short_onecue&amp;correlator=";

  //Minimum Video you want to buffer while Playing
  public static final int MIN_BUFFER_DURATION = 25000;
  //Max Video you want to buffer during PlayBack
  public static final int MAX_BUFFER_DURATION = 30000;
  //Min Video you want to buffer before start Playing it
  public static final int MIN_PLAYBACK_START_BUFFER = 10000;
  //Min video You want to buffer when user resumes video
  public static final int MIN_PLAYBACK_RESUME_BUFFER = 10000;

  private TrackSelector selector=new DefaultTrackSelector();


  private final LoadControl loadControl=new DefaultLoadControl(
      new DefaultAllocator(true,16),MIN_BUFFER_DURATION,MAX_BUFFER_DURATION,
      MIN_PLAYBACK_START_BUFFER,MIN_PLAYBACK_RESUME_BUFFER,0,false
  );


private CustomProgressBar customProgressBar;





  @Override protected void onSaveInstanceState(Bundle outState) {
    outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
    outState.putLong(STATE_RESUME_POSITION, mResumePosition);
    outState.putBoolean(STATE_PLAYER_FULL_SCREEN, mExoPlayerFullScreen);
    super.onSaveInstanceState(outState);
  }

  private MediaSource buildMediaSource(Uri uri,String s) {
    @C.ContentType
    int type = Util.inferContentType(uri);
    /*DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(UiCustomizeActivity.this,
        Util.getUserAgent(this, "ExoPlayerSample"));*/
    MediaSource mediaSource = null;
    switch (type) {
      case C.TYPE_DASH:
        mediaSource= new DashMediaSource(uri, dataSourceFactory,
            new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);
        Log.i(TAG, "buildMediaSource: dash");
        break;
      case C.TYPE_SS:
        mediaSource=new SsMediaSource(uri,dataSourceFactory,new DefaultSsChunkSource.Factory(dataSourceFactory),null,null);
        Log.i(TAG, "buildMediaSource: ss");
      //  mediaSource=new SsMediaSource.Factory(dataSourceFactory)
        break;
      case C.TYPE_HLS:
        //mediaSource = new HlsMediaSource(uri, dataSourceFactory, 1,null, null);
        Log.i(TAG, "buildMediaSource: HLS");
        mediaSource =
            new HlsMediaSource.Factory(new DefaultDataSourceFactory(this,
                Util.getUserAgent(this,getResources().getString(R.string.app_name)))).createMediaSource(uri);
        break;
      case C.TYPE_OTHER:
      default:
        //mediaSource=new ExtractorMediaSource(uri,dataSourceFactory,new DefaultExtractorsFactory(),null, null);
        Log.i(TAG, "buildMediaSource: other");
        mediaSource=new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this,
            Util.getUserAgent(this,getResources().getString(R.string.app_name))))
            .setExtractorsFactory(new DefaultExtractorsFactory())
            .createMediaSource(uri);
        break;
    }

    return mediaSource;
  }
private MediaSource createMediaSource(Uri url){
    MediaSource contentMediaSource=buildMediaSource(url,"");
    return contentMediaSource;

}


private AdsMediaSource createMediaSourceWithAds(Uri uri, PlayerView simpleExoPlayerView){
  String userAgent=Util.getUserAgent(this,getString(R.string.app_name));
  MediaSource contentMediaSource=buildMediaSource(uri,"");
  ImaAdsLoader imaAdsLoader=new ImaAdsLoader(this,Uri.parse(AD_TAG_URI));
  AdsMediaSource contentMediaSourceAds=new AdsMediaSource(
      contentMediaSource, new DefaultDataSourceFactory(this, userAgent), imaAdsLoader,
      simpleExoPlayerView.getOverlayFrameLayout());
  return contentMediaSourceAds;


}
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ui_customize);
    customProgressBar=new CustomProgressBar(this);
    if (savedInstanceState != null) {
      mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
      mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
      mExoPlayerFullScreen = savedInstanceState.getBoolean(STATE_PLAYER_FULL_SCREEN);
    }


    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    TrackSelection.Factory videoTrackSelectionFactory =
        new AdaptiveTrackSelection.Factory(bandwidthMeter);

    TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

    simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
        trackSelector);

    playerView = findViewById(R.id.player_view);
    playerControlView = findViewById(R.id.player_control_view);
    int h = playerView.getResources().getConfiguration().screenHeightDp;
    int w = playerView.getResources().getConfiguration().screenWidthDp;
    playerView.requestFocus();
    playerView.setPlayer(simpleExoPlayer);



    MediaSource videoSource = createMediaSource(mp4VideoUri);
    MediaSource adsVideoSource = createMediaSourceWithAds(Uri.parse(AD_TAG_URI),playerView);



    final LoopingMediaSource loopingMediaSource = new LoopingMediaSource(videoSource);
 //  simpleExoPlayer.prepare(videoSource);
   simpleExoPlayer.prepare(adsVideoSource);



    simpleExoPlayer.addListener(new Player.EventListener() {
      @Override public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        Log.i(TAG, "onTimelineChanged: ");
      }

      @Override public void onTracksChanged(TrackGroupArray trackGroups,
          TrackSelectionArray trackSelections) {
        Log.i(TAG, "onTracksChanged: ");
      }

      @Override public void onLoadingChanged(boolean isLoading) {
        Log.i(TAG, "onLoadingChanged: ");
      }

      private String status;

      @Override public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.i(TAG, "onPlayerStateChanged: ");
customProgressBar.hide();
        switch (playbackState) {
          case Player.STATE_BUFFERING:
            status = PlayBackStatus.LOADING;
            customProgressBar.show();
            //CustomProgressBar.show(UiCustomizeActivity.this);
            break;
          case Player.STATE_ENDED:
            status = PlayBackStatus.STOPPED;
            break;
          case Player.STATE_IDLE:
            status = PlayBackStatus.IDLE;
            break;
          case Player.STATE_READY:
            status = playWhenReady ? PlayBackStatus.PLAYING : PlayBackStatus.PAUSED;
            break;
          default:
            break;
        }
      }

      @Override public void onRepeatModeChanged(int repeatMode) {
        Log.i(TAG, "onRepeatModeChanged: ");
      }

      @Override public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        Log.i(TAG, "onShuffleModeEnabledChanged: ");
      }

      @Override public void onPlayerError(ExoPlaybackException error) {
        Log.i(TAG, "onPlayerError: ");
        if (error.type == ExoPlaybackException.TYPE_SOURCE) {
          IOException cause = error.getSourceException();
          if (cause instanceof HttpDataSource.HttpDataSourceException) {//HTTP Error
            HttpDataSource.HttpDataSourceException httpError =
                (HttpDataSource.HttpDataSourceException) cause;
            Log.e(TAG, "onPlayerError: ",httpError );
            DataSpec requestDataSpec = httpError.dataSpec;
            if (httpError instanceof HttpDataSource.InvalidResponseCodeException) {
              /// Cast to InvalidResponseCodeException and retrieve the response code,
              //        // message and headers.
            } else{

            }
          }
        }
        simpleExoPlayer.stop();
        simpleExoPlayer.prepare(loopingMediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
      }

      @Override public void onPositionDiscontinuity(int reason) {
        Log.i(TAG, "onPositionDiscontinuity: ");
      }

      @Override public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.i(TAG, "onPlaybackParametersChanged: ");
      }

      @Override public void onSeekProcessed() {
        Log.i(TAG, "onSeekProcessed: ");
      }
    });
    simpleExoPlayer.setPlayWhenReady(true);
    simpleExoPlayer.setVideoDebugListener(this);
    ;
  }

  @Override public void onVideoEnabled(DecoderCounters counters) {
    Log.i(TAG, "onVideoEnabled: ");
  }

  @Override public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs,
      long initializationDurationMs) {
    Log.i(TAG, "onVideoDecoderInitialized: ");
  }

  @Override public void onVideoInputFormatChanged(Format format) {
    Log.i(TAG, "onVideoInputFormatChanged: ");
  }

  @Override public void onDroppedFrames(int count, long elapsedMs) {
    Log.i(TAG, "onDroppedFrames: ");
  }

  @Override public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
      float pixelWidthHeightRatio) {

    Log.i(TAG, "onVideoSizeChanged: ");
  }

  @Override public void onRenderedFirstFrame(Surface surface) {
    Log.i(TAG, "onRenderedFirstFrame: ");
  }

  @Override public void onVideoDisabled(DecoderCounters counters) {
    Log.i(TAG, "onVideoDisabled: ");
  }

  @Override protected void onDestroy() {
    Log.i(TAG, "onDestroy: ");
    super.onDestroy();
  }

  @Override protected void onStart() {
    super.onStart();
    if (Util.SDK_INT>23){

    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      simpleExoPlayer.release();
    }
  }

  @Override protected void onStop() {
    super.onStop();
    /**
     * Enable Multi screen
     * */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      simpleExoPlayer.release();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      hideSystemUi();
    } else {
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) private void hideSystemUi() {
    playerView.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {

    }
  }

  @Override protected void onResume() {
    super.onResume();
  }
}
