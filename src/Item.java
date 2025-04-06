import java.io.Serializable;


/**
 * Item class
 *
 * This class represents an item with a title, description, price, seller, and sold status.
 * 
 * @author L10-Team1 
 *
 * @version April 2024
 *
 */
public class Item implements ItemInterface, Serializable {
    
    private int id; //ID of the item
    private String title; //Title of the item
    private String description; //Description of the item
    private double price; //Price of the item
    private String seller; //Seller of the item
    private boolean sold; //Sold status of the item
    
    public Item(String title, String description, double price, String seller) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.sold = false; // Item is not sold (False) upon creation
    }
    
    /**
    * Returns the ID of the item.
    *
    * @return id
    */
    public int getId() {
        return id;
    }
    
    /**
    * Sets the ID of the item. 
    *
    * @param id the ID to set
    */
    public void setId(int id) {
        this.id = id;
    }

    /**
    * Returns title of the item.
    *
    * @return title
    */
    public String getTitle() {
        return title;
    }
    
    /**
    * Returns description of the item.
    *
    * @return description
    */
    public String getDescription() {
        return description;
    }
    
    /**
    * Returns price of the item.
    *
    * @return price
    */
    public double getPrice() {
        return price;
    }
    
    /**
    * Returns seller of the item.
    *
    * @return seller
    */
    public String getSeller() {
        return seller;
    }
    
    /**
    * Returns if the item is sold or not.
    *
    * @return sold
    */
    public boolean isSold() {
        return sold;
    }
    
    /**
    * Sets the sold status of the item. 
    *
    * @param sold the sold status to set
    */
    public void setSold(boolean sold) {
        this.sold = sold;
    }
}
