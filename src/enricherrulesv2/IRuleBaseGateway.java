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
public interface IRuleBaseGateway {
    public String getRules(int creditScore, int loanDuration, double loanAmount);
}
