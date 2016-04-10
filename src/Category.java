/**
 * Created by Laura on 4/10/2016.
 */
public class Category extends Model {
    public int id;
    public String name;

    @Override
    public String toString() {
        return String.format("[Category %d: %s]", id, name);
    }
}
