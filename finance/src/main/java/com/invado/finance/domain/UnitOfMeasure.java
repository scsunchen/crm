/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.invado.finance.domain;

import com.invado.finance.Utils;


/**
 *
 * @author Bobic Dragan
 */
public enum UnitOfMeasure {

    PIECE,
    KILOGRAM,
    GRAM,
    TON,
    LITER,
    DECILITER,
    METER,
    SQUARE_METER,
    CUBE_METER,
    DOZEN,
    SET,
    USER_DEFINED;
    
    public String getDescription() {
        switch(this) {
            case PIECE : return Utils.getMessage("UOM.Piece");
            case KILOGRAM : return Utils.getMessage("UOM.Kilogram");
            case GRAM : return Utils.getMessage("UOM.Gram");
            case TON : return Utils.getMessage("UOM.Ton");
            case LITER : return Utils.getMessage("UOM.Liter");
            case DECILITER : return Utils.getMessage("UOM.Deciliter");
            case METER : return Utils.getMessage("UOM.Meter");
            case SQUARE_METER : return Utils.getMessage("UOM.SquareMeter");
            case CUBE_METER : return Utils.getMessage("UOM.CubicMeter");
            case DOZEN : return Utils.getMessage("UOM.Dozen");
            case SET : return Utils.getMessage("UOM.Set");
            case USER_DEFINED : return Utils.getMessage("UOM.UserDefined");
        }
        return "";
    }
}