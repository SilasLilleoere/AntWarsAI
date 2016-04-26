/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import aiantwars.IAntAI;
import tournament.player.PlayerFactory;

/**
 *
 * @author martins
 */
public class A1 implements PlayerFactory<IAntAI> {

    @Override
    public IAntAI getNewInstance() {
        return new QueenAI();
    }

    @Override
    public String getID() {
        return "A1";
    }

    @Override
    public String getName() {
        return "Skynet 2.0";
    }
    
}
