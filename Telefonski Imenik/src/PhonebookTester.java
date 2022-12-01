import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Comparator;

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch (line) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine())
            phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook, text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if (!pb.equals(phonebook)) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine()) {
            String command = jin.nextLine();
            switch (command) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook);
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while (jin.hasNextLine()) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        } catch (InvalidNameException e) {
            System.out.println(e.name);
            exception_thrown = true;
        } catch (Exception e) {
        }
        if (!exception_thrown) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String[] names_to_test = {"And\nrej", "asd", "AAAAAAAAAAAAAAAAAAAAAA", "Ð�Ð½Ð´Ñ€ÐµÑ˜A123213", "Andrej#", "Andrej<3"};
        for (String name : names_to_test) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if (!exception_thrown) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String[] numbers_to_test = {"+071718028", "number", "078asdasdasd", "070asdqwe", "070a56798", "07045678a", "123456789", "074456798", "073456798", "079456798"};
        for (String number : numbers_to_test) {
            try {
                new Contact("Andrej", number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if (!exception_thrown)
                System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String[] nums = new String[10];
        for (int i = 0; i < nums.length; ++i) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej", nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if (!exception_thrown)
            System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej", getRandomLegitNumber(rnd), getRandomLegitNumber(rnd), getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact);
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact);
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact);
    }

    static String[] legit_prefixes = {"070", "071", "072", "075", "076", "077", "078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for (int i = 3; i < 9; ++i)
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }

}
class Contact{
    private String name;
    private ArrayList<String> phoneNumbers;

    public Contact(String name, String... phoneNumber) throws Exception {
        this.name = name;
        if (!name.matches("[A-Za-z0-9]{5,10}")) throw new InvalidNameException(name);
        phoneNumbers = new ArrayList<>();
        for (String s : phoneNumber) addNumber(s);
    }

    static boolean validatePhoneNumber(String phone) {
        return phone.matches("^07[0125678][0-9]{6}$");
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        return phoneNumbers.stream().sorted().toArray(String[]::new);
    }

    public void addNumber(String phonenumber) throws Exception {
        if (phoneNumbers.size() == 5) throw new MaximumSizeExceddedException();
        if (validatePhoneNumber(phonenumber)) {
            phoneNumbers.add(phonenumber);
        } else throw new InvalidNumberException();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        stringBuilder.append("\n");
        stringBuilder.append(this.phoneNumbers.size());
        stringBuilder.append("\n");
        for (String i : getNumbers()) {
            stringBuilder.append(i);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
class InvalidNameException extends Exception {
    String name;

    public InvalidNameException(String name) {
        super();
        this.name = name;
    }
}
class InvalidNumberException extends Exception {
    public InvalidNumberException() {
        super();
    }
}

class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
        super();
    }
}
class PhoneBook {
    Contact[] contacts;

    public PhoneBook() {
        contacts = new Contact[0];
    }

    public void addContact(Contact contact) throws Exception {
        if (contacts.length == 250) throw new MaximumSizeExceddedException();
        if (contactExists(contact.getName())) throw new InvalidNameException(contact.getName());

        Contact[] temp = Arrays.copyOf(contacts, contacts.length + 1);
        temp[temp.length - 1] = contact;
        contacts = temp;
    }

    public boolean contactExists(String contact) {
        return Arrays.stream(contacts).anyMatch(x -> x.getName().equals(contact));
    }

    public Contact getContactForName(String name) {
        if (contactExists(name)) {
            return Arrays.stream(contacts).filter(x -> x.getName().equals(name))
                    .findFirst().get();
        }
        return null;
    }

    public int numberOfContacts() {
        return contacts.length;
    }

    public Contact[] getContacts() {
        return getSortedContacts();
    }

    public boolean removeContact(String name) {
        if (contactExists(name)) {
            contacts = Arrays.stream(contacts)
                    .filter(x -> !x.getName()
                            .equals(name)).toArray(Contact[]::new);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Contact[] sortedContacts = getSortedContacts();
        for (Contact c : sortedContacts) {
            stringBuilder.append(c);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public Contact[] getSortedContacts() {
        return Arrays.stream(contacts).sorted(Comparator.comparing(Contact::getName)).toArray(Contact[]::new);
    }

    public static boolean saveAsTextFile(PhoneBook phoneBook, String path) {
        return true;
    }

    public static PhoneBook loadFromTextFile(String path) {
        return new PhoneBook();
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        return Arrays.stream(contacts)
                .filter(x -> Arrays.stream(x.getNumbers())
                        .anyMatch(y -> y.startsWith(number_prefix))).toArray(Contact[]::new);
    }


}

