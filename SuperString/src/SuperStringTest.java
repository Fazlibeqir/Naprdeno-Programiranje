import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class SuperStringTest {
        public static void main(String[] args) {
            Scanner jin = new Scanner(System.in);
            int k = jin.nextInt();
            if (  k == 0 ) {
                SuperString s = new SuperString();
                while ( true ) {
                    int command = jin.nextInt();
                    if ( command == 0 ) {//append(String s)
                        s.append(jin.next());
                    }
                    if ( command == 1 ) {//insert(String s)
                        s.insert(jin.next());
                    }
                    if ( command == 2 ) {//contains(String s)
                        System.out.println(s.contains(jin.next()));
                    }
                    if ( command == 3 ) {//reverse()
                        s.reverse();
                    }
                    if ( command == 4 ) {//toString()
                        System.out.println(s);
                    }
                    if ( command == 5 ) {//removeLast(int k)
                        s.removeLast(jin.nextInt());
                    }
                    if ( command == 6 ) {//end
                        break;
                    }
                }
            }
        }
    }
    class SuperString{
    private LinkedList<String> strings;
    private Stack<String> history;
    public SuperString(){
        strings =new LinkedList<>();
        history =new Stack<>();
    }

        public void append(String s) {
            strings.addLast(s);
            history.push(s);
        }
        public void insert(String s) {
        strings.addFirst(s);
        history.push(s);
        }

        public boolean contains(String s) {
            return this.toString().contains(s);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder=new StringBuilder();
            for (String s:strings){
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        }

        public void reverse() {
            LinkedList<String> reversed=new LinkedList<>();
            String string;
            while (!strings.isEmpty()){
                string=strings.getLast();
                string=stringReverse(string);
                reversed.addLast(string);
                strings.removeLast();
            }
            for (String s: reversed){
                strings.addLast(s);
            }
        }
        private String stringReverse(String s){
            StringBuilder stringBuilder=new StringBuilder(s);
            return stringBuilder.reverse().toString();
        }

        public void removeLast(int k) {
           while (k>0){
               String toRemove=history.pop();
               strings.remove(toRemove);
               String toRemoveReversed= stringReverse(toRemove);
               strings.remove(toRemoveReversed);
               k--;
           }
        }
    }
