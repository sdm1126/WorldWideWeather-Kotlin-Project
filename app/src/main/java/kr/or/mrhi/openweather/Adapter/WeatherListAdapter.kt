package kr.or.mrhi.openweather.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import kr.or.mrhi.openweather.Data.Daily
import kr.or.mrhi.openweather.Data.Hourly
import kr.or.mrhi.openweather.databinding.LvWeatherItemBinding
import java.text.SimpleDateFormat
import java.util.*

class WeatherListAdapter(val context: Context, val arrayList: MutableList<Daily>) : BaseAdapter() {
    private lateinit var mContext: Context
    private lateinit var mArrayList: MutableList<Daily>

    init {
        this.mContext = context
        this.mArrayList = arrayList
    }

    override fun getCount(): Int = mArrayList.size

    override fun getItem(position: Int): Any = mArrayList.get(position)

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val binding = LvWeatherItemBinding.inflate(LayoutInflater.from(context),parent,false)
        val url = "http://openweathermap.org/img/w/${mArrayList.get(position).weather.get(0).icon}.png"
        val date = Date(mArrayList.get(position).dt.toLong()*1000)
        val cal = Calendar.getInstance()
        cal.time = date
        val dayNum = cal.get(Calendar.DAY_OF_WEEK)
        var day : String = ""
        when(dayNum){
            1 -> day = "일요일"
            2 -> day = "월요일"
            3 -> day = "화요일"
            4 -> day = "수요일"
            5 -> day = "목요일"
            6 -> day = "금요일"
            7 -> day = "토요일"
        }

        binding.textView.text = day
        binding.textView2.text = String.format("%.1f",mArrayList.get(position).temp.day - 273.15)
        Glide.with(mContext).load(url).into(binding.imageView)
        return binding.root
    }
}