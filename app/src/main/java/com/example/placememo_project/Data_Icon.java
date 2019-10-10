package com.example.placememo_project;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Data_Icon extends RealmObject {
    int button; //-- 사용자가 메모입력시 선택하는 버튼
    int buttonclick;  //-- 사용자가  메모입력시 선택하는 버튼에 클릭된 모양
    String name;  //--  사용자가 메모 입력시 미리 작성해둔 위치이름
    double longitude;  //-- 최종적으로 메모를 저장할때 넘겨주는 경도
    double latitude;   //-- 최종적으로 메모를 저장할때 넘겨주는 위도
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public int getButtonclick() {
        return buttonclick;
    }

    public void setButtonclick(int buttonclick) {
        this.buttonclick = buttonclick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
