package edu.rosehulman.vacationtracker;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;

public class
MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int RC_PERMISSIONS = 1;
    private GoogleMap mMap;
    private List<StateBoundary> mStateBoundaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mStateBoundaries = Utils.getStateBoundaries(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng terreHaute = new LatLng(39.4696, -87.3898);
        final Marker terreHauteMarker = mMap.addMarker(new MarkerOptions().position(terreHaute).title("Terre Haute"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(terreHaute, 5.0f));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                createMarker(latLng);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(terreHauteMarker)) {
                    Toast.makeText(MainActivity.this, "You clicked Terre Haute!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "You clicked " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

//        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
//
//            mMap.setMyLocationEnabled(true);
//        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MainActivity.this, "No location permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, RC_PERMISSIONS);
        }

        for (StateBoundary sb : mStateBoundaries) {
            int fillColor = sb.getColor();
            int alpha = 10;
            fillColor = ColorUtils.setAlphaComponent(fillColor, alpha);
            mMap.addPolygon(new PolygonOptions()
                    .fillColor(fillColor)
                    .strokeWidth(1.0f)
                    .addAll(sb.getVertices())
                    .clickable(true));
        }

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                int fillColor = polygon.getFillColor();
                int alpha = Color.alpha(fillColor) == 10 ? 95 : 10;
                fillColor = ColorUtils.setAlphaComponent(fillColor, alpha);
                polygon.setFillColor(fillColor);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RC_PERMISSIONS) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException se) {
                Toast.makeText(MainActivity.this, "Don't have security permission to add location", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_go_to_place_name:
                getPlaceNameOrAddress();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // The LatLng class is part of GooglePlayServices, which you will add when you add a MapsActivity.
    private void createMarker(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_marker_title, latLng.latitude, latLng.longitude));
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_marker, null, false);
        final EditText editTextTitle = (EditText) view.findViewById(R.id.dialog_add_marker_edit_text_title);
        final EditText editTextSnippet = (EditText) view.findViewById(R.id.dialog_add_marker_edit_text_snippet);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editTextTitle.getText().toString();
                String snippet = editTextSnippet.getText().toString();

                // TODO: Add a marker at that location.
                mMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet));
            }
        });
        builder.create().show();
    }


    private void getPlaceNameOrAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_get_place_name_title);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_get_place_name, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_get_place_name_edit_text);
        final ToggleButton toggleButtonZoom = (ToggleButton) view.findViewById(R.id.dialog_get_place_name_toggle_button);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String placeName = editText.getText().toString();
                float zoomLevel = toggleButtonZoom.isChecked() ? 17.0f : 7.0f;
                goToPlace(placeName, zoomLevel);
            }
        });
        builder.create().show();
    }

    private void goToPlace(String locationName, float zoomLevel) {
        Geocoder geocoder = new Geocoder(this);
        if (geocoder.isPresent()) {
            try {
                List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
                if (addresses.size() == 0) {
                    Toast.makeText(MainActivity.this, "Place not found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Address address = addresses.get(0);
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Network connection not working", Toast.LENGTH_SHORT).show();
                Log.d("goToPlace: ", e.getMessage());
            } catch (IllegalArgumentException e) {
                Toast.makeText(MainActivity.this, "No space entered", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
