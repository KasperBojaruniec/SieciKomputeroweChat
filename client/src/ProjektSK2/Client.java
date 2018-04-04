package ProjektSK2;

//Klasa trzymająca nazwę użytkownika oraz jego ID

public class Client {
    private String name;
    private int id;

    public Client(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Client(){}

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        return true;
    }


    public void setId(int id) {
        this.id = id;
    }
}
