/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enricherrulesv2;

/**
 *
 * @author Toozxious
 */
public class RuleBaseGateway implements IRuleBaseGateway{

    @Override
    public String getRules(int creditScore, int loanDuration, double loanAmount) {
        
       ws.RuleBaseWebService_Service service = new ws.RuleBaseWebService_Service();
       ws.RuleBaseWebService port = service.getRuleBaseWebServicePort();
        return port.getRules(creditScore, loanDuration, loanAmount);
       
    }
    
}
