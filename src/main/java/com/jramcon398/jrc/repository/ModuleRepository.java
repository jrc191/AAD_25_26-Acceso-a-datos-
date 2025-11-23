package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.config.PostgresqlDriver;
import com.jramcon398.jrc.models.Module;
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
     * @param module to insert
     * @return Module inserted
     */
    @Override
    public Module insert(Module module) {
        String sql = "INSERT INTO modulo (codigo, nombre, horas) VALUES (?, ?, ?)";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                module.setId(rs.getInt("id_modulo"));
                log.info("Module inserted: {}", module);
                return module;
            }

        } catch (SQLException e) {
            log.error("Error inserting module: {}", e.getMessage());
            throw new RuntimeException("Error inserting module", e);
        }
        return null;
    }

    /**
     * @return List<Module> of all modules
     */
    @Override
    public List<Module> findAll() {
        String sql = "SELECT id_modulo, codigo, nombre, horas FROM modulo";
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
     * @param id of the module to find
     * @return Module found or null
     */
    @Override
    public Module findById(Integer id) {
        String sql = "SELECT id_modulo, codigo, nombre, horas FROM modulo WHERE id_modulo = ?";

        try (Connection conn = postgresqlDriver.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Module module = mapRow(rs);
                log.info("Module found: {}", module);
                return module;
            }

        } catch (SQLException e) {
            log.error("Error finding module by id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error finding module by id", e);
        }

        return null;
    }

    /**
     * @param module to update
     * @return Module updated
     */
    @Override
    public Module update(Module module) {
        String sql = "UPDATE modulo SET codigo = ?, nombre = ?, horas = ? WHERE id_modulo = ?";

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
     * @param id of the module to delete
     * @return boolean indicating success
     */
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM modulo WHERE id_modulo = ?";

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