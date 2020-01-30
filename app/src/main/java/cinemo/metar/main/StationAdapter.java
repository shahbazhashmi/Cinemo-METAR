package cinemo.metar.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cinemo.metar.R;
import cinemo.metar.databinding.RowStationBinding;
import cinemo.metar.interfaces.StationClickListener;
import cinemo.metar.room.Station;

/**
 * Created by Shahbaz Hashmi on 2020-01-30.
 */
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> implements StationClickListener {

    private List<Station> dataModelList = new ArrayList<>();

    public void setStations(List<Station> dataModelList) {
        this.dataModelList = dataModelList;
        notifyDataSetChanged();
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
        Station dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.setItemClickListener(this);
    }


    @Override
    public int getItemCount() {
        return dataModelList.size();
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

    }
}