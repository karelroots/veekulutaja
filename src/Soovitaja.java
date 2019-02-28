import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Soovitaja {
    HashMap<String,String> soovitused;
    Isik isik;

    public Soovitaja(HashMap<String,String> soovitused, Isik isik) {
        this.soovitused = soovitused;
        this.isik = isik;
    }

    public void infoDialoog(AlertType type, String title, String header, String content) {
        ButtonType edasiNupp = new ButtonType("Edasi", ButtonBar.ButtonData.OK_DONE); //loome edasi ja cancel
        ButtonType sulgeNupp = new ButtonType("Sulge", ButtonBar.ButtonData.CANCEL_CLOSE);
        Dialog<ButtonType> infoDialoog = new Alert(type);
        infoDialoog.setTitle(title);
        infoDialoog.setHeaderText(header);
        infoDialoog.setContentText(content);
        infoDialoog.getDialogPane().getButtonTypes().clear();
        infoDialoog.getDialogPane().getButtonTypes().addAll(sulgeNupp, edasiNupp);
        infoDialoog.setResizable(true);

        Optional<ButtonType> nupuValik = infoDialoog.showAndWait();

        if (nupuValik.get().equals(sulgeNupp)) {
            System.exit(0);
        }

    }



    public void annaSoovitusi() {

        ArrayList<Veekulu> veekulud = isik.getVeekulu();
        double summa = isik.getToiduVeekuluSumma()+ isik.getRiieteVeekuluSumma();
        if (summa > 20000) {

            infoDialoog(AlertType.ERROR, "Oh, issand!", "Sinu veekulu on päris kõrge!", "Edasi on mõned soovitused oma veekulu vähendamiseks.");

        }
        else if (summa < 2000) {

            infoDialoog(AlertType.INFORMATION, "Palju õnne!", null, "Tubli! Sinu veekulu on väga madal. Jätka samas vaimus!");

        }
        else {

            infoDialoog(AlertType.WARNING, "Pole paha!", "Sinu veekulu pole liiga kõrge.", "Kuid kui sooviksid seda veelgi vähendada, siis loe edasi meie soovitusi, kuidas.");


        }
        for (Veekulu veekulu : veekulud) {

            String tähis = veekulu.getTähis();
            double kulu = veekulu.arvutaKulu();

            if (soovitused.containsKey(tähis)){
                String[] soovitus = soovitused.get(tähis).split(":");
                String sõnum = soovitus[0]+" ("+Math.round(kulu)+" liitrit):"+soovitus[1];

                if(tähis.equals("r") || tähis.equals("e") || tähis.equals("o") || (tähis.equals("f") && kulu > 400) || kulu > 600) { //riiete, kohvi kulu > 400 ja kogu kulu > 600 puhul väljastame soovitusi

                    infoDialoog(AlertType.INFORMATION, "Sinu tänased valikud:", null, sõnum);

                }
            }
        }
    }
}

