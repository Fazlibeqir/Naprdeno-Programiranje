import java.util.Date;

public abstract class Contact {
  private Date date;

    public Contact(String date) {
        String [] splited=date.split("-");
        this.date = new Date(Integer.parseInt(splited[0]),Integer.parseInt(splited[1]),Integer.parseInt((splited[2])));
    }

    public abstract String getType();

  boolean isNewerThan(Contact c) {
      return this.date.after(c.date);
  }
}
