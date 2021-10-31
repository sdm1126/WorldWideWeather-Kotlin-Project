package kr.or.mrhi.openweather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.runBlocking
import kr.or.mrhi.openweather.Data.WeatherData
import kr.or.mrhi.openweather.databinding.ActivityMapBinding
import org.jetbrains.anko.locationManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    // view binding
    private lateinit var binding : ActivityMapBinding

    // permission
    private val PERMISSIONS_REQUEST_CODE = 1001
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    // google map
    private val markerOptions = MarkerOptions()
    private var latitude : Double? = null
    private var longitude : Double? = null
    private var address : String? = null

    // openweather
    private val WeatherApi = WeatherClient.apiService
    private lateinit var weatherSource : WeatherSource
    private val weather = MutableLiveData<WeatherData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init
        findMyLatLng()
        findMyLocation()
        val mapFragment = supportFragmentManager.findFragmentById(binding.fcvMap.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.clMap.setOnClickListener {
            Intent(this@MapActivity, WeatherActivity::class.java).apply {
                putExtra("weatherSource", weatherSource)
                putExtra("address", address)
            }.run {
                startActivity(this)
            }
        }
    }

    // 현 위치의 위도, 경도를 구하는 메소드
    private fun findMyLatLng() : Location {
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        var myLatLng : Location?

        // 위치 액세스 권한 요청 승인 상태 시,
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // getLastKnownLocation(): 가장 마지막에 기록된 위치 정보를 가져오는 메소드
            var locationProvider = LocationManager.GPS_PROVIDER
            myLatLng = locationManager.getLastKnownLocation(locationProvider)
            if(myLatLng == null){
                myLatLng = locationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER )
            }


            // 현재 위치 액세스 권한 요청 미승인 상태 시,
        } else {
            // 이전에 위치 액세스 권한 요청 거부한 적이 있을 경우,
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){
                Toast.makeText(this, "위치 액세스 권한 승인이 필요합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
                // 이전에 위치 액세스 권한 요청 거부한 적이 별도로 없는 경우,
            } else{
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
            myLatLng = findMyLatLng() // 현재 메소드 재실행
        }

        return myLatLng!! // @NotNull 설정
    }

    // Geocoder로 위도, 경도를 실제 주소로 변환하는 메소드(내 위치)
    private fun findMyLocation() {
        var myLocation : Location = findMyLatLng()
        if(myLocation != null) {
            // 위도, 경도 확인
            latitude = myLocation.latitude
            longitude = myLocation.longitude
            Log.d("확인", "내 위치(좌표) : ${latitude}, ${longitude}")

            // 실제 주소 확인
            var geocoder = Geocoder(applicationContext, Locale.ENGLISH)
            var addressList : List<Address>? = null
            try {
                addressList = geocoder.getFromLocation(latitude!!, longitude!!, 1)
            } catch (e : IOException) {
                Log.e("확인", "findMyLocation()_" + e.printStackTrace())
            }
            if(addressList != null){
                address = addressList[0].getAddressLine(0)
                Log.d("확인", "내 위치(주소) : $address")
            }
        }
    }

    // Geocoder로 위도, 경도를 실제 주소로 변환하는 메소드(선택한 위치)
    private fun findSelectedLocation(latLng: LatLng) {
        if(latLng != null) {
            // 위도, 경도 확인
            latitude = latLng.latitude
            longitude = latLng.longitude
            Log.d("확인", "선택한 위치(좌표) : ${latitude}, ${longitude}")

            // 실제 주소 확인
            var geocoder = Geocoder(applicationContext, Locale.ENGLISH)
            var addressList : List<Address>? = null
            try {
                addressList = geocoder.getFromLocation(latitude!!, longitude!!, 1)
            } catch (e : IOException) {
                Log.e("확인", "findSelectedLocation()_" + e.printStackTrace())
            }
            if(addressList!!.isNotEmpty()){
                address = (addressList[0].adminArea + " " + addressList[0].countryName).replace("null", "")
                Log.d("확인", "선택한 위치(주소) : $address")
            } else {
                address = weather.value?.timezone + "(Timezone)" // 불러올 주소가 없을 경우, Openweather API 데이터의 timezone으로 대체
                Log.d("확인", "선택한 위치(주소) : $address")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // 첫 화면 내 위치 띄우기
        val myLocation = LatLng(latitude!!, longitude!!)
        markerOptions.title(address)
        markerOptions.snippet("My Location")
        markerOptions.position(myLocation)
        googleMap.addMarker(markerOptions)
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(myLocation))
        googleMap.uiSettings.setAllGesturesEnabled(true)

        // 지도 클릭 시 이벤트
        googleMap.setOnMapClickListener { latLng ->
            binding.clMap.visibility = View.VISIBLE
            googleMap.clear()
            findSelectedLocation(latLng)
            markerOptions.title(address)
            markerOptions.position(latLng)
            googleMap.addMarker(markerOptions)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, googleMap.cameraPosition.zoom))
            googleMap.uiSettings.setAllGesturesEnabled(true)

            runBlocking {
                callWeatherKeyword("$latitude", "$longitude", "minutely", "en")
            }
            Glide.with(applicationContext).load("http://openweathermap.org/img/w/${weather.value?.current?.weather?.get(0)?.icon}.png").into(binding.ivWeatherMap)
            binding.tvTempMap.text = String.format("%.1f", weatherSource.currentTemp.toDouble() - 273.15) + "℃"
            binding.tvWeatherMap.text = weatherSource.currentWeather
            binding.tvLocationMap.text = address
            binding.tvWindMap.text = weather.value?.current?.wind_speed.toString() + "m/s"
            binding.tvHumidityMap.text = weather.value?.current?.humidity.toString() + "%"
        }
    }


    suspend fun callWeatherKeyword(
        lat: String,
        lon: String,
        exclude: String,
        lang: String
    ) {
        weather.value = WeatherApi.getWeatherAddress(
            lat = lat,
            lon = lon,
            exclude = exclude,
            lang = lang,
            WeatherAPI.API_KEY
        )
        weatherSource = WeatherSource(
            weather.value?.timezone!!,
            weather.value?.current?.weather?.get(0)!!.main,
            weather.value?.current?.temp.toString(),
            weather.value?.hourly!!,
            weather.value?.daily!!
        )
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if(requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
//            var checkResult = false
//            for(result in grantResults) {
//                if(result == PackageManager.PERMISSION_GRANTED) {
//                    checkResult = true
//                    break
//                }
//            }
//            if(!checkResult) {
//                if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) ||
//                        ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
//                    Toast.makeText(this, "권한 설정 거부되었습니다.\n재실행이 필요합니다.", Toast.LENGTH_LONG).show()
//                    finish()
//                } else {
//                    Toast.makeText(this, "권한 설정이 거부되었습니다.\n설정에서 허용이 필요합니다.", Toast.LENGTH_LONG).show()
//                }
//            } else {
//                Toast.makeText(this, "권한 설정이 승인되었습니다.", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
}