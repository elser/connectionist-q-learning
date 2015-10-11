package pl.gdan.elsy.tool;

import tool.Mat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileOut extends PrintWriter {
    public FileOut(String fileName) throws IOException {
        super(new BufferedWriter(new FileWriter(fileName, true)));
    }

    public static void main(String[] args) throws IOException {
        FileOut f = new FileOut("test.txt");
        f.print("test ");
        f.printlnFormat(0.123);
        f.println("new line");
        f.close();
    }

    public void printlnFormat(double d) {
        super.println(Mat.df2.format(d));
    }

    public void printFormat(double d) {
        super.print(Mat.df2.format(d) + "	");
    }
}
