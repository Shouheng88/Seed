package com.seed.base;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base generator.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 11:11
 */
public abstract class BaseGenerator {

    protected static String convertFirstLetterToLower(String className) {
        String lowerString;
        String firstLetter = className.substring(0, 1);
        String leftLetters = className.substring(1);
        lowerString = firstLetter.toLowerCase() + leftLetters;
        return lowerString;
    }

    protected static String header(String author, String comment) {
        return String.format(
                "/**\n" +
                " * %s\n" +
                " *\n" +
                " * @author " + author + "\n" +
                " * @version 1.0 \n" +
                " * @date %s\n" +
                " */\n", comment, new SimpleDateFormat("yyy/MM/dd hh:mm").format(new Date()));
    }

    protected static String header(String pkg, String author, String comment) {
        return String.format(pkg +
                "\n\n" +
                "/**\n" +
                " * %s\n" +
                " *\n" +
                " * @author " + author + "\n" +
                " * @version %s\n" +
                " */\n", comment, new SimpleDateFormat("yyy/MM/dd hh:mm").format(new Date()));
    }

    protected static void printFile(String fileName, String content, String outputDirectory, boolean cover) {
        System.out.println(content);
        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            boolean succeed = dir.mkdir();
            if (!succeed) {
                System.out.println("Failed to create directory!");
                return;
            }
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(dir, fileName);
            if (file.exists() && !cover) {
                System.err.println("WARN: files exist, cover them may make your data loss!!!");
                return;
            }
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            safeClose(bw);
            safeClose(fw);
        }
    }

    protected static void printFile(String fileName, String content, String outDir) {
        printFile(fileName, content, outDir, true);
    }

    protected static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
