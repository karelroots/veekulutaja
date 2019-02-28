import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AndmeteGeneraator {
	ArrayList<String> vastused = new ArrayList<>();
	HashMap<String, Integer> kogused = new HashMap<>();
	String[] tähised = {"a","b","c","d","e","f","g","h","isik","j","k","l","m","n","o","p","q","r","t","u","v","x","y","z"};

	public void genereeri(){
		for (int i = 0; i < suvalineArv(5, tähised.length); i++) { // tsükkel jookseb suvaline arv kordi 1-st 24ni

			int j = suvalineArv(0, tähised.length-1); // suvaline index 0-st 23ni
			int k = suvalineArv(1, 10); //suvaline kogus 1-st 10ni

			if (!vastused.contains(tähised[j])) { // vaatame, et see tähis juba poleks valitud
				vastused.add(tähised[j]); // tähis suvalise indeksiga vastustesse
				kogused.put(tähised[j], k); // suvaline tähis ja kogus kogustesse
				}
			}
		}

	public ArrayList<String> getVastused() {
		return vastused;
	}

	public HashMap<String, Integer> getKogused() {
		return kogused;
	}

	public int suvalineArv(int min, int max) {
		Random rand = new Random();
		//int suvalineArv = rand.ints(1, min, max+1).findFirst().getAsInt(); // Striim versioon, mida hetkel ei kasuta
		int suvalineArv = rand.nextInt(max - min + 1) + min;
		return suvalineArv;
	}
}
