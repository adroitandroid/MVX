# MVX
Separation of concerns in Android. The library is still a work in progress.

## What is MVX
MVX achieves separation of concerns by enabling building on the following architecture.

![MVX architecture](/mvx_info.png)

The arrows show what entity has access to what other entity, the one that points has access.

There's no state maintained in the Presenter layer, it has only logic, which takes in inputs, processes them, updates the PresenterModel if required, and updates the View if required.

## How the library is implemented
The above architecture has good similarity with the following Android architecture, if we restrict the access to the Binder from the Service. (Like in other MVx frameworks, Model is the least of the concerns)

![MVX in Android](/mvx_vs_android.png)

We take advantage of this similarity which gives us a few advantages, listed in the key features below.

## Key Features
- Stateful Views and PresenterModels. You don't need to worry about saving instance state and restoring anymore.
- Loosely coupled. You can have some logic sit within a PresenterModel or a View, its totally up to you. However, as you start using, you'll find Presenter being the obvious place to implement all logic since it can be easily unit tested. PresenterModel simply maintains the state that the View reflects.
- Highly Flexible. You can have multiple Views within the same Activity or Fragment or your Fragment or Activity or CustomView can itself be the View.
- The obvious feature is testability - Presenter is out of the scope of Android lifecycles here and so go ahead and easily unit test your business logic.

## TODO
* remove logs
* unit tests for sample presenters with retrofit using mockito and dagger
* expose presenter API to restore state, i.e., to stop saving state
