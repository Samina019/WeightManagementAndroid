package developers.weightmanagement.Room;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Time;

@Entity(tableName = "food")

public class Food {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "email")
    @ForeignKey(entity = Register.class,
        parentColumns = "user_email",
        childColumns = "email")
    private String email;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "meal_type")
    private String mealType;

    @ColumnInfo(name = "food_name")
    private String foodName;

    @ColumnInfo(name = "calories")
    private String calories;

    @ColumnInfo(name = "carbohydrates")
    private String carbohydrates;

    @ColumnInfo(name = "fats")
    private String fats;

    @ColumnInfo(name = "proteins")
    private String proteins;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getFats() {
        return fats;
    }

    public void setFats(String fats) {
        this.fats = fats;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }
}
