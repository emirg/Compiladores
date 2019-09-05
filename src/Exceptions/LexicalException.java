/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author emiliano
 */
public class LexicalException extends Exception {

    private static final long serialVersionUID = 1L;

    public LexicalException(String message, int numLine) {
        super("Lexical - " + message + " at line " + numLine);
    }
    
}
