enum Operator{VIP, ONE , TMOBILE};
public class PhoneContact extends Contact {
    String phone;
    private  Operator operator;
    public PhoneContact(String date,String phone) {
        super(date);
        int num= Integer.parseInt(""+phone.charAt(2));
        if(num==0 || num==1 || num==2) operator=Operator.TMOBILE;
        if(num==5 || num==6 ) operator=Operator.ONE;
        if(num==7 || num==8) operator=Operator.VIP;
        this.phone=phone;
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operator;
    }


    @Override
    public String getType() {
        return "Phone";
    }
}
