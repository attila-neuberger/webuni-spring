package hu.webuni.hr.comtur.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.webuni.hr.comtur.dto.IDtoKey;

public class BaseService<T extends IDtoKey> {

	/**
	 * Map of IDs - entities.
	 */
	protected Map<Long, T> entities;
	
	{
		entities = new HashMap<>();
	}
	
	public T save(T t) {
		entities.put(t.getId(), t); // Unique check does not happen here (can be replaced during modify).
		return t;
	}
	
	public List<T> findAll() {
		return new ArrayList<>(entities.values());
	}
	
	public T findById(long id) {
		return entities.get(id);
	}
	
	public void delete(long id) {
		entities.remove(id);
	}

	/**
	 * Determines entity is present with the parametric ID.
	 * @param id ID to search for.
	 * @return Entity with the ID exists.
	 */
	public boolean containsKey(long id) {
		return entities.containsKey(id);
	}
}
