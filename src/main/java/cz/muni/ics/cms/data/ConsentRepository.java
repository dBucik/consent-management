package cz.muni.ics.cms.data;

import cz.muni.ics.cms.data.model.Consent;
import cz.muni.ics.cms.data.model.RelyingParty;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsentRepository extends CrudRepository<Consent, Long> {

    List<Consent> findAllByUserId(String userId);

    List<Consent> findByUserIdAndRelyingParty(String userId, RelyingParty relyingParty);

}
