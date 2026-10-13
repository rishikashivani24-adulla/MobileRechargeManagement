package com.recharge.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.recharge.model.RechargePlan;

@Repository
public class RechargePlanRepository {

    private final Map<Integer, RechargePlan> plans = new HashMap<>();

    public RechargePlan save(RechargePlan plan) {
        plans.put(plan.getPlanId(), plan);
        return plan;
    }

    public Optional<RechargePlan> findById(int planId) {
        return Optional.ofNullable(plans.get(planId));
    }

    public List<RechargePlan> findAll() {
        return new ArrayList<>(plans.values());
    }

    public boolean existsById(int planId) {
        return plans.containsKey(planId);
    }

    public boolean deleteById(int planId) {
        return plans.remove(planId) != null;
    }

    public int count() {
        return plans.size();
    }
}
