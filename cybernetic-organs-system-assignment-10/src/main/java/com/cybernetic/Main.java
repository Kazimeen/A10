package com.cybernetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
            // Create diagnostic tree with BST structure
            DiagnosticDecisionTree diagnosticTree = createDiagnosticTree();

            // Define the number of random patients to generate
            int numberOfPatients = 5; // You can change this number as needed

            // Create sample patients with randomized measurements
            List<Patient> patients = createSamplePatients(numberOfPatients);

            // Create sample organs
            List<CyberneticOrgan> organs = createSampleOrgans();

            // Create compatibility checker
            CyberneticOrganCompatibility checker = new CyberneticOrganCompatibility();

            // Test compatibility for each patient
            for (Patient patient : patients) {
                System.out.println("\n=== Testing Patient: " + patient.getName() + " ===");

                // Print patient measurements
                System.out.println("Patient Measurements:");
                patient.getAllMeasurements().forEach((key, value) ->
                        System.out.printf("- %s: %.1f%n", key, value));

                // Test with each organ
                for (CyberneticOrgan organ : organs) {
                    System.out.println("\nTesting with " + organ.getType());

                    // Check compatibility
                    boolean isCompatible = checker.isCompatible(patient, organ, diagnosticTree);

                    // Print results
                    System.out.println("Compatibility: " +
                            (isCompatible ? "COMPATIBLE" : "NOT COMPATIBLE"));

                    if (!isCompatible) {
                        System.out.println("Reasons for Incompatibility:");
                        checker.getIncompatibilityReasons().forEach(reason ->
                                System.out.println("- " + reason));
                    }

                    // Show diagnostic path (demonstrates BST traversal)
                    System.out.println("\nDiagnostic Path Taken:");
                    diagnosticTree.getDiagnosticPath().forEach(step ->
                            System.out.println("- " + step));
                }
            }

            // Show BST structure
            System.out.println("\n=== Diagnostic Tree Structure ===");
            diagnosticTree.printTree();
    }

    /**
     * Generates a list of patients with randomized measurements.
     *
     * @param numberOfPatients The number of patients to generate.
     * @return A list of randomized Patient objects.
     */
    private static List<Patient> createSamplePatients(int numberOfPatients) {
        List<Patient> patients = new ArrayList<>();
        Random random = new Random(); // You can set a seed for reproducibility, e.g., new Random(123)

        for (int i = 1; i <= numberOfPatients; i++) {
            String id = "P" + i;
            String name = "Patient " + i;

            Patient patient = new Patient(id, name);

            // Generate random measurements within realistic ranges
            double bloodPressure = 80.0 + random.nextDouble() * 100.0; // 80 - 180 mmHg
            double heartRate = 50.0 + random.nextDouble() * 100.0;      // 50 - 150 bpm
            double oxygenSaturation = 85.0 + random.nextDouble() * 15.0; // 85 - 100%

            // Optionally, round to one decimal place
            bloodPressure = Math.round(bloodPressure * 10.0) / 10.0;
            heartRate = Math.round(heartRate * 10.0) / 10.0;
            oxygenSaturation = Math.round(oxygenSaturation * 10.0) / 10.0;

            patient.addMeasurement("Blood Pressure", bloodPressure);
            patient.addMeasurement("Heart Rate", heartRate);
            patient.addMeasurement("Oxygen Saturation", oxygenSaturation);

            patients.add(patient);
        }

        return patients;
    }

    private static List<CyberneticOrgan> createSampleOrgans() {
        List<CyberneticOrgan> organs = new ArrayList<>();

        CyberneticOrgan heart = new CyberneticOrgan("O1", "CyberHeart-X1");
        heart.addRequirement("Blood Pressure", 90.0, 140.0);
        heart.addRequirement("Heart Rate", 60.0, 100.0);
        heart.addRequirement("Oxygen Saturation", 95.0, 100.0);
        organs.add(heart);

        return organs;
    }

    private static DiagnosticDecisionTree createDiagnosticTree() {
        DiagnosticDecisionTree tree = new DiagnosticDecisionTree();

        // Build diagnostic tree using BST properties
        tree.addDiagnosticCriteria("Blood Pressure", 140.0, null);
        tree.addDiagnosticCriteria("Blood Pressure", 90.0, null);
        tree.addDiagnosticCriteria("Heart Rate", 100.0, null); // Internal node
        tree.addDiagnosticCriteria("Heart Rate", 60.0, "Not Compatible");
        tree.addDiagnosticCriteria("Heart Rate", 100.0, "Compatible");
        tree.addDiagnosticCriteria("Oxygen Saturation", 95.0, "Compatible");

        return tree;
    }
}
