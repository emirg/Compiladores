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
public class UnexpectedCharException extends LexicalException{
    private static final long serialVersionUID = 1L;

    public UnexpectedCharException(char found, int numLine) {
        super("Unexpected character. '" + found + "' is not a recognizable character", numLine);
    }
}
