import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Labyrint{
  private Rute[][] rutenett;
  int kolonner;
  int rader;
  Lenkeliste<String> utveier;

  private Labyrint(Rute[][] ruter, int k, int r){
    rutenett = ruter;
    kolonner = k;
    rader = r;
  }

  static public Labyrint lesFraFil(File f)throws FileNotFoundException{
    Scanner fil;
    try{
      fil = new Scanner(f);
    }
    catch (Exception e){
      throw new FileNotFoundException();
    }
    String[] biter = fil.nextLine().split(" ");
    int r = Integer.parseInt(biter[0]);
    int k = Integer.parseInt(biter[1]);

    Rute[][] tmpRutenett = new Rute[r][k];
    int rCount = 0;
    Labyrint tmpLabyrint = new Labyrint(tmpRutenett, k, r);

    while (fil.hasNextLine()){
      String linje = fil.nextLine();
      for (int i = 0; i < linje.length(); i++){
        if((linje.charAt(i) == '.') && (checkAapning(rCount, i, r, k))){
          // if white and edge
          tmpRutenett[rCount][i] = new Aapning(tmpLabyrint, rCount, i);
        } else if(linje.charAt(i) == '.'){
          // else if just white
          tmpRutenett[rCount][i] = new HvitRute(tmpLabyrint, rCount, i);
        } else {
          // else black
          tmpRutenett[rCount][i]= new SvartRute(tmpLabyrint, rCount, i);
        }
      }
      // done with the row, mark that we've completed the row.
      rCount++;
    }
    // finish setting up rutenett before setting adjacents


    for(int i = 0; i < r; i++){
      for(int j = 0; j < k; j++){
        // If on top of rutenett (r = 0), dont set adjacentNorth
        // If on bottom of rutenett (r = rader), dont set adjacentSouth
        // If on far left of rutenett (k = 0), dont set adjacentWest
        // If on far right of rutenett (k = kolonner), dont set adjacentEast

        tmpRutenett[i][j].labyrint = tmpLabyrint;
        // NSWE
        tmpRutenett[i][j].adjacents[0] = (i > 0) ? tmpRutenett[i-1][j] : null;
        tmpRutenett[i][j].adjacents[1] = (i < r - 1) ? tmpRutenett[i+1][j] : null;
        tmpRutenett[i][j].adjacents[2] = (j > 0) ? tmpRutenett[i][j-1] : null;
        tmpRutenett[i][j].adjacents[3] = (j < k - 1) ? tmpRutenett[i][j+1] : null;
      }
    }

    return tmpLabyrint;
  }

  private static boolean checkAapning(int r, int k, int rader, int kolonner){
    // If the rute is on the edges, it qualifies to be an Aapning
    if (k == 0 || k == kolonner - 1 || r == 0 || r == rader - 1){
      return true;
    } else {
      return false;
    }
  }

  public Liste<String> finnUtveiFra(int r, int k){
    utveier = new Lenkeliste<String>();
    rutenett[k][r].finnUtVei();
    return utveier;
  }

  public Rute[][] getRutenett(){
    return rutenett;
  }

  public String toString(){
    String s = "";
    for(int i = 0; i < rader; i++){
      for(int j = 0; j < kolonner; j++){
        s += rutenett[i][j].tilTegn();
      }
      s += "\n";
    }
    return s;
  }
}
