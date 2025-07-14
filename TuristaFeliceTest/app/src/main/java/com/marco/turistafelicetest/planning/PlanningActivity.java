package com.marco.turistafelicetest.planning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.models.Trip;
import com.marco.turistafelicetest.databinding.ActivityPlanningBinding;
import com.marco.turistafelicetest.planning.dialog.DialogSetCity;
import com.marco.turistafelicetest.planning.dialog.DialogSetTitle;
import com.marco.turistafelicetest.planning.model.MyAdapter;
import com.marco.turistafelicetest.utils.Constants;
import com.marco.turistafelicetest.utils.TripsImageInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlanningActivity extends AppCompatActivity implements DialogSetTitle.DialogSetTitleListener, DialogSetCity.DialogSetCityListener {


    private ActivityPlanningBinding binding;
    String Departure = null;
    String Return = null;
    ArrayList<String> items;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int dDay = 0;
    int dMonth = 0;
    int dYear = 0;
    final String TAG = "PlanningActivity";
    MyAdapter mAdapter;
    Set<String> POI = new HashSet<>();
    private  Trip modifyTrip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlanningBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences sharedPref = getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_PLANNING, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        Intent intent = getIntent();
        modifyTrip = (Trip) intent.getParcelableExtra("trip");

        items = new ArrayList<>();

        if(modifyTrip != null)
        {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfD = new SimpleDateFormat("dd");
            SimpleDateFormat sdfM = new SimpleDateFormat("MM");
            SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
            String d;
            String m;
            String y;


            Date tripDate = null;

            try {
                tripDate = sdf.parse(modifyTrip.getDeparture());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            binding.textViewDepartureDate.setText(modifyTrip.getDeparture());
            editor.putString(Constants.DEPARTURE, modifyTrip.getDeparture());
            d = sdfD.format(tripDate);
            m = sdfM.format(tripDate);
            y = sdfY.format(tripDate);
            dDay = Integer.parseInt(d);
            dMonth = Integer.parseInt(m);
            dYear = Integer.parseInt(y);

            binding.textViewReturnDate.setText(modifyTrip.getReturnDate());
            editor.putString(Constants.RETURN, modifyTrip.getReturnDate());

            binding.textViewCity.setText(modifyTrip.getCity());
            editor.putString(Constants.CITY_TRIP, modifyTrip.getCity());

            binding.textViewTitleTrip.setText(modifyTrip.getTitle());
            editor.putString(Constants.TITLE_TRIP, modifyTrip.getTitle());



            String[] temp = modifyTrip.getPlaceName().split(",");

            //verificare se funziona correttammente

            POI.addAll(Arrays.asList(temp));
            items.addAll(Arrays.asList(temp));
            editor.putStringSet(Constants.POI_NAME, POI);
            editor.apply();
        }


        // inizializzazione Recycle view
        final LinearLayoutManager layoutManager = new LinearLayoutManager(PlanningActivity.this);
        binding.recyclerViewPOI.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(PlanningActivity.this,items);
        binding.recyclerViewPOI.setAdapter(mAdapter);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), Constants.PLACES_API_KEY_PLANNING);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // salvataggio Place nello shared e inserito a video nella recycle view

                POI.add(place.getName() /*+ ';' + place.getAddress()*/);
                editor.putStringSet(Constants.POI_NAME, POI);
                editor.apply();

                mAdapter.newAddPlace(place.getName());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        //Set departure date
        binding.textViewDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                datePickerDialog = new DatePickerDialog(PlanningActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        Departure = mDay + "/" + (mMonth+1) + "/" + mYear;
                        if(year > mYear ||  month > mMonth || month == mMonth && day > mDay)
                        {
                            Snackbar.make(v, R.string.planning_snackbar_travel_information, Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            dDay = mDay;
                            dMonth = mMonth;
                            dYear = mYear;
                            editor.putString(Constants.DEPARTURE, Departure);
                            editor.apply();
                            binding.textViewDepartureDate.setText(Departure);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //Set return date
        binding.textViewReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                
                datePickerDialog = new DatePickerDialog(PlanningActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        Return = mDay + "/" + (mMonth+1) + "/" + mYear;
                        if(dDay == 0 && dMonth == 0 && dYear == 0)
                        {
                            Snackbar.make(v, R.string.planning_snackbar_missing_information, Snackbar.LENGTH_SHORT).show();
                        }
                        else {
                            if(dYear >= mYear){
                                if(dMonth >= mMonth){
                                    if(dDay > mDay){
                                        Snackbar.make(v, R.string.planning_snackbar_travel_information, Snackbar.LENGTH_SHORT).show();
                                    }
                                    else {
                                        editor.putString(Constants.RETURN, Return);
                                        editor.apply();
                                        binding.textViewReturnDate.setText(Return);
                                    }
                                }
                            }
                            else {
                                editor.putString(Constants.RETURN, Return);
                                editor.apply();
                                binding.textViewReturnDate.setText(Return);
                            }
                        }
                    }
                }, dYear, dMonth, dDay);
                datePickerDialog.show();
            }
        });

        binding.textViewTitleTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        binding.textViewCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openDialogCity();
            }
        });

        binding.buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.textViewTitleTrip.getText().equals("Title") || binding.textViewCity.getText().equals("City") || binding.textViewDepartureDate.getText().equals("Departure") || binding.textViewReturnDate.getText().equals("Return") ||
                        binding.textViewTitleTrip.getText().equals("") || binding.textViewCity.getText().equals("") || binding.textViewDepartureDate.getText().equals("") || binding.textViewReturnDate.getText().equals("") || mAdapter.getItemCount() == 0) {
                    Snackbar.make(v, R.string.data_missing, Snackbar.LENGTH_SHORT).show();
                } else {
                        saveInformation();
                }
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewPOI);
    }

    //Drag and drop
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN |ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPos = viewHolder.getAdapterPosition();
            int toPos = target.getAdapterPosition();
            Collections.swap(items, fromPos, toPos);
            recyclerView.getAdapter().notifyItemMoved(fromPos, toPos);
            resetAdapter();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) { }
    };
    public void resetAdapter(){
        mAdapter = new MyAdapter(PlanningActivity.this,items);
        binding.recyclerViewPOI.setAdapter(mAdapter);
        mAdapter.notifyItemRangeChanged(0,items.size());
    }

    //Dialog for title trip
    public void openDialog() {
        DialogSetTitle dialogSetTitle = new DialogSetTitle();
        dialogSetTitle.show(getSupportFragmentManager(), "Set title");
    }

    //Dialog for city name
    public void openDialogCity() {
        DialogSetCity dialogSetCity = new DialogSetCity();
        dialogSetCity.show(getSupportFragmentManager(), "Set city");
    }

    @Override
    public void applyText(String titleTrip) {
        binding.textViewTitleTrip.setText(titleTrip);
    }

    @Override
    public void applyTextCity(String city) {
        binding.textViewCity.setText(city);
    }

    //Saves trip's information on a txt file
    private void saveInformation() {
        Trip mTrip = new Trip();
        final Trip[] temp_trip = {null};
        SharedPreferences sharedPref = getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_PLANNING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        /**caricamento variabili prese o da un trip in entrata o dall'inserimento nelle caselle di testo **/

        Set<String> poi = sharedPref.getStringSet(Constants.POI_NAME, null);
        String title = sharedPref.getString(Constants.TITLE_TRIP,null);
        String city = sharedPref.getString(Constants.CITY_TRIP,null);
        String departure = sharedPref.getString(Constants.DEPARTURE, null);
        String ret = sharedPref.getString(Constants.RETURN, null);
        final int[] id = {sharedPref.getInt(Constants.ID, 0)};
        //creo stringa con tutti i POI separati dalla virgola.

        String placesName = "";

        if (poi != null) {
            Iterator<String> iterator = poi.iterator();
            String next = iterator.next();
            placesName = next;

            while (iterator.hasNext()) {
                next = iterator.next();
                placesName = placesName + "," + next;
            }

        }

        mTrip.setPlaceName(placesName);
        mTrip.setTitle(title);
        mTrip.setCity(city);
        mTrip.setDeparture(departure);
        mTrip.setReturnDate(ret);

            /**getting image from qwent image seach and saving the url in cityTrip**/
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.QWANT_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TripsImageInterface tripsImageInterface = retrofit.create(TripsImageInterface.class);
            Call<Object> call = tripsImageInterface.getCityImage(city, 1, "images", 1, 4);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Object d = response.body();
                    String url = d.toString();
                    int linkStart = url.indexOf("media") + 6;
                    int linkEnd = url.indexOf(',', linkStart);
                    url = url.substring(linkStart, linkEnd);

                    mTrip.setUrlToCityImage(url);

                    /**se non abbiamo avuto nessun trip in ingresso salviamo i dati in una nuova riga nel file **/
                    if (modifyTrip == null) {

                        File file;

                        file = new File(getFilesDir() + "/" + Constants.FILE_NAME);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                                id[0] = 0;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        id[0] = id[0] + 1;

                        mTrip.setId(id[0]);
                        editor.putInt(Constants.ID, id[0]);
                        editor.apply();

                        try {

                            try {
                                OutputStreamWriter file_writer = new OutputStreamWriter(new FileOutputStream(file, true));
                                BufferedWriter buffered_writer = new BufferedWriter(file_writer);
                                buffered_writer.write(mTrip.toString().replaceAll("\n", "") + "\n");

                                Log.d(TAG, "saveInformation: " + mTrip.toString());
                                buffered_writer.close();
                                finish();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    /**Modifica il file con la riga contente il trip in ingresso **/
                    else{
                        mTrip.setId(id[0]);
                        try {
                            BufferedReader file = new BufferedReader(new FileReader(getFilesDir() + "/" + Constants.FILE_NAME));
                            StringBuilder inputBuffer = new StringBuilder();
                            String line;

                            while ((line = file.readLine()) != null) {
                                temp_trip[0] = new Trip(line);
                                if(temp_trip[0].getId() == mTrip.getId()) {
                                    Log.d(TAG,mTrip.toString());
                                    inputBuffer.append(mTrip.toString());
                                    inputBuffer.append('\n');
                                }
                                else {
                                    inputBuffer.append(line);
                                    inputBuffer.append('\n');
                                }
                            }
                            file.close();

                            // write the new string with the replaced line OVER the same file
                            FileOutputStream fileOut = new FileOutputStream(getFilesDir() + "/" + Constants.FILE_NAME);
                            fileOut.write(inputBuffer.toString().getBytes());
                            fileOut.close();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });

            //Toast.makeText(this, "Saved to " + getFilesDir() + "/" + Constants.FILE_NAME, Toast.LENGTH_LONG).show();
        }

    }



