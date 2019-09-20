program prueba; { Archivo Prueba  }
var
  a,b:integer;
  c:boolean;
  
function algo(a: boolean): integer;
Var b:integer;
begin
  write(a);
  b:=0;
  {a:=m(b);} {problema de alcances} 
  algo := b + 1; 
end;

function m (a:integer) :boolean;
var b:boolean; 
begin
  b:=true;
  m:=b;
end;


procedure algo1(a: boolean);

begin
  write ( a );
end;


 begin
   {m := 2;} {detecta que m es una funcion y larga excepcion contructor erroneo}
   a:=algo(a,b); { no detecta que la cantidad de parametros es erronea , fuera de que no estan inicializadas }
   c:= true;
   write ( b );
   write( a );
 end.