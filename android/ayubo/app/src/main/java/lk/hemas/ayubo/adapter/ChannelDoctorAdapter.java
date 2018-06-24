package lk.hemas.ayubo.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.activity.SourceActivity;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.Appointment;
import lk.hemas.ayubo.model.Expert;
import lk.hemas.ayubo.model.Hospital;
import lk.hemas.ayubo.model.Session;
import lk.hemas.ayubo.model.VideoSession;
import lk.hemas.ayubo.util.TimeFormatter;

/**
 * Created by Sabri on 3/12/2018. Adapter for schedule
 */

public class ChannelDoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_CHANNEL = 1;
    public static final int VIEW_TYPE_SESSION = 2;
    public static final int VIEW_TYPE_HOSPITAL = 3;
    public static final int VIEW_TYPE_SOURCE = 4;
    public static final int VIEW_TYPE_VIDEO = 5;
    private static final String AVAILABLE = "Available";

    //instance
    private Activity activity;
    private List<Object> sessions;
    private OnChannelClickListener listener;

    //primary data
    private int viewType;

    public ChannelDoctorAdapter(Activity activity, List<Object> doctors, int viewType) {
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
        } else if (viewType == VIEW_TYPE_HOSPITAL) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_hospital_row, parent, false);
            return new HospitalViewHolder(view);
        } else if (viewType == VIEW_TYPE_SOURCE) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_source_row, parent, false);
            return new SourceViewHolder(view);
        } else if (viewType == VIEW_TYPE_VIDEO) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_video_session_row, parent, false);
            return new VideoViewHolder(view);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.component_session_row, parent, false);
            return new SessionViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final int pos = holder.getAdapterPosition();

        if (sessions.get(position) instanceof Session) {
            Session session = (Session) sessions.get(position);

            if (holder instanceof SessionViewHolder) {
                SessionViewHolder sessionViewHolder = (SessionViewHolder) holder;
                Date sessionDate = TimeFormatter.stringToDate(session.getShow_date(), TimeFormatter.DATE_FORMAT_VIDEO);
                if (sessionDate != null) {
                    sessionViewHolder.txtDate.setText(TimeFormatter.getRelativeDay(activity, sessionDate.getTime()));
                    String weekDay = TimeFormatter.millisecondsToString(sessionDate.getTime(), TimeFormatter.TIME_FORMAT_WEEK_SHORT);
                    sessionViewHolder.txtTime.setText(String.format("%s %s", weekDay, session.getTime()));
                }
                if (session.getStatus().equals(AVAILABLE)) {
                    sessionViewHolder.txtNumber.setTextSize(24);
                    sessionViewHolder.txtNumber.setText(String.format("#%s", String.valueOf(session.getNext_appointment_no())));
                } else {
                    sessionViewHolder.txtNumber.setTextSize(16);
                    sessionViewHolder.txtNumber.setText(session.getStatus());
                }
            }
        } else if (sessions.get(position) instanceof VideoSession) {
            VideoSession videoSession = (VideoSession) sessions.get(position);

            SessionViewHolder sessionViewHolder = (SessionViewHolder) holder;
            sessionViewHolder.txtDate.setText(TimeFormatter.millisecondsToString(videoSession.getStart().getTime(), TimeFormatter.TIME_FORMAT_APPOINTMENT_DATE));

            String startTime = TimeFormatter.millisecondsToString(videoSession.getStart().getTime(), TimeFormatter.TIME_FORMAT);
            String endTime = TimeFormatter.millisecondsToString(videoSession.getEnd().getTime(), TimeFormatter.TIME_FORMAT);

            sessionViewHolder.txtTime.setText(String.format(Locale.getDefault(), "%s - %s", startTime, endTime));
            sessionViewHolder.txtNumber.setText("");
        } else if (sessions.get(position) instanceof Hospital) {
            Hospital hospital = (Hospital) sessions.get(position);
            HospitalViewHolder hospitalViewHolder = (HospitalViewHolder) holder;

            hospitalViewHolder.txtName.setText(hospital.getHospital_name());
            Glide.with(activity).load(hospital.getHospital_image()).into(hospitalViewHolder.imgHospital);
        } else if (sessions.get(position) instanceof Session.Info) {
            Session.Info source = (Session.Info) sessions.get(position);
            SourceViewHolder sourceViewHolder = (SourceViewHolder) holder;

            sourceViewHolder.txtName.setText(String.format(Locale.getDefault(), AppConfig.AMOUNT_VIEW, source.getAmount_local()));

            switch (source.getFrom()) {
                case "doc990":
                    sourceViewHolder.imgSource.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.doc990));
                    break;
                case "echannelling":
                    sourceViewHolder.imgSource.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.echannelling));
                    break;
                default:
                    Appointment appointment = ((SourceActivity) activity).appointment;
                    Glide.with(activity).load(appointment.getSessionParent().getHospital_image()).into(sourceViewHolder.imgSource);
                    break;
            }
        } else if (sessions.get(position) instanceof Expert.Location) {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            Expert.Location location = (Expert.Location) sessions.get(position);
            Date sessionDate = TimeFormatter.stringToDate(location.getNext_available(), "yyyy-MM-dd hh:mm:ss");
            if (sessionDate != null) {
                videoViewHolder.txtDate.setText(TimeFormatter.millisecondsToString(sessionDate.getTime(), "EEEE, MMM dd, yyyy"));
                videoViewHolder.txtTime.setText(TimeFormatter.millisecondsToString(sessionDate.getTime(), "h:mm aa"));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    if (sessions.get(pos) instanceof Session) {
                        if (((Session) sessions.get(pos)).getStatus().equals(AVAILABLE))
                            listener.onChannelClicked(sessions.get(pos));
                    } else
                        listener.onChannelClicked(sessions.get(pos));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                if (listener != null)
//                    if (sessions.get(pos) instanceof Session)
//                        listener.onGetMoreSessionsClicked(((Session) sessions.get(pos)).getHospital());
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
        TextView txtName, txtPrice, txtDate, txtTime, txtBookAction, txtMoreAction;

        ChannelViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_title_schedule_row);
            txtDate = itemView.findViewById(R.id.txt_date_schedule_row);
            txtPrice = itemView.findViewById(R.id.txt_price_schedule_row);
            txtTime = itemView.findViewById(R.id.txt_time_schedule_row);
            txtBookAction = itemView.findViewById(R.id.txt_action_book_schedule_row);
            txtMoreAction = itemView.findViewById(R.id.txt_action_more_schedule_row);
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

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime;

        VideoViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_video_session_date);
            txtTime = itemView.findViewById(R.id.txt_video_session_time);
        }
    }

    class HospitalViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgHospital;

        HospitalViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name_hospital_channel_row);
            imgHospital = itemView.findViewById(R.id.img_hospital_channel_row);
        }
    }

    class SourceViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgSource;

        SourceViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_price_source_row);
            imgSource = itemView.findViewById(R.id.img_source_row);
        }
    }

    public interface OnChannelClickListener {
        void onChannelClicked(Object channelDoctor);

//        void onGetMoreSessionsClicked(String location);
    }
}
