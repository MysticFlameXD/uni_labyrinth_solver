import java.util.Iterator;
class Lenkeliste<T> implements Liste<T>{
  class Node{
    Node neste = null;
    T data;
    Node(T x){
      data = x;
    }
  }

  class LenkelisteIterator implements Iterator<T>{

    Node peker = new Node(null);

    LenkelisteIterator(){
      peker.neste = start;
    }

    public boolean hasNext(){ // sjekker om den har en til i listen
      return (peker.neste != null);
    }

    public T next(){ // henter dataen i neste
      if (hasNext()) {
        Node n = peker.neste;
        peker = peker.neste;
        return n.data;
      }
      return null;
    }

    public void remove(){ //

    }
  }


  private Node start = null;

  public Iterator<T> iterator(){
    LenkelisteIterator it = new LenkelisteIterator();
    return it;
  }

  public int stoerrelse(){
    Node p = start;
    int n = 0;
    while (p != null){
      n++;
      p = p.neste;
    }
    return n;
  }

  public void leggTil(int pos, T x) throws UgyldigListeIndeks{
    Node p = start;
    Node n = new Node(x);
    if (stoerrelse() < pos || pos < 0){
      throw new UgyldigListeIndeks(pos);
    }

    else if (stoerrelse() == 0){
      start = n;
    } else if (pos == 0){
      start = n;
      n.neste = p;
    } else {
      for (int i = 0; i < pos-1; i++){
        p = p.neste;
      }
      Node tmp = p.neste;
      p.neste = n;
      n.neste = tmp;
    }
  }

  public void leggTil(T x){
    Node p = start;
    Node n = new Node(x);
    if (stoerrelse() == 0){
      start = n;
    } else {
      for (int i = 0; i < stoerrelse()-1; i++){
        p = p.neste;
      }
      Node tmp = p.neste;
      p.neste = n;
      n.neste = tmp;
  }
}

  public void sett(int pos, T x) throws UgyldigListeIndeks{
    Node p = start;
    if (start == null || stoerrelse() <= pos || pos < 0){
      throw new UgyldigListeIndeks(pos);
    } else {for (int i = 0; i < pos; i++){
      p = p.neste;
    }
    p.data = x;
  }
}

  public T hent(int pos) throws UgyldigListeIndeks{
    Node p = start;
    if (start == null || stoerrelse() <= pos || pos < 0){
      throw new UgyldigListeIndeks(pos);
  } else {
    for (int i = 0; i < pos; i++){
      p = p.neste;
    }
    return p.data;
  }
  }

  public T fjern(int pos) throws UgyldigListeIndeks{
    Node p = start;
    if (start == null || stoerrelse() <= pos || pos < 0 ){
      throw new UgyldigListeIndeks(pos);
    } else if (start.neste == null){
      Node tmp = start;
      start = null;
      // System.out.println("start.neste == null");
      return tmp.data;
    } else {
      for (int i = 0; i < pos-1; i++){
        p = p.neste;
      }
      Node n = p.neste;
      p.neste = n.neste;
      // System.out.println("start.neste != null");
      return n.data;
    }
  }

  public T fjern() throws UgyldigListeIndeks{
    Node p = start;
    if (start == null){
      throw new UgyldigListeIndeks(0);
    } else if (start.neste == null){
      Node tmp = start;
      start = null;
      return tmp.data;
    } else {
      Node n = p;
      start = p.neste;
      return n.data;
    }
  }
}
