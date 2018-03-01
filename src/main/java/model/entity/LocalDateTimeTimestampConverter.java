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

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The class convert a LocalDateTime object
 * into a Timestamp object to
 * store date into a timestamp format
 * on the column.
 *
 * @since SC-Alert Lib 1.0
 * @version 1.0
 */
public class LocalDateTimeTimestampConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    
    /**
     * This method convert the attribute from the entity
     * into the timestamp store on database.
     *
     * @param attribute
     *  The attribute from the entity.
     * @return
     *  The conversion of the attribute on timestamp.
     */
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute != null ? Timestamp.valueOf(attribute) : null;
    }
    
    /**
     * This method convert the attribute from the database
     * into the LocalDateTime on the entity.
     *
     * @param dbData
     *  The column from the database.
     * @return
     *  The conversion of the column on date.
     */
    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData != null ? dbData.toLocalDateTime() : null;
    }
}
