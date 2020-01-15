package com.commonsware.todo2.ui.roster

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.commonsware.todo2.R
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.repo.ToDoRepository

import kotlinx.android.synthetic.main.todo_roster.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class RosterListFragment :Fragment() {

    /*
    inject() is available for activities, fragments, and some other Android classes, and
    other versions of injection are available for other types of objects. Here, inject()
    sees that we are looking for a ToDoRepository, and it asks Koin to supply one. The
    by inject() syntax indicates that what inject() creating is a “delegate” for our
    Kotlin property. When we go to access this repo property, in reality, the delegate will
    handle that work for us
     */
    //private val repo: ToDoRepository by inject()
    private val motor: RosterMotor by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_roster, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    /*
    The job of onCreateView() of a fragment is to set up the UI for that fragment. In
    MainActivity, right now, we are doing that by calling
    setContentView(R.layout.todo_roster). We want to use that layout file here
    instead. To do that, modify onCreateView() to look like:
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.todo_roster, container, false) //false part meaning:- Do not put those widgets in that container right now, as the fragment system
    //will handle that for us at an appropriate time


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        Here, we create the updated model by using copy(), a function added to all Kotlin
        data classes. As the name suggests, copy() makes a copy of the immutable object,
        except it replaces whatever properties we include as parameters to the copy() call.
        ---- In our case, we replace isCompleted with the opposite of its current value. ----
         */

        val adapter =
            RosterAdapter(
                inflater = layoutInflater,
                onCheckboxToggle = { model ->
                    motor.save(model.copy(isCompleted = !model.isCompleted))
                },
                onRowClick = { model -> display(model) })

        items_rv.apply {

            setAdapter(adapter)

            layoutManager = LinearLayoutManager(context)

            /*
             Add divider lines between the rows by creating a DividerItemDecoration
             and adding it as a decoration to the RecyclerView
             */

            addItemDecoration(
                DividerItemDecoration(
                    activity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        motor.getItems().observe(this, Observer { items ->
            adapter.submitList(items)
            empty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        })

    }

    private fun display(model: ToDoModel) {
        findNavController().navigate(
            RosterListFragmentDirections.displayModel(
                model.id
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> { add(); return true; }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun add() {
        findNavController().navigate(RosterListFragmentDirections.createModel())
    }

}
