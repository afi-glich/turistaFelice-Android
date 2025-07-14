package com.marco.turistafelicetest.ui.timeline;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.models.Trip;
import com.marco.turistafelicetest.planning.PlanningActivity;
import com.marco.turistafelicetest.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import static com.marco.turistafelicetest.R.color.divider;
import static com.marco.turistafelicetest.R.color.secondary_light;
import static com.marco.turistafelicetest.R.layout.timeline_item;

public class TimelineTripAdapter extends RecyclerView.Adapter<TimelineTripAdapter.TripViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }

    private static final String TAG = "timelineAdapter";
    public static final String TRIP = "trip";

    private List<Trip> trips;
    private LayoutInflater layoutInflater;
    private static Context context;
    private OnItemClickListener onItemClickListener;
    private int nextTripPosition;

    public static class TripViewHolder extends RecyclerView.ViewHolder {

        private TextView tripTitle;
        private TextView tripCity;
        private TextView tripDeparture;
        private TextView tripReturn;
        private ImageView deleteButton;
        private ImageView modifyButton;
        private ImageView cityImage;
        private CardView item_card;

        public TripViewHolder(View v) {
            super(v);
            tripTitle = v.findViewById(R.id.timeline_item_title);
            tripCity = v.findViewById(R.id.timeline_item_city);
            tripDeparture = v.findViewById(R.id.timeline_item_departure);
            tripReturn = v.findViewById(R.id.timeline_item_return);
            deleteButton = v.findViewById(R.id.timeline_item_button_delete);
            modifyButton = v.findViewById(R.id.timeline_item_modify_button);
            cityImage = v.findViewById(R.id.timeline_item_image);
            item_card = v.findViewById(R.id.timeline_item_cardview);
        }

        public void bind(final Trip trip, OnItemClickListener onItemClickListener) {
            tripTitle.setText(trip.getTitle());
            tripCity.setText(trip.getCity());
            tripDeparture.setText(trip.getDeparture());
            tripReturn.setText(trip.getReturnDate());

            Picasso.get().load(trip.getUrlToCityImage()).into(cityImage);
            tripTitle.setText(trip.getTitle());
            tripCity.setText(trip.getCity());
            tripDeparture.setText(trip.getDeparture());
            tripReturn.setText(trip.getReturnDate());

            item_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(trip);
                }
            });

        }
    }

    public TimelineTripAdapter(Context context, List<Trip> trips, int nextTripPosition, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.trips = trips;
        this.onItemClickListener = onItemClickListener;
        this.nextTripPosition = nextTripPosition;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(timeline_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        holder.bind(trips.get(position), this.onItemClickListener);

        if (position < nextTripPosition) {
            holder.item_card.setCardBackgroundColor(ContextCompat.getColor(context, divider));

            holder.modifyButton.setVisibility(View.GONE);

        } else if (position == nextTripPosition) {
            holder.item_card.setCardBackgroundColor(ContextCompat.getColor(context, secondary_light));
        }

        holder.modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlanningActivity.class);
                intent.putExtra(TRIP, trips.get(position));
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trips.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();

                boolean isRemoved = removeTripFromFile();

                if (isRemoved) {
                    Snackbar.make(holder.itemView, R.string.timeline_delete_success, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(holder.itemView, R.string.timeline_delete_fail, Snackbar.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    private boolean removeTripFromFile() {

        File file = new File(context.getFilesDir() + "/" + Constants.FILE_NAME);

        if (!file.exists()) {
            Log.e(TAG, "File not found.");
            return false;
        }

        if (file.delete()) {
            Log.d(TAG, "File Eliminato.");
        } else {
            return false;
        }

        File newFile = new File(context.getFilesDir() + "/" + Constants.FILE_NAME);

        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(newFile));
            BufferedWriter buffer = new BufferedWriter(writer);

            for (Trip t : trips) {
                buffer.write(t.toString());
            }

            buffer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }

}
