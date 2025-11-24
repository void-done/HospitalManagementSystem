import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
public class HospitalManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Patient> patients = new ArrayList<>();
    private static final List<Doctor> doctors = new ArrayList<>();
    private static final List<Appointment> appointments = new ArrayList<>();

    private static int patientIdSeq = 1000;
    private static int doctorIdSeq = 500;
    private static int appointmentIdSeq = 2000;

    public static void main(String[] args) {
        seedSampleData();
        showMainMenu();
    }

    private static void seedSampleData() {
        doctors.add(new Doctor(nextDoctorId(), "Dr. Aisha Khan", "Cardiology"));
        doctors.add(new Doctor(nextDoctorId(), "Dr. Rahul Verma", "Orthopedics"));

        patients.add(new Patient(nextPatientId(), "Ravi Patel", 35, "M"));
        patients.add(new Patient(nextPatientId(), "Sonia Mehta", 28, "F"));
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== Hospital Management System ===");
            System.out.println("1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Appointment Management");
            System.out.println("4. Billing");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> patientMenu();
                case "2" -> doctorMenu();
                case "3" -> appointmentMenu();
                case "4" -> billingMenu();
                case "0" -> {
                    System.out.println("Goodbye!"); return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // -------------------- Patient --------------------
    private static void patientMenu() {
        while (true) {
            System.out.println("\n--- Patient Management ---");
            System.out.println("1. Add Patient");
            System.out.println("2. List Patients");
            System.out.println("3. Find Patient by ID");
            System.out.println("4. Update Patient");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> addPatient();
                case "2" -> listPatients();
                case "3" -> findPatientById();
                case "4" -> updatePatient();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private static void addPatient() {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Age: ");
        int age = readIntWithDefault(0);
        System.out.print("Gender (M/F/O): ");
        String gender = scanner.nextLine().trim();

        Patient p = new Patient(nextPatientId(), name, age, gender);
        patients.add(p);
        System.out.println("Added patient: " + p);
    }

    private static void listPatients() {
        System.out.println("\nPatients:");
        if (patients.isEmpty()) System.out.println("No patients.");
        for (Patient p : patients) System.out.println(p);
    }

    private static void findPatientById() {
        System.out.print("Enter patient ID: ");
        int id = readIntWithDefault(-1);
        Patient p = findPatient(id);
        if (p == null) System.out.println("Patient not found.");
        else System.out.println(p.detailedString());
    }

    private static void updatePatient() {
        System.out.print("Enter patient ID to update: ");
        int id = readIntWithDefault(-1);
        Patient p = findPatient(id);
        if (p == null) { System.out.println("Not found."); return; }
        System.out.println("Updating patient: " + p);
        System.out.print("New name (leave blank to keep): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) p.setName(name);
        System.out.print("New age (leave blank to keep): ");
        String ageStr = scanner.nextLine().trim();
        if (!ageStr.isEmpty()) {
            try { p.setAge(Integer.parseInt(ageStr)); } catch (NumberFormatException e) { System.out.println("Invalid age ignored."); }
        }
        System.out.print("New gender (leave blank to keep): ");
        String g = scanner.nextLine().trim();
        if (!g.isEmpty()) p.setGender(g);

        System.out.println("Updated: " + p);
    }

    private static Patient findPatient(int id) {
        for (Patient p : patients) if (p.getId() == id) return p;
        return null;
    }

    // -------------------- Doctor --------------------
    private static void doctorMenu() {
        while (true) {
            System.out.println("\n--- Doctor Management ---");
            System.out.println("1. Add Doctor");
            System.out.println("2. List Doctors");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> addDoctor();
                case "2" -> listDoctors();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private static void addDoctor() {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Specialization: ");
        String spec = scanner.nextLine().trim();
        Doctor d = new Doctor(nextDoctorId(), name, spec);
        doctors.add(d);
        System.out.println("Added doctor: " + d);
    }

    private static void listDoctors() {
        System.out.println("\nDoctors:");
        if (doctors.isEmpty()) System.out.println("No doctors.");
        for (Doctor d : doctors) System.out.println(d);
    }

    private static Doctor findDoctor(int id) {
        for (Doctor d : doctors) if (d.getId() == id) return d;
        return null;
    }

    // -------------------- Appointment --------------------
    private static void appointmentMenu() {
        while (true) {
            System.out.println("\n--- Appointment Management ---");
            System.out.println("1. Schedule Appointment");
            System.out.println("2. List Appointments");
            System.out.println("3. Cancel Appointment");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();
            switch (c) {
                case "1" -> scheduleAppointment();
                case "2" -> listAppointments();
                case "3" -> cancelAppointment();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private static void scheduleAppointment() {
        System.out.print("Patient ID: ");
        int pid = readIntWithDefault(-1);
        Patient p = findPatient(pid);
        if (p == null) { System.out.println("Patient not found."); return; }

        System.out.print("Doctor ID: ");
        int did = readIntWithDefault(-1);
        Doctor d = findDoctor(did);
        if (d == null) { System.out.println("Doctor not found."); return; }

        System.out.print("Enter date and time (yyyy-MM-dd HH:mm), e.g. 2025-10-31 14:30: ");
        String dt = scanner.nextLine().trim();
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            System.out.println("Invalid date/time format. Appointment not scheduled.");
            return;
        }

        Appointment ap = new Appointment(nextAppointmentId(), p, d, dateTime);
        appointments.add(ap);
        System.out.println("Scheduled: " + ap);
    }

    private static void listAppointments() {
        System.out.println("\nAppointments:");
        if (appointments.isEmpty()) System.out.println("No appointments.");
        for (Appointment a : appointments) System.out.println(a);
    }

    private static void cancelAppointment() {
        System.out.print("Enter appointment ID to cancel: ");
        int id = readIntWithDefault(-1);
        Appointment found = null;
        for (Appointment a : appointments) if (a.getId() == id) { found = a; break; }
        if (found == null) { System.out.println("Not found."); return; }
        appointments.remove(found);
        System.out.println("Cancelled appointment " + id);
    }

    // -------------------- Billing --------------------
    private static void billingMenu() {
        System.out.println("\n--- Billing ---");
        System.out.println("1. Generate bill for appointment");
        System.out.println("2. List bills for a patient");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        String c = scanner.nextLine().trim();
        switch (c) {
            case "1" -> generateBill();
            case "2" -> listBillsForPatient();
            case "0" -> {
            }

            default -> System.out.println("Invalid.");
        }
    }

    private static void generateBill() {
        System.out.print("Appointment ID: ");
        int id = readIntWithDefault(-1);
        Appointment a = null;
        for (Appointment ap : appointments) if (ap.getId() == id) { a = ap; break; }
        if (a == null) { System.out.println("Appointment not found."); return; }

        // Simple billing logic: base consultation fee + specialization surcharge
        double base = 500.0;
        double surcharge = 0.0;
        String spec = a.getDoctor().getSpecialization().toLowerCase();
        if (spec.contains("cardio") || spec.contains("cardio")) surcharge = 700.0;
        else if (spec.contains("ortho")) surcharge = 300.0;
        else surcharge = 100.0;
        double total = base + surcharge;

        System.out.println("\n--- Bill ---");
        System.out.println("Appointment: " + a.getId());
        System.out.println("Patient: " + a.getPatient().getName());
        System.out.println("Doctor: " + a.getDoctor().getName() + " (" + a.getDoctor().getSpecialization() + ")");
        System.err.println("Base fee : INR" + base);
        System.err.println("Surcharge: INR" + surcharge);
        System.out.printf("Amount due: INR %.2f\n", total);
        System.out.println("(This is a demo bill. Extend billing for tests, medicines, procedures.)");
    }

    private static void listBillsForPatient() {
        System.out.print("Enter patient ID: ");
        int pid = readIntWithDefault(-1);
        Patient p = findPatient(pid);
        if (p == null) { System.out.println("Patient not found."); return; }
        System.out.println("\nBills for " + p.getName() + ":");
        boolean any = false;
        for (Appointment a : appointments) {
            if (a.getPatient().getId() == pid) {
                any = true;
                System.out.printf("Appt %d with Dr.%s on %s\n", a.getId(), a.getDoctor().getName(), a.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        }
        if (!any) System.out.println("No appointments/bills found.");
    }

    // -------------------- Helpers --------------------
    private static int nextPatientId() { return patientIdSeq++; }
    private static int nextDoctorId() { return doctorIdSeq++; }
    private static int nextAppointmentId() { return appointmentIdSeq++; }

    private static int readIntWithDefault(int def) {
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return def;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }
}

// -------------------- Model classes --------------------
class Patient {
    private final int id;
    private String name;
    private int age;
    private String gender;

    public Patient(int id, String name, int age, String gender) {
        this.id = id; this.name = name; this.age = age; this.gender = gender;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }

    @Override public String toString() { return String.format("[%d] %s, %d yrs, %s", id, name, age, gender); }
    public String detailedString() { return "Patient ID: " + id + "\nName: " + name + "\nAge: " + age + "\nGender: " + gender; }
}

class Doctor {
    private final int id;
    private final String name;
    private final String specialization;

    public Doctor(int id, String name, String specialization) {
        this.id = id; this.name = name; this.specialization = specialization;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }

    @Override public String toString() { return String.format("[%d] %s (%s)", id, name, specialization); }
}

class Appointment {
    private final int id;
    private final Patient patient;
    private final Doctor doctor;
    private final LocalDateTime dateTime;

    public Appointment(int id, Patient patient, Doctor doctor, LocalDateTime dateTime) {
        this.id = id; this.patient = patient; this.doctor = doctor; this.dateTime = dateTime;
    }

    public int getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDateTime getDateTime() { return dateTime; }

    @Override public String toString() {
        return String.format("[%d] %s -> %s at %s", id, patient.getName(), doctor.getName(), dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}
