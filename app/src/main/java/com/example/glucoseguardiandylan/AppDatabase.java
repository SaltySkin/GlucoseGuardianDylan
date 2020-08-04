package com.example.glucoseguardiandylan;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Feeding.class, Pet.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance; //turn it to a singlton, so that another instance cannot be made
    public abstract FeedingDao feedingDao(); //Used to access the feedings in the database
    public abstract PetDao petDao();
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //used to run code on the background thread, originally was going to use Async task, Async is now deprecated

    public static synchronized AppDatabase getInstance(Context context) { //synchronised allows only one thread at a time to access the method. To stop creating to instances of the database when two threads access the method  at once
        if (instance == null) { //builds new database only if there isn't one already, as to keep one instance of the database only
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").fallbackToDestructiveMigration().addCallback(roomCallback).build(); //fallback destroys database upon migration to avoid error
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseExecutor.execute(new PopulateDbExecutor(instance));
        }
    };

    private static void databaseExecutor(AppDatabase instance) {
    }

    private static class PopulateDbExecutor implements Runnable {

        private FeedingDao feedingDao;
        private PetDao petDao;

        public PopulateDbExecutor(AppDatabase database) {
            feedingDao = database.feedingDao();
            petDao = database.petDao();
        }

        @Override
        public void run() {
            feedingDao.insert(new Feeding(5, "Pizza and Chips", 70));
            feedingDao.insert(new Feeding(10, "Sushi", 56));
            feedingDao.insert(new Feeding(5.2, "Smoked Salmon Bagel", 42));
            petDao.insert(new Pet("Vampy", 100, 100));
        }
    }
}



