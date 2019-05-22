package developers.weightmanagement.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SleepDao {

    @Insert
    void registerSleep(Sleep sleep);

    @Query("SELECT * FROM sleep WHERE email=:userEmail")
    List<Sleep> allSleepRecord(final String userEmail);

}
