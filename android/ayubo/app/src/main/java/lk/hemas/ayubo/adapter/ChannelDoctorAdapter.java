package lk.hemas.ayubo.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.model.Session;
import lk.hemas.ayubo.util.TimeFormatter;

/**
 * Created by Sabri on 3/12/2018. Adapter for schedule
 */

public class ChannelDoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    public static final int VIEW_TYPE_CHANNEL = 1;
    public static final int VIEW_TYPE_SESSION = 2;

    //instance
    private Activity activity;
    private List<Session> sessions;
    private OnChannelClickListener listener;

    //primary data
    private int viewType;

    public ChannelDoctorAdapter(Activity activity, List<Session> doctors, int viewType) {
        this.activity = activity;
        this.sessions = doctors;
        this.viewType = viewType;
    }

    public void setOnScheduleClickListener(OnChannelClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_CHANNEL) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_schedule_doctor_row, parent, false);
            return new ChannelViewHolder(view);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.component_session_row, parent, false);
            return new SessionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Session session = sessions.get(position);
        if (holder instanceof ChannelViewHolder) {
            ChannelViewHolder viewHolder = (ChannelViewHolder) holder;
            viewHolder.txtName.setText(session.getHospital());
            viewHolder.txtAction.setText(R.string.action_book);
            Date schedule = TimeFormatter.stringToDate(session.getDate(), TimeFormatter.CHANNEL_DATE_FORMAT);
            if (schedule != null) {
                viewHolder.txtDay.setText(TimeFormatter.millisecondsToString(schedule.getTime(), TimeFormatter.TIME_FORMAT_DAY));
                viewHolder.txtDate.setText(TimeFormatter.millisecondsToString(schedule.getTime(), TimeFormatter.TIME_FORMAT_WEEK_DAY));
                viewHolder.txtTime.setText(TimeFormatter.millisecondsToString(schedule.getTime(), TimeFormatter.TIME_FORMAT));
            }

        } else if (holder instanceof SessionViewHolder) {
            SessionViewHolder sessionViewHolder = (SessionViewHolder) holder;
            Date sessionDate = TimeFormatter.stringToDate(session.getDate(), TimeFormatter.SCHEDULE_DATE_FORMAT);
            if (sessionDate != null) {
                sessionViewHolder.txtDate.setText(TimeFormatter.millisecondsToString(sessionDate.getTime(), TimeFormatter.TIME_FORMAT_APPOINTMENT_DATE));
                sessionViewHolder.txtTime.setText(TimeFormatter.millisecondsToString(sessionDate.getTime(), TimeFormatter.TIME_FORMAT));
            }
            sessionViewHolder.txtNumber.setText(String.valueOf(session.getCurrentAppNumber()));
        }

        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onChannelClicked(sessions.get(pos));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (listener != null)
                    listener.onGetMoreSessionsClicked(sessions.get(pos).getHospital());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (sessions == null)
            return 0;
        else
            return sessions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDay, txtDate, txtTime, txtAction;

        ChannelViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_title_schedule_row);
            txtDay = itemView.findViewById(R.id.txt_date_schedule_row);
            txtDate = itemView.findViewById(R.id.txt_week_schedule_row);
            txtTime = itemView.findViewById(R.id.txt_time_schedule_row);
            txtAction = itemView.findViewById(R.id.txt_action_schedule_row);
        }
    }

    class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime, txtNumber;

        SessionViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date_session);
            txtTime = itemView.findViewById(R.id.txt_time_session);
            txtNumber = itemView.findViewById(R.id.txt_number_session);
        }
    }

    public interface OnChannelClickListener {
        void onChannelClicked(Session channelDoctor);

        void onGetMoreSessionsClicked(String location);
    }
}
