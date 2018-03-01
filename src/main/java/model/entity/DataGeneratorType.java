/*
 * SMART-CLASS
 * Copyright (c) 2018 Université de Rouen - M2GIL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * The entity which represent a
 * type of generator in Smart-Class.
 *
 * @since SC-Data-Generator Lib 1.0
 * @version 1.0
 */
@Entity
public class DataGeneratorType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /*
     * Indicate the type of the generator.
     */
    @NotNull
    private String type;

    /*
     * The unit measure of the generator.
     */
    @Column(name = "unit_measure")
    @Size(min = 1)
    private String unitMeasure;

    /*
     * The minimum value authorize
     * for the generator type.
     */
    @Column(name = "max")
    @NotNull
    private double maxValue;

    /*
     * The maximum value authorize
     * for the generator type.
     */
    @Column(name = "min")
    @NotNull
    private double minValue;
    
    /*
     * The generator associated to the
     * generator type object.
     */
    @OneToMany
    @JoinColumn
    private List<DataGenerator> dataGenerator;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getUnitMeasure() {
        return unitMeasure;
    }
    
    public void setUnitMeasure(String unitMeaseure) {
        this.unitMeasure = unitMeaseure;
    }
    
    public double getMaxValue() {
        return maxValue;
    }
    
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    
    public double getMinValue() {
        return minValue;
    }
    
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
    
    public List<DataGenerator> getDataGenerator() {
      return dataGenerator;
    }
    
    public void setDataGenerator(List<DataGenerator> dataGenerator) {
      this.dataGenerator = dataGenerator;
    }
}
