package com.marco.turistafelicetest.planning.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.planning.PlanningActivity;
import com.marco.turistafelicetest.ui.around.AroundFragment;
import com.marco.turistafelicetest.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;
    Context cx;


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;
        ImageView imgDel, imgNav, imgDrag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mTextView = itemView.findViewById(R.id.TextViewPOI);
            this.imgDel = itemView.findViewById(R.id.imageDelete);
            this.imgNav = itemView.findViewById(R.id.image_navigate);
            this.imgDrag = itemView.findViewById(R.id.imageDragAndDrop);
        }

    }


    public MyAdapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        cx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.poi_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String text = data.get(position);
        holder.mTextView.setText(text);
        SharedPreferences sharedPref = cx.getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_PLANNING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> POI;


        POI = sharedPref.getStringSet(Constants.POI_NAME,null);
        Set<String> finalPOI = POI;
        Object[] temp = POI.toArray();

        holder.imgNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<temp.length; i++) {
                    if (temp[i].toString().equals(text)) {
                        //invio LocAdd per navigazione
                        String str;
                        str = temp[i].toString().replaceAll(" ", "+").toLowerCase();
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + str);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        cx.startActivity(mapIntent);

                       Log.d("PlanningActivity", str);
                    }
                }
            }
        });

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

                      if (finalPOI.remove(text)) {
                          removeAt(position);
                          editor.remove(Constants.POI_NAME);
                          editor.apply();
                          editor.putStringSet(Constants.POI_NAME, finalPOI);
                          editor.apply();
                      }
              }
      });

    }

    public void newAddPlace(String place){
        data.add(place);
        notifyDataSetChanged();
    }

    private void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
