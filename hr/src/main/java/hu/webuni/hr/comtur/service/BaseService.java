package hu.webuni.hr.comtur.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.comtur.dto.IDtoKey;

public class BaseService<T extends IDtoKey> {
	
	@Autowired
	protected JpaRepository<T, Long> repository;

	@Transactional
	public T save(T t) {
		return repository.save(t);
	}
	
	@Transactional
	public T update(T t) {
		if (repository.existsById(t.getId())) {
			return repository.save(t);
		} else {
			throw new NoSuchElementException();
		}
	}
	
	public List<T> findAll() {
		return repository.findAll();
	}
	
	public Optional<T> findById(long id) {
		return repository.findById(id);
	}
	
	@Transactional
	public void delete(long id) {
		repository.deleteById(id);
	}

	/**
	 * Determines entity is present with the parametric ID.
	 * @param id ID to search for.
	 * @return Entity with the ID exists.
	 */
	public boolean exists(long id) {
		return repository.existsById(id);
	}
}
