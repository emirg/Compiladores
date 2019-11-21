program ejemplo;

var a,b,c:integer;

function potencia(n:integer;m:integer):integer;
{n^m}
var i,result:integer;

begin
    result:=1;
    i:=1;
    while i<=m do
        begin
            result:=result*n;
            i:=i+1;
        end;
    write(result);
    potencia:=result;
end;

begin
    read(b);
    read(c);
    a:=potencia(b,c);
    write(a);  
end.
