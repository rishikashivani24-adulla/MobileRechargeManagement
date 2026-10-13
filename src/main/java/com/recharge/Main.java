package com.recharge;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.recharge.config.AppConfig;
import com.recharge.model.RechargePlan;
import com.recharge.service.RechargePlanService;

public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        RechargePlanService service =
                context.getBean(RechargePlanService.class);

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        try {
            while (running) {

                System.out.println("\n===== MOBILE RECHARGE MANAGEMENT =====");
                System.out.println("1. Add Recharge Plan");
                System.out.println("2. View All Plans");
                System.out.println("3. Search Plan by ID");
                System.out.println("4. Search Plan by Name");
                System.out.println("5. View Active Plans");
                System.out.println("6. Find Plans Under Price");
                System.out.println("7. Sort Plans by Price");
                System.out.println("8. Update Plan Using PATCH");
                System.out.println("9. Update Plan Status");
                System.out.println("10. Delete Plan");
                System.out.println("11. View Statistics");
                System.out.println("12. Exit");
                System.out.print("Enter choice: ");

                int choice;
                try {
                    choice = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                try {
                    switch (choice) {

                    case 1: {
                        System.out.print("Plan ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Plan name: ");
                        String name = sc.nextLine();

                        System.out.print("Price: ");
                        double price = Double.parseDouble(sc.nextLine().trim());

                        System.out.print("Validity in days: ");
                        int days = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Data allowance: ");
                        String data = sc.nextLine();

                        System.out.print("Active (true/false): ");
                        String activeInput = sc.nextLine().trim();

                        if (!activeInput.equalsIgnoreCase("true")
                                && !activeInput.equalsIgnoreCase("false")) {
                            System.out.println("Enter true or false.");
                            break;
                        }

                        RechargePlan plan = new RechargePlan(
                                id, name, price, days, data,
                                Boolean.parseBoolean(activeInput));

                        service.addPlan(plan);
                        System.out.println("Recharge plan added successfully.");
                        break;
                    }

                    case 2:
                        service.getAllPlans().forEach(System.out::println);
                        break;

                    case 3: {
                        System.out.print("Enter plan ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());
                        System.out.println(service.getPlanById(id));
                        break;
                    }

                    case 4: {
                        System.out.print("Enter plan name: ");
                        String name = sc.nextLine();
                        service.searchByName(name).forEach(System.out::println);
                        break;
                    }

                    case 5:
                        service.getActivePlans().forEach(System.out::println);
                        break;

                    case 6: {
                        System.out.print("Enter maximum price: ");
                        double price = Double.parseDouble(sc.nextLine().trim());
                        service.getPlansUnderPrice(price)
                               .forEach(System.out::println);
                        break;
                    }

                    case 7:
                        service.sortByPrice().forEach(System.out::println);
                        break;

                    case 8: {
                        System.out.print("Enter plan ID to update: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.println("1. Plan name");
                        System.out.println("2. Price");
                        System.out.println("3. Validity days");
                        System.out.println("4. Data allowance");
                        System.out.println("5. Active status");
                        System.out.print("Choose field to update: ");

                        int field = Integer.parseInt(sc.nextLine().trim());
                        Map<String, Object> updates = new HashMap<>();

                        switch (field) {
                        case 1:
                            System.out.print("New plan name: ");
                            updates.put("planName", sc.nextLine());
                            break;
                        case 2:
                            System.out.print("New price: ");
                            updates.put("price",
                                    Double.parseDouble(sc.nextLine().trim()));
                            break;
                        case 3:
                            System.out.print("New validity days: ");
                            updates.put("validityDays",
                                    Integer.parseInt(sc.nextLine().trim()));
                            break;
                        case 4:
                            System.out.print("New data allowance: ");
                            updates.put("dataAllowance", sc.nextLine());
                            break;
                        case 5: {
                            System.out.print("Active (true/false): ");
                            String value = sc.nextLine().trim();

                            if (!value.equalsIgnoreCase("true")
                                    && !value.equalsIgnoreCase("false")) {
                                System.out.println("Enter true or false.");
                                break;
                            }

                            updates.put("active", Boolean.parseBoolean(value));
                            break;
                        }
                        default:
                            System.out.println("Invalid field choice.");
                        }

                        if (!updates.isEmpty()) {
                            System.out.println(service.patchPlan(id, updates));
                            System.out.println("PATCH update completed.");
                        }
                        break;
                    }

                    case 9: {
                        System.out.print("Enter plan ID: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Set active (true/false): ");
                        String value = sc.nextLine().trim();

                        if (!value.equalsIgnoreCase("true")
                                && !value.equalsIgnoreCase("false")) {
                            System.out.println("Enter true or false.");
                            break;
                        }

                        service.updatePlanStatus(
                                id, Boolean.parseBoolean(value));
                        System.out.println("Plan status updated.");
                        break;
                    }

                    case 10: {
                        System.out.print("Enter plan ID to delete: ");
                        int id = Integer.parseInt(sc.nextLine().trim());

                        if (service.deletePlan(id)) {
                            System.out.println("Plan deleted successfully.");
                        } else {
                            System.out.println("Plan ID not found.");
                        }
                        break;
                    }

                    case 11:
                        System.out.println("Total plans: "
                                + service.getTotalCount());
                        System.out.println("Average price: "
                                + service.getAveragePrice());
                        System.out.println("Most expensive plan: "
                                + service.getMostExpensivePlan());
                        break;

                  case 12:
                        running = false;
                        System.out.println("Exiting Mobile Recharge Management.");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please try again.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }
            }
        } finally {
            sc.close();
            context.close();
        }
    }
}
