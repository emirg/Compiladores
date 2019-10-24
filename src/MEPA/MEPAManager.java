/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MEPA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MEPAManager {

    private BufferedWriter writer;

    public MEPAManager(String archivoMEPA) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(archivoMEPA));
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }
    
    public void closeWriter() throws IOException{
        this.writer.close();
    }

    // Inicio de programa
    public void INPP() throws IOException {
        writer.write("INPP");
        writer.newLine();
    }

    // Entrada procedimiento de nivel k 
    public void ENPR(int k) throws IOException {
        writer.write("ENPR   " + k);
        writer.newLine();
    }

    // Llamada procedimiento
    public void LLPR(String label) throws IOException {
        writer.write("LLPR  " + label);
        writer.newLine();
    }

    // Parada maquina MEPA
    public void PARA() throws IOException {
        writer.write("PARA");
        writer.newLine();
    }

    // Apila valor de variable m,n -> s:=s+1; M[s]:=M[D[m]+n]
    public void APVL(int anidamiento, int offset) throws IOException {
        writer.write("APVL   " + anidamiento + "," + offset);
        writer.newLine();
    }

    // Almacena tope de pila en variable m,n -> M[D[m]+n]:=M[s]; s:=s-1;
    public void ALVL(int anidamiento, int offset) throws IOException {
        writer.write("ALVL   " + anidamiento + "," + offset);
        writer.newLine();
    }

    // Apila una constante
    public void APCT(String constante) throws IOException {
        writer.write("APCT   " + constante);
        writer.newLine();
    }

    public void SUMA() throws IOException {
        writer.write("SUMA");
        writer.newLine();
    }

    public void SUST() throws IOException {
        writer.write("SUST");
        writer.newLine();
    }

    public void MULT() throws IOException {
        writer.write("MULT");
        writer.newLine();
    }

    public void DIVI() throws IOException {
        writer.write("DIVI");
        writer.newLine();
    }

    // Menos unario
    public void UMEN() throws IOException {
        writer.write("UMEN");
        writer.newLine();
    }

    // Conjuncion logica
    public void CONJ() throws IOException {
        writer.write("CONJ");
        writer.newLine();
    }

    // Disyuncion logica
    public void DISJ() throws IOException {
        writer.write("DISJ");
        writer.newLine();
    }

    // Negacion logica
    public void NEGA() throws IOException {
        writer.write("NEGA");
        writer.newLine();
    }

    // Comparar por menor
    public void CMME() throws IOException {
        writer.write("CMME");
        writer.newLine();
    }

    // Comporar por mayor
    public void CMMA() throws IOException {
        writer.write("CMMA");
        writer.newLine();
    }

    // Comparar por igual
    public void CMIG() throws IOException {
        writer.write("CMIG");
        writer.newLine();
    }

    // Comparar por desigual
    public void CMDG() throws IOException {
        writer.write("CMDG");
        writer.newLine();
    }

    // Comparar por menor o igual
    public void CMNI() throws IOException {
        writer.write("CMNI");
        writer.newLine();
    }

    // Comprar por mayor o igual
    public void CMYI() throws IOException {
        writer.write("CMYI");
        writer.newLine();
    }

    public void LEER() throws IOException {
        writer.write("LEER");
        writer.newLine();
    }

    public void IMPR() throws IOException {
        writer.write("IMPR");
        writer.newLine();
    }

    public void NADA() throws IOException {
        writer.write("NADA");
        writer.newLine();
    }
    
    public void RMEM(int memoria) throws IOException {
        writer.write("RMEM   " + memoria);
        writer.newLine();
    }

    // Liberar memoria de variables locales
    public void LMEM(int memoria) throws IOException {
        writer.write("LMEM   " + memoria);
        writer.newLine();
    }

    // Retornar de un procedimiento de nivel k
    public void RTPR(int anidamiento, int cantParametros) throws IOException {
        writer.write("RTPR   " + anidamiento + "," + cantParametros);
        writer.newLine();
    }

    // Desviar siempre
    public void DSVS(String label) throws IOException {
        writer.write("DSVS   " + label);
        writer.newLine();
    }

    // Desviar si tope falso
    public void DSVF(String label) throws IOException {
        writer.write("DSVF   " + label);
        writer.newLine();
    }

}
