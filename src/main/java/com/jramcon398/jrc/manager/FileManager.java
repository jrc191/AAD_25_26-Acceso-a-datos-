package com.jramcon398.jrc.manager;

import com.jramcon398.jrc.explorer.FileExplorer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class FileManager {

    public void menu(){

        Scanner input = new Scanner(System.in);
        int op=-1;

        while (op!=0){
            log.info("Bienvenido al gestor de ficheros.\n" +
                    "¿Qué desea hacer? Por favor, escoja alguna de las siguientes opciones:\n"+
                    "=======================================================================\n"+
                    "1. Crear un fichero vacío.\n"+
                    "2. Mover un fichero a otra ubicación.\n"+
                    "3. Borrar un fichero existente.\n"+
                    "4. Listar contenidos de una ruta.\n"+
                    "0. Salir del gestor de ficheros.\n"+
                    "=======================================================================");

            op= input.nextInt();
            switch (op){
                case 0:
                    log.warn("Saliendo del gestor de ficheros...");
                    break;
                case 1:
                    log.info("Usted escogió: 1. Crear un fichero vacío.");
                    createFiles();
                    break;
                case 2:
                    log.info("Usted escogió: 2. Mover un fichero a otra ubicación.");
                    moveFiles();
                    break;
                case 3:
                    log.info("Usted escogió: 3. Borrar un fichero existente.");
                    break;
                case 4:
                    log.info("Usted escogió: 4. Listar contenidos de una ruta.");
                    listFiles();
                    break;
                default:
                    log.error("La opción escogida es incorrecta. Inténtelo de nuevo.");
                    break;

            }
        }

    }

    private void listFiles(){
        FileExplorer fileEx = new FileExplorer();
        String path = fileEx.handleInputPath(false);

        //Mostramos la info del directorio
        fileEx.showDirectory(path);
    }

    private void createFiles(){
        FileExplorer fileEx = new FileExplorer();
        String path = fileEx.handleInputPath(true); //es fichero nuevo <= TRUE
        fileEx.createFile(path);
    }

    private void moveFiles(){
        FileExplorer fileEx = new FileExplorer();
        String path = fileEx.handleInputPath(false);
        String newPath = fileEx.handleInputPath(true); //es fichero nuevo <= TRUE
        fileEx.moveFile(path,newPath);
    }

}
