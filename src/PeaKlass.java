import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.*;

public class PeaKlass extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage peaLava) throws Exception {
        FailiLugeja lugeja = new FailiLugeja(); //loeme kohe vajalikud failid sisse ja tegeleme erinditega FailiLugeja klassi siseselt
        HashMap<String, Double> otsesedKulud = lugeja.loeVeekulud("veekulud.txt", 2);
        HashMap<String, Double> kaudsedKulud = new FailiLugeja().loeVeekulud("veekulud.txt", 1);
        HashMap<String, String> soovitused = new HashMap<>(lugeja.loeSoovitused("soovitused.txt"));
        ArrayList<String> küsimused = new ArrayList<>(lugeja.loeKüsimused("küsimused.txt"));


        Dialog<Pair<String, ButtonType>> tervitus = new Dialog<>(); //Esimene dialoogi aken, kus kasutaja saab oma infot sisestada ja programmi edasise töö osas valikuid teha
        tervitus.setTitle("Veekulutaja 2.0 by K&M");
        tervitus.setResizable(true);
        tervitus.setHeaderText("Magevesi on maailma üks väärtuslikumaid ressursse.\r\nSelle kalkulaatori abil saad teada, kui palju sa päevas tegelikult vett kulutad.");

        ButtonType uusNupp = new ButtonType("Sisesta uued kulud", ButtonBar.ButtonData.OK_DONE); //loome edasi ja cancel nupud ja lisame need aknasse
        ButtonType nädalaNupp = new ButtonType("Vaata kulusid", ButtonBar.ButtonData.OK_DONE);
        ButtonType sulgeNupp = new ButtonType("Sulge", ButtonBar.ButtonData.CANCEL_CLOSE);
        tervitus.getDialogPane().getButtonTypes().addAll(uusNupp, nädalaNupp, sulgeNupp);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nimeKast = new TextField();
        nimeKast.setPromptText("Nimi");
        nimeKast.setMaxWidth(300);
        nimeKast.prefWidthProperty().bind(tervitus.widthProperty());

        Label nimeSilt = new Label("Sisesta nimi:");
        nimeSilt.setWrapText(true);
        nimeSilt.setMaxWidth(100);
        nimeSilt.prefWidthProperty().bind(tervitus.widthProperty());

        grid.add(nimeSilt, 0, 1);
        grid.add(nimeKast, 1, 1);
        tervitus.getDialogPane().setContent(grid);

        tervitus.setHeight(400);

        Platform.runLater(() -> nimeKast.requestFocus()); // võtame nime sisestamise kasti kohe fookusesse, et kasutaja sellele eraldi klikkima ei peaks

        tervitus.setResultConverter(dialoogiNupp -> { //tegeleme erinevate nupuvajutustega erinevalt
            if (dialoogiNupp == uusNupp) {
                return new Pair<>(nimeKast.getText(), uusNupp);
            } else if (dialoogiNupp == nädalaNupp) {
                return new Pair<>(nimeKast.getText(), nädalaNupp);
            } else if (dialoogiNupp == sulgeNupp) {
                System.exit(0);
            }
            return null;
        });

        while (true) {

            Optional<Pair<String, ButtonType>> result = tervitus.showAndWait();

            String nimi = result.get().getKey(); // võtame sisestatud nime

            if (nimi.length() == 0) { //kontrollime nime sisestamist
                Alert nimeError = new Alert(AlertType.ERROR);
                nimeError.setTitle("Viga!");
                nimeError.setHeaderText(null);
                nimeError.setContentText("Kahjuks, pead ikka nime ka sisestama!");
                nimeError.showAndWait();
                continue;

            }

            Isik kasutaja = new Isik(nimi, otsesedKulud, kaudsedKulud); //loome uue kasutaja isendi

            if (result.get().getValue().equals(uusNupp)) {


                Dialoog dialoog = new Dialoog(küsimused, kasutaja);

                dialoog.esita(); //näitame kasutajale dialoogi

                kasutaja.arvutaVeekulu(dialoog.getVastused(), dialoog.getKogused()); //arvutame kasutaja veekulud
                kasutaja.salvestaVeekulu(); //salvestame kasutaja veekulud
                Soovitaja soovitaja = new Soovitaja(soovitused, kasutaja); //Loeme sisse soovitused

                Alert veekuluInfo = new Alert(AlertType.INFORMATION);
                veekuluInfo.setResizable(true);
                veekuluInfo.setTitle("Tere, " + nimi);
                veekuluInfo.setHeaderText(null);
                veekuluInfo.setContentText(kasutaja.toString());

                veekuluInfo.showAndWait();

                soovitaja.annaSoovitusi(); //pakume kasutajale soovitusi
                break;

            } else if (result.get().getValue().equals(nädalaNupp)) {

                kasutaja.meenutaVeekulu(); // kutsume välja logi lugemise meetodi

            }
        }
    }
}
