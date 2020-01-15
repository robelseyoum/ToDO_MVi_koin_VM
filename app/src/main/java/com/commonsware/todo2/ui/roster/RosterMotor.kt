package com.commonsware.todo2.ui.roster

import androidx.lifecycle.ViewModel
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.repo.ToDoRepository

class RosterMotor(private var repo: ToDoRepository) : ViewModel() {


    /*
    Our RosterMotor uses the items property to get at the list of to-do items. That used
    to be a simple list of
     */
    //Each call to getItems() would get the now-current list from the repository
    fun getItems() = repo.items

    fun save(model: ToDoModel)  = repo.save(model)

}
