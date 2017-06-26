package com.adroitandroid.weatherapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.adroitandroid.mvx.lce.XLceView;
import com.adroitandroid.weatherapp.model.CurrentWeatherPresenterModel;
import com.adroitandroid.weatherapp.model.Weather;
import com.adroitandroid.weatherapp.model.WeatherData;
import com.adroitandroid.weatherapp.network.WeatherApiClient;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by pv on 27/06/17.
 */

public class CurrentWeatherPresenterTest {

    @Mock
    private WeatherApiClient weatherApiClient;

    @BeforeClass
    public static void setUpRxSchedulers() {
        final Scheduler immediate = new Scheduler() {
            @Override
            public Scheduler.Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable runnable) {
                        runnable.run();
                    }
                });
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitComputationSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitNewThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxJavaPlugins.setInitSingleSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> scheduler) throws Exception {
                return immediate;
            }
        });
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFetchWithInvalidZipcode() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final StringBuilder errorBuilder = new StringBuilder();
        CurrentWeatherPresenterForTest presenterForTest = new CurrentWeatherPresenterForTest(
                new ViewForTest() {
                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        errorBuilder.append(error);
                        countDownLatch.countDown();
                    }
                });
        presenterForTest.setFetchParams("-12344", "US");
        presenterForTest.startFetch();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Assert.fail();
        }
        Assert.assertEquals(CurrentWeatherPresenter.ERROR_INVALID_ZIPCODE, errorBuilder.toString());
        Assert.assertEquals(CurrentWeatherPresenterModel.STATE_FETCH_FAILED, presenterForTest.getPresenterModel().getStatus());

        final CountDownLatch countDownLatch1 = new CountDownLatch(1);
        final StringBuilder errorBuilder1 = new StringBuilder();
        CurrentWeatherPresenterForTest presenterForTest1 = new CurrentWeatherPresenterForTest(
                new ViewForTest() {
                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        errorBuilder1.append(error);
                        countDownLatch1.countDown();
                    }
                });
        presenterForTest1.setFetchParams("123ABC", "US");
        presenterForTest1.startFetch();
        try {
            countDownLatch1.await();
        } catch (InterruptedException e) {
            Assert.fail();
        }
        Assert.assertEquals(CurrentWeatherPresenter.ERROR_INVALID_ZIPCODE, errorBuilder1.toString());
        Assert.assertEquals(CurrentWeatherPresenterModel.STATE_FETCH_FAILED, presenterForTest1.getPresenterModel().getStatus());
    }

    @Test
    public void testFetchWithInvalidCountryCode() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final StringBuilder errorBuilder = new StringBuilder();
        CurrentWeatherPresenterForTest presenterForTest = new CurrentWeatherPresenterForTest(
                new ViewForTest() {
                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        errorBuilder.append(error);
                        countDownLatch.countDown();
                    }
                });
        presenterForTest.setFetchParams("12345", "USA");
        presenterForTest.startFetch();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Assert.fail();
        }
        Assert.assertEquals(CurrentWeatherPresenter.ERROR_INVALID_COUNTRY, errorBuilder.toString());
        Assert.assertEquals(CurrentWeatherPresenterModel.STATE_FETCH_FAILED, presenterForTest.getPresenterModel().getStatus());
    }

    @Test
    public void testFetchFromApi() {
        String zipCodeAndCountry = "12345,IN";
        final String mainWeatherSummary = "Rainy";
        WeatherData weatherData = new WeatherData() {
            @Override
            public Weather getWeatherSummary() {
                return new Weather() {
                    @Override
                    public String getMain() {
                        return mainWeatherSummary;
                    }
                };
            }
        };
        Mockito.when(weatherApiClient.getWeatherData(zipCodeAndCountry)).thenReturn(Observable.just(weatherData));

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final StringBuilder summaryInViewBuilder = new StringBuilder();
        CurrentWeatherPresenterForTest presenterForTest = new CurrentWeatherPresenterForTest(
                new ViewForTest() {
                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onContentReady(WeatherData content) {
                        super.onContentReady(content);
                        summaryInViewBuilder.append(content.getWeatherSummary().getMain());
                        countDownLatch.countDown();
                    }
                });
        presenterForTest.setFetchParams("12345", "India");
        presenterForTest.startFetch();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Assert.fail();
        }
        Assert.assertEquals(mainWeatherSummary, summaryInViewBuilder.toString());
        Assert.assertEquals(CurrentWeatherPresenterModel.STATE_FETCH_COMPLETE, presenterForTest.getPresenterModel().getStatus());
    }

    @Test
    public void testFetchFromCache() {
        String zipCodeAndCountry = "90400,US";
        final String mainWeatherSummary = "Rainy";
        WeatherData weatherData = new WeatherData() {
            @Override
            public Weather getWeatherSummary() {
                return new Weather() {
                    @Override
                    public String getMain() {
                        return mainWeatherSummary;
                    }
                };
            }
        };
        Mockito.when(weatherApiClient.getWeatherData(zipCodeAndCountry)).thenReturn(Observable.just(weatherData));

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final StringBuilder summaryInViewBuilder = new StringBuilder();
        CurrentWeatherPresenterForTest presenterForTest = new CurrentWeatherPresenterForTest(
                new ViewForTest() {
                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onContentReady(WeatherData content) {
                        super.onContentReady(content);
                        summaryInViewBuilder.delete(0, summaryInViewBuilder.length());
                        summaryInViewBuilder.append(content.getWeatherSummary().getMain());
                        countDownLatch.countDown();
                    }
                });
        presenterForTest.setFetchParams("90400", "United States");
        presenterForTest.startFetch();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Assert.fail();
        }
        Assert.assertEquals(mainWeatherSummary, summaryInViewBuilder.toString());
        Assert.assertEquals(CurrentWeatherPresenterModel.STATE_FETCH_COMPLETE, presenterForTest.getPresenterModel().getStatus());

        WeatherData weatherDataNew = new WeatherData() {
            @Override
            public Weather getWeatherSummary() {
                return new Weather() {
                    @Override
                    public String getMain() {
                        return "";
                    }
                };
            }
        };
        Mockito.when(weatherApiClient.getWeatherData(zipCodeAndCountry)).thenReturn(Observable.just(weatherDataNew));

        presenterForTest.setFetchParams("90400", "United States");
        presenterForTest.startFetch();
        Assert.assertEquals(mainWeatherSummary, summaryInViewBuilder.toString());
        Assert.assertEquals(CurrentWeatherPresenterModel.STATE_FETCH_COMPLETE, presenterForTest.getPresenterModel().getStatus());
    }

    private class CurrentWeatherPresenterForTest extends CurrentWeatherPresenter {
        private final ViewForTest mView;
        private CurrentWeatherPresenterModel mPresenterModel = new CurrentWeatherPresenterModel();

        public CurrentWeatherPresenterForTest(ViewForTest viewForTest) {
            mView = viewForTest;
        }

        @Override
        protected XLceView<WeatherData> getView() {
            return mView;
        }

        @Override
        protected CurrentWeatherPresenterModel getPresenterModel() {
            return mPresenterModel;
        }

        @Override
        protected WeatherApiClient getWeatherApiClient() {
            return weatherApiClient;
        }
    }

    private class ViewForTest implements XLceView<WeatherData> {

        @Override
        public Context getContext() {
            return null;
        }

        @Override
        public void onContentReady(WeatherData content) {

        }

        @Override
        public void onError(String error) {
        }

        @Override
        public void onFetchStart() {

        }
    }
}
