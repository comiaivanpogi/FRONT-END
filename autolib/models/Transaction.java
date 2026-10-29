package autolib.models;

import java.time.LocalDateTime;

public class Transaction {
    public enum Type {
        BORROWED, RETURNED, OVERDUE
    }

    private String studentName;
    private String studentId;
    private String bookTitle;
    private String bookBarcode;
    private Type type;
    private LocalDateTime timestamp;
    private int daysOverdue;

    public Transaction(String studentName, String studentId, String bookTitle,
            String bookBarcode, Type type, LocalDateTime timestamp) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.bookTitle = bookTitle;
        this.bookBarcode = bookBarcode;
        this.type = type;
        this.timestamp = timestamp;
        this.daysOverdue = 0;
    }

    public Transaction(String studentName, String studentId, String bookTitle,
            String bookBarcode, Type type, LocalDateTime timestamp, int daysOverdue) {
        this(studentName, studentId, bookTitle, bookBarcode, type, timestamp);
        this.daysOverdue = daysOverdue;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookBarcode() {
        return bookBarcode;
    }

    public Type getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getDaysOverdue() {
        return daysOverdue;
    }

    public String getStudentInitial() {
        if (studentName == null || studentName.isEmpty())
            return "?";
        return String.valueOf(studentName.charAt(0)).toUpperCase();
    }

    public String getTypeText() {
        switch (type) {
            case BORROWED:
                return "Borrowed";
            case RETURNED:
                return "Returned";
            case OVERDUE:
                return "Overdue";
            default:
                return "";
        }
    }
}
