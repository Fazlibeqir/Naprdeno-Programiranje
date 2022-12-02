import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MojDDVTest{
    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}
class MojDDV{
    private List<Bill> listName;
    public static double repay=0.15;

    public void readRecords(InputStream inputStream) {
        BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream));
        listName =bf.lines().map(x->{
            try {
                return new Bill(x);
            }catch (AmountNotAllowedException e){
                System.out.println(e.getMessage());
                return new Bill();
            }
        }).collect(Collectors.toList());
    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw=new PrintWriter(new PrintStream(outputStream));
        listName.stream().filter(x->x.getId()!=-1).forEach(x->pw.println(x.toString()));
        pw.close();
    }
}
class Bill {
    private int id;
    private List<Product> products;

    public Bill() {
        id=-1;
    }
    public int getId(){
        return id;
    }
    public Bill(String line) throws AmountNotAllowedException{
        id=Integer.parseInt(line.split("\\s+")[0]);
        products=new ArrayList<Product>();
        Arrays.stream(line.split("\\s+")).skip(1).forEach(x->{
            try {
                int price=Integer.parseInt(x);
                products.add(new Product(price));
            }catch (NumberFormatException nfe){
                Type type=Type.A;
                if (x.equals("B")) type=Type.B;
                if (x.equals("V")) type=Type.V;
                products.get(products.size()-1).setType(type);
            }
        });
        if (sum()>30000) throw new AmountNotAllowedException(sum());
    }
    public int sum(){
        return products.stream().mapToInt(Product::getPrice).sum();
    }

    @Override
    public String toString() {
        return String.format("%d %d %.2f",id,sum(),taxReturn());
    }
    private double taxReturn(){
        return products.stream().mapToDouble(x->x.getTax()*x.getPrice()*MojDDV.repay).sum();
    }
}
enum Type{A,B,V}
class Product{
    private int price;
    private Type type;
    private double tax;
    public Product(int price){
        this.price=price;
    }
    public int getPrice() {
        return price;
    }
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        if (type==Type.A) tax=0.18;
        if (type==Type.B) tax=0.05;
        if (type==Type.V) tax=0;

    }
    public double getTax() {
        return tax;
    }
}
class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int sum){
        super(String.format("Receipt with amount %d is not allowed to be scanned",sum));
    }
}