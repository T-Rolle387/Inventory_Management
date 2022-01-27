package model;

/**
 * @author Tiffany Rolle
 * */

/** InHouse Part class extending the Part Class*/
public class InHouse extends Part {

    /** Declares field */
    private static int machineId;

    /** InHouse Constructor*/
    public InHouse(int id, String name, int stock, double price, int min, int max, int machineId) {
        super(id, name, stock, price, min, max);
        this.machineId = machineId;
    }

    /**
     @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     @param machineId set the machineId
     */
    public void setMachineid(int machineId) {
        this.machineId = machineId;
    }

/** This method checks if the part is valid. */
    public static boolean ispartValid( int stock, double price, int min, int max)
    {
        if((stock > max) || (stock < min) )
            return false;
        if((min > max) || (max < min))
            return false;

        else return true;
    }
}

