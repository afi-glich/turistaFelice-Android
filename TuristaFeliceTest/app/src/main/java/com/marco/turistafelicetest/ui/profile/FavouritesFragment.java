package com.marco.turistafelicetest.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.ui.profile.model.FavouriteAdapter;
import com.marco.turistafelicetest.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class FavouritesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.fragment_favourites, null);

        TextView textView = view.findViewById(R.id.noFav);

        ArrayList<String> items = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.favourites_recyclerView);

        SharedPreferences sharedPref = getContext().getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_POI, Context.MODE_PRIVATE);
        Set<String> temp;
        temp = sharedPref.getStringSet(Constants.FAVOURITES,null);

        if(temp != null) {
            Object[] array = temp.toArray();
            //copia dei dati contenenti nel file all'interno di un ArrayList
            for (int i = 0; i < array.length; i++) {
                items.add(array[i].toString());
            }
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        FavouriteAdapter adapter = new FavouriteAdapter(this.getContext(), items);
        recyclerView.setAdapter(adapter);

        if (items.size() != 0){
            view.findViewById(R.id.noFav).setVisibility(View.GONE);
        }

        return view;


    }
}