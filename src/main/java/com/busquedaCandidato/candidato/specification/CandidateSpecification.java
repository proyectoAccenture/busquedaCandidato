package com.busquedaCandidato.candidato.specification;



import com.busquedaCandidato.candidato.entity.CandidateEntity;
import com.busquedaCandidato.candidato.entity.PostulationEntity;
import com.busquedaCandidato.candidato.entity.RoleIDEntity;
import com.busquedaCandidato.candidato.entity.ProcessEntity;
import com.busquedaCandidato.candidato.entity.CandidatePhasesEntity;
import com.busquedaCandidato.candidato.entity.PhaseEntity;
import com.busquedaCandidato.candidato.entity.StateEntity;
import com.busquedaCandidato.candidato.entity.VacancyCompanyEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CandidateSpecification {
    public static Specification<CandidateEntity> filterBySingleField(String searchValue) {
        return (Root<CandidateEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Join<CandidateEntity, PostulationEntity> postulationJoin = root.join("postulations", JoinType.LEFT);
            Join<PostulationEntity, VacancyCompanyEntity> vacancyJoin = postulationJoin.join("vacancyCompany", JoinType.LEFT);
            Join<VacancyCompanyEntity, RoleIDEntity> roleJoin = vacancyJoin.join("role", JoinType.LEFT);
            Join<PostulationEntity, ProcessEntity> processJoin = postulationJoin.join("process", JoinType.LEFT);
            Join<ProcessEntity, CandidatePhasesEntity> phaseJoin = processJoin.join("processPhases", JoinType.LEFT);
            Join<CandidatePhasesEntity, PhaseEntity> phaseTypeJoin = phaseJoin.join("phase", JoinType.LEFT);
            Join<CandidatePhasesEntity, StateEntity> stateJoin = phaseJoin.join("state", JoinType.LEFT);

            Predicate predicate = cb.conjunction();

            if (StringUtils.hasText(searchValue)) {
                String searchPattern = "%%" + searchValue.toLowerCase() + "%%";

                // Si el valor es alfanumérico, buscar en el Role ID
                Predicate rolePredicate = cb.like(cb.lower(roleJoin.get("name")), searchPattern);

                // Si el valor contiene un espacio, asumir que es nombre y apellido
                if (searchValue.contains(" ")) {
                    String[] parts = searchValue.split(" ", 2);
                    String firstName = "%%" + parts[0].toLowerCase() + "%%";
                    String lastName = "%%" + parts[1].toLowerCase() + "%%";
                    predicate = cb.and(
                            cb.like(cb.lower(root.get("name")), firstName),
                            cb.like(cb.lower(root.get("lastName")), lastName)
                    );
                } else {
                    // Si el valor es solo texto/números, buscar en nombre o apellido
                    Predicate namePredicate = cb.or(
                            cb.like(cb.lower(root.get("name")), searchPattern),
                            cb.like(cb.lower(root.get("lastName")), searchPattern)
                    );
                    predicate = cb.or(rolePredicate, namePredicate);
                }
            }

            query.distinct(true);
            return predicate;
        };
    }
}
