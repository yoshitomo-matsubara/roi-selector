package ymatsubara.roiselector.util;

import ymatsubara.roiselector.common.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class FileUtil {
    public static TreeMap<String, List<String>> readPositiveFile(File posFile) {
        TreeMap<String, List<String>> treeMap = new TreeMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(posFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(Config.DELIMITER);
                int length = Integer.parseInt(elements[1]);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    int index = 2 + i * 4;
                    list.add(elements[index] + Config.DELIMITER + elements[index + 1] + Config.DELIMITER
                            + elements[index + 2] + Config.DELIMITER + elements[index + 3]);
                }

                if (list.size() > 0) {
                    treeMap.put(elements[0], list);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeMap;
    }
    public static boolean isTargetFile(File file) {
        if (!file.isFile()) {
            return false;
        }

        String fileName = file.getName();
        for (int i = 0; i < Config.VALID_FORMATS.length; i++) {
            if (fileName.endsWith(Config.VALID_FORMATS[i])) {
                return true;
            }
        }
        return false;
    }

    public static List<File> getFileListR(String dirPath) {
        File dir = new File(dirPath);
        List<File> fileList = new ArrayList<>();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file);
            } else {
                List<File> childFileList = getFileListR(file.getPath());
                fileList.addAll(childFileList);
            }
        }
        return fileList;
    }

    public static void writeNegativeFile() {
        File negDir = new File(Config.INPUT_NEGATIVE_DIR);
        if (!negDir.exists()) {
            negDir.mkdirs();
        }

        try {
            List<File> fileList = getFileListR(negDir.getPath());
            BufferedWriter bw = new BufferedWriter(new FileWriter(Config.OUTPUT_NEGATIVE_FILE));
            for (File file : fileList) {
                if (isTargetFile(file)) {
                    bw.write(file.getPath());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writePositiveFile(TreeMap<String, List<String>> rectMap) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Config.OUTPUT_POSITIVE_FILE)));
            for (String key : rectMap.keySet()) {
                List<String> rectList = rectMap.get(key);
                StringBuilder sb = new StringBuilder(key + Config.DELIMITER + rectList.size());
                if (rectList.size() > 0) {
                    for (int i = 0; i < rectList.size(); i++) {
                        sb.append(Config.DELIMITER + rectList.get(i));
                    }
                }

                bw.write(sb.toString());
                bw.newLine();
                System.out.println(sb.toString());
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File[] getInputFiles() {
        File inputDir = new File(Config.INPUT_POSITIVE_DIR);
        if (!inputDir.exists()) {
            inputDir.mkdirs();
        }

        List<File> fileList = getFileListR(inputDir.getPath());
        List<File> targetFileList = new ArrayList<>();
        for (File file : fileList) {
            if (FileUtil.isTargetFile(file)) {
                targetFileList.add(file);
            }
        }

        Collections.sort(fileList);
        return fileList.toArray((new File[fileList.size()]));
    }

    public static String[] getFileNames(File[] files) {
        String[] fileNames = new String[files.length];
        for (int i = 0;i < fileNames.length; i++) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }
}
