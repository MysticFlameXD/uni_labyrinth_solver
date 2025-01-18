import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.*;
import javafx.event.*;

import java.io.File;
import java.io.FileNotFoundException;
/*
 +----+-----+-----+
 | ⬛ | ⬜ | ⬜ |
 +----+-----+-----+
 | ⬜ | ⬜ | ⬜ |
 +----+-----+-----+
 | ⬜ | ⬜ | ⬜ |
 +----+-----+-----+

 ⬛ = black, unclickable
 ⬜ = white, clickable, run solver on click
*/
public class LabyrintSkjerm extends Application{
  Text feedback;
  Text currentPathText;
  Labyrint l = null;
  Rute[][] lRutenett;
  Tile[][] brett;
  int bredde;
  int hoyde;
  int currentPath = 0;
  int maxPaths = 0;
  Button nesteKnapp;
  Button forrigeKnapp;
  boolean[][] validUtvei = null;
  Liste<String> utveier;

  class Tile extends Button{
    public int rPos;
    public int kPos;

    Tile(int r, int k){
      super(" ");
      rPos = r;
      kPos = k;
    }
  }

  class NesteBehandler implements EventHandler<ActionEvent>{
    @Override
    public void handle(ActionEvent e){

      for(int i = 0; i < hoyde; i++){
        for(int j = 0; j < bredde; j++){
          if(lRutenett[i][j].tilTegn() == '.'){
            // if white, set white
            brett[i][j].setStyle("-fx-background-color: #ffffff");
          } else{
            // else, set black
            brett[i][j].setStyle("-fx-background-color: #000000");
          }
        }
      }
      currentPath++;
      currentPathText.setText(currentPath + " av " + maxPaths);

      if (currentPath >= maxPaths){
        nesteKnapp.setDisable(true);
      } else {
        nesteKnapp.setDisable(false);
      }

      if (currentPath <= 1){
        forrigeKnapp.setDisable(true);
      } else {
        forrigeKnapp.setDisable(false);
      }

      validUtvei = losningStringTilTabell(utveier.hent(currentPath-1), bredde, hoyde);

      for(int i = 0; i < hoyde; i++){
        for(int j = 0; j < bredde; j++){
          if(validUtvei[i][j] == true){
            brett[i][j].setStyle("-fx-background-color: #00ff00");
          }
        }
      }

    }
  }

  class ForrigeBehandler implements EventHandler<ActionEvent>{
    @Override
    public void handle(ActionEvent e){

      for(int i = 0; i < hoyde; i++){
        for(int j = 0; j < bredde; j++){
          if(lRutenett[i][j].tilTegn() == '.'){
            // if white, set white
            brett[i][j].setStyle("-fx-background-color: #ffffff");
          } else{
            // else, set black
            brett[i][j].setStyle("-fx-background-color: #000000");
          }
        }
      }

      currentPath--;
      currentPathText.setText(currentPath + " av " + maxPaths);

      if (currentPath >= maxPaths){
        nesteKnapp.setDisable(true);
      } else {
        nesteKnapp.setDisable(false);
      }

      if (currentPath <= 1){
        forrigeKnapp.setDisable(true);
      } else {
        forrigeKnapp.setDisable(false);
      }

      validUtvei = losningStringTilTabell(utveier.hent(currentPath-1), bredde, hoyde);

      for(int i = 0; i < hoyde; i++){
        for(int j = 0; j < bredde; j++){
          if(validUtvei[i][j] == true){
            brett[i][j].setStyle("-fx-background-color: #00ff00");
          }
        }
      }
    }
  }
  class KlikkBehandler implements EventHandler<ActionEvent>{
    @Override
    public void handle(ActionEvent e){
      // get button where event triggered
      Tile tile = (Tile)e.getSource();

      // optimally get a immutable copy of brett
      // this is hella time inefficient
      for(int i = 0; i < hoyde; i++){
        for(int j = 0; j < bredde; j++){
          if(lRutenett[i][j].tilTegn() == '.'){
            // if white, set white
            brett[i][j].setStyle("-fx-background-color: #ffffff");
          } else {
            // else, set black
            brett[i][j].setStyle("-fx-background-color: #000000");
          }
        }
      }

      validUtvei = null;
      utveier = l.finnUtveiFra(tile.kPos, tile.rPos);
      try{
        validUtvei = losningStringTilTabell(utveier.hent(0), bredde, hoyde);
      }
      catch(Exception ex){
        // if utveier doesnt exist -> no exits
        feedback.setText("Ingen utveier.");
        currentPathText.setText("");
        nesteKnapp.setDisable(true);
        forrigeKnapp.setDisable(true);
        tile.setStyle("-fx-background-color: #ff0000");
      }

      if(validUtvei != null){
        feedback.setText("Fant " + utveier.stoerrelse() + " utvei(er).");
        for(int i = 0; i < hoyde; i++){
          for(int j = 0; j < bredde; j++){
            if(validUtvei[i][j] == true){
              brett[i][j].setStyle("-fx-background-color: #00ff00");
            }
          }
        }
        maxPaths = utveier.stoerrelse();
        currentPath = 1;
        currentPathText.setText(currentPath + " av " + maxPaths);

        if (currentPath >= maxPaths){
          nesteKnapp.setDisable(true);
        } else {
          nesteKnapp.setDisable(false);
        }
        if (currentPath <= 1){
          forrigeKnapp.setDisable(true);
        } else {
          forrigeKnapp.setDisable(false);
        }

      }

    }
  }
  class StoppBehandler implements EventHandler<ActionEvent>{
    @Override
    public void handle(ActionEvent e){
      // stops program when called / pressed
      Platform.exit();
    }
  }

