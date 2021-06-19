package kg.tutorialapp.weather.ui.rv

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.tutorialapp.weather.models.HourlyForeCast

class HourlyForeCastAdapter: RecyclerView.Adapter<HourlyForeCastVH>() {

    private val items = arrayListOf<HourlyForeCast>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForeCastVH {
        return HourlyForeCastVH.create(parent)
    }

    override fun onBindViewHolder(holder: HourlyForeCastVH, position: Int) {
        return holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    fun setItemsHourly(newItems: List<HourlyForeCast>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

}