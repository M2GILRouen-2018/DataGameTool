package model.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class DataModel extends Data {
    // ATTRIBUTE
    @ManyToOne
    private DataGenerator sourceData;

    // ACCESSORS
    public DataGenerator getSourceData() {
        return sourceData;
    }

    public void setSourceData(DataGenerator sourceData) {
        this.sourceData = sourceData;
    }
}
