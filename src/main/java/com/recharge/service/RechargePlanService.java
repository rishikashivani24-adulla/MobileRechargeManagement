package com.recharge.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.recharge.model.RechargePlan;
import com.recharge.repository.RechargePlanRepository;

import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Service
@Validated
public class RechargePlanService {

    private final RechargePlanRepository repository;
    private final Validator validator;

    public RechargePlanService(
            RechargePlanRepository repository,
            Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public RechargePlan addPlan(@Valid RechargePlan plan) {
        validatePlan(plan);

        if (repository.existsById(plan.getPlanId())) {
            throw new IllegalArgumentException("Plan ID already exists.");
        }

        return repository.save(plan);
    }

    public Optional<RechargePlan> getPlanById(int planId) {
        return repository.findById(planId);
    }

    public List<RechargePlan> getAllPlans() {
        return repository.findAll();
    }

    public List<RechargePlan> searchByName(String name) {
        return repository.findAll()
                .stream()
                .filter(plan -> plan.getPlanName() != null
                        && plan.getPlanName().toLowerCase()
                                .contains(name.toLowerCase()))
                .toList();
    }

    public List<RechargePlan> getActivePlans() {
        return repository.findAll()
                .stream()
                .filter(plan -> Boolean.TRUE.equals(plan.getActive()))
                .toList();
    }

    public List<RechargePlan> getPlansUnderPrice(double price) {
        return repository.findAll()
                .stream()
                .filter(plan -> plan.getPrice() < price)
                .toList();
    }

    public List<RechargePlan> sortByPrice() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparingDouble(RechargePlan::getPrice))
                .toList();
    }

    public RechargePlan updatePlanStatus(int planId, boolean active) {
        RechargePlan plan = repository.findById(planId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Plan ID not found."));

        plan.setActive(active);
        validatePlan(plan);

        return repository.save(plan);
    }

    public boolean deletePlan(int planId) {
        return repository.deleteById(planId);
    }

    public int getTotalCount() {
        return repository.count();
    }

    public double getAveragePrice() {
        return repository.findAll()
                .stream()
                .mapToDouble(RechargePlan::getPrice)
                .average()
                .orElse(0.0);
    }

    public Optional<RechargePlan> getMostExpensivePlan() {
        return repository.findAll()
                .stream()
                .max(Comparator.comparingDouble(RechargePlan::getPrice));
    }

    public RechargePlan patchPlan(
            int planId, Map<String, Object> updates) {

        RechargePlan existing = repository.findById(planId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Plan ID not found."));

        RechargePlan updated = new RechargePlan(
                existing.getPlanId(),
                existing.getPlanName(),
                existing.getPrice(),
                existing.getValidityDays(),
                existing.getDataAllowance(),
                existing.getActive());

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();

            switch (field) {
            case "planName":
                updated.setPlanName((String) value);
                break;

            case "price":
                updated.setPrice(((Number) value).doubleValue());
                break;

            case "validityDays":
                updated.setValidityDays(((Number) value).intValue());
                break;

            case "dataAllowance":
                updated.setDataAllowance((String) value);
                break;

            case "active":
                updated.setActive((Boolean) value);
                break;

            default:
                throw new IllegalArgumentException(
                        "Unknown field: " + field);
            }
        }

        validatePlan(updated);
        return repository.save(updated);
    }

    private void validatePlan(RechargePlan plan) {
        var violations = validator.validate(plan);

        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + ": "
                            + v.getMessage())
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("Invalid recharge plan.");

            throw new IllegalArgumentException(message);
        }
    }
}
