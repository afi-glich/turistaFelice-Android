package com.marco.turistafelicetest.ui.timeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marco.turistafelicetest.R;

import java.util.List;

import static com.marco.turistafelicetest.R.layout.fragment_trip_details;

public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.PoiViewHolder>{

    private List<String> places;
    private LayoutInflater layoutInflater;

    public static class PoiViewHolder extends RecyclerView.ViewHolder {

        private TextView placeName;

        public PoiViewHolder(View v) {
            super(v);
            placeName = v.findViewById(R.id.detail_place_text);
        }

        public void bind(final String place) {
            placeName.setText(place);
        }

    }


    public PoiAdapter(Context context, List<String> places) {
        this.layoutInflater = LayoutInflater.from(context);
        this.places = places;
    }

    @NonNull
    @Override
    public PoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.trip_details_poi_item, parent, false);
        return new PoiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoiViewHolder holder, int position) {
        holder.bind(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

}
