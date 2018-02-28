package repository;

import io.univ.rouen.m2gil.smartclass.core.data.DataRepository;
import model.entity.DataModel;
import org.springframework.stereotype.Repository;

@Repository
public interface DataModelRepository extends DataRepository<DataModel> {
}
