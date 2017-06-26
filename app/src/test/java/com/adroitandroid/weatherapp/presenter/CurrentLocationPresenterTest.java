package com.adroitandroid.weatherapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.adroitandroid.mvx.lce.XLceView;
import com.adroitandroid.weatherapp.model.IpLocationData;
import com.adroitandroid.weatherapp.network.IpApiClient;
import com.adroitandroid.weatherapp.network.RetrofitClient;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by pv on 26/06/17.
 */

public class CurrentLocationPresenterTest {

    @Mock
    private IpApiClient ipApiClient;
    @Mock
    private RetrofitClient retrofitClient;

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
    public void testFetchFail() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Mockito.when(ipApiClient.getIpLocationData()).thenReturn(new Observable<IpLocationData>() {
            @Override
            protected void subscribeActual(Observer<? super IpLocationData> observer) {
                observer.onError(new IOException("No internet"));
                countDownLatch.countDown();
            }
        });

        CurrentLocationPresenterForTest presenter = new CurrentLocationPresenterForTest(ipApiClient);
        presenter.startFetch();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Assert.fail();
        }
        Assert.assertEquals(CurrentLocationPresenter.ERROR_MSG_LOCATION_NOT_FOUND, presenter.errorInView);
    }

    private class CurrentLocationPresenterForTest extends CurrentLocationPresenter {

        private final IpApiClient mApiClient;
        private String errorInView;

        public CurrentLocationPresenterForTest(IpApiClient ipApiClient) {
            mApiClient = ipApiClient;
        }

        @Override
        protected IpApiClient getIpApiClient() {
            return mApiClient;
        }

        @Override
        protected XLceView<IpLocationData> getView() {
            return new XLceView<IpLocationData>() {
                @Override
                public void onContentReady(IpLocationData content) {

                }

                @Override
                public void onError(String error) {
                    errorInView = error;
                }

                @Override
                public void onFetchStart() {

                }

                @Override
                public Context getContext() {
                    return null;
                }
            };
        }
    }
}
