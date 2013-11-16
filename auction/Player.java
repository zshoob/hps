import java.util.List;
import java.util.ArrayList;

public class Player{
  private int id;
  private int budget;
  private List<Item> ownedItems;

  public Player(int id){
    this.id = id;
    this.budget = 100;
    this.ownedItems = new ArrayList<Item>();
  }

  public int getBudget(){
    return this.budget;
  }

  public List<Item> getOwnedItems(){
    return this.ownedItems;
  }

  public void winItem(Item item, int price){
    this.ownedItems.add(item);
    this.budget = this.budget - price;
  }

  //public void winItem(Item item, int remainBudget){
  //  this.ownedItems.add(item);
  //  this.budget = remainBudget;
  //}

}

