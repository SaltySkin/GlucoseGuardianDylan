package com.example.glucoseguardiandylan;

        import androidx.lifecycle.LiveData;
        import androidx.room.Dao;
        import androidx.room.Delete;
        import androidx.room.Insert;
        import androidx.room.Query;
        import androidx.room.Update;

        import java.util.List;

@Dao //One DAO per Entity
public interface FeedingDao {
    @Insert
    void insert(Feeding feeding);

    @Update
    void update(Feeding feeding);

    @Delete
    void delete(Feeding feeding);

    @Query("DELETE FROM feeding_table") //allows you to make your own queries
    void deleteAllFeedings();

    @Query("SELECT * FROM feeding_table ORDER BY date DESC")
    LiveData<List<Feeding>> getAllFeedings();
}
