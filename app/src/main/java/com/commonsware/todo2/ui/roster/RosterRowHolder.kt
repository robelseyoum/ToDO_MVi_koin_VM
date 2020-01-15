package com.commonsware.todo2.ui.roster

import androidx.recyclerview.widget.RecyclerView
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.databinding.TodoRowBinding

class RosterRowHolder(private val binding: TodoRowBinding,
                      val onCheckboxToggle: (ToDoModel) -> Unit,
                      val onRowClick: (ToDoModel) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: ToDoModel) {

        binding.model = model
        binding.holder = this
        binding.executePendingBindings()
    }

}