package kuhn.pierre.com.rugbyappnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

import kuhn.pierre.com.rugbyappnews.R;
import kuhn.pierre.com.rugbyappnews.utils.Video;

/**
 * Created by pierre
 * Adapter class which constructs the list of video.
 * The thumbnail images are displayed using Universal Image Loader Library.
 */
public class VideoAdapter extends BaseAdapter {

    private List<Video> mVideoList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    public VideoAdapter(Context context, List<Video> videoList){
        this.mVideoList = videoList;
        this.mInflater = LayoutInflater.from(context);
        mImageLoader = ImageLoader.getInstance();
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
            convertView = mInflater.inflate(R.layout.list_video, null);
        }
        Video videoTmp = mVideoList.get(position);
        ImageView thumbIV = (ImageView) convertView.findViewById(R.id.thumbIV);
        TextView title = (TextView) convertView.findViewById(R.id.titleTV);
        mImageLoader.displayImage(videoTmp.getThumbUrl(),thumbIV);
        title.setText(videoTmp.getTitle());

        return convertView;
    }
}
