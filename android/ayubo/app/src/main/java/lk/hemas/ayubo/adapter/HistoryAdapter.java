package lk.hemas.ayubo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.model.History;
import lk.hemas.ayubo.util.TimeFormatter;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_TITLE = R.layout.component_history_title_row;
    private static final int VIEW_TYPE_ITEM = R.layout.component_history_item_row;

    //instances
    private List<Object> items;
    private Context context;
    private OnCancelAppointmentListener listener;

    public HistoryAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnAppointmentCancelListener(OnCancelAppointmentListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        if (viewType == VIEW_TYPE_ITEM)
            return new ItemViewHolder(view);
        else
            return new TitleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            History history = (History) items.get(position);

            itemViewHolder.txtExpert.setText(history.getDocname());

            Date startDate = TimeFormatter.stringToDate(history.getStarts(), "yyyy-MM-dd hh:mm:ss");
            if (!startDate.after(new Date())) {
                itemViewHolder.txtCancel.setVisibility(View.GONE);
            }

            itemViewHolder.txtDate.setText(TimeFormatter.millisecondsToString(startDate.getTime(), "EEEE, MMM dd, yyyy  h:mm aa"));

            if (history.getLocation().equals("Video Chat"))
                itemViewHolder.imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.video_call_small_selected));
            else
                itemViewHolder.imgIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.channel_small_selected));

            final int pos = itemViewHolder.getAdapterPosition();
            itemViewHolder.txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                        listener.onCancel((History) items.get(pos));
                }
            });

        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

            titleViewHolder.txtTitle.setText((String) items.get(position));
        }

    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else
            return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String)
            return VIEW_TYPE_TITLE;
        else if (items.get(position) instanceof History)
            return VIEW_TYPE_ITEM;
        return super.getItemViewType(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView txtExpert, txtDate, txtCancel;

        ItemViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_history_item_row);
            txtExpert = itemView.findViewById(R.id.txt_history_item_row_expert);
            txtDate = itemView.findViewById(R.id.txt_history_item_row_date);
            txtCancel = itemView.findViewById(R.id.txt_history_item_row_cancel);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_history_title_row);
        }
    }

    public interface OnCancelAppointmentListener {
        void onCancel(History history);
    }
}
