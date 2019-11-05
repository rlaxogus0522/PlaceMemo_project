package com.example.placememo_project.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.R;
import com.example.placememo_project.databinding.ActivityLocationBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

import static com.example.placememo_project.activity.InsertActivity.locationName;


public class LocationActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private final static String TAG = "LocationActivity : ";
    ActivityLocationBinding lBinding;
    private int icon, clickicon;  //--  아이콘 추가시 클릭/미클릭 이미지 구분
    private GoogleMap mMap;
    LocationManager manager;
    double longitude;
    double latitude;
    private boolean isIconcheck = false;  //-- 사용자가 아이콘을 선택했는지 구분
    CircleOptions circle; //원점
    Geocoder geocoder;   //-- 지역검색을 위한 메소드
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        lBinding = DataBindingUtil.setContentView(this, R.layout.activity_location);
       circle = new CircleOptions();
        geocoder= new Geocoder(this);
        lBinding.btnAddIcon.setOnClickListener(this);
        lBinding.btnAddlocation.setOnClickListener(this);
        lBinding.btnSerch.setOnClickListener(this);
        lBinding.btnMylocation.setOnClickListener(this);
        lBinding.serchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {  //-- 사용자 키보드 엔터버튼을 검색으로 설정
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
               if(i== EditorInfo.IME_ACTION_SEARCH){
                   locationSerch();
               }
                return false;
            }
        });
        lBinding.locationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //-- 사용자가 포커싱을 하면 해당내용 비워줌
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    lBinding.locationName.setText("");
                }
            }
        });
            startLastLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startLocation() { //-- 위치 검색 시작
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        long minTime = 0;
        float minDistance = 0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocation);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocation);

    }

    public void startLastLocation() { //-- 위치 검색 시작
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location1 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location != null){
            setMyLocation(location);
        }else if(location1 != null){
            setMyLocation(location1);
        }else{
            startLocation();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(LocationActivity.this);
    }

    private  void setMyLocation(Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    private void stopLocation() { //--  위치 검색 종료
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.removeUpdates(mLocation);

    }


    LocationListener mLocation = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(LocationActivity.this);
                stopLocation();  //-- 위치를 가져온 후 위치 검색 종료

            }catch (Exception e){}

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                mMap.clear();
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mMap.clear();
                LatLng location = cameraPosition.target;
                latitude = location.latitude;
                longitude = location.longitude;
                circle.center(location);
                circle.radius(200);      //반지름 단위 : m
                circle.strokeWidth(0f);  //선너비 0f : 선없음
                circle.fillColor(Color.parseColor("#500000ff")); //배경색
                mMap.addCircle(circle);
            }
        });
        LatLng selectLocation = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectLocation, 15));   //-- 현재 내 위치로 마커 이동


    }




    @Override
    public void onClick(View v) {
        if (v == lBinding.btnSerch) {
            locationSerch();
        } else if (v == lBinding.btnMylocation) {  //-- 현재 내위치를 가져오는 메소드
            startLocation();
            Toast.makeText(this, "위치 정보 받아오는중..", Toast.LENGTH_SHORT).show();
        } else if (v == lBinding.btnAddIcon) {
            Intent intent = new Intent(this, IconActivity.class);
            startActivityForResult(intent, 0522);
        } else if (v == lBinding.btnAddlocation) {  //-- 최종 위치 추가를 클릭했을시 해당하는 내용 위치추가버튼을 눌렀던 메모추가액티비티로 전송
            if(!locationName.contains(lBinding.locationName.getText().toString()) && isIconcheck) {  //-- 만약 위치를 선택했다면
                Intent intent = new Intent();
                intent.putExtra("name", lBinding.locationName.getText().toString());
                intent.putExtra("icon", icon);
                intent.putExtra("clickicon", clickicon);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                if(locationName.contains(lBinding.locationName.getText().toString())) Toast.makeText(this,"이미 존재하는 장소명 입니다.",Toast.LENGTH_LONG).show();
                if(!isIconcheck) Toast.makeText(this,"아이콘을 선택해주세요.",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void locationSerch() {
        List<Address> list = null;
        String address = lBinding.serchLocation.getText().toString();
        lBinding.locationName.setText(lBinding.serchLocation.getText().toString());
        lBinding.locationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                return;
            }
        });
        try{
            list = geocoder.getFromLocationName(address,10);
        }catch (IOException e){
            Log.d(TAG,e+"");
        }
        if(list != null){
            if(list.size() == 0) {
                Toast.makeText(this,"해당되는 주소 정보가 없습니다.",Toast.LENGTH_LONG).show();
            }else{
                Address address1 = list.get(0);
                latitude = address1.getLatitude();
                longitude = address1.getLongitude();
                LatLng selectLocation = new LatLng(latitude, longitude);
//                marker(selectLocation);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectLocation, 15));
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //-- 사용자가 선택한 아이콘 정보를 가져와서 보여줌
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            icon = data.getIntExtra("icon", -1);
            clickicon = data.getIntExtra("clickicon", -1);
            lBinding.btnAddIcon.setBackgroundResource(data.getIntExtra("icon", -1));
            isIconcheck = true;
        } else {
            Log.d(TAG, String.valueOf(resultCode));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true;
    }




}
