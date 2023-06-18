package br.edu.scl.ifsp.ads.sharedlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.sharedlist.R
import br.edu.scl.ifsp.ads.sharedlist.databinding.TileTaskBinding
import br.edu.scl.ifsp.ads.sharedlist.model.Task

class TaskAdapter(
    context: Context,
    private val taskList: MutableList<Task>
): ArrayAdapter<Task>(context, R.layout.tile_task, taskList){
    private lateinit var ttb: TileTaskBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact: Task = taskList[position]
        var tileTaskView = convertView
        if(tileTaskView == null){

            ttb = TileTaskBinding.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false

            )
            tileTaskView = ttb.root


            val ttvh: TileTaskViewHolder = TileTaskViewHolder(
                ttb.titleTv,
                ttb.creationDateTv,
                ttb.dueDateTv,


            )

            tileTaskView.tag = ttvh
        }

        with(tileTaskView.tag as TileTaskViewHolder){
            titleTv.text = contact.title
            creationDateTv.text = contact.creationDate
            dueDateTv.text = contact.dueDate
        }

        return tileTaskView
    }

    private data class TileTaskViewHolder(
        val titleTv: TextView,
        val creationDateTv: TextView,
        val dueDateTv: TextView
    )
}