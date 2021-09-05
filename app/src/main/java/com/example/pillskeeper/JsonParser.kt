package com.example.pillskeeper

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonParser {
    private fun parseJsonObject(obj: JSONObject): HashMap<String, String>{
        //Initialize hash map
        var dataList: HashMap<String, String> = HashMap()
        try {
            //Get name from object
            var name: String = obj.getString("name")
            //Get latitude from object
            var latitude: String = obj.getJSONObject("geometry")
                .getJSONObject("location").getString("lat")
            //Get longitude from object
            var longitude: String = obj.getJSONObject("geometry")
                .getJSONObject("location").getString("lng")
            //Put all value in hash map
            dataList.put("name",name)
            dataList.put("lat",latitude)
            dataList.put("lng",longitude)
        } catch(e: JSONException) {
            e.printStackTrace()
        }
        //Return hash map
        return dataList
    }

    private fun parseJsonArray(jsonArray: JSONArray?): List<HashMap<String, String>> {
        //Initialize hash map list
        var dataList: MutableList<HashMap<String, String>> = ArrayList()
        if (jsonArray != null) {
            for (i in 0 until jsonArray.length()) {
                try {
                    //Initialize hash map
                    var data: HashMap<String, String> = parseJsonObject(jsonArray.get(i) as JSONObject)
                    //Add data in hash map list
                    dataList.add(data)
                } catch(e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        //Return hash map list
        return dataList
    }

    fun parseResult(obj: JSONObject): List<HashMap<String,String>> {
        //Initialize json array
        var jsonArray: JSONArray? = null
        //Get result array
        try {
            jsonArray = obj.getJSONArray("results")
        } catch(e: JSONException) {
            e.printStackTrace()
        }
        //Return array
        return parseJsonArray(jsonArray)
    }
}