package com.commonsware.todo2.ui.edit

import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.commonsware.todo2.R
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.databinding.TodoEditBinding
import com.commonsware.todo2.repo.ToDoRepository
import com.commonsware.todo2.ui.SingleModelMotor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditFragment : Fragment() {

    /**
    inject() is available for activities, fragments, and some other Android classes, and
    other versions of injection are available for other types of objects. Here, inject()
    sees that we are looking for a ToDoRepository, and it asks Koin to supply one. The
    by inject() syntax indicates that what inject() creating is a “delegate” for our
    Kotlin property. When we go to access this repo property, in reality, the delegate will
    handle that work for us
     */
    //private val repo: ToDoRepository by inject()


    //As we did with DisplayFragment, replace the repo property with a motor property:
    private val motor :SingleModelMotor by viewModel { parametersOf(args.modelId)  }

    private lateinit var binding: TodoEditBinding
    private val args: EditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = TodoEditBinding.inflate(inflater, container, false)
        .apply { binding = this }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //binding.model = repo.find(args.modelId)
        motor.states.observe(viewLifecycleOwner, Observer { state -> binding.model = state.item })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_edit, menu)

        menu.findItem(R.id.delete).isVisible = args.modelId != null

        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.save -> { save(); return true }
            R.id.delete -> { delete(); return true; }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun save() {

        val edited = if (binding.model == null) {

            ToDoModel (
                description = binding.desc.text.toString(),
                isCompleted = binding.isCompleted.isChecked,
                notes = binding.notes.text.toString()
            )

        } else {

            binding.model?.copy (
                description = binding.desc.text.toString(),
                isCompleted = binding.isCompleted.isChecked,
                notes = binding.notes.text.toString()
            )

        }

        edited?.let { motor.save(it) }
        navToDisplay()

    }


    private fun navToDisplay(){
        hideKeyboard()
        findNavController().popBackStack()
    }

    private fun hideKeyboard() {

        view?.let {
            val imm = context?.getSystemService<InputMethodManager>()


            imm?.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

    }

    private fun delete(){
        binding.model?.let { motor.delete(it) }
        navToList()
    }

    private fun navToList() {
        hideKeyboard()
        findNavController().popBackStack(R.id.rosterListFragment, false)
    }
}