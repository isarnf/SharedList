package br.edu.scl.ifsp.ads.sharedlist.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.scl.ifsp.ads.sharedlist.databinding.TileTaskBinding
import br.edu.scl.ifsp.ads.sharedlist.model.Task

class TaskRvAdapter(
    private val taskList: MutableList<Task>, // data service
    private val onTaskClickListener: OnTaskClickListener //objeto que implementa as funções de clique
) : RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(tileTaskBinding: TileTaskBinding) :
        RecyclerView.ViewHolder(tileTaskBinding.root), View.OnCreateContextMenuListener {
        val titleTv: TextView = tileTaskBinding.titleTv
        val creationDateTv: TextView = tileTaskBinding.creationDateTv
        val dueDateTv: TextView = tileTaskBinding.dueDateTv
        var taskPosition = -1 // Para saber qual célula foi clicada

        init { // serve para dizer que o criador do menu de contexto da minha célula é o próprio view holder
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

    // Chamada pelo LayoutManager para buscar a quantidade de dados e preparar a quantidade de células necessárias
    // Retirna a quantidade de dados que tenho no data service
    // Chamada inicialmente quando a tela estiver vazia e quando modificamos a quantidade de itens no data service
    override fun getItemCount(): Int = taskList.size

    // Chamada pelo LayoutManager para criar uma nova célula (E consequentemente um novo ViewHolder)
    // Retorna o ViewHolder e não a view
    // Se a célula já existe, essa função não é chamada (neste caso quem é chamada é a onBindViewHolder)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Criar a célula;
        val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))

        // Criar um ViewHolder usando a célula
        val taskViewHolder = TaskViewHolder(tileTaskBinding)

        // Retorna o ViewHolder
        return taskViewHolder
    }

    // Chamada pelo LayoutManager para alterar os valores de uma célula
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        //Busca o contato pela posição no data service
        val task = taskList[position]

        // Altera os valores da célula usando o ViewHolder
        holder.titleTv.text = task.title
        holder.creationDateTv.text = task.creationDate
        holder.dueDateTv.text = task.dueDate
        holder.taskPosition = position

        // Tratamento da visualização das informações de cada célula ao clicar sobre a célula
        holder.itemView.setOnClickListener{
            onTaskClickListener.onTileTaskClick(position)
        }
    }

}












