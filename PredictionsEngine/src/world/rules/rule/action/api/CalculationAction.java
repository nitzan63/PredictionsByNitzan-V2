package world.rules.rule.action.api;

import world.entities.EntitiesDefinition;

import java.util.Map;

public abstract class CalculationAction extends AbstractAction {
    protected final String resProp;
    protected final String args1;
    protected final String args2;

    public CalculationAction(Map<String,EntitiesDefinition> allEntitiesDefinition, String entityName, String resultProp, String s1, String s2){
        super(ActionType.CALCULATION , allEntitiesDefinition, entityName);
        this.resProp = resultProp;
        this.args1 = s1;
        this.args2 = s2;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getArgs1() {
        return args1;
    }

    public String getArgs2() {
        return args2;
    }

    public String getPropertyName() {
        return resProp;
    }

    public String getByExpression(){
        return null;
    }


    @Override
    public String toString() {
        return "CalculationAction{" +
                "entityName='" + entityName + '\'' +
                ", resProp='" + resProp + '\'' +
                ", args1='" + args1 + '\'' +
                ", args2='" + args2 + '\'' +
                '}';
    }
}
