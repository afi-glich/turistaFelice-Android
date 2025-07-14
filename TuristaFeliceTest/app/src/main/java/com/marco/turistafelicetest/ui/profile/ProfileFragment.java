package com.marco.turistafelicetest.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.databinding.FragmentProfileBinding;
import com.marco.turistafelicetest.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment  {
    private ProfileViewModel notificationsViewModel;
    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    public TextView textViewUsername;
    private Button buttonSetName;


    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Change profile picture
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pick an image"), 1);
            }
        });

        //Change profile name
        binding.textViewUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        //Change profile name
        binding.imageViewEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        //Go to favourites
        binding.favouritesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouritesFragment favouritesFragment = new FavouritesFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, favouritesFragment);
                fragmentTransaction.commit();
            }
        });

        setViewChecked();
        return root;
    }

    private void openDialog() {

        DialogEditUsername dialogFragment = new DialogEditUsername();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "Edit name");
    }


    public void applyText(String username) {
        binding.textViewUsername.setText(username);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {

            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.profileImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"PAUSA");
        saveInformation();
    }

    private static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    private static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /**
     * It saves point of interest using the SharedPreferences API.
     */
    private void saveInformation() {

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_POI, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();



        Set<String> POISet = new HashSet<>();


        if (binding.checkBoxTouristAttraction.isChecked()) {
            POISet.add(Constants.TOURIST_ATTRACTION);
        }

        if (binding.checkBoxBusStation.isChecked()) {
            POISet.add(Constants.BUS_STATION);
        }

        if (binding.checkBoxBar.isChecked()) {
            POISet.add(Constants.BAR);
        }

        if (binding.checkBoxBakery.isChecked()) {
            POISet.add(Constants.BAKERY);
        }

        if (binding.checkBoxChurch.isChecked()) {
            POISet.add(Constants.CHURCH);
        }

        if (binding.checkBoxAquarium.isChecked()) {
            POISet.add(Constants.AQUARIUM);
        }

        if (binding.checkBoxMuseum.isChecked()) {
            POISet.add(Constants.MUSEUM);
        }

        if (binding.checkBoxArtGallery.isChecked()) {
            POISet.add(Constants.ART_GALLERY);
        }

        if (binding.checkBoxATM.isChecked()) {
            POISet.add(Constants.ATM);
        }

        if (binding.checkBoxCarRental.isChecked()) {
            POISet.add(Constants.CAR_RENTAL);
        }

        if (binding.checkBoxGusStation.isChecked()) {
            POISet.add(Constants.GUS_STATION);
        }

        if (binding.checkBoxEmbassy.isChecked()) {
            POISet.add(Constants.EMBASSY);
        }

        if (binding.checkBoxHinduTemple.isChecked()) {
            POISet.add(Constants.HINDU_TEMPLE);
        }

        if (binding.checkBoxHospital.isChecked()) {
            POISet.add(Constants.HOSPITAL);
        }

        if (binding.checkBoxLightRailStation.isChecked()) {
            POISet.add(Constants.LIGHT_RAIL_STATION);
        }

        if (binding.checkBoxLocalGovernmentOffice.isChecked()) {
            POISet.add(Constants.LOCAL_GOVERNMENT_OFFICE);
        }

        if (binding.checkBoxMosque.isChecked()) {
            POISet.add(Constants.MOSQUE);
        }

        if (binding.checkBoxPark.isChecked()) {
            POISet.add(Constants.PARK);
        }

        if (binding.checkBoxPharmacy.isChecked()) {
            POISet.add(Constants.PHARMACY);
        }

        if (binding.checkBoxStadium.isChecked()) {
            POISet.add(Constants.STADIUM);
        }

        if (binding.checkBoxStore.isChecked()) {
            POISet.add(Constants.STORE);
        }

        if (binding.checkBoxSubwayStation.isChecked()) {
            POISet.add(Constants.SUBWAY_STATION);
        }

        if (binding.checkBoxSynagogue.isChecked()) {
            POISet.add(Constants.SYNAGOGUE);
        }

        if (binding.checkBoxTrainStation.isChecked()) {
            POISet.add(Constants.TRAIN_STATION);
        }

        if (binding.checkBoxZoo.isChecked()) {
            POISet.add(Constants.ZOO);
        }

        BitmapDrawable drawable = (BitmapDrawable) binding.profileImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        editor.putString(Constants.IMAGE, encodeTobase64(bitmap));
        editor.putStringSet(Constants.SHARED_PREFERENCES_SELECTED_POI, POISet);
        editor.apply();

    }

    /**
     * It marks checked the CheckBox based on what it has been saved in the SharedPreferences file.
     */
    private void setViewChecked() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_POI, Context.MODE_PRIVATE);


        Set<String> POISet = sharedPref.getStringSet(Constants.SHARED_PREFERENCES_SELECTED_POI, null);
        String username = sharedPref.getString("USERNAME", null);
        binding.textViewUsername.setText(username);
        String image = sharedPref.getString(Constants.IMAGE, null);
        if (image != null) {
            Bitmap Bimage = decodeBase64(image);
            binding.profileImage.setImageBitmap(Bimage);
        }

        if (POISet != null) {
            if (POISet.contains(Constants.TOURIST_ATTRACTION)) {
                binding.checkBoxTouristAttraction.setChecked(true);
            }

            if (POISet.contains(Constants.BUS_STATION)) {
                binding.checkBoxBusStation.setChecked(true);
            }

            if (POISet.contains(Constants.BAR)) {
                binding.checkBoxBar.setChecked(true);
            }

            if (POISet.contains(Constants.BAKERY)) {
                binding.checkBoxBakery.setChecked(true);
            }

            if (POISet.contains(Constants.CHURCH)) {
                binding.checkBoxChurch.setChecked(true);
            }

            if (POISet.contains(Constants.AQUARIUM)) {
                binding.checkBoxAquarium.setChecked(true);
            }

            if (POISet.contains(Constants.MUSEUM)) {
                binding.checkBoxMuseum.setChecked(true);
            }

            if (POISet.contains(Constants.ATM)) {
                binding.checkBoxATM.setChecked(true);
            }

            if (POISet.contains(Constants.CAR_RENTAL)) {
                binding.checkBoxCarRental.setChecked(true);
            }

            if (POISet.contains(Constants.EMBASSY)) {
                binding.checkBoxEmbassy.setChecked(true);
            }

            if (POISet.contains(Constants.GUS_STATION)) {
                binding.checkBoxGusStation.setChecked(true);
            }

            if (POISet.contains(Constants.HINDU_TEMPLE)) {
                binding.checkBoxHinduTemple.setChecked(true);
            }

            if (POISet.contains(Constants.HOSPITAL)) {
                binding.checkBoxATM.setChecked(true);
            }

            if (POISet.contains(Constants.LIGHT_RAIL_STATION)) {
                binding.checkBoxATM.setChecked(true);
            }

            if (POISet.contains(Constants.LOCAL_GOVERNMENT_OFFICE)) {
                binding.checkBoxLocalGovernmentOffice.setChecked(true);
            }

            if (POISet.contains(Constants.MOSQUE)) {
                binding.checkBoxMosque.setChecked(true);
            }

            if (POISet.contains(Constants.PARK)) {
                binding.checkBoxPark.setChecked(true);
            }

            if (POISet.contains(Constants.PHARMACY)) {
                binding.checkBoxPharmacy.setChecked(true);
            }

            if (POISet.contains(Constants.STADIUM)) {
                binding.checkBoxStadium.setChecked(true);
            }

            if (POISet.contains(Constants.STORE)) {
                binding.checkBoxStore.setChecked(true);
            }

            if (POISet.contains(Constants.SUBWAY_STATION)) {
                binding.checkBoxSubwayStation.setChecked(true);
            }

            if (POISet.contains(Constants.SYNAGOGUE)) {
                binding.checkBoxSynagogue.setChecked(true);
            }

            if (POISet.contains(Constants.TRAIN_STATION)) {
                binding.checkBoxTrainStation.setChecked(true);
            }

            if(POISet.contains(Constants.ART_GALLERY)) {
                binding.checkBoxArtGallery.setChecked(true);
            }

            if(POISet.contains(Constants.HOSPITAL)) {
                binding.checkBoxHospital.setChecked(true);
            }

            if(POISet.contains(Constants.LIGHT_RAIL_STATION)) {
                binding.checkBoxLightRailStation.setChecked(true);
            }

            if(POISet.contains(Constants.ZOO)) {
                binding.checkBoxZoo.setChecked(true);
            }

        }
    }
}



