package com.marco.turistafelicetest.ui.around;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.marco.turistafelicetest.R;
import com.marco.turistafelicetest.Wikipedia.Page;
import com.marco.turistafelicetest.Wikipedia.WikiRepository;
import com.marco.turistafelicetest.Wikipedia.WikiViewModel;
import com.marco.turistafelicetest.models.Place;
import com.marco.turistafelicetest.models.Resource;
import com.marco.turistafelicetest.models.Trip;
import com.marco.turistafelicetest.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AroundFragment extends Fragment implements OnSuccessListener<Location>, GoogleMap.OnCameraMoveListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {
    private final String TAG = "AROUNDFRAGMENT";
    private MapView mapView;
    private GoogleMap map;
    private AroundViewModel aroundViewModel;
    private PlaceViewModel placeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aroundViewModel = new ViewModelProvider(this).get(AroundViewModel.class);
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        View root = inflater.inflate(R.layout.fragment_around, container, false);
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Observer dell'aggiunta di places, aggiunge Marker alla mappa
        placeViewModel.getPlacesLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<List<Place>>>() {
            @Override
            public void onChanged(Resource<List<Place>> listResource) {
                Log.i(TAG, "Dimensione lista places: "+listResource.getData().size());
                // Colorazione dei Marker in base alla categoria(tipo) a cui appartengono
                for(int i=0;i<listResource.getData().size();i++) {
                    Marker tmp = map.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(listResource.getData().get(i).getGeometry().getLocation().getLat()),
                                    Double.parseDouble(listResource.getData().get(i).getGeometry().getLocation().getLng())))
                            .title(listResource.getData().get(i).getName())
                            .snippet(listResource.getData().get(i).getTypes().toString().substring(1, listResource.getData().get(i).getTypes().toString().length() - 1))
                    );
                    if (listResource.getData().get(i).getTypes().contains("bar") || listResource.getData().get(i).getTypes().contains("bakery")
                            || listResource.getData().get(i).getTypes().contains("store") || listResource.getData().get(i).getTypes().contains("ATM")) {
                        tmp.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    }
                    if (listResource.getData().get(i).getTypes().contains("hospital") || listResource.getData().get(i).getTypes().contains("pharmacy")) {
                        tmp.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    if (listResource.getData().get(i).getTypes().contains("museum") || listResource.getData().get(i).getTypes().contains("aquarium")
                            || listResource.getData().get(i).getTypes().contains("tourist_attraction") || listResource.getData().get(i).getTypes().contains("art_gallery")
                            || listResource.getData().get(i).getTypes().contains("park") || listResource.getData().get(i).getTypes().contains("stadium")
                            || listResource.getData().get(i).getTypes().contains("zoo")) {
                        tmp.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    if (listResource.getData().get(i).getTypes().contains("church") || listResource.getData().get(i).getTypes().contains("hindu_temple")
                            || listResource.getData().get(i).getTypes().contains("mosque") || listResource.getData().get(i).getTypes().contains("synagogue")) {
                        tmp.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    }
                    if (listResource.getData().get(i).getTypes().contains("bus_station") || listResource.getData().get(i).getTypes().contains("car_rental")
                            || listResource.getData().get(i).getTypes().contains("light_rail_station") || listResource.getData().get(i).getTypes().contains("subway_station")
                            || listResource.getData().get(i).getTypes().contains("gas_station") || listResource.getData().get(i).getTypes().contains("train_station")) {
                        tmp.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }
                    if (listResource.getData().get(i).getTypes().contains("embassy") || listResource.getData().get(i).getTypes().contains("local_government_office")) {
                        tmp.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    }
                }
                // Invocato se next_page_token della richiesta aplaces è non nullo. indica la presenza di altri POI
                if(listResource.getNextPageToken() != null){
                    Log.i(TAG, "Next page non nullo. CARICO");
                }
            }
        });
        // Richiesta dei permessi di utilizzo della posizione
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getContext(),getString(R.string.around_perm_pos), Toast.LENGTH_LONG).show();
            } else {
                int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            // L'autorizzazione all'utilizzo della posizione è stata concessa
            map.setMyLocationEnabled(true);
            map.getUiSettings().setRotateGesturesEnabled(true);
            map.getUiSettings().setZoomGesturesEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.setOnCameraMoveListener(this);
            map.setInfoWindowAdapter(this);
            map.setOnInfoWindowClickListener(this);
            // Richiedo posizione da GPS/Wifi/Celle telefoniche
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), this);
        }
    }
    // Eseguito quando la mappa viene spostata
    @Override
    public void onCameraMove() {
        LatLng nCentre = map.getCameraPosition().target;
        LatLng prev = aroundViewModel.getPos().getValue();
        float[] res = new float[1];
        Location.distanceBetween(nCentre.latitude, nCentre.longitude, prev.latitude, prev.longitude, res);
        // Aggiorno la posizione solo se la distanza è superiore a 1km
        if (res[0] >= 1000f) {
            aroundViewModel.setPos(nCentre);

        }
    }
    // Eseguito quando posizione è stata stabilita da GPS
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            aroundViewModel.setPos(pos);
            map.moveCamera(CameraUpdateFactory.newLatLng(pos));
            map.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
            map.getUiSettings().setMyLocationButtonEnabled(false);
            // Observer dei cambi di posizione
            aroundViewModel.getPos().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
                @Override
                public void onChanged(LatLng pos) {
                    map.moveCamera(CameraUpdateFactory.newLatLng(pos));
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                    Set<String> POISet = new HashSet<>();
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_POI, Context.MODE_PRIVATE);
                    POISet = sharedPref.getStringSet(Constants.SHARED_PREFERENCES_SELECTED_POI, null);
                    // Richiedo nuovi POI perchè la posizione è cambiata o la mappa è stata mossa
                    if(POISet != null) {
                        for (String s : POISet) {
                            if(map.getCameraPosition().zoom > 14.0f) {
                                String language = Locale.getDefault().getDisplayLanguage();
                                if(language.equals("italiano")) {
                                    placeViewModel.getPlacesResource(pos, s, "it");
                                }else{
                                    placeViewModel.getPlacesResource(pos, s, "en");
                                }
                            }
                        }
                    }
                }
            });
        }
    }
    // Implementazione di InfoWindow personalizzate
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View mContents = getLayoutInflater().inflate(R.layout.around_marker_infowindow, null);
        TextView name = mContents.findViewById(R.id.around_infowindow_title);
        TextView maintype = mContents.findViewById(R.id.around_infowindow_maintype);
        TextView wikiurl = mContents.findViewById(R.id.around_infowindow_wikiurl);
        TextView wikicont = mContents.findViewById(R.id.around_content);
        wikicont.setVisibility(View.INVISIBLE);
        wikiurl.setVisibility(View.INVISIBLE);
        name.setText(marker.getTitle());
        name.setTypeface(null,Typeface.BOLD);
        maintype.setText(marker.getSnippet().replace('_', ' '));
        // Observer della richiesta a wikipedia, necessario per verificare l'esistenza della pagina
        WikiViewModel wikiViewModel = new ViewModelProvider(this).get(WikiViewModel.class);
        final Observer<Page> observer = new Observer<Page>() {
            @Override
            public void onChanged(Page page) {
                if(page.getContent() != null) {
                    marker.setTag(page.getContent());
                }
            }
        };
        wikiViewModel.getInfo(marker.getTitle().replace(' ', '_')).observe(this,observer);
        return mContents;
    }

    // Callback per click su InfoWindow
    @Override
    public void onInfoWindowClick(Marker marker) {
        // Visualizza Dialog per scegliere l'azione da compiere sul POI
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.around_choose));
        String[] opts = new String[4];
        if(marker.getTag() == null){
            opts[0] = getString(R.string.around_add_existing);
            opts[1] = getString(R.string.around_add_favorites);
            opts[2] = "";
            opts[3] = "";
        }else {
            opts[0] = getString(R.string.around_add_existing);;
            opts[1] = getString(R.string.around_add_favorites);
            opts[2] = getString(R.string.around_go_wiki);;
            opts[3] = getString(R.string.around_show_desc);
        }
        builder.setItems(opts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    // L'azione scelta è di aggiungere il POI ad un viaggio
                    List<Trip> trips = new ArrayList<>();
                    String fileName = "Planning.txt";

                    File file = new File(getContext().getFilesDir() + "/" + fileName);
                    if(!file.exists()) {
                        Toast.makeText(getContext(), getString(R.string.around_no_trips), Toast.LENGTH_LONG).show();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getString(R.string.around_choose_trip));
                    String[] tripsA = new String[trips.size()];
                    for(int i=0;i<trips.size();i++){
                        tripsA[i] = trips.get(i).getTitle();
                    }
                    builder.setItems(tripsA, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddPOI(trips.get(which).getId(), marker.getTitle());
                        }
                    });
                    AlertDialog dialogT = builder.create();
                    dialogT.show();
                }
                if(which == 1){
                    // L'azione scelta è aggiungi ai preferiti
                    SharedPreferences sharedPref = getContext().getSharedPreferences(Constants.POI_SHARED_PREFERENCES_FILE_NAME_POI, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    Set<String> favourite = new HashSet<String>(sharedPref.getStringSet(Constants.FAVOURITES, new HashSet<String>()));
                    favourite.add(marker.getTitle());
                    editor.putStringSet(Constants.FAVOURITES,favourite);
                    editor.apply();
                    Toast.makeText(getContext(), getString(R.string.around_add_trip_ok),Toast.LENGTH_LONG).show();
                }
                if(which == 2){
                    // L'azione scelta è aprire wikipedia sulla pagina del POI
                    if(opts[which].equals(getString(R.string.around_go_wiki))) {
                        String language = Locale.getDefault().getDisplayLanguage();
                        if(language.equals("italiano")) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(Constants.WIKI_URL_IT + marker.getTitle().replace(' ', '_')));
                            startActivity(i);
                        }
                        else {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(Constants.WIKI_URL_EN + marker.getTitle().replace(' ', '_')));
                            startActivity(i);
                        }
                    }
                }
                if(which == 3){
                    if(marker.getTag() != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(getString(R.string.around_desc_title));
                        builder.setMessage((String) marker.getTag());
                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog al = builder.create();
                        al.show();
                    }
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Modifica il viaggio definito da id aggiungendo POI
    private void AddPOI(int id, String POI){
        Trip temp_trip;

        try {
            BufferedReader file = new BufferedReader(new FileReader(getContext().getFilesDir() + "/" + Constants.FILE_NAME));
            StringBuilder inputBuffer = new StringBuilder();
            String line;

            while ((line = file.readLine()) != null) {
                temp_trip = new Trip(line);
                if(temp_trip.getId() == id) {
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
            FileOutputStream fileOut = new FileOutputStream(getContext().getFilesDir() + "/" + Constants.FILE_NAME);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            Toast.makeText(getContext(), getString(R.string.around_add_trip_ok),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null) {
            mapView.onDestroy();
        }
    }
}
