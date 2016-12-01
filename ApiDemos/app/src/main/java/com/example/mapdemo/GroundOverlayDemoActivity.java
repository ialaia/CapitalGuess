/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This shows how to add a ground overlay to a map.
 */
public class GroundOverlayDemoActivity extends AppCompatActivity
        implements OnSeekBarChangeListener, OnMapReadyCallback,
        GoogleMap.OnGroundOverlayClickListener {


    private static final LatLng NEWARK = new LatLng(43.6761, -79.4105);

    private FrameLayout m_MainMenu;
    private FrameLayout m_GameOptionsMenu;
    private LinearLayout m_SettingsMenu;
    private RelativeLayout m_GameView;

    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();
    private static final String DEBUGTAG = "MAIN DEBUG";

    private int mCurrentEntry = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ground_overlay_demo);


        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        m_MainMenu = (FrameLayout) findViewById(R.id.Main_Menu);//Layer->0
        m_GameOptionsMenu = (FrameLayout) findViewById(R.id.Game_Options_Menu);//Layer->1
        m_SettingsMenu = (LinearLayout) findViewById(R.id.Settings_Menu);//Layer->2
        m_GameView = (RelativeLayout) findViewById(R.id.Map_Screen);//Layer->3

        changeMenu(0);

        final Button btn_Settings = (Button) findViewById(R.id.btn_Settings);
        btn_Settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeMenu(2);
            }
        });
        final Button btn_Capitals = (Button) findViewById(R.id.btn_Capital_Mode);
        btn_Capitals.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeMenu(1);
            }
        });
        final Button btn_Country = (Button) findViewById(R.id.btn_Country_Mode);
        btn_Country.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeMenu(1);
            }
        });
        final Button btn_BackToMainMenuFromSettings = (Button) findViewById(R.id.btn_BackToMainMenuFromSettings);
        btn_BackToMainMenuFromSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeMenu(0);
            }
        });
        final Button btn_StartGame = (Button) findViewById(R.id.btn_Start);
        btn_StartGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeMenu(3);
            }
        });
        final Button btn_BackToMainMenuFromGameOptions = (Button) findViewById(R.id.btn_BackToMainMenuFromGameOptions);
        btn_BackToMainMenuFromGameOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeMenu(0);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        MapStyleOptions mapStyles;
        // Register a listener to respond to clicks on GroundOverlays.
        //map.setOnGroundOverlayClickListener(this);
        map.setIndoorEnabled(false);
        map.setBuildingsEnabled(false);
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(NEWARK, 18));
        //map.setMapType(2);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NEWARK, 18));


        mapStyles = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_cgg_none);

        map.setMapStyle(mapStyles);


          // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        map.setContentDescription("The game");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String logged = Integer.toString(seekBar.getProgress());
        Log.v(DEBUGTAG, logged );
    }

    private void changeMenu(int layer){
        switch(layer){
            case 0:
                m_MainMenu.setVisibility(View.VISIBLE);
                m_GameOptionsMenu.setVisibility(View.GONE);
                m_SettingsMenu.setVisibility(View.GONE);
                m_GameView.setVisibility(View.GONE);
                break;
            case 1:
                m_MainMenu.setVisibility(View.GONE);
                m_GameOptionsMenu.setVisibility(View.VISIBLE);
                m_SettingsMenu.setVisibility(View.GONE);
                m_GameView.setVisibility(View.GONE);
                break;
            case 2:
                m_MainMenu.setVisibility(View.GONE);
                m_GameOptionsMenu.setVisibility(View.GONE);
                m_SettingsMenu.setVisibility(View.VISIBLE);
                m_GameView.setVisibility(View.GONE);
                break;
            case 3:
                m_MainMenu.setVisibility(View.GONE);
                m_GameOptionsMenu.setVisibility(View.GONE);
                m_SettingsMenu.setVisibility(View.GONE);
                m_GameView.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Toggles the visibility between 100% and 50% when a {@link GroundOverlay} is clicked.
     */
    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
    }


}
