import java.util.*;
import static java.util.Arrays.*;

enum TYPE {
    POINT,
    CIRCLE
}
enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}
public class CirclesTest {
    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);
        try {
            if (Integer.parseInt(parts[0]) == 0) { //point
                collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            }
        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());

    }
}
interface Movable{
    void moveUp()throws ObjectCanNotBeMovedException;
    void moveDown()throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
}
class MovablePoint implements Movable{
    private int x;
    private int y;
    private  int xSpeed;
    private  int ySpeed;
    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates ("+x+","+y+")";
    }
    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        canMove(0,ySpeed);
        y+=ySpeed;
    }
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException{
        canMove(0,-ySpeed);
        y-=ySpeed;
    }
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException{
        canMove(xSpeed,0);
        x+=xSpeed;
    }
    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException{
        canMove(-xSpeed,0);
        x-=xSpeed;
    }
    @Override
    public int getCurrentXPosition() {
        return x;
    }
    @Override
    public int getCurrentYPosition() {
        return y;
    }

    public boolean canMove(int deltaX, int deltaY) throws ObjectCanNotBeMovedException {
        if(x+deltaX> MovablesCollection.x_MAX || x+deltaX<0
                || y+deltaY> MovablesCollection.y_MAX || y+deltaY<0){
          throw new ObjectCanNotBeMovedException(String.format("Point (%d,%d) is out of bounds",x+deltaX,y+deltaY));
        }
        return true;
    }
}
class MovableCircle implements Movable{
    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }
    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        center.moveUp();
    }
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        center.moveDown();
    }
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
    center.moveRight();
    }
    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
    center.moveLeft();
    }
    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }
    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }
    public int getRadius(){
        return radius;
    }
    public MovablePoint getCenter() {
        return center;
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates ("+center.getCurrentXPosition()+","
                +center.getCurrentYPosition()+") and radius "+radius;
    }
}
class ObjectCanNotBeMovedException extends Exception{
    public ObjectCanNotBeMovedException(String message){
        super(message);
    }
}
class MovableObjectNotFittableException extends Exception{
    public MovableObjectNotFittableException(String message){
        super(message);
    }
}
class MovablesCollection {
    private List<Movable> movables;
    public static int x_MAX;
    public static int y_MAX;

