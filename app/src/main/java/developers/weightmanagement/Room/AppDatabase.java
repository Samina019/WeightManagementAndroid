package developers.weightmanagement.Room;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Register.class,Exercise.class,Sleep.class,Food.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RegisterDao registerDao();
    public abstract ExerciseDao exerciseDao();
    public abstract SleepDao sleepDao();
    public abstract FoodDao foodDao();

}

