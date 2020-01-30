package com.commonsware.todo2.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.commonsware.todo2.MainDispatcherRule
import com.commonsware.todo2.repo.ToDoModel
import com.commonsware.todo2.repo.ToDoRepository
import com.jraska.livedata.test

/**
 * Change this import org.amshove.kluent.mock to import com.nhaarman.mockitokotlin2.mock
 *
 * https://github.com/nhaarman/mockito-kotlin/issues/364
*/
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SingleModelMotorTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(paused = true)

    private val testModel = ToDoModel("this is a test")

    /**
    Another difference of note is the repo property. Here, we use a mock() extension
    function from Kluent to set up a mock implementation of ToDoRepository. This
    function, in turn, wraps Mockito’s mock engine. It generates an instance of a
    generated subclass of ToDoRepository, one where we can dictate how it behaves in
    our test code, rather than relying on the real ToDoRepository implementation.
     */
    private  val repo: ToDoRepository = mock()

    private lateinit var underTest: SingleModelMotor


    /**
    This When calling ... itReturns ... syntax comes from Kluent, as it tries to
    make it easy for us to describe how a mock should behave. Here, we are saying:
    • When something calls find() on the mock, and the passed-in parameter to
    find() is this particular ID value…
    • …then return a MutableLiveData wrapped around our testModel object
    Our SingleModelMotor then uses that mock ToDoRepository. When it calls find()
    and passes in our desired model ID, the mock will return the MutableLiveData that
    we declared in the When calling ... itReturns ... line.
     */
    @Before
    fun setUp(){

        When calling repo.find(testModel.id) itReturns MutableLiveData<ToDoModel>(testModel)

        underTest = SingleModelMotor(repo, testModel.id)
    }

    @Test
    fun `initial state`(){
        underTest.states.test().awaitValue().assertValue { it.item == testModel }
    }

    @Test
    fun `actions pass through to repo`(){

        val replacement = testModel.copy("whatevs")

        underTest.save(replacement)
        /**
        • Gets our TestCoroutineDispatcher from the MainDispatcherRule
        • Tells that dispatcher to run any coroutines that are set up for that dispatcher
        Our save() and delete() functions are setting up coroutines to run on
        Dispatchers.Main, and Dispatchers.Main is tied to our TestCoroutineDispatcher
        through our MainDispatcherRule. The effect is that when we call runCurrent(), we
        cause those coroutines to be executed, making their requests of our (mock)
        repository. runCurrent() shows up with warning highlights, as it too is
        experimental, as with much of the test coroutines API that we are calling in
        MainDispatcherRule.
         */
        mainDispatcherRule.dispatcher.runCurrent()
        runBlocking { Verify on repo that repo.save(replacement) was called }

        underTest.delete(replacement)
        mainDispatcherRule.dispatcher.runCurrent()
        runBlocking { Verify on repo that repo.delete(replacement) was called }
    }

}