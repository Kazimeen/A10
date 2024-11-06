package com.cybernetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiagnosticDecisionTree {
    private DiagnosticNode root;
    private List<String> diagnosticPath;

    public DiagnosticDecisionTree() {
        this.diagnosticPath = new ArrayList<>();
    }

    public List<String> getDiagnosticPath() {
        return new ArrayList<>(diagnosticPath);
    }

    public void addDiagnosticCriteria(String measurementType, double threshold, String diagnosis) {
        DiagnosticNode newNode = new DiagnosticNode(measurementType, threshold, diagnosis);

        if (root == null) {
            root = newNode;
        } else {
            addNodeRecursive(root, newNode);
        }
    }

    private void addNodeRecursive(DiagnosticNode current, DiagnosticNode newNode) {
        if (newNode.thresholdValue < current.thresholdValue) {
            if (current.left == null) {
                current.left = newNode;
            } else {
                addNodeRecursive(current.left, newNode);
            }
        } else {
            if (current.right == null) {
                current.right = newNode;
            } else {
                addNodeRecursive(current.right, newNode);
            }
        }
    }

    public String diagnosePatient(Map<String, Double> measurements) {
        diagnosticPath.clear();
        return diagnosePatientRecursive(root, measurements, 1);
    }

    private String diagnosePatientRecursive(DiagnosticNode node, Map<String, Double> measurements, int level) {
        if (node == null) {
            diagnosticPath.add("Level " + level + ": No further criteria. Diagnosis: Inconclusive");
            return "Inconclusive";
        }

        Double measurementValue = measurements.getOrDefault(node.measurementType, Double.NaN);
        String comparison = measurementValue < node.thresholdValue ? "<" : "≥";
        String step = String.format("Level %d: %s = %.1f %s %.1f", level, node.measurementType, measurementValue, comparison, node.thresholdValue);
        diagnosticPath.add(step);

        if (node.diagnosis != null) {
            return node.diagnosis;
        }

        if (measurementValue < node.thresholdValue) {
            return diagnosePatientRecursive(node.left, measurements, level + 1);
        } else {
            return diagnosePatientRecursive(node.right, measurements, level + 1);
        }
    }

    public void printTree() {
        printTreeRec(root, "", true);
    }

    private void printTreeRec(DiagnosticNode node, String prefix, boolean isLeft) {
        if (node != null) {
            System.out.println(prefix + (isLeft ? "├── " : "└── ") +
                    node.measurementType + " (" + node.thresholdValue + ")" +
                    (node.diagnosis != null ? " -> " + node.diagnosis : ""));
            printTreeRec(node.left, prefix + (isLeft ? "│   " : "    "), true);
            printTreeRec(node.right, prefix + (isLeft ? "│   " : "    "), false);
        }
    }
}
