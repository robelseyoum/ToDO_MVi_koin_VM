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

    /**
    In JUnit, a Rule is a standard way to package reusable bits of test logic, particularly
    related to common test configuration. The androidx.arch.core:core-testing
    library that we added earlier gives us a Rule called InstantTaskExecutorRule. This
    rule configures the threading behavior of the Jetpack components, such that things
    like LiveData and MutableLiveData do all their work on the current thread, rather
    than special threads like Android’s main application thread. In JUnit, all tests run on
    a test thread — and in a unit test, such as this, since we are not running on Android,
    there is no “Android’s main application thread”. InstantTaskExecutorRule therefore
    lets us test LiveData with unit tests, by having it no longer depend upon Android’s
    main application thread.
    so @get:Rule allows that annotation processor to work with a Kotlin
    InstantTaskExecutorRule says “do threading differently for LiveData in this test”,
     */
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

    @Test
    fun `can modify items`(){

        val testModel = ToDoModel("test model")
        val replacement = testModel.copy(notes = "This is the replacement")

        underTest.apply {

            items.test().value().shouldBeEmpty()

            runBlocking { save(testModel) }

            items.test().value() shouldContainSame listOf(testModel)

            runBlocking { save(replacement) }
        }
    }

    @Test
    fun `can remove items`() {
        val testModel = ToDoModel("test model")

        underTest.apply {

            items.test().value().shouldBeEmpty()

            runBlocking { save(testModel) }

            items.test().value() shouldContainSame  listOf(testModel)

            runBlocking { delete(testModel) }

            items.test().value().shouldBeEmpty()
        }
    }

}