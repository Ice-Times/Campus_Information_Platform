package com.example.campusinformationplatform;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Personal_Page  extends Fragment {

    private Global_Value gv;

    private String Cache_Head_Path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_page, container,false);


        gv = (Global_Value) getActivity().getApplication();
        Cache_Head_Path=gv.getCache_Head_Path();

        System.out.println("进入person");
        view.setClickable(true);

        return view;
    }
}
