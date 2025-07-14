package com.marco.turistafelicetest.planning.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.utils.Constants;

public class DialogSetCity extends AppCompatDialogFragment {
    private EditText editTextCity;
    private DialogSetCityListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_set_city, null);

        builder.setView(view)
                .setTitle(getContext().getString(R.string.set_city))
                .setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String city = editTextCity.getText().toString();
                        listener.applyTextCity(city);
                        SharedPreferences sharedPref = getContext().getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_PLANNING, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(Constants.CITY_TRIP, city);
                        editor.apply();
                    }
                });

        editTextCity = view.findViewById(R.id.edit_city);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogSetCityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement DialogSetCityListener");
        }
    }

    public interface DialogSetCityListener{
        void applyTextCity(String city);
    }
}
