import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student {
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private List<Contact> contactList;


    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        this.contactList=new ArrayList<Contact>();
    }
    public void addEmailContact(String date, String email) {
        contactList.add(new EmailContact(date,email));
    }
    public void addPhoneContact(String date, String phone) {
        contactList.add(new PhoneContact(date,phone));
    }
    public String getCity() {
        return city;
    }
    public long getIndex(){
        return index;
    }
    public String getFullName(){
        return this.firstName+" "+this.lastName;
    }

    public Contact getLatestContact() {
        Contact newest=contactList.get(0);
        for (Contact c:contactList){
            if (c.isNewerThan(newest)) newest=c;
        }
        return newest;
    }
    public int getContactLengh(){
        return contactList.size();
    }
    public Contact[] getEmailContacts() {
        List<Contact> emailContacts=new ArrayList<Contact>();
        for (Contact c: contactList){
            if(Objects.equals(c.getType(), "Email")) emailContacts.add(c);
        }
        return emailContacts.toArray(new Contact[emailContacts.size()]);
    }

    public Contact[] getPhoneContacts() {
        List<Contact> phoneContacts=new ArrayList<Contact>();
        for (Contact c:contactList){
            if (Objects.equals(c.getType(), "Phone")) phoneContacts.add(c);
        }
        return phoneContacts.toArray(new Contact[phoneContacts.size()]);
    }

    @Override
    public String toString() {
        String str ="{"+
                "\"ime\":\"" + firstName + "\", " +
                "\"prezime\":\"" + lastName + "\", " +
                "\"vozrast\":" + age + ", " +
                "\"grad\":\"" + city + "\", " +
                "\"indeks\":" + index + ", " +
                "\"telefonskiKontakti\":[";
        for(Contact c:this.getPhoneContacts()){
            PhoneContact phoneContact=(PhoneContact) c;
            str=str.concat("\""+phoneContact.getPhone()+ "\"");
            str+=",";
        }
        if (this.getPhoneContacts().length>0)str=str.substring(0,str.length()-2);
        str+="], \"emailKontakti\":[";
        for(Contact c: this.getEmailContacts()){
            EmailContact emailContact=(EmailContact) c;
            str=str.concat("\""+emailContact.getEmail()+"\"");
            str+=",";
        }
        if(this.getEmailContacts().length>0) str=str.substring(0,str.length()-2);
        str+="]}";
        return str;
    }
}
