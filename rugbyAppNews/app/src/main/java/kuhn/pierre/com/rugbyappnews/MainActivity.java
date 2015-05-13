package kuhn.pierre.com.rugbyappnews;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import kuhn.pierre.com.rugbyappnews.adapter.VideoAdapter;
import kuhn.pierre.com.rugbyappnews.rest.RestClient;
import kuhn.pierre.com.rugbyappnews.utils.Video;

/**
 * Main Activity class that contains a listView of videos and a Youtube player.
 */
public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=";
    static final String BUNDLE_SAVE_IS_SHOWN = "isVideoShown";
    static final String BUNDLE_SAVE_IS_FULL_SCREEN = "isFullScreen";

    private ListView mVideoListView;
    private YouTubePlayerView mYouTubePlayerView;
    private RelativeLayout mPlayerLayout;
    private Button mClosePlayerB;
    private ShareButton fbShareB;
    private Button tweetB;

    private List<Video> mVideoList;
    private String mVideoSelectedId;
    private boolean isVideoShown;
    private boolean isFullScreen;
    private YouTubePlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise FB sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        // Initialise Twitter sdk
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(getString(R.string.twitter_key_1),
                        getString(R.string.twitter_key_2));
        Fabric.with(this, new Twitter(authConfig));
        Fabric.with(this, new TweetComposer());

        //Get the views
        mVideoListView = (ListView) findViewById(R.id.videoLV);
        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        mPlayerLayout = (RelativeLayout) findViewById(R.id.player_layout);
        mClosePlayerB = (Button) findViewById(R.id.player_close_button);
        fbShareB = (ShareButton) findViewById(R.id.fb_share_button);
        tweetB = (Button) findViewById(R.id.twitter_share_button);

        fbShareB.setBackgroundResource(R.drawable.facebook);
        fbShareB.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        fbShareB.setText("");

        mVideoList = RestClient.getInstance().getmVideoList();
        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext(), mVideoList);
        mVideoListView.setAdapter(videoAdapter);

        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean(BUNDLE_SAVE_IS_SHOWN)) {
                if(savedInstanceState.getBoolean(BUNDLE_SAVE_IS_FULL_SCREEN)){
                    isFullScreen = true;
                    mPlayerLayout.setVisibility(View.VISIBLE);
                }
                isVideoShown = true;
                mYouTubePlayerView.initialize(getString(R.string.youtube_api_key), this);
            }
        }


        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                loadYoutubePlayerOnScreen(mVideoList.get(position).getUrl());
                // Set the listView below the Youtube Player.
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.player_layout);
                mVideoListView.setLayoutParams(params);
            }
        });

        mClosePlayerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.release();
                hidePlayerOnScreen();
                isVideoShown = false;
            }
        });

        tweetB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build the tweet
                TweetComposer.Builder builder = new TweetComposer.Builder(MainActivity.this)
                        .text(YOUTUBE_VIDEO_URL+mVideoSelectedId);
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void loadYoutubePlayerOnScreen(String videoId){
        // Load the Youtube Player
        isVideoShown = true;
        mVideoSelectedId = videoId;
        // Prepare the FB post
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(YOUTUBE_VIDEO_URL+mVideoSelectedId))
                .build();
        fbShareB.setShareContent(content);

        if(mPlayer != null)
            mPlayer.release();
        mYouTubePlayerView.initialize(getString(R.string.youtube_api_key), this);
        showPlayerOnScreen();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            //First time the Player is initialized
            if(mVideoSelectedId != null) {
                mPlayer = youTubePlayer;
                youTubePlayer.loadVideo(mVideoSelectedId);
            }
        }
        else{
            youTubePlayer.play();
            if(isFullScreen)
                isFullScreen = false;
            else
                isFullScreen = true;
            mPlayer = youTubePlayer;
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(MainActivity.this, getString(R.string.player_load_fail), Toast.LENGTH_SHORT).show();
    }


    /**
     * Show the Youtube Player on the screen with an animation slide in top
     */
    private void showPlayerOnScreen(){
        mPlayerLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_top);
        mPlayerLayout.startAnimation(animation);
    }

    /**
     * Hide de player on the screen with an animation slide out top
     */
    private void hidePlayerOnScreen(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_out_top);
        mPlayerLayout.startAnimation(animation);
        mPlayerLayout.setVisibility(View.GONE);
    }



    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        // Save states in the Bundle object to handle screen orientation changes
        if(isVideoShown)
            bundle.putBoolean(BUNDLE_SAVE_IS_SHOWN, isVideoShown);
        else
            bundle.putBoolean(BUNDLE_SAVE_IS_SHOWN, false);
        if(isFullScreen )
            bundle.putBoolean(BUNDLE_SAVE_IS_FULL_SCREEN, isFullScreen);
        else
            bundle.putBoolean(BUNDLE_SAVE_IS_FULL_SCREEN, false);

        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onBackPressed() {
        if(isVideoShown && isFullScreen){
            mPlayer.setFullscreen(false);
        }
        else if(isVideoShown){
            mPlayer.release();
            hidePlayerOnScreen();
            isVideoShown = false;
        }
    }

}
