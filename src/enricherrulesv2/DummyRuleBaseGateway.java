/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package enricherrulesv2;

import java.util.ArrayList;

/**
 *
 * @author Kasper
 */
public class DummyRuleBaseGateway implements IRuleBaseGateway{

    
    @Override
    public String getRules(int creditScore, int loanDuration, double loanAmount) {
        String rule = "expensive.lol";
/*        Random rand = new Random();
        int result =  rand.nextInt(2);
        if(result == 0){
            banks.add("expensive");
        }else
            banks.add("cheap");*/
        return rule;
    }
    
}
