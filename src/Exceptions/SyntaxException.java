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
public class SyntaxException extends Exception {

    private static final long serialVersionUID = 1L;

    public SyntaxException(String message, int numLine) {
        super("Syntax - " + message + " at line " + numLine);
        
    }
}
