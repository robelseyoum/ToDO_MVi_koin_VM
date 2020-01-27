package com.commonsware.todo2.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.repo.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SingleModelViewState(
    val item: ToDoModel? = null
)

/*
This has the same basic structure as does RosterMotor and RosterViewState. The
difference in the view-state is that our state is just a single ToDoModel… or possibly
null, if we cannot find a model matching the requested ID.
Our SingleModelMotor class takes two constructor parameters: our ToDoRepository
and the ID of the model that we want. We use both of those to set up the states
property, using find() on the ToDoRepository to get a LiveData for our desired item
and using that to construct the SingleModelViewState.
 */
class SingleModelMotor(private val repo: ToDoRepository, modelId: String?) : ViewModel() {

    val states: LiveData<SingleModelViewState> =
    Transformations.map(repo.find(modelId)) { SingleModelViewState(it) }

    /*fun save(model: ToDoModel) = repo.save(model)
    fun delete(model: ToDoModel) = repo.delete(model)*/

    //Coroutine suspend utilised here both for save and delete methods belows

    /*
    To consume a suspend function from a normal function, you can use launch on a
    CoroutineScope. In effect, launch says “I am willing to deal with suspend functions,
    allowing my work to be suspended as needed to wait for the suspend work to
    complete”.
     */

    /*
    A CoroutineScope is a container, of sorts, for these sorts of coroutine-based bits of
    work. viewModelScope is an extension function supplied by lifecycle-viewmodelktx,
    to give us a CoroutineScope associated with our ViewModel.
     */

    /*
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

    fun delete(model: ToDoModel) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.delete(model)
        }
    }




}



