import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestRunner {
	public static void main(String[] args) {
		int count = 0;
		try(BufferedReader reader = new BufferedReader(new FileReader("Home_and_Kitchen_5.json"));
				BufferedWriter writer = new BufferedWriter(new FileWriter("testInput2.json"))){
			while(count < 20) {
				String line = reader.readLine();
				writer.write(line);
				count++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
