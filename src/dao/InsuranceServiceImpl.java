package dao;

import exceptions.PolicyNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import entity.Policy;

public class InsuranceServiceImpl implements IPolicyService {
    private Connection conn;

    public InsuranceServiceImpl(Connection conn) {
        this.conn = conn;
    }

    // Create policy
    @Override
    public boolean createPolicy(Policy policy) {
        String query = "insert into Policy (policyId, policyName, policyDetails) values (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, policy.getPolicyId());
            ps.setString(2, policy.getPolicyName());
            ps.setString(3, policy.getPolicyType());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Policy getPolicy(int policyId) throws PolicyNotFoundException {
        try {
            String query = "select * from Policy where policyId = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, policyId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Policy(
                    rs.getInt("policyId"),
                    rs.getString("policyName"),
                    rs.getString("policyDetails")
                );
            } else {
                throw new PolicyNotFoundException("Policy with ID " + policyId + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<Policy> getAllPolicies() {
        Collection<Policy> policies = new ArrayList<>();
        try {
            String query = "select * from Policy";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Policy policy = new Policy(
                    rs.getInt("policyId"),
                    rs.getString("policyName"),
                    rs.getString("policyDetails")
                );
                policies.add(policy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return policies;
    }

    @Override
    public boolean updatePolicy(Policy policy) throws PolicyNotFoundException {
        try {
            Policy existingPolicy = getPolicy(policy.getPolicyId());
            if (existingPolicy == null) {
                throw new PolicyNotFoundException("Policy with ID " + policy.getPolicyId() + " not found.");
            }
            String query = "update Policy set policyName = ?, policyDetails = ? where policyId = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, policy.getPolicyName());
            ps.setString(2, policy.getPolicyType());
            ps.setInt(3, policy.getPolicyId());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (PolicyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletePolicy(int policyId) throws PolicyNotFoundException {
        try {
            Policy existingPolicy = getPolicy(policyId);
            if (existingPolicy == null) {
                throw new PolicyNotFoundException("Policy with ID " + policyId + " not found.");
            }
            String query = "delete from Policy where policyId = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, policyId);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (PolicyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
