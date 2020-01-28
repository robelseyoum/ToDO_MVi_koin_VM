package com.commonsware.todo2.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldEqual
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ToDoRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var underTest : ToDoRepository

    @Before
    fun setUp(){
        underTest = ToDoRepository()
    }


    @Test
    fun `can add items`(){
        val testModel = ToDoModel("test model")

        underTest.apply {

            items.test().value().shouldBeEmpty()

            runBlocking { save(testModel) }

            items.test().value()  shouldContainSame listOf(testModel)
            find(testModel.id).test().value() shouldEqual  testModel
        }
    }

}
