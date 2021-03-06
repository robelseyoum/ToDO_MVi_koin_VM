package com.commonsware.todo2.ui.display

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.commonsware.todo2.R
import com.commonsware.todo2.databinding.TodoDisplayBinding
import com.commonsware.todo2.repo.ToDoRepository
import com.commonsware.todo2.ui.SingleModelMotor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DisplayFragment : Fragment() {

    /**
    DisplayFragmentArgs is code-generated by the Safe Args plugin for the Navigation
    component. It looks at our declared arguments and creates a Kotlin class that
    represents them. Moreover, we get a navArgs() delegate that will build that
    DisplayFragmentArgs for us when we first access the args property. We will be able
    to use this to access our modelId value.
     */
    private val args: DisplayFragmentArgs by navArgs()
    private lateinit var binding: TodoDisplayBinding
    /**
    This is similar to how we injected the motor into RosterListFragment. The
    difference is the lambda expression, where we use a parametersOf() function to
    wrap up the modelId that we get from our args. These parameters wind up as
    parameters to the lambda expression that creates the SingleModelMotor, and that is
    how SingleModelMotor determines the ID of the item that we want.
     */
    private val motor : SingleModelMotor by viewModel { parametersOf(args.modelId) }

    /**
    inject() is available for activities, fragments, and some other Android classes, and
    other versions of injection are available for other types of objects. Here, inject()
    sees that we are looking for a ToDoRepository, and it asks Koin to supply one. The
    by inject() syntax indicates that what inject() creating is a “delegate” for our
    Kotlin property. When we go to access this repo property, in reality, the delegate will
    handle that work for us
     */
   // private val repo: ToDoRepository by inject()

    /**
    This works akin to how onCreateViewHolder() does in RosterAdapter, inflating the
    binding from the resource, using the code-generated TodoDisplayBinding class.
    Here, we assign the binding itself to the binding property, while returning the root
    View of the inflated layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = TodoDisplayBinding.inflate(inflater, container, false)
        .apply { binding = this}
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //This retrieves the model given its ID and binds it to the layout.s
        //binding.model = repo.find(args.modelId)

        /**
        As we did with RosterListFragment, this has us observe the states from our motor
        and pass our ToDoModel to the data binding framework, so our widgets can be
        populated.
         */
        motor.states.observe(viewLifecycleOwner, Observer { state -> binding.model = state.item })
        //super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_display, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
    Displaying the app bar item, we can get control and show the
    presently-empty EditFragment.
     */
    private fun edit() {
        findNavController().navigate(
            DisplayFragmentDirections.editModel(
                args.modelId
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.edit -> { edit(); return true; }
        }
        return super.onOptionsItemSelected(item)
    }
}