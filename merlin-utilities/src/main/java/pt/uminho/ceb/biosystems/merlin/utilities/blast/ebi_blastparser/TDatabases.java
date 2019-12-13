//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.12.11 at 06:10:17 PM GMT 
//


package pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tDatabases complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDatabases">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="database" type="{http://www.ebi.ac.uk/schema}tDatabase" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="total" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="sequences" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="letters" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDatabases", propOrder = {
    "database"
})
public class TDatabases {

    @XmlElement(required = true)
    protected List<TDatabase> database;
    @XmlAttribute(name = "total", required = true)
    protected int total;
    @XmlAttribute(name = "sequences", required = true)
    protected long sequences;
    @XmlAttribute(name = "letters", required = true)
    protected long letters;

    /**
     * Gets the value of the database property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the database property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatabase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDatabase }
     * 
     * 
     */
    public List<TDatabase> getDatabase() {
        if (database == null) {
            database = new ArrayList<TDatabase>();
        }
        return this.database;
    }

    /**
     * Gets the value of the total property.
     * 
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     */
    public void setTotal(int value) {
        this.total = value;
    }

    /**
     * Gets the value of the sequences property.
     * 
     */
    public long getSequences() {
        return sequences;
    }

    /**
     * Sets the value of the sequences property.
     * 
     */
    public void setSequences(long value) {
        this.sequences = value;
    }

    /**
     * Gets the value of the letters property.
     * 
     */
    public long getLetters() {
        return letters;
    }

    /**
     * Sets the value of the letters property.
     * 
     */
    public void setLetters(long value) {
        this.letters = value;
    }

}
