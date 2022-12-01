import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class PizzaOrderTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            Orders(jin, order);
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            Orders(jin, order);
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            Orders(jin, order);
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

    private static void Orders(Scanner jin, Order order) {
        while (true) {
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                if (!jin.hasNextInt()) break;
                order.addItem(item, jin.nextInt());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
interface Item{
    int getPrice();
    String getType();
}
class ExtraItem implements Item{
    private String type;
    private int price;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if (type.equals("Ketchup"))price=3;
        else if (type.equals("Coke"))price=5;
        else throw new InvalidExtraTypeException();
        this.type = type;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getType() {
        return type;
    }
}
class PizzaItem implements Item{
    private String type;
    private int price;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        switch (type) {
            case "Standard" -> price = 10;
            case "Pepperoni" -> price = 12;
            case "Vegetarian" -> price = 8;
            default -> throw new InvalidPizzaTypeException();
        }
        this.type = type;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getType() {
        return type;
    }
}
class InvalidExtraTypeException extends Exception{
    public InvalidExtraTypeException() {
        super("Invalid extra type");
    }
}
class InvalidPizzaTypeException extends Exception{
    public InvalidPizzaTypeException(){
        super("Invalid pizza type");
    }
}
class ItemOutOfStockException extends Exception{
    public ItemOutOfStockException(Item item) {
        super(item.toString());
    }
}
class EmptyOrder extends Exception{
 public EmptyOrder(){
     super();
 }
}
class OrderLockedException extends Exception{
    public OrderLockedException(){
        super();
    }
}
class Order{
    private List<Item> items;
    private boolean locked=false;
    public Order() {
        this.items=new ArrayList<Item>();
    }
    public void addItem(Item item,int count)throws ItemOutOfStockException,OrderLockedException{
        if(locked) throw new OrderLockedException();
        if(count>10) throw new ItemOutOfStockException(item);
        int first=-1;
        for (int i=0;i<items.size();i++){
            if(items.get(i).getType().equals(item.getType())){
                first=i;
                break;
            }
        }
        items=items.stream().filter(x->!x.getType().equals(item.getType())).collect(Collectors.toList());
        for (int i=0;i<count;i++){
            if (first==-1){
                items.add(item);
            }else {
                items.add(first,item);
            }
        }
    }
    public void removeItem(int idx)throws OrderLockedException,ArrayIndexOutOfBoundsException{
        if(locked) throw new OrderLockedException();
        if(idx<0 || idx>items.size()-1)throw new ArrayIndexOutOfBoundsException();
        String type=items.get(idx).getType();
        items=items.stream().filter(x->!x.getType().equals(type)).collect(Collectors.toList());
    }
    public void lock()throws EmptyOrder {
     if(items.size()>0)this.locked=true;
     else throw new EmptyOrder();
    }
    public int getPrice(){
        int price=0;
        for(Item i:items)price+=i.getPrice();
        return price;
    }
    public void displayOrder(){
        int i=1;
        for(List<Item> item: categorize()){
            int price=item.size()*item.get(0).getPrice();
            System.out.printf("%3s.%-15sx%2s%5s$\n",i++,item.get(0).getType(),item.size(),price);
        }
        System.out.printf("%-22s%5s$\n","Total:",this.getPrice());

    }
    private List<List<Item>> categorize(){
        List<List<Item>> categorized= new ArrayList<List<Item>>();
        for (Item i:items){
            boolean found=false;
            for (List<Item>cat:categorized){
                if(cat.get(0).getType().equals(i.getType())){
                    cat.add(i);
                    found=true;
                }
            }
            if (!found){
                List<Item> newArrayList=new ArrayList<Item>();
                newArrayList.add(i);
                categorized.add(newArrayList);
            }
        }
        return categorized;
    }
}