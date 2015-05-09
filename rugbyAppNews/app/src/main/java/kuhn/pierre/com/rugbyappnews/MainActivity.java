package kuhn.pierre.com.rugbyappnews;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import kuhn.pierre.com.rugbyappnews.adapter.VideoAdapter;
import kuhn.pierre.com.rugbyappnews.rest.RestClient;
import kuhn.pierre.com.rugbyappnews.utils.Video;


public class MainActivity extends ActionBarActivity {

    private ListView mVideoListView;
    private List<Video> mVideoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoListView = (ListView) findViewById(R.id.videoLV);
        mVideoList = RestClient.getInstance().getmVideoList();
        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext(), mVideoList);
        mVideoListView.setAdapter(videoAdapter);
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
}
