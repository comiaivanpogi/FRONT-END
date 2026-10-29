package autolib;

import autolib.models.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * In-memory data store — replace method bodies with real DB calls
 * when backend is ready.
 */
public class DataStore {

    private static DataStore instance;
    private List<Book> books = new ArrayList<>();
    private List<Student> students = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private int nextBookSeq = 1249;

    private DataStore() {
        seed();
    }

    public static DataStore get() {
        if (instance == null)
            instance = new DataStore();
        return instance;
    }

    // ── Seed data ─────────────────────────────────────────────────────────────
    private void seed() {
        books.add(new Book("BK-00492", "Data Structures & Algorithms", "Cormen et al.", "QA76.9", "Science & Tech", 5,
                2));
        books.add(new Book("BK-00211", "Chemistry for Engineers", "Zumdahl, S.", "QD11.3", "Engineering", 3, 0));
        books.add(new Book("BK-00387", "Engineering Mathematics Vol. 2", "Bird, J.", "QA37.3", "Engineering", 4, 1));
        books.add(new Book("BK-00501", "Fundamentals of Physics", "Halliday et al.", "QC21.3", "Science & Tech", 6, 4));
        books.add(new Book("BK-00652", "Introduction to Psychology", "Myers, D.", "BF121", "Humanities", 2, 1));
        books.add(new Book("BK-00798", "Calculus: Early Transcendentals", "Stewart, J.", "QA303.2", "Science & Tech", 5,
                3));

        students.add(new Student("2024-10042", "Santos, Maria L.", "CICS", "3rd Year", true, 1, 0));
        students.add(new Student("2023-10087", "Reyes, Ana C.", "COE", "4th Year", true, 1, 1));
        students.add(new Student("2025-10015", "Cruz, Jose P.", "CAS", "2nd Year", true, 0, 0));
        students.add(new Student("2024-20093", "Torres, Ben M.", "COE", "3rd Year", false, 2, 1));
        students.add(new Student("2023-30061", "Lim, Carlos R.", "COED", "4th Year", true, 1, 0));

        LocalDateTime now = LocalDateTime.now();
        transactions.add(new Transaction("Santos, Maria L.", "2024-10042", "Data Structures & Algorithms", "BK-00492",
                Transaction.Type.BORROWED, now.minusMinutes(20)));
        transactions.add(new Transaction("Cruz, Jose P.", "2025-10015", "Engineering Mathematics Vol. 2", "BK-00387",
                Transaction.Type.RETURNED, now.minusMinutes(38)));
        transactions.add(new Transaction("Reyes, Ana C.", "2023-10087", "Chemistry for Engineers", "BK-00211",
                Transaction.Type.OVERDUE, now.minusDays(1), 5));
        transactions.add(new Transaction("Lim, Carlos R.", "2023-30061", "Introduction to Psychology", "BK-00652",
                Transaction.Type.BORROWED, now.minusMinutes(63)));
        transactions.add(new Transaction("Dela Cruz, J.B.", "2022-40011", "Calculus: Early Transcendentals", "BK-00798",
                Transaction.Type.RETURNED, now.minusMinutes(74)));
    }

    // ── Books ─────────────────────────────────────────────────────────────────
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public void addBook(Book b) {
        books.add(b);
    }

    public void removeBook(int idx) {
        if (idx >= 0 && idx < books.size())
            books.remove(idx);
    }

    public String nextBarcode() {
        return String.format("BK-%05d", nextBookSeq++);
    }

    public List<Book> searchBooks(String q, String section, String status) {
        List<Book> r = new ArrayList<>();
        String lq = q == null ? "" : q.toLowerCase().trim();
        for (Book b : books) {
            boolean matchQ = lq.isEmpty()
                    || b.getTitle().toLowerCase().contains(lq)
                    || b.getAuthor().toLowerCase().contains(lq)
                    || b.getBarcode().toLowerCase().contains(lq)
                    || b.getCallNumber().toLowerCase().contains(lq);
            boolean matchS = section == null || section.equals("All Sections") || b.getSection().equals(section);
            boolean matchSt = status == null || status.equals("All Status")
                    || b.getStatusText().equalsIgnoreCase(status.trim());
            if (matchQ && matchS && matchSt)
                r.add(b);
        }
        return r;
    }

    // ── Students ──────────────────────────────────────────────────────────────
    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public void addStudent(Student s) {
        students.add(s);
    }

    public void removeStudent(int idx) {
        if (idx >= 0 && idx < students.size())
            students.remove(idx);
    }

    public List<Student> searchStudents(String q, String dept) {
        List<Student> r = new ArrayList<>();
        String lq = q == null ? "" : q.toLowerCase().trim();
        for (Student s : students) {
            boolean matchQ = lq.isEmpty()
                    || s.getName().toLowerCase().contains(lq)
                    || s.getStudentId().toLowerCase().contains(lq)
                    || s.getDepartment().toLowerCase().contains(lq);
            boolean matchD = dept == null || dept.equals("All Departments") || s.getDepartment().equals(dept);
            if (matchQ && matchD)
                r.add(s);
        }
        return r;
    }

    // ── Transactions ──────────────────────────────────────────────────────────
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void addTransaction(Transaction t) {
        transactions.add(0, t);
    }

    // ── Dashboard stats ───────────────────────────────────────────────────────
    public int totalBooks() {
        return 1248;
    }

    public int currentlyBorrowed() {
        return 87;
    }

    public int overdueBooks() {
        return 14;
    }

    public int registeredStudents() {
        return 342;
    }

    public int[] weeklyBorrowData() {
        return new int[] { 18, 22, 15, 28, 20, 10, 5 };
    }

    public int[] monthlyBorrowData() {
        return new int[] { 145, 178, 132, 210, 190, 88, 55 };
    }

    public List<Book> mostBorrowed() {
        List<Book> r = new ArrayList<>(books);
        r.sort((a, b) -> b.getBorrowedCount() - a.getBorrowedCount());
        return r.subList(0, Math.min(5, r.size()));
    }

    public List<Transaction> overdueAlerts() {
        List<Transaction> r = new ArrayList<>();
        for (Transaction t : transactions)
            if (t.getType() == Transaction.Type.OVERDUE)
                r.add(t);
        return r;
    }

    public List<String> getSections() {
        Set<String> set = new LinkedHashSet<>();
        for (Book b : books)
            set.add(b.getSection());
        return new ArrayList<>(set);
    }

    public List<String> getDepartments() {
        Set<String> set = new LinkedHashSet<>();
        for (Student s : students)
            set.add(s.getDepartment());
        return new ArrayList<>(set);
    }
}
