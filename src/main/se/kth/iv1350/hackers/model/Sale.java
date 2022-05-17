package se.kth.iv1350.hackers.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import se.kth.iv1350.hackers.DTO.*;
import se.kth.iv1350.hackers.util.Amount;

/**
 * A representation of a sale.
 */
public class Sale{
    private TotalPrice totalPrice;
    private HashMap <String, Item> items = new HashMap<String, Item>();
    private LocalDateTime dateAndTime;
    private Amount amountPaid;
    private Amount changeAmount;
    private ArrayList<PaymentObserver> paymentObserversList = new ArrayList<PaymentObserver>();

    /**
     * Creates a new instance of sale with initial amount set to zero.
     */
    public Sale(){
        this.totalPrice = new TotalPrice();
        this.items = new HashMap<String, Item>();
        this.dateAndTime =  LocalDateTime.now();
        this.amountPaid = new Amount(0);
        this.changeAmount = new Amount(0);
    }

    /**
     * Search if the item that is being added exists inside the <code>items</code> object.
     * If the item already exists the method will call a nesting method to update the quantity
     * and total payment amount.
     * If the item does not not exists the method will call a nesting method to add the item
     * and update the total payment amount.
     * 
     * @param item The current item that is being added.
     */
    public SaleDTO addItem(Item item){
        if (itemListContainsItem(item)){

            this.updateTotal(item);
            this.updateQuantity(item);
        }
        else {
            this.addItemToList(item);
        }
        return new SaleDTO(this);
    }

    /**
     * Checks if the scanned item already exist in the HashMap.
     * @param item The current item that is being added.
     * @return <code>true</code> if the item exists in the HashMap.
     * returns <code>false</code> if the item 
     */
    private boolean itemListContainsItem(Item item){
        System.out.println ("Sale: Checks if " + item.getItemDescription().getItemName() + " exists on the item list");
        return items.containsKey(item.getItemIdentifier());
    }

    /**
     * Updates the quantity of an item.
     * 
     * @param item The current item that is being added.
     */
    private void updateQuantity (Item item){
        Item existingItem = items.get(item.getItemIdentifier());
        existingItem.increaseQuantity(item.getQuantity());
        System.out.println("Sale: Increasing the quantity of" + item.getItemDescription().getItemName() + "\n");
        addItemToList(existingItem);
        updateTotal(item);
    }

    /**
     * Adds a new item to the sale
     * 
    * @param item The item that is being added.
    */
    private void addItemToList (Item item){
        items.put(item.getItemIdentifier(), item);
        System.out.println("Sale: adding " + item.getItemDescription().getItemName() + " to the Sale");
        updateTotal(item);

    }
    /**
     * updates the total with the corresponding price of that item
     * 
     * @param item
     */
    private void updateTotal (Item item){
        totalPrice.UpdatePrice(item);
        System.out.println("Sale: Updating the total price: " + totalPrice.getTotal().getAmount() + " with the item: " + item.getItemDescription().getItemName());
    }

     /**
     * Applies the discount to the <code>currentSale</code> object.
     * 
     * @param discount contains the discount information that shall
     * be applied to the <code>totalPrice</code> parameter.
     * @return a new updated <code>SaleDTO</code> with the applied discount
     */
    public SaleDTO applyDiscount (DiscountDTO discount){
        totalPrice.setTotalDiscountedIncludingVAT(totalPrice.getTotalIncludingVAT().multiply(
            discount.getTotalDiscountPercentage()));
            System.out.println("Sale: applying discount: " + discount.getTotalDiscountPercentage().toString() + "% * " + totalPrice.getTotalIncludingVAT().toString() + " = " + totalPrice.getTotalDiscountedIncludingVAT() + "SEK");
        return new SaleDTO (this);
   }

    /**
    * Finalizes the sale. 
    *
    * @param currentSale The current sale that is being processed.
    * @return The final version of SaleDTO containing all the sale information.
    */
   public SaleDTO endSale(){
       SaleDTO endedSale = new SaleDTO(this);
       notifyObservers(endedSale.getTotalPrice().getTotalDiscountedIncludingVAT());
       return endedSale;
   }

    /**
     * Gets the value of totalPrice.
     * 
     * @return the totalPrice.
     */
    public TotalPrice getTotalPrice(){
        return totalPrice;
    }

    /**
     * Get the value of HashMap items.
     * 
     * @return the HashMap items.
     */
    public HashMap<String, Item> getItems(){
        return items;
    }

    /**
     * Get the value of dateAndTime.
     * 
     * @return the dateAndTime value.
     */
    public LocalDateTime getLocalDateTime(){
        return this.dateAndTime;
    }

    /**
     * Get the value of amountPaid.
     * 
     * @return the paid amount.
     */
    public Amount getAmountPaid(){
        return this.amountPaid;
    }

    /**
     * Get the value of changeAmount.
     * 
     * @return the changeAmount.
     */
    public Amount getChangeAmount(){
        return this.changeAmount;
    } 

    /**
     * Registers customer payment by asserting amount paid in currentSale, 
     * and calculates change.
     * @param payment Customer payment.
     * @return change in form of Amount
     */

    public Amount registerPayment (Amount payment){
        this.amountPaid = payment;
        this.changeAmount = totalPrice.getTotalDiscountedIncludingVAT().decrease(amountPaid);
        return this.changeAmount;
    }

    /**
     * Notifies all obervers of the total revenue.
     * 
     * @param totalRevenue total revenue of the sale of type Amount.
     */

    void notifyObservers(Amount totalRevenue){
        for(PaymentObserver obs : paymentObserversList ){
            obs.updateTotal(totalRevenue.getAmount());
        }
    }

    /**
     * Adds a payment observer to the list of payment observers.
     * 
     * @param paymentObserver payment observer.
     */

    public void addPaymentObserver(PaymentObserver paymentObserver){
        paymentObserversList.add(paymentObserver);
    }

    /**
     * 
     * @param paymentObserversList
     */

    public void addPaymentObserver(ArrayList<PaymentObserver> paymentObserversList){
        for(PaymentObserver paymentObserver : paymentObserversList){
            this.paymentObserversList.add(paymentObserver);
        }
    }
}