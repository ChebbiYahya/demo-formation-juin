package com.theBridge.demoFormationJuin.repository;

import com.theBridge.demoFormationJuin.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRespository extends JpaRepository<UserEntity,Long> {

    // NAMED METHOD
    // Spring Data JPA lit le nom de la methode et genere la requete automatiquement.
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByEmailAndAddressStartingWith(String email, String address);

    UserEntity findByEmail(String email);


    // JPQL METHOD
    // JPQL utilise les noms des classes Java et des attributs Java, pas les noms SQL.
    @Query("SELECT u from UserEntity u WHERE u.username=?1")
    UserEntity findUserByUsernameJPQL(String username);

    @Query("SELECT CASE WHEN COUNT(u)>0 THEN true ELSE false end from UserEntity u WHERE u.username=:username")
    boolean existsByUsernameJPQL(@Param("username") String username);



    // SQL METHOD
    // nativeQuery = true indique que la requete utilise les noms reels de la table SQL.
    @Query(value = "SELECT * FROM users where username=?1", nativeQuery = true)
    UserEntity findByUsernameSQL(String username);

    /*
     * En SQL natif, COUNT(*) retourne un nombre.
     * Pour eviter une erreur de conversion nombre -> boolean, on retourne 0 ou 1,
     * puis le service transforme ce int en boolean.
     */
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM users WHERE username=:u", nativeQuery = true)
    int existsByUsernameSQL(@Param("u") String username);

    @Query(value = "select * from users where username like :cle", nativeQuery = true)
    List<UserEntity> findByCle(@Param("cle") String cle);

    /*
     * Avec une requete native, il vaut mieux utiliser CONCAT pour ajouter les %
     * autour du parametre au lieu d'ecrire directement %:domain%.
     */
    @Query(value = "select * from users where email like CONCAT('%', :domain, '%')", nativeQuery = true)
    List<UserEntity> findByDomain(@Param("domain") String un);

}
