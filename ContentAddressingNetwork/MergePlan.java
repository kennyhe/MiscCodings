import java.util.ArrayList;

/**
 * This class is used to save the Merge Plan of the **Splitables** class.
 * When try to merge some Splitables, the system need to run greedy algorithm to make out several merge
 * plans and choose the optimized one. However, the Splitable will calculate a weight of each plan according
 * to the rule and finally choose the plans which weighs highest as the final plan. 
 * @author kenny
 *
 */
public class MergePlan {
    ArrayList<Splitable>     l;
    Splitable                t;
    double                     weight;
}
