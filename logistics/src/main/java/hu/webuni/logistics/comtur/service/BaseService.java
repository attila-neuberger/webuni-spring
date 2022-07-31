package hu.webuni.logistics.comtur.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.logistics.comtur.dto.IDtoKey;

/**
 * Generic superclass for services.
 * 
 * @author comtur
 *
 * @param <T> Template of the service.
 */
public class BaseService<T extends IDtoKey> {

	@Autowired
	protected JpaRepository<T, Long> repository;

	/**
	 * Saves an entity (creates / modifies).
	 * 
	 * @param t Entity object.
	 * @return Saved entity.
	 */
	@Transactional
	public T save(T t) {
		return repository.save(t);
	}

	/**
	 * Modifies an entity.
	 * 
	 * @param t Entity object.
	 * @return Saved entity.
	 */
	@Transactional
	public T update(T t) {
		if (repository.existsById(t.getId())) {
			return repository.save(t);
		} else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Gets list of all entities.
	 * 
	 * @return List of entities.
	 */
	public List<T> findAll() {
		return repository.findAll();
	}

	/**
	 * Gets one entity (optional) based on its ID.
	 * 
	 * @param id ID of the entity.
	 * @return Found entity as {@link Optional}.
	 */
	public Optional<T> findById(long id) {
		return repository.findById(id);
	}

	/**
	 * Removes an entity.
	 * 
	 * @param id ID of the entity.
	 */
	@Transactional
	public void delete(long id) {
		repository.deleteById(id);
	}

	/**
	 * Determines entity is present with the parametric ID.
	 * 
	 * @param id ID to search for.
	 * @return Entity with the ID exists.
	 */
	public boolean exists(long id) {
		return repository.existsById(id);
	}

	public JpaRepository<T, Long> getRepository() {
		return repository;
	}
}
