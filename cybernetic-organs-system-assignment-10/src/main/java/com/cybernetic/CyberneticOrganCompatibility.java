package com.cybernetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CyberneticOrganCompatibility {
    private List<String> incompatibilityReasons;

    public CyberneticOrganCompatibility() {
        this.incompatibilityReasons = new ArrayList<>();
    }

    public boolean isCompatible(Patient patient, CyberneticOrgan organ, DiagnosticDecisionTree diagnosticTree) {
        incompatibilityReasons.clear();
        String diagnosticResult = diagnosticTree.diagnosePatient(patient.getAllMeasurements());

        // Treat any result other than "Compatible" as incompatible
        if (!"Compatible".equals(diagnosticResult)) {
            incompatibilityReasons.add("Diagnostic Tree Result: " + diagnosticResult);
        }

        // Check all measurement ranges and collect all incompatibilities
        for (Map.Entry<String, CyberneticOrgan.Range> entry : organ.getRequirements().entrySet()) {
            String measurementType = entry.getKey();
            CyberneticOrgan.Range range = entry.getValue();
            Double measurementValue = patient.getMeasurement(measurementType);

            if (measurementValue == null || measurementValue < range.min || measurementValue > range.max) {
                incompatibilityReasons.add(measurementType + " out of range: " +
                        String.format("%.2f", measurementValue != null ? measurementValue : Double.NaN) + " (required: " +
                        String.format("%.2f", range.min) + " - " + String.format("%.2f", range.max) + ")");
            }
        }

        // If there are any incompatibility reasons, return false
        return incompatibilityReasons.isEmpty();
    }

    public List<String> getIncompatibilityReasons() {
        return new ArrayList<>(incompatibilityReasons);
    }
}
