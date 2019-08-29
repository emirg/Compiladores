import java.util.Scanner;

public class Compilador {

    public static void main(String[] args) {
        //AnalizadorLexico lexico = new AnalizadorLexico(args[0]);

        //PC Emiliano
        //AnalizadorLexico lexico = new AnalizadorLexico("C:\\Users\\emi_r\\Documents\\Git\\Compiladores\\Analizador Sintactico\\tests\\Ej01a.pas");

        //PC German
        String [] tests = new String[128];
        tests[0]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej01a.pas";

        tests[1]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej01b.pas";
        tests[2]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej01c.pas";
        tests[3]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej02a.pas";
        tests[4]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej02b.pas";
        tests[5]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej03a.pas";
        tests[6]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej03b.pas";
        tests[7]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej03c.pas";
        tests[8]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej04a.pas";
        tests[9]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej04b.pas";
        tests[10]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej04c.pas";
        tests[11]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej05a.pas";
        tests[12]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej05b.pas";
        tests[13]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej06a.pas";
        tests[14]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej06b.pas";
        tests[15]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej07a.pas";
        tests[16]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej08a.pas";
        tests[17]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej08b.pas";
        tests[18]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej08c.pas";
        tests[19]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej08d.pas";
        tests[20]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej09a.pas";
        tests[21]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej10a.pas";
        tests[22]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej10b.pas";
        tests[23]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej10c.pas";
        tests[24]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej10d.pas";
        tests[25]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej10e.pas";
        tests[26]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej11a.pas";
        tests[27]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej11b.pas";
        tests[28]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej11c.pas";
        tests[29]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej11d.pas";
        tests[30]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej12a.pas";
        tests[31]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej13a.pas";
        tests[32]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej13b.pas";
        tests[33]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej13c.pas";
        tests[34]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej14a.pas";
        tests[35]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej15a.pas";
        tests[36]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej15b.pas";
        tests[37]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej15c.pas";
        tests[38]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej15b.pas";
        tests[39]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej15e.pas";
        tests[40]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16a.pas";
        tests[41]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16b.pas";
        tests[42]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16c.pas";
        tests[43]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16d.pas";

        tests[44]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16e.pas";
        tests[45]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16f.pas";
        tests[46]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16g.pas";
        tests[47]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16h.pas";
        tests[48]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16i.pas";
        tests[49]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16j.pas";
        tests[50]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej16k.pas";
        tests[51]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej17a.pas";
        tests[52]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej17b.pas";
        tests[53]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej17c.pas";
        tests[54]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej17d.pas";
        tests[55]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej17e.pas";
        tests[56]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej17f.pas";
        tests[57]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej18a.pas";
        tests[58]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej18b.pas";
        tests[59]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej18c.pas";
        tests[60]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej19a.pas";
        tests[61]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej19b.pas";
        tests[62]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej20a.pas";
        tests[63]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej20b.pas";
        /*controle hasta aca*/
        tests[64]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej20c.pas";
        tests[65]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej20d.pas";
        tests[66]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej20e.pas";
        tests[67]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej21a.pas";
        tests[68]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej21b.pas";
        tests[69]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej22a.pas";
        tests[70]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej22b.pas";
        tests[71]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej22c.pas";
        tests[72]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej22d.pas";
        tests[73]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej23a.pas";
        tests[74]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej23b.pas";
        tests[75]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej23c.pas";
        tests[76]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej23d.pas";
        tests[77]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej23e.pas";
        tests[78]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej23f.pas";
        tests[79]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej24a.pas";
        tests[80]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej24b.pas";
        tests[81]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej24c.pas";
        tests[82]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej24d.pas";
        tests[83]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej25a.pas";
        tests[84]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej25b.pas";
        tests[85]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej26a.pas";
        tests[86]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej27a.pas";
        tests[87]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej27b.pas";
        tests[88]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej27c.pas";
        tests[89]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej28a.pas";
        tests[90]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej28b.pas";
        tests[91]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej29a.pas";
        tests[92]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej29b.pas";
        tests[93]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej30.pas";
        tests[94]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej30a.pas";
        tests[95]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej30b.pas";
        tests[96]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej30c.pas";
        tests[97]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej30d.pas";
        tests[98]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej31.pas";
        tests[99]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej31a.pas";
        tests[100]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej31c.pas";
        tests[101]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej32.pas";
        tests[102]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej32a.pas";
        tests[103]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej32b.pas";
        tests[104]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej32c.pas";
        tests[105]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej33a.pas";
        tests[106]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej33b.pas";
        tests[107]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej34a.pas";
        tests[108]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej34b.pas";
        tests[109]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej34c.pas";
        tests[110]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej34d.pas";
        tests[111]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej35a.pas";
        tests[112]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej36a.pas";
        tests[113]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej36b.pas";
        tests[114]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej36c.pas";
        tests[115]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej39a.pas";
        tests[116]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej40a.pas";
        tests[117]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej40b.pas";
        tests[118]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej41a.pas";
        tests[119]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej41b.pas";
        tests[120]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej41c.pas";
        tests[121]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej41d.pas";
        tests[122]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej42a.pas";
        tests[123]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej42b.pas";
        tests[124]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej42c.pas";
        tests[125]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej43a.pas";
        tests[126]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej43b.pas";
        tests[127]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej43c.pas";

        //tests[]="/home/german/Escritorio/Compiladores/Analizador Sintactico/tests/Ej.pas";


        boolean continuar=true;
        String entrada;
        int cont=98;
        while(continuar){
            AnalizadorLexico lexico = new AnalizadorLexico(tests[cont]);

            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);
            try {
                sintactico.program();
            } catch (UnexpectedToken e) {
                System.out.println(e.getMessage());
            }
            System.out.println("precione c para continuar, test actual "+cont);
            Scanner entradaEscaner = new Scanner (System.in); //Creación de un objeto Scanner
            entrada= entradaEscaner.nextLine (); //Invocamos un método sobre un objeto Scanner
            if(!entrada.equals("c")){
                continuar=false;
            }
            cont++;

        }
        
        AnalizadorLexico lexico = new AnalizadorLexico(tests[17]);

        AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);
        try {
            sintactico.program();
        } catch (UnexpectedToken e) {
            System.out.println(e.getMessage());
        }
        

    }
}
