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
        
       loanbroker.webservice.RuleBaseWebService_Service service = new loanbroker.webservice.RuleBaseWebService_Service();
       loanbroker.webservice.RuleBaseWebService port = service.getRuleBaseWebServicePort();
        return port.getRules(creditScore, loanDuration, loanAmount);
       
    }
    
}
