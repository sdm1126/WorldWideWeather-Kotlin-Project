package kr.or.mrhi.worldwideweather.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.worldwideweather.Data.Hourly
import kr.or.mrhi.worldwideweather.databinding.RvWeatherItemBinding
import java.text.SimpleDateFormat
import java.util.*

class WeatherRecyclerAdapter(val context: Context, weatherList: MutableList<Hourly>):
        RecyclerView.Adapter<WeatherRecyclerAdapter.CustomViewHolder>(){

    private var mContext: Context
    private var mWeatherList: MutableList<Hourly>

    init {
        this.mContext = context
        this.mWeatherList = weatherList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = RvWeatherItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)  {
        val url = "http://openweathermap.org/img/w/${mWeatherList.get(position).weather.get(0).icon}.png"
        val sdf = SimpleDateFormat("HH시")
        val date = Date(mWeatherList.get(position).dt.toLong()*1000)
        val dateF = sdf.format(date)
        with(holder){
            binding.tvRvItemTemp.text = String.format("%.1f",mWeatherList.get(position).temp - 273.15) + "℃"
            binding.tvRvItemTime.text = dateF
            Glide.with(mContext).load(url).into(binding.ivRvItemIcon)
        }
    }

    override fun getItemCount(): Int {
        return mWeatherList.size
    }

    inner class CustomViewHolder(val binding: RvWeatherItemBinding) : RecyclerView.ViewHolder(binding.root)
}