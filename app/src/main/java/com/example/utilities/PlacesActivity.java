package com.example.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class PlacesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;

    TextView textView;
    final String APIKEY = "AIzaSyCbSFgyQgfK-9FezLOXXOmN8FkVG9xqwvI";

    double longitude; // 경도
    double latitude;   // 위도
    double altitude;   // 고도
    float accuracy;    // 정확도
    String provider;   // 위치제공자
    LatLng selectedPosition, myPosition; // 내 위치

    private final int AUTOCOMPLETE_REQUEST_CODE = 105;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //textView = findViewById(R.id.textView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), APIKEY);
        }

        //findNearbyPlaces();

        AutoCompleteWidget();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 서울 신사역 37.516066 127.019361
        myPosition = new LatLng(37.516066, 127.019361);
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Sinsa"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));

//        dialog = new ProgressDialog(MapsActivity.this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("현재 위치를 찾는 중...");
//        dialog.show(); // ProgressDialog 띄우기

        myLocation();
        AutoCompleteActivity();
    }

    private void myLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 핸드폰의 GPS 센서로 받는 위치(정확도가 더 높음)
                3000, 10, locationListener); // 통지사이의 최소 시간간격(ms), 통지사이의 최소 변경거리(m)

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 통신회사에서 받는 위치
                3000, 10, locationListener);
    }
    /**
     * 자신의 위치를 찾는 Location 리스너
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {

            longitude = location.getLongitude(); // 경도
            latitude = location.getLatitude();   // 위도
            altitude = location.getAltitude();   // 고도
            accuracy = location.getAccuracy();    // 정확도
            provider = location.getProvider();   // 위치제공자

            // 내 위치
            myPosition = new LatLng(latitude, longitude); // 위도, 경도
            mMap.addMarker(new MarkerOptions().position(myPosition).title("My Location")); // 내 위치와 마커 클릭시 나오는 텍스트
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 18)); // 화면을 내 위치로 이동시키는 함수, Zoom Level 설정

            //dialog.dismiss(); // ProgressDialog 종료
        }

        @Override // Provider의 상태 변경시 호출
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override // GPS가 사용할 수 없었다가 사용할 수 있을 때 호출
        public void onProviderEnabled(String provider) {

        }

        @Override // GPS가 사용할 수 없을 때 호출
        public void onProviderDisabled(String provider) {

        }
    };

    /**
     * AutoCompleteWidget 미완성
     */
    private void AutoCompleteWidget() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                selectedPosition = place.getLatLng();
                Log.i("AutoC", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("AutoC", "An error occurred: " + status);
            }
        });
    }


    /**
     * AutoComplete Activity
     */
    private void AutoCompleteActivity() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("AutoCompleteTEST", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("AutoCompleteTEST", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    /**
     * 현재 위치에서 주위 장소들 찾아서 textView에 뿌려주는 함수
     */
    private void findNearbyPlaces() {

        PlacesClient placesClient = Places.createClient(this);


        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();
        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            placesClient.findCurrentPlace(request).addOnSuccessListener(((response) -> {
                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                    Log.i("TAGG", String.format("Place '%s' has likelihood: %f",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                    textView.append(String.format("Place '%s' has likelihood: %f\n",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
            })).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e("TAGG", "Place not found: " + apiException.getStatusCode());
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting

            getLocationPermission();
        }
    }

    private void getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                return;
            }
        }
    }

    /**
     * AutoComplete Prediction
     */
//    private void AutoCompletePrediction() {
//        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
//        // and once again when the user makes a selection (for example when calling fetchPlace()).
//        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
//
//        // Create a RectangularBounds object.
//        RectangularBounds bounds = RectangularBounds.newInstance(
//                new LatLng(-33.880490, 151.184363),
//                new LatLng(-33.858754, 151.229596));
//        // Use the builder to create a FindAutocompletePredictionsRequest.
//        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//        // Call either setLocationBias() OR setLocationRestriction().
//                .setLocationBias(bounds)
//                //.setLocationRestriction(bounds)
//                .setCountry("au")
//                .setTypeFilter(TypeFilter.ADDRESS)
//                .setSessionToken(token)
//                .setQuery(query)
//                .build();
//
//        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
//            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
//                Log.i(TAG, prediction.getPlaceId());
//                Log.i(TAG, prediction.getPrimaryText(null).toString());
//            }
//        }).addOnFailureListener((exception) -> {
//            if (exception instanceof ApiException) {
//                ApiException apiException = (ApiException) exception;
//                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//            }
//        });
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 리스너 해제
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                return;
            }
        }
        locationManager.removeUpdates(locationListener);
    }
}
