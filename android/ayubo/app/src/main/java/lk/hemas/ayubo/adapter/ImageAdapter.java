package lk.hemas.ayubo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import lk.hemas.ayubo.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    //instances
    private Context context;
    private List<Uri> urls;

    public ImageAdapter(Context context, List<Uri> urls) {
        this.context = context;
        this.urls = urls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_upload_image_row, parent, false);
        return new ImageAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context).load(urls.get(position)).into(holder.image);
        holder.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urls.remove(urls.size() - 1);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(urls != null)
            return urls.size();
        else
            return 0;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView imgClose;

        ImageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_upload_image);
            imgClose = itemView.findViewById(R.id.img_upload_close);
        }
    }
}
