package com.example.exoplayer

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.DrmSessionManager
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto
import com.google.android.exoplayer2.drm.FrameworkMediaDrm
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback
import com.google.android.exoplayer2.drm.UnsupportedDrmException
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.util.UUID

/**
 * Created by Ashik on 23/5/19 6:03 PM.
 * ashik.ka@mobiotics.com
 * Mobiotics
 */
class DRMSampleActivity : AppCompatActivity() {

  private val DRM_LICENSE_URL =
    "https://proxy.uat.widevine.com/proxy?video_id=d286538032258a1c&provider=widevine_test"
  private val DRM_DASH_URL = "https://storage.googleapis" + ".com/wvmedia/cenc/h264/tears/tears.mpd"
private val USER_AGENT="user-agent"
  private val handler = Handler()
  private val bandwidthMeter = DefaultBandwidthMeter()
  private val drmCallback = HttpMediaDrmCallback(
      DRM_LICENSE_URL,
      DefaultHttpDataSourceFactory(USER_AGENT)
  )
  private val drmSessionManager = DefaultDrmSessionManager(C.WIDEVINE_UUID,
      FrameworkMediaDrm.newInstance(C.WIDEVINE_UUID), drmCallback, null, handler, null)
  private val selector = DefaultTrackSelector()
  private val loadControl = DefaultLoadControl()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_ui_customize)
    val player = initPlayer(isDrm = true, type = StreamingType.DASH)
    player.playWhenReady = true

  }
  private fun initPlayer(isDrm: Boolean, type: StreamingType): SimpleExoPlayer {
    val renderersFactory = if (isDrm)
      DefaultRenderersFactory(this, drmSessionManager) else DefaultRenderersFactory(this)
    val player = ExoPlayerFactory.newSimpleInstance(renderersFactory, selector, loadControl)

    val playerView = findViewById<PlayerView>(R.id.player_view)
    playerView.player = player

    val dataSourceFactory = DefaultDataSourceFactory(this, bandwidthMeter,
        DefaultHttpDataSourceFactory(USER_AGENT, bandwidthMeter)
    )
    val mediaSource = when (type) {
      StreamingType.DASH ->
        createDashSource(  DRM_DASH_URL, dataSourceFactory)

      else -> null
    }

    player.prepare(mediaSource)

    return player
  }
  private fun createDashSource(url: String,
    dataSourceFactory: DefaultDataSourceFactory): DashMediaSource {
    return DashMediaSource.Factory(
        DefaultDashChunkSource.Factory(dataSourceFactory),
        dataSourceFactory
    ).createMediaSource(Uri.parse(url))
  }


}
