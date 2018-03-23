package lk.hemas.ayubo.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.hemas.ayubo.R;
import lk.hemas.ayubo.model.Doctor;

/**
 * Created by Sabri on 3/12/2018. Doctors image adapter
 */

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    private Activity activity;
    private List<Doctor> doctors;
    private OnDoctorClickListener listener;

    public DoctorsAdapter(Activity activity, List<Doctor> doctors) {
        this.activity = activity;
        this.doctors = doctors;
    }

    public void setOnDoctorClickListener(OnDoctorClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.component_doctor_image_row, parent, false);
        return new DoctorsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(activity).load(doctors.get(position).getImgUrl()).into(holder.imgDoctor);
        holder.txtDoctor.setText(doctors.get(position).getName());

        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onDoctorClick(doctors.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (doctors == null)
            return 0;
        else
            return doctors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgDoctor;
        TextView txtDoctor;

        ViewHolder(View itemView) {
            super(itemView);
            imgDoctor = itemView.findViewById(R.id.img_profile_doctor_row);
            txtDoctor = itemView.findViewById(R.id.txt_name_doctor_row);
        }
    }

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }
}
