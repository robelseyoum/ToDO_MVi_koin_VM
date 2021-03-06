package com.commonsware.todo2.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.commonsware.todo2.databinding.TodoEditBinding
import java.util.concurrent.ThreadLocalRandom.current


/**
Right now, ToDoRepository is a Kotlin object, meaning that we are stuck with this
one implementation. To make effective use of Koin for testing, we are going to want
to replace ToDoRepository with a test (“mock”) implementation for some tests, and
for that, we need ToDoRepository to be a class
 */
class ToDoRepository {

    /**
    var items = listOf(

        ToDoModel(
            description = "Buy a copy of _Exploring Android_",
            isCompleted = true,
            notes = "See https://wares.commonsware.com"
        ),

        ToDoModel(
            description = "Complete all of the tutorials"
        ),
        ToDoModel(

            description = "Write an app for somebody in my community",
            notes = "Talk to some people at non-profit organizations to see what they need!"
        )
    )*/

    //var items = listOf<ToDoModel>()


    /**
     Making Our Items Live
     */
    /**
    Our RosterMotor uses the items property to get at the list of to-do items. That used
    to be a simple list of ToDoModel objects, but now it is a LiveData of the list of
    ToDoModel objects.
     */
    /**
    However, the real object is _items. This is a MutableLiveData, which is a subclass of
    LiveData. MutableLiveData is designed for classes, like ToDoRepository, that are
    creating new LiveData objects. Many implementations of LiveData are really
    MutableLiveData objects. However, we do not need consumers of this _items object
    to have all of the capabilities of MutableLiveData. So, the typical pattern is to keep
    the MutableLiveData as a private object to its maintainer, and expose the object as
    a LiveData type. In Kotlin, we can do that by having the items property be typed as
    LiveData but point to the underlying MutableLiveData object.
         */
    /**
    • Initialize _items right away, by setting the value to an empty list via
    listOf(). This way, we know for certain that _items always has a value.
    • Use the !! operator inside a current() function to give us the current value
    without the nullable type
     */
    private val _items = MutableLiveData<List<ToDoModel>>(listOf())
    /**
     When you set value, any registered observers will find out about the changes. Previously,
     items was a var, and we would replace the items with a new list that reflects any changes
     (e.g., newly added items in save()). Now, items is a val, and we set value on _items to reflect changes.
     */
    val items: LiveData<List<ToDoModel>> = _items


    /**
    Here, we see if the items list already contains the supplied model, based on the id
    values. If it does, this means we are replacing an existing ToDoModel with an updated
    copy, so we generate a new list of models, replacing the old one with the new one via
    map(). If, however, the list does not contain a model with this id, then we must be
    adding some new model to the list, so we just create a list that adds the new model
    to the end.
     */
    suspend fun save(model: ToDoModel) {

        _items.value = if(current().any {  it.id == model.id }) {

            current().map { if(it.id == model.id) model else it }


        } else {
            current() + model
        }
    }

    //This just iterates over the models to find the one with the ID.
    //fun find(modelID: String?) = current().find { it.id == modelID }

    /**
    items is a LiveData of our list of to-do items. Our mapping lambda expression uses
    find() to find the first model whose ID matches the one passed into our own
    find() function. This means that our find() function returns a LiveData of
    ToDoModel?, as there are two scenarios in which our mapping lambda expression will
    not find a matching ToDoModel:
    • The modelId might be null, such as when we start EditFragment to create a
    new to-do item
    • The modelId might not reflect a currently-available item (e.g., it was deleted)
     */
    /**
     * Using a Transformations.map(), this time to implement a filtering operation, to return a
     * LiveData of a single ToDoModel.
     */
    fun find(modelID: String?) : LiveData<ToDoModel?> =
        Transformations.map(items) {
            it.find { model -> model.id == modelID }
        }


    //Keeping any item that has an ID different than the one that we are trying to remove.
    suspend fun delete(model: ToDoModel){
        _items.value = current().filter { it.id != model.id }
    }


    private fun current() = _items.value!!


}