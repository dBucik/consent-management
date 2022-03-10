package cz.muni.ics.cms.data;

import cz.muni.ics.cms.data.model.RelyingParty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelyingPartyRepository extends CrudRepository<RelyingParty, String> {

    RelyingParty getByIdentifier(String id);

}
