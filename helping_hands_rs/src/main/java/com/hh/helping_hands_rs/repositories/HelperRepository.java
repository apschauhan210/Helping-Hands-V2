package com.hh.helping_hands_rs.repositories;

import com.hh.helping_hands_rs.entities.Helper;
import com.hh.helping_hands_rs.entities.Job;
import com.hh.helping_hands_rs.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HelperRepository extends JpaRepository<Helper, Long> {

    Optional<Helper> findHelperByEmail(String email);

    @Query("SELECT h FROM Helper h JOIN h.addressToWork a WHERE a = ?1")
    Optional<List<Helper>> findHelpersByAddressToWork(Address address);

    @Query("SELECT h FROM Helper h JOIN h.addressToWork a WHERE (a.city, a.state, a.country, a.zipCode) = (:#{#address.city}, :#{#address.state}, :#{#address.country}, :#{#address.zipCode})")
    Optional<List<Helper>> findHelpersByAddressToWorkExcludingLocation(@Param("address") Address address);

    @Query("SELECT h FROM Helper h JOIN h.addressToWork a WHERE (a.city, a.state, a.country) = (:#{#address.city}, :#{#address.state}, :#{#address.country})")
    Optional<List<Helper>> findHelpersByAddressToWorkWithCityAndState(@Param("address") Address address);

    @Query("SELECT h FROM Helper h JOIN h.addressToWork a ON a = ?1 JOIN h.jobs j ON j = ?2")
    Optional<List<Helper>> findHelpersByAddressToWorkAndJob(Address address, @Param("jobs") Job job);

}
