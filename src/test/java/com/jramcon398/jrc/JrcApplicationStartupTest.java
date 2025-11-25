// java
package com.jramcon398.jrc;

import com.jramcon398.jrc.application.StudentManagementService;
import com.jramcon398.jrc.models.Module;
import com.jramcon398.jrc.models.Student;
import com.jramcon398.jrc.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class JrcApplicationStartupTest {

    @Autowired
    private StudentManagementService studentManagementService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JrcApplication application; // fuerza arranque del contexto y ejecución del CommandLineRunner

    @Test
    void applicationRunShouldInvokeServiceAndRepositoryMethods() throws Exception {
        Student stubStudent = new Student(1, "66280457T", "Miriam", "miriam@g.educaand.es", "DAW");
        Module stubModule = new Module(1, "0485", "Programación", 250);

        when(studentManagementService.createStudent(any(Student.class))).thenReturn(stubStudent);
        when(studentManagementService.createModule(any(Module.class))).thenReturn(stubModule);

        // El CommandLineRunner de la aplicación ya se ejecuta al iniciar el contexto.
        // Verificar interacciones (con timeout para esperar a que termine el runner).
        verify(studentManagementService, timeout(2000).times(1)).createStudent(any(Student.class));
        verify(studentManagementService, timeout(2000).times(1)).createModule(any(Module.class));
        verify(studentManagementService, timeout(2000).times(1)).enrollStudentInModule(eq(1), eq(1));
        verify(studentRepository, timeout(2000).times(1)).delete(eq(1));
        verify(studentManagementService, timeout(2000).times(1)).getEnrollmentCount(eq(1));
    }

    @TestConfiguration
    static class MocksConfiguration {

        @Bean
        StudentManagementService studentManagementService() {
            return mock(StudentManagementService.class);
        }

        @Bean
        StudentRepository studentRepository() {
            return mock(StudentRepository.class);
        }
    }
}