    public MovablesCollection(Movable[] movables,int x_MAX,int y_MAX) {
        this.movables = asList(movables);
        MovablesCollection.x_MAX =x_MAX;
        MovablesCollection.y_MAX =y_MAX;
    }
    public MovablesCollection(Movable[] movables,int x_MAX){
        this.movables=Arrays.asList(movables);
        MovablesCollection.x_MAX =x_MAX;
        y_MAX=100;
    }
    public MovablesCollection(Movable[] movables){
        this.movables=Arrays.asList(movables);
        x_MAX=100;
        y_MAX=100;
    }
    public MovablesCollection(int x_MAX,int y_MAX){
        this.movables=new ArrayList<Movable>();
        MovablesCollection.x_MAX =x_MAX;
        MovablesCollection.y_MAX =y_MAX;
    }
    public static void setxMax(int x_MAX){
        MovablesCollection.x_MAX=x_MAX;
    }
    public static void setyMax(int y_MAX){
        MovablesCollection.y_MAX=y_MAX;
    }
    void addMovableObject(Movable m)throws MovableObjectNotFittableException{
        canExistMovable(m);
        movables.add(m);
    }
    boolean canExistMovable(Movable m) throws MovableObjectNotFittableException{
        if(m.getCurrentXPosition()>x_MAX || m.getCurrentXPosition()<0
                || m.getCurrentYPosition()>y_MAX || m.getCurrentYPosition()<0){
            if( m instanceof MovableCircle){
                throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",m.getCurrentXPosition(),m.getCurrentYPosition(),((MovableCircle) m).getRadius()));
            }
            else throw new MovableObjectNotFittableException(String.format("Movable point with center (%d,%d) can not be fitted into the collection",m.getCurrentXPosition(),m.getCurrentYPosition()));
        }

        if(m instanceof MovableCircle){
            MovableCircle mc = (MovableCircle) m;
            if(mc.getCurrentXPosition()+mc.getRadius()>x_MAX || mc.getCurrentXPosition()-mc.getRadius()<0
                    || mc.getCurrentYPosition()+mc.getRadius()>y_MAX || mc.getCurrentYPosition()-mc.getRadius()<0){
                throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection",m.getCurrentXPosition(),m.getCurrentYPosition(),((MovableCircle) m).getRadius()));
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String str="Collection of movable objects with size "+movables.size()+":\n";
        for(Movable movable:movables){
            str+=movable.toString()+'\n';
        }
        return str;
    }


    void moveObjectsFromTypeWithDirection (TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException {
        Movable[] arrayTest = movables.toArray(new Movable[movables.size()]);
        ObjectCanNotBeMovedException ex = null;
        for(Movable mv : arrayTest){
            try {
                if (mv instanceof MovablePoint && type == TYPE.POINT) {
                    if (direction == DIRECTION.DOWN) mv.moveDown();
                    if (direction == DIRECTION.UP) mv.moveUp();
                    if (direction == DIRECTION.LEFT) mv.moveLeft();
                    if (direction == DIRECTION.RIGHT) mv.moveRight();
                } else if (mv instanceof MovableCircle && type == TYPE.CIRCLE) {
                    if (direction == DIRECTION.DOWN) mv.moveDown();
                    if (direction == DIRECTION.UP) mv.moveUp();
                    if (direction == DIRECTION.LEFT) mv.moveLeft();
                    if (direction == DIRECTION.RIGHT) mv.moveRight();
                }
            }catch (ObjectCanNotBeMovedException e){
                ex = e;
            }
        }
        if(ex!=null) throw ex;
    }
}
//100
//0 32 52 12 28
//0 24 40 4 25
//0 36 36 4 27
//0 14 11 2 15
//1 4 11 6 17 4
//1 33 24 4 18 21
//0 10 49 10 14
//0 4 4 3 29
//1 45 59 12 15 34
//0 34 17 9 28
//0 35 11 11 20
//1 46 59 19 8 24
//0 22 4 17 13
//0 34 19 12 18
//0 39 58 11 6
//1 15 35 9 23 1
//0 27 56 3 4
//0 22 34 11 21
//1 28 49 2 27 25
//1 20 4 19 3 30
//0 34 33 3 29
//1 11 50 7 24 23
//0 27 51 11 0
//1 1 48 1 8 15
//1 38 54 8 3 6
//0 13 5 9 27
//0 5 22 17 22
//1 2 33 2 7 23
//0 31 51 13 14
//1 43 21 17 25 38
//0 12 44 15 22
//1 1 57 12 15 31
//0 21 55 14 17
//0 33 4 0 1
//0 12 25 19 6
//1 31 12 2 24 38
//1 12 1 11 16 13
//0 8 22 9 19
//0 6 52 7 5
//0 8 39 14 23
//1 4 46 7 27 8
//0 6 8 9 0
//0 8 57 5 6
//0 26 28 8 21
//1 6 43 4 12 30
//1 48 32 9 13 28
//0 30 0 7 16
//1 3 19 18 5 31
//0 42 30 18 4
//1 8 2 18 19 5
//0 8 24 14 7
//0 16 8 3 11
//0 42 47 6 20
//1 17 16 14 28 0
//0 15 36 6 22
//1 23 35 4 27 10
//0 43 15 17 23
//0 38 4 3 19
//0 5 35 12 23
//1 30 26 10 9 16
//1 48 21 14 27 3
//0 21 3 12 2
//0 45 58 1 19
//1 5 23 0 24 19
//0 21 53 19 23
//0 26 52 17 7
//0 15 12 17 23
//0 29 3 3 21
//1 21 47 8 8 0
//1 38 23 13 19 33
//1 36 49 17 6 21
//0 27 11 10 15
//0 21 42 0 18
//0 17 59 18 27
//1 18 13 18 15 32
//0 34 8 10 18
//0 9 52 17 21
//0 33 35 9 23
//1 31 56 15 19 27
//0 26 9 19 5
//1 9 9 3 22 27
//0 6 48 11 12
//0 47 33 1 1
//1 15 8 18 26 4
//1 28 43 4 15 15
//0 2 31 12 16
//from here doesnt need to print down
//0 1 53 1 6
//1 16 1 18 7 33
//0 4 38 13 17
//0 41 27 10 10
//1 13 38 2 10 14
//1 47 26 16 29 16
//1 36 15 7 20 30
//0 28 25 2 25
//1 0 52 0 19 27
//0 7 59 8 1
//0 26 3 5 5
//0 15 19 2 21
//0 28 22 16 14
//0 17 23 17 18