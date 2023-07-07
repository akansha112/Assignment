package com.app.test.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.test.R
import com.app.test.adapter.FilterAdapter
import com.app.test.adapter.LinkAdapter
import com.app.test.databinding.FragmentLinkBinding
import com.app.test.graphData.MyAxisValueFormatter
import com.app.test.model.FilterClass
import com.app.test.model.LinkResponse
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class LinkFragment : Fragment() {
    private lateinit var binding : FragmentLinkBinding
    private var linkResponse : LinkResponse?=null
    private lateinit var context : Context
    val keyList = ArrayList<String>()
    val clickList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_link, container, false))!!
        binding.click = ClickAction()
        context = requireActivity()
        getAPIData()
        return binding.root
    }

    private fun getAPIData() {
        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, "https://api.inopenapp.com/api/v1/dashboardNew",null,
            Response.Listener { response ->
                binding.linkRecycler.isVisible = true
                linkResponse = Gson().fromJson(response.toString(), LinkResponse::class.java)
                setLinkAdapter("1")
                setFilterAdapter()
                setGraphData()
            },
            Response.ErrorListener { error: VolleyError? ->
                Log.i("TAG", "onErrorResponse: " + Gson().toJson(error))
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val map: MutableMap<String, String> = HashMap()
                map["Content-Type"] = "application/json"
                map["Authorization"]= "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
                return map
            }
        }

        queue.add(jsonObjectRequest)
    }

    private fun setGraphData() {
        val jsonInString = Gson().toJson(linkResponse!!.data.overall_url_chart)
        val mJSONObject = JSONObject(jsonInString)
        val x: Iterator<*> = mJSONObject.keys()
        val jsonArray = JSONArray()

        //for getting value
        while (x.hasNext()) {
            val key = x.next() as String
            jsonArray.put(mJSONObject[key])
        }

        for (i in 0 until jsonArray.length()) {
            clickList.add(jsonArray.get(i).toString())
        }


    //for getting date as date
        val iter = mJSONObject.keys() //This should be the iterator you want.
        while (iter.hasNext()) {
            val key = iter.next()
            keyList.add(getDateMonthFromDate(key))
        }

        chart(keyList, clickList)

        binding.graphRangeButton.text = keyList.get(0)+"-"+keyList.get(keyList.size-1)
    }


    private fun getDateMonthFromDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("dd MMM")
        var dateTimeStamp: Date? = null
        try {
            dateTimeStamp = inputFormat.parse(date)
            inputFormat.timeZone = TimeZone.getDefault()
        } catch (ex: ParseException) {
            ex.printStackTrace()
            return ""
        }
        return outputFormat.format(dateTimeStamp)
    }


    private fun chart(dateList: List<String>,listPrice: ArrayList<String>) {
        val scatterEntries: ArrayList<Entry> = ArrayList<Entry>()
        for (i in listPrice.indices) {
            scatterEntries.add(
                BarEntry(
                    i.toFloat(),
                    String.format("%.0f", listPrice[i].toDouble()).toFloat()
                )
            )
        }
        val lineDataSet = LineDataSet(scatterEntries, "Time per Question")
        val lineData = LineData(lineDataSet)
        binding.lineChart.data = lineData
        lineDataSet.setColors(ContextCompat.getColor(context, R.color.purple_200)) //color
        val drawable = ContextCompat.getDrawable(context, R.drawable.linechart_gradiet)
        lineDataSet.fillDrawable = drawable
        lineData.setDrawValues(false)
        lineDataSet.setDrawFilled(true)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        val yAxisL = binding.lineChart.axisLeft
        val yAxisR = binding.lineChart.axisRight
        yAxisL.setDrawAxisLine(false)
        yAxisR.setDrawLabels(false)
        val custom: IAxisValueFormatter = MyAxisValueFormatter()
        yAxisL.valueFormatter = custom as MyAxisValueFormatter
        yAxisL.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        val xAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(dateList)
        binding.lineChart.setDrawBorders(true)
        binding.lineChart.setBorderColor(context.getColor(R.color.purple_200))
        binding.lineChart.setTouchEnabled(true)
        binding.lineChart.setPinchZoom(true)
        val ds = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(ds)
        val width = ds.widthPixels
        val height = ds.heightPixels
        binding.lineChart.description.setPosition(width.toFloat(), height.toFloat())
        val desc = Description()
        desc.text = "Time per Question"
        desc.textSize = 30f
        desc.setPosition((width - 50).toFloat(), (height - 175).toFloat())
        binding.lineChart.description = desc
        binding.lineChart.description.isEnabled = true
        binding.lineChart.isHighlightPerTapEnabled = false
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.invalidate()
    }

    private fun setLinkAdapter(type: String) {
        if (type == "1")
            binding.linkRecycler.adapter = LinkAdapter(context,linkResponse!!.data.top_links)
        else
            binding.linkRecycler.adapter = LinkAdapter(context,linkResponse!!.data.recent_links)
    }

    private fun setFilterAdapter() {
        val filterList = ArrayList<FilterClass>()
        filterList.add(FilterClass(R.drawable.today_click,linkResponse!!.today_clicks.toString(),"Today’s clicks"))
        filterList.add(FilterClass(R.drawable.location,linkResponse!!.top_location,"Top Location"))
        filterList.add(FilterClass(R.drawable.instagram, linkResponse!!.top_source,"Top Source"))
        filterList.add(FilterClass(R.drawable.today_click, linkResponse!!.startTime,"Best Time"))
        binding.filterRecycler.adapter = FilterAdapter(context,filterList)
    }

    inner class ClickAction{
        fun onTopLink(view : View){
            binding.topLinks.setBackgroundResource(R.drawable.round_blue)
            binding.recentLink.setBackgroundResource(0)
            binding.topLinks.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.recentLink.setTextColor(ContextCompat.getColor(context,
                R.color.greyTextColor
            ))
            setLinkAdapter("1")
        }

        fun onRecentLink(view : View){
            binding.recentLink.setBackgroundResource(R.drawable.round_blue)
            binding.topLinks.setBackgroundResource(0)
            binding.recentLink.setTextColor(ContextCompat.getColor(context, R.color.white))
            binding.topLinks.setTextColor(ContextCompat.getColor(context,
                R.color.greyTextColor
            ))
            setLinkAdapter("2")
        }
    }
}