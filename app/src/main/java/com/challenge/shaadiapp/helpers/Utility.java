package com.challenge.shaadiapp.helpers;

import android.database.Cursor;

import com.challenge.shaadiapp.modal.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utility {

    public static String USER_NA = "NA";
    public static String USER_ACCEPTED = "A";
    public static String USER_DECLINED = "D";

    public static ArrayList<UserInfo> parseUserList (JSONArray jArrayUsers){

        ArrayList<UserInfo> arrUserInfo = new ArrayList<>();

        try{
            for (int position = 0; position < jArrayUsers.length(); position++){
                JSONObject jObjectUser = jArrayUsers.getJSONObject(position);

                //Get User Name
                JSONObject jObjUserName = jObjectUser.optJSONObject("name");
                String u_name = jObjUserName.optString("first", "").concat(" ").concat(jObjUserName.optString("last", ""));

                //Get Date of birth and age
                JSONObject jObjDOB = jObjectUser.optJSONObject("dob");
                String date = jObjDOB.optString("date");
                int age = jObjDOB.optInt("age");

                //Get Other data necessary for a short description

                JSONObject jObjUserLocation = jObjectUser.optJSONObject("location");
                String city = jObjUserLocation.optString("city", "");
                String state = jObjUserLocation.optString("state", "");
                String country = jObjUserLocation.optString("country", "");

                String description = createDescription(age, city, state, country);

                //Get User Profile Pic Link for medium size
                JSONObject jObjProfilePic = jObjectUser.optJSONObject("picture");
                String profile_pic = jObjProfilePic.optString("large");

                String status = "NA"; //Not accepted or rejected yet

                arrUserInfo.add(new UserInfo(u_name.trim(),
                        convertDate(date), description.trim(), profile_pic, status));
            }
        }
        catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
        catch (Exception exception){
            exception.printStackTrace();
        }

        return arrUserInfo;
    }


    public static ArrayList<UserInfo> fetchInfoFromCursor (Cursor cursor){
        ArrayList<UserInfo> arrUserInfo = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String u_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USERNAME));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESC));
                String profile_pic = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PROFILEPIC));
                String status = cursor.getString(cursor.getColumnIndex(DatabaseHelper.STATUS));

                UserInfo userInfo = new UserInfo(u_name, date, description, profile_pic, status);

                arrUserInfo.add(userInfo);
            } while (cursor.moveToNext());
        }

        return arrUserInfo;
    }

    //Function to convert date from Z timestamp to dd-MM-yyyy format
    public static String convertDate(String datetime) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date dateObject = null;
        try
        {
            dateObject = inputFormat.parse(datetime);
        }
        catch (ParseException exception)
        {
            exception.printStackTrace();
        }
        String formattedDate = outputFormat.format(dateObject);

        return formattedDate;
    }

    //A getter class to generate small description to display below the profile pic.
    // I have omitted the data I am not getting in response, such as Height, Caste, Sub caste, education.
    public static String createDescription(int age, String city, String state, String country){
        String descriptionText = age + ", " + city +", " + state + " ," + country;
        return descriptionText;
    }
}
