package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_CityID, btn_GetWeatherByCityID, btn_GetWeatherByCityName;
    EditText et_dataInput;
    ListView LV_WeatherReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_CityID = findViewById(R.id.btn_GetCityID);
        btn_GetWeatherByCityID = findViewById(R.id.btn_GetWeatherByCityID);
        btn_GetWeatherByCityName = findViewById(R.id.btn_GetWeatherByCityName);

        et_dataInput = findViewById(R.id.et_dataInput);
        LV_WeatherReports = findViewById(R.id.Lv_WeatherReports);
        WeatherDataServices weatherDataServices = new WeatherDataServices(MainActivity.this);

        btn_GetWeatherByCityID.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                                        weatherDataServices.getCityForecastByID(et_dataInput.getText().toString(), new WeatherDataServices.ForeCastByIDResponse() {
                                                            @Override
                                                            public void onError(String message) {
                                                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onResponse(List<WeatherReportModule> weatherReportModule) {
                                                                ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModule);
                                                                    LV_WeatherReports.setAdapter(arrayAdapter);
                                                            }
                                                        });
                                                              }

                                                  }
        );
        btn_GetWeatherByCityName.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            weatherDataServices.getCityForecastByName(et_dataInput.getText().toString(), new WeatherDataServices.GetCityForecastByNameCallBack() {
                                                                @Override
                                                                public void onError(String message) {
                                                                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onResponse(List<WeatherReportModule> weatherReportModule) {
                                                                    ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,weatherReportModule);
                                                                    LV_WeatherReports.setAdapter(arrayAdapter);
                                                                }
                                                            });                                                        }

                                                    }
        );
        btn_CityID.setOnClickListener(new View.OnClickListener() {

                                          @Override
                                          public void onClick(View view) {

                                              weatherDataServices.getCityID(et_dataInput.getText().toString(), new WeatherDataServices.VolleyResponseListener() {
                                                  @Override
                                                  public void onError(String message) {
                                                      Toast.makeText(MainActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();

                                                  }

                                                  @Override
                                                  public void onResponse(String CityID) {
                                                      Toast.makeText(MainActivity.this, "Returned an ID of " + CityID, Toast.LENGTH_SHORT).show();

                                                  }
                                              });
//// Request a string response from the provided URL.
//
                                          }

                                      }
        );

    }
}