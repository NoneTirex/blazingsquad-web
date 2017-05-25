package pl.edu.tirex.blazingsquad.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.tirex.blazingsquad.web.groups.Group;

public interface GroupRepository extends JpaRepository<Group, Long>
{
}
