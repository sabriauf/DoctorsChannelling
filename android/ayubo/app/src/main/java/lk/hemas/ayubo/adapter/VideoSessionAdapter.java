package lk.hemas.ayubo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.model.Expert;
import lk.hemas.ayubo.util.TimeFormatter;

public class VideoSessionAdapter extends RecyclerView.Adapter<VideoSessionAdapter.VideoViewHolder> {

    //instances
    private Context context;
    private List<Expert.Location> locations;
    private OnVideoSessionListener listener;

    public VideoSessionAdapter(Context context, List<Expert.Location> locations) {
        this.context = context;
        this.locations = locations;
    }

    public void setOnVideoSessionListener(OnVideoSessionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoSessionAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_video_session_row, parent, false);
        return new VideoSessionAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoSessionAdapter.VideoViewHolder holder, int position) {
        Expert.Location location = locations.get(position);
        Date sessionDate = TimeFormatter.stringToDate(location.getNext_available(), "yyyy-MM-dd hh:mm:ss");
        if (sessionDate != null) {
            holder.txtDate.setText(TimeFormatter.millisecondsToString(sessionDate.getTime(), "EEEE, MMM dd, yyyy"));
            holder.txtTime.setText(TimeFormatter.millisecondsToString(sessionDate.getTime(), "h:mm aa"));
        }

        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onVideoSessionClicked(locations.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(locations != null)
            return locations.size();
        else
            return 0;
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime;

        VideoViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_video_session_date);
            txtTime = itemView.findViewById(R.id.txt_video_session_time);
        }
    }

    public interface OnVideoSessionListener {
        void onVideoSessionClicked(Expert.Location location);
    }
}
