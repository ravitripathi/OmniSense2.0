package xyz.ravitripathi.omnisense

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * Created by Ravi on 30-12-2017.
 */

class recycleAdapter(usbList: ArrayList<activeUSB>) : RecyclerView.Adapter<recycleAdapter.arcVH>() {

    var usbList: ArrayList<activeUSB>

    init {
        this.usbList = usbList
    }

    inner class arcVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bindStuff(item: activeUSB) {
            itemView.device.text = item.device
            itemView.devId.text = item.id
            itemView.devTag.text = item.tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): arcVH {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return arcVH(itemView)
    }

    override fun onBindViewHolder(holder: arcVH, position: Int) {
        val usbDevice = usbList.get(position)
        holder.bindStuff(usbDevice)
    }

    override fun getItemCount(): Int {
        return usbList.size
    }
}
