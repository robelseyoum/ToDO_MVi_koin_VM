package com.commonsware.todo2.ui.roster

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.repo.ToDoRepository


///This just holds onto our list of to-do items, though over time we will add other
//properties to this class.
class RosterViewState(
    val items: List<ToDoModel> = listOf()
)




class RosterMotor(private var repo: ToDoRepository) : ViewModel() {

    /*
    Our RosterMotor uses the items property to get at the list of to-do items. That used
    to be a simple list of
     */
    //Each call to getItems() would get the now-current list from the repository
    //fun getItems() = repo.items

    /*
    Transformations.map() takes a LiveData and a lambda expression. That lambda
    expression will be invoked each time the LiveData gets a new value, and the job of
    the lambda expression is to convert the input value (list of to-do items) into the
    desired output value (RosterViewState). Transformations.map() returns a new
    LiveData object that emits the objects returned by the lambda expression. So, in our
    case, states is a LiveData of RosterViewState.
     */
    val states: LiveData<RosterViewState> = Transformations.map(repo.items) { RosterViewState(it) }

    fun save(model: ToDoModel)  = repo.save(model)

}
