package sandrohc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import sandrohc.gui.GuiMain;
import sandrohc.handlers.GsonHandler;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Main {
	public static File output = new File("sumarios.json");
	public static ArrayList<Sumario> LISTA = new ArrayList<>();

	public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		changeStyle();

		createFile();

		// Cria o Object do JSON
		JsonObject jObj = new JsonParser().parse(new JsonReader(new FileReader(output))).getAsJsonObject();

		// Lê os todos os dados do Object e adiciona-os à Lista
		GsonHandler.deserialize(jObj);

		// Inicia a GUI
		new GuiMain(0);
	}

	/**
	 * Changes window's style according to OS's style
	 *
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void changeStyle() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}

	public static void createFile() throws IOException {
		if(!output.exists()) // If file doesn't exist, try to create one
			if(!output.createNewFile()) // If the creating failed, throw a new Exception
				throw new IOException("Unable to create the file " + output.getName() + ".");
	}

	public static void saveToFile() throws IOException {
		createFile();

		PrintWriter writer = new PrintWriter(output);
		writer.append(GsonHandler.serialize().toString());
		writer.close();
	}
}