  // public
  @Override
  public void start(Stage teater){
    FileChooser fileChooser = new FileChooser();

    // file type filter, only shows .in files
    fileChooser.getExtensionFilters().addAll(
      new FileChooser.ExtensionFilter("Labyrint Files", "*.in")
    );
    // dialog box
    File f = fileChooser.showOpenDialog(teater);

    try {
      l = Labyrint.lesFraFil(f);
    } catch (FileNotFoundException e) {
      // only runs on cancelling
      System.out.printf("FEIL: Kunne ikke lese fra fil");
      System.exit(1);
    }

    bredde = l.kolonner;
    hoyde = l.rader;
    lRutenett = l.getRutenett();

    feedback = new Text("Velg en rute");
    feedback.setFont(new Font(22));
    feedback.setX(70);
    feedback.setY(30);

    currentPathText = new Text("");
    currentPathText.setFont(new Font(16));
    currentPathText.setX(43);
    currentPathText.setY(58);

    StoppBehandler stopp = new StoppBehandler();
    Button stoppKnapp = new Button("Stopp");
    stoppKnapp.setLayoutX(10);
    stoppKnapp.setLayoutY(10);
    stoppKnapp.setOnAction(stopp);

    NesteBehandler neste = new NesteBehandler();
    nesteKnapp = new Button(">");
    nesteKnapp.setLayoutX(130);
    nesteKnapp.setLayoutY(40);
    nesteKnapp.setOnAction(neste);

    ForrigeBehandler forrige = new ForrigeBehandler();
    forrigeKnapp = new Button("<");
    forrigeKnapp.setLayoutX(10);
    forrigeKnapp.setLayoutY(40);
    forrigeKnapp.setOnAction(forrige);

    nesteKnapp.setDisable(true);
    forrigeKnapp.setDisable(true);

    GridPane rutenett = new GridPane();
    // rutenett.setGridLinesVisible(true);

    brett = new Tile[l.rader][l.kolonner];
    KlikkBehandler klikk = new KlikkBehandler();
    for(int i = 0; i < l.rader; i++){
      for(int j = 0; j < l.kolonner; j++){
        brett[i][j] = new Tile(i, j);
        brett[i][j].setMinSize(28, 28);
        brett[i][j].setOnAction(klikk);

        if(lRutenett[i][j].tilTegn() == '.'){
          // if white, set white
          brett[i][j].setStyle("-fx-background-color: #ffffff");
        } else{
          // else, set black
          brett[i][j].setStyle("-fx-background-color: #000000");
        }
        // setting the tile at j x-pos and i y-pos
        rutenett.add(brett[i][j], j, i);
      }
    }
    rutenett.setLayoutX(10);
    rutenett.setLayoutY(100);



    Pane kulisser = new Pane();
    // kulisser.setPrefSize(400, 500);
    kulisser.getChildren().add(stoppKnapp);
    kulisser.getChildren().add(nesteKnapp);
    kulisser.getChildren().add(forrigeKnapp);
    kulisser.getChildren().add(feedback);
    kulisser.getChildren().add(currentPathText);
    kulisser.getChildren().add(rutenett);

    Scene scene = new Scene(kulisser);
    teater.setTitle("Labyrinth Solver");
    teater.setScene(scene);
    teater.show();
  }

  /**
   * Konverterer losning-String fra oblig 5 til en boolean[][]-representasjon
   * av losningstien.
   * @param losningString String-representasjon av utveien
   * @param bredde        bredde til labyrinten
   * @param hoyde         hoyde til labyrinten
   * @return              2D-representasjon av rutene der true indikerer at
   *                      ruten er en del av utveien.
   */
  static boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
      boolean[][] losning = new boolean[hoyde][bredde];
      java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
      java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
      while (m.find()) {
          int x = Integer.parseInt(m.group(1));
          int y = Integer.parseInt(m.group(2));
          losning[y][x] = true;
      }
      return losning;
  }

  public static void main(String[] args){
    launch();
  }
}
