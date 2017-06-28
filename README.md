# MVX
Separation of concerns in Android. The library is still a work in progress.

## What is MVX
MVX achieves separation of concerns by enabling building on the following architecture.

![MVX architecture](/mvx_info.png)

The arrows show what entity has access to what other entity, the one that points has access.

There's no state maintained in the Presenter layer, which takes in inputs, processes them, updates the PresenterModel if required, and updates the View if required. So the PresenterModel and the View are passive components and all business logic resides in the Presenter layer.

## How the library is implemented
The above architecture has good similarity with the following Android architecture, if we restrict the access to the Binder from the Service. (Like in other MVx frameworks, Model is the least of the concerns)

![MVX in Android](/mvx_vs_android.png)

We take advantage of this similarity which gives us a few advantages, listed in some of the key features below.

## Key Features
- *Testability* - Presenter is out of the scope of Android lifecycles here and so go ahead and easily unit test your business logic!
- *Stateful PresenterModels*. **You don't need to worry about saving instance state and restoring it anymore.** All state related data is maintained in PresenterModels which are Services, so have their own lifecycles, separate from that of the Views. However, as mentioned in another point below, the library is meant to be least enforcing - if you don't even need to save the PresenterModel state, you don't need to start the PresenterModel Services.
- *Highly Flexible*. You can have multiple Views within the same Activity or Fragment or your Fragment or Activity or CustomView can itself be the View.
- *Loosely coupled*. Changing the View or Presenter or the PresenterModel in a particular coupling simply means a quick change in the definition. You'll simply be calling ```getPresenter()``` within the View or ```getView()``` or ```getPresenterModel()``` within the Presenter.
- *Unenforcing*. Your Activity/Fragment/View doesn't need to extend any View from the library. You can also have some logic sit within a PresenterModel or a View, its totally up to you. However, as you start using, you'll find Presenter being the obvious place to implement all logic since it can be easily unit tested. PresenterModel simply maintains the state that the View reflects.
- *LCE Pattern* - There's an implementation for the (Input)-Loader-Content-Error Pattern which can be used directly. Appreciate Mosby for introducing me to the same. Their in-built implementation, however, is enforcing - there have to views with particular IDs, what happens in the different states isn't in the View's control and there can't be multiple LCEs implemented in the same Activity/Fragment/CustomView. We try to makes things better in this implementation.

## [Getting Started](/GettingStarted.md)

## Credits
Again, I thank the creators of Mosby, particularly Hannes Dorfmann and his articles on the relevant topics where I realized the PresenterModel being a layer of it's own (called PresentationModel there) and also the LCE pattern.
