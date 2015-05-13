package bezheckov;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
	public static void main(String args[]) {
		try {
			EBMLParser parser = new EBMLParser(new BufferedInputStream(new FileInputStream(
					new File("/home/dima00782/code/gsoc2015/bezheckov/data/sample.mkv"))));
			parser.parseHeader();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}