/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.service;

/**
 *
 * @author vishnukdawah
 */
public class ItemDoesNotExistException extends Exception {

    public ItemDoesNotExistException(String message) {
        super(message);
    }

    public ItemDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
