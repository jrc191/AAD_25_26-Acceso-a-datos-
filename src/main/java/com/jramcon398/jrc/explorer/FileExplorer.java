package com.jramcon398.jrc.explorer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class FileExplorer {

    public void showDirectory(String path) {
        File dir = new File(path);

        //Comprobamos si existe el directorio, listamos sus ficheros y mostramos la ruta de los mismos
        if (dir.exists()) {
            File[] files = dir.listFiles();                                 //guardamos listado de ficheros
            if (files != null) {                                            //si hay ficheros, mostramos la info
                for (File file : files) {
                    if (file.isDirectory()) {
                        log.info(" Subdirectorio: {}", file.getAbsolutePath());

                    } else if (file.isFile()) {
                        log.info(" Fichero: {}", file.getAbsolutePath());

                    }
                    //Info común a ficheros y subdirectorios
                    log.info(" \t|_Nombre: -> {}", file.getName());
                    log.info(" \t|_Espacio ocupado: -> {}", file.getTotalSpace());
                    log.info(" \t|_Fecha de última modificación: -> {}", file.lastModified());
                }
            }

        }
    }

    public void createFile(String path) {

        File file = new File(path);

        //Todos los return son para devolver al usuario al menú sin lanzar excepciones y que el programa siga funcionando
        try {

            //Si es un directorio, no podremos crear el fichero
            if (file.exists() && file.isDirectory()) {
                log.error("La ruta especificada es un directorio, no un fichero: {}", path);
                return;
            }

            //No podremos crear si ya existe
            if (file.exists()) {
                log.warn("El archivo ya existe: {}", path);
                return;
            }

            //Chequeamos si la carpeta donde queremos crear el fichero existe. IMPORTANTE: EN C:\ NO SE PUEDE ESCRIBIR, COSA DE WINDOWS
            File parentD = file.getParentFile();

            if (parentD != null && parentD.getAbsolutePath().equals("C:\\")) {
                log.error("No se puede crear archivos en C:\\ directamente. Use otra ruta.");
                return;
            }

            //Creamos el directorio padre si no existe
            if (parentD!=null && !parentD.exists()) {
                log.info ("Creando el directorio padre: {}", parentD.getAbsolutePath());
                boolean exito = parentD.mkdirs();

                //Handle de error de creación
                if (!exito && !parentD.exists()) {
                    log.error("Error creando el directorio padre: {]", parentD);
                    return;
                }
            }

            //Chequeo de permisos
            if (parentD!=null && !parentD.canWrite()) {
                log.error("No tienes permiso de escritura en: {]", parentD.getAbsolutePath());
                return;
            }

            //Y lo creamos
            boolean success = file.createNewFile();

            if (success) {
                log.info(" Creando el archivo: {}", path);
                log.info(" \t|_Nombre: -> {}", file.getName());
                log.info("CREADO.");
            }

        } catch (IOException e) {
            log.error("Se produjo un error al crear el fichero, inténtelo de nuevo.");
            throw new RuntimeException(e);
        }

    }

    public void moveFile(String path, String newPath) {
        try {
            Files.move(Paths.get(path), Paths.get(newPath));

        } catch (IOException e) {
            log.error("Se produjo un error al mover el fichero. Inténtelo de nuevo.");
            //throw new RuntimeException(e);
        }

    }

    //Método para manejar el input del usuario.
    // Si es un fichero nuevo (TRUE), entonces la ruta no existirá nunca,
    // por lo que no debemos de comprobar si existe, como sí haríamos si es una ruta existente
    // (porque estemos listando o seleccionando la ruta de origen al mover un fichero)
    public String handleInputPath(boolean newFile) {
        Scanner input = new Scanner(System.in);

        String path = "";
        File pathF = new File(path);

        if (!newFile) {
            while (!pathF.exists()) {
                log.info("Introduzca la ruta: ");
                path=input.nextLine();
                pathF=new File(path);
            }
        }
        else{
            log.info("Introduzca la ruta: ");
            path=input.nextLine();

        }

        log.info("Ruta correcta.");

        return path;
    }


}
