package autolib.models;

public class Book {
    private String barcode;
    private String title;
    private String author;
    private String callNumber;
    private String section;
    private int totalCopies;
    private int availableCopies;
    private String isbn;
    private String deweyDecimal;

    public enum Status {
        AVAILABLE, BORROWED, ALL_OUT
    }

    public Book(String barcode, String title, String author, String callNumber,
            String section, int totalCopies, int availableCopies) {
        this.barcode = barcode;
        this.title = title;
        this.author = author;
        this.callNumber = callNumber;
        this.section = section;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String b) {
        this.barcode = b;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String a) {
        this.author = a;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String c) {
        this.callNumber = c;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String s) {
        this.section = s;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int t) {
        this.totalCopies = t;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int a) {
        this.availableCopies = a;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String i) {
        this.isbn = i;
    }

    public String getDeweyDecimal() {
        return deweyDecimal;
    }

    public void setDeweyDecimal(String d) {
        this.deweyDecimal = d;
    }

    public int getBorrowedCount() {
        return totalCopies - availableCopies;
    }

    public Status getStatus() {
        if (availableCopies == 0)
            return Status.ALL_OUT;
        if (availableCopies < totalCopies)
            return Status.BORROWED;
        return Status.AVAILABLE;
    }

    public String getStatusText() {
        switch (getStatus()) {
            case ALL_OUT:
                return "All Out";
            case BORROWED:
                return "Borrowed";
            default:
                return "Available";
        }
    }
}
