package com.commonsware.todo2.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.repo.ToDoRepository


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

    fun save(model: ToDoModel) = repo.save(model)
    fun delete(model: ToDoModel) = repo.delete(model)

}



