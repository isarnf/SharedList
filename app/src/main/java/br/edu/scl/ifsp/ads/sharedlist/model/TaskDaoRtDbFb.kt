package br.edu.scl.ifsp.ads.sharedlist.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TaskDaoRtDbFb : TaskDao {
    private val TASK_LIST_ROOT_NODE = "taskList"
    private val taskRtDbFbReference = Firebase.database.getReference(TASK_LIST_ROOT_NODE)

    private val taskList:MutableList<Task> = mutableListOf()
    init{
        taskRtDbFbReference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()
                task?.let{_task ->
                    if(!taskList.any{_task.title == it.title}){
                        taskList.add(_task)
                    }
                }

            }


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()
                task?.let{_task ->
                    taskList[taskList.indexOfFirst { _task.title == it.title }] = _task
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val contact: Task? = snapshot.getValue<Task>()
                contact?.let{_contact ->
                    taskList.remove(_contact)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }
        })
        taskRtDbFbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val  contactHashMap = snapshot.getValue<HashMap<String, Task>>()
                taskList.clear()
                contactHashMap?.values?.forEach {
                    taskList.add(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Não se aplica
            }

        })
    }

    override fun createTask(task: Task) {
        createOrUpdateTask(task)
    }

    override fun retrieveTask(id: Int): Task? {
        return taskList[taskList.indexOfFirst{id == it.id}]
    }

    override fun retrieveTasks(): MutableList<Task> {
        return taskList
    }

    override fun updateTask(task: Task): Int {
        createOrUpdateTask(task)
        return 1
    }

    override fun deleteTask(task: Task): Int {
        taskRtDbFbReference.child(task.title).removeValue()
        return 1
    }

    private fun createOrUpdateTask(task:Task) = taskRtDbFbReference.child(task.title).setValue(task)
}