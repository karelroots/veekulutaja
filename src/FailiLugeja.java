import javafx.scene.control.Alert;
import javafx.util.Pair;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FailiLugeja {
	private ArrayList<String> küsimused = new ArrayList<>();
	private HashMap<String, String> soovitused = new HashMap<>();
	private HashMap<String, Double> veekulud = new HashMap<>();
	private Map<String, String> tähised = new HashMap<>();
	private Alert lugejaViga = new Alert(Alert.AlertType.ERROR);


	public ArrayList<String> loeKüsimused (String failinimi) { //scanneri asemel on nüüd try-with-resources bufferedreader
		String rida;

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(failinimi), "UTF-8"))) {

			while ((rida = br.readLine()) != null) {
                küsimused.add(rida);
                String[] tükid = rida.split(";");
                /*if (tükid.length > 2) {
					String[] valikud = tükid[1].split("/");
					for(String valik : valikud) { // teeme tähistele vastavatest toiduainetest hashmapi
						String[] tähiseTükid = valik.split("-");
						tähised.put(tähiseTükid[0], tähiseTükid[1]);
					}
				}*/
            }

		} catch (FileNotFoundException e) {
			veaDialoog("Küsimuste fail puudub kataloogis");
		} catch (IOException e) {
			veaDialoog("Küsimuste failist lugemisel tekkis viga");
		}
		return küsimused;
	}

	public void veaDialoog(String teade) {
		lugejaViga.setHeaderText("Viga!");
		lugejaViga.setContentText(teade);
		lugejaViga.showAndWait();
		System.exit(1);
	}

	public HashMap<String, String> loeSoovitused (String failinimi) {
		String rida;

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(failinimi), "UTF-8"))) {

			while ((rida = br.readLine()) != null) {
				String[] tükid = rida.split(";");
				soovitused.put(tükid[0],tükid[1]);
			}

		} catch (FileNotFoundException e) {
			veaDialoog("Soovituste fail puudub kataloogis");
		} catch (IOException e) {
			veaDialoog("Soovituste failist lugemisel tekkis viga");
		}

		return soovitused;
	}


	public HashMap<String, Double> loeVeekulud (String failinimi, int x) throws Exception {
		String rida;

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(failinimi), "UTF-8"))) {

			while ((rida = br.readLine()) != null) {
				String[] tükid = rida.split(";");
				veekulud.put(tükid[0],Double.parseDouble(tükid[x]));
			}

		} catch (FileNotFoundException e) {
			veaDialoog("Veekulude fail puudub kataloogis");
		} catch (IOException e) {
			veaDialoog("Veekulude failist lugemisel tekkis viga");
		}


		return veekulud;
	}
}
