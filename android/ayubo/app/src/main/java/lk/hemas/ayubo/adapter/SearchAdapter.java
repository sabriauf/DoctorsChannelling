package lk.hemas.ayubo.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lk.hemas.ayubo.R;
import lk.hemas.ayubo.activity.DetailActivity;
import lk.hemas.ayubo.activity.SearchActivity;

/**
 * Created by Sabri on 3/16/2018. Adapter for Search
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int TITLE_TYPE = 1;
    private static final int DETAIL_TYPE = 2;
    private static final int SUMMARY_TYPE = 3;

    //instances
    private Activity activity;
    private List<Object> values;
    private OnItemClickListener listener;
    private SearchActivity.SearchActions searchActions;

    public SearchAdapter(Activity activity, List<Object> values, SearchActivity.SearchActions searchActions) {
        this.activity = activity;
        this.values = values;
        this.searchActions = searchActions;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == DETAIL_TYPE) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_search_row, parent, false);
            return new DetailViewHolder(view);
        } else if (viewType == SUMMARY_TYPE) {
            view = LayoutInflater.from(activity).inflate(R.layout.component_summary_row, parent, false);
            return new DetailViewHolder(view);
        } else {
            view = LayoutInflater.from(activity).inflate(R.layout.component_search_title_row, parent, false);
            return new TitleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        String name = "";
        String value = "";

        if (searchActions != null) {
            name = searchActions.getName(values.get(position));
            value = searchActions.getValue(values.get(position));
        }

        if (holder instanceof DetailViewHolder) {
            DetailViewHolder detailViewHolder = (DetailViewHolder) holder;
            detailViewHolder.txtTitle.setText(name);
            detailViewHolder.txtSubTitle.setText(value);
        } else {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.txtTitle.setText(name);
        }

        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && holder instanceof DetailViewHolder)
                    listener.onItemClick(values.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (values == null)
            return 0;
        else
            return values.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (values.get(position) instanceof String)
            return TITLE_TYPE;
        else if (values.get(position) instanceof DetailActivity.DetailRow)
            return SUMMARY_TYPE;
        else
            return DETAIL_TYPE;
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtSubTitle;

        DetailViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_name_search_row);
            txtSubTitle = itemView.findViewById(R.id.txt_sub_name_search_row);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title_search_row);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Object object);
    }
}
