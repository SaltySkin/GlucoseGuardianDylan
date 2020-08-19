package com.example.glucoseguardiandylan;

        import androidx.lifecycle.LiveData;
        import androidx.room.Dao;
        import androidx.room.Delete;
        import androidx.room.Insert;
        import androidx.room.Query;
        import androidx.room.Update;
        import java.util.List;

@Dao //One Data Access Object per Entity - Used to access and query the feeding objects stored in the database from the repository
public interface FeedingDao {
   //CRUD
    @Insert
    void insert(Feeding feeding);

    @Update
    void update(Feeding feeding);

    @Delete
    void delete(Feeding feeding);

    @Query("DELETE FROM feeding_table") //Room allows you to make your own queries as well
    void deleteAllFeedings();

    @Query("SELECT * FROM feeding_table ORDER BY date DESC") //Live data object in android allows you to observe a list and update it when changes are made
    LiveData<List<Feeding>> getAllFeedings();

    @Query("SELECT * FROM feeding_table ORDER BY date DESC LIMIT 1") //gets the last feeding object added to the database
    Feeding getLatestFeeding();
}
