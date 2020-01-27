package com.commonsware.todo2.ui.roster

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.repo.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


///This just holds onto our list of to-do items, though over time we will add other
//properties to this class.
class RosterViewState(
    val items: List<ToDoModel> = listOf()
)


class RosterMotor(private var repo: ToDoRepository) : ViewModel() {

    /**
    Our RosterMotor uses the items property to get at the list of to-do items. That used
    to be a simple list of
     */
    //Each call to getItems() would get the now-current list from the repository
    //fun getItems() = repo.items

    /**
    Transformations.map() takes a LiveData and a lambda expression. That lambda
    expression will be invoked each time the LiveData gets a new value, and the job of
    the lambda expression is to convert the input value (list of to-do items) into the
    desired output value (RosterViewState). Transformations.map() returns a new
    LiveData object that emits the objects returned by the lambda expression. So, in our
    case, states is a LiveData of RosterViewState.
     */
    val states: LiveData<RosterViewState> = Transformations.map(repo.items) { RosterViewState(it) }

    //fun save(model: ToDoModel)  = repo.save(model)

    //Coroutine suspend utilised here both for save and delete methods belows

    /**
    To consume a suspend function from a normal function, you can use launch on a
    CoroutineScope. In effect, launch says “I am willing to deal with suspend functions,
    allowing my work to be suspended as needed to wait for the suspend work to
    complete”.
     */

    /**
    A CoroutineScope is a container, of sorts, for these sorts of coroutine-based bits of
    work. viewModelScope is an extension function supplied by lifecycle-viewmodelktx,
    to give us a CoroutineScope associated with our ViewModel.
     */

    /**
    We are passing two things to launch. The big thing is the lambda expression
    indicating what we want “launched”, which is our code that calls suspend functions.
    We also pass Dispatchers.Main, which says “until told otherwise, do this work on
    the Android main application thread”. Later, if we want to do something that affects
    our UI after we save or delete the model, we could just have those statements after
    our repo.save() and repo.delete() calls, and those statements would be executed
    on the main application thread.
     */
    fun save(model: ToDoModel) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.save(model)
        }
    }

}
