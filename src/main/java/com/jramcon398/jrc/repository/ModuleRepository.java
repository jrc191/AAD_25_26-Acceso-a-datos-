package com.jramcon398.jrc.repository;

import com.jramcon398.jrc.models.Module;
import com.jramcon398.jrc.utils.Constants;
import com.jramcon398.jrc.utils.SQLQueries;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

/**
 * ModuleRepository class: Handles CRUD operations for Module entities.
 * Uses JdbcTemplate for database interactions.
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class ModuleRepository implements CrudRepository<Module> {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Inserts module in the database
     *
     * @param module to insert
     * @return Module inserted
     */
    @Override
    public Module insert(Module module) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    SQLQueries.Module_Queries.INSERT,
                    new String[]{"id_modulo"}
            );
            ps.setString(1, module.getCode());
            ps.setString(2, module.getName());
            ps.setInt(3, module.getHours());
            return ps;
        }, keyHolder);

        Integer generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        module.setId(generatedId);

        log.info("Successfully added module with ID {}: {}", generatedId, module);
        return module;
    }

    /**
     * Searches and returns all modules
     *
     * @return List<Module> of all modules
     */
    @Override
    public List<Module> findAll() {
        return jdbcTemplate.query(
                SQLQueries.Module_Queries.FIND_ALL,
                (rs, rowNum) -> new Module(
                        rs.getInt("id_modulo"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("horas")
                )
        );
    }

    /**
     * Finds a module by ID using provided connection (for transactions)
     *
     * @return Module found or null
     */
    public Module findById(Integer id) {
        try {
            Module module = jdbcTemplate.queryForObject(
                    SQLQueries.Module_Queries.FIND_BY_ID,
                    (rs, rowNum) -> new Module(
                            rs.getInt("id_modulo"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("horas")
                    ),
                    id
            );
            log.info("Found module: {}", module);
            return module;
        } catch (EmptyResultDataAccessException e) {
            log.warn(Constants.MODULE_NOT_FOUND, id);
            return null;
        }
    }

    /**
     * Finds a module by code using provided connection (for transactions)
     *
     * @param code of the module to find
     * @return Module found or null
     */

    public Module findByCode(String code) {
        try {
            Module module = jdbcTemplate.queryForObject(
                    SQLQueries.Module_Queries.FIND_BY_CODE,
                    (rs, rowNum) -> new Module(
                            rs.getInt("id_modulo"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("horas")
                    ),
                    code
            );
            log.info("Found module by code: {}", module);
            return module;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Module not found with code: {}", code);
            return null;
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
        int rowsAffected = jdbcTemplate.update(
                SQLQueries.Module_Queries.UPDATE,
                module.getCode(),
                module.getName(),
                module.getHours(),
                module.getId()
        );

        if (rowsAffected > 0) {
            log.info("Module updated: {} (rows affected: {})", module, rowsAffected);
            return module;
        } else {
            log.warn(Constants.MODULE_NOT_FOUND, module.getId());
            return null;
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
        int rowsAffected = jdbcTemplate.update(
                SQLQueries.Module_Queries.DELETE,
                id
        );

        if (rowsAffected > 0) {
            log.info("Module deleted: id={} (rows affected: {})", id, rowsAffected);
            return true;
        } else {
            log.warn(Constants.MODULE_NOT_FOUND, id);
            return false;
        }
    }

}