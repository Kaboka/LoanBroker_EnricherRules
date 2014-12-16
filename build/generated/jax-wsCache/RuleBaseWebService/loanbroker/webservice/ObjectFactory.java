
package loanbroker.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the loanbroker.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetRulesResponse_QNAME = new QName("http://webservice.loanbroker/", "getRulesResponse");
    private final static QName _GetRules_QNAME = new QName("http://webservice.loanbroker/", "getRules");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: loanbroker.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetRules }
     * 
     */
    public GetRules createGetRules() {
        return new GetRules();
    }

    /**
     * Create an instance of {@link GetRulesResponse }
     * 
     */
    public GetRulesResponse createGetRulesResponse() {
        return new GetRulesResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRulesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.loanbroker/", name = "getRulesResponse")
    public JAXBElement<GetRulesResponse> createGetRulesResponse(GetRulesResponse value) {
        return new JAXBElement<GetRulesResponse>(_GetRulesResponse_QNAME, GetRulesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRules }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.loanbroker/", name = "getRules")
    public JAXBElement<GetRules> createGetRules(GetRules value) {
        return new JAXBElement<GetRules>(_GetRules_QNAME, GetRules.class, null, value);
    }

}
