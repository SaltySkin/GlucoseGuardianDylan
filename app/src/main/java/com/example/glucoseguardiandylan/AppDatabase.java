package com.example.glucoseguardiandylan;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//The database the contains the Feeding, and pet classes, uses room annotation @database to tell room as such.
@Database(entities = {Feeding.class, Pet.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance; //turn it to a singlton, so that another instance cannot be made
    public abstract FeedingDao feedingDao(); //Used to access the feedings in the database
    public abstract PetDao petDao(); //Used to access and query the pet objects stored in the database
    private static final int NUMBER_OF_THREADS = 4;//Used as the number of threads for running CRUD commands in the background
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS); //Background thread executor object, so CRUD commands don't lock the UI
    //originally was going to use Async task, Async is now deprecated

    //static synchronised allows only one thread at a time to access the method. To stop creating to instances of the database when two threads access the method  at once
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) { //builds new database only if there isn't one already, as to keep one instance of the database only
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database").fallbackToDestructiveMigration().addCallback(roomCallback).allowMainThreadQueries().build(); //fallback destroys database upon migration to avoid error
        }
        return instance;
    }

    //runs the room database and executes a PopulateDbExecutor innerclass to populate the app (for testing, and demo)
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseExecutor.execute(new PopulateDbExecutor(instance));
        }
    };

    private static void databaseExecutor(AppDatabase instance) {
    }

    //Runnable class that is used to populate the database on the background thread on install
    private static class PopulateDbExecutor implements Runnable {

        private FeedingDao feedingDao;
        private PetDao petDao;
        private Date date = new Date();
        private Long currentDate = date.getTime();



        public PopulateDbExecutor(AppDatabase database) {
            feedingDao = database.feedingDao();
            petDao = database.petDao();
        }

        @Override
        public void run() {
            feedingDao.insert(new Feeding(5, "Pizza and Chips", 70, "Before Meal"));
            feedingDao.insert(new Feeding(10, "Sushi", 56, "After Meal"));
            feedingDao.insert(new Feeding(5.2, "Smoked Salmon Bagel", 42, "No Meal"));
            petDao.insert(new Pet(1,"Vampy", 90, 50, currentDate));

        }
    }
}



