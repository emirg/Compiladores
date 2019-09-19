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
public class WrongTypeException extends SemanticException{

    private static final long serialVersionUID = 1L;

    public WrongTypeException(String message, int numLine) {
        super("Type '" + message + "' was expected", numLine);
    }
}
