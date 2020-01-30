package com.commonsware.todo2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description


//https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471

/**
 * MainDispatcherRule says “do threading differently for coroutines in this test”
 */
class MainDispatcherRule(paused: Boolean) : TestWatcher() {

    val dispatcher = TestCoroutineDispatcher().apply { if (paused) pauseDispatcher() }

/**
    This is a JUnit rule implementation. In the previous tutorial, we applied an alreadyexisting rule (InstantTaskExecutorRule).
    Here, we define our own custom rule. It fills the same sort of role as does InstantTaskExecutorRule — just as
    InstantTaskExecutorRule says “do threading differently for LiveData in this test”,
    MainDispatcherRule says “do threading differently for coroutines in this test”.
    Specifically, we create a TestCoroutineDispatcher and use that for
    Dispatchers.Main. starting() is called on our rule when a test is starting, and
    there we call Dispatches.setMain() to provide a dispatcher to use for
    Dispatchers.Main. finished() is called on our rule when a test is ending, and there
    we call Dispatchers.resetMain() to reset the Dispatchers.Main definition to its
    default. We also call cleanupTestCoroutines() on our TestCoroutineDispatcher,
    to indicate that we are done with this dispatcher and anything still outstanding
    should be canceled.
 **/

    override fun starting(description: Description?) {
        super.starting(description)

        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)

        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

}