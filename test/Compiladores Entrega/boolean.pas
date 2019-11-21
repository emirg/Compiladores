program ejemplo;

var b,c:integer;
    a:boolean;

function equals(n:integer;m:integer):boolean;
{n^m}
var result:boolean;

begin
    result:= true;
    write(result);
    equals:=result;
end;

begin
    read(b);
    read(c);
    a:=equals(b,c);
    write(a);  
end.