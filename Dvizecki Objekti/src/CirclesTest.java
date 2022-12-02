import java.util.*;


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
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
}
class MovablePoint implements Movable{
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
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
    public void moveLeft() throws ObjectCanNotBeMovedException{
        canMove(-xSpeed,0);
        x-=xSpeed;
    }
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException{
        canMove(xSpeed,0);
        x+=xSpeed;
    }
    @Override
    public int getCurrentXPosition() {
        return x;
    }
    @Override
    public int getCurrentYPosition() {
        return y;
    }
    public int getxSpeed() {
        return xSpeed;
    }
    public int getySpeed() {
        return ySpeed;
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
    public void moveLeft() throws ObjectCanNotBeMovedException {
    center.moveLeft();
    }
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        center.moveRight();
    }
    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }
    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }
    public int getRadius() {
        return radius;
    }
    public MovablePoint getCenter() {
        return center;
    }
    @Override
    public String toString() {
        return "Movable circle with center coordinates ("+center.getCurrentXPosition()+","+center.getCurrentYPosition()+") and radius "+radius;
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
        this.movables = Arrays.asList(movables);
        MovablesCollection.x_MAX =x_MAX;
        MovablesCollection.y_MAX =y_MAX;
    }
    public MovablesCollection(Movable[] movables,int x_MAX){
        this.movables=Arrays.asList(movables);
        this.x_MAX =x_MAX;
        y_MAX=100;
    }
    public MovablesCollection(Movable[] movables){
        this.movables=Arrays.asList(movables);
        x_MAX=100;
        y_MAX=100;
    }
    public MovablesCollection(int x_MAX,int y_MAX){
        this.movables=new ArrayList<Movable>();
        this.x_MAX =x_MAX;
        this.y_MAX =y_MAX;
    }
    public static void setxMax(int x_MAX){
        MovablesCollection.x_MAX=x_MAX;
    }
    public static void setyMax(int y_MAX){
        MovablesCollection.y_MAX=y_MAX;
    }
    public void addMovableObject(Movable m)throws MovableObjectNotFittableException{
        canExistMovable(m);
        movables.add(m);
    }
    public boolean canExistMovable(Movable m) throws MovableObjectNotFittableException{
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
        String str = "Collection of movable objects with size " + movables.size() + ":\n";
        for(Movable mv:movables){
            str+=(mv.toString())+'\n';
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
