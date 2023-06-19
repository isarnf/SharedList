package br.edu.scl.ifsp.ads.sharedlist.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.ads.sharedlist.databinding.TileTaskBinding
import br.edu.scl.ifsp.ads.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth

class TaskRvAdapter(
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: OnTaskClickListener
) : RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(tileTaskBinding: TileTaskBinding) :
        RecyclerView.ViewHolder(tileTaskBinding.root), View.OnCreateContextMenuListener {
        val titleTv: TextView = tileTaskBinding.titleTv
        val creationDateTv: TextView = tileTaskBinding.creationDateTv
        val dueDateTv: TextView = tileTaskBinding.dueDateTv
        val statusTv: TextView = tileTaskBinding.statusTv
        val completionUserTv: TextView = tileTaskBinding.completionUserTv
        var taskPosition = -1

        init {
            tileTaskBinding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, 0, 0, "Editar")?.setOnMenuItemClickListener {
                if (taskPosition != -1) {
                    onTaskClickListener.onEditMenuIconClick(taskPosition)

                }
                true
            }
            menu?.add(Menu.NONE, 1, 1, "Remover")?.setOnMenuItemClickListener {
                if (taskPosition != -1) {
                    onTaskClickListener.onRemoveMenuItemClick(taskPosition)

                }
                true
            }
        }
    }

    override fun getItemCount(): Int = taskList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))

        val taskViewHolder = TaskViewHolder(tileTaskBinding)

        return taskViewHolder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        val task = taskList[position]

        if(task.isCompleted) {
            holder.completionUserTv.visibility = View.VISIBLE
            holder.completionUserTv.text = "Completion user: " + task.completionUser
            holder.statusTv.text = "Status: completed"
        }
        else {
            holder.completionUserTv.visibility = View.GONE
            holder.statusTv.text = "Status: in progress..."
        }


        holder.completionUserTv.text = task.creationUser
        holder.titleTv.text = task.title
        holder.creationDateTv.text = "Created on: " + task.creationDate
        holder.dueDateTv.text = "Due date: " + task.dueDate
        holder.taskPosition = position
        holder.itemView.setOnClickListener{
            onTaskClickListener.onTileTaskClick(position)
        }
    }

}






