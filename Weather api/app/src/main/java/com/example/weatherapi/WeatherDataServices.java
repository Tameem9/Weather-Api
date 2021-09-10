package com.example.weatherapi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataServices {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
    Context context;
    String CityID;

    public WeatherDataServices(Context context) {
        this.context = context;
    }

    public WeatherDataServices() {

    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String CityID);
    }

    public void getCityID(String cityID, VolleyResponseListener volleyResponseListener) {
        String url = QUERY_FOR_CITY_ID + cityID;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                CityID = "";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    CityID = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                volleyResponseListener.onResponse(CityID);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyResponseListener.onError("Error");
                    }
                });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface ForeCastByIDResponse {
        void onError(String message);

        void onResponse(List<WeatherReportModule> weatherReportModule);
    }

    public void getCityForecastByID(String cityID, ForeCastByIDResponse foreCastByIDResponse) {
        List<WeatherReportModule> weatherReportModels = new ArrayList<>();
        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");


                    for(int i=0;i<consolidated_weather_list.length();i++) {
                        WeatherReportModule one_day = new WeatherReportModule();
                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);
                        one_day.setId(first_day_from_api.getInt("id"));
                        one_day.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        one_day.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                        one_day.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        one_day.setCreated(first_day_from_api.getString("created"));
                        one_day.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        one_day.setMin_temp(first_day_from_api.getLong("min_temp"));
                        one_day.setThe_temp(first_day_from_api.getLong("the_temp"));
                        one_day.setWind_speed(first_day_from_api.getInt("wind_speed"));
                        one_day.setWind_direction(first_day_from_api.getLong("wind_direction"));
                        one_day.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                        one_day.setHumidity(first_day_from_api.getInt("humidity"));
                        one_day.setVisibility(first_day_from_api.getInt("visibility"));
                        one_day.setPredictability(first_day_from_api.getInt("predictability"));
                        weatherReportModels.add(one_day);
                    }
                    foreCastByIDResponse.onResponse(weatherReportModels);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        });
        MySingleton.getInstance(context).addToRequestQueue(request);

    }
        public interface GetCityForecastByNameCallBack{
        void onError(String message);
        void onResponse(List<WeatherReportModule>weatherReportModules );
        }
public void getCityForecastByName(String cityName,GetCityForecastByNameCallBack getCityForecastByNameCallBack){
    getCityID(cityName, new VolleyResponseListener() {
        @Override
        public void onError(String message) {

        }

        @Override
        public void onResponse(String CityID) {
getCityForecastByID(CityID, new ForeCastByIDResponse() {
    @Override
    public void onError(String message) {

    }

    @Override
    public void onResponse(List<WeatherReportModule> weatherReportModule) {
getCityForecastByNameCallBack.onResponse(weatherReportModule);
    }
});
        }
    });
}

}