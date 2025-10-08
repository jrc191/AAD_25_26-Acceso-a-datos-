package com.jramcon398.jrc.dataReaderWriter;

import com.jramcon398.jrc.models.Alumno;
import com.jramcon398.jrc.utils.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

/// Clase DataRW: esta clase se utiliza para poder ejecutar las operaciones
///               de lectura y escritura binarias.
///               El manejo de las operaciones de Inserción, consulta o
///               modificación, se encuentran en la clase RegistryManager.

@SpringBootApplication
@Slf4j
public class DataRW {

    private final int NAME_LENGTH = 20;

    public DataRW() {

    }

    public void readFileObject(File file) {
        final int RECORD_SIZE = 4 + NAME_LENGTH * 2 + 4; // 48 bytes

        //Chequeo si el archivo existe y no está vacío
        if (checkFile(file)) {

            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                // Recuento de registros
                long totalRecords = getRecordCount(file);
                log.warn("------------------------------------------------------------------------------");
                log.info("LECTURA DE ALUMNOS");

                for (int pos = 0; pos < totalRecords; pos++) {
                    // Nos vamos a la posición del alumno. Cada registro ocupa 48 bytes.
                    raf.seek(pos * RECORD_SIZE);

                    int id = raf.readInt();
                    char[] nameChars = new char[NAME_LENGTH];

                    //Leemos el nombre carácter a carácter y creamos el String
                    for (int i = 0; i < NAME_LENGTH; i++) {
                        nameChars[i] = raf.readChar();
                    }
                    String name = new String(nameChars).trim();

                    float grade = raf.readFloat();

                    log.info("Alumno leído [{}]: ID: {}, Nombre: {}, Nota: {}", pos, id, name, grade);
                }

                log.warn("------------------------------------------------------------------------------");

            } catch (IOException e) {
                log.error("Error durante la lectura del archivo: {}", e.getMessage());
            }
        } else {
            log.warn("El archivo no existe o está vacío.");
        }
    }

    public void writeFileObject(File file, Alumno alumno) {
        // Validaciones
        if (!InputValidator.isNonEmptyString(alumno.getNombre())) {
            log.error("El nombre no puede estar vacío.");
            return;
        }
        if (alumno.getNota() < 0 || alumno.getNota() > 10) {
            log.error("La nota debe estar entre 0 y 10.");
            return;
        }

        //Escribimos el objeto en el archivo
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.seek(raf.length());
            raf.writeInt(alumno.getId());
            String name = String.format("%-" + NAME_LENGTH + "s", alumno.getNombre());
            for (char c : name.toCharArray()) {
                raf.writeChar(c);
            }
            raf.writeFloat(alumno.getNota());
            log.info("Alumno agregado correctamente.");
        } catch (IOException e) {
            log.error("Error escribiendo archivo: {}", e.getMessage());
        }
    }

    private boolean checkFile(File file) {

        return file.exists() && file.isFile() && (file.length() > 0);
    }

    public boolean idExists(File file, int id) {
        final int RECORD_SIZE = 4 + NAME_LENGTH * 2 + 4;
        // Verificamos si el archivo existe y no está vacío. De estarlo, salimos
        if (!file.exists() || file.length() == 0) {
            return false;
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            //recuento de registros
            long totalRecords = raf.length() / RECORD_SIZE;
            // Iteramos a través de los registros para buscar el ID. De encontrarlo, retornamos true
            for (int pos = 0; pos < totalRecords; pos++) {
                raf.seek(pos * RECORD_SIZE); //solo leemos el ID, no necesitamos leer el registro completo
                int existingId = raf.readInt();
                if (existingId == id){
                    return true;
                }
            }
        } catch (IOException e) {
            log.error("Error leyendo archivo: {}", e.getMessage());
        }
        return false;
    }

    // Método para obtener el número de registros en el archivo binario
    public long getRecordCount(File file) {
        final int RECORD_SIZE = 4 + NAME_LENGTH * 2 + 4;
        if (!file.exists() || file.length() == 0){
            return 0;
        }
        return file.length() / RECORD_SIZE;
    }


}
