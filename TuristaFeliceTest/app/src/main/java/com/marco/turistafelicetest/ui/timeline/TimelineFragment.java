package com.marco.turistafelicetest.ui.timeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marco.turistafelicetest.planning.PlanningActivity;
import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.models.Trip;
import com.marco.turistafelicetest.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TimelineFragment extends Fragment {

    private static final String TAG = "timelineFragment";

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private Context context;
    private List<Trip> trip;

    private List<Trip> trips;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_timeline, container, false);
        context = getContext();

        recyclerView = root.findViewById(R.id.timeline_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PlanningActivity.class);
                startActivity(i);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(getView());
    }

    private void updateUI(View view) {

        trips = getTrips();

        if (trips == null || trips.size() == 0) {
            view.findViewById(R.id.timeline_recyclerView).setVisibility(View.GONE);
            view.findViewById(R.id.placeholder).setVisibility(View.VISIBLE);
        } else {

            view.findViewById(R.id.placeholder).setVisibility(View.GONE);
            view.findViewById(R.id.timeline_recyclerView).setVisibility(View.VISIBLE);

            //scroll to next trip
            int position = 0;
            try {
                position = getNextTrip();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TimelineTripAdapter tripAdapter = new TimelineTripAdapter(context, trips, position, new TimelineTripAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(Trip trip) {
                    //open details
                    TimelineFragmentDirections.ShowTripDetailsAction action = TimelineFragmentDirections.showTripDetailsAction(trip);
                    Navigation.findNavController(view).navigate(action);
                }
            });
            recyclerView.setAdapter(tripAdapter);
            recyclerView.scrollToPosition(position);
        }

    }

    private int getNextTrip() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        Date tripDate;

        int position = 0;
        boolean found = false;

        if(!trips.isEmpty()) {
            for (int i = 0; !found && i < trips.size(); i++) {
                tripDate = sdf.parse(trips.get(i).getDeparture());

                if (sdf.format(tripDate).equals(sdf.format(currentDate)) || tripDate.compareTo(currentDate) > 0) {
                    position = i;
                    found = true;
                }
            }
        }

        return position;
    }


    public List<Trip> getTrips() {

        List<Trip> trips = new ArrayList<>();

        File file = new File(getContext().getFilesDir() + "/" + Constants.FILE_NAME);

        if(!file.exists()) {
            Log.d(TAG, "File non found.");
            return null;
        }

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            Log.d(TAG, "Viaggi che sto caricando: ");

            while(!line.isEmpty()) {
                Log.d(TAG, "| "+line);
                Trip trip = new Trip(line);
                trips.add(trip);
                line = bufferedReader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(trips);

        return trips;
    }

}
