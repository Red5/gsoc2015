/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package webm2flv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
		
		Converter converter = new Converter();
		try (
				InputStream input = new BufferedInputStream(new FileInputStream(new File(args[0])));
				OutputStream output = new BufferedOutputStream(new FileOutputStream(new File(args[1])))
			) {
			converter.convert(input, output);
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("IO exception " + e.getMessage());
		} catch (ConverterException e) {
			System.out.println("ConverterException " + e.getMessage());
		}
	}
}
