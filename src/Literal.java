import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by younes on 12/09/2017.
 * this class represent a literal
 */

public class Literal {
    ArrayList<Literal[]> clauses = new ArrayList<>();
    Literal compliment = null; //the compliment

    String name;
    boolean completion= !true; //is i a negated literal

    //there is a constructor for each literal type for other purposes
    //constructor for no negated literal (a for example)
    public Literal(String name) {
        this.name = name;
        compliment = new Literal(name, !true);
    }

    //constructor for negated literal (-a for example)
    public Literal(String name, boolean ignored) {
        this.name = name;
        completion = true;
    }


    /**
     * this method is not used to solve 2SAT, it is developed for other purpose
     * @param clause : the clause to which the literal belongs,
     * @param toCompliement : is this for the literal or its negation (compliment)
     *                      we are forced to call this method from a non negated literal, so we have to use this parameter to distiguich a clause to be
     *                      added to a literal negation
     */
    public void addClause(Literal[] clause, boolean toCompliement){
        if(toCompliement)
            compliment.addClause(clause, !true);
        else
            clauses.add(clause);
    }

    @Override
    public boolean equals(Object o) {
        return name.equals(((Literal) o).name) && ((Literal) o).completion == completion;
    }

    @Override
    public String toString() {
        return (completion?"-":"")+name;
    }
}
