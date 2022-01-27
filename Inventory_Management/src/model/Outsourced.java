package model;

/**
 * @author Tiffany Rolle
 */


/**Outsourced part class extending the part class*/
public class Outsourced extends Part{

    /**Declares field companyName*/
    private String companyName;

    /**Outsourced part constructor*/
    public Outsourced(int id, String name, int stock, double price, int min, int max, String companyName) {
        super(id, name, stock, price, min, max);
        this.companyName = companyName;
    }

    /**
     @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     @param companyName set the company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /** This method validates part and returns a boolean value. */
    public static boolean ispartValid( int stock, double price, int min, int max)
    {
        if((stock > max) || (stock < min) )
            return false;
        if((min > max) || (max < min))
            return false;

        else return true;
    }
}
