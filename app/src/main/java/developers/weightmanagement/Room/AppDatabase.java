package developers.weightmanagement.Room;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Register.class,Exercise.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RegisterDao registerDao();
    public abstract ExerciseDao exerciseDao();

}

