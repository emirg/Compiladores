program prueba ;{ Archivo Prueba C&I: EJ16D.PAS }
var
  a,b:integer;
function algo(x, y:integer): integer;
 begin
   begin 
     x:= 10;
     algo:= x + y - 10;
   end
 end;
begin
  a:=9;
  algo( a );
end.

