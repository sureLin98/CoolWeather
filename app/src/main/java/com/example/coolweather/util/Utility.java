package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.gson.HeWeather;
import com.example.coolweather.db.City;
import com.example.coolweather.db.Country;
import com.example.coolweather.db.Province;
import com.google.gson.Gson;;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 解析处理从服务器获取的JSON数据
 */
public class Utility {

    /**
     * 解析处理省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Province province=new Province();
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.setProvinceName(jsonObject.getString("name"));
                    province.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return  false;
    }

    /**
     * 解析市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    City city=new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析县级数据
     */
    public static boolean handleCountryResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Country country=new Country();
                    country.setCityId(cityId);
                    country.setCountryName(jsonObject.getString("name"));
                    country.setWeatherId(jsonObject.getString("weather_id"));
                    country.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 用GSON解析数据
     */
    public static HeWeather handleWeatherResponseByGSON(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,HeWeather.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 用Jackson解析接送数据
     */
    public static HeWeather handleWeatherResponseByJaskson(String response){
        ObjectMapper objectMapper=JacksonMapper.getMapper();
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();

            HeWeather heWeather=objectMapper.readValue(weatherContent,HeWeather.class);/**解析json得到HeWeather实体类**/

            return heWeather;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析bing每日一图数据
     */
    public static String handleBingPicResponse(String reponse){
        try{
            JSONArray jsonArray=new JSONArray(reponse);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            String pic_url=jsonObject.getString("url");
            return pic_url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
