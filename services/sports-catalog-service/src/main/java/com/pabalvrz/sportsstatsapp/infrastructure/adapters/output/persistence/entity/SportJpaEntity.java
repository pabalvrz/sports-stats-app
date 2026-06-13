package com.pabalvrz.sportsstatsapp.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "sports")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SportJpaEntity {

	@Id
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(nullable = false, length = 120)
	private String name;

	@Column(nullable = false)
	private boolean active;

	public SportJpaEntity(UUID id, String name, boolean active) {
		this.id = id;
		this.name = name;
		this.active = active;
	}
}