program prueba; { Archivo Prueba  }
var
  a,b,c:integer;
  d:boolean;
  
function algo(a: boolean): integer;
Var b:integer;
begin
  write(a);
  b:=0;
  {a:=m(b);} {error m fuera de alcances} 
  algo := b + 1; 
end;

function m (a:integer) :boolean;
var b:boolean; 
    c:integer;
begin
  b:=true;
  {c:= algo(b);}
  m:= b <> b; {permite op_comp entre dos int o dos boolean}
end;


procedure algo1(f: boolean);

begin
  write ( f );
end;

procedure sinParametro;
begin
  write(a);
end;


 begin
   algo1(true);
   sinParametro;
   {m := 2;} {detecta que m es una funcion y larga excepcion constructor erroneo}
   a := algo(d,a); { no detecta que la cantidad de parametros es erronea , fuera de que no estan inicializadas }
   c := 4;
   b := 3;
   d:= 3+b>6 OR (2<>2) AND 4<c ;
   algo;
   write ( b );
   write( a );
 end.