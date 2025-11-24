package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.config.PostgresqlDriver;
import com.jramcon398.jrc.models.Module;
import com.jramcon398.jrc.utils.SQLQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ModuleRepository implements CrudRepository<Module> {

    private final PostgresqlDriver postgresqlDriver;

    /**
     * Inserts module in the database
     *
     * @param module to insert
     * @return Module inserted
     */
    @Override
    public Module insert(Module module) {
        String sql = SQLQueries.Module_Queries.INSERT;

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt("id_modulo");
                    module.setId(generatedId);
                    log.info("Successfully added module with ID {}: {}", generatedId, module);
                    return module;
                }
            }

            log.error("Failed to insert module, no ID generated");
            return null;

        } catch (SQLException e) {
            log.error("Error inserting module: {}", e.getMessage());
            throw new RuntimeException("Error inserting module", e);
        }
    }

    /**
     * Searches and returns all modules
     *
     * @return List<Module> of all modules
     */
    @Override
    public List<Module> findAll() {
        String sql = SQLQueries.Module_Queries.FIND_ALL;
        List<Module> modules = new ArrayList<>();

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Module module = mapRow(rs);
                modules.add(module);
            }

            log.info("Found {} modules", modules.size());

        } catch (SQLException e) {
            log.error("Error finding all modules: {}", e.getMessage());
            throw new RuntimeException("Error finding all modules", e);
        }

        return modules;
    }

    /**
     * Finds a module by ID using provided connection (for transactions)
     *
     * @param id   of the module to find
     * @param conn active connection
     * @return Module found or null
     */
    public Module findById(Integer id, Connection conn) {
        String sql = SQLQueries.Module_Queries.FIND_BY_ID;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Module module = mapRow(rs);
                    log.info("Found module: {}", module);
                    return module;
                }

                log.warn("Module not found with id: {}", id);
                return null;
            }

        } catch (SQLException e) {
            log.error("Error finding module by id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error finding module by id", e);
        }
    }

    //No transaction version
    @Override
    public Module findById(Integer id) {
        try (Connection conn = postgresqlDriver.getConnection()) {
            return findById(id, conn);
        } catch (SQLException e) {
            log.error("Error getting connection: {}", e.getMessage());
            throw new RuntimeException("Error finding module", e);
        }
    }

    /**
     * Finds a module by code using provided connection (for transactions)
     *
     * @param code of the module to find
     * @param conn active connection
     * @return Module found or null
     */

    public Module findByCode(String code, Connection conn) {
        String sql = SQLQueries.Module_Queries.FIND_BY_CODE;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Module module = mapRow(rs);
                    log.info("Found module by code: {}", module);
                    return module;
                }

                log.warn("Module not found with code: {}", code);
                return null;
            }

        } catch (SQLException e) {
            log.error("Error finding module by code {}: {}", code, e.getMessage());
            throw new RuntimeException("Error finding module by code", e);
        }
    }

    // For no transaction use
    public Module findByCode(String code) {
        try (Connection conn = postgresqlDriver.getConnection()) {
            return findByCode(code, conn);
        } catch (SQLException e) {
            log.error("Error getting connection: {}", e.getMessage());
            throw new RuntimeException("Error finding module by code", e);
        }
    }

    /**
     * Updates module in the database
     *
     * @param module to update
     * @return Module updated
     */
    @Override
    public Module update(Module module) {
        String sql = SQLQueries.Module_Queries.UPDATE;

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());
            ps.setInt(4, module.getId());

            int rowsAffected = ps.executeUpdate();
            log.info("Module updated: {} (rows affected: {})", module, rowsAffected);
            return module;

        } catch (SQLException e) {
            log.error("Error updating module {}: {}", module.getId(), e.getMessage());
            throw new RuntimeException("Error updating module", e);
        }
    }

    /**
     * Deletes module from the database
     *
     * @param id of the module to delete
     * @return boolean indicating success
     */
    @Override
    public boolean delete(Integer id) {
        String sql = SQLQueries.Module_Queries.DELETE;

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            log.info("Module deleted: id={} (rows affected: {})", id, rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            log.error("Error deleting module {}: {}", id, e.getMessage());
            throw new RuntimeException("Error deleting module", e);
        }
    }

    private Module mapRow(ResultSet rs) throws SQLException {
        Module module = new Module();
        module.setId(rs.getInt("id_modulo"));
        module.setCode(rs.getString("codigo"));
        module.setName(rs.getString("nombre"));
        module.setHours(rs.getInt("horas"));
        return module;
    }
}