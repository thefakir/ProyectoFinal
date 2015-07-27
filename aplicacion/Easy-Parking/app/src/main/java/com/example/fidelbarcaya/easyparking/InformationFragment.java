package com.example.fidelbarcaya.easyparking;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fidel.barcaya on 4/4/2015.
 */
public class InformationFragment extends Fragment {
    TextView text,vers;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_information, container, false);
        text= (TextView) view.findViewById(R.id.AndroidOs);
        vers= (TextView)view.findViewById(R.id.Version);
        return view;
    }
    public void change(String txt, String txt1){
        text.setText(txt);
        vers.setText(txt1);
    }
    public void hide(View view){
        view.setBackgroundColor(Color.BLACK);
        LinearLayout l = (LinearLayout)view.findViewById(R.id.slide);
        l.setBackgroundColor(Color.BLACK);
    }
}