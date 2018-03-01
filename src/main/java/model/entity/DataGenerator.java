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

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * The entity which represent a generator in Smart-Class.
 *
 * @since SC-Data-Generator Lib 1.0
 * @version 1.0
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class DataGenerator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /*
     * Instance of DataGeneratorType
     * which represent the type of
     * data send by the generator.
     */
    @NotNull
    @ManyToOne
    private DataGeneratorType type;
    
    /*
     * Indicate if the data generator is enable
     * or disable.
     */
    @NotNull
    private boolean enable;

    /*
     * Indicate if the generator produce
     * real or virtual data.
     */
    @NotNull
    private boolean producingVirtual;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public DataGeneratorType getType() {
        return type;
    }
    
    public void setType(DataGeneratorType type) {
        this.type = type;
    }
    
    public boolean isEnable() {
        return enable;
    }
    
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isProducingVirtual() {
        return producingVirtual;
    }

    public void setProducingVirtual(boolean producingVirtual) {
        this.producingVirtual = producingVirtual;
    }
}
