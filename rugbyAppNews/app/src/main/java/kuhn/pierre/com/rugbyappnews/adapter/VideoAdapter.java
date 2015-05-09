package kuhn.pierre.com.rugbyappnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import kuhn.pierre.com.rugbyappnews.utils.Video;

/**
 * Created by pierre on 09/05/2015.
 */
public class VideoAdapter extends BaseAdapter {

    List<Video> mVideoList;
    private LayoutInflater mInflater;


    public VideoAdapter(Context context, List<Video> videoList){
        this.mVideoList = videoList;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mVideoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVideoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //convertView = mInflater.inflate(R.layout.list_video);



        }
    }
}
