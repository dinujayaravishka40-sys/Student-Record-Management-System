import java.util.Scanner;


class Student {
    int id;
    String name;
    double gpa;
    Student next; // Linked List

    public Student(int id, String name, double gpa) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.next = null;
    }
}

// 2. STACK IMPLEMENTATION 
class UndoStack {
    private Student top;

    public void push(Student student) {
        Student temp = new Student(student.id, student.name, student.gpa);
        temp.next = top;
        top = temp;
    }

    public Student pop() {
        if (top == null) return null;
        Student popped = top;
        top = top.next;
        return popped;
    }
}

// 3. QUEUE IMPLEMENTATION (Registration Waiting List 
class RegistrationQueue {
    private Student front, rear;

    public void enqueue(int id, String name, double gpa) {
        Student newStudent = new Student(id, name, gpa);
        if (rear == null) {
            front = rear = newStudent;
            return;
        }
        rear.next = newStudent;
        rear = newStudent;
    }

    public Student dequeue() {
        if (front == null) return null;
        Student temp = front;
        front = front.next;
        if (front == null) rear = null;
        return temp;
    }
}

// 4. MAIN SYSTEM CLASS (Linked List
public class StudentManagementSystem {
    private Student head = null; 
    private UndoStack undoStack = new UndoStack();
    private RegistrationQueue regQueue = new RegistrationQueue();

    // 1. Queue  (Waiting List)
    public void addToWaitingList(int id, String name, double gpa) {
        regQueue.enqueue(id, name, gpa);
        System.out.println("Student added to registration waiting queue!");
    }

    // 2. Queue (Linked List) (FIFO)
    public void processNextRegistration() {
        Student nextInQueue = regQueue.dequeue();
        if (nextInQueue == null) {
            System.out.println("No students waiting in the queue.");
            return;
        }

        if (head == null) {
            head = nextInQueue;
        } else {
            Student temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = nextInQueue;
        }

        undoStack.push(nextInQueue);
        System.out.println("Successfully registered: " + nextInQueue.name);
    }

    // 3. (Undo - Stack LIFO)
    public void undoLastRegistration() {
        Student lastRegistered = undoStack.pop();
        if (lastRegistered == null) {
            System.out.println("Nothing to undo!");
            return;
        }

        if (head == null) return;

        if (head.id == lastRegistered.id) {
            head = head.next;
            System.out.println("Registration undone for: " + lastRegistered.name);
            return;
        }

        Student prev = null;
        Student curr = head;
        while (curr != null && curr.id != lastRegistered.id) {
            prev = curr;
            curr = curr.next;
        }

        if (curr != null) {
            prev.next = curr.next;
            System.out.println("Registration undone for: " + lastRegistered.name);
        }
    }

    // 4. (Linear Search Algorithm)
    public void searchStudent(int id) {
        Student temp = head;
        while (temp != null) {
            if (temp.id == id) {
                System.out.println("Found! Name: " + temp.name + ", GPA: " + temp.gpa);
                return;
            }
            temp = temp.next;
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    // 5. NEW FEATURE: GPA   (Descending Order)
    public void sortByGPA() {
        if (head == null || head.next == null) {
            return; 
        }

        boolean swapped;
        Student ptr1;
        Student lptr = null;

        do {
            swapped = false;
            ptr1 = head;

            while (ptr1.next != lptr) {
                if (ptr1.gpa < ptr1.next.gpa) {
                    //  (Swap)
                    int tempId = ptr1.id;
                    String tempName = ptr1.name;
                    double tempGpa = ptr1.gpa;

                    ptr1.id = ptr1.next.id;
                    ptr1.name = ptr1.next.name;
                    ptr1.gpa = ptr1.next.gpa;

                    ptr1.next.id = tempId;
                    ptr1.next.name = tempName;
                    ptr1.next.gpa = tempGpa;

                    swapped = true;
                }
                ptr1 = ptr1.next;
            }
            lptr = ptr1;
        } while (swapped);

        System.out.println("Students sorted by GPA successfully!");
    }

    public void displayAllStudents() {
        if (head == null) {
            System.out.println("No registered students to display.");
            return;
        }
        Student temp = head;
        System.out.println("\n--- Registered Students ---");
        while (temp != null) {
            System.out.println("ID: " + temp.id + " | Name: " + temp.name + " | GPA: " + temp.gpa);
            temp = temp.next;
        }
    }

    // MAIN METHOD - Menu driven console interface
    public static void main(String[] args) {
        StudentManagementSystem system = new StudentManagementSystem();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student to Waiting Queue (Queue)");
            System.out.println("2. Process Next Registration (Linked List)");
            System.out.println("3. Undo Last Registration (Stack)");
            System.out.println("4. Search Student by ID (Linear Search)");
            System.out.println("5. Sort Students by GPA (Bubble Sort)");
            System.out.println("6. Display All Registered Students");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter GPA: ");
                    double gpa = scanner.nextDouble();
                    system.addToWaitingList(id, name, gpa);
                    break;
                case 2:
                    system.processNextRegistration();
                    break;
                case 3:
                    system.undoLastRegistration();
                    break;
                case 4:
                    System.out.print("Enter ID to search: ");
                    int searchId = scanner.nextInt();
                    system.searchStudent(searchId);
                    break;
                case 5:
                    system.sortByGPA();
                    break;
                case 6:
                    system.displayAllStudents();
                    break;
                case 7:
                    System.out.println("Exiting System. Good Luck with your Assignment!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 7);

        scanner.close();
    }
}