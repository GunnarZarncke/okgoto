
/<title>/ {
  person=0;
  a="";
  m=match($0, "<title>([^<]*)", r);
  if(m) name=r[1];
}
/<text/ {
  a=""
}
{
  a=a "\n" $0
}
/Kategorie:Deutscher]]/ {
  if(index(name,"/")==0)
    person=1;
}
/<\/text/ {
  if(person) {
    print name;
    file="person/" name;
    print a > file;
    close(file);
    person = 0;
  }
}
/<\/page>/ {
  if(person) {
    print name;
    file="person/" name;
    print a > file;
    close(file);
  }
}

