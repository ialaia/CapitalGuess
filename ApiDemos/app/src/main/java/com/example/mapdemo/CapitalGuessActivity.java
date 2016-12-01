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
import java.lang.Math;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;

/**
 * This shows how to add a ground overlay to a map.
 */
public class CapitalGuessActivity extends AppCompatActivity
        implements OnSeekBarChangeListener, OnMapReadyCallback,
        GoogleMap.OnGroundOverlayClickListener {


    private static LatLng m_MapCenter = new LatLng(43.6761, -79.4105);

    private FrameLayout m_MainMenu;
    private FrameLayout m_GameOptionsMenu;
    private LinearLayout m_SettingsMenu;
    private RelativeLayout m_GameView;
    private LinearLayout m_EndGameOverlay;


    private static final String DEBUGTAG = "CapitalGuessDebug";

    //Game Variables
    private int m_NumberOfRounds = 10;
    private boolean m_HasClues = true;
    private boolean m_HasMapInfo = true;
    private int m_NumberOfCountries = 50;
    private ArrayList<Integer> m_PreviouslySelectedCountries = new ArrayList<Integer>();
    private ArrayList<JSONObject> m_Data = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capital_guess);


        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        m_MainMenu = (FrameLayout) findViewById(R.id.Main_Menu);//Layer->0
        m_GameOptionsMenu = (FrameLayout) findViewById(R.id.Game_Options_Menu);//Layer->1
        m_SettingsMenu = (LinearLayout) findViewById(R.id.Settings_Menu);//Layer->2
        m_GameView = (RelativeLayout) findViewById(R.id.Map_Screen);//Layer->3
        m_EndGameOverlay = (LinearLayout) findViewById(R.id.EndScreen);//Layer->4

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
        final Button btn_BackToMainMenuFromEndScreen = (Button) findViewById(R.id.btn_back_from_end_game_menu);
        btn_BackToMainMenuFromEndScreen.setOnClickListener(new View.OnClickListener() {
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
        map.setIndoorEnabled(false);
        map.setBuildingsEnabled(false);

        mapStyles = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_cgg_none);

        map.setMapStyle(mapStyles);
        map.setContentDescription("The game");
        try {
            loadCountryList(0);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            setNewLocation(map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            case 0: //MainMenu Active
                m_MainMenu.setVisibility(View.VISIBLE);
                m_GameOptionsMenu.setVisibility(View.GONE);
                m_SettingsMenu.setVisibility(View.GONE);
                m_GameView.setVisibility(View.GONE);
                m_EndGameOverlay.setVisibility(View.GONE);
                break;
            case 1: //Game Options Active
                m_MainMenu.setVisibility(View.GONE);
                m_GameOptionsMenu.setVisibility(View.VISIBLE);
                m_SettingsMenu.setVisibility(View.GONE);
                m_GameView.setVisibility(View.GONE);
                m_EndGameOverlay.setVisibility(View.GONE);
                break;
            case 2: //Settings Active
                m_MainMenu.setVisibility(View.GONE);
                m_GameOptionsMenu.setVisibility(View.GONE);
                m_SettingsMenu.setVisibility(View.VISIBLE);
                m_GameView.setVisibility(View.GONE);
                m_EndGameOverlay.setVisibility(View.GONE);
                break;
            case 3: //Gameplay Active
                m_MainMenu.setVisibility(View.GONE);
                m_GameOptionsMenu.setVisibility(View.GONE);
                m_SettingsMenu.setVisibility(View.GONE);
                m_GameView.setVisibility(View.VISIBLE);
                m_EndGameOverlay.setVisibility(View.GONE);
                break;
            case 4: //End Game Active
                m_EndGameOverlay.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Toggles the visibility between 100% and 50% when a {@link GroundOverlay} is clicked.
     */
    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
    }

    public String loadJSONFromAsset(String filename) throws IOException {
        Log.v(DEBUGTAG, "Loading JSON");
        InputStream is = getResources().openRawResource(R.raw.country_list_50);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();
        return jsonString;
    }

    private void setNewLocation(GoogleMap map) throws JSONException {
        boolean previouslyDone = false;
        int currentRandomIndex = 0;
        do{
            currentRandomIndex = (int)Math.floor( Math.random() * (double)m_NumberOfCountries);
            previouslyDone = false;
            for (int i = 0; i < m_PreviouslySelectedCountries.size(); ++i)
            {
                if (m_PreviouslySelectedCountries.get(i) == currentRandomIndex)
                {
                    previouslyDone = true;
                    break;
                }

            }
        }
        while (previouslyDone);

        m_PreviouslySelectedCountries.add(currentRandomIndex);

        double lat, lng;
        lat = (double)m_Data.get(currentRandomIndex).getJSONArray("latlng").get(0);
        lng = (double)m_Data.get(currentRandomIndex).getJSONArray("latlng").get(1);
        m_MapCenter =  new LatLng(lat, lng);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(m_MapCenter, 18));
     /*

        latPos = data[i].latlng[0];
        lngPos = data[i].latlng[1];
        country = data[i].name.common;
        countryOf = data[i].name.official;
        demonym = data[i].demonym;
        city = data[i].capital;
        countryArea = data[i].area;
        countryRegion = data[i].region;
        //returns an array
        countryLang = data[i].languages;
        testArray = data[0].languages;
        results.innerHTML = "";
        clueResults();*/
    }


    //Loads the json file based on selected difficulty/number of countries.
    public void loadCountryList(int difficulty) throws JSONException, IOException {
        String loadedFilename;
        switch(difficulty){
            case 0:
                loadedFilename = "country_list_50.json";
                m_NumberOfCountries = 50;
                break;
            case 1:
                loadedFilename = "country_list_100.json";
                m_NumberOfCountries = 100;
                break;
            case 2:
                loadedFilename = "country_list_185.json";
                m_NumberOfCountries = 185;
                break;
            case 3:
                loadedFilename = "country_list_238.json";
                m_NumberOfCountries = 238;
                break;
            default:
                loadedFilename = "country_list_50.json";
                m_NumberOfCountries = 50;
                break;
        }
        Log.v(DEBUGTAG, loadedFilename);
        JSONArray jArray = new JSONArray();

        String s = loadJSONFromAsset(loadedFilename);
        Log.v(DEBUGTAG, s);
        jArray = new JSONArray(s);

        for(int i = 0; i < jArray.length(); ++i){
            m_Data.add(new JSONObject(String.valueOf(jArray.get(i))));
        }
    }
    //Getters and setters for game variables.
    public void setNumberOfRounds(int num){
        m_NumberOfRounds = num;
    }
    public int getNumberOfRounds(){
        return m_NumberOfRounds;
    }
    public void setClues(boolean val){
        m_HasClues = val;
    }
    public boolean getClues(){
        return m_HasClues;
    }
    public void setMapInfo(boolean val){
        m_HasMapInfo = val;
    }
    public boolean getMapInfo(){
        return m_HasMapInfo;
    }


}
