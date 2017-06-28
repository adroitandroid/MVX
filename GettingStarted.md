# Getting Started
MVX has 2 variants - plain vanilla and LCE. LCE is the Load-Content-Error pattern where you show the loading state till data is fetched, if fetch is successful you show the content otherwise the error. The 3 components for the two variants are as listed below.

| Variant | View interface | abstract Presenter Model | abstract Presenter |
| ------- | -------------- | ------------------------ | ------------------ |
| Vanilla | XView          | XPresenterModel          | XPresenter         |
| LCE     | XLceView       | XLcePresenterModel       | XLcePresenter      |

We'll first look at the plain vanilla implementation and checkout LCE [at the end](/GettingStarted.md#lce-implementation).

We'll look at the usage in [the sample app](/app) where required, the guide is generic otherwise.

## Step 1 - Adding MVX to your project
Add jitpack.io to your root build.gradle
```Gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
Then add the dependency in your project build.gradle
```Gradle
dependencies {
    ...
    compile 'com.github.adroitandroid:MVX:v1.0'
    ...
}
```
You can find the latest version [here](https://github.com/adroitandroid/MVX/releases/latest/).

## Step 2 - Defining the view, presenter model and presenter
You'll define you view first, as the presenter model depends on the view and the presenter on both of them.
```java
public interface MainView extends XView {

}
```
Next, your presenter model:
```java
public class MainPresenterModel extends XPresenterModel<MainView> {
    public MainPresenterModel() {
        super("MainPresenterModel");
    }
}
```
It'll ask you to implement the `getPresenter()` method.
```java
public class MainPresenterModel extends XPresenterModel<MainView> {
    ...
    public MainPresenterModel() {
        super("MainPresenterModel");
    }

    @Override
    public <xPresenterModel extends XPresenterModel<MainView>> XPresenter<MainView, xPresenterModel> getPresenter() {
        return new MainPresenter();
    }
    ...
}
```
where since you know the presenter model type, you can refactor it to
```java
    @Override
    public XPresenter<MainView, MainPresenterModel> getPresenter() {
        return new MainPresenter();
    }
```
The IDE will ask you to make ```MainPresenter``` now, which for now you can leave as an empty class.
```java
public class MainPresenter extends XPresenter<MainView, MainPresenterModel> {

}
```
## Step 3 - Connecting the dots
Where your view implementation is initialised, we bind the presenter to it using the following call
```java
XPresenter.bind(View view,
                Class<PresenterModel> presenterModelServiceClass,
                OnBindListener<Presenter> bindListener);
```
The three arguments here are tightly coupled, so incorrect types cannot be bound by mistake.
Let's suppose we want to bind the MainView implementation above in the `onCreate()` of the MainActivity which holds the view. If the activity holds a single XView, it can implement it directly. Our `bind()` call then becomes
```java
XPresenter.bind((MainView) this, MainPresenterModel.class,
        new XPresenter.OnBindListener<MainPresenter>() {
            @Override
            public void onBind(MainPresenter presenter) {
                mPresenter = presenter;
                ...
                // do initializations with this presenter
            }
});
```
For any business logic, you call the methods of ```mPresenter```, the Presenter returned in ```onBind()```. For any updates to the view, the MainPresenter instance can call the MainView's methods and for any updates in state variables, the MainPresenter will call MainPresenterModel's methods. The following XPresenter methods allow this
```java
public abstract class XPresenter<vView extends XView, vPresenterModel extends XPresenterModel<vView>> {
    ...
    protected vView getView() {
        // returns the view the presenter is bound to
    }

    protected vPresenterModel getPresenterModel() {
        // returns the presenter model associated with this presenter
    }
    ...
}
```
Examples using this can be found [here](/app).

## Step 4 - Saving State
This might, as per your use case, be a requirement. Good part about MVX is that it allows to save the state without passing in bundles. That's because the PresenterModel we just saw, is actually an Android Service, and the Presenter an implementation of IBinder!
We just need to put a few Presenter method calls at the right lifecycle methods of the Activity or Fragment to enable state saving, namely the following.
```java
public class MainActivity extends AppCompatActivity implements MainView {
...
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.saveState();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.restoreState();
        }
    }

    @Override
    public void finish() {
        super.finish();
        mPresenter.disposeState();
    }
...
}
```
## Step 5 - Into the Manifest
As mentioned in Step 4, the PresenterModel is actually a Service implementation. So don't forget to add it to your ```AndroidManifest.xml```.
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="your.package.name">

    <application
        ...
        android:theme="@style/AppTheme">
        ...
        <service android:name=".model.MainPresenterModel" />
    </application>

</manifest>
```
## LCE implementation
The LCE implementation is similar to the vanilla implementation in all steps except Step 2.
The View now takes the type of the data that needs to be loaded in its definition as in the below example.
```java
        mCurrentLocationView = new XLceView<IpLocationData>() {

            @Override
            public void onContentReady(IpLocationData content) {
                // load data
            }

            @Override
            public void onError(String error) {
                // show error state
            }

            @Override
            public void onFetchStart() {
                // show loading state
            }

            @Override
            public Context getContext() {
                return CurrentWeatherActivity.this;
            }
        };
```
The Presenter and the PresenterModel that makes the Presenter in the ```getPresenter()``` implementation should also be aware of the type of data to be loaded. Taking from the LCE example [here](/app):
```java
public class CurrentLocationPresenterModel extends XLcePresenterModel<IpLocationData, XLceView<IpLocationData>> {
    public CurrentLocationPresenterModel() {
        super("CurrentLocationPresenterModel");
    }

    @Override
    public CurrentLocationPresenter getPresenter() {
        return new CurrentLocationPresenter();
    }
    ...
}
```
```java
public class CurrentLocationPresenter
        extends XLcePresenter<IpLocationData, XLceView<IpLocationData>, CurrentLocationPresenterModel> {
    ...
    @Override
    protected void onFetchComplete(IpLocationData data) {
        // do anything you need to in the presenter or presenter model once the fetch is complete here
    }

    @Override
    protected void onFetchError(String error) {
        // do anything you need to in the presenter or presenter model when the fetch fails here
    }

    @Override
    protected void onStartFetch() {
        // your implementation of the fetch that returns the required datatype
    }
    ...
}
```
The data fetch is invoked when the following method of the Presenter is called.
```java
presenter.startFetch();
```
To complete the fetch with data, you should call the Presenter method
```java
this.complete(data);
```
or to set error in fetch, call
```java
this.setError("This is a sample error message");
```
