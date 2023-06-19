package br.edu.scl.ifsp.ads.sharedlist.view

import android.content.Intent
import android.os.*
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.scl.ifsp.ads.sharedlist.R
import br.edu.scl.ifsp.ads.sharedlist.adapter.OnTaskClickListener
import br.edu.scl.ifsp.ads.sharedlist.adapter.TaskRvAdapter
import br.edu.scl.ifsp.ads.sharedlist.controller.TaskController
import br.edu.scl.ifsp.ads.sharedlist.databinding.ActivityMainBinding
import br.edu.scl.ifsp.ads.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()

    private val taskAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(taskList, this)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    private val taskController: TaskController by lazy{
        TaskController(this)
    }

    lateinit var updateViewsHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        supportActionBar?.subtitle = getString(R.string.tasks_list)

        taskController.getTasks()

        amb.taskRv.layoutManager =
            LinearLayoutManager(this)
        amb.taskRv.adapter = taskAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                } else {
                    result.data?.getParcelableExtra(EXTRA_TASK)
                }
                task?.let { _task ->

                    val position = taskList.indexOfFirst { it.id == _task.id }
                    if (position != -1) {
                        taskList[position] = _task
                        taskController.editTask(_task)
                        Toast.makeText(this, "Tarefa editada!", Toast.LENGTH_LONG).show()
                    } else {
                        taskController.insertTask(_task)
                        Toast.makeText(this, "Tarefa adicionada!", Toast.LENGTH_LONG).show()
                    }
                    taskController.getTasks()
                    taskAdapter.notifyDataSetChanged()

                }
            }
        }

        updateViewsHandler = Handler(Looper.myLooper()!!){ msg ->
            taskController.getTasks()
            true
        }
        updateViewsHandler.sendMessageDelayed(Message(), 3000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when(item.itemId){
            R.id.addTaskMi -> {
                carl.launch(Intent(this, TaskActivity::class.java))
                true
            }
            R.id.refreshTasksMi -> {
                taskController.getTasks()
                true
            }
            R.id.signOutMi -> {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                finish()
                true
            }
            else -> false
        }
    }

    fun updateTaskList(_taskList: MutableList<Task>){
        taskList.clear()
        taskList.addAll(_taskList)
        taskAdapter.notifyDataSetChanged()
    }

    override fun onTileTaskClick(position: Int) {
        val task = taskList[position]
        val taskIntent = Intent(this@MainActivity, TaskActivity::class.java)
        taskIntent.putExtra(EXTRA_TASK, task)
        taskIntent.putExtra(EXTRA_VIEW_TASK, true)
        startActivity(taskIntent)
    }

    override fun onEditMenuIconClick(position: Int) {
        val task = taskList[position]
        if(!task.isCompleted){
            val taskIntent= Intent(this, TaskActivity::class.java)
            taskIntent.putExtra(EXTRA_TASK, task)
            carl.launch(taskIntent)
        }else{
            Toast.makeText(this, "Tarefa não pode ser editada pois já foi completada!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRemoveMenuItemClick(position: Int) {
        val task = taskList[position]
        if(!task.isCompleted){
            taskList.removeAt(position)
            taskController.removeTask(task)
            taskAdapter.notifyDataSetChanged()
            Toast.makeText(this, "Tarefa removida!", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Tarefa não pode ser removida pois já foi completada!", Toast.LENGTH_LONG).show()
        }

    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null){
            Toast.makeText(this, "Não há usuário autenticado!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}