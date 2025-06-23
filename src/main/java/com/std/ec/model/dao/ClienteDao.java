package com.std.ec.model.dao;

import com.std.ec.model.entity.Cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClienteDao extends JpaRepository<Cliente, Long> {


}
