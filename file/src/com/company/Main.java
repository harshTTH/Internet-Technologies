package com.company;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Main {

    private void printMetaInfo(File f, BasicFileAttributes attr){
        System.out.println("Name : "+f.getName());
        System.out.println("Creation Time: "+attr.creationTime());
        System.out.println("Last Access time: "+attr.lastAccessTime());
        System.out.println("Last Modified time: "+attr.lastModifiedTime());
        System.out.println("Size :"+attr.size()+"B");
        System.out.println("Parent Directory : "+f.getParent());
    }
    public static void main(String[] args) {
	    System.out.println("Enter a file or directory address");
	    Scanner in = new Scanner(System.in);
	    String add = in.nextLine();
        File f = new File(add);

        if(f.exists()){
            Path p = FileSystems.getDefault().getPath(add);
            BasicFileAttributes attr;
            if(f.isFile()){
                //handling file metainfo
                try {
                    attr = Files.readAttributes(p, BasicFileAttributes.class);
                    System.out.println("File Meta-Info:");
                    Main m = new Main();
                    m.printMetaInfo(f,attr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(f.canRead()) {
                    System.out.println("File can be read, Do you want to read the contents?(y/n)");
                    char ans = in.next().charAt(0);
                    if(ans == 'y' || ans == 'Y'){
                        System.out.println("File Contents : \n");
                        try {
                            FileReader fileReader = new FileReader(f);
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            String line;
                            while ((line = bufferedReader.readLine()) != null){
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                try {
                    attr = Files.readAttributes(p, BasicFileAttributes.class);System.out.println("File Meta-Info:");
                    Main m = new Main();
                    m.printMetaInfo(f,attr);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(f.canRead()) {
                    File files[] = f.listFiles();
                    System.out.println("File Name\n");
                    for (File file:
                            files) {
                        System.out.println(file.getName());
                    }
                }
            }
        }else{
            System.out.println("Invalid Address");
        }
    }
}
