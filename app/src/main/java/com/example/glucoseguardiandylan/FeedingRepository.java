package com.example.glucoseguardiandylan;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class FeedingRepository {
    private FeedingDao feedingDao;
    private LiveData<List<Feeding>> allFeedings;
    ExecutorService executorService;

    public FeedingRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        feedingDao = database.feedingDao();
        allFeedings = feedingDao.getAllFeedings();
    }
        //Database Operation Methods (API that repository shows to the outside)
    public void insert(Feeding feeding) {
        AppDatabase.databaseExecutor.execute(new InsertExecutor(feedingDao, feeding));
    }

    public void update(Feeding feeding){
        AppDatabase.databaseExecutor.execute(new UpdateExecutor(feedingDao, feeding));
    }

    public void delete(Feeding feeding){
        AppDatabase.databaseExecutor.execute(new DeleteExecutor(feedingDao, feeding));
    }

    public void deleteAllFeedings(){
        AppDatabase.databaseExecutor.execute(new DeleteAllFeedingsExecutor(feedingDao));
    }

    public LiveData<List<Feeding>> getAllFeedings(){
        return allFeedings;
    }

    public Feeding getLatestFeeding(){
        return feedingDao.getLatestFeeding();
    }

    private static class InsertExecutor implements Runnable{

        private FeedingDao feedingDao;
        private Feeding feeding;

        public InsertExecutor(FeedingDao feedingDao, Feeding feeding) {
            super();
            this.feedingDao = feedingDao;
            this.feeding = feeding;
        }

        @Override
        public void run() {
            feedingDao.insert(feeding);
        }
    }

    private static class UpdateExecutor implements Runnable{

        private FeedingDao feedingDao;
        private Feeding feeding;

        public UpdateExecutor(FeedingDao feedingDao, Feeding feeding) {
            super();
            this.feedingDao =feedingDao;
            this.feeding = feeding;
        }

        @Override
        public void run() {
            feedingDao.update(feeding);
        }
    }

    private static class DeleteExecutor implements Runnable{

        private FeedingDao feedingDao;
        private Feeding feeding;

        public DeleteExecutor(FeedingDao feedingDao, Feeding feeding) {
            super();
            this.feedingDao = feedingDao;
            this.feeding = feeding;
        }

        @Override
        public void run() {
            feedingDao.delete(feeding);
        }
    }

    private static class DeleteAllFeedingsExecutor implements Runnable{

        private FeedingDao feedingDao;
        private Feeding feeding;

        public DeleteAllFeedingsExecutor(FeedingDao feedingDao) {
            super();
            this.feedingDao = feedingDao;
        }

        @Override
        public void run() {
            feedingDao.deleteAllFeedings();
        }

    }

}
