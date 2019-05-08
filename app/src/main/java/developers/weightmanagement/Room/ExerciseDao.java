package developers.weightmanagement.Room;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert
    void registerExercise(Exercise exercise);

    @Query("SELECT * FROM exercise WHERE email=:userEmail")
    List<Exercise> allExercisesRecord(final String userEmail);

}

