package autolib.models;

public class Student {
    private String studentId;
    private String name;
    private String department;
    private String year;
    private boolean nfcRegistered;
    private boolean nfcPending;
    private int activeLoans;
    private int overdueCount;

    public Student(String studentId, String name, String department, String year,
            boolean nfcRegistered, int activeLoans, int overdueCount) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.year = year;
        this.nfcRegistered = nfcRegistered;
        this.nfcPending = !nfcRegistered;
        this.activeLoans = activeLoans;
        this.overdueCount = overdueCount;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String s) {
        this.studentId = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String d) {
        this.department = d;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String y) {
        this.year = y;
    }

    public boolean isNfcRegistered() {
        return nfcRegistered;
    }

    public void setNfcRegistered(boolean r) {
        this.nfcRegistered = r;
        this.nfcPending = !r;
    }

    public boolean isNfcPending() {
        return nfcPending;
    }

    public int getActiveLoans() {
        return activeLoans;
    }

    public void setActiveLoans(int a) {
        this.activeLoans = a;
    }

    public int getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(int o) {
        this.overdueCount = o;
    }

    public String getNfcStatusText() {
        return nfcRegistered ? "Yes" : "Pending";
    }
}
