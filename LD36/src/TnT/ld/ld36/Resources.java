package TnT.ld.ld36;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Resources {
	/**
	 * Returns a Scanner object for the given resource.
	 * @param name the Resource to access
	 * @return the Scanner for the resource
	 * @throws FileNotFoundException if the file is not found
	 */
	public static Scanner getScanner(String name) throws FileNotFoundException{
		return new Scanner(getInputStream(name));
	}
	
	/**
	 * Returns an InputStream for the given resource.
	 * @param name the name of the resource
	 * @return the InputStream
	 * @throws FileNotFoundException if file cannot be found
	 */
	@SuppressWarnings("resource")
	public static InputStream getInputStream(String name) throws FileNotFoundException{
		InputStream in;
		/*try root save directory first- if we need to change a file on the fly it loads that one first 
		(also any mod like add team- rewrite new event data file there and its saved)
		*/
			try{		
				in=Resources.class.getResourceAsStream("/Resources/"+name);
				if(in==null)throw new FileNotFoundException("Resource " + name + " not in save root");
			}catch(FileNotFoundException e1){
				System.err.println("Unable to load Resource: "+name);
				throw new FileNotFoundException("Could not find resource: "+name);
			}
		return in;
	}
	
	public static Image getImage(String name){
		try {
			return ImageIO.read(getInputStream(name));
		} catch (Exception e) {
			System.err.println("YA DONE MESSED UP A A RON");
			System.err.println(name);
			System.exit(0);
			return null;
		}
	}
}
