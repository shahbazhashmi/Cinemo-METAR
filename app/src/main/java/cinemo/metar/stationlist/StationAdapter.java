package cinemo.metar.stationlist;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import cinemo.metar.R;
import cinemo.metar.databinding.RowStationBinding;
import cinemo.metar.interfaces.StationClickListener;
import cinemo.metar.room.Station;

/**
 * Created by Shahbaz Hashmi on 2020-01-30.
 */
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> implements StationClickListener, Filterable {

    private List<Station> mDataModelList = new ArrayList<>();
    private List<Station> mFilteredDataModelList = new ArrayList<>();
    private ItemFilter mFilter = new ItemFilter();
    private Callable<Void> mEmptySearchCallable;
    private StationClickListener mOnStationClickListener;

    public StationAdapter(Callable<Void> emptySearchCallable) {
        mEmptySearchCallable = emptySearchCallable;
    }

    public void setStations(List<Station> dataModelList) {
        mDataModelList = dataModelList;
        mFilteredDataModelList = dataModelList;
        notifyDataSetChanged();
    }

    public void setOnStationClickListener(StationClickListener onStationClickListener) {
        mOnStationClickListener = onStationClickListener;
    }

    @Override
    public StationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        RowStationBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_station, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Station dataModel = mFilteredDataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setItemClickListener(this);
    }


    @Override
    public int getItemCount() {
        return mFilteredDataModelList.size();
    }

    public Object getItem(int position) {
        return mFilteredDataModelList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RowStationBinding itemRowBinding;

        public ViewHolder(RowStationBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }


    @Override
    public void onStationClick(Station station) {
        if(mOnStationClickListener != null) {
            mOnStationClickListener.onStationClick(station);
        }
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Station> list = mDataModelList;

            int count = list.size();
            final ArrayList<Station> nlist = new ArrayList<Station>(count);

            Station filterableStation;

            for (int i = 0; i < count; i++) {
                filterableStation = list.get(i);
                if (filterableStation.getFileName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableStation);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredDataModelList = (ArrayList<Station>) results.values;
            try {
                if (mFilteredDataModelList.size() == 0) {
                    mEmptySearchCallable.call();
                }
            } catch (Exception e) {e.printStackTrace();}
            notifyDataSetChanged();
        }
    }

}