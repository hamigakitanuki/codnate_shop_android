package com.example.codnate_shop_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class Fragment_start2 extends Fragment implements OnMapReadyCallback, LocationListener, LocationSource {
    LocationManager locationManager;
    private GoogleMap mMap;
    private Marker mMarker;
    private View view;
    double latitude,longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_2,container,false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        //getLocation(location);
        mapFragment.getMapAsync(this);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1000);
        } else {
            //Mapの初期化
            MapsInitializer.initialize(getActivity());
            locationStart();
            locationManager.requestLocationUpdates(NETWORK_PROVIDER,
                    1000, 50, this);

        }
        Button button = view.findViewById(R.id.start2_yes_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager viewPager = getActivity().findViewById(R.id.opening_pager);
                viewPager.arrowScroll(2);
                SharedPreferences data = getActivity().getSharedPreferences("DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = data.edit();
                editor.putFloat("latitube",(float)latitude);
                editor.putFloat("longitube",(float)longitude);
            }
        });
        Button button2 = view.findViewById(R.id.start2_onemore_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationStart();
            }
        });
        return view;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }



    private void locationStart() {
        //mMarkerに何か入っていた場合Markerを消す
        if(mMarker != null){
            mMarker.remove();
            mMarker = null;
        }

        //LocationManagerインスタンス生成
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (locationManager == null && locationManager.isProviderEnabled(NETWORK_PROVIDER)) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        locationManager.requestLocationUpdates(NETWORK_PROVIDER, 1000, 50,  this);
    }

    //パーミッションチェックリストのリクエスト結果を受け取る
   @SuppressLint("MissingPermission")
    public void onRequestPermissionResult(int requestCode,
                                          @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            //使用が許可された
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //位置測定を始めるcodeへ飛ぶ
                locationStart();

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000 , 50,  this);
            }else{
                //それでも拒否された時の対応
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("この機能には位置情報へのアクセス権限が必要です。");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                        }
                    });

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                }
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //mMarkerに何か入っていた場合Markerを消す
        if(mMarker != null){
            mMarker.remove();
            mMarker = null;
        }
        //緯度の取得
        latitude = location.getLatitude();
        //経度の取得
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        //cameraを現在地に合わせる
        CameraUpdate cu = CameraUpdateFactory.newLatLng(
                new LatLng(latitude,longitude));
        mMap.moveCamera(cu);
        //アイコン表示
        mMap.addMarker(new MarkerOptions().position(latLng).title("現在地"));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
   /*public void getLocation(Location location){
        //テスト用
        //Log.d("debug","str1="+ location.getLatitude());

        //緯度の取得
        double str1 = location.getLatitude();
        //経度の取得
        double str2 = location.getLongitude();

        latLng = new LatLng(str1,str2);
        if(latLng != null){

        }else{
            Toast toast =Toast.makeText(this,
                    "現在地を取得できませんでした",Toast.LENGTH_SHORT);
            toast.show();

        }
    }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
      /*  //Cameraの初期位置を小倉駅に設定
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                new LatLng(33.887108,130.886251),5);
        mMap.moveCamera(cu);
       */

        //位置を取得
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},1000);
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);//現在地取得
         double str1 = location.getLatitude();                                     //緯度取得
         double str2 = location.getLongitude();                                    //経度取得
         Log.d("Latitude", String.valueOf(str1));
         Log.d("Longitude", String.valueOf(str2));
         LatLng latLng = new LatLng(str1,str2);
        //cameraを現在地に合わせる＋ズーム
         CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                 new LatLng(str1,str2),15);
         mMap.moveCamera(cu);
         //アイコン表示

         mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("現在地"));
    }



/*
    private void setMarker() {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("現在地");
        mMap.addMarker(markerOptions);
    }

*/

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

}
