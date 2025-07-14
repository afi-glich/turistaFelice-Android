package com.marco.turistafelicetest.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.marco.turistafelicetest.R;

//** DA CANCELLARE COMMENTI SE NON SI RISOLVE **//
public class DialogEditUsername extends AppCompatDialogFragment {
    private EditText editTextUsername;
    // public OnInputSelected mOnInputSelected;
    private static final String TAG = "DialogEditUsername";
    private TextView textViewUsername;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog, null);
        editTextUsername = view.findViewById(R.id.editUsername);
        textViewUsername = getActivity().findViewById(R.id.textViewUsername);

        builder.setView(view)
                .setTitle(getContext().getString(R.string.set_your_name_planning))
                .setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = editTextUsername.getText().toString();
                        Log.d(TAG, "onClick: " + username);
                        SharedPreferences sharedPref = getContext().getSharedPreferences("PointOfInterest_shared_preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("USERNAME", username);
                        editor.apply();
                        //ProfileFragment fragment = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.Profile);
                        if(textViewUsername != null) {
                            textViewUsername.setText(username);
                        }else{
                            Log.e(TAG, "textViewUsername is NULL");
                        }
                        //mOnInputSelected.SendInput(username);
                    }
                });

        return builder.create();
    }
/*
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }
        catch (ClassCastException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    public interface OnInputSelected
    {
        void SendInput(String input);
    }*/
}
