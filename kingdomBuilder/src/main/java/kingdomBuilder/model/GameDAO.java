package kingdomBuilder.model;

import java.util.Objects;

/**
 * Stores information about the game.
 */
public class GameDAO {
    /**
     * Represents the id of the game.
     */
    private int id;
    /**
     * Represents the name of the game.
     */
    private String name;

    /**
     * Creates game object.
     * @param id id of the game.
     * @param name name of the game.
     */
    public GameDAO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the id of the game.
     * @return id of the game.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets name of the game.
     * @return name of the game.
     */
    public String getName() {
        return name;
    }

    /**
     * Computes whether the game object and the given object are equal.
     * @param obj object which is to be compared.
     * @return whether the object is equal to this game object.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GameDAO) obj;
        return this.id == that.id &&
                Objects.equals(this.name, that.name);
    }

    /**
     * Generates hashcode for id and name of the game.
     * @return the hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /**
     * Generates a string of the game and its attributes.
     * @return string of the game and its attributes.
     */
    @Override
    public String toString() {
        return "GameDAO[" +
                "id=" + id + ", " +
                "name=" + name + ']';
    }
}
