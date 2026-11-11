package com.example.nienluancoso_grapheditor.algorithm;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmStepManager {
    private List<AlgorithmStep> steps;
    private int currentStepIndex;

    public AlgorithmStepManager() {
        steps = new ArrayList<>();
        currentStepIndex = -1;
    }

    public void setSteps(List<AlgorithmStep> steps) {
        this.steps = List.copyOf(steps);
        reset();
    }

    public void reset() {
        currentStepIndex = steps.isEmpty() ? -1 : 0;
    }

    public int getCurrentStepIndex() {
        return currentStepIndex;
    }

    public int getTotalSteps(){
        return steps.size();
    }

    public AlgorithmStep getCurrentStep() {
        if (currentStepIndex != -1)
            return steps.get(currentStepIndex);
        else return null;
    }

    public boolean hasNext() {
        return !steps.isEmpty() && (currentStepIndex) <= (steps.size() - 2);
    }

    public boolean hasPrevious() {
        return !steps.isEmpty() && currentStepIndex >= 1;
    }

    public boolean next(){
        if(!hasNext()) return false;
        currentStepIndex++;
        return true;
    }

    public boolean previous(){
        if(!hasPrevious()) return false;
        currentStepIndex--;
        return true;
    }
}

