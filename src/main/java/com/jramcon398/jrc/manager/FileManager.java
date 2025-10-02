package com.jramcon398.jrc.manager;

import com.jramcon398.jrc.explorer.FileExplorer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/// CLASE QUE MANEJA FILEEXPLORER Y GESTIONA LA INTERACCIÓN Y APLICA LAS FUNCIONES
/// LAS FUNCIONALIDADES DE HANDLE INPUT CON BOOLEANOS SE EXPLICAN EN FILEEXPLORER

@SpringBootApplication
@Slf4j
public class FileManager {

    public void menu() throws IOException {

        Scanner input = new Scanner(System.in);
        int op=-1;

        //Funcionalidad del menú
        while (op!=0){
            log.info("Bienvenido al gestor de ficheros.\n" +
                    "==========================================================================\n" +
                    "| ¿Qué desea hacer? Por favor, escoja alguna de las siguientes opciones: |\n"+
                    "==========================================================================\n"+
                    "| 1. Crear un fichero vacío.                                             |\n"+
                    "| 2. Mover un fichero a otra ubicación.                                  |\n"+
                    "| 3. Borrar un fichero existente.                                        |\n"+
                    "| 4. Listar contenidos de una ruta.                                      |\n"+
                    "| 0. Salir del gestor de ficheros.                                       |\n"+
                    "==========================================================================");

            //Comprobamos que el input sea numérico
            if (input.hasNextInt()) {
                op = input.nextInt();
                switch (op) {
                    case 0:
                        log.warn("Saliendo del gestor de ficheros...");
                        break;
                    case 1:
                        log.info("\n=============================================\n"
                                +"| Usted escogió: 1. Crear un fichero vacío. |\n"
                                +"=============================================\n");
                        createFiles();
                        break;
                    case 2:
                        log.info("\n========================================================\n"
                                +"| Usted escogió: 2. Mover un fichero a otra ubicación. |\n"
                                +"========================================================\n");
                        moveFiles();
                        break;
                    case 3:
                        log.info("\n==================================================\n"
                                +"| Usted escogió: 3. Borrar un fichero existente. |\n"
                                +"==================================================\n");
                        deleteFiles();
                        break;
                    case 4:
                        log.info("\n====================================================\n"
                                +"| Usted escogió: 4. Listar contenidos de una ruta. |\n"
                                +"====================================================\n");

                        listFiles();
                        break;
                    default:
                        log.error("La opción escogida es incorrecta. Inténtelo de nuevo.");
                        break;
                }
            } else {
                log.error("Entrada inválida. Por favor, introduzca un número.");
                input.next(); //Descartas la entrada anterior
            }
        }

    }


    //Los siguientes métodos son importados de FileExplorer. Aquí solo recoge las rutas y poco más

    private void listFiles(){
        FileExplorer fileEx = new FileExplorer();
        String path = fileEx.handleInputPath(false);

        //Mostramos la info del directorio
        fileEx.showDirectory(path);
    }

    private void createFiles(){
        FileExplorer fileEx = new FileExplorer();
        String path = fileEx.handleInputPath(true); //es fichero nuevo <= TRUE
        fileEx.createFile(path, false);
    }

    private void moveFiles() throws IOException {
        FileExplorer fileEx = new FileExplorer();
        String path = fileEx.handleInputPath(false); //Es fichero ya existente <= FALSE
        String newPath = fileEx.handleInputPath(true); //es fichero nuevo <= TRUE
        fileEx.moveFile(path,newPath);
    }

    private void deleteFiles() throws IOException {
        FileExplorer fileEx = new FileExplorer();
        String path = fileEx.handleInputPath(false); //Es fichero ya existente <= FALSE
        File file = new File(path);
        fileEx.deleteFile(file, "Se produjo un error al borrar el fichero");
    }

}
