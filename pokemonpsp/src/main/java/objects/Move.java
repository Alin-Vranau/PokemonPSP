package objects;

public class Move {
    private int id;
    private String name;
    private int typeID;
    private int accuracy;
    private int power;

    public Move(int id, String name, int typeID, int accuracy, int power) {
        this.id = id;
        this.name = name;
        this.typeID = typeID;
        this.accuracy = accuracy;
        this.power = power;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setType(int typeID) {
        this.typeID = typeID;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public boolean equals(Object obj) {

        Move m = (Move) obj;
        if (this.name.equals(m.getName())){
            return true;
        } else {
            return false;
        }

    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
