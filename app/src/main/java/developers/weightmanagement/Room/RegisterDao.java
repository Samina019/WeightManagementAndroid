package developers.weightmanagement.Room;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface RegisterDao {

    @Insert
    void registerUser(Register user);

    @Query("SELECT COUNT(user_email) FROM register WHERE user_email = :email AND user_password = :password")
    int isValidUser(String email, String password);

}

