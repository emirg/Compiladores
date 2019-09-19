program prueba; { Archivo Prueba  }
var
  a,b:integer;
  c:boolean;
  
function algo(a: boolean): integer;
Var b:integer;
begin
  write(a);
  b := 10;
  algo := b + 1;
end;

procedure algo1(a: boolean);

begin
  write ( a );
end;


 begin
   algo1;
   a := 2;
   algo(a);
   c:= true;
   write ( b );
   write( a );
 end.