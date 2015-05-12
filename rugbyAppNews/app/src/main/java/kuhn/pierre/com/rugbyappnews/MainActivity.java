package kuhn.pierre.com.rugbyappnews;

import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import kuhn.pierre.com.rugbyappnews.adapter.VideoAdapter;
import kuhn.pierre.com.rugbyappnews.rest.RestClient;
import kuhn.pierre.com.rugbyappnews.utils.OnSwipeTouchListener;
import kuhn.pierre.com.rugbyappnews.utils.Video;


public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private ListView mVideoListView;
    private List<Video> mVideoList;
    private YouTubePlayerView mYouTubePlayerView;
    private RelativeLayout mPlayerLayout;
    private String mVideoSelectedId;
    private Button mClosePlayerB;
    private boolean isVideoShown;
    private YouTubePlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mVideoListView = (ListView) findViewById(R.id.videoLV);
        mPlayerLayout = (RelativeLayout) findViewById(R.id.player_layout);
        mVideoList = RestClient.getInstance().getmVideoList();
        mClosePlayerB = (Button) findViewById(R.id.player_close_button);
        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext(), mVideoList);
        mVideoListView.setAdapter(videoAdapter);
        mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadYoutubePlayerOnScreen(mVideoList.get(position).getUrl());
            }
        });

        mClosePlayerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.release();
                mPlayerLayout.setVisibility(View.GONE);
                isVideoShown = false;
            }
        });

        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);



        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean("isVideoShown"))
                mYouTubePlayerView.initialize("AIzaSyCdoQ7E1XOgtMRskJ4-EZN3b19VMz5u9do",this);
        }



        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        isVideoShown = true;
        mPlayerLayout.setVisibility(View.VISIBLE);
        mVideoSelectedId = videoId;
        if(mPlayer != null)
            mPlayer.release();
        mYouTubePlayerView.initialize("AIzaSyCdoQ7E1XOgtMRskJ4-EZN3b19VMz5u9do",this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b) {
            mPlayer = youTubePlayer;
            youTubePlayer.loadVideo(mVideoSelectedId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        if(isVideoShown)
            bundle.putBoolean("isVideoShown", isVideoShown);
        else
            bundle.putBoolean("isVideoShown", false);

        super.onSaveInstanceState(bundle);
    }

}
