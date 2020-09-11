package factory;

import java.util.HashMap;
import java.util.Map;

interface Aircraft {
    Map<String, Boolean> PartsNeeded();
    void PartAcquisition(String part);
    Long IdRequest();

}

// TODO add id to Aircraft
class Boeing2461 implements Aircraft{
    private Long id;
    private  Map<String, Boolean> parts = new HashMap<>();
    public Boeing2461(Long id){
        parts.put("b2461-wings", Boolean.FALSE);
        parts.put("fuselage", Boolean.FALSE);
        parts.put("245-stabilizer", Boolean.FALSE);
        parts.put("common-rudder", Boolean.FALSE);
        parts.put("b2461-engine", Boolean.FALSE);
        parts.put("200x217-spoiler", Boolean.FALSE);
        this.id = id;
    }

    @Override
    public Map<String, Boolean> PartsNeeded() {
        return parts;
    }

    @Override
    public void PartAcquisition(String part) {
        parts.replace(part, Boolean.TRUE);
    }

    @Override
    public Long IdRequest() {
        return this.id;
    }
}

class Boeing777 implements Aircraft{ 
    private Long id;
    private  Map<String, Boolean> parts = new HashMap<>();
    public Boeing777(Long id){
        parts.put("b777-wings", Boolean.FALSE);
        parts.put("fuselage", Boolean.FALSE);
        parts.put("245-stabilizer", Boolean.FALSE);
        parts.put("common-rudder", Boolean.FALSE);
        parts.put("b777-engine", Boolean.FALSE);
        parts.put("256x217-spoiler", Boolean.FALSE);
        this.id = id;

    }

    @Override
    public Map<String, Boolean> PartsNeeded() {
        return parts;
    }

    @Override
    public void PartAcquisition(String part) {
        parts.replace(part, Boolean.TRUE);
    }

    @Override
    public Long IdRequest() {
        return this.id;
    }
}

class Airbus implements Aircraft{
    private Long id;
    private  Map<String, Boolean> parts = new HashMap<>();
    public Airbus(Long id){
        parts.put("bus-wings", Boolean.FALSE);
        parts.put("fuselage", Boolean.FALSE);
        parts.put("bus-stabilizer", Boolean.FALSE);
        parts.put("max-rudder", Boolean.FALSE);
        parts.put("ab-engine", Boolean.FALSE);
        parts.put("256x217-spoiler", Boolean.FALSE);
        this.id = id;

    }

    @Override
    public Map<String, Boolean> PartsNeeded() {
        return parts;
    }

    @Override
    public void PartAcquisition(String part) {
        parts.replace(part, Boolean.TRUE);
    }

    @Override
    public Long IdRequest() {
        return this.id;
    }
}
