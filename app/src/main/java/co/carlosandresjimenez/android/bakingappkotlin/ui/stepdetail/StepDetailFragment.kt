/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Carlos Andres Jimenez <apps@carlosandresjimenez.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package co.carlosandresjimenez.android.bakingappkotlin.ui.stepdetail

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import co.carlosandresjimenez.android.bakingappkotlin.R
import co.carlosandresjimenez.android.bakingappkotlin.data.model.BakingStep
import co.carlosandresjimenez.android.bakingappkotlin.ui.recipedetail.DetailActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.squareup.picasso.Picasso

/**
 * Created by carlosjimenez on 1/3/18.
 */
class StepDetailFragment : Fragment() {

    val LOG_TAG = StepDetailFragment::class.simpleName

    companion object {
        private val STEP_DETAIL_PARAMS = "STEP_DETAIL_PARAMS"
        private val STEP_DETAIL_POSITION = "STEP_DETAIL_POSITION"
        private val STEP_DETAIL_AUTOPLAY = "STEP_DETAIL_AUTOPLAY"

        fun newInstance(step: BakingStep): StepDetailFragment {
            val args = Bundle()
            args.putParcelable(STEP_DETAIL_PARAMS, step)
            val fragment = StepDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var rootView: View
    private var exoPlayer: SimpleExoPlayer? = null
    private var playerView: SimpleExoPlayerView? = null
    private var shortDescription: TextView? = null
    private var description: TextView? = null
    private var imageView: ImageView? = null

    private lateinit var bakingStep: BakingStep

    private var shouldAutoPlay: Boolean = false
    private var currentPosition: Long = 0
    private var showImmersive: Boolean = false
    private var twoPane: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_step_detail, container, false)

        twoPane = resources.getBoolean(R.bool.show_master_detail_flow)
        if (!twoPane) {
            setHasOptionsMenu(true)
        }

        bakingStep = arguments!!.getParcelable(STEP_DETAIL_PARAMS)

        playerView = rootView.findViewById(R.id.stepVideo)
        shortDescription = rootView.findViewById(R.id.shortDescription)
        description = rootView.findViewById(R.id.description)
        imageView = rootView.findViewById(R.id.stepImage)

        shortDescription?.text = bakingStep.shortDescription
        description?.text = bakingStep.description

        currentPosition = savedInstanceState?.getLong(STEP_DETAIL_POSITION) ?: 0
        shouldAutoPlay = savedInstanceState?.getBoolean(STEP_DETAIL_AUTOPLAY) ?: true

        showImmersive = resources.getBoolean(R.bool.show_immersive_mode)

        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(STEP_DETAIL_POSITION, exoPlayer?.currentPosition ?: 0)
        outState.putBoolean(STEP_DETAIL_AUTOPLAY, exoPlayer?.playWhenReady ?: false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            android.R.id.home -> {
                if (showImmersive) {
                    showSystemUI()
                }

                fragmentManager!!.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeMedia() {
        when {
            bakingStep.videoURL.endsWith("mp4", true) -> initializePlayer(bakingStep.videoURL)
            bakingStep.thumbnailURL.endsWith("jpg", true) -> displayPhoto(bakingStep.thumbnailURL)
            else -> displayPhoto()
        }
    }

    private fun displayPhoto(mediaUri: String = "") {
        playerView?.visibility = View.GONE

        if(!mediaUri.isEmpty()) {
            Picasso.with(context)
                    .load(mediaUri)
                    .placeholder(R.drawable.ic_forest_fruit_cake)
                    .into(imageView)
        } else {
            Picasso.with(context)
                    .load(R.drawable.ic_forest_fruit_cake)
                    .into(imageView)
        }
    }

    private fun initializePlayer(mediaUri: String) {
        imageView?.visibility = View.GONE

        if (showImmersive) {
            hideSystemUI()
        }

        // Create a default TrackSelector
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // Create the player
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        playerView?.player = exoPlayer

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, getString(R.string.app_name)), bandwidthMeter)
        // This is the MediaSource representing the media to be played.
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(mediaUri))

        // Prepare the player with the source.
        exoPlayer?.playWhenReady = shouldAutoPlay
        exoPlayer?.prepare(videoSource)
        exoPlayer?.seekTo(currentPosition)

    }

    private fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        rootView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        (activity as DetailActivity).hideToolbar()
    }

    private fun showSystemUI() {
        rootView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        (activity as DetailActivity).showToolbar()
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer?.stop()
            exoPlayer?.release()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializeMedia()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT <= 23)) {
            initializeMedia()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }
}