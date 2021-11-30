package kingdomBuilder.model;

public class ClientDAO {

    int id;
    String name;
    int gameid;

    public ClientDAO() {
    }

    public ClientDAO(int id, String name, int gameid) {
        this.id = id;
        this.name = name;
        this.gameid = gameid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }
}