package coe528Final;

/**
 * @author musheer siddiqui
 */
public class Customer extends User {
    private int points;
    private CustomerStatus status = new Silver();
    
    //constructor
    public Customer (String username, String password, int points, CustomerStatus status){
        super (username, password);
        this.points = points;
        this.status = status;
    }
    
    //state design method
    public void promote(){
        status.promote(this);
    }
    
    //state design method
    public void demote(){
        status.demote(this);
    }
    
    //Does the calculations of the price and points, writes down updates to customers file
    public void buyABook (Book book, Owner currentOwner){
        points += (book.price*10);
        if (points >=1000){
            promote();
            
        }
        for (Customer customer: currentOwner.getCustomers()){
            if (customer.getUsername().equals(this.getUsername())){
                customer.setPoints(this.points);
                customer.setStatus(this.status);
                break;
            }
        }
        currentOwner.writeCustomers();
    }
    
    
//Does the calculations of price and points, updates the customers file
    public double redeemPointsAndBuy (Book book, Owner currentOwner){
        double balanceInPoints  = book.getBookPrice()*10;
        double remainingBalance = 0;
        if (points >= balanceInPoints){
            points -= balanceInPoints; 
           }else{
            remainingBalance = balanceInPoints - points;
            points = 0;
            remainingBalance /= 10;
            System.out.println ("Insufficient points, your total is " + book.getBookPrice() + ". After redeeming points, you owe " + remainingBalance + " CAD");
        }
        if (this.points >= 1000){
            promote();
        }else{
            demote();
        }
                for (Customer customer: currentOwner.getCustomers()){
            if (customer.getUsername().equals(this.getUsername())){
                customer.setPoints(this.points);
                customer.setStatus(this.status);
                break;
            }
        }
        currentOwner.writeCustomers();
        return remainingBalance;
    }
    
    
    //status getter
    public CustomerStatus getStatus(){
        return status;
    }
    
    //points getter
    public int getPoints(){
        return this.points;
    }
    
    //points setter
    public void setPoints (int points){
        this.points  = points;
    }
    
    //status setter
    public void setStatus(CustomerStatus s){
        status = s;
    }
    
    //username getter
     public String getUsername(){
        return this.username;
    }
    
     //password getter
    public String getPassword(){
        return this.password;
    }
}

