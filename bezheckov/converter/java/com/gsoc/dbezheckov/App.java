package com.gsoc.dbezheckov;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.gsoc.dbezheckov.matroska.MatroskaParser;
import com.gsoc.dbezheckov.matroska.SimpleMatroskaParser;
import com.gsoc.dbezheckov.matroska.dtd.Tag;

public class App  {
	
    public static void main( String[] args ) {
    	
    	if (0 == args.length) {
    		System.out.println("usage: java -jar converter.jar path/to/your/file.mkv");
    		return;
    	}
    	
    	if ("".equals(args[0])) {
    		System.out.println("empty path");
    		return;
    	}
    	
    	MatroskaParser parser = new SimpleMatroskaParser();
    	
    	try ( InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(args[0]))) ) {
    		// simple log
    		for (Tag tag : parser.parse(inputStream)) {
    			System.out.println(tag);
    		}
    	} catch (FileNotFoundException e) {
    		System.out.println("File not found " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO exception " + e.getMessage());
		}
    }
}
