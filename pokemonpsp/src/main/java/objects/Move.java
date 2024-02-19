package objects;

public class Move {
    private String name;
    private String type;
    private int accuracy;
    private int power;

    public Move(String name, String type, int accuracy, int power) {
        this.name = name;
        this.type = type;
        this.accuracy = accuracy;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
