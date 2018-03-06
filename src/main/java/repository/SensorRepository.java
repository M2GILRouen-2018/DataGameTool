package repository;

import io.univ.rouen.m2gil.smartclass.core.datagenerator.DataGeneratorRepository;
import model.entity.Sensor;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends DataGeneratorRepository<Sensor> {

}
