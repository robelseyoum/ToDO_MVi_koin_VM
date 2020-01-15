package com.commonsware.todo2.repo

import java.util.*

data class ToDoModel (val description: String,
                      val id: String = UUID.randomUUID().toString(),
                      val isCompleted: Boolean = false,
                      val notes: String = "",
                      val createdOn: Calendar = Calendar.getInstance())
