program programa;
var a,b: integer;

function SoyFuncion(a: boolean): integer;
var c: integer;
begin
	write(a);
	c:=0;
	read(a);
	SoyFuncion := a / c;
end;

begin
	a:=0;
	SoyFuncion(a<>2);
end.