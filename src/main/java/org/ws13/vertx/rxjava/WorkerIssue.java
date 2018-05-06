package org.ws13.vertx.rxjava;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.vertx.reactivex.RxHelper;
import io.vertx.reactivex.core.Vertx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author ctranxuan
 */
public class WorkerIssue {

    public static void main(String[] args) {
        String data = readFile();
        Vertx vertx = Vertx.vertx();

        Scheduler scheduler = RxHelper.blockingScheduler(vertx.getDelegate()); // see rxjava-scheduler-vertx-blocking.png
//        scheduler = RxHelper.scheduler(vertx.getDelegate().createSharedWorkerExecutor("toto")); // see rxjava-scheduler-vertx-workerexecutor.png
//        scheduler = RxHelper.scheduler(vertx.getDelegate().getOrCreateContext()); // see rxjava-scheduler-vertx-eventloop.png
//        scheduler = Schedulers.io(); // see rxjava-scheduler-io.png
//        scheduler = Schedulers.computation(); // see rxjava-scheduler-computation.png

        Flowable<DataInfo> dataInfos = Flowable.interval(1, MILLISECONDS)
                                               .map(l -> new DataInfo(UUID.randomUUID().toString(), data))
                                               .share();

        int subscribersNumber = 1000;

        for (int i = 0; i < subscribersNumber; i++) {
            dataInfos.onBackpressureLatest()
                     .observeOn(scheduler)
                     .subscribe();

        }

        Flowable.timer(1, HOURS)
                .blockingSubscribe();
    }

    private static String readFile() {
        StringBuilder result = new StringBuilder();
        try (InputStream inputStream = WorkerIssue.class.getResourceAsStream("/data.txt")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
