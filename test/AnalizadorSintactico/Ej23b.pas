program prueba ;{ Archivo Prueba C&I: EJ23B.PAS }
var
  a,b:integer;

function algo(a,b,c: integer): integer;
begin
  c := a + b;
  write ( a );
  algo := a + 1;
end;

 begin
   a:= 9; 
   b:= algo(a,b,a,10);
 end.

