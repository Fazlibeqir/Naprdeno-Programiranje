import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Shapes1Test {
    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication();
        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
class ShapesApplication{
    private List<Canvas> canvases;
    public ShapesApplication() {
    }
    public int readCanvases(InputStream inputStream) throws IOException {
        BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream));
        canvases= bf.lines().map(x->new Canvas(x)).collect(Collectors.toList());
        int count=0;
        for (Canvas c:canvases) count+=c.count();
        bf.close();
        return count;
    }
    public void printLargestCanvasTo(OutputStream outputStream){
    PrintWriter pw=new PrintWriter(new PrintStream(outputStream));
    pw.println(max().toString());
    pw.close();
    }
    public Canvas max(){
        return canvases.stream().max(Comparator.naturalOrder()).get();
    }
}
class Canvas implements Comparable<Canvas>{
    private String id;
    private List<Integer> list;

    public Canvas(String line){
        String [] info=line.split("\\s+");
        id=info[0];
        list=new ArrayList<Integer>();
        for (int i = 1 ; i <info.length; i++) {
            list.add(Integer.parseInt(info[i]));
        }
    }
    public int count(){
        return list.size();
    }
    public int sum(){
        int sum=0;
        for (int i:list) sum+=i;
        return 4*sum;
    }
    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(sum(),o.sum());
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,count(),sum());
    }
}