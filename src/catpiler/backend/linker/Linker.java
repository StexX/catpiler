package catpiler.backend.linker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Linker {

	public Linker(String[] files) {
		File program = new File("main.s");
		try {
			FileWriter writer = new FileWriter(program);
			// write every character out of the symbol files 
			// and place them into the final program file
			for(String f : files) {
				File file = new File(f);
				try {
					FileReader reader = new FileReader(file);
					int ch;
					while((ch = reader.read()) != -1) {
						writer.write(ch);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
