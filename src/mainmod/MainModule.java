package mainmod;

import dao.InsuranceServiceImpl;
import exceptions.PolicyNotFoundException;
import util.DBConnection;
import java.sql.*;
import java.util.*;

import entity.Policy;

public class MainModule {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;

        try {
       
          
            conn = DBConnection.getConnection("db.properties");
            if (conn != null) {
                System.out.println("Connection established.");
            }
            InsuranceServiceImpl inSer = new InsuranceServiceImpl(conn);

            while (true) {
                System.out.println("Insurance Management System");
                System.out.println("1. Create Policy");
                System.out.println("2. Get Policy by ID");
                System.out.println("3. Get All Policies");
                System.out.println("4. Update Policy");
                System.out.println("5. Delete Policy");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Policy ID: ");
                        int createPolicyId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Policy Name: ");
                        String policyName = scanner.nextLine();
                        System.out.print("Enter Policy Type: ");
                        String policyType = scanner.nextLine();
                        Policy newPolicy = new Policy(createPolicyId, policyName, policyType);
                        boolean isCreated = inSer.createPolicy(newPolicy);
                        System.out.println(isCreated ? "Policy created successfully." : "Failed to create policy.");
                        break;

                    case 2:
                        System.out.print("Enter Policy ID to retrieve: ");
                        int getPolicyId = scanner.nextInt();
                        try {
                            Policy policy = inSer.getPolicy(getPolicyId);
                            System.out.println("Policy ID: " + policy.getPolicyId());
                            System.out.println("Policy Name: " + policy.getPolicyName());
                            System.out.println("Policy Type: " + policy.getPolicyType());
                        } catch (PolicyNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 3:
                        Collection<Policy> policies = inSer.getAllPolicies();
                        System.out.println("All Policies:");
                        for (Policy policy : policies) {
                            System.out.println("Policy ID: " + policy.getPolicyId() + ", Name: " + policy.getPolicyName() + ", Type: " + policy.getPolicyType());
                        }
                        break;

                    case 4:
                        System.out.print("Enter Policy ID to update: ");
                        int updatePolicyId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new Policy Name: ");
                        String newPolicyName = scanner.nextLine();
                        System.out.print("Enter new Policy Type: ");
                        String newPolicyType = scanner.nextLine();
                        Policy updatedPolicy = new Policy(updatePolicyId, newPolicyName, newPolicyType);
                        try {
                            boolean isUpdated = inSer.updatePolicy(updatedPolicy);
                            System.out.println(isUpdated ? "Policy updated successfully." : "Failed to update policy.");
                        } catch (PolicyNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.print("Enter Policy ID to delete: ");
                        int deletePolicyId = scanner.nextInt();
                        try {
                            boolean isDeleted = inSer.deletePolicy(deletePolicyId);
                            System.out.println(isDeleted ? "Policy deleted successfully." : "Failed to delete policy.");
                        } catch (PolicyNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 6:
                        System.out.println("Exiting the program.");
                        return;

                    default:
                        System.out.println("Invalid option, please try again.");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                scanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
