package lk.hemas.ayubo.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.config.AppConfig;
import lk.hemas.ayubo.model.Doctor;
import lk.hemas.ayubo.model.Expert;

/**
 * Created by Sabri on 3/12/2018. Doctors image adapter
 */

public class DoctorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_EXPERT = 1;
    private static final int VIEW_TYPE_DOCTOR = 2;

    //instances
    private Activity activity;
    private List<Expert> expertList;
    private List<Object> objectList;
    private OnDoctorClickListener listener;


    public DoctorsAdapter(Activity activity, List<Expert> experts, List<Object> objects) {
        this.activity = activity;
        this.expertList = experts;
        this.objectList = objects;
    }

    public void setOnDoctorClickListener(OnDoctorClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_EXPERT) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_expert_image_row, parent, false);
            return new ImageViewHolder(view);
//        } else if (viewType == VIEW_TYPE_DOCTOR) {
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.component_doctor_image_row, parent, false);
            return new DetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder) {
            ImageViewHolder imgViewHolder = (ImageViewHolder) holder;
            Expert expert = expertList.get(position);
            Glide.with(activity).load(getImageUrl(expert.getPicture())).into(imgViewHolder.imgDoctor);

        } else if (holder instanceof DetailViewHolder) {
            DetailViewHolder detailViewHolder = (DetailViewHolder) holder;
            Doctor doctor = (Doctor) objectList.get(position);
            if (doctor.getName() == null) {
                detailViewHolder.txtDoctor.setText(R.string.add_new);
                detailViewHolder.txtDoctor.setTextColor(ContextCompat.getColor(activity, R.color.text_color_secondary));
                detailViewHolder.imgDoctor.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.add_btn));
                detailViewHolder.imgDoctor.setPadding(50, 50, 50, 50);
            } else {
                detailViewHolder.txtDoctor.setTextColor(ContextCompat.getColor(activity, R.color.text_color_primary));
                detailViewHolder.imgDoctor.setPadding(0, 0, 0, 0);
                Glide.with(activity).load(doctor.getDoc_image()).into(detailViewHolder.imgDoctor);
                detailViewHolder.txtDoctor.setText(doctor.getFull_name());
            }
        }

        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (objectList != null)
                        listener.onDoctorClick(objectList.get(pos));
                    else
                        listener.onDoctorClick(expertList.get(pos));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (expertList == null && objectList == null)
            return 0;
        else if (expertList != null)
            return expertList.size();
        else
            return objectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (expertList == null)
            return VIEW_TYPE_DOCTOR;
        else
            return VIEW_TYPE_EXPERT;
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgDoctor;
        TextView txtDoctor;

        DetailViewHolder(View itemView) {
            super(itemView);
            imgDoctor = itemView.findViewById(R.id.img_profile_doctor_row);
            txtDoctor = itemView.findViewById(R.id.txt_name_doctor_row);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgDoctor;

        ImageViewHolder(View itemView) {
            super(itemView);
            imgDoctor = itemView.findViewById(R.id.img_profile_expert_row);
        }
    }

    public interface OnDoctorClickListener {
        void onDoctorClick(Object object);

//        void onNewExpertFocused(Expert expert);
    }

    private String getImageUrl(String content) {
        if (content.contains(AppConfig.DOCTOR_IMAGE_DEAFULT))
            return AppConfig.AYUBO_BASIC_URL + content;
        else if (!content.equals(""))
            return AppConfig.AYUBO_BASIC_URL + AppConfig.DOCTOR_IMAGE_BASIC + content.substring(content.lastIndexOf("/"));
        else
            return "";
    }
}
