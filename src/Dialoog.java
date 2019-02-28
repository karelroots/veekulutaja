import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.*;
import java.util.List;

public class Dialoog {

	private List<String> küsimused;
	private List<String> vastused = new ArrayList<>();
	private Isik isik;
	private HashMap<String, Integer> kogused = new HashMap<>();
	private Map<String, String> tähised = new HashMap<>();


	public Dialoog(ArrayList<String> küsimused, Isik isik) { //võtame failinime ja Isiku isendi sisse
		this.küsimused = küsimused;
		this.isik = isik;
	}

	public Map<String, String> getTähised() {
		return tähised;
	}

	public List<String> getVastused() {
		return vastused;
	}

	public HashMap<String, Integer> getKogused() {
		return kogused;
	}

	public void esita() { //meetod mis esitab kasutajale küsimusi ja kogub vastuseid/koguseid
		ButtonType vastaNupp = new ButtonType("Vasta", ButtonBar.ButtonData.OK_DONE); //loome edasi ja cancel nupud
		ButtonType sulgeNupp = new ButtonType("Sulge", ButtonBar.ButtonData.CANCEL_CLOSE);

		for (int i = 0; i < küsimused.size(); i++){

			Dialog<ButtonType> dialoog = new Dialog<>(); //uus dialoogi aken
			String[] tükid = küsimused.get(i).split(";"); //failist loetud read teeme tükkideks
			String küsimus = tükid[0]; //küsimus on esimeses tükis
			String[] valikud = {}; //valikud igal loopil tühjaks
			String[] tähiseTükid;
			int vastus;
			int nr = i+1;

			dialoog.getDialogPane().getButtonTypes().addAll(vastaNupp, sulgeNupp); //nupud aknasse

			if (tükid[1].length() > 1) {
				 valikud = tükid[1].split("/");
				 for(String valik : valikud) { // teeme tähistele vastavatest toiduainetest hashmapi
					 tähiseTükid = valik.split("-");
					 tähised.put(tähiseTükid[0], tähiseTükid[1]);
				 }

			}

			if (valikud.length > 1) { //kui valikute tükis on rohkem kui 1 väärtus, siis kuvame valikutega akna
				dialoog.setTitle("Küsimus "+nr);
				dialoog.setHeaderText(küsimus);
				dialoog.setContentText("Märgista kõik sobivad valikud:");
				dialoog.setResizable(true);

				GridPane grid = new GridPane(); //loome gripaani ja määrame talle parameetrid
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(20, 150, 10, 10));

				List<CheckBox> cbList = new ArrayList<>();

				for(int j = 0; j<valikud.length; j++) { //lisame kõik valikud gridpaanile
					String valik = valikud[j].split("-")[0]; //eraldame valiku nime tähisest
					CheckBox cb = new CheckBox(valik);
					grid.add(cb, 0, j);
					cbList.add(cb);
				}

				dialoog.getDialogPane().setContent(grid); //paan dialoogiaknasse
				dialoog.getDialogPane().setMinWidth(400);

				Optional<ButtonType> nupp = dialoog.showAndWait();
				if (nupp.get() == sulgeNupp) { // Sulge nupp väljub programmist
					System.exit(0);
				}

				cbList.forEach(cb -> { //vaatame kõik linnukesed checkboxides üle ja lisame vastustesse
					if (cb.isSelected()) {
						vastused.add(tähised.get(cb.getText()));
					}
				});


			} else { //koguste küsimuste esitamine
				TextInputDialog koguseDialoog = new TextInputDialog();
				koguseDialoog.setTitle("Küsimus "+nr);
				koguseDialoog.getDialogPane().setMinWidth(100);
				koguseDialoog.setHeaderText(küsimus);
				koguseDialoog.setContentText("Sisesta kogus:");

				koguseDialoog.getDialogPane().getButtonTypes().clear();
				koguseDialoog.getDialogPane().getButtonTypes().addAll(vastaNupp, sulgeNupp);
				Optional<String> vastuseKast = koguseDialoog.showAndWait();

				while(true) {
					if (vastuseKast.isPresent()) { //Madli: kui ei vajutata Cancel.
						try {
							vastus = Integer.parseInt(vastuseKast.get()); //Madli: proovib teha täisarvu.

							if (vastus<0) { //Madli: saab täisarvu tehtud, aga see on negatiivne
								throw new NumberFormatException(); //Madli: viskame ikkagi sama erindi, mis tekstilise sisendi või komaarvu puhul.
							}
							else {
								break;
							}
						} catch (NumberFormatException e){ //Madli: kui tekkis erind
							if (vastuseKast.get().length()>0) { //Madli: kui välja ei jäetud tühjaks (st pandi mingi vale asi)
								koguseDialoog.setContentText("Palun sisesta kogus positiivse täisarvuna!"); //Madli: siis küsitakse uuesti
								vastuseKast = koguseDialoog.showAndWait();
							}
							else { //Madli: kui väli jäeti tühjaks, siis arvestame koguse nulliks ja lähme tsüklist välja.
								vastus = 0;
								break;
							}
						}
					}
					else {
						System.exit(0);
					}
				}

				kogused.put(tükid[1], vastus);
			}


		}
	}
}
