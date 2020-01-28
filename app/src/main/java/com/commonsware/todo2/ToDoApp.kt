package com.commonsware.todo2

import android.app.Application
import com.commonsware.todo2.repo.ToDoRepository
import com.commonsware.todo2.ui.SingleModelMotor
import com.commonsware.todo2.ui.roster.RosterMotor
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


/**
In Android, the typical place to configure something like Koin is in a custom
Application subclass. The Android framework creates a singleton instance of
Application — or of a custom subclass — when your process starts. That
Application object will be around for the life of the process. And, it has an
onCreate() method where we can initialize libraries like Koin.
 */

/**
When we start our app and Android forks a process for us, the framework will create
a ToDoApp instance for our process and call onCreate(). That allows us to set up
Koin before any of the rest of our code might need it.
 */
class ToDoApp : Application() {


    /**
    module() is part of a Koin domain-specific language (DSL) that describes the roster
    of objects to be available via dependency inversion. An app can have one or several
    Koin modules — for our purposes, one will be enough.
     */

    /**
    In that module, single() defines an object that will be available as a Koin-managed
    singleton. In our case, it is an instance of our ToDoRepository. The nice thing about
    Koin — and about dependency inversion frameworks in general — is that a
    singleton like this can be replaced where needed, such as for testing.
     */
    /**
    single() is a Koin DSL function that says “make a singleton instance of this object
    available to those needing it”. viewModel() is a Koin DSL function that says “use the
    AndroidX ViewModel system to make this ViewModel available to those activities and
    fragments that need it”.
     */
    private val koinModule = module {

        single { ToDoRepository() }

        /**
        In our case, we are saying that we are willing to supply instances of RosterMotor to
        interested activities and fragments. To satisfy the RosterMotor constructor, we use
        get() to retrieve a ToDoRepository from Koin itself. When it comes time to create
        an instance of RosterMotor, Koin will get the ToDoRepository singleton and supply
        it to the RosterMotor constructor.
        */
        viewModel { RosterMotor(get()) }

        /**
        This time, not only do we need our ToDoRepository but also the modelId. While we
        can get the ToDoRepository from Koin itself, we need to get the modelId from
        whoever wants to use a SingleModelMotor. The way that we do that in Koin is to add
        a parameter list to the lambda expression that creates our ViewModel and include
        modelId in that parameter list. In the next step, we will see how we pass in that value
        when injecting our motor.
         */
        viewModel { (modelId: String) -> SingleModelMotor(get(), modelId) }
    }


    /**
    Simply having a Koin module is insufficient — we need to tell Koin about it. To that
    end, add this onCreate()
     */
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger() // telling Koin that if it has any messages to log, use Logcat
            modules(koinModule) //where we can provide one or more modules that we want Koin to
                                //support (in our case, just the one we declared as koinModule)
        }
    }




}