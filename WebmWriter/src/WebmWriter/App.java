package WebmWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Object;
import java.util.Scanner;

import webmTags.TagFactory;
import webmTags.UnsignedIntegerTag;
import WebmWriter.WebmWriter;

public class App {
	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.println("usage: java -jar converter.jar path/to/your/file.mkv");
			return;
		}
		
		if ("".equals(args[0])) {
			System.out.println("invalid arguments");
			return;
		}
		
		try {
			writeRecord(args[0]);
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("IO exception " + e.getMessage());
		} 
	}
	public static void writeRecord(String path) throws IOException, FileNotFoundException {
		File outputFile = getRecordFile(path);
		WebmWriter writer = new WebmWriter(outputFile, false);
		writer.writeHeader();
	}
	public static File getRecordFile(String path) throws IOException {
		File file = new File(path);
		file.createNewFile();
		return file;
	}
}