package model.entity;

import io.univ.rouen.m2gil.smartclass.core.data.Data;
import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGenerator;

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
