package hu.webuni.hr.comtur.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import hu.webuni.hr.comtur.dto.IDtoKey;

public class HrBaseRestController<T extends IDtoKey> {

	protected Map<Long, T> entities;
	
	{
		entities = new HashMap<>();
	}
	
	@GetMapping
	public Collection<T> getAll() {
		return entities.values();
	}

	@GetMapping("/{id}")
	public ResponseEntity<T> getById(@PathVariable long id) {
		T entity = entities.get(id);
		if (entity == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(entity);
	}
	
	@PostMapping
	public ResponseEntity<T> create(@RequestBody T entity) {
		if (entities.containsKey(entity.getId())) {
			return ResponseEntity.unprocessableEntity().build();
		}
		entities.put(entity.getId(), entity);
		return ResponseEntity.ok(entity);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<T> modify(@PathVariable long id, @RequestBody T entity) {
		if (!entities.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}
		entity.setId(id);
		entities.put(id, entity);
		return ResponseEntity.ok(entity);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<T> delete(@PathVariable long id) {
		if (entities.containsKey(id)) {
			entities.remove(id);
			return ResponseEntity.accepted().build();
		}
		return ResponseEntity.notFound().build();
	}
}
