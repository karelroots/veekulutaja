import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Isik {
	private String nimi;
	private double riieteVeekuluSumma;
	private double toiduVeekuluSumma;
	private double otsene;
	private ArrayList<Veekulu> veekulud = new ArrayList<>();
	private HashMap<String, Double> otsesedKulud;
	private HashMap<String, Double> kaudsedKulud;
	private Alert logiViga = new Alert(Alert.AlertType.ERROR);

	public Isik(String nimi, HashMap<String,Double> otsesedKulud, HashMap<String, Double> kaudsedKulud) {
		this.nimi = nimi;
		this.kaudsedKulud = kaudsedKulud;
		this.otsesedKulud = otsesedKulud;
	}

	public ArrayList<Veekulu> getVeekulu() {
		return veekulud;
	}

	public double getRiieteVeekuluSumma() {
		return riieteVeekuluSumma;
	}

	public double getToiduVeekuluSumma() {
		return toiduVeekuluSumma;
	}

	public String getNimi() {

		return nimi;
	}

	public double getOtsene() {
		return otsene;
	}

	public void arvutaVeekulu(List<String> vastused, HashMap<String, Integer> kogused) throws Exception {
		riieteVeekuluSumma = 0;
		toiduVeekuluSumma = 0.0;
		otsene = 0.0;

		for (String vastus : vastused) {

			String[] tähised = vastus.split("");
			for (String tähis : tähised) {
				if (tähis.isEmpty()) {
					continue; //jätame tühjad elemendid vahele
				}
				if (tähis.equals("t") || tähis.equals("u") || tähis.equals("v")) { //riiete tähised
					RiieteVeekulu x = new RiieteVeekulu(tähis, kaudsedKulud.get(tähis));
					riieteVeekuluSumma = riieteVeekuluSumma + kaudsedKulud.get(tähis);
					veekulud.add(x);
				} else {
					int kogus = 1;
					if (kogused.containsKey(tähis)) { //vaatame, kas kogustes eksisteerib tähis ja siis võtame selle koguse
						kogus = kogused.get(tähis);
					}
					ToiduVeekulu x = new ToiduVeekulu(tähis, kogus, kaudsedKulud.get(tähis), otsesedKulud.get(tähis)); //loome uue toiduveekulu isendi
					toiduVeekuluSumma = toiduVeekuluSumma + x.arvutaKulu();
					otsene = otsene + kogus * otsesedKulud.get(tähis);
					veekulud.add(x);
				}
			}
		}
	}

	public void salvestaVeekulu() { //Meetod, mis salvestab veekulud logifaili ja kontrollib, et samal kuupäeval juba poleks salvestatud infot

		String failinimi = nimi + "_veekulu.txt";
		try (OutputStream välja = new BufferedOutputStream(new FileOutputStream(failinimi, true));
			 OutputStreamWriter tekstVälja = new OutputStreamWriter(välja, "UTF-8")) {
			String rida;

			if(new File(failinimi).exists()) { //kas selline kasutaja juba on?
				try (InputStreamReader sisse = new InputStreamReader(new FileInputStream(failinimi));
				BufferedReader tekstSisse = new BufferedReader(sisse)) {
					while((rida = tekstSisse.readLine())!=null) { //vaatame, kas samal kuupäeval juba on logisse sisestatud info ja väljastame vea, kui nii
						if (rida.split(";")[0].equals(LocalDate.now().toString())) {
							veaDialoog("Veekulusid ei salvestatud, sest oled täna juba oma kulud salvestanud!");
							System.exit(1);
						}
					}
				}
			}

			java.time.LocalDate tänanekuupäev = LocalDate.now();
			tekstVälja.write(tänanekuupäev.toString() + ";");
			tekstVälja.write(Double.toString(otsene) + ";");
			tekstVälja.write(Double.toString(toiduVeekuluSumma) + ";");
			tekstVälja.write(Double.toString(riieteVeekuluSumma));
			tekstVälja.write("\r\n");

			Alert salvestusDialoog = new Alert(Alert.AlertType.INFORMATION);
			salvestusDialoog.setTitle("Veekulu logi");
			salvestusDialoog.setHeaderText("Veekulud salvestatud");
			salvestusDialoog.setContentText("Salvestasime sinu tänase veekulu!");
			salvestusDialoog.showAndWait();

		} catch (IOException g) {
			veaDialoog("Veekulusid ei salvestatud, sest veekulude salvestamisel tekkis viga.");
		}
	}

	public void meenutaVeekulu() { // meetod, mis loeb salvestatud veekulusid logifailist
		String failinimi=nimi+"_veekulu.txt";

		try(BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(failinimi), "UTF-8"))) {
			String rida;
			String[] tükid;
			HashMap<String, String> mälestused = new HashMap<>();
			Dialog<ButtonType> mälestusDialoog = new Dialog<>();
			ButtonType sulgeNupp = new ButtonType("Välju", ButtonBar.ButtonData.CANCEL_CLOSE);
			ButtonType tagasiNupp = new ButtonType("Tagasi", ButtonBar.ButtonData.BACK_PREVIOUS);

			mälestusDialoog.setResizable(true);
			mälestusDialoog.setTitle("Veekulu logi");
			mälestusDialoog.setHeaderText("Kuupäev: "+LocalDate.now());

			ChoiceBox<String> kuupäevaValikud = new ChoiceBox<>();

			while ((rida = bf.readLine())!=null) { //lisame valikutekastikesse kõik kasutaja logis sisalduvad kuupäevad
				tükid=rida.split(";");
				kuupäevaValikud.getItems().add(tükid[0]);
				mälestused.put(tükid[0], rida.substring(tükid[0].length()));
			}

			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));

			grid.add(new Label("Vali kuupäev:"), 0, 0);
			grid.add(kuupäevaValikud, 0, 1);

			Label info = new Label();

			grid.add(info, 0, 2);

			info.setWrapText(true);
			info.setMaxHeight(100);
			info.prefHeightProperty().bind(mälestusDialoog.heightProperty()); //informatsiooni tekst peab olema dialoogi akent muutes dünaamiline

			mälestusDialoog.getDialogPane().setContent(grid);
			mälestusDialoog.getDialogPane().getButtonTypes().addAll(tagasiNupp, sulgeNupp);


			kuupäevaValikud.setOnAction(e -> { //eventhandler, mis muudab veekulu ajaloo infot vastavalt kasutaja valitud kuupäevale
				String kuupäev = kuupäevaValikud.getValue();
				String[] tükk = mälestused.get(kuupäev).split(";");
				mälestusDialoog.setHeaderText("Kuupäev: "+kuupäev);
				String ms="Sinu otsene veekulu kuupäeval "+ kuupäev +" oli " + tükk[1] + " liitrit.\r\nKaudne veekulu toidule oli  "+ tükk[2] +" liitrit ja riietele " + tükk[3] + " liitrit. ";
				info.setText(ms);
			});

			kuupäevaValikud.setValue(kuupäevaValikud.getItems().get(0));


			Optional<ButtonType> nupuVajutus = mälestusDialoog.showAndWait();
			if (nupuVajutus.get().equals(sulgeNupp)) {
				System.exit(0);
			}

		} catch (FileNotFoundException e) {
			veaDialoog("Kasutajanimega "+nimi+" logi ei leitud!");
		} catch (IOException e) {
			veaDialoog("Logi lugemisel tekkis viga");
		}

	}

	private void veaDialoog(String teade) { //veadialoogidele ühine meetod
		logiViga.setTitle("Viga!");
		logiViga.setHeaderText(null);
		logiViga.setContentText(teade);
		logiViga.showAndWait();
	}

	@Override
	public String toString() {
		String info = nimi + ", kuigi kulutasid täna toidu tegemisele vaid umbes " + otsene + " liitrit vett, on sinu tänane tegelik (kaudne) veekulu toidule " + Math.round(toiduVeekuluSumma) + " liitrit";
		if (riieteVeekuluSumma != 0) {
			return info+" ja lisaks riietele " + Math.round(riieteVeekuluSumma) + " liitrit.";
		} else {
			return info+".";
		}


	}
}

