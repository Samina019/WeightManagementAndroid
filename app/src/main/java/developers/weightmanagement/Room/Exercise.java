package developers.weightmanagement.Room;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Time;

@Entity(tableName = "exercise")

public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "email")
    @ForeignKey(entity = Register.class,
        parentColumns = "user_email",
        childColumns = "email")
    private String email;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "user_weight")
    private String weight;

    @ColumnInfo(name = "user_burned_calories")
    private String calories;

    @ColumnInfo(name = "user_exercise_time")
    private String time;

    @ColumnInfo(name = "user_exercise_name")
    private String exercise_name;

    @ColumnInfo(name = "user_exercise_type")
    private String exercise_type;

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public String getExercise_type() {
        return exercise_type;
    }

    public void setExercise_type(String exercise_type) {
        this.exercise_type = exercise_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String user_id) {
        this.email = user_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
