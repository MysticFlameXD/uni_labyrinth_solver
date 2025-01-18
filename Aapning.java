class Aapning extends HvitRute{
  public Aapning(Labyrint l, int r, int k){
    super(l, r, k);
  }

  public void gaa(String road){
    labyrint.utveier.leggTil(road);
    return;
  }
}
