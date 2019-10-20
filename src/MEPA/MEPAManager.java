/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MEPA;

import java.io.BufferedWriter;
import java.io.IOException;

public class MEPAManager {

    private BufferedWriter flujo;

    public MEPAManager(BufferedWriter flujo) {
        this.flujo = flujo;
    }

    public BufferedWriter getFlujo() {
        return flujo;
    }

    public void setFlujo(BufferedWriter flujo) {
        this.flujo = flujo;
    }

    // Inicio de programa
    public void INPP() throws IOException {
        flujo.write("INPP");
        flujo.newLine();
    }

    // Entrada procedimiento
    public void ENPR(int anidamiento) throws IOException {
        flujo.write("ENPR " + anidamiento);
        flujo.newLine();
    }

    // Llamada procedimiento
    public void LLPR(String label) throws IOException {
        flujo.write("LLPR " + label);
        flujo.newLine();
    }

    // Parada maquina MEPA
    public void PARA() throws IOException {
        flujo.write("PARA");
        flujo.newLine();
    }

    // Apila valor de variable
    public void APVL(int anidamiento, int offset) throws IOException {
        flujo.write("APVL" + anidamiento + "," + offset);
        flujo.newLine();
    }

    // Almacena tope de pila en variable
    public void ALVL(int anidamiento, int offset) throws IOException {
        flujo.write("APVL" + anidamiento + "," + offset);
        flujo.newLine();
    }

    // Apila una constante
    public void APCT(String constante) throws IOException {
        flujo.write("APCT " + constante);
        flujo.newLine();
    }

    public void SUMA() throws IOException {
        flujo.write("SUMA");
        flujo.newLine();
    }

    public void SUST() throws IOException {
        flujo.write("SUST");
        flujo.newLine();
    }

    public void MULT() throws IOException {
        flujo.write("MULT");
        flujo.newLine();
    }

    public void DIVI() throws IOException {
        flujo.write("DIVI");
        flujo.newLine();
    }

    // Menos unario
    public void UMEN() throws IOException {
        flujo.write("UMEN");
        flujo.newLine();
    }

    // Conjuncion logica
    public void CONJ() throws IOException {
        flujo.write("CONJ");
        flujo.newLine();
    }

    // Disyuncion logica
    public void DISJ() throws IOException {
        flujo.write("DISJ");
        flujo.newLine();
    }

    // Negacion logica
    public void NEGA() throws IOException {
        flujo.write("NEGA");
        flujo.newLine();
    }

    // Comparar por menor
    public void CMME() throws IOException {
        flujo.write("CMME");
        flujo.newLine();
    }

    // Comporar por mayor
    public void CMMA() throws IOException {
        flujo.write("CMMA");
        flujo.newLine();
    }

    // Comparar por igual
    public void CMIG() throws IOException {
        flujo.write("CMIG");
        flujo.newLine();
    }

    // Comparar por desigual
    public void CMDG() throws IOException {
        flujo.write("CMDG");
        flujo.newLine();
    }

    // Comparar por menor o igual
    public void CMNI() throws IOException {
        flujo.write("CMNI");
        flujo.newLine();
    }

    // Comprar por mayor o igual
    public void CMYI() throws IOException {
        flujo.write("CMYI");
        flujo.newLine();
    }

    public void LEER() throws IOException {
        flujo.write("LEER");
        flujo.newLine();
    }

    public void IMPR() throws IOException {
        flujo.write("IMPR");
        flujo.newLine();
    }

    public void NADA() throws IOException {
        flujo.write("NADA");
        flujo.newLine();
    }

    // Liberar memoria de variables locales
    public void LMEM(int memoria) throws IOException {
        flujo.write("LMEM   " + memoria);
        flujo.newLine();
    }

    // Retornar de un procedimiento de nivel k
    public void RTPR(int anidamiento, int cantParametros) throws IOException {
        flujo.write("RTPR   " + anidamiento + "," + cantParametros);
        flujo.newLine();
    }

    // Desviar siempre
    public void DSVS(String label) throws IOException {
        flujo.write("DSVS   " + label);
        flujo.newLine();
    }

    // Desviar si tope falso
    public void DSVF(String label) throws IOException {
        flujo.write("DSVF   " + label);
        flujo.newLine();
    }

}
