package com.jramcon398.jrc.explorer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.nio.file.*;
import java.util.Date;
import java.util.Scanner;

/// CLASE QUE PERMITE LA FUNCIONALIDAD DE FICHEROS: MOVER, ELIMINAR, LISTAR...
/// USADA POR FILEMANAGER, QUE SE ENCARGA DE GESTIONAR EL MENÚ DE INTERACCION Y APLICAR LAS FUNCIONES

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
                    // Tamaño.
                    if (file.isFile()) {
                        log.info(" \t|_Espacio ocupado: -> {} bytes", file.length());
                    } else if (file.isDirectory()) {
                        log.info(" \t|_Espacio ocupado: -> {} bytes", getDirectorySize(file));
                    }
                    //Fecha modificación. Ni idea de cómo ponerla más bonita, así se queda
                    log.info(" \t|_Fecha de última modificación: -> {}", new Date(file.lastModified()));
                }
            }

        }


    }

    //Función recursiva para obtener el tamaño total de un directorio (incluyendo su contenido)
    //Comprueba si es fichero y suma el tamaño, si no, lo recorre de nuevo y lo suma.
    private long getDirectorySize(File directory) {
        long size = 0;
        //lista ficheros
        File[] files = directory.listFiles();
        if (files != null) {
            //Recorremos y chequeamos si es fichero (sumamos) o directorio (recorremos de nuevo)
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else if (file.isDirectory()) {
                    size += getDirectorySize(file);
                }
            }
        }
        return size;
    }

    public void createFile(String path, boolean fromMove) {

        File file = new File(path);

        //Todos los return son para devolver al usuario al menú sin lanzar excepciones y que el programa siga funcionando
        try {

            if (invalidFileCreation(path, file)) {
                return;
            }

            //Chequeamos si la carpeta donde queremos crear el fichero existe.
            // IMPORTANTE: EN C:\ NO SE PUEDE ESCRIBIR
            File parentD = file.getParentFile();

            if (isRestrictedPath(parentD)){
                return;
            }

            //Creamos el directorio padre si no existe
            if (ensureParentExists(parentD)){
                return;
            }

            //Chequeo de permisos
            if (hasWritePermission(parentD)){
                return;
            }

            boolean success = file.createNewFile();

            //Problemilla de aquí. Antes los logs se mostraban antes de mover correctamente los ficheros
            //Ahora con esto podemos distinguir los logs para que al mover no se muestren hasta una vez creados.
            if (success) {
                if (fromMove) {
                    log.info(" Preparando destino para mover: {}", path);
                } else {
                    logSuccess(path, true, file);
                }
            }

        } catch (IOException e) {
            log.error("Se produjo un error al crear el fichero, inténtelo de nuevo.");
        }

    }

    public void moveFile(String path, String newPath) throws IOException {
        File fileOld = new File(path);
        File fileNew = new File(newPath);

        // Validamos que el fichero a copiar exista
        if (!isValidSourceFile(fileOld, path)) {
            return;
        }

        // Y que el fichero a donde lo moveremos no exista
        if (fileNew.exists()) {
            log.error("El archivo de destino ya existe: {}", newPath);
            return;
        }

        // Chequeamos que el directorio donde movamos exista y tengamos permisos
        File parentNew = fileNew.getParentFile();
        if (parentNew != null && !parentNew.exists()) {
            log.info("Creando el directorio padre: {}", parentNew.getAbsolutePath());
            if (!parentNew.mkdirs() && !parentNew.exists()) {
                log.error("Error creando el directorio padre: {}", parentNew);
                return;
            }
        }

        // Comprobamos que no sean directorios restringidos por Windows
        if (isRestrictedPath(parentNew)) {
            return;
        }

        // Validamos permisos
        if (!validateFilePermissions(fileOld, fileNew)) {
            return;
        }

        // Copiamos contenido
        try {
            log.info("Copiando contenido de {} a {}", path, newPath);
            copyFileContent(fileOld, fileNew);
        } catch (IOException e) {
            log.error("Error copiando contenido: {}", e.getMessage());
            // Eliminamos el fichero nuevo si ocurre algún error
            if (fileNew.exists()) {
                deleteFile(fileNew, "Error eliminando el fichero nuevo después de fallo de copia: {}");
            }
            return;
        }

        // Verificar que los ficheros coinciden en contenido
        try {
            //MARAVILLA. CHEQUEA QUE EL CONTENIDO DEL FICHERO (EN BYTES) SEA IDÉNTICO (y devuelve -1 de ser así)
            long mismatch = Files.mismatch(Path.of(path), Path.of(newPath));

            //FURULA
            if (mismatch == -1) {
                log.info("Verificación exitosa. Eliminando archivo origen...");
                deleteFile(fileOld, "Error eliminando el fichero antiguo: {}");
                log.info("Fichero movido exitosamente de {} a {}", path, newPath);
            } else {
                // NO FURULA
                log.error("Los archivos no coinciden después de la copia (diferencia en byte {}). Limpiando...", mismatch);
                deleteFile(fileNew, "Error eliminando el fichero nuevo: {}");
            }
        } catch (IOException e) {
            log.error("Error verificando la copia del fichero: {}", e.getMessage());
            deleteFile(fileNew, "Error eliminando el fichero nuevo después de fallo de verificación: {}");
            throw e;
        }
    }

    //Muestra logs de información con el éxito de operaciones del explorador
    private void logSuccess(String path, boolean success, File file) {
        if (success) {
            log.info(" Creando el archivo: {}", path);
            log.info(" \t|_Nombre: -> {}", file.getName());
            log.info("CREADO.");
        }
    }

    //Chequeo de escritura
    private boolean hasWritePermission(File parentD) {
        if (parentD !=null && !parentD.canWrite()) {
            log.error("No tienes permiso de escritura en: {]", parentD.getAbsolutePath());
            return true;
        }
        return false;
    }

    //Para crear un archivo, necesitamos que el directorio en el que lo crearemos exista
    private boolean ensureParentExists(File parentD) {
        if (parentD !=null && !parentD.exists()) {
            log.info ("Creando el directorio padre: {}", parentD.getAbsolutePath());
            boolean exito = parentD.mkdirs();

            //Handle de error de creación
            if (!exito && !parentD.exists()) {
                log.error("Error creando el directorio padre: {]", parentD);
                return true;
            }
        }
        return false;
    }

    // Para comprobar que no creamos en rutas restringidas por windows
    private boolean isRestrictedPath(File parentD) {
        if (parentD == null){
            return false;
        }

        //Para la ruta absoluta del directorio padre (donde queremos escribir)
        String absolutePath = parentD.getAbsolutePath().toLowerCase();

        // Directorios que no podemos usar. Comprobados vaya, vaya sistema más...
        if (absolutePath.equals("c:\\")) {
            log.error("No se puede crear archivos en C:\\ directamente. Use otra ruta.");
            return true;
        }

        if (absolutePath.equals("c:\\users")) {
            log.error("No se puede crear archivos directamente en C:\\Users. Use una subcarpeta.");
            return true;
        }

        if (absolutePath.startsWith("c:\\windows")) {
            log.error("No se puede crear archivos en directorios de Windows: {}", absolutePath);
            return true;
        }

        return false;
    }

    //Comprobaciones para crear ficheros
    private boolean invalidFileCreation(String path, File file) {
        //Si es un directorio, no podremos crear el fichero
        if (file.exists() && file.isDirectory()) {
            log.error("La ruta especificada es un directorio, no un fichero: {}", path);
            return true;
        }

        //No podremos crear si ya existe
        if (file.exists()) {
            log.warn("El archivo ya existe: {}", path);
            return true;
        }
        return false;
    }


    //Para comprobar que el fichero a mover sea fichero y que exista
    private boolean isValidSourceFile(File fileOld, String newPath) {
        if (!fileOld.exists() || !fileOld.isFile()) {
            log.error("El fichero de origen no existe: {}", newPath);
            return false;
        }

        return true;
    }
    //Chequeo de permisos de lectura y escritura
    private boolean validateFilePermissions(File fileOld, File fileNew) {
        File parentNew = fileNew.getParentFile();

        if (parentNew != null && !parentNew.canWrite()) {
            log.error("Permisos insuficientes de escritura en el directorio de destino: {}", parentNew.getAbsolutePath());
            return false;
        }

        if (!fileOld.canRead()) {
            log.error("Permisos insuficientes de lectura del fichero de origen: {}", fileOld.getAbsolutePath());
            return false;
        }

        return true;
    }


    //Vaya tocada de huevos. FUNCIONA CON TO, antes solo txt (de ahí lo de cambiarlo como 500 veces)
    private void copyFileContent(File fileOld, File fileNew) throws IOException {
        FileInputStream fis = new FileInputStream(fileOld);
        FileOutputStream fos = new FileOutputStream(fileNew);

        //Dolor de cabeza pero vamos. Byte a byte, porque la mierda esta fallaba si había líneas vacías (con el FileReader y FileWriter)
        try {
            int byteRead;
            while ((byteRead = fis.read()) != -1) {
                fos.write(byteRead);
            }

            fos.flush(); // Y lo quitamos
        } catch (IOException e) {
            log.error("Error durante la copia de archivo");

        }

        // Cerramos los input y output stream
        try {
            fis.close();
        } catch (IOException e) {
            log.error("Error cerrando FileInputStream");
        }

        try {
            fos.close();
        } catch (IOException e) {
            log.error("Error cerrando FileOutputStream");
        }
    }

    //Método para borrar un fichero. Chequea permisos y demás
    public void deleteFile(File file, String error) {
        //No borramos si los requerimientos no se cumplen
        if (!canDeleteFile(file)) {
            return;
        }

        boolean deleted = file.delete();
        if (deleted) {
            log.info("Archivo eliminado exitosamente: {}", file.getAbsolutePath());
        } else {
            log.error(error, file.getAbsolutePath());
        }
    }

    // Comprobaciones para poder borrar o no ficheros
    private boolean canDeleteFile(File file) {
        // Verificar que el archivo existe y es un fichero
        if (file == null || !file.exists()) {
            log.error("El archivo {} no existe", file.getAbsolutePath());
            return false;
        }

        //Chequeamos que sea fichero y no directorio y/u otros
        if (file.isDirectory() || !file.isFile()) {
            log.error("La ruta especificada es un directorio: {}", file.getAbsolutePath());
        }

        // Comprobar directorio padre
        File parent = file.getParentFile();
        if (isRestrictedPath(parent)) {
            log.error("No se puede borrar archivos en rutas restringidas: {}", parent.getAbsolutePath());
            return false;
        }

        if (parent != null && !parent.canWrite()) {
            log.error("Permisos insuficientes de escritura en el directorio: {}", parent.getAbsolutePath());
            return false;
        }

        return true;
    }


    //Método para manejar el input del usuario.
    // Si es un fichero nuevo (TRUE), entonces la ruta no existirá nunca,
    // por lo que no debemos de comprobar si existe, como sí haríamos si es una ruta existente.
    // (porque estemos listando o seleccionando la ruta de origen al mover un fichero)
    public String handleInputPath(boolean newFile) {
        Scanner input = new Scanner(System.in);
        String path;
        File pathF;

        while (true) {
            log.info("Introduzca la ruta: ");
            path = input.nextLine().trim();

            // Validar que la ruta no esté vacía
            if (path.isEmpty()) {
                log.error("La ruta no puede estar vacía.");
                continue;
            }

            //Si poniamos, por ejemplo, ª como ruta, salia que era válida pero no funcionaban crear o mover.
            //Con este regex pattern, podemos hacer que toda ruta empiece por una letra de la A a la Z (^[a-zA-Z]), que continúe con :
            // y posteriormente con \ y cualquier otro carácter las veces que se quiera (0 o muchas). La página que he usado para testearlo es https://regexr.com/
            if (!path.matches("^[a-zA-Z]:\\\\.*")) {
                log.error("Ruta inválida. Debe empezar con una letra de unidad y :\\, por ejemplo C:\\Carpeta\\archivo.txt");
                continue;
            }

            pathF = new File(path);

            // Si es ruta existente
            if (!newFile && !pathF.exists()) {
                log.error("La ruta no existe. Intente de nuevo.");
                continue;
            }

            break; // ruta válida
        }

        log.info("Ruta correcta.");
        return path;
    }


}
