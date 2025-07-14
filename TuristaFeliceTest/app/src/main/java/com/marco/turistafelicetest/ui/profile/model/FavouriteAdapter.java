package com.marco.turistafelicetest.ui.profile.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.models.Trip;
import com.marco.turistafelicetest.ui.around.AroundFragment;
import com.marco.turistafelicetest.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> data;
    Context cx;

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;
        ImageView imgDel, imgAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mTextView = itemView.findViewById(R.id.TextViewFavourite);
            this.imgDel = itemView.findViewById(R.id.favouriteDelete);
            this.imgAdd = itemView.findViewById(R.id.favouriteAdd);
        }
    }

    public FavouriteAdapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        cx = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.favourite_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = data.get(position);
        holder.mTextView.setText(text);

        SharedPreferences sharedPref = cx.getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_POI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> favourite;

        favourite = sharedPref.getStringSet(Constants.FAVOURITES,null);

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (favourite.remove(text)) {
                    removeAt(position);
                    editor.remove(Constants.FAVOURITES);
                    editor.apply();
                    editor.putStringSet(Constants.FAVOURITES, favourite);
                    editor.apply();
                }
            }
        });

        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Trip> trips = new ArrayList<>();
                String fileName = "Planning.txt";
                AroundFragment aroundFragment = null;

                File file = new File(cx.getFilesDir() + "/" + fileName);
                if(!file.exists()) {
                    Log.d("FavouriteFragment", "File non found.");
                    Toast.makeText(cx, cx.getString(R.string.around_no_trips), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line = bufferedReader.readLine();

                    while(line != null && !line.isEmpty()) {
                        Trip trip = new Trip(line);
                        trips.add(trip);
                        line = bufferedReader.readLine();
                    }

                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Dialog per la scelta del viaggio a cui aggiungere il POI
                AlertDialog.Builder builder = new AlertDialog.Builder(cx);
                builder.setTitle(cx.getString(R.string.around_choose_trip));
                String[] tripsA = new String[trips.size()];
                for(int i=0;i<trips.size();i++){
                    tripsA[i] = trips.get(i).getTitle();
                }
                builder.setItems(tripsA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("FavouriteFragment", "Scelto viaggio: "+tripsA[which]);
                        AddPOI(trips.get(which).getId(), text);
                    }
                });
                AlertDialog dialogT = builder.create();
                dialogT.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    private void AddPOI(int id, String POI){
        Log.d("ADDPOI", "id: "+id+" poi: "+POI);
        Trip temp_trip;

        try {
            BufferedReader file = new BufferedReader(new FileReader(cx.getFilesDir() + "/" + Constants.FILE_NAME));
            StringBuilder inputBuffer = new StringBuilder();
            String line;

            while ((line = file.readLine()) != null) {
                temp_trip = new Trip(line);
                Log.d("ADDPOI", "temp id: "+temp_trip.getId()+", my id"+id);
                if(temp_trip.getId() == id) {
                    Log.d("FavouriteFragment",temp_trip.toString() + "," + POI);
                    inputBuffer.append(temp_trip.toString() + "," + POI);
                    inputBuffer.append('\n');
                }
                else {
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }
            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(cx.getFilesDir() + "/" + Constants.FILE_NAME);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
