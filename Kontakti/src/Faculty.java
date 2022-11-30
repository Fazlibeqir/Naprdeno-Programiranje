public class Faculty {
    private String name;
    private Student [] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }
    public int countStudentsFromCity(String cityName){
        int counter =0;
        for(Student student:students){
            if(student.getCity().compareTo(cityName)==0)counter++;
        }
        return counter;
    }

    public Student getStudent(long index) {
        for(Student student: students){
            if(student.getIndex()==index) return student;
        }
        return null;
    }

    public double getAverageNumberOfContacts() {
       double avg = 0.0;
       for (Student student: students){
           avg+=student.getContactLengh();
       }
       return avg/ students.length;
    }

    public Student getStudentWithMostContacts() {
        Student maxContactsSudent=students[0];
        for(Student student:students){
            if(student.getContactLengh()>maxContactsSudent.getContactLengh())maxContactsSudent=student;
            else if(student.getContactLengh()==maxContactsSudent.getContactLengh()){
                if (student.getIndex()>maxContactsSudent.getIndex())maxContactsSudent=student;
            }
        }
        return maxContactsSudent;
    }

    @Override
    public String toString() {
        StringBuilder str= new StringBuilder("{" +
                "\"fakultet\":\"" + name + "\"," +
                "\"studenti\":[");
        for(Student student:students){
            str.append(student.toString()).append(",");
        }
        if(students.length>0) str = new StringBuilder(str.substring(0, str.length() - 2));
        str.append("]}");
        return str.toString();
    }
}
