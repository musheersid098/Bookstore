
package coe528Final;

/**
 *
 * @author musheer siddiqui
 */
public class Silver extends CustomerStatus{
    public void promote (Customer c){
            c.setStatus (new Gold());
    }
    
    //@Override
    public void demote(Customer c){
    
    }
    
    @Override
    public String toString(){
        return "Silver Member";
    }
}
