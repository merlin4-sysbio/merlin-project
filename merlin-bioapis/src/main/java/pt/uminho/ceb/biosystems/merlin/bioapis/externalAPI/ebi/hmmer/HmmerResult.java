/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.hmmer;

/**
 *
 * @author fxe
 */
public class HmmerResult {
	
    private String species;
    private String desc;
    private String gi;
    private String acc;
    private String taxid;
    private String kg;
    private double eval;
    private double pval;
    private double score;
    private int taxaRange;
    private String ecn;
    
    /**
     * @param species
     * @param desc
     * @param gi
     * @param acc
     * @param taxid
     * @param kg
     * @param eval
     * @param pval
     * @param score
     */
    public HmmerResult(String species, String desc, String gi, String acc, String taxid, String kg, double eval, double pval, double score) {
        this.species = species;
        this.desc = desc;
        this.eval = eval;
        this.gi = gi;
        this.acc = acc;
        this.taxid = taxid;
        this.kg = kg;
        this.pval = pval;
        this.score = score;
    }
    
    /**
     * @param species
     * @param desc
     * @param gi
     * @param acc
     * @param taxid
     * @param kg
     * @param eval
     * @param pval
     * @param score
     * @param taxaRange
     * @param ecn
     */
    public HmmerResult(String species, String desc, String gi, String acc, String taxid, String kg, double eval, double pval, double score, int taxaRange, String ecn) {
        this.species = species;
        this.desc = desc;
        this.eval = eval;
        this.gi = gi;
        this.acc = acc;
        this.taxid = taxid;
        this.kg = kg;
        this.pval = pval;
        this.score = score;
        this.taxaRange = taxaRange;
        this.ecn = ecn;
    }
    
    public String getECNumber() {
        return this.ecn;
    }
    
    public int getTaxaRange() {
        return this.taxaRange;
    }
    
    public String getSpecies() {
        return this.species;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public String getGI() {
        return this.gi;
    }
    
    public String getAcc() {
        return this.acc;
    }
    
    public String getTaxId() {
        return this.taxid;
    }
    
    public String getKingdom() {
        return this.kg;
    }
    
    public double getEval() {
        return this.eval;
    }
    
    public double getPval() {
        return this.pval;
    }
    
    public double getScore() {
        return this.score;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.acc).append("\t");
        sb.append(this.gi).append("\t");
        sb.append(this.getEval()).append("\t");
        sb.append(this.getScore()).append("\n");
        return sb.toString();
    }
}
