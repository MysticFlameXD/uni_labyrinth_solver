import java.util.ArrayList;

abstract class Rute{
  int kolonne;
  int rad;
  Labyrint labyrint;
  public Rute[] adjacents = new Rute[4];
  protected boolean found = false;

  public Rute(Labyrint l, int r, int k){
    labyrint = l;
    rad = r;
    kolonne = k;
  }

  abstract public char tilTegn();

  abstract public void gaa(String road);
  // {
  //
  //   if (tilTegn() == '#'){ //if black, stop
  //     return;
  //   }
  //   else if (kolonne == 0 || kolonne == labyrint.kolonner - 1 || rad == 0 || rad == labyrint.rader - 1){
  //     // System.out.println(road);
  //     labyrint.utveier.leggTil(road);
  //     return;
  //   }
  //   else{ //else white
  //     found = true;
  //     for (Rute r : adjacents){ //run through list of adjacents
  //       if (r != null && !r.found){ //if direction exists, and has it been found
  //         r.gaa(road + " --> (" + r.kolonne + "," + r.rad + ")");
  //       }
  //     }
  //     found = false;
  //   }
  // }

  public void finnUtVei(){
    gaa("(" + kolonne + "," + rad + ")");
  }

}
