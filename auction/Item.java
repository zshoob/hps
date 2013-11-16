public class Item{
  private int id;
  private int type;

  public Item(int id, int type){
    this.id = id;
    this.type = type;
  }

  public int getId(){
    return this.id;
  }

  public int getType(){
    return this.type;
  }

  public void setId(int id){
    this.id = id;
  }

  public void setType(int type){
    this.type = type;
  }
}

