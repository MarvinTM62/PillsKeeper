package com.example.pillskeeper

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MapsActivity : AppCompatActivity() {
    lateinit var spType: Spinner
    lateinit var btFind: Button
    lateinit var supportMapFragment: SupportMapFragment
    lateinit var map: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var currentLat: Double = 0.0
    var currentLong: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        spType = findViewById(R.id.sp_type)
        btFind = findViewById(R.id.bt_find)
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        //Initialize array of place type
        var placeTypeList = arrayOf("pharmacy", "supermarket")
        //Initialize array of place name
        var placeNameList = arrayOf("Farmacie", "Supermercati")

        //Set adapter on spinner
        spType.adapter = ArrayAdapter<Any?>(
            this@MapsActivity,
            android.R.layout.simple_spinner_dropdown_item,
            placeNameList
        )

        //Initialize fused location provider client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)

        //Check permission
        if(ActivityCompat.checkSelfPermission(this@MapsActivity
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation()
        } else {
            //When permission denied
            //Request permission
            ActivityCompat.requestPermissions(this@MapsActivity
                ,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 44)
        }

        btFind.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                //Get selected position of spinner
                var i = spType.selectedItemPosition
                //Initialize url
                var url: String = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + //Url
                        "location=" + currentLat + "," + currentLong + //Location latitude and longitude
                        "&radius=5000" + //Nearby radius
                        "&type=" + placeTypeList[i] + //Place type
                        "&sensor=true" + //Sensor
                        "&key=" + resources.getString(R.string.google_maps_key)//Google map key

                //Execute place task method to download json data
                PlaceTask().execute(url)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        //Initialize task Location
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener(object: OnSuccessListener<Location> {
            override fun onSuccess(location: Location?) {
                //When success
                if(location != null) {
                    //Get current latitude
                    currentLat = location.latitude
                    //Get current longitude
                    currentLong = location.longitude
                    //Sync map
                    supportMapFragment.getMapAsync(object: OnMapReadyCallback {
                        override fun onMapReady(googleMap: GoogleMap?) {
                            //When map is ready
                            if (googleMap != null) {
                                map = googleMap
                            }
                            //Zoom current location on map
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(currentLat, currentLong), 10F
                            ))
                        }
                    })
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 44) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //When permission granted
                //Call method
                getCurrentLocation()
            }
        }
    }

    inner class PlaceTask: AsyncTask<String, Integer, String>() {
        override fun doInBackground(vararg strings: String): String? {
            var data: String? = null
            try {
                //Initialize data
                data = downloadUrl(strings[0])
            } catch(e: IOException) {
                e.printStackTrace()
            }
            return data
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            //Execute parser task
            ParserTask().execute(s)
        }
    }

    @Throws(IOException::class)
    private fun downloadUrl(string: String): String {
        //Initialize url
        var url: URL = URL(string)
        //Initialize connection
        var connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        //Connect connection
        connection.connect()
        //Initialize input stream
        var stream: InputStream = connection.inputStream
        //Initialize buffer reader
        var reader: BufferedReader = BufferedReader(InputStreamReader(stream))
        //Initialize string builder
        var builder: StringBuilder = StringBuilder()
        //Initialize string variable
        var line: String? = ""
        line = reader.readLine()
        while(line != null) {
            builder.append(line)
            line = reader.readLine()
        }
        var data: String = builder.toString()
        reader.close()
        //Return data
        return data
    }

    inner class ParserTask: AsyncTask<String,Integer,List<HashMap<String,String>>>() {
        override fun doInBackground(vararg strings: String?): List<HashMap<String, String>>? {
            //Create json parser class
            var jsonParser: JsonParser = JsonParser()
            //Initialize hash map list
            var mapList: List<HashMap<String, String>>? = null
            var obj: JSONObject? = null
            try {
                //Initialize json object
                    if(strings[0] != null) {
                        obj = JSONObject(strings[0])
                        //Parse json object
                        mapList = jsonParser.parseResult(obj)
                    }
            } catch(e: JSONException) {
                e.printStackTrace()
            }
            //Return map list
            return mapList
        }

        override fun onPostExecute(hashMaps: List<HashMap<String, String>>?) {
            super.onPostExecute(hashMaps)
            map.clear()
            //Use for loop
            if (hashMaps != null) {
                for (i in hashMaps.indices) {
                    //Initialize hash map
                    var hashMapList: HashMap<String,String> = hashMaps.get(i)
                    //Get latitude
                    var lat: Double = (hashMapList.get("lat"))!!.toDouble()
                    //Get longitude
                    var lng: Double = (hashMapList.get("lng"))!!.toDouble()
                    //Get name
                    var name: String = (hashMapList.get("name"))!!
                    //Concat latitude and longitude
                    var latLng: LatLng = LatLng(lat,lng)
                    //Initialize marker options
                    var options: MarkerOptions = MarkerOptions()
                    //Set position
                    options.position(latLng)
                    //Set title
                    options.title(name)
                    //Add marker on map
                    map.addMarker(options)
                }
            }
        }
    }
}