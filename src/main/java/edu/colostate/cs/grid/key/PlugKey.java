package edu.colostate.cs.grid.key;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 9/13/14
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlugKey {

    private int plugID;
    private int houseHoldID;

    public PlugKey(int plugID, int houseHoldID) {
        this.plugID = plugID;
        this.houseHoldID = houseHoldID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlugKey) {
            PlugKey key = (PlugKey) obj;
            return (this.plugID == key.plugID) && (this.houseHoldID == key.houseHoldID);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.plugID + this.houseHoldID;
    }

    public int getPlugID() {
        return plugID;
    }

    public void setPlugID(int plugID) {
        this.plugID = plugID;
    }

    public int getHouseHoldID() {
        return houseHoldID;
    }

    public void setHouseHoldID(int houseHoldID) {
        this.houseHoldID = houseHoldID;
    }
}
