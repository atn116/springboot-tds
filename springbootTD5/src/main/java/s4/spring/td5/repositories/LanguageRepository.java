package s4.spring.td5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import s4.spring.td5.entities.Language;

public interface LanguageRepository extends JpaRepository<Language,Integer> {

}
