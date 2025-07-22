
package coe528Final;

/**
 *
 * @author musheer siddiqui
 */
public class Gold extends CustomerStatus{
    @Override
    public void promote (Customer c){
      
    }
    
    @Override
    public void demote(Customer c){
        c.setStatus (new Silver());
    }
    
    @Override
    public String toString(){
        return "Gold Member";
    }
}
