class HvitRute extends Rute{
  public HvitRute(Labyrint l, int r, int k){
    super(l, r, k);
  }

  public char tilTegn(){
    return '.';
  }
  
  public void gaa(String road){
    found = true;
    for (Rute r : adjacents){ //run through list of adjacents
      if (r != null && !r.found){ //if direction exists, and has it been found
        r.gaa(road + " --> (" + r.kolonne + "," + r.rad + ")");
      }
    }
    found = false;
  }
}
