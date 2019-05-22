package developers.weightmanagement.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FoodDao {

    @Insert
    void registerFood(Food food);

    @Query("SELECT * FROM food WHERE email=:userEmail")
    List<Food> allFoodRecord(final String userEmail);

}
