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

import webmTags.TagFactory;
import webmTags.UnsignedIntegerTag;
import WebmWriter.WebmWriter;

public class App {
	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out.println("usage: java -jar converter.jar path/to/your/file.mkv");
			return;
		}
		
		if ("".equals(args[0]) || "".equals(args[1])) {
			System.out.println("invalid arguments");
			return;
		}
		
		/*Converter converter = new Converter();*/
		try (
				//InputStream input = new BufferedInputStream(new FileInputStream(new File(args[0])));
				//OutputStream output = new BufferedOutputStream(new FileOutputStream(new File(args[1])))
				File outputFile = getRecordFile(args[1]);
				WebmWriter = new WebmWriter(outputFile, false);

			) {
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("IO exception " + e.getMessage());
		} 
	}
	public static File getRecordFile(String path) throws IOException {
		File file = new File(path);
		file.createNewFile();
		return file;
	}
}