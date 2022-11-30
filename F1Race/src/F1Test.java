import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
public class F1Test {

    public static void main(String[] args) throws IOException {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }
}
class F1Race {
    // vashiot kod ovde
    private List<Racer> racerList;
    public F1Race() {
    }
    public void readResults(InputStream inputStream ) throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        racerList=reader.lines().map(x-> new Racer(x)).collect(Collectors.toList());
        reader.close();
    }

    public void printSorted(OutputStream outputStream) {
        PrintWriter print=new PrintWriter(new PrintWriter(outputStream));
        sort();
        for (int i=0;i<racerList.size();i++){
            print.println((i+1)+". "+racerList.get(i));
        }
        print.close();
    }
    public void sort(){
        racerList.sort(Comparator.naturalOrder());
    }
}
class Racer implements Comparable<Racer>{
    private String name;
    private String bestTime;
    public Racer(String inputLine) {
        String [] contents=inputLine.split("\\s+");
        name=contents[0];
        int rawTime=Integer.MAX_VALUE;
        int bestIndex=-1;
        for(int i=1;i<contents.length;i++){
            int localRaw=getRawTime(contents[i]);
            if(localRaw<rawTime)
            {
                rawTime=localRaw;
                bestIndex=i;
            }
        }
        bestTime=contents[bestIndex];
    }
    private int getRawTime(String time){
        String[] times=time.split(":");
        return Integer.parseInt(times[0])*60000+
                Integer.parseInt(times[1])*1000+
                Integer.parseInt(times[2]);
    }

    @Override
    public String toString() {
        String nameString=name;
        while (nameString.length()<10)nameString+=" ";
        String bestTimeString=bestTime;
        while (bestTimeString.length()<10)bestTimeString= " "+bestTimeString;
        return nameString+bestTimeString;
    }

    @Override
    public int compareTo(Racer o) {
        return Integer.compare(getRawTime(bestTime),o.getRawTime(o.bestTime));
    }
}