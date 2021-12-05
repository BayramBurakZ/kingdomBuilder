package kingdomBuilder.model;

import java.util.Objects;

public class GameDAO {

    private int id;
    private String name;

    public GameDAO(
            int id,
            String name
    ) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GameDAO) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "GameDAO[" +
                "id=" + id + ", " +
                "name=" + name + ']';
    }
}